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
package org.nabucco.testautomation.engine.proxy.swing.process.ui.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JTable;

import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.SwingTable;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.base.PropertyContainer;

/**
 * Maps read data from a {@link JTable} to a {@link PropertyMap}.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class HierarchicalSwingTableMapper implements SwingTableMapper {

    private List<Property> propertyList;

    private PropertyList parentList;

    private JTable table;

    /**
     * Creates a new {@link HierarchicalSwingTableMapper} instance for a given list of {@link Property}.
     * 
     * @param propertyList
     *            the {@link Property} list
     */
    public HierarchicalSwingTableMapper(JTable table) {
        this.table = table;
    }

    /**
     * Extracts all read {@link SwingTable} information into a {@link PropertyMap} given construct.
     * 
     * @param parentMap
     *            the {@link PropertyMap} to insert the result.
     */
    @Override
    public void createPropertyList(PropertyList parentList) {

        if (parentList == null) {
            System.out.println("SwingTable mapping not possible. PropertyMap is null.");
            return;
        }

        this.parentList = parentList;

        this.initPropertyList();
        this.createProperty(parentList, 0);
        this.removeDots(parentList);
    }

    private void initPropertyList() {

        this.propertyList = new ArrayList<Property>();

        for (int row = 0; row < table.getModel().getRowCount(); row++) {

            String name = SwingComponentEventCreator.readText(table, row, 0);

            if (name != null && !name.isEmpty() && ".".equals(name.substring(name.length() - 1))) {
                name = name.substring(0, name.length() - 1);
            }

            String value = SwingComponentEventCreator.readText(table, row, 1);

            TextProperty rowProperty = PropertyHelper.createTextProperty(name, value);
            this.propertyList.add(rowProperty);
        }
    }

    /**
     * Creates a single property and inserts it into the parent container
     * 
     * @param parent
     *            the parent map
     * @param index
     *            the current index in the propertyList
     */
    private void createProperty(PropertyList parent, int index) {

        Property current = propertyList.get(index);

        if (current == null) {
            System.out.println("SwingTable mapping not possible. Properties not valid.");
            return;
        }

        Property property = null;
        Property successor = getSuccessor(index);

        if (successor != null) {

            final int dots = getDots(current.getName().getValue());
            final int successorDots = getDots(successor.getName().getValue());

            if (dots < successorDots) {
                property = PropertyHelper.createPropertyList(current.getName().getValue());
                propertyList.set(index, property);
                createProperty((PropertyList) property, index + 1);
            } else if (dots > successorDots) {
                property = current;
                propertyList.set(index, property);
                createProperty(getParent(successor, index + 1), index + 1);
            } else {
                property = current;
                createProperty(parent, index + 1);
            }
            PropertyHelper.add(property, parent);
        } else {
        	PropertyHelper.add(current, parent);
        }
    }

    /**
     * Returns the parent property of the current property
     * 
     * @param property
     *            the property
     * @param index
     *            the index of the property
     * 
     * @return the parent map of the property
     */
    private PropertyList getParent(Property property, int index) {

        Property predecessor = getPredecessor(index);

        if (predecessor != null) {

            int dots = getDots(property.getName().getValue());

            if (dots == 0) {
                return parentList;
            }

            int predecessorDots = getDots(predecessor.getName().getValue());

            int currentIndex = index;

            while (predecessor != null && predecessorDots >= dots) {
                predecessor = getPredecessor(currentIndex);

                if (predecessor != null) {
                    predecessorDots = getDots(predecessor.getName().getValue());
                }
                currentIndex--;
            }

            if (!(predecessor instanceof PropertyList)) {
                throw new IllegalArgumentException("Parent property is not a PropertyMap.");
            }

            return (PropertyList) predecessor;
        }

        return parentList;
    }

    /**
     * Returns the predecessor of the current component.
     * 
     * @param index
     *            the current index
     * 
     * @return the previous property in the list
     */
    private Property getPredecessor(int index) {
        if (index - 1 >= 0) {
            return propertyList.get(index - 1);
        }
        return null;
    }

    /**
     * Returns the successor of the the current component.
     * 
     * @param index
     *            the current index
     * 
     * @return next property in the list
     */
    private Property getSuccessor(int index) {
        if (index + 1 <= propertyList.size() - 1) {
            return propertyList.get(index + 1);
        }
        return null;
    }

    /**
     * Counts the number of preceding dots.
     * 
     * @param value
     *            the string
     * 
     * @return the number of dots
     */
    private int getDots(String value) {

        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] != '.') {
                return i;
            }
        }
        return 0;
    }

    /**
     * Removes all preceding dots.
     * 
     * @param parent
     *            the filled {@link PropertyMap}
     */
    private void removeDots(PropertyList parent) {

        Map<String, Property> properties = new HashMap<String, Property>();
        for (PropertyContainer container : parent.getPropertyList()) {
        	Property property = container.getProperty();
            properties.put(property.getName().getValue(), property);
        }

        List<String> removeKeys = new ArrayList<String>();
        List<Property> newProperties = new ArrayList<Property>();

        for (Entry<String, Property> entry : properties.entrySet()) {

            String key = entry.getKey();
            Property value = entry.getValue();

            if (value instanceof PropertyList) {

                PropertyList oldProperty = (PropertyList) value;
                removeDots(oldProperty);

                if (key.startsWith("..")) {
                    removeKeys.add(key);
                    PropertyList newProperty = PropertyHelper.createPropertyList(oldProperty.getName().getValue().replace(".", ""));
                    newProperty.getPropertyList().addAll((oldProperty).getPropertyList());
                    newProperties.add(newProperty);
                }
            } else if (value instanceof TextProperty) {

                TextProperty oldProperty = (TextProperty) value;

                if (key.startsWith("..")) {
                    removeKeys.add(key);
                    TextProperty newProperty = PropertyHelper.createTextProperty(oldProperty.getName().getValue().replace(
                            ".", ""), (oldProperty).getValue().getValue());

                    newProperties.add(newProperty);
                }
            }
        }

        for (String key : removeKeys) {
            properties.remove(key);
        }

        for (Property newProperty : newProperties) {
            properties.put(newProperty.getName().getValue(), newProperty);
        }
    }

}
