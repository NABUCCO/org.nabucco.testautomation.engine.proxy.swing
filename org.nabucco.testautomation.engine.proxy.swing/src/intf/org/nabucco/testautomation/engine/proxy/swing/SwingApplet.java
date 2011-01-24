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
package org.nabucco.testautomation.engine.proxy.swing;

/**
 * SwingApplet
 *
 * @author Steffen Schmidt, PRODYNA AG
 */
public interface SwingApplet extends SwingEngineOperation {

	SwingButton getSwingButton();

	SwingCheckbox getSwingCheckBox();

	SwingComboBox getSwingComboBox();

	SwingDialog getSwingDialog();

	SwingFrame getSwingFrame();

	SwingMenu getSwingMenu();

	SwingPassword getSwingPassword();

	SwingRadioButton getSwingRadioButton();

	SwingTab getSwingTab();

	SwingTable getSwingTable();

	SwingTextInput getSwingTextInput();

	SwingTree getSwingTree();

}
