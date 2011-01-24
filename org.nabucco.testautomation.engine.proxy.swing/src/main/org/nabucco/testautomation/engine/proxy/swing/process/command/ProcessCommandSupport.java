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
 * ProcessCommanfSupport
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ProcessCommandSupport implements ProcessCommand {

    private static final long serialVersionUID = 1L;

    private ProcessCommandType type;

    /**
     * Creates {@link ProcessCommandType} instance.
     */
    public ProcessCommandType getType() {
        return type;
    }

    /**
     * Setter for the {@link ProcessCommandType} type.
     * 
     * @param type
     *            the type to set.
     */
    public void setType(ProcessCommandType type) {
        this.type = type;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
    	return "Command " + getType();
    }

}
