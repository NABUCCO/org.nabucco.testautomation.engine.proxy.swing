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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JWindow;

import junit.extensions.jfcunit.finder.ComponentFinder;
import junit.extensions.jfcunit.finder.DialogFinder;
import junit.extensions.jfcunit.finder.FrameFinder;
import junit.extensions.jfcunit.finder.JWindowFinder;

import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * Finds {@link JComponent} in a running Swing application.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingComponentFinder {

	// private static final String COMPONENT_INDEX = "COMPONENT_INDEX";
	private static final String			PATH	= "PATH";

	/**
	 * Singleton instance.
	 */
	private static SwingComponentFinder	instance		= new SwingComponentFinder();

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
	 * Finds a {@link JFrame} in an application.
	 * 
	 * @param index
	 *            index of the frame in the component tree
	 * 
	 * @return the frame or null
	 */
	public List<?> findFrames() {
		FrameFinder finder = new FrameFinder(null);
		List<?> component = finder.findAll();
		return component;
	}

	/**
	 * Finds a {@link JDialog} in a parent component.
	 * 
	 * @param index
	 *            index of the dialog in the component tree
	 * 
	 * @return the dialog or null
	 */
	public List<?> findDialogs(Container parent) {
		DialogFinder finder = new DialogFinder(null);
		finder.setWait(0);
		if(parent != null) {
			List<?> component = finder.findAll(parent);
			return component;			
		} else {
			List<?> component = finder.findAll();
			return component;
		}
	}
	
	/**
	 * Finds a {@link JWindow} in a parent component.
	 * 
	 * @param index
	 *            index of the dialog in the component tree
	 * 
	 * @return the dialog or null
	 */
	public List<?> findWindows(Container parent) {
		JWindowFinder finder = new JWindowFinder();
		finder.setWait(0);
		if(parent != null) {
			List<?> component = finder.findAll(parent);
			return component;			
		} else {
			List<?> component = finder.findAll();
			return component;
		}
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
	 * @param metadata
	 *            the list of {@link Metadata} elements pointing to the component
	 * 
	 * @return the component or null
	 * @throws MultipleEntriesFoundException 
	 */
	public Component findComponent(PropertyList propertyList, Metadata metadata) throws MultipleEntriesFoundException {
		Component component = null;
		if (isSwing(metadata)) {
			for(int i = 0; i < 10; i++) {
				component = find(propertyList, metadata);
				if(component != null) break;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		if (component != null) {
			System.out.println("Found Swing Component: " + component.getClass().getSimpleName());
		} else {
			System.out.println("No Swing Component found for " + metadata.getId());
		}
		return component;
	}

	/**
	 * 
	 * @param metadata
	 * @return
	 * @throws Exception 
	 */
	private Component find(PropertyList propertyList, Metadata metadata) throws MultipleEntriesFoundException {
		TextProperty pathProperty = (TextProperty)PropertyHelper.getFromList(propertyList, PATH);
		if(pathProperty == null) {
			pathProperty = (TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(), PATH);
		}
		String path = pathProperty.getValue().getValue();
		String[] pathTokens = path.split(",");
		int selector = -1;
		if(pathTokens[pathTokens.length-1].matches("\\[\\d+\\]")) {
			selector = Integer.parseInt(pathTokens[pathTokens.length-1].substring(1, pathTokens[pathTokens.length-1].length()-1));
			pathTokens = Arrays.copyOfRange(pathTokens, 0, pathTokens.length-1);
		}
		int index = 0;
		List<?> comps = null;
		if (pathTokens[index].equalsIgnoreCase("W")) {
			comps = findWindows(null);
			logFound(comps, "W");
			index++;
		} else if (pathTokens[index].equalsIgnoreCase("F")) {
			comps = findFrames();
			logFound(comps, "F");
			index++;
		} else if (pathTokens[index].equalsIgnoreCase("D")) {
			comps = findDialogs(null);
			logFound(comps, "D");
			index++;
		} else {
			comps = new AllWindowLikeElementFinder().findAll();
			logFound(comps, "AllWindowLikeElements");
		}
		List<Component> components = new ArrayList<Component>();
		for(Object object: comps) {
			components.add((Component) object);
		}
//		try {
//			int number = Integer.parseInt(pathTokens[index]);
//			container = (Container) comps.get(number);
//			components.add(container);
//			logFound(components, pathTokens[index]);
//			index++;
//		} catch(NumberFormatException e) {
//			for(Object object: comps) {
//				Component component = (Component) object;
//				if(matchParameter(component, pathTokens[index])) {
//					container = (Container) component;
//					components.add(container);
//				}
//			}
//			logFound(components, pathTokens[index]);
//			index++;	
//		}
		{
			List<Component> found = new ArrayList<Component>();
			for (Component component : components) {
				found.addAll(findByToken(component, pathTokens[index], new ArrayList<Component>()));
			}
			logFound(found, pathTokens[index++]);
			components = found;
		}
		
		if (components.size() == 0) {
			return null;
		}
		for (int i = index; i < pathTokens.length; i++) {
			List<Component> found = new ArrayList<Component>();
			for (Component component : components) {
				found.addAll(findByToken(component, pathTokens[i], components));
			}
			logFound(found, pathTokens[i]);
			components = found;
		}
		if (components.size() == 0) {
			return null;
		} else if(selector != -1) {
			if(components.size() <= selector) {
				throw new MultipleEntriesFoundException("Selected component " + selector + " but found only " + components.size() + " components.");
			} else {
				return components.get(selector);
			}
		} else if(components.size() == 1) {
			return components.get(0);
		} else {
			throw new MultipleEntriesFoundException("Found " + components.size() + " components instead of 1.");
		}
	}
	
	static void logFound(List<?> comps, String token) {
		if(comps.size() > 0) {
			System.err.println("Found Element(s) for \""+ token + "\":");
			for(Object o: comps) {
				System.err.println(o.toString());
			}
		} else {
			System.err.println("No Element(s) found for \""+ token + "\":");
		}
	}

	private List<Component> findByToken(Component comp, String token, List<Component> alreadyFound) {
		List<Component> componentList = new ArrayList<Component>();
		if (comp != null) {
			if(token.startsWith("x")) {
				componentList.addAll(xMatch(comp, token));
			} else if ((!alreadyFound.contains(comp)) && match(comp, token)) {
				componentList.add(comp);
			} else if (comp instanceof Container) {
				Container cont = (Container) comp;
				int count = cont.getComponentCount();
				for (int i = 0; i < count; i++) {
					Component component = cont.getComponent(i);
					componentList.addAll(findByToken(component, token, alreadyFound));
				}
			}
		}
		return componentList;
	}
	
	private List<Component> xMatch(Component comp, String token) {
		List<Component> list = new ArrayList<Component>();
		// schlechter Code! muss bei gelegenheit neu geschrieben werden
		
		String[] tokenParts1 = token.split("::", 2);
		String[] tokenParts2 = tokenParts1[0].split(":", 2);
		String name = tokenParts2[1];
		String value = tokenParts1[1];
		Component c = null;
		if(name.equalsIgnoreCase("parent")) {
			if(value.equalsIgnoreCase("node()")) {
				c = comp.getParent();
			}
		} else if(name.equalsIgnoreCase("following-sibling")) {
			if(value.equalsIgnoreCase("node()")) {
				Container parent = comp.getParent();
				Component[] siblings = parent.getComponents();
				for(int i = 0; i < siblings.length; i++) {
					if(siblings[i] == comp && i < siblings.length - 1) {
						c = siblings[i + 1];
						break;
					}
				}
			}
		} else if(name.equalsIgnoreCase("preceding-sibling")) {
			if(value.equalsIgnoreCase("node()")) {
				Container parent = comp.getParent();
				Component[] siblings = parent.getComponents();
				for(int i = 0; i < siblings.length; i++) {
					if(siblings[i] == comp && i > 0) {
						c = siblings[i - 1];
						break;
					}
				}
			}
		}
		if(c != null) {
			list.add(c);
		}
		
		return list;
	}
	

	/**
	 * 
	 * @param component
	 * @param token
	 * @return
	 */
	private boolean match(Component component, String token) {
		if (token.contains("=")) {
			return matchParameter(component, token);
		} else {
			return matchPosition(component, token);
		}
	}

	private boolean matchPosition(Component component, String token) {
		int i = Integer.parseInt(token);
		Container parent = component.getParent();
		if (parent == null)
			return false;
		int position = Arrays.asList(parent.getComponents()).indexOf(component);
		return i == position;
	}

	private boolean matchParameter(Component component, String token) {
		String[] tokenParts1 = token.split("=", 2);
		String[] tokenParts2 = tokenParts1[0].split(":", 2);
		String check = tokenParts2[0];
		String name = tokenParts2[1];
		String value = tokenParts1[1];
		Method[] methods = component.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && method.getGenericParameterTypes().length == 0 && method.getReturnType() != Void.TYPE) {
				if (method.getName().substring(3).equalsIgnoreCase(name)) {
					Object obj = null;
					try {
						obj = method.invoke(component, new Object[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(obj == null) return false;
					String content = obj.toString().trim();
					if(check.equals("e")) {
						return content.equalsIgnoreCase(value);
					} else if(check.equals("c")) {
						return content.toLowerCase().contains(value.toLowerCase());
					}
				}
			}
		}
		return false;
	}

	// /**
	// * Find a component in a parent component for a given {@link Metadata} object.
	// *
	// * @param container
	// * the parent component
	// * @param metadata
	// * the {@link Metadata} object
	// *
	// * @return the component or null
	// */
	// private Container findMetadataComponent(Container container, Metadata metadata) {
	//
	//
	//
	//
	//
	//
	//
	//
	//
	//
	// NumericProperty componentIndex = (NumericProperty)
	// PropertyHelper.getFromList(metadata.getPropertyList(), COMPONENT_INDEX);
	// Integer index;
	//
	// if (componentIndex != null) {
	// index = componentIndex.getValue().getValue().intValue();
	// } else {
	// String path = ((TextProperty) PropertyHelper.getFromList(metadata.getPropertyList(),
	// COMPONENT_PATH)).getValue().getValue();
	// String[] pathTokens = path.split(",");
	//
	// if (pathTokens == null || pathTokens.length == 0) {
	// System.out.println(COMPONENT_PATH + " is not valid.");
	// return null;
	// }
	//
	// for (int i = 0; i < pathTokens.length - 1; i++) {
	// String token = pathTokens[i];
	//
	// container = findByIndex(container, null, Integer.parseInt(token));
	// }
	// index = Integer.parseInt(pathTokens[pathTokens.length - 1]);
	// }
	// return findByIndex(container, metadata, index);
	// }
	// private String printTree2(Container container, String pre) {
	// String path = "";
	// int index = 0;
	// Container cont = findComponent(index, container);
	// if(cont != null) {
	// while(cont != null) {
	// path += printTree2(cont, pre + "." + index + "[" +
	// (cont.getClass().getSimpleName().equals("") ?
	// cont.getClass().getGenericSuperclass().toString().split("\\.")[cont.getClass().getGenericSuperclass().toString().split("\\.").length
	// - 1] : cont.getClass().getSimpleName()) + "," + cont.getName() +"]");
	// cont = findComponent(++index, cont);
	// }
	// } else {
	// return pre;
	// }
	// return path;
	// }
	//
	// private String printTree() {
	// Component c = new FrameFinder(null).find();
	// if(c instanceof Container) {
	// return printTree((Container) c, "");
	// } else {
	// return "";
	// }
	// }
	// private Container findByIndex(Container container, Metadata metadata, Integer index) {
	//
	// String name = metadata == null ? "null" : (metadata.getName() == null ? "null" :
	// metadata.getName().getValue());
	//
	// if (index == null) {
	// System.out.println("Index does not exist for metadata " + name);
	// return null;
	// }
	//
	// SwingEngineOperationType type = metadata == null ? null :
	// SwingEngineOperationType.valueOf(metadata.getOperation().getCode().getValue());
	//
	// if (type == SwingEngineOperationType.SWING_FRAME) {
	// container = findFrame(index);
	// } else if (type == SwingEngineOperationType.SWING_DIALOG) {
	// container = findDialog(index, null);
	// } else {
	//
	// if (container == null) {
	// System.out.println("Parent container does not exist for metadata " + name);
	// return null;
	// }
	//
	// if (container.getComponentCount() == 0) {
	// System.out.println("Parent container does contain metadata " + name);
	// return null;
	// }
	//
	// if (index > container.getComponentCount() - 1) {
	//
	// // Skip technical Swing components
	// while (isSkip(findComponent(0, container))) {
	// container = findComponent(0, container);
	// System.out.println("Skip Swing Component: "
	// + container.getClass().getSimpleName());
	// }
	// }
	//
	// container = findComponent(index, container);
	//
	// // Skip technical Swing components
	// while (isSkip(container)) {
	// System.out.println("Skip Swing Component: " + container.getClass().getSimpleName());
	// container = findComponent(0, container);
	// }
	// }
	//
	// return container;
	// }
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
	// /**
	// * Skip technical Swing components.
	// *
	// * @param component
	// * the component to validate.
	// *
	// * @return <b>true</b> if the component has to be skipped, <b>false</b> if not.
	// */
	// private boolean isSkip(Component component) {
	//
	// if (component == null) {
	// return false;
	// }
	//
	// boolean skip = false;
	//
	// skip |= component instanceof JRootPane;
	// skip |= component instanceof JLayeredPane;
	// skip |= component instanceof JViewport;
	// skip |= component instanceof JScrollPane;
	//
	// // return skip;
	// return false;
	// }
}
