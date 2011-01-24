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

import java.io.Serializable;

/**
 * ProcessInvocationException
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class ProcessInvocationException extends Exception implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link ProcessInvocationException} without parameters.
     */
    public ProcessInvocationException() {
        super();
    }

    /**
     * Creates a new {@link ProcessInvocationException} with message and a causing exception.
     * 
     * @param message
     *            the exception message
     * @param cause
     *            the cause of the exception
     */
    public ProcessInvocationException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Creates a new {@link ProcessInvocationException} with message.
     * 
     * @param message
     *            the exception message
     */
    public ProcessInvocationException(String message) {
        super(message);
    }

    /**
     * Creates a new {@link ProcessInvocationException} with a causing exception.
     * 
     * @param cause
     *            the cause of the exception
     */
    public ProcessInvocationException(Exception cause) {
        super(cause);
    }

}
