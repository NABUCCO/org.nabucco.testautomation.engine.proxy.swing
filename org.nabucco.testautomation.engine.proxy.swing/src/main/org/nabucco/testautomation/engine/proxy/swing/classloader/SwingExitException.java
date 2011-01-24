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

/**
 * SwingExitException
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class SwingExitException extends SecurityException {

    private static final long serialVersionUID = -2822795517781251730L;

    public SwingExitException() {
        super();
    }

    public SwingExitException(String message, Exception cause) {
        super(message, cause);
    }

    public SwingExitException(String message) {
        super(message);
    }

    public SwingExitException(Exception cause) {
        super(cause);
    }

}
