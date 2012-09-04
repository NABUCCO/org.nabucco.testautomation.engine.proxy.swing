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

import java.net.URL;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;


/**
 * SwingProxyConfiguration
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public interface SwingProxyConfiguration extends ProxyEngineConfiguration {

    /**
     * Adds the URL of a runtime lib to the configuration.
     * 
     * @param runtimeLib the URL of the lib to add
     */
    public void addRuntimeLib(URL runtimeLib);
    
    /**
     * Gets the URLs loaded by this classloader.
     * 
     * @return the URLs
     */
    public List<URL> getClassloaderURLs(String applicationName);

    /**
     * Gets the name of the class containing the main-method.
     * 
     * @return the name of the main class
     */
    public String getMainClass(String applicationName);

    /**
     * Gets the port.
     * 
     * @return the port
     */
    public int getStartPort();
    
    /**
     * Gets the port.
     * 
     * @return the port
     */
    public int getEndPort();

    /**
     * Gets the port enabled for debugging. If DebugPort is null, debugging will be disabled.
     * 
     * @return the debug port
     */
    public Integer getDebugPort();

	/**
	 * 
	 * @param applicationName
	 * @return
	 */
	String[] getArguments(String applicationName);
    
}
