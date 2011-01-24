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

import javax.swing.JTable;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableMapper;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableMapperFactory;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableType;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessTable
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingProcessTable extends SwingProcessComponentSupport implements SwingProcessComponent {

    private static final long serialVersionUID = 1L;

    private static final String TABLE_TYPE = "TABLE_TYPE";

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
            this.failure("SwingTable not found.");
            return;
        }
        
		if (!(component instanceof JTable)) {
			this.failure("Component is not of type JTable but "	+ component.getClass().getName());
			return;
		}

        JTable table = (JTable) component;

        SwingActionType type = actionType;

        switch (type) {

        case SELECT:
            executeSelect(table, propertyList);
            break;

        case LEFTCLICK:
            executeLeftClick(table, propertyList);
            break;

        case RIGHTCLICK:
            executeRightClick(table, propertyList);
            break;

        case READ:
            readTable(table, metadataList.get(metadataList.size() - 1), propertyList);
            break;
            
        case FIND:
        	executeFind(table, propertyList);
        	break;
        	
        case COUNT:
        	executeCount(table, propertyList);
        	break;
        }
    }

	private void executeSelect(JTable table, PropertyList propertyList) {
        Integer row = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
        Integer column = ((IntegerProperty) propertyList.getPropertyList().get(1).getProperty()).getValue().getValue();
        SwingComponentEventCreator.createTableClickEvent(table, row, column);
    }

    private void executeLeftClick(JTable table, PropertyList propertyList) {
        Integer row = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
        Integer column = ((IntegerProperty) propertyList.getPropertyList().get(1).getProperty()).getValue().getValue();
        SwingComponentEventCreator.createTableClickEvent(table, row, column);
    }

    private void executeRightClick(JTable table, PropertyList propertyList) {
        Integer row = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
        Integer column = ((IntegerProperty) propertyList.getPropertyList().get(1).getProperty()).getValue().getValue();
        SwingComponentEventCreator.createTableRightClickEvent(table, row, column);
    }
    
    private void executeCount(JTable table, PropertyList propertyList) {
        IntegerProperty counter = (IntegerProperty) propertyList.getPropertyList().get(0).getProperty();
        int rowCount = table.getModel().getRowCount();
        counter.setValue(rowCount);
        addProperty(counter);
    }
    
    private void executeFind(JTable table, PropertyList propertyList) {
        Integer column = ((IntegerProperty) propertyList.getPropertyList().get(0).getProperty()).getValue().getValue();
        String entry = ((StringProperty) propertyList.getPropertyList().get(1).getProperty()).getValue().getValue();
        SwingComponentEventCreator.createTableClickEvent(table, entry, column);
    }


    private void readTable(JTable table, Metadata metadata, PropertyList propertyList) {

        String property = ((StringProperty) PropertyHelper.getFromList(metadata.getPropertyList(), TABLE_TYPE)).getValue().getValue();
        SwingTableType type = property != null ? SwingTableType.valueOf(property) : null;
        SwingTableMapper mapper = SwingTableMapperFactory.getInstance().createTableMapper(table,
                type);

        PropertyList list = (PropertyList) propertyList.getPropertyList().get(1).getProperty();
        addProperty(list);

        try {
            mapper.createPropertyList(list);
        } catch (ProcessInvocationException e) {
            this.log("External method invocations invalid.", e, Level.FATAL, false);
            this.raiseException(e);
        }
    }
    
    @Override
	boolean isAvailable(Component component) {
		return component instanceof JTable && !((JTable) component).isEnabled();
	}
}
