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

import javax.swing.JToggleButton;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessToggleButton
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingProcessToggleButton extends SwingProcessComponentSupport implements
        SwingProcessComponent {

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
            this.failure("SwingToggleButton not found.");
            return;
        }
        
		if (!(component instanceof JToggleButton)) {
			this.failure("Component is not of type JToggleButton but " + component.getClass().getName());
			return;
		}

        JToggleButton button = (JToggleButton) component;

        switch (actionType) {

        case SELECT:
            executeSelect(button);
            break;

        case LEFTCLICK:
            executeLeftClick(button);
            break;

        case RIGHTCLICK:
            executeRightClick(button);
            break;

        case READ:
            readStatus(button, propertyList);
            break;
        }
    }

    private void executeSelect(JToggleButton toggleButton) {
        SwingComponentEventCreator.createComponentClickEvent(toggleButton);
    }

    private void executeLeftClick(JToggleButton toggleButton) {
        SwingComponentEventCreator.createComponentClickEvent(toggleButton);
    }

    private void executeRightClick(JToggleButton toggleButton) {
        SwingComponentEventCreator.createComponentRightClickEvent(toggleButton);
    }

    private void readStatus(JToggleButton button, PropertyList propertyList) {
        BooleanProperty property = (BooleanProperty) propertyList.getPropertyList().get(0).getProperty();
        Boolean status = SwingComponentEventCreator.readStatus(button);

        property.setValue(status);
        this.addProperty(property);
    }
    
    @Override
	boolean isAvailable(Component component) {
		return component instanceof JToggleButton && !((JToggleButton) component).isEnabled();
	}

}
