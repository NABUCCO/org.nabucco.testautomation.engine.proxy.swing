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

import org.nabucco.testautomation.engine.proxy.swing.SwingEngineOperationType;

/**
 * SwingProcessComponentFactory
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingProcessComponentFactory {

    /**
     * Singleton instance.
     */
    private static SwingProcessComponentFactory instance = new SwingProcessComponentFactory();

    /**
     * Private constructor.
     */
    private SwingProcessComponentFactory() {
    }

    /**
     * Singleton access.
     * 
     * @return the SwingProcessComponentFactory instance.
     */
    public static SwingProcessComponentFactory getInstance() {
        return instance;
    }

    /**
     * Returns the {@link SwingProcessComponent} for the related {@link SwingEngineOperationType}.
     * 
     * @param type
     *            type of the component
     * 
     * @return the component instance.
     */
    public SwingProcessComponent getSwingProcessComponent(SwingEngineOperationType type) {

        switch (type) {

        case SWING_BUTTON:
            return new SwingProcessButton();
        case SWING_TEXTINPUT:
            return new SwingProcessText();
        case SWING_PASSWORD:
            return new SwingProcessText();
        case SWING_CHECKBOX:
            return new SwingProcessToggleButton();
        case SWING_RADIOBUTTON:
            return new SwingProcessToggleButton();
        case SWING_MENU:
            return new SwingProcessMenu();
        case SWING_TAB:
            return new SwingProcessTab();
        case SWING_TABLE:
            return new SwingProcessTable();
        case SWING_TREE:
            return new SwingProcessTree();
        case SWING_DIALOG:
            return new SwingProcessDialog();
        case SWING_COMBOBOX:
        	return new SwingProcessComboBox();
        case SWING_FRAME:
        	return new SwingProcessFrame();
        default:
            throw new IllegalArgumentException("SwingEngineOperationType '" + type + "' is not supported.");
        }
    }
}
