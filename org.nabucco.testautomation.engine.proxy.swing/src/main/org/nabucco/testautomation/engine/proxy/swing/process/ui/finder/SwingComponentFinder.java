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
package org.nabucco.testautomation.engine.proxy.swing.process.ui.finder;

import java.applet.Applet;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.nabucco.testautomation.engine.base.util.PropertyHelper;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;

import junit.extensions.jfcunit.finder.ComponentFinder;
import junit.extensions.jfcunit.finder.DialogFinder;
import junit.extensions.jfcunit.finder.FrameFinder;

import org.nabucco.testautomation.facade.datatype.property.IntegerProperty;
import org.nabucco.testautomation.facade.datatype.property.StringProperty;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * Finds {@link JComponent} in a running Swing application.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingComponentFinder {

    private static final String COMPONENT_INDEX = "COMPONENT_INDEX";

    private static final String COMPONENT_PATH = "COMPONENT_PATH";

    /**
     * Singleton instance.
     */
    private static SwingComponentFinder instance = new SwingComponentFinder();

    /**
     * Private constructor.
     */
    private SwingComponentFinder() {
    }

    /**
     * Singleton access.
     * 
     * @return the SwingComponentFinder instance.
     */
    public static SwingComponentFinder getInstance() {
        return instance;
    }

    /**
     * Returns the ultimate root component of a component tree.
     * 
     * @param component
     *            the current component in the tree
     * 
     * @return the root component of the tree
     */
    public Component getRoot(final Component component) {
        Component root = null;

        for (Component parent = component; parent != null; parent = parent.getParent()) {
            if (parent instanceof Window || !parent.isLightweight()) {
                return parent;
            }

            if (parent instanceof Applet) {
                root = parent;
            }
        }

        return root;
    }

    /**
     * Finds a {@link JFrame} in an application with the given index.
     * 
     * @param index
     *            index of the frame in the component tree
     * 
     * @return the frame or null
     */
    public JFrame findFrame(Integer index) {
        if (index == null) {
            index = 0;
        }
        FrameFinder finder = new FrameFinder(null);

        Component component = finder.find(index);
        if (component instanceof JFrame) {
            return (JFrame) component;
        }
        return null;
    }

    /**
     * Finds a {@link JDialog} in a parent component with the given index.
     * 
     * @param index
     *            index of the dialog in the component tree
     * 
     * @return the dialog or null
     */
    public JDialog findDialog(Integer index, Container parent) {
        if (index == null) {
            index = 0;
        }
        DialogFinder finder = new DialogFinder(null);
        finder.setWait(0);

        Component component = finder.find(parent, index);
        if (component instanceof JDialog) {
            return (JDialog) component;
        }
        return null;
    }

    /**
     * Finds a {@link JComponent} in a parent component with the given index.
     * 
     * @param index
     *            index of the component in the component tree
     * 
     * @return the component or null
     */
    public Container findComponent(Integer index, Container parent) {
        if (index == null) {
            index = 0;
        }
        ComponentFinder finder = new ComponentFinder(Component.class);

        Component component = finder.find(parent, index);
        if (component instanceof JComponent) {
            return (JComponent) component;
        } else if (component instanceof Container) { // in case of an java.awt.Applet
        	return (Container) component;
        }
        return null;
    }

    /**
     * Finds a component in the component tree for a metadata path.
     * 
     * @param metadataList
     *            the list of {@link Metadata} elements pointing to the component
     * 
     * @return the component or null
     */
    public Component findComponent(List<Metadata> metadataList) {

        Container container = null;

        for (Metadata metadata : metadataList) {

            if (isSwing(metadata)) {
                container = findMetadataComponent(container, metadata);
            }

            if (container != null) {
                System.out.println("Found Swing Component: " + container.getClass().getSimpleName());
            } else {
                System.out.println("No Swing Component found for " + metadata.getId());
            }
        }
        return container;
    }

    /**
     * Find a component in a parent component for a given {@link Metadata} object.
     * 
     * @param container
     *            the parent component
     * @param metadata
     *            the {@link Metadata} object
     * 
     * @return the component or null
     */
    private Container findMetadataComponent(Container container, Metadata metadata) {

        IntegerProperty componentIndex = (IntegerProperty) PropertyHelper.getFromList(metadata.getPropertyList(), COMPONENT_INDEX);
        Integer index;

        if (componentIndex != null) {
            index = componentIndex.getValue().getValue();
        } else {
            String path = ((StringProperty) PropertyHelper.getFromList(metadata.getPropertyList(), COMPONENT_PATH)).getValue().getValue();
            String[] pathTokens = path.split(",");

            if (pathTokens == null || pathTokens.length == 0) {
                System.out.println(COMPONENT_PATH + " is not valid.");
                return null;
            }

            for (int i = 0; i < pathTokens.length - 1; i++) {
                String token = pathTokens[i];

                container = findByIndex(container, null, Integer.parseInt(token));
            }
            index = Integer.parseInt(pathTokens[pathTokens.length - 1]);
        }
        return findByIndex(container, metadata, index);
    }

    private Container findByIndex(Container container, Metadata metadata, Integer index) {

        String name = metadata.getName() == null ? "null" : metadata.getName().getValue();

        if (index == null) {
            System.out.println("Index does not exist for metadata " + name);
            return null;
        }
        
        SwingEngineOperationType type = SwingEngineOperationType.valueOf(metadata.getOperation().getCode().getValue());
        
        if (type == SwingEngineOperationType.SWING_FRAME) {
            container = findFrame(index);
        } else if (type == SwingEngineOperationType.SWING_DIALOG) {
            container = findDialog(index, null);
        } else {

            if (container == null) {
                System.out.println("Parent container does not exist for metadata " + name);
                return null;
            }

            if (container.getComponentCount() == 0) {
                System.out.println("Parent container does contain metadata " + name);
                return null;
            }

            if (index > container.getComponentCount() - 1) {

                // Skip technical Swing components
                while (isSkip(findComponent(0, container))) {
                    container = findComponent(0, container);
                    System.out.println("Skip Swing Component: "
                            + container.getClass().getSimpleName());
                }
            }

            container = findComponent(index, container);

            // Skip technical Swing components
            while (isSkip(container)) {
                System.out.println("Skip Swing Component: " + container.getClass().getSimpleName());
                container = findComponent(0, container);
            }
        }

        return container;
    }

    /**
     * Skip non-Swing and functional-Swing {@link Metadata} elements.
     * 
     * @param metadata
     *            the metadata element
     * 
     * @return <b>true</b> if the metadata element has to be processed, <b>false</b> if not.
     */
    private boolean isSwing(Metadata metadata) {
        
    	SwingEngineOperationType type = SwingEngineOperationType.valueOf(metadata.getOperation().getCode().getValue());
    	
    	switch (type) {
    		
    	case SWING_APPLICATION:
    		return false;
    	case SWING_APPLET:
    		return false;
    	case SWING_BUTTON:
    	case SWING_CHECKBOX:
    	case SWING_COMBOBOX:
    	case SWING_DIALOG:
    	case SWING_FRAME:
    	case SWING_MENU:
    	case SWING_PASSWORD:
    	case SWING_RADIOBUTTON:
    	case SWING_TAB:
    	case SWING_TABLE:
    	case SWING_TEXTINPUT:
    	case SWING_TREE:
		default:
			return true;
    	
    	}
    	
    }

    /**
     * Skip technical Swing components.
     * 
     * @param component
     *            the component to validate.
     * 
     * @return <b>true</b> if the component has to be skipped, <b>false</b> if not.
     */
    private boolean isSkip(Component component) {

        if (component == null) {
            return false;
        }

        boolean skip = false;

        skip |= component instanceof JRootPane;
        skip |= component instanceof JLayeredPane;
        skip |= component instanceof JViewport;
        skip |= component instanceof JScrollPane;

        return skip;
    }
}
