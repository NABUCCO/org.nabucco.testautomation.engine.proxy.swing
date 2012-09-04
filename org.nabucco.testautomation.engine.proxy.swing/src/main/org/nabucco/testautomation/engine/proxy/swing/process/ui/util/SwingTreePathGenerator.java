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
package org.nabucco.testautomation.engine.proxy.swing.process.ui.util;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;

/**
 * SwingTreePathGenerator
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingTreePathGenerator {

	private static final String				METHOD_GETHANDLER	= "getCdrHandler";

	private static final String				METHOD_GETVALUEOBJECT		= "getValueObject";
	
	private static final String				METHOD_GETVALUE		= "getValue";
	
	private static final String				METHOD_GETLABEL		= "getLabel";

	/**
	 * Singleton instance.
	 */
	private static SwingTreePathGenerator	instance			= new SwingTreePathGenerator();

	/**
	 * Private constructor.
	 */
	private SwingTreePathGenerator() {
	}

	/**
	 * Singleton access.
	 * 
	 * @return the SwingTreePathGenerator instance.
	 */
	public static SwingTreePathGenerator getInstance() {
		return instance;
	}

	/**
	 * Creates a {@link TreePath} by an array of displayed names.
	 * 
	 * @param stringPath
	 *            the path tokens
	 * @param tree
	 *            the tree
	 * 
	 * @return the tree path pointing on the specified path
	 * 
	 * @throws ProcessInvocationException
	 */
	public TreePath getTreePath(final String[] stringPath, final JTree tree) throws ProcessInvocationException {
		if (tree == null) {
			throw new IllegalArgumentException("Tree must not be null.");
		}
		if (stringPath.length == 0) {
			return null;
		}
		List<Object> path = new ArrayList<Object>();
		TreeModel model = tree.getModel();
		Object node = model.getRoot();
		path.add(node);
		for (int i = 0; i < stringPath.length; i++) {
			String text = stringPath[i];
			boolean found = false;
			int children;
			children = model.getChildCount(node);
			for (int j = 0; (j < children) && !found; j++) {
				Object newnode = model.getChild(node, j);
				String name = getName(newnode);
				if (text.equals(name)) {
					node = newnode;
					path.add(node);
					found = true;
				}
			}
			if (!found) {
				return null;
			}
		}
		return new TreePath(path.toArray());
	}

	private String getName(Object node) throws ProcessInvocationException {
		try {
			Method method = node.getClass().getMethod(METHOD_GETHANDLER);
			Object handler = method.invoke(node);
			method = handler.getClass().getMethod(METHOD_GETVALUEOBJECT);
			Object name = method.invoke(handler);
			if(name == null) {
				method = handler.getClass().getMethod(METHOD_GETVALUE);
				name = method.invoke(handler);
			}
			if(name == null) {
				method = handler.getClass().getMethod(METHOD_GETLABEL);
				name = method.invoke(handler);
			}
//			ArrayList<String> strings = new ArrayList<String>();
//			for (Method m : handler.getClass().getMethods()) {
//				if (m.getName().startsWith("get") && m.getGenericParameterTypes().length == 0 && m.getReturnType() != Void.TYPE) {
//					String mn = m.getName();
//					try {
//						String mv = m.invoke(handler).toString();
//						String mcomp = mn + "=" + mv;
//						strings.add(mcomp);
//						System.err.println(mcomp);
//					} catch(Exception e) {}
//				}
//			}
			if (name == null) {
				return null;
			}
			return name.toString();
		} catch (Exception e) {
			throw new ProcessInvocationException("Error retrieving swing tree node labels.", e);
		}
	}
}
