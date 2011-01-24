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
package org.nabucco.testautomation.engine.proxy.swing.process.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Main class of the external process. Gets invoked with a port to start a socket server.
 * 
 * @see ClientCommunication
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ProcessCommunicationClient {

    /**
     * Starts the client process.
     * 
     * @param args
     *            the application port has to be submitted.
     */
    public static void main(String[] args) {

        if (args != null && args.length > 0) {

            ClientCommunication communication = null;
            try {
                communication = initCommunication(Integer.parseInt(args[0]));
            } catch (Exception e) {
                System.err.println("Error establishing connection at address: " + args[0]);
                System.err.println("Shutdown Process.");
                System.exit(-1);
            }

            int exitCode = communication.processCommands();

            try {
                communication.close();
                System.out.println("Process shutdown successful.");
                System.exit(exitCode);

            } catch (IOException e) {
                System.err.println("Error shutting down process.");
                System.exit(-1);
            }
        }
    }

    private static ClientCommunication initCommunication(Integer port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Socket Server started at address: " + port);

        Socket clientSocket = serverSocket.accept();
        return new ClientCommunication(clientSocket, serverSocket);
    }

}
