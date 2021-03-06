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
package org.nabucco.testautomation.engine.proxy.swing.config;

/**
 * SwingProxyConfigurationType
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public enum SwingProxyConfigurationType {

	/**
     * Constant for the property-key for the swing URL.
     */
    SWING_URL("[$AppName].SWING_URL_[$Index]"),

    /**
     * Constant for the property-key for the swing URL.
     */
    SWING_PARAM("[$AppName].SWING_PARAM_[$Index]"),

    /**
     * Constant for the property-key for the swing URL.
     */
    SWING_URL_FILE("[$AppName].SWING_URL_FILE"),

    /**
     * Constant for the property-key for the swing process port.
     */
    SWING_PROCESS_PORT_START("1892"),
    
    /**
     * Constant for the property-key for the swing process port.
     */
    SWING_PROCESS_PORT_END("1898"),
    
    /**
     * Constant for the property-key for the swing process debugging port.
     */
    SWING_PROCESS_DEBUG_PORT("1899"),
    
    /**
     * Constant for the property-key for the main class.
     */
    MAIN_CLASS("[$AppName].MAIN_CLASS");
	
	private String key;
    
    private SwingProxyConfigurationType(String key) {
    	this.key = key;    	 
    }
    
    public String getKey() {
    	return this.key;
    }
	
}
