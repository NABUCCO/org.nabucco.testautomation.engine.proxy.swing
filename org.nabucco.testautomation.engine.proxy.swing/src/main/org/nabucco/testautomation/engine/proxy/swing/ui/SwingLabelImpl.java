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
package org.nabucco.testautomation.engine.proxy.swing.ui;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;
import org.nabucco.testautomation.engine.proxy.swing.SwingLabel;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;
import org.nabucco.testautomation.engine.proxy.swing.ui.validator.SwingComponentConstraints;


/**
 * SwingLabelImpl
 * 
 * @author Florian Schmidt, PRODYNA AG
 */
public class SwingLabelImpl extends AbstractSwingComponent implements SwingLabel{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= -6687735661209000120L;

	public SwingLabelImpl(ProcessCommunication communication) {
		super(communication, SwingEngineOperationType.SWING_LABEL);
	}

	@Override
	protected void defineConstraints(SwingComponentConstraints constraints, SwingActionType actionType) {
		constraints.actions(SwingActionType.IS_AVAILABLE, SwingActionType.READ);
	}
	
}
