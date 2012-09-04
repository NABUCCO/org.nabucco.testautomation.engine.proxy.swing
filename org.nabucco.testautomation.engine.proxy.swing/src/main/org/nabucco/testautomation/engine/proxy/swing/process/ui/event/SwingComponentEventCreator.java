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
package org.nabucco.testautomation.engine.proxy.swing.process.ui.event;

import java.awt.Component;
import java.awt.TextComponent;
import java.awt.Window;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.JTextComponent;
import javax.swing.tree.TreePath;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.TestHelper;
import junit.extensions.jfcunit.eventdata.AbstractMouseEventData;
import junit.extensions.jfcunit.eventdata.JComboBoxMouseEventData;
import junit.extensions.jfcunit.eventdata.JTabbedPaneMouseEventData;
import junit.extensions.jfcunit.eventdata.JTableMouseEventData;
import junit.extensions.jfcunit.eventdata.JTreeMouseEventData;
import junit.extensions.jfcunit.eventdata.KeyEventData;
import junit.extensions.jfcunit.eventdata.MouseEventData;
import junit.extensions.jfcunit.eventdata.StringEventData;

import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.util.SwingTreePathGenerator;


/**
 * Creates mouse, key and text events for {@link JComponent} nodes.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public final class SwingComponentEventCreator {

    /**
     * Private constructor.
     */
    private SwingComponentEventCreator() {
    }

    /**
     * Writes text to a {@link JComponent}
     * 
     * @param component
     *            the component to send the message to
     * @param msg
     *            the string message
     */
    public static boolean createTextEvent(JTextComponent component, String msg) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Enter text in Swing component ["
                + name
                + ", "
                + type
                + "] msg='"
                + msg
                + "'.");

        if (msg == null) {
        	msg = "";
        }

        JFCTestCase testCase = TestHelper.getCurrentTestCase();
        component.setText("");
        
        StringEventData stringEvent = new StringEventData(testCase, component, msg);
        testCase.getHelper().sendString(stringEvent);

        if (!component.getText().equals(msg)) {
        	component.setText(msg);
        	component.repaint();
        	
        	if (!component.getText().equals(msg)) {
	            System.err.println("Message could not be send to text component.");
	            return false;
        	}
        }
        return true;
    }

    /**
     * Clicks once on a {@link JComponent}.
     * 
     * @param component
     *            the component to click on
     */
    public static void createComponentClickEvent(JComponent component) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Execute left click on Swing component [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new MouseEventData(testCase, component, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JComponent} with the right mouse button.
     * 
     * @param component
     *            the component to click on
     */
    public static void createComponentRightClickEvent(JComponent component) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Execute right click on Swing component [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new MouseEventData(testCase, component, 1, true);
        testCase.getHelper().enterClickAndLeave(event);
    }
    
	/**
	 * Presses the button with keyCode (@see java.awt.event.KeyEvent) button on a {@link JComponent}.
	 * 
	 * @param component
	 *            the component to press the button on
	 * @param keyCode
	 *            the code of the key to press
	 * @see java.awt.event.KeyEvent
	 */
    public static void createComponentKeyPressEvent(JComponent component, Integer keyCode) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Execute keycode " + keyCode + " on Swing component [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        KeyEventData event = new KeyEventData(testCase, component, keyCode);
        testCase.getHelper().sendKeyAction(event);
    }

    /**
     * Clicks twice on a {@link JComponent}
     * 
     * @param component
     *            the component to click on
     */
    public static void createComponentDoubleClickEvent(JComponent component) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Execute double left click on Swing component ["
                + name
                + ", "
                + type
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new MouseEventData(testCase, component, 2);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JComponent}.
     * 
     * @param comboBox
     *            the component to click on
     */
    public static void createComboBoxClickEvent(JComboBox comboBox, int index) {
        String name = (comboBox.getName() == null) ? "null" : comboBox.getName();
        String type = comboBox.getClass().getSimpleName();
        System.out.println("Execute left click on Swing combobox [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JComboBoxMouseEventData(testCase, comboBox, index, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }
    
    /**
     * Clicks once on a {@link JComponent}.
     * 
     * @param comboBox
     *            the component to click on
     */
    public static void createComboBoxClickEvent(JComboBox comboBox, String value) {
        String name = (comboBox.getName() == null) ? "null" : comboBox.getName();
        String type = comboBox.getClass().getSimpleName();
        System.out.println("Execute left click on Swing combobox [" + name + ", " + type + "].");

        for (int i = 0; i < comboBox.getItemCount(); i++) {
        	Object item = comboBox.getItemAt(i);
        	String itemString = item.toString();
        	
        	if (itemString != null && itemString.equalsIgnoreCase(value)) {
        		JFCTestCase testCase = TestHelper.getCurrentTestCase();
        		AbstractMouseEventData event = new JComboBoxMouseEventData(testCase, comboBox, i, 1);
        		testCase.getHelper().enterClickAndLeave(event);
        		break;
        	}
        }        
    }

    /**
     * Clicks twice on a {@link JComponent}.
     * 
     * @param comboBox
     *            the component to click on
     */
    public static void createComboBoxDoubleClickEvent(JComboBox comboBox, int index) {
        String name = (comboBox.getName() == null) ? "null" : comboBox.getName();
        String type = comboBox.getClass().getSimpleName();
        System.out.println("Execute left click on Swing combobox [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JComboBoxMouseEventData(testCase, comboBox, index, 2);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JComponent} menu item.
     * 
     * @param menu
     *            the menu bar to click on
     * @param indexes
     *            the indexes of the menu
     */
    public static void createMenuClickEvent(JMenu menu, JMenuItem item) {
        String name = (menu.getName() == null) ? "null" : menu.getName();
        String type = menu.getClass().getSimpleName();
        System.out.println("Execute left click on Swing menu [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();
        AbstractMouseEventData event = new MouseEventData(testCase, menu);
        testCase.getHelper().enterClickAndLeave(event);
        
        event = new MouseEventData(testCase, item);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTree} node.
     * 
     * @param tree
     *            the tree to click on
     * @param path
     *            path to the node
     * 
     * @throws ProcessInvocationException
     */
    public static void createTreeClickEvent(JTree tree, String[] path)
            throws ProcessInvocationException {

        String name = (tree.getName() == null) ? "null" : tree.getName();
        String type = tree.getClass().getSimpleName();
        String node = path[path.length - 1];
        System.out.println("Select item in Swing tree [" + name + ", " + type + ", " + node + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        TreePath treePath = SwingTreePathGenerator.getInstance().getTreePath(path, tree);

        if (treePath == null) {
            System.err.println("Path for SwingTree not valid.");
            return;
        }

        AbstractMouseEventData event = new JTreeMouseEventData(testCase, tree, treePath, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTree} node with the right mouse button.
     * 
     * @param tree
     *            the tree to click on
     * @param path
     *            path to the node
     * 
     * @throws ProcessInvocationException
     */
    public static void createTreeRightClickEvent(JTree tree, String[] path)
            throws ProcessInvocationException {

        String name = (tree.getName() == null) ? "null" : tree.getName();
        String type = tree.getClass().getSimpleName();
        String node = path[path.length - 1];
        System.out.println("Execute right on item in Swing tree ["
                + name
                + ", "
                + type
                + ", "
                + node
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        TreePath treePath = SwingTreePathGenerator.getInstance().getTreePath(path, tree);

        if (treePath == null) {
            System.out.println("Path for SwingTree not valid.");
            return;
        }

        AbstractMouseEventData event = new JTreeMouseEventData(testCase, tree, treePath, 1, true);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks twice on a {@link JTree} node.
     * 
     * @param tree
     *            the tree to click on
     * @param path
     *            path to the node
     * 
     * @throws ProcessInvocationException
     */
    public static void createTreeDoubleClickEvent(JTree tree, String[] path)
            throws ProcessInvocationException {

        String name = (tree.getName() == null) ? "null" : tree.getName();
        String type = tree.getClass().getSimpleName();
        String node = path[path.length - 1];
        System.out.println("Select item in Swing tree [" + name + ", " + type + ", " + node + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        TreePath treePath = SwingTreePathGenerator.getInstance().getTreePath(path, tree);

        if (treePath == null) {
            System.out.println("Path for SwingTree not valid.");
            return;
        }

        AbstractMouseEventData event = new JTreeMouseEventData(testCase, tree, treePath, 2);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTabbedPane} node.
     * 
     * @param tab
     *            the tab to click on
     * @param index
     *            index of the tab
     */
    public static void createTabClickEvent(JTabbedPane tab, Integer index) {
        String name = (tab.getName() == null) ? "null" : tab.getName();
        String type = tab.getClass().getSimpleName();
        System.out.println("Select item in Swing tab ["
                + name
                + ", "
                + type
                + ", "
                + index.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTabbedPaneMouseEventData(testCase, tab, index, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks twice on a {@link JTabbedPane} node.
     * 
     * @param tab
     *            the tab to click on
     * @param index
     *            index of the tab
     */
    public static void createTabDoubleClickEvent(JTabbedPane tab, Integer index) {
        String name = (tab.getName() == null) ? "null" : tab.getName();
        String type = tab.getClass().getSimpleName();
        System.out.println("Select item in Swing tab ["
                + name
                + ", "
                + type
                + ", "
                + index.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTabbedPaneMouseEventData(testCase, tab, index, 2);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTabbedPane} node with the right mouse button.
     * 
     * @param tab
     *            the tab to click on
     * @param index
     *            index of the tab
     */
    public static void createTabRightClickEvent(JTabbedPane tab, Integer index) {
        String name = (tab.getName() == null) ? "null" : tab.getName();
        String type = tab.getClass().getSimpleName();
        System.out.println("Execute right click on Swing tab ["
                + name
                + ", "
                + type
                + ", "
                + index.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTabbedPaneMouseEventData(testCase, tab, index, 1, true);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTable} node.
     * 
     * @param table
     *            the table to click on
     * @param row
     *            the row to click
     * @param column
     *            the column to click
     */
    public static void createTableClickEvent(JTable table, Integer row, Integer column) {
        String name = (table.getName() == null) ? "null" : table.getName();
        String type = table.getClass().getSimpleName();
        System.out.println("Select item in Swing table ["
                + name
                + ", "
                + type
                + ", "
                + row.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTableMouseEventData(testCase, table, row, column, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTable} node.
     * 
     * @param table
     *            the table to click on
     * @param entry
     *            the entry to select
     * @param column
     *            the column to click
     */
    public static void createTableClickEvent(JTable table, String entry, Integer column) {
        String name = (table.getName() == null) ? "null" : table.getName();
        String type = table.getClass().getSimpleName();
        System.out.println("Try to select item in Swing table ["
                + name
                + ", "
                + type
                + ", "
                + entry
                + "].");
        
        TableModel model = table.getModel();
		int rowCount = model.getRowCount();
		int row = 0;
        
        for (int i = 0; i < rowCount; i++) {
        	String value = (String) model.getValueAt(i, column);
        	
        	if (value.equalsIgnoreCase(entry)) {
        		row = i;
        		break;
        	}
        }

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTableMouseEventData(testCase, table, row, column, 1);
        testCase.getHelper().enterClickAndLeave(event);
    }

    
    /**
     * Clicks twice on a {@link JTable} node.
     * 
     * @param table
     *            the table to click on
     * @param row
     *            the row to click
     * @param column
     *            the column to click
     */
    public static void createTableDoubleClickEvent(JTable table, Integer row, Integer column) {
        String name = (table.getName() == null) ? "null" : table.getName();
        String type = table.getClass().getSimpleName();
        System.out.println("Select item in Swing table ["
                + name
                + ", "
                + type
                + ", "
                + row.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTableMouseEventData(testCase, table, row, column, 2);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Clicks once on a {@link JTable} node with the right mouse button.
     * 
     * @param table
     *            the table to click on
     * @param row
     *            the row to click
     * @param column
     *            the column to click
     */
    public static void createTableRightClickEvent(JTable table, Integer row, Integer column) {
        String name = (table.getName() == null) ? "null" : table.getName();
        String type = table.getClass().getSimpleName();
        System.out.println("Execute right click on Swing table cell ["
                + name
                + ", "
                + type
                + ", "
                + row.toString()
                + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        AbstractMouseEventData event = new JTableMouseEventData(testCase, table, row, column, 1,
                true);
        testCase.getHelper().enterClickAndLeave(event);
    }

    /**
     * Reads the status from a {@link JToggleButton}.
     * 
     * @param button
     *            the toggle button to check
     * 
     * @return <b>true</b> if the toggle button is selected, otherwise <b>false</b>
     */
    public static Boolean readStatus(JToggleButton button) {
        String name = (button.getName() == null) ? "null" : button.getName();
        String type = button.getClass().getSimpleName();
        System.out.println("Read status from Swing toggle button [" + name + ", " + type + "].");

        return button.isSelected();
    }

    /**
     * Reads text from a {@link JDialog}.
     * 
     * @param dialog
     *            the dialog to check
     * 
     * @return the string message
     */
    public static String readText(JDialog dialog) {
        String name = (dialog.getName() == null) ? "null" : dialog.getName();
        String type = dialog.getClass().getSimpleName();
        System.out.println("Read text from Swing dialog [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        return testCase.getHelper().getMessageFromJDialog(dialog);
    }

    /**
     * Reads text from a {@link JTabbedPane}.
     * 
     * @param tab
     *            the tab pane to check
     * @param index
     *            the tab index
     * 
     * @return the string message
     */
    public static String readText(JTabbedPane tab, Integer index) {
        String name = (tab.getName() == null) ? "null" : tab.getName();
        String type = tab.getClass().getSimpleName();
        System.out.println("Read text from Swing tab [" + name + ", " + type + "].");

        return tab.getTitleAt(index);
    }

    /**
     * Reads text from a {@link JComboBox}.
     * 
     * @param combo
     *            the combo box
     * @param the
     *            index
     * 
     * @return the string message
     */
    public static String readText(JComboBox combo, int index) {
        String name = (combo.getName() == null) ? "null" : combo.getName();
        String type = combo.getClass().getSimpleName();
        System.out.println("Read text from Swing tab [" + name + ", " + type + "].");

        Object item = combo.getItemAt(index);

        if (item instanceof JTextComponent) {
            return readText((JTextComponent) item);
        } else if (item instanceof JDialog) {
            return readText((JDialog) item);
        } else if (item instanceof TextComponent) {
            return readText((TextComponent) item);
        } else if (item instanceof JLabel) {
            return readText((JLabel) item);
        } else if (item instanceof Component) {
            return item.toString();
        } else if (item instanceof String) {
            return (String) item;
        } else {
            return null;
        }
    }

    /**
     * Reads text from a {@link JLabel}.
     * 
     * @param label
     *            the component to read
     * 
     * @return the string message
     */
    public static String readText(JLabel label) {
        String name = (label.getName() == null) ? "null" : label.getName();
        String type = label.getClass().getSimpleName();
        System.out.println("Read text from Swing label [" + name + ", " + type + "].");
        return label.getText();
    }

    /**
     * Reads text from a {@link JTextComponent}.
     * 
     * @param component
     *            the component to read
     * 
     * @return the string message
     */
    public static String readText(JTextComponent component) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Read text from Swing component [" + name + ", " + type + "].");
        return component.getText();
    }

    /**
     * Reads text from a {@link TextComponent}.
     * 
     * @param component
     *            the component to read
     * 
     * @return the string message
     */
    public static String readText(TextComponent component) {
        String name = (component.getName() == null) ? "null" : component.getName();
        String type = component.getClass().getSimpleName();
        System.out.println("Read text from Swing component [" + name + ", " + type + "].");
        return component.getText();
    }

    /**
     * Reads text from a {@link JTable} cell.
     * 
     * @param table
     *            the table
     * @param row
     *            row of the cell
     * @param column
     *            column of the cell
     * @return the string message
     */
    public static String readText(JTable table, Integer row, Integer column) {
        String name = (table.getName() == null) ? "null" : table.getName();
        String type = table.getClass().getSimpleName();

        System.out.println("Read text from Swing table [" + name + ", " + type + "].");

        TableCellRenderer renderer = table.getCellRenderer(row, column);
        Object value = table.getModel().getValueAt(row, column);

        Component component = renderer.getTableCellRendererComponent(table, value, false, false,
                row, column);

        if (component instanceof JTextComponent) {
            return readText((JTextComponent) component);
        } else if (component instanceof JDialog) {
            return readText((JDialog) component);
        } else if (component instanceof TextComponent) {
            return readText((TextComponent) component);
        } else if (component instanceof JLabel) {
            return readText((JLabel) component);
        } else {
            return value == null ? "" : value.toString();
        }
    }

    /**
     * Closes a {@link Window}
     * 
     * @param window
     *            the window to close
     */
    public static void closeWindow(Window window) {
        String name = (window.getName() == null) ? "null" : window.getName();
        String type = window.getClass().getSimpleName();
        System.out.println("Close Swing component [" + name + ", " + type + "].");

        JFCTestCase testCase = TestHelper.getCurrentTestCase();

        TestHelper.disposeWindow(window, testCase);
    }

}
