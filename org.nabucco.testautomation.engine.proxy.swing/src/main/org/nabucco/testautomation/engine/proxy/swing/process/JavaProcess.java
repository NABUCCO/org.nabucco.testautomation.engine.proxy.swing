/*
* Copyright 2010 PRODYNA AG
*
* Licensed under the Eclipse Public License (EPL), Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.opensource.org/licenses/eclipse-1.0.php or
* http://www.nabucco-source.org/nabucco-license.html
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package org.nabucco.testautomation.engine.proxy.swing.process;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * JavaProcess
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class JavaProcess {

    private static final String TARGET_CLASS = "org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessCommunicationClient";

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            JavaProcess.class);

    private Integer socketPort;

    private final boolean debugEnabled;

    private Integer debugPort;

    /**
     * Creates a new JavaProcess instance without debugging.
     * 
     * @param socketPort
     *            the port of the remote application.
     */
    public JavaProcess(Integer socketPort) {
        this(socketPort, null);
    }

    /**
     * Creates a new JavaProcess instance with debugging.
     * 
     * @param socketPort
     *            the port of the remote application.
     * @param debugPort
     *            the debug port.
     */
    public JavaProcess(Integer socketPort, Integer debugPort) {

        initSocketPort(socketPort);

        if (debugPort == null) {
            this.debugEnabled = false;
            logger.debug("Debug disabled.");
        } else {
            this.debugEnabled = true;
            logger.debug("Debug enabled.");
            this.debugPort = debugPort;
        }
    }

    private void initSocketPort(Integer socketPort) {
        if (socketPort != null) {
            this.socketPort = socketPort;
            logger.info("External java process port set to: " + socketPort);
        } else {
            this.socketPort = ProcessCommunicationConstants.DEFAULT_PROCESS_PORT;
            logger.warning("External java process port is not valid, set to default: "
                    + ProcessCommunicationConstants.DEFAULT_PROCESS_PORT);
        }
    }

    /**
     * Starts the JavaProcess with the given classpath URLs.
     * 
     * @param urls
     *            the URLs used as classpath
     * @return the open communication
     * 
     * @throws IOException
     */
    public ProcessCommunication startJavaProcess(String classpath) throws IOException {

        logger.info("Starting external java process with classpath='" + classpath + "'");

        String javaHome = System.getProperty("java.home");
        String fileSeparator = System.getProperty("file.separator");

        StringBuilder command = new StringBuilder();
        command.append(javaHome);
		command.append(fileSeparator);
		command.append("bin");
		command.append(fileSeparator);
        command.append("java -cp ");
        command.append(classpath);

        if (debugEnabled) {
            command.append(" -Xdebug -Xrunjdwp:transport=dt_socket,address=");
            command.append(debugPort);
            command.append(",server=y,suspend=n ");
        }
        
        command.append(" ");
        command.append(TARGET_CLASS);
        command.append(" ");
        command.append(socketPort);

        Process process = Runtime.getRuntime().exec(command.toString());

        return new ProcessCommunication(process, socketPort, "localhost");
    }

    public String prepareClasspath(List<URL> urls) {

        if (urls == null) {
            logger.warning("No classpath URLs defined.");
            return "";
        }

        StringBuilder sb = new StringBuilder();

        int size = urls.size();
        for (int i = 0; i < size; i++) {

            String fileURL = urls.get(i).getFile().substring(1);

            if (fileURL.contains("..")) {
                fileURL = resolvePath(fileURL);
            }

            sb.append(fileURL);
            sb.append(";");
        }
        return sb.toString();
    }

    private String resolvePath(String fileURL) {
        String[] tokens = fileURL.split("/");

        for (int j = 0; j < tokens.length; j++) {
            if (tokens[j].equals("..") && j > 0) {
                tokens[j - 1] = null;
                tokens[j] = null;
            }
        }

        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            if (token != null) {
                result.append(token);
                result.append("/");
            }
        }
        return result.toString();
    }

}
