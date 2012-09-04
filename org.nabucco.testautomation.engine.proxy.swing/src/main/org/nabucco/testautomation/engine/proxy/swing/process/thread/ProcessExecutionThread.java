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

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;


/**
 * Monitors the external process for execution and exit.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ProcessExecutionThread extends Thread {

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            ProcessExecutionThread.class);

    private Process process;

    /**
     * Creates a new instance.
     * 
     * @param process
     *            the process to monitor.
     */
    public ProcessExecutionThread(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        try {
            int exitCode = process.waitFor();
            logger.info("External java process has exited with return code: " + exitCode);
        } catch (Exception e) {
            logger.error(e, "Exception catched while waiting for external java process.");
        }
    }

}
