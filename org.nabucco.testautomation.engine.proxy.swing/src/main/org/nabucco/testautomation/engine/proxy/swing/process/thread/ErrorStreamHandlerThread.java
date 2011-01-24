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
package org.nabucco.testautomation.engine.proxy.swing.process.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * Monitors the external process System.err stream for occurrences and logs them.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ErrorStreamHandlerThread extends Thread {

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            ErrorStreamHandlerThread.class);

    BufferedReader reader;

    /**
     * Creates a new instance.
     * 
     * @param in
     *            the input stream to monitor
     */
    public ErrorStreamHandlerThread(InputStream in) {
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void run() {

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.warning(line);
            }
        } catch (Exception e) {
            logger.fatal(e, "Exception reading error stream.");
        }

    }
}
