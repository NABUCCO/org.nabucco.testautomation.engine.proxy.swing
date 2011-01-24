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
package org.nabucco.testautomation.engine.proxy.swing.ui.validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;

import org.nabucco.testautomation.facade.datatype.property.base.PropertyType;

/**
 * SwingComponentConstraints
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingComponentConstraints {

    private final SwingEngineOperationType operationType;

    private List<PropertyType> propertyTypes = new ArrayList<PropertyType>();

    private List<SwingActionType> actionTypes = new ArrayList<SwingActionType>();

    /**
     * Creates a new {@link SwingComponentConstraints} instance.
     * 
     * @param operationType
     *            the particular operation
     */
    public SwingComponentConstraints(SwingEngineOperationType operationType) {

        if (operationType == null) {
            throw new IllegalArgumentException(
                    "'Operation Type' for Swing validation is not valid [null].");
        }

        this.operationType = operationType;
    }

    /**
     * Adds the type(s) to the constraint list.
     * <p>
     * Defines the PropertyType constraints. All necessary properties for a Swing Component must be
     * defined here. If none are defined there must not exist any properties for this Swing
     * Component execution.
     * 
     * @param type
     *            the types to set
     * 
     * @return the current {@link SwingComponentConstraints} instance for chaining
     */
    public SwingComponentConstraints properties(PropertyType... type) {
        this.propertyTypes.addAll(Arrays.asList(type));
        return this;
    }

    /**
     * Adds the action(s) to the constraint list.
     * <p>
     * Defines the SwingAction constraints. All possible actions for a Swing Component must be
     * defined here. If none are defined there is no execution allowed for this Swing Component
     * execution.
     * 
     * @param action
     *            the action to set
     * 
     * @return the current {@link SwingComponentConstraints} instance for chaining
     */
    public SwingComponentConstraints actions(SwingActionType... action) {
        this.actionTypes.addAll(Arrays.asList(action));
        return this;
    }

    List<PropertyType> getPropertyTypeList() {
        return propertyTypes;
    }

    SwingEngineOperationType getOperationType() {
        return operationType;
    }

    List<SwingActionType> getActionTypeList() {
        return actionTypes;
    }

    /**
     * Creates a new {@link SwingComponentValidator} instance for the current builder instance.
     * 
     * @return the new {@link SwingComponentValidator} instance.
     */
    public SwingComponentValidator buildValidator() {
        return new SwingComponentValidator(this);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Constraints:\n - Operation = ");
        builder.append(operationType == null ? "undefined" : operationType.name());
        builder.append("\n - Actions = ");
        builder.append(actionTypes.isEmpty() ? "none" : actionTypes);
        builder.append("\n - Properties = ");
        builder.append(propertyTypes.isEmpty() ? "none" : propertyTypes);
        return builder.toString();
    }

}
