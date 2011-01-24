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
package org.nabucco.testautomation.engine.proxy.swing.classloader;

import java.net.URL;
import java.net.URLClassLoader;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;


/**
 * TargetApplicationClassloader
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class TargetApplicationClassloader {

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            TargetApplicationClassloader.class);

    private ClassLoader cl;

    /**
     * Constructs a new {@link TargetApplicationClassloader} instance based on URLs.
     * 
     * @see URLClassLoader
     * 
     * @param urls
     *            the URLs to initialize the classloader
     */
    public TargetApplicationClassloader(URL[] urls) {

        System.setSecurityManager(createNoExitSecurityManager());

        cl = new URLClassLoader(urls, Thread.currentThread().getContextClassLoader());
        logger.info("Initializing new TargetApplicationClassloader with URLs");

        for (int i = 0; i < urls.length; i++) {
            logger.info("using URL: " + urls[i].getFile());
        }
    }

    private SecurityManager createNoExitSecurityManager() {
        return new SwingExitSecurityManager(System.getSecurityManager());
    }

    /**
     * Loads a class by its name.
     * 
     * @param className
     *            the classname
     * 
     * @return the loaded class
     */
    public Class<?> loadClass(String className) {
        try {
            return this.cl.loadClass(className);
        } catch (ClassNotFoundException cne) {
            logger.error(cne, "Cannot load Class of TargetApplication");
        }
        return null;
    }

    /**
     * Resets the {@link TargetApplicationClassloader}.
     */
    public void reset() {
        if (System.getSecurityManager() instanceof SwingExitSecurityManager) {
            System.setSecurityManager(((SwingExitSecurityManager) System.getSecurityManager())
                    .getDelegate());
        }
    }
}
