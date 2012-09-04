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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.nabucco.testautomation.engine.proxy.config.AbstractProxyEngineConfiguration;
import org.nabucco.testautomation.settings.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * SwingProxyConfigImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 * 
 */
public class SwingProxyConfigImpl extends AbstractProxyEngineConfiguration implements SwingProxyConfiguration {

	List<URL> urlList = new ArrayList<URL>();

	/**
	 * Creates a new instance getting the configuration from the given Properties.
	 * 
	 * @param the
	 *            classloader that loaded the proxy
	 * @param properties
	 *            the properties containing the configuration
	 */
	public SwingProxyConfigImpl(ClassLoader classloader, ProxyConfiguration configuration) {
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
		List<URL> urls = new ArrayList<URL>();
		String url = null;
		int i = 0;
		Properties urlProps = getApplicationURLPropertiesFromFile(applicationName);
		if (urlProps != null) {
			while ((url = urlProps.getProperty("SWING_URL_" + intToString(i++))) != null) {
				try {
					urls.add(new File(url).toURI().toURL());
				} catch (Exception e) {
					System.err.println("Cannot parse URL='" + url + "', skipping this URL");
				}
			}
		}
		i = 0;
		while ((url = getConfigurationValue(getApplicationURLParameter(i++, applicationName))) != null) {
			try {
				urls.add(new File(url).toURI().toURL());
			} catch (Exception e) {
				System.err.println("Cannot parse URL='" + url + "', skipping this URL");
			}
		}
		urls.addAll(this.urlList);
		return urls;
	}

	private static String intToString(int i) {
		String index = "";
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
		return index;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getMainClass(String applicationName) {
		return getConfigurationValue(SwingProxyConfigurationType.MAIN_CLASS.getKey().replace("[$AppName]", applicationName));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer getDebugPort() {
		String debugPort = getConfigurationValue(SwingProxyConfigurationType.SWING_PROCESS_DEBUG_PORT.toString());
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
	public String[] getArguments(String applicationName) {
		List<String> args = new ArrayList<String>();
		String arg = null;
		int i = 0;
		while ((arg = getConfigurationValue(getApplicationMainClassParameter(i++, applicationName))) != null) {
			try {
				args.add(arg);
			} catch (Exception e) {
				System.err.println("Cannot parse parameter='" + arg + "', skipping this URL");
			}
		}
		if(args.size() > 0) {
			return args.toArray(new String[args.size()]);
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getStartPort() {
		String port = getConfigurationValue(SwingProxyConfigurationType.SWING_PROCESS_PORT_START.toString());
		try {
			return Integer.valueOf(port);
		} catch (Exception ex) {
			return 0;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getEndPort() {
		String port = getConfigurationValue(SwingProxyConfigurationType.SWING_PROCESS_PORT_END.toString());
		try {
			return Integer.valueOf(port);
		} catch (Exception ex) {
			return 0;
		}
	}

	private String getApplicationURLParameter(int i, String applicationName) {
		String key = SwingProxyConfigurationType.SWING_URL.getKey().replace("[$AppName]", applicationName).replace("[$Index]", intToString(i));
		return key;
	}
	
	private String getApplicationMainClassParameter(int i, String applicationName) {
		String key = SwingProxyConfigurationType.SWING_PARAM.getKey().replace("[$AppName]", applicationName).replace("[$Index]", intToString(i));
		return key;
	}

	private Properties getApplicationURLPropertiesFromFile(String applicationName) {
		String filename = getConfigurationValue(SwingProxyConfigurationType.SWING_URL_FILE.getKey().replace("[$AppName]", applicationName));
		if (filename == null)
			return null;
		Properties properties = new Properties();
		try {
			File f = new File(filename);
			properties.load(new InputStreamReader(new FileInputStream(f), Charset.forName("UTF-8")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}
}
