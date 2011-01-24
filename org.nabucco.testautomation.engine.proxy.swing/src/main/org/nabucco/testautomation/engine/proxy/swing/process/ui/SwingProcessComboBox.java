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
package org.nabucco.testautomation.engine.proxy.swing.process.ui;

import java.awt.Component;
import java.util.List;

import javax.swing.JComboBox;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessComboBox
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingProcessComboBox extends SwingProcessComponentSupport implements SwingProcessComponent {

    private static final long serialVersionUID = 1L;

    @Override
    public void internalExecute(PropertyList propertyList, List<Metadata> metadataList,
            SwingActionType actionType) {

    	if (actionType == SwingActionType.IS_AVAILABLE) {
    		BooleanProperty prop = (BooleanProperty) propertyList.getPropertyList().get(0).getProperty();
    		checkAvailability(metadataList, prop);
    		return;
    	}
    	
        Component component = SwingComponentFinder.getInstance().findComponent(metadataList);

        if (component == null) {
            this.failure("SwingComboBox not found.");
            return;
        }
        
		if (!(component instanceof JComboBox)) {
			this.failure("Component is not of type JComboBox but "	+ component.getClass().getName());
			return;
		}
        
        JComboBox comboBox = (JComboBox) component;
        Integer index;

        switch (actionType) {

        case SELECT:
			index = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
            executeComboBoxSelect(comboBox, index);
            break;

        case LEFTCLICK:
			index = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
            executeComboBoxClick(comboBox, index);
            break;
            
        case FIND:
        	StringProperty value = (StringProperty) propertyList.getPropertyList().get(0).getProperty();
            executeComboBoxSelect(comboBox, value.getValue().getValue());
            break;

        case READ:
            StringProperty property = (StringProperty) propertyList.getPropertyList().get(0).getProperty();
			readText(comboBox, property);
            break;
        }

    }
    
    private void executeComboBoxSelect(JComboBox comboBox, String value) {
        SwingComponentEventCreator.createComboBoxClickEvent(comboBox, value);
    }

    private void executeComboBoxSelect(JComboBox comboBox, int index) {
        SwingComponentEventCreator.createComboBoxClickEvent(comboBox, index);
    }

    private void executeComboBoxClick(JComboBox comboBox, int index) {
        SwingComponentEventCreator.createComboBoxClickEvent(comboBox, index);
    }

    private void readText(JComboBox comboBox, StringProperty property) {
        String text = SwingComponentEventCreator.readText(comboBox, comboBox.getSelectedIndex());
        property.setValue(text);
        this.addProperty(property);
    }
    
    @Override
	boolean isAvailable(Component component) {
		return component instanceof JComboBox && !((JComboBox) component).isEnabled();
	}

}
