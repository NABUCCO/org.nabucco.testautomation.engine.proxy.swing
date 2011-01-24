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
 * This {@link ProcessCommand} initializes the external process by invoking the given class'es main
 * method.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class InitCommand extends ProcessCommandSupport implements ProcessCommand {

    private static final long serialVersionUID = 1L;

    private String className;
    
    private String[] args;

    /**
     * Creates a {@link InitCommand} instance.
     */
    public InitCommand() {
        setType(ProcessCommandType.INIT);
    }

    /**
     * Getter for the main class name.
     * 
     * @return the main class name to invoke.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Setter for the main class name.
     * 
     * @param className
     *            the main class name to set
     */
    public void setClassName(String className) {
        this.className = className;
    }

	public void setArguments(String[] args) {
		this.args = args;
	}

	public String[] getArguments() {
		return args;
	}

}
