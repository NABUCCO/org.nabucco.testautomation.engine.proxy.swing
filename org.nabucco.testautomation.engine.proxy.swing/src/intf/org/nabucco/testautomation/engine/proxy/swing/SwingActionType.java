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

import org.nabucco.testautomation.engine.proxy.SubEngineActionType;

/**
 * SwingActionType
 * 
 * @author Frank Ratschinski, PRODYNA AG
 * 
 */
public enum SwingActionType implements SubEngineActionType {

    /**
     * The ActionType to start a swing application.
     */
    START("Start the Swing application"),

    /**
     * The ActionType to stop a swing application.
     */
    STOP("Stop the Swing application"),

    /**
     * The ActionType to enter a text into a input field.
     */
    ENTER("Entering text into input field"),

    /**
     * The ActionType to select an option.
     */
    SELECT("Selecting an option"),

    /**
     * The ActionType to read a text.
     */
    READ("Reading text"),

    /**
     * The ActionType for a left click with the mouse.
     */
    LEFTCLICK("Left mouse click"),

    /**
     * The ActionType for a right click with the mouse.
     */
    RIGHTCLICK("Right mouse click"),
    
    /**
     * The ActionType for a doubleclick with the mouse.
     */
    DOUBLECLICK("Left mouse click"),

    /**
     * The ActionType to close a swing dialog or window.
     */
    CLOSE("Close Window or Dialog"),

    /**
     * The ActionType to clear an input field.
     */
    CLEAR("Clearing text input field"),
    
    /**
     * The ActionType to check availability.
     */
    IS_AVAILABLE("Is Component available"),
    
    /**
     * The ActionType to find an entry.
     */
    FIND("Finds an entry"),
    
    /**
     * The ActionType to count entries.
     */
    COUNT("Counts entries in a table"),
    
    /**
     * The ActionType to press a key.
     */
    PRESS_KEY("Presses a certain key");

    private String description;

    private SwingActionType(String desc) {
        this.description = desc;
    }

    /**
     * Gets the description for the action type.
     * 
     * @return the description
     */
    public String getDescription() {
        return description;
    }

}
