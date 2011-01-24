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
package org.nabucco.testautomation.engine.proxy.swing.ui;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingDialog;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;
import org.nabucco.testautomation.engine.proxy.swing.ui.validator.SwingComponentConstraints;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;

/**
 * SwingDialogImpl
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingDialogImpl extends AbstractSwingComponent implements SwingDialog {

    private static final long serialVersionUID = 8069406842479920767L;

    public SwingDialogImpl(ProcessCommunication communication) {
        super(communication, SwingEngineOperationType.SWING_DIALOG);
    }

    @Override
    protected void defineConstraints(SwingComponentConstraints constraints,
            SwingActionType actionType) {

    	constraints.actions(SwingActionType.CLOSE, SwingActionType.READ, SwingActionType.IS_AVAILABLE);

    	switch (actionType) {
    	case IS_AVAILABLE:
    		constraints.properties(PropertyType.BOOLEAN);
    		break;
    	case READ:
    		constraints.properties(PropertyType.STRING);
    		break;
    	}
    }

    @Override
    public void storeReadProperty(PropertyList propertyList, Property readProperty) {

    	Property property = propertyList.getPropertyList().get(0).getProperty();
    	
    	if (property.getType() == PropertyType.BOOLEAN) {
    		((BooleanProperty) property).setValue(((BooleanProperty) readProperty).getValue());
    	}
    	if (property.getType() == PropertyType.STRING) {
    		((StringProperty) property).setValue(((StringProperty) readProperty).getValue());
    	}
    }

}