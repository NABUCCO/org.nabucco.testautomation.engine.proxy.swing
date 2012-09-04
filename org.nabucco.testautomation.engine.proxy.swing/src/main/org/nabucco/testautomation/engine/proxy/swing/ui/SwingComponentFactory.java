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
package org.nabucco.testautomation.engine.proxy.swing.ui;

import org.nabucco.testautomation.engine.proxy.swing.SwingApplet;
import org.nabucco.testautomation.engine.proxy.swing.SwingApplication;
import org.nabucco.testautomation.engine.proxy.swing.SwingButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingCheckbox;
import org.nabucco.testautomation.engine.proxy.swing.SwingComboBox;
import org.nabucco.testautomation.engine.proxy.swing.SwingJComponent;
import org.nabucco.testautomation.engine.proxy.swing.SwingDialog;
import org.nabucco.testautomation.engine.proxy.swing.SwingFrame;
import org.nabucco.testautomation.engine.proxy.swing.SwingLabel;
import org.nabucco.testautomation.engine.proxy.swing.SwingMenu;
import org.nabucco.testautomation.engine.proxy.swing.SwingPassword;
import org.nabucco.testautomation.engine.proxy.swing.SwingPrint;
import org.nabucco.testautomation.engine.proxy.swing.SwingRadioButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingTab;
import org.nabucco.testautomation.engine.proxy.swing.SwingTable;
import org.nabucco.testautomation.engine.proxy.swing.SwingTextInput;
import org.nabucco.testautomation.engine.proxy.swing.SwingTree;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.swing.process.JavaProcess;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;


/**
 * SwingComponentFactory
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingComponentFactory {

    /**
     * Singleton instance.
     */
    private static SwingComponentFactory instance = new SwingComponentFactory();

    /**
     * Private constructor.
     */
    private SwingComponentFactory() {
    }

    /**
     * Singleton access.
     * 
     * @return the SwingComponentFactory instance.
     */
    public static synchronized SwingComponentFactory getInstance() {
        return instance;
    }
    
    /**
     * Creates a new {@link SwingApplet} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingApplet createSwingApplet(JavaProcess process, SwingProxyConfiguration config) {
        return new SwingAppletImpl(process, config);
    }

    /**
     * Creates a new {@link SwingApplication} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingApplication createSwingApplication(JavaProcess process, SwingProxyConfiguration config) {
        return new SwingApplicationImpl(process, config);
    }

    /**
     * Creates a new {@link SwingButton} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingButton createSwingButton(ProcessCommunication communication) {
        return new SwingButtonImpl(communication);
    }

    /**
     * Creates a new {@link SwingDialog} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingDialog createSwingDialog(ProcessCommunication communication) {
        return new SwingDialogImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingFrame} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingFrame createSwingFrame(ProcessCommunication communication) {
        return new SwingFrameImpl(communication);
    }

    /**
     * Creates a new {@link SwingTextInput} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingTextInput createSwingTextInput(ProcessCommunication communication) {
        return new SwingTextInputImpl(communication);
    }

    /**
     * Creates a new {@link SwingPassword} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingPassword createSwingPassword(ProcessCommunication communication) {
        return new SwingTextInputImpl(communication);
    }

    /**
     * Creates a new {@link SwingCheckbox} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingCheckbox createSwingCheckbox(ProcessCommunication communication) {
        return new SwingToggleButtonImpl(communication);
    }

    /**
     * Creates a new {@link SwingRadioButton} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingRadioButton createSwingRadioButton(ProcessCommunication communication) {
        return new SwingToggleButtonImpl(communication);
    }

    /**
     * Creates a new {@link SwingTree} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingTree createSwingTree(ProcessCommunication communication) {
        return new SwingTreeImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingPrint} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingPrint createSwingPrint(ProcessCommunication communication) {
    	return new SwingPrintImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingJComponent} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingJComponent createComponent(ProcessCommunication communication) {
    	return new SwingJComponentImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingJComponent} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance.
     */
    public SwingLabel createLabel(ProcessCommunication communication) {
    	return new SwingLabelImpl(communication);
    }

    /**
     * Creates a new {@link SwingTable} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingTable createSwingTable(ProcessCommunication communication) {
        return new SwingTableImpl(communication);
    }

    /**
     * Creates a new {@link SwingTab} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingTab createSwingTab(ProcessCommunication communication) {
        return new SwingTabImpl(communication);
    }

    /**
     * Creates a new {@link SwingMenu} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingMenu createSwingMenu(ProcessCommunication communication) {
        return new SwingMenuImpl(communication);
    }

    /**
     * Creates a new {@link SwingComboBox} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingComboBox createSwingComboBox(ProcessCommunication communication) {
        return new SwingComboBoxImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingJComponent} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingJComponent createSwingComponent(ProcessCommunication communication) {
    	return new SwingJComponentImpl(communication);
    }
    
    /**
     * Creates a new {@link SwingLabel} instance.
     * 
     * @param communication
     *            the process communication
     * 
     * @return the instance
     */
    public SwingLabel createSwingLabel(ProcessCommunication communication) {
    	return new SwingLabelImpl(communication);
    }

}
