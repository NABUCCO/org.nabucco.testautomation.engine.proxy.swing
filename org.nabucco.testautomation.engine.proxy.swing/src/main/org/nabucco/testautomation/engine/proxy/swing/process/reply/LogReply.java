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
 * This {@link CommandReply} contains logging information from the external process, which should be
 * logged on receiver side.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class LogReply extends CommandReplySupport implements CommandReply {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a {@link LogReply} instance.
     */
    public LogReply() {
        setType(CommandReplyType.LOG);
    }

    private String message;

    private Exception exception;

    private String stacktrace;

    private Integer level;

    private Boolean success = true;

    /**
     * @return the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message
     *            the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the exception
     */
    public Exception getException() {
        return exception;
    }

    /**
     * @param exception
     *            the exception to set
     */
    public void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * @return the stacktrace
     */
    public String getStacktrace() {
        return stacktrace;
    }

    /**
     * @param stacktrace
     *            the exception stacktrace to set
     */
    public void setStacktrace(String stacktrace) {
        this.stacktrace = stacktrace;
    }

    /**
     * @return the log level
     */
    public Integer getLevel() {
        return level;
    }

    /**
     * @param level
     *            the log level to set
     */
    public void setLevel(Integer level) {
        this.level = level;
    }

    /**
     * @return the success
     */
    public Boolean getSuccess() {
        return success;
    }

    /**
     * @param success
     *            the success to set
     */
    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
