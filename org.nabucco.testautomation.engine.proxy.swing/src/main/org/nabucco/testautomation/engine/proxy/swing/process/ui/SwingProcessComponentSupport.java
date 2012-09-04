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
package org.nabucco.testautomation.engine.proxy.swing.process.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.WindowMonitor;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.ExceptionReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.FinalReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.LogReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.PropertyReply;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.MultipleEntriesFoundException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;
import org.nabucco.testautomation.property.facade.datatype.BooleanProperty;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessComponentSupport
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
abstract class SwingProcessComponentSupport extends JFCTestCase implements SwingProcessComponent, Serializable {

	private static final long	serialVersionUID	= 1L;

	private List<CommandReply>	replyList			= new ArrayList<CommandReply>();

	public SwingProcessComponentSupport() {
		super("Simulation");
	}
	
	private static ArrayList<Object> objects = new ArrayList<Object>();

	@Override
	public void execute(PropertyList propertyList, Metadata metadata, SwingActionType actionType) {
		initJFC();
		try {
			SwingProcessComponentTestThread swingThread = new SwingProcessComponentTestThread(propertyList, metadata, actionType, this);
			super.runCode(swingThread);
		} catch (Throwable t) {
			this.log("Error during test simulation.", new Exception(t), Level.ERROR, false);
			this.raiseException(new Exception(t));
		}
	}

	/**
	 * Initializes JFC components.
	 */
	private void initJFC() {
		setName("Simulation");
		setHelper(new JFCTestHelper());
		WindowMonitor.start();
	}

	abstract void internalExecute(PropertyList propertyList, Metadata metadata, SwingActionType actionType);

	@Override
	public List<CommandReply> getReplyList() {
		return Collections.unmodifiableList(replyList);
	}

	/**
	 * Adds a {@link PropertyReply} to the reply list.
	 * 
	 * @param property
	 *            the property for the {@link PropertyReply}
	 */
	void addProperty(Property property) {
		PropertyReply reply = new PropertyReply();
		reply.setProperty(property);
		this.replyList.add(reply);
	}

	/**
	 * Adds a {@link LogReply} to the reply list.
	 * 
	 * @param message
	 *            the message to log
	 * @param e
	 *            the exception to log
	 * @param level
	 *            the log level
	 * @param success
	 *            defines the test result, <b>true</b> for a successful execution, <b>false</b> for
	 *            a failed execution
	 */
	void log(String message, Exception e, Level level, boolean success) {
		LogReply reply = new LogReply();
		reply.setMessage(message);
		reply.setException(e);
		reply.setLevel(level.toInt());
		reply.setSuccess(success);
		if (e != null) {
			StringBuilder stackTrace = new StringBuilder();
			for (StackTraceElement stackTraceElement : e.getStackTrace()) {
				stackTrace.append(stackTraceElement);
				stackTrace.append("\n");
			}
			reply.setStacktrace(stackTrace.toString());
		}
		this.replyList.add(reply);
	}

	void failure(String message) {
		FinalReply reply = new FinalReply();
		reply.setMessage(message);
		reply.setSuccess(Boolean.FALSE);
		this.replyList.add(reply);
	}

	/**
	 * Adds a {@link LogReply} to the reply list.
	 * 
	 * @param message
	 *            the message to log
	 * @param e
	 *            the exception to log
	 * @param level
	 *            the log level
	 * @param success
	 *            defines the test result, <b>true</b> for a successful execution, <b>false</b> for
	 *            a failed execution
	 */
	void raiseException(Exception e) {
		ExceptionReply reply = new ExceptionReply();
		reply.setException(e);
		this.replyList.add(reply);
	}

	void checkAvailability(PropertyList propertyList, Metadata metadata) {
		BooleanProperty prop = PropertyHelper.createBooleanProperty(AVAILABLE, false);
		try {
			Component component = find(propertyList, metadata);
			if (component == null) {
				prop.setValue(Boolean.FALSE);
			} else if (isAvailable(component)) {
				prop.setValue(Boolean.TRUE);
			} else {
				prop.setValue(Boolean.FALSE);
			}
		} catch (Exception e) {
			this.log("Eror while checking availability", e, Level.ERROR, false);
			prop.setValue(Boolean.FALSE);
		}
		this.addProperty(prop);
	}

	protected static PropertyList getProperties(Component component) {
		Method[] methods = component.getClass().getMethods();
		PropertyList returnList = PropertyHelper.createPropertyList(PROPERTIES);
		for (Method method : methods) {
			if (method.getName().startsWith("get") && method.getGenericParameterTypes().length == 0 && method.getReturnType() != Void.TYPE) {
				Object obj = null;
				try {
					obj = method.invoke(component, new Object[0]);
				} catch (Exception e) {}
				if (obj != null) {
					String object = objectToString(obj);
					if (object != null) {
						PropertyHelper.add(PropertyHelper.createTextProperty(method.getName().substring(3), object), returnList);
					}
				}
			}
		}
		return returnList;
	}
	
