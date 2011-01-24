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

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.config.AbstractProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfigurationType;

import org.nabucco.testautomation.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * SwingProxyConfigImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class SwingProxyConfigImpl extends AbstractProxyEngineConfiguration implements
		SwingProxyConfiguration {

	List<URL> urlList = new ArrayList<URL>();
	
    /**
     * Creates a new instance getting the configuration from
     * the given Properties.
     * 
     * @param the classloader that loaded the proxy
     * @param properties the properties containing the configuration
     */
	public SwingProxyConfigImpl(ClassLoader classloader,
			ProxyConfiguration configuration) {
		super(classloader, configuration);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addRuntimeLib(URL runtimeLib) {
		this.urlList.add(runtimeLib);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<URL> getClassloaderURLs(String applicationName) {

		List<URL> urls = new ArrayList<URL>(this.urlList);
		String url = null;
		int i = 0;
		
		while ((url = getConfigurationValue(getApplicationURLParameter(i++, applicationName))) != null) {
			try {
				urls.add(new File(url).toURI().toURL());
			} catch (Exception e) {
				System.err.println("Cannot parse URL='" + url
						+ "', skipping this URL");
			}
		}
		return urls;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public String getMainClass(String applicationName) {
		return getConfigurationValue(applicationName + "." + SwingProxyConfigurationType.MAIN_CLASS);
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
    public Integer getDebugPort() {
	    String debugPort = SwingProxyConfigurationType.SWING_PROCESS_DEBUG_PORT.getKey();
        
        try {
            return Integer.valueOf(debugPort);
        } catch (Exception ex) {
            return null;
        }
    }

	/**
     * {@inheritDoc}
     */
    @Override
    public int getPort() {
        String port = SwingProxyConfigurationType.SWING_PROCESS_PORT.getKey();
        
        try {
            return Integer.valueOf(port);
        } catch (Exception ex) {
            return 0;
        }
    }

	private String getApplicationURLParameter(int i, String applicationName) {

		String index;
		
		if (i < 10) {
			index = "00" + i;
		} else if (i < 100) {
			index = "0" + i;
		} else if (i < 1000) {
			index = "" + i;
		} else {
			System.err.println("No Support for more than 999 URLs");
			return null;
		}
		
		String key = SwingProxyConfigurationType.SWING_URL.getKey()
				.replace("[$AppName]", applicationName)
				.replace("[$Index]", index);
		return key;
	}
	
}
