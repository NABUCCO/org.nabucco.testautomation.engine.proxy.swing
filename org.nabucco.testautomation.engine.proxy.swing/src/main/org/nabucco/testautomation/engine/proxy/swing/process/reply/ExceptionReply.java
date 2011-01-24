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
package org.nabucco.testautomation.engine.proxy.swing.process.reply;

/**
 * This {@link CommandReply} contains an exception that got raised in the external process and
 * should be rethrown on receiver side.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ExceptionReply extends CommandReplySupport implements CommandReply {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an {@link ExceptionReply} instance.
     */
    public ExceptionReply() {
        setType(CommandReplyType.EXCEPTION);
    }

    private Exception exception;

    /**
     * Getter for the exception to raise.
     * 
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * Setter for the exception to raise.
     * 
     * @param exception
     *            the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

}
