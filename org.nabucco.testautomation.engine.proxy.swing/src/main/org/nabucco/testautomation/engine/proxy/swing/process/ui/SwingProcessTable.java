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
import java.math.BigDecimal;

import javax.swing.JTable;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.MultipleEntriesFoundException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableMapper;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableMapperFactory;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.table.SwingTableType;
import org.nabucco.testautomation.property.facade.datatype.NumericProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessTable
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingProcessTable extends SwingProcessComponentSupport implements SwingProcessComponent {

	private static final long	serialVersionUID	= 1L;

	private static final String	TABLE_TYPE			= "TABLE_TYPE";

	@Override
	public void internalExecute(PropertyList propertyList, Metadata metadata, SwingActionType actionType) {
		if (actionType == SwingActionType.IS_AVAILABLE) {
			checkAvailability(propertyList, metadata);
			return;
		}
		Component component = null;
		try {
			component = find(propertyList, metadata);
		} catch (MultipleEntriesFoundException e) {
			this.failure("More than one Component found.");
		}
		if (component == null) {
			this.failure("SwingTable not found.");
			return;
		}
		if (!(component instanceof JTable)) {
			this.failure("Component is not of type JTable but " + component.getClass().getName());
			return;
		}
		JTable table = (JTable) component;
		SwingActionType type = actionType;
		switch (type) {
			case LEFTCLICK:
				executeLeftClick(table, propertyList);
				break;
			case RIGHTCLICK:
				executeRightClick(table, propertyList);
				break;
			case READ:
				readTable(table, metadata, propertyList);
				break;
			case FIND:
				executeFind(table, propertyList);
				break;
			case COUNT:
				executeCount(table, propertyList);
				break;
			case READPROPERTY:
				TextProperty returnValue = getProperty(component, ((TextProperty) PropertyHelper.getFromList(propertyList, NAME)).getValue().getValue());
				if (returnValue != null) {
					this.addProperty(returnValue);
				}
				break;
			case READALLPROPERTIES:
				PropertyList properties = getProperties(component);
				this.addProperty(properties);
				break;
		}
	}

	private void executeLeftClick(JTable table, PropertyList propertyList) {
		Integer row = ((NumericProperty) PropertyHelper.getFromList(propertyList, ROW)).getValue().getValue().intValue();
		Integer column = ((NumericProperty) PropertyHelper.getFromList(propertyList, COLUMN)).getValue().getValue().intValue();
		SwingComponentEventCreator.createTableClickEvent(table, row, column);
	}

	private void executeRightClick(JTable table, PropertyList propertyList) {
		Integer row = ((NumericProperty) PropertyHelper.getFromList(propertyList, ROW)).getValue().getValue().intValue();
		Integer column = ((NumericProperty) PropertyHelper.getFromList(propertyList, COLUMN)).getValue().getValue().intValue();
		SwingComponentEventCreator.createTableRightClickEvent(table, row, column);
	}

	private void executeCount(JTable table, PropertyList propertyList) {
		int rowCount = table.getModel().getRowCount();
		NumericProperty counter = (NumericProperty) PropertyHelper.createNumericProperty(COUNT, BigDecimal.valueOf(rowCount));
		addProperty(counter);
	}

	private void executeFind(JTable table, PropertyList propertyList) {
		String entry = ((TextProperty) PropertyHelper.getFromList(propertyList, VALUE)).getValue().getValue();
		Integer column = ((NumericProperty) PropertyHelper.getFromList(propertyList, COLUMN)).getValue().getValue().intValue();
		SwingComponentEventCreator.createTableClickEvent(table, entry, column);
	}

	private void readTable(JTable table, Metadata metadata, PropertyList propertyList) {
//		TableModel tableModel = table.getModel();
//		int rowCount = tableModel.getRowCount();
//		int columnCount = tableModel.getColumnCount();
//		Class<? extends Object> clazzt = tableModel.getClass();
//		Method[] methodz = clazzt.getMethods();
//		Method[] methods1;
//		try {
//			Method m = clazzt.getMethod("getTreeTableModel", new Class<?>[0]);
//			Object invoke = m.invoke(tableModel, null);
//			methods1 = invoke.getClass().getMethods();
//		} catch (Exception e) {}
//		for(int i = 0; i < rowCount; i++) {
//			for(int j = 0; j < columnCount; j++) {
//				Object object = tableModel.getValueAt(i, j);
//				if(object instanceof Boolean) {
//					if((Boolean)object == false) {
//						tableModel.setValueAt(Boolean.TRUE, i, j);
//					} else {
//						tableModel.setValueAt(Boolean.FALSE, i, j);
//					}
//				}
//				Class<? extends Object> clazz = object.getClass();
//				Method[] methods = clazz.getMethods();
//			}
//		}
		
		
		
		
		
		
		
		String property = ((TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), TABLE_TYPE)).getValue().getValue();
		SwingTableType type = property != null ? SwingTableType.valueOf(property) : null;
		SwingTableMapper mapper = SwingTableMapperFactory.getInstance().createTableMapper(table, type);
		PropertyList list = (PropertyList) PropertyHelper.createPropertyList(TABLE);
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
		return component instanceof JTable && ((JTable) component).isEnabled();
	}
}