	protected static TextProperty getProperty(Component component, String name) {
		Method[] methods = component.getClass().getMethods();
		for (Method method : methods) {
			if (method.getName().equalsIgnoreCase("get" + name) && method.getGenericParameterTypes().length == 0 && method.getReturnType() != Void.TYPE) {
				Object obj = null;
				try {
					obj = method.invoke(component, new Object[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (obj != null) {
					String object = objectToString(obj);
					if (object != null) {
						return PropertyHelper.createTextProperty(CONTENT, object);
					}
				}
			}
		}
		return null;
	}
	
    
	protected void executeLeftClick(JComponent component) {
		SwingComponentEventCreator.createComponentClickEvent(component);
	}
	
	protected void executeDoubleClick(JComponent component) {
		SwingComponentEventCreator.createComponentDoubleClickEvent(component);
	}

	protected void executeRightClick(JComponent component) {
		SwingComponentEventCreator.createComponentRightClickEvent(component);
	}
	private static String objectToString(Object obj) {
		String object = obj.toString();
		object = object.trim();
		if (object.length() == 0) return null; 
		return object;
	}
	
	protected static Component find(PropertyList propertyList, Metadata metadata) throws MultipleEntriesFoundException {
		Component component = SwingComponentFinder.getInstance().findComponent(propertyList, metadata);
		if(component != null) {
			Container root = getRoot(component);
			bringToFront(root);
		}
		return component;
	}
	
	/**
	 * 
	 * @param container
	 */
	private static void bringToFront(Container container) {
		if(container != null) {
			if(container instanceof Window) {
				Window window = (Window) container;
				window.toFront();
			}
		}
	}

	private static Container getRoot(Component component) {
		Component parent = component;
		Container root = null;
		while(root == null) {
			Container temp = parent.getParent();
			if(temp == null) {
				root = (Container) parent;
			} else {
				parent = temp;
			}
		}
		
		
		
		// TEST
		
		if(root != null) {
			if(!objects.contains(root)) {
				root.addHierarchyListener(new HierarchyListener() {
					
					@Override
					public void hierarchyChanged(HierarchyEvent evt) {
						evt.getComponent();
					}
				});
				root.addMouseListener(new MouseListener() {
					
					@Override
					public void mouseReleased(MouseEvent evt) {
						System.out.println("RELEASED");
						Component comp = evt.getComponent();
						Component parent = comp;
						String list = parent.getClass().getSimpleName();
						while(parent.getParent() != null) {
							parent = parent.getParent();
							list = parent.getClass().getSimpleName() + "->" + list;
						}
						System.err.println(list);
						Color temp = comp.getBackground();
						comp.setBackground(Color.RED);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						comp.setBackground(temp);
					}
					
					@Override
					public void mousePressed(MouseEvent evt) {
						System.out.println("PRESSED");
						Component comp = evt.getComponent();
						Component parent = comp;
						String list = parent.getClass().getSimpleName();
						while(parent.getParent() != null) {
							parent = parent.getParent();
							list = parent.getClass().getSimpleName() + "->" + list;
						}
						System.err.println(list);
						Color temp = comp.getBackground();
						comp.setBackground(Color.RED);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						comp.setBackground(temp);
					}
					
					@Override
					public void mouseExited(MouseEvent evt) {
						System.out.println("EXITED");
						Component comp = evt.getComponent();
						Component parent = comp;
						String list = parent.getClass().getSimpleName();
						while(parent.getParent() != null) {
							parent = parent.getParent();
							list = parent.getClass().getSimpleName() + "->" + list;
						}
						System.err.println(list);
					}
					
					@Override
					public void mouseEntered(MouseEvent evt) {
						System.out.println("ENTERED");
						Component comp = evt.getComponent();
						Component parent = comp;
						String list = parent.getClass().getSimpleName();
						while(parent.getParent() != null) {
							parent = parent.getParent();
							list = parent.getClass().getSimpleName() + "->" + list;
						}
						System.err.println(list);
					}
					
					@Override
					public void mouseClicked(MouseEvent evt) {
						System.out.println("CLICKED");
						Component comp = evt.getComponent();
						Component parent = comp;
						String list = parent.getClass().getSimpleName();
						while(parent.getParent() != null) {
							parent = parent.getParent();
							list = parent.getClass().getSimpleName() + "->" + list;
						}
						System.err.println(list);
						Color temp = comp.getBackground();
						comp.setBackground(Color.RED);
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						comp.setBackground(temp);
					}
				});
			}
		}
		
		//TEST
		
		
		
		
		return root;
	}

	abstract boolean isAvailable(Component component);
}
