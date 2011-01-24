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

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingMenu;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessMenu
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class SwingProcessMenu extends SwingProcessComponentSupport implements SwingProcessComponent {

	private static final long serialVersionUID = 1L;

	private static final String COMPONENT_INDEX = "COMPONENT_INDEX";

    @Override
    public void internalExecute(PropertyList propertyList, List<Metadata> metadataList,
            SwingActionType actionType) {

    	if (actionType == SwingActionType.IS_AVAILABLE) {
    		BooleanProperty prop = (BooleanProperty) propertyList.getPropertyList().get(0).getProperty();
    		checkAvailability(metadataList, prop);
    		return;
    	}
    	
    	Component component = null;
    	Metadata lastMetadata = null;
    	
    	while (component == null && !metadataList.isEmpty()) {
    		
			component = SwingComponentFinder.getInstance().findComponent(metadataList);
	
	    	if (component == null) {
	    		lastMetadata = metadataList.remove(metadataList.size() - 1);
	    	}
    	}
		
    	if (component == null) {
            this.failure("SwingMenu not found.");
            return;
        }
        
		if (!(component instanceof JMenu)) {
			this.failure("Component is not of type JMenu but "	+ component.getClass().getName());
			return;
		}
        
        JMenu menu = (JMenu) component;
        JMenuItem item = null;

		if (lastMetadata instanceof SwingMenu) {
			IntegerProperty indexProperty = (IntegerProperty) PropertyHelper
					.getFromList(lastMetadata.getPropertyList(), COMPONENT_INDEX);
			int index = indexProperty.getValue().getValue();
			item = menu.getItem(index);
		}

        switch (actionType) {

        case SELECT:
            executeMenuSelect(menu, item);
            break;

        case LEFTCLICK:
            executeMenuClick(menu, item);
            break;
        }
    }

    private void executeMenuSelect(JMenu menu, JMenuItem item) {
        SwingComponentEventCreator.createMenuClickEvent(menu, item);
    }

    private void executeMenuClick(JMenu menu, JMenuItem item) {
        SwingComponentEventCreator.createMenuClickEvent(menu, item);
    }
    
    @Override
	boolean isAvailable(Component component) {
		return component instanceof JMenu && !((JMenu) component).isEnabled();
	}

}
