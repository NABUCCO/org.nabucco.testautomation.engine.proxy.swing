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

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultMutableTreeNode;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;

import org.nabucco.testautomation.facade.datatype.property.PropertyList;

/**
 * TreeTableMapper
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class TreeTableMapper implements SwingTableMapper {

    private static final String GET_LABEL = "getLabel";

    private static final String GET_PARAMETER = "getParameter";

    private static final String GET_PARAMETER_KEY = "getParameterKey";

    private JTable table;

    public TreeTableMapper(JTable table) {
        this.table = table;
    }

    @Override
    public void createPropertyList(PropertyList propertyList) throws ProcessInvocationException {

        JTree tree = null;

        for (int row = 0; row < table.getModel().getRowCount(); row++) {

            PropertyList rowList = PropertyHelper.createPropertyList(String.valueOf(row));

            for (int col = 0; col < table.getModel().getColumnCount(); col++) {

                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Object value = table.getModel().getValueAt(row, col);

                Component component = renderer.getTableCellRendererComponent(table, value, false,
                        false, row, col);

                if (tree == null && component instanceof JTree) {
                    tree = (JTree) component;
                }

                PropertyHelper.add(PropertyHelper.createStringProperty(String.valueOf(col), value == null ? "" : value
                        .toString()), rowList);
            }
            PropertyHelper.add(rowList, propertyList);
        }
        for (int treeRow = 0; treeRow < tree.getRowCount(); treeRow++) {
            this.insertTreeTableElement(propertyList, tree, treeRow, 0);
        }
    }

    /**
     * Reads an element of the tree-table by row and column.
     * 
     * @param parentMap
     *            the map to insert the result
     * @param tree
     *            the tree to read
     * @param row
     *            the row
     * @param col
     *            the column
     * 
     * @throws ProcessInvocationException
     */
    private void insertTreeTableElement(PropertyList parentList, JTree tree, Integer row, Integer col)
            throws ProcessInvocationException {

        if (tree.getPathForRow(row).getLastPathComponent() instanceof DefaultMutableTreeNode) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getPathForRow(row)
                    .getLastPathComponent();
            try {
                Object value = null;

                for (Method method : node.getUserObject().getClass().getMethods()) {
                    if (method.getName().equals(GET_LABEL)) {
                        value = node.getUserObject().getClass().getMethod(GET_LABEL).invoke(
                                node.getUserObject());
                        break;
                    } else if (method.getName().equals(GET_PARAMETER)) {
                        value = node.getUserObject().getClass().getMethod(GET_PARAMETER).invoke(
                                node.getUserObject());
                        value = value.getClass().getMethod(GET_PARAMETER_KEY).invoke(value);
                        break;
                    }
                }

                PropertyList rowProperty = (PropertyList) parentList.getPropertyList().get(row).getProperty();
                PropertyHelper.add(PropertyHelper.createStringProperty(String.valueOf(col), value == null ? "" : value.toString()), rowProperty);
            } catch (Exception e) {
                throw new ProcessInvocationException(
                        "Error retrieving swing tree table node labels.", e);
            }
        }
    }

}
