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
package org.nabucco.testautomation.engine.proxy.swing.process.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Queue;

import org.nabucco.testautomation.engine.base.logging.NBCTestLogger;
import org.nabucco.testautomation.engine.base.logging.NBCTestLoggingFactory;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunicationConstants;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReplyType;
import org.nabucco.testautomation.engine.proxy.swing.process.ser.CommandAndReplySerializer;


/**
 * Monitors the socket connection for reply objects and adds them to the queue.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class CommandReplyThread extends Thread {

    private static final NBCTestLogger logger = NBCTestLoggingFactory.getInstance().getLogger(
            CommandReplyThread.class);

    private BufferedReader reader;

    private Queue<CommandReply> replyQueue;

    private CommandAndReplySerializer ser;

    /**
     * Creates a new instance.
     * 
     * @param in
     *            the input stream to monitor.
     * @param replyQueue
     *            the queue to add replies to.
     * @param lock
     */
    public CommandReplyThread(InputStream in, Queue<CommandReply> replyQueue) {
        this.replyQueue = replyQueue;
        this.ser = CommandAndReplySerializer.getInstance();
        this.reader = new BufferedReader(new InputStreamReader(in));
    }

    @Override
    public void run() {

        try {
            String line;
            StringBuilder encodedReply = null;

            while ((line = reader.readLine()) != null) {

                if (line.equals(ProcessCommunicationConstants.START_COMMAND)) {
                    encodedReply = new StringBuilder();

                } else if (line.equals(ProcessCommunicationConstants.QUIT_COMMAND)) {

                    CommandReply reply = ser.deserializeReply(encodedReply.toString());

                    if (reply.getType() == null || reply.getType() == CommandReplyType.NOR) {
                        logger.debug("Reply object is [None].");
                    } else {
                        synchronized (replyQueue) {
                            this.replyQueue.add(reply);
                            logger.debug("Reply object is [", reply.getType().getDescription(),"].");

                            if (reply.getType() == CommandReplyType.FINAL) {
                                replyQueue.notify();
                            }
                        }
                    }

                } else {
                    if (encodedReply != null) {
                        encodedReply.append(line);
                    }
                }
            }

        } catch (SocketException e) {
            logger.warning("Socket connection lost: " + e.getMessage());
        } catch (Exception e) {
            logger.fatal(e, "Exception while reading input stream.");
        }
    }

}
