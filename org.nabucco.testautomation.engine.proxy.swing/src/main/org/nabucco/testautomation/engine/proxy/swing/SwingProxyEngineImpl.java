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

import java.io.File;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.util.FileUtils;
import org.nabucco.testautomation.engine.proxy.SubEngine;
import org.nabucco.testautomation.engine.proxy.base.AbstractProxyEngine;
import org.nabucco.testautomation.engine.proxy.config.ProxyEngineConfiguration;
import org.nabucco.testautomation.engine.proxy.exception.ProxyConfigurationException;
import org.nabucco.testautomation.engine.proxy.exception.SubEngineException;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfigImpl;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.swing.process.JavaProcess;
import org.nabucco.testautomation.engine.proxy.swing.ui.SwingAppletImpl;
import org.nabucco.testautomation.engine.proxy.swing.ui.SwingApplicationImpl;
import org.nabucco.testautomation.engine.proxy.swing.ui.SwingComponentFactory;
import org.nabucco.testautomation.settings.facade.datatype.engine.SubEngineType;
import org.nabucco.testautomation.settings.facade.datatype.engine.proxy.ProxyConfiguration;

/**
 * SwingProxyEngineImpl
 *
 * @author Steffen Schmidt, PRODYNA AG
 *
 */
public class SwingProxyEngineImpl extends AbstractProxyEngine {

	private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
			SwingProxyEngineImpl.class);
	
	private SwingApplet swingApplet;

	private SwingApplication swingApplication;
	
	private SwingProxyConfiguration applicationConfiguration;
	
	/**
     * Constructs a new ProxyEngine with {@link SubEngineType.SWING}.
     */
	protected SwingProxyEngineImpl() {
		super(SubEngineType.SWING);
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void initialize() throws ProxyConfigurationException {
		// nothing todo so far
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	public void configure(ProxyEngineConfiguration config) throws ProxyConfigurationException {
		try {
            this.applicationConfiguration = (SwingProxyConfigImpl) config;
            this.applicationConfiguration.addRuntimeLib(FileUtils.toURL(getProxySupport().getProxyJar()));

            // Add libraries from lib to applicationConfiguration
            for (File runtimeLib : getProxySupport().getRuntimeLibs()) {
            	this.applicationConfiguration.addRuntimeLib(FileUtils.toURL(runtimeLib));
            }
            logger.info("SwingProxyEngine configured");
        } catch (Exception ex) {
        	throw new ProxyConfigurationException("Could not configure SwingProxyEngine", ex);
        }
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public SubEngine start() throws ProxyConfigurationException {
		logger.info("Starting SwingProxyEngine ...");
		JavaProcess process = new JavaProcess(this.applicationConfiguration.getStartPort(), this.applicationConfiguration.getEndPort(), this.applicationConfiguration.getDebugPort());
		this.swingApplication = (SwingApplicationImpl) SwingComponentFactory
				.getInstance().createSwingApplication(process, this.applicationConfiguration);
		this.swingApplet = (SwingAppletImpl) SwingComponentFactory
				.getInstance().createSwingApplet(process, this.applicationConfiguration);
		SubEngine subEngine = null;
		subEngine = new SwingSubEngineImpl(this.swingApplication, this.swingApplet);
		logger.info("SwingSubEngine created");
		return subEngine;
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void stop() throws ProxyConfigurationException {
		
		if (swingApplication != null) {
            try {
				swingApplication.execute(null, null, null, SwingActionType.STOP);
			} catch (SubEngineException ex) {
				throw new ProxyConfigurationException("Could not stop SwingProxyEngine", ex);
			}
        } 
		
		if (swingApplet != null) {
            try {
				swingApplet.execute(null, null, null, SwingActionType.STOP);
			} catch (SubEngineException ex) {
				throw new ProxyConfigurationException("Could not stop SwingProxyEngine", ex);
			}
        }
	}

	/**
     * {@inheritDoc}
     */
	@Override
	public void unconfigure() throws ProxyConfigurationException {
		swingApplication = null;		
		swingApplet = null;		
		this.applicationConfiguration = null;		
	}
	
	/**
     * {@inheritDoc}
     */
	@Override
	protected ProxyEngineConfiguration getProxyConfiguration(
			ProxyConfiguration configuration) {
		SwingProxyConfiguration config = new SwingProxyConfigImpl(
				getProxySupport().getProxyClassloader(), configuration);
		return config;
	}

}
