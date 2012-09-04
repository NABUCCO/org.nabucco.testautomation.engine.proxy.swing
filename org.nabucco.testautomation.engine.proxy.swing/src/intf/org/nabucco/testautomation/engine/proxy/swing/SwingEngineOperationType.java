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

import org.nabucco.testautomation.engine.proxy.SubEngineOperationType;

/**
 * SwingEngineOperationType
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public enum SwingEngineOperationType implements SubEngineOperationType {

	/**
     * EngineOperationType for a swing applet.
     */
    SWING_APPLET,
	
    /**
     * EngineOperationType for a swing application.
     */
    SWING_APPLICATION,

    /**
     * EngineOperationType for a swing button.
     */
    SWING_BUTTON,

    /**
     * EngineOperationType for a swing checkbox.
     */
    SWING_CHECKBOX,

    /**
     * EngineOperationType for a swing combobox.
     */
    SWING_COMBOBOX,

    /**
     * EngineOperationType for a swing dialog.
     */
    SWING_DIALOG,

    /**
     * EngineOperationType for a swing frame.
     */
    SWING_FRAME,
    
    /**
     * EngineOperationType for a swing menu.
     */
    SWING_MENU,

    /**
     * EngineOperationType for a swing password.
     */
    SWING_PASSWORD,

    /**
     * EngineOperationType for a swing radiobutton.
     */
    SWING_RADIOBUTTON,

    /**
     * EngineOperationType for a swing tab.
     */
    SWING_TAB,

    /**
     * EngineOperationType for a swing table.
     */
    SWING_TABLE,

    /**
     * EngineOperationType for a swing text input.
     */
    SWING_TEXTINPUT,
    
    /**
     * EngineOperationType for a printout of the swingtree.
     */
    SWING_PRINT,

    /**
     * EngineOperationType for a swing tree.
     */
    SWING_TREE,
    
    /**
     * EngineOperationType for a swing label.
     */
    SWING_LABEL,
    
    /**
     * EngineOperationType for a component.
     */
    SWING_COMPONENT;

}
