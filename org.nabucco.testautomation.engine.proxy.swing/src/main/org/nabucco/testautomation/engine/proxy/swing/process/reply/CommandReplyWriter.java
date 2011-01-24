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

import java.io.BufferedOutputStream;
import java.io.OutputStream;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunicationConstants;
import org.nabucco.testautomation.engine.proxy.swing.process.ser.CommandAndReplySerializer;

import org.nabucco.testautomation.facade.datatype.property.base.Property;

/**
 * Replies {@link CommandReply} instances to the given output stream.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class CommandReplyWriter {

    private BufferedOutputStream replyStream;

    /**
     * Creates a new instance.
     * 
     * @param out
     *            the reply stream to write to.
     */
    public CommandReplyWriter(OutputStream out) {
        this.replyStream = new BufferedOutputStream(out);
    }

    /**
     * Replies a log message to the server.
     * 
     * @param message
     *            the log message
     * @param e
     *            the exception to log
     * @param level
     *            the log level
     */
    public void replyLog(String message, Exception e, Level level) {

        LogReply log = new LogReply();
        log.setMessage(message);
        log.setException(e);
        log.setLevel(level.toInt());
        log.setStacktrace("Not yet implemented"); // TODO create stacktrace

        reply(log);
    }

    /**
     * Replies an exception to the server.
     * 
     * @param exception
     *            the exception to reply
     */
    public void replyException(Exception exception) {

        ExceptionReply ex = new ExceptionReply();
        ex.setException(exception);

        reply(ex);
    }

    /**
     * Replies a property to the server.
     * 
     * @param property
     *            the property to reply
     */
    public void replyProperty(Property property) {

        PropertyReply prop = new PropertyReply();
        prop.setProperty(property);

        reply(prop);
    }

    /**
     * Replies a complete execution to the server.
     * 
     * @param message
     *            the success message
     */
    public void replyFinal(String message, boolean isSuccess) {

        FinalReply fin = new FinalReply();
        fin.setMessage(message);
        fin.setSuccess(isSuccess);

        reply(fin);
    }

    /**
     * Replies a {@link CommandReply} instance to the server.
     * 
     * @param reply
     *            the reply object
     */
    public synchronized void reply(CommandReply reply) {

        String relyLine = CommandAndReplySerializer.getInstance().serializeReply(reply);
        try {
            this.replyStream.write(ProcessCommunicationConstants.START_COMMAND.getBytes());
            this.replyStream.write("\n".getBytes());
            this.replyStream.write(relyLine.getBytes());
            this.replyStream.write("\n".getBytes());
            this.replyStream.write(ProcessCommunicationConstants.QUIT_COMMAND.getBytes());
            this.replyStream.write("\n".getBytes());
            this.replyStream.flush();

        } catch (Exception e) {
            System.err.println("Error sending reply: " + e.getMessage());
        }
    }

}
