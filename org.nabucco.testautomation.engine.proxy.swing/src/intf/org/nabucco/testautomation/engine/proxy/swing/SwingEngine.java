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

import org.nabucco.testautomation.engine.proxy.SubEngine;

/**
 * SwingEngine
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public interface SwingEngine extends SubEngine {

	/**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing applet.
     * 
     * @return the SwingApplet
     */
    SwingApplet getSwingApplet();
	
    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing application.
     * 
     * @return the SwingApplication
     */
    SwingApplication getSwingApplication();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing button.
     * 
     * @return the SwingButton
     */
    SwingButton getSwingButton();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing text input field.
     * 
     * @return the SwingTextInput
     */
    SwingTextInput getSwingTextInput();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing password field.
     * 
     * @return the SwingPassword
     */
    SwingPassword getSwingPassword();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing dialog.
     * 
     * @return the SwingDialog
     */
    SwingDialog getSwingDialog();
    
    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing frame.
     * 
     * @return the SwingFrame
     */
    SwingFrame getSwingFrame();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing check box.
     * 
     * @return the SwingCheckbox
     */
    SwingCheckbox getSwingCheckBox();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing radio button.
     * 
     * @return the SwingRadioButton
     */
    SwingRadioButton getSwingRadioButton();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing tab.
     * 
     * @return the SwingTab
     */
    SwingTab getSwingTab();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing table.
     * 
     * @return the SwingTable
     */
    SwingTable getSwingTable();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing tree.
     * 
     * @return the SwingTree
     */
    SwingTree getSwingTree();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing menu.
     * 
     * @return the SwingMenu
     */
    SwingMenu getSwingMenu();

    /**
     * Gets an implementation of {@link SwingEngineOperation}´
     * to access a swing combobox.
     * 
     * @return the SwingComboBox
     */
    SwingComboBox getSwingComboBox();
    
}
