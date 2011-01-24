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
import java.util.List;

import javax.swing.JFrame;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessFrame
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
class SwingProcessFrame extends SwingProcessComponentSupport implements SwingProcessComponent {

    private static final long serialVersionUID = 1L;

	@Override
	public void internalExecute(PropertyList propertyList,
			List<Metadata> metadataList, SwingActionType actionType) {

		BooleanProperty prop = (BooleanProperty) propertyList.getPropertyList().get(0).getProperty();

		switch (actionType) {

		case IS_AVAILABLE:
			checkAvailability(metadataList, prop);
			break;
		}

	}

    @Override
	boolean isAvailable(Component component) {
		return component instanceof JFrame && !((JFrame) component).isVisible();
	}

}
