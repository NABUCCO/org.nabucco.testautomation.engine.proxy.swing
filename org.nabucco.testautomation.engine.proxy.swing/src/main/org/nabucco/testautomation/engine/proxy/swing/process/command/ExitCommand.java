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
package org.nabucco.testautomation.engine.proxy.swing.process.command;

/**
 * This {@link ProcessCommand} forces to exit the external process.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ExitCommand extends ProcessCommandSupport implements ProcessCommand {

    private static final long serialVersionUID = 1L;

    private int exitCode;

    /**
     * Creates an {@link ExitCommand} instance.
     */
    public ExitCommand() {
        setType(ProcessCommandType.EXIT);
    }

    /**
     * Getter for the exit code.
     * 
     * @return the exit code.
     */
    public int getExitCode() {
        return exitCode;
    }

    /**
     * Setter for the exit code.
     * 
     * @param exitCode
     *            the exitcode to set.
     */
    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

}
