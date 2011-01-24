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
package org.nabucco.testautomation.engine.proxy.swing.process.client;

import java.lang.reflect.Method;

/**
 * ClientApplicationThread
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class ClientApplicationThread extends Thread {

    private static final String DEFAULT_MAIN_METHOD = "main";

    private String className;
    
    private Object args;

    /**
     * Creates a new thread instance.
     * 
     * @param className
     *            the class to invoke
     */
    public ClientApplicationThread(String className) {
        this(className, new String[0]);
    }
    
    public ClientApplicationThread(String className, String[] args) {
        this.className = className;
        this.args = args;
    }

    @Override
    public void run() {
        try {
            Class<?> cc = Class.forName(className);
            Method mainMethod = cc.getMethod(DEFAULT_MAIN_METHOD, String[].class);
            mainMethod.invoke(cc, args);
        } catch (Exception e) {
            System.err.println("Error invoking main method of " + className + ": " + e.toString());
        }
    }

}
