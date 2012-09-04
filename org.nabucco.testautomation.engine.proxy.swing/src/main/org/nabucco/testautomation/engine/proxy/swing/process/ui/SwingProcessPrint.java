/*
 * Copyright 2012 PRODYNA AG
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

import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.AllWindowLikeElementFinder;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.MultipleEntriesFoundException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessPrintTree
 * 
 * @author Florian Schmidt, PRODYNA AG
 */
public class SwingProcessPrint extends SwingProcessComponentSupport {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	void internalExecute(PropertyList propertyList, Metadata metadata, SwingActionType actionType) {
		String text = "";
		switch (actionType) {
			case READ:
				List<Object> comps = new ArrayList<Object>();
				TextProperty path = (TextProperty) PropertyHelper.getFromList(propertyList, PATH);
				if(path == null || path.getValue().getValue().trim().equals("")) {
					comps.addAll(new AllWindowLikeElementFinder().findAll());
				} else {
					try {
						comps.add(find(propertyList, metadata));
					} catch (MultipleEntriesFoundException e) {
						e.printStackTrace();
					}
				}
				int i = 0;
				for (Object item : comps) {
					if (item instanceof Container) {
						text += printTree((Container) item, i++ + "[" + item.getClass().getSimpleName() + "(" + findSwingOrAWTSuperclass(item) + ")" + "," + getPropsString((Container) item) + "]\n", 1);
					}
				}
		}
		TextProperty property = (TextProperty) PropertyHelper.createTextProperty(CONTENT, text);
		this.addProperty(property);
	}

	/**
	 * 
	 * @param container
	 */
	private String printTree(Container container, String pre, int indent) {
		String path = "";
		if (container != null) {
			int count = container.getComponentCount();
			for (int i = 0; i < count; i++) {
				Component component = container.getComponent(i);
				String props = getPropsString(component);
				if (component instanceof Container) {
					int compcount = ((Container) component).getComponentCount();
					if (compcount > 0) {
						path += printTree((Container) component, pre
								+ getIndent(indent) + i + "[" + (component.getClass().getSimpleName().equals("") ? component.getClass().getGenericSuperclass().toString().split("\\.")[component.getClass().getGenericSuperclass().toString().split("\\.").length - 1] : component.getClass().getSimpleName()) + "(" + findSwingOrAWTSuperclass(component) + ")" + "," + props + "]\n", indent + 1);
					} else {
						path += pre + getIndent(indent) + i + "[" + (component.getClass().getSimpleName().equals("") ? component.getClass().getGenericSuperclass().toString().split("\\.")[component.getClass().getGenericSuperclass().toString().split("\\.").length - 1] : component.getClass().getSimpleName()) + "(" + findSwingOrAWTSuperclass(component) + ")" + "," + props + "]\n";
					}
				} else {
					path += pre + i + "[" + (component.getClass().getSimpleName().equals("") ? component.getClass().getGenericSuperclass().toString().split("\\.")[component.getClass().getGenericSuperclass().toString().split("\\.").length - 1] : component.getClass().getSimpleName()) + "(" + findSwingOrAWTSuperclass(component) + ")" + "," + props + "]\n";
				}
			}
		}
		return path;
	}

	private static String getIndent(int indent) {
		String s = "";
		for (int i = 0; i < indent; i++) {
			s += "\t";
		}
		return s;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	boolean isAvailable(Component component) {
		return true;
	}
	
	
	
	private static String getPropsString(Component component) {
		Method[] methods = component.getClass().getMethods();
		String s = "";
		for (Method method : methods) {
			if (method.getName().startsWith("get") && method.getGenericParameterTypes().length == 0 && method.getReturnType() == String.class) {
				if(!(method.getName().toLowerCase().contains("name") || method.getName().toLowerCase().contains("text") || method.getName().toLowerCase().contains("label") || method.getName().toLowerCase().contains("value"))) continue;
				Object obj = null;
				try {
					obj = method.invoke(component, new Object[0]);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (obj != null) {
					String object = objectToString(obj);
					if (object != null) {
						s += method.getName().substring(3) + "=" + object + ",";
					}
				}
			}
		}
		if (s.length() > 0) {
			s = s.substring(0, s.length() - 1);
		}
		return s;
	}
	
	private static String objectToString(Object obj) {
		String object = (obj + "");
		object = object.trim();
		if (object.length() == 0) return null; 
		return object;
	}
	
	String findSwingOrAWTSuperclass(Object o) {
		Class<?> find = findSwingOrAWTSuperclass(o.getClass());
		if(find == null) {
			return "";
		} else {
			return (find.getSimpleName().equals("") ? find.getGenericSuperclass().toString().split("\\.")[find.getGenericSuperclass().toString().split("\\.").length - 1] : find.getSimpleName());
		}
	}
	
	Class<?> findSwingOrAWTSuperclass(Class<?> c) {
		if(c.getPackage().getName().startsWith("javax.swing") || c.getPackage().getName().startsWith("java.awt")) {
			return c;
		}
		Class<?> superclass = c.getSuperclass();
		if(superclass != Object.class) {
			Class<?> swingOrAWTSuperclass = findSwingOrAWTSuperclass(superclass);
			if(swingOrAWTSuperclass != null) {
				return swingOrAWTSuperclass;
			}
		}
		Class<?>[] interfaces = c.getInterfaces();
		for(Class<?> interfaze: interfaces) {
			Class<?> find = findSwingOrAWTSuperclass(interfaze);
			if(find != null) {
				return find;
			}
		}
		return null;
	}
}
