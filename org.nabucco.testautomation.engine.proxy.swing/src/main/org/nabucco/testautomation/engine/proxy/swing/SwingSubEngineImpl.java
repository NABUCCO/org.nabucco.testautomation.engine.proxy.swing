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
package org.nabucco.testautomation.engine.proxy.swing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.SubEngineOperationType;
import org.nabucco.testautomation.engine.proxy.base.AbstractSubEngine;
import org.nabucco.testautomation.engine.proxy.exception.SubEngineException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;
import org.nabucco.testautomation.settings.facade.datatype.engine.SubEngineType;

/**
 * SwingSubEngineImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class SwingSubEngineImpl extends AbstractSubEngine implements SwingEngine {

	private static final long	serialVersionUID	= 1L;

	private SwingApplet			swingApplet;

	private SwingApplication	swingApplication;

	public SwingSubEngineImpl(SwingApplication swingApplication, SwingApplet swingApplet) {
		super();
		this.swingApplication = swingApplication;
		this.swingApplet = swingApplet;
	}

	@Override
	public ActionResponse executeSubEngineOperation(SubEngineOperationType operationType, SubEngineActionType actionType, List<Metadata> metadataList, PropertyList propertyList, TestContext context) throws SubEngineException {
		// Map OperationType
		SwingEngineOperationType swingEngineOperationType = (SwingEngineOperationType) operationType;
		// execute operation
		switch (swingEngineOperationType) {
			case SWING_APPLET: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingApplet());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_APPLICATION: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingApplication());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_BUTTON: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingButton());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_COMBOBOX: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingComboBox());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_CHECKBOX: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingCheckBox());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_DIALOG: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingDialog());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_FRAME: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingFrame());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_MENU: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingMenu());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_PASSWORD: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingPassword());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_RADIOBUTTON: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingRadioButton());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_TAB: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingTab());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_TABLE: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingTable());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_TEXTINPUT: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingTextInput());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_TREE: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingTree());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_PRINT: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingPrint());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_COMPONENT: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingComponent());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			case SWING_LABEL: {
				SwingEngineOperation operation = this.validateOperation(this.getSwingLabel());
				return operation.execute(context, propertyList, metadataList, actionType);
			}
			default: {
				throw new SubEngineException("Unsupported operationtype='" + operationType + "'");
			}
		}
	}

	@Override
	public SwingApplet getSwingApplet() {
		return this.swingApplet;
	}

	@Override
	public SwingApplication getSwingApplication() {
		return this.swingApplication;
	}

	@Override
	public SwingButton getSwingButton() {
		if (this.swingApplication.getSwingButton() != null) {
			return this.swingApplication.getSwingButton();
		} else {
			return this.swingApplet.getSwingButton();
		}
	}

	@Override
	public SwingCheckbox getSwingCheckBox() {
		if (this.swingApplication.getSwingCheckBox() != null) {
			return this.swingApplication.getSwingCheckBox();
		} else {
			return this.swingApplet.getSwingCheckBox();
		}
	}

	@Override
	public SwingComboBox getSwingComboBox() {
		if (this.swingApplication.getSwingComboBox() != null) {
			return this.swingApplication.getSwingComboBox();
		} else {
			return this.swingApplet.getSwingComboBox();
		}
	}

	@Override
	public SwingDialog getSwingDialog() {
		if (this.swingApplication.getSwingDialog() != null) {
			return this.swingApplication.getSwingDialog();
		} else {
			return this.swingApplet.getSwingDialog();
		}
	}

	@Override
	public SwingFrame getSwingFrame() {
		if (this.swingApplication.getSwingFrame() != null) {
			return this.swingApplication.getSwingFrame();
		} else {
			return this.swingApplet.getSwingFrame();
		}
	}

	@Override
	public SwingMenu getSwingMenu() {
		if (this.swingApplication.getSwingMenu() != null) {
			return this.swingApplication.getSwingMenu();
		} else {
			return this.swingApplet.getSwingMenu();
		}
	}

	@Override
	public SwingPassword getSwingPassword() {
		if (this.swingApplication.getSwingPassword() != null) {
			return this.swingApplication.getSwingPassword();
		} else {
			return this.swingApplet.getSwingPassword();
		}
	}

	@Override
	public SwingRadioButton getSwingRadioButton() {
		if (this.swingApplication.getSwingRadioButton() != null) {
			return this.swingApplication.getSwingRadioButton();
		} else {
			return this.swingApplet.getSwingRadioButton();
		}
	}

	@Override
	public SwingTab getSwingTab() {
		if (this.swingApplication.getSwingTab() != null) {
			return this.swingApplication.getSwingTab();
		} else {
			return this.swingApplet.getSwingTab();
		}
	}

	@Override
	public SwingTable getSwingTable() {
		if (this.swingApplication.getSwingTable() != null) {
			return this.swingApplication.getSwingTable();
		} else {
			return this.swingApplet.getSwingTable();
		}
	}

	@Override
	public SwingTextInput getSwingTextInput() {
		if (this.swingApplication.getSwingTextInput() != null) {
			return this.swingApplication.getSwingTextInput();
		} else {
			return this.swingApplet.getSwingTextInput();
		}
	}

	@Override
	public SwingTree getSwingTree() {
		if (this.swingApplication.getSwingTree() != null) {
			return this.swingApplication.getSwingTree();
		} else {
			return this.swingApplet.getSwingTree();
		}
	}

	@Override
	public SwingPrint getSwingPrint() {
		if (this.swingApplication.getSwingPrint() != null) {
			return this.swingApplication.getSwingPrint();
		} else {
			return this.swingApplet.getSwingPrint();
		}
	}

	@Override
	public SwingJComponent getSwingComponent() {
		if (this.swingApplication.getSwingPrint() != null) {
			return this.swingApplication.getSwingComponent();
		} else {
			return this.swingApplet.getSwingComponent();
		}
	}
	
	@Override
	public SwingLabel getSwingLabel() {
		if (this.swingApplication.getSwingPrint() != null) {
			return this.swingApplication.getSwingLabel();
		} else {
			return this.swingApplet.getSwingLabel();
		}
	}

	@Override
	public Map<String, SubEngineActionType> getActions() {
		Map<String, SubEngineActionType> actions = new HashMap<String, SubEngineActionType>();
		for (SwingActionType action : SwingActionType.values()) {
			actions.put(action.toString(), action);
		}
		return actions;
	}

	@Override
	public Map<String, SubEngineOperationType> getOperations() {
		Map<String, SubEngineOperationType> operations = new HashMap<String, SubEngineOperationType>();
		for (SwingEngineOperationType operation : SwingEngineOperationType.values()) {
			operations.put(operation.toString(), operation);
		}
		return operations;
	}

	@Override
	public SubEngineType getType() {
		return SubEngineType.SWING;
	}

	private SwingEngineOperation validateOperation(SwingEngineOperation operation) {
		if (operation == null) {
			throw new IllegalArgumentException("SwingApplication not started correctly.");
		}
		return operation;
	}
}
