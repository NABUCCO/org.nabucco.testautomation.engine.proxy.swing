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

import java.io.Serializable;
import java.util.List;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.exception.SubEngineException;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingCheckbox;
import org.nabucco.testautomation.engine.proxy.swing.SwingComboBox;
import org.nabucco.testautomation.engine.proxy.swing.SwingDialog;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperation;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.engine.proxy.swing.SwingMenu;
import org.nabucco.testautomation.engine.proxy.swing.SwingPassword;
import org.nabucco.testautomation.engine.proxy.swing.SwingRadioButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingTab;
import org.nabucco.testautomation.engine.proxy.swing.SwingTable;
import org.nabucco.testautomation.engine.proxy.swing.SwingTextInput;
import org.nabucco.testautomation.engine.proxy.swing.SwingTree;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ExecutionCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommandType;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReplyEvaluator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.SwingProperties;
import org.nabucco.testautomation.engine.proxy.swing.ui.validator.SwingComponentConstraints;
import org.nabucco.testautomation.engine.proxy.swing.ui.validator.SwingValidationException;
import org.nabucco.testautomation.property.facade.datatype.NumericProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * Abstract super class of all swing components. This class realizes validation and command
 * execution for its subclasses.
 * 
 * @see SwingButton
 * @see SwingCheckbox
 * @see SwingComboBox
 * @see SwingDialog
 * @see SwingMenu
 * @see SwingPassword
 * @see SwingRadioButton
 * @see SwingTab
 * @see SwingTable
 * @see SwingTextInput
 * @see SwingTree
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
abstract class AbstractSwingComponent implements SwingEngineOperation, SwingProperties, Serializable {

    /* Constants */
    private static final long serialVersionUID = 1L;

    private static final String WAIT_READ = "WAIT_READ";

    private static final String WAIT_CLICK = "WAIT_CLICK";

    private static final String WAIT_ENTER = "WAIT_ENTER";

    private static final int DEFAULT_WAIT_TIME = 1000;

    /* Static fields */
    private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            AbstractSwingComponent.class);

    /* Instance fields */
    private ProcessCommunication communication;

    private SwingEngineOperationType type;

    private ActionResponse result;

    /**
     * Creates a new {@link AbstractSwingComponent} instance with the connection to the external
     * process.
     * 
     * @param communication
     *            the connection to the external process
     * @param type
     *            the swing engine operation type
     */
    public AbstractSwingComponent(ProcessCommunication communication, SwingEngineOperationType type) {
        if (communication == null) {
            throw new IllegalArgumentException("Communication not valid [null].");
        }
        if (type == null) {
            throw new IllegalArgumentException("SwingEngine operation type not valid [null].");
        }

        this.communication = communication;
        this.type = type;
    }

    @Override
    public ActionResponse execute(TestContext context, PropertyList propertyList,
            List<Metadata> metadataList, SubEngineActionType actionType) throws SubEngineException {

        Metadata metadata = null;

        try {
            // Validation
            this.validateArguments(context, propertyList, metadataList, actionType);

            SwingActionType swingAction = (SwingActionType) actionType;

            // Init result
            metadata = metadataList.get(metadataList.size() - 1);
            this.initResult(context, metadata, actionType);

//            this.validateComponent(propertyList, swingAction);

            // Execute Command
            ExecutionCommand command = createCommand(propertyList, metadata, swingAction);
            Integer timeout = getTimeout(metadata, swingAction);
            Property reply = executeCommand(context, command, timeout);

            if (reply != null) {
            	PropertyList returnList = PropertyHelper.createPropertyList("Return");
            	PropertyHelper.add(reply, returnList);
                result.setReturnProperties(returnList);
            }

        } catch (SwingValidationException e) {
            logger.error(e, "Error validating Swing component.");
            failResult(metadata, actionType, e.getMessage());
        } catch (Exception e) {
            logger.error(e, "Error during Swing component execution.");
            failResult(metadata, actionType);
        }

        return result;
    }

    /**
     * Defines validation constraints for a Swing component.
     * 
     * @param constraints
     *            the constraint container
     */
    protected abstract void defineConstraints(SwingComponentConstraints constraints,
            SwingActionType actionType);

    /**
     * Validates the input arguments.
     * 
     * @param context
     *            the test context
     * @param propertyList
     *            the list of properties
     * @param metadataList
     *            the list of metadata
     * @param actionType
     *            the action type
     */
    private void validateArguments(TestContext context, PropertyList propertyList,
            List<Metadata> metadataList, SubEngineActionType actionType) {

        if (context == null) {
            throw new IllegalArgumentException("TestContext must not be null.");
        }

        if (propertyList == null) {
            throw new IllegalArgumentException("PropertyList must not be null.");
        }

        if (metadataList == null) {
            throw new IllegalArgumentException("MetadataList must not be null.");
        }

        if (actionType == null) {
            throw new IllegalArgumentException("ActionType must not be null.");
        }

        if (!(actionType instanceof SwingActionType)) {
            throw new IllegalArgumentException("ActionType must be a SwingActionType.");
        }
    }

