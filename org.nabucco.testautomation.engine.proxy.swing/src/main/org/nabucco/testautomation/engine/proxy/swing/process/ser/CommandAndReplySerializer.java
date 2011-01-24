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
package org.nabucco.testautomation.engine.proxy.swing.process.ser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;
import org.nabucco.testautomation.engine.proxy.swing.process.command.NopCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.NoReply;


/**
 * ObjectSerializer
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class CommandAndReplySerializer {

    private static CommandAndReplySerializer instance;

    /**
     * Private constructor.
     */
    private CommandAndReplySerializer() {
    }

    /**
     * Singleton access
     * 
     * @return the {@link CommandAndReplySerializer} instance.
     */
    public static final synchronized CommandAndReplySerializer getInstance() {
        if (instance == null) {
            instance = new CommandAndReplySerializer();
        }
        return instance;
    }

    /**
     * Serializes a {@link ProcessCommand} to send it to the external process.
     * 
     * @param command
     *            the command to serialize
     * 
     * @return the serialized command.
     */
    public String serializeCommand(ProcessCommand command) {

        try {

            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(buf);

            objOut.writeObject(command);

            byte[] byteData = buf.toByteArray();
            String stringData = Base64.encodeBase64String(byteData);
            return stringData;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Serializes a {@link CommandReply} to return it from the external process.
     * 
     * @param reply
     *            the reply to serialize
     * 
     * @return the serialized reply.
     */
    public String serializeReply(CommandReply reply) {

        try {

            ByteArrayOutputStream buf = new ByteArrayOutputStream();
            ObjectOutputStream objOut = new ObjectOutputStream(buf);

            objOut.writeObject(reply);

            byte[] byteData = buf.toByteArray();

            String stringData = Base64.encodeBase64String(byteData);
            return stringData;
        } catch (Exception e) {
            return "";
        }

    }

    /**
     * Deserializes a received and encoded {@link ProcessCommand}.
     * 
     * @param encodedCommand
     *            the encoded command to deserialize
     * 
     * @return the deserialized command.
     */
    public ProcessCommand deserializeCommand(String encodedCommand) {
        try {
            byte[] byteData = Base64.decodeBase64(encodedCommand);

            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteData));

            Object obj = objIn.readObject();
            if (obj instanceof ProcessCommand) {

                return (ProcessCommand) obj;
            }

        } catch (Exception e) {
            return new NopCommand();
        }
        return new NopCommand();
    }

    /**
     * Deserializes a received and encoded {@link CommandReply}.
     * 
     * @param encodedReply
     *            the encoded reply to deserialize
     * 
     * @return the deserialized reply.
     */
    public CommandReply deserializeReply(String encodedReply) {
        try {
            byte[] byteData = Base64.decodeBase64(encodedReply);

            ObjectInputStream objIn = new ObjectInputStream(new ByteArrayInputStream(byteData));

            Object obj = objIn.readObject();
            if (obj instanceof CommandReply) {

                return (CommandReply) obj;
            }

        } catch (Exception e) {
            return new NoReply();
        }
        return new NoReply();
    }
}
