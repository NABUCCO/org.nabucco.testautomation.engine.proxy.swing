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

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * This {@link ProcessCommand} executes a specific GUI command (e.g. a button press, text input) on
 * a Swing Component.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class ExecutionCommand extends ProcessCommandSupport implements ProcessCommand {

    private static final long serialVersionUID = 1L;

    private Metadata metadata;

    private PropertyList propertyList;

    private SwingEngineOperationType componentType;

    private SwingActionType actionType;

    /**
     * Creates a {@link ExitCommand} instance.
     */
    public ExecutionCommand() {
        setType(ProcessCommandType.EXEC);
    }

    /**
     * @return the componentType
     */
    public SwingEngineOperationType getComponentType() {
        return componentType;
    }

    /**
     * @param componentType
     *            the componentType to set
     */
    public void setComponentType(SwingEngineOperationType componentType) {
        this.componentType = componentType;
    }

    /**
     * @return the actionType
     */
    public SwingActionType getActionType() {
        return actionType;
    }

    /**
     * @param actionType
     *            the actionType to set
     */
    public void setActionType(SwingActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the metadataList
     */
    public Metadata getMetadata() {
        return metadata;
    }
    
    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the propertyList
     */
    public PropertyList getPropertyList() {
        if (propertyList == null) {
            propertyList = new PropertyList();
        }
        return propertyList;
    }
    
    /**
     * Sets the propertList
     * @param propertyList
     */
    public void setPropertyList(PropertyList propertyList) {
    	this.propertyList = propertyList;
    }

    @Override
    public String toString() {
		String actionTargetString = metadata == null ? "" : " on " + metadata.getId();
    	return super.toString() + " Action " + getActionType() + actionTargetString;
    }
}
