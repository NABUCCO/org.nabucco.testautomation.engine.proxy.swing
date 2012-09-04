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
import java.util.List;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyType;

/**
 * Validates Swing Components.
 * 
 * @see SwingComponentConstraints
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingComponentValidator {

    private final SwingEngineOperationType operationType;

    private final List<PropertyType> propertyTypes = new ArrayList<PropertyType>();

    private final List<SwingActionType> actionTypes = new ArrayList<SwingActionType>();

    /**
     * Constructs a new SwingComponentValidator instance depending on its builder.
     * 
     * @param builder
     *            the builder with all values
     */
    SwingComponentValidator(SwingComponentConstraints builder) {
        if (builder == null) {
            throw new IllegalArgumentException("SwingComponentConstraints are not valid [null].");
        }

        this.operationType = builder.getOperationType();
        this.propertyTypes.addAll(builder.getPropertyTypeList());
        this.actionTypes.addAll(builder.getActionTypeList());
    }

    /**
     * Validates the list of properties against the {@link SwingComponentConstraints}
     * constraints.
     * 
     * @param properties
     *            the list of properties
     * @param actionTypes
     *            the action type to validate
     * 
     * @throws SwingValidationException
     *             if a property is not valid
     */
    public void validate(PropertyList properties, SwingActionType actionType)
            throws SwingValidationException {

        String info = " [operation = " + operationType.name() + ", action = " + actionType.name() + "].";

        int size = propertyTypes.size();

        if (properties.getPropertyList().size() != size) {
            throw new SwingValidationException("PropertyList must contain " + size
                    + " properties but contains " + properties.getPropertyList().size() + info);
        }
        
        if (!this.actionTypes.contains(actionType)) {
            throw new SwingValidationException("Action is not supported "
                    + actionType + " for operation " + operationType.name()
                    + ". Must be one of " + actionTypes + ".");
        }

        for (int i = 0; i < properties.getPropertyList().size(); i++) {
            validateProperty(properties.getPropertyList().get(i).getProperty(), propertyTypes.get(i), info);
        }

    }

    /**
     * Validates a single property for obligation and type.
     * 
     * @param property
     *            the property to validate
     * @param type
     *            required property type
     * @param info
     *            exception information
     * 
     * @throws SwingValidationException
     *             if a property is not valid
     */
    private void validateProperty(Property property, PropertyType type, String info)
            throws SwingValidationException {

        if (property == null) {
            throw new SwingValidationException("Property is not valid [null]" + info);
        }

        if (property.getName() == null || property.getName().getValue() == null) {
            throw new SwingValidationException("PropertyName is not valid [null]" + info);
        }

        if (property.getType() != type) {
            throw new SwingValidationException("Property must be of type "
                    + type + " but was " + property.getType() + info);
        }

    }
}
