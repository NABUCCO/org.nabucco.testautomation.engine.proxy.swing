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

import java.awt.Component;

import javax.swing.JTree;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.event.SwingComponentEventCreator;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.MultipleEntriesFoundException;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.property.facade.datatype.util.PropertyHelper;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessTree
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingProcessTree extends SwingProcessComponentSupport implements SwingProcessComponent {

	private static final long	serialVersionUID	= 1L;

	private static final String	TREE_PATH_SEPARATOR	= "|";

	@Override
	public void internalExecute(PropertyList propertyList, Metadata metadata, SwingActionType actionType) {
		if (actionType == SwingActionType.IS_AVAILABLE) {
			checkAvailability(propertyList, metadata);
			return;
		}
		Component component = null;
		try {
			component = find(propertyList, metadata);
		} catch (MultipleEntriesFoundException e) {
			this.failure("More than one Component found.");
		}
		if (component == null) {
			this.failure("SwingTree not found.");
			return;
		}
		if (!(component instanceof JTree)) {
			this.failure("Component is not of type JTree but " + component.getClass().getName());
			return;
		}
		JTree tree = (JTree) component;
		switch (actionType) {
			case LEFTCLICK:
				leftClickInTree(tree, PropertyHelper.toString(PropertyHelper.getFromList(propertyList, TREEPATH)));
				break;
			case DOUBLECLICK:
				doubleClickInTree(tree, PropertyHelper.toString(PropertyHelper.getFromList(propertyList, TREEPATH)));
				break;
			case RIGHTCLICK:
				rightClickInTree(tree, PropertyHelper.toString(PropertyHelper.getFromList(propertyList, TREEPATH)));
				break;
			case READPROPERTY:
				TextProperty returnValue = getProperty(component, ((TextProperty) PropertyHelper.getFromList(propertyList, NAME)).getValue().getValue());
				if (returnValue != null) {
					this.addProperty(returnValue);
				}
				break;
			case READALLPROPERTIES:
				PropertyList properties = getProperties(component);
				this.addProperty(properties);
				break;
		}
	}

	private void leftClickInTree(JTree tree, String path) {
		try {
			SwingComponentEventCreator.createTreeClickEvent(tree, path.split("\\" + TREE_PATH_SEPARATOR));
		} catch (ProcessInvocationException e) {
			this.raiseException(e);
		}
	}

	private void rightClickInTree(JTree tree, String path) {
		try {
			SwingComponentEventCreator.createTreeRightClickEvent(tree, path.split("\\" + TREE_PATH_SEPARATOR));
		} catch (ProcessInvocationException e) {
			this.raiseException(e);
		}
	}

	private void doubleClickInTree(JTree tree, String path) {
		try {
			SwingComponentEventCreator.createTreeDoubleClickEvent(tree, path.split("\\" + TREE_PATH_SEPARATOR));
		} catch (ProcessInvocationException e) {
			this.raiseException(e);
		}
	}

	@Override
	boolean isAvailable(Component component) {
		return component instanceof JTree && ((JTree) component).isEnabled();
	}
}