//    /**
//     * Validates a Swing Component for properties and action type.
//     * 
//     * @param propertyList
//     *            the list of properties
//     * @param actionType
//     *            the type of action
//     * 
//     * @throws SwingValidationException
//     *             if a component is not valid
//     */
//    private void validateComponent(PropertyList properties, SwingActionType actionType)
//            throws SwingValidationException {
//        SwingComponentConstraints constraints = new SwingComponentConstraints(this.type);
//
//        this.defineConstraints(constraints, actionType);
//
//        SwingComponentValidator validator = constraints.buildValidator();
//        validator.validate(properties, actionType);
//    }

    /**
     * Extracts the timeout time for the current action on the {@link Metadata} component.
     * 
     * @param metadata
     *            the current {@link Metadata} object
     * @param actionType
     *            the type of action
     * 
     * @return the wait time for this object and action
     */
    private Integer getTimeout(Metadata metadata, SwingActionType actionType) {

        NumericProperty property = null;

        try {
            switch (actionType) {
            case LEFTCLICK:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_CLICK);
                break;
            case RIGHTCLICK:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_CLICK);
                break;
            case DOUBLECLICK:
            	property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_CLICK);
            	break;
            case FIND:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_CLICK);
                break;
            case ENTER:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_ENTER);
                break;
            case READ:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_READ);
                break;
            case COUNT:
                property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_READ);
                break;
            case IS_AVAILABLE:
            	property = (NumericProperty) PropertyHelper.getFromList(metadata.getPropertyList(), WAIT_READ);
                break;
            }

        } catch (Exception e) {
            logger.warning(e, "Problem extracting wait time for ", metadata.getName().getValue(),
                    ", using DEFAULT.");
        }

        Integer waitTime = property != null ? property.getValue().getValue().intValue() : DEFAULT_WAIT_TIME;

        return waitTime;
    }

    /**
     * Creates a new execution command which is sent to the external process.
     * 
     * @param propertyList
     *            the list of properties for the command
     * @param metadata
     *            the metadata for the command
     * @param actionType
     *            the action type for the command
     * 
     * @return the prepared command
     */
    private ExecutionCommand createCommand(PropertyList propertyList,
            Metadata metadata, SwingActionType actionType) {

        ExecutionCommand command = new ExecutionCommand();
        command.setActionType(actionType);
        command.setComponentType(type);
        command.setMetadata(metadata);
        command.setPropertyList(propertyList);
        return command;
    }

    /**
     * Sends an {@link ExecutionCommand} to the remote test application.
     * 
     * @param context
     *            the test context
     * @param command
     *            the command to execute
     * @param timeout
     *            the time to wait for execution
     * 
     * @return the resulting property or null if none exist.
     * 
     * @throws Exception
     */
    private Property executeCommand(TestContext context, ExecutionCommand command, Integer timeout)
            throws Exception {

        Property property = null;
        validateCommand(command);

        property = CommandReplyEvaluator.getInstance().evaluateReply(
                communication.executeCommand(command, timeout), this.result);
        
        return property;
    }

    private void validateCommand(ExecutionCommand command) {

        if (command == null) {
            throw new IllegalArgumentException("Execution command must not be null.");
        }

        if (command.getActionType() == null) {
            throw new IllegalArgumentException("Swing action type must not be null.");
        }

        if (command.getComponentType() == null) {
            throw new IllegalArgumentException("Swing component type must not be null.");
        }

        if (command.getType() != ProcessCommandType.EXEC) {
            throw new IllegalArgumentException("ExecutionCommand must be of type 'EXEC'.");
        }
    }

    /**
     * Initializes the {@link ActionResponse} with default information.
     * 
     * @param context
     *            the test context
     * @param metadata
     *            the current metadata object
     * @param actionType
     *            the current action
     */
    private void initResult(TestContext context, Metadata metadata, SubEngineActionType actionType) {
        StringBuilder msg = new StringBuilder();
        msg.append("Executing ");
        msg.append(getClass().getSimpleName().replace("Impl", ""));
        msg.append(" with name='");
        msg.append(metadata != null ? metadata.getId() : "null");
        msg.append(" and action='");
        msg.append(actionType);
        msg.append("'");

        this.result = TestResultHelper.createActionResponse();
        this.result.setMessage(msg.toString());
    }

    /**
     * Fail the {@link ActionResponse} with an appropriate error message.
     * 
     * @param metadata
     *            the current metadata object
     * @param actionType
     *            the current action
     * @param info
     *            optional messages added to the result.
     */
    private void failResult(Metadata metadata, SubEngineActionType actionType, String... infos) {
        this.result.setActionStatus(ActionStatusType.FAILED);

        StringBuilder msg = new StringBuilder();
        msg.append("Failed executing ");
        msg.append(getClass().getSimpleName().replace("Impl", ""));
        msg.append(" with name='");
        msg.append(metadata != null ? metadata.getId() : "null");
        msg.append("' and action='");
        msg.append(actionType);
        msg.append("'. ");

        if (infos != null) {
            for (String info : infos) {
                msg.append(info);
            }
        }
        this.result.setErrorMessage(msg.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("SwingComponent ");
        builder.append(type);
        builder.append(":\n - Communication = ");
        builder.append(communication == null || !communication.isActive() ? "Deactive" : "Active");
        builder.append("\n  - Result = ");
        builder.append(result == null || result.getActionStatus() == null ? "NOT FINISHED"
                : result.getActionStatus());
        return builder.toString();
    }

}
