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
 * CommandReplySupport
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class CommandReplySupport implements CommandReply {

    private static final long serialVersionUID = 1L;

    private CommandReplyType type;

    @Override
    public CommandReplyType getType() {
        return type;
    }

    /**
     * Setter for the {@link CommandReplyType}.
     * 
     * @param type
     *            the type to set.
     */
    public void setType(CommandReplyType type) {
        this.type = type;
    }

}
