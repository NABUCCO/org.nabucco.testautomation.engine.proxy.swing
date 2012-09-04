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
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.engine.proxy.swing.SwingTable;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;
import org.nabucco.testautomation.engine.proxy.swing.ui.validator.SwingComponentConstraints;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;

/**
 * SwingTableImpl
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingTableImpl extends AbstractSwingComponent implements SwingTable {

	private static final long	serialVersionUID	= 7684946048405023898L;

	public SwingTableImpl(ProcessCommunication communication) {
		super(communication, SwingEngineOperationType.SWING_TABLE);
	}

	@Override
	protected void defineConstraints(SwingComponentConstraints constraints, SwingActionType actionType) {
		constraints.actions(SwingActionType.LEFTCLICK, SwingActionType.RIGHTCLICK, SwingActionType.READ, SwingActionType.FIND, SwingActionType.COUNT, SwingActionType.IS_AVAILABLE);
		switch (actionType) {
			case IS_AVAILABLE:
				break;
			case READ:
				constraints.properties(PropertyType.LIST);
				break;
			case FIND:
				constraints.properties(PropertyType.NUMERIC, PropertyType.TEXT);
				break;
			case COUNT:
				constraints.properties(PropertyType.NUMERIC);
				break;
			default:
				constraints.properties(PropertyType.NUMERIC, PropertyType.NUMERIC);
				break;
		}
	}
}
