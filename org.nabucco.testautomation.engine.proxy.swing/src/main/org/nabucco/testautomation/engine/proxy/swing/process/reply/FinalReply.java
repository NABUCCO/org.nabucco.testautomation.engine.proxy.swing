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
 * This {@link CommandReply} qualifies a complete execution within the external process.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class FinalReply extends CommandReplySupport implements CommandReply {

    private static final long serialVersionUID = 1L;

    private String message;

    private boolean success = false;

    /**
     * Creates a {@link FinalReply} instance.
     */
    public FinalReply(){
        setType(CommandReplyType.FINAL);
    }
    
    /**
     * @return the message
     */
    public String getMessage() {
        if (message == null) {
            message = success ? "Execution successful." : "Execution unsuccessful.";
        }

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
