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
package org.nabucco.testautomation.engine.proxy.swing.process;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.proxy.swing.process.client.ProcessInvocationException;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.ser.CommandAndReplySerializer;
import org.nabucco.testautomation.engine.proxy.swing.process.thread.CommandReplyThread;
import org.nabucco.testautomation.engine.proxy.swing.process.thread.ErrorStreamHandlerThread;
import org.nabucco.testautomation.engine.proxy.swing.process.thread.LogStreamHandlerThread;
import org.nabucco.testautomation.engine.proxy.swing.process.thread.ProcessExecutionThread;


/**
 * ProcessCommunication
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class ProcessCommunication implements Closeable {

    private static final int MIN_TIMEOUT = 60000;

    private static final int MAX_TIMEOUT = 180000;

    private static final NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            ProcessCommunication.class);

    private CommandReplyThread inputStreamHandler;

    private ProcessExecutionThread executionThread;

    private LogStreamHandlerThread logStreamHandler;

    private ErrorStreamHandlerThread errStreamHandler;

    private Process process;

    private Socket socket;

    private OutputStream out;
    
    private Queue<CommandReply> replyQueue = new ConcurrentLinkedQueue<CommandReply>();

    private CommandAndReplySerializer serializer;

    /**
     * Creates a new socket communication. Initializes two {@link Thread} instances for System.out
     * and System.err monitoring and two {@link Thread} instances for socket stream monitoring.
     * 
     * @param process
     *            the java process
     * @param port
     *            the address port
     * @param host
     *            the address host
     * 
     * @throws IOException
     *             if the socket connection can not be established.
     */
    public ProcessCommunication(Process process, Integer port, String host) throws IOException {

        this.serializer = CommandAndReplySerializer.getInstance();

        this.process = process;
        initLogStreamHandler();
        initErrorStreamHandler();

        this.socket = new Socket(host, port);
        
        initInputStreamHandler();
        initExecutionThread();

        this.out = socket.getOutputStream();
    }

    /**
     * Sends a command to the external java process, which will execute it. Using default timeout.
     * 
     * @param pc
     *            the
     * @return
     * @throws Exception
     */
    public List<CommandReply> executeCommand(ProcessCommand pc) throws Exception {
        return this.executeCommand(pc, MIN_TIMEOUT);
    }

    /**
     * Sends a command to the external java process, which will execute it.
     * 
     * @param pc
     *            the command to execute
     * @param waitTime
     *            time to wait for the command execution
     * 
     * @return the list of reply objects returned by the process
     * 
     * @throws Exception
     *             if an exception occurs during serialization or communication
     */
    public List<CommandReply> executeCommand(ProcessCommand pc, Integer waitTime) throws Exception {

        if (!this.isActive()) {
            logger.error("Communication is not active.");
            throw new ProcessInvocationException("Communication is not active.");
        }

        if (waitTime < MIN_TIMEOUT) {
            waitTime = MIN_TIMEOUT;
            logger.debug("Wait time too small, setting to min: " + MIN_TIMEOUT);
        }

        if (waitTime > MAX_TIMEOUT) {
            waitTime = MAX_TIMEOUT;
            logger.debug("Wait time too large, setting to max: " + MAX_TIMEOUT);
        }

        logger.debug("Executing " + pc);

        String commandLine = serializer.serializeCommand(pc);
        this.out.write(ProcessCommunicationConstants.START_COMMAND.getBytes());
        this.out.write("\n".getBytes());
        this.out.write(commandLine.getBytes());
        this.out.write("\n".getBytes());
        this.out.write(ProcessCommunicationConstants.QUIT_COMMAND.getBytes());
        this.out.write("\n".getBytes());
        this.out.flush();

        return pollReplies(waitTime);
    }

    private List<CommandReply> pollReplies(Integer waitTime) {

        List<CommandReply> replyList = new ArrayList<CommandReply>();

        synchronized (replyQueue) {
            try {

                while (replyQueue.isEmpty()) {

                    logger.debug("Waiting " + waitTime + "ms for reply.");

                    replyQueue.wait(waitTime);

                    if (replyQueue.isEmpty()) {
                        logger.warning("Polling reply queue interrupted by timeout.");
                        break;
                    }
                }

            } catch (InterruptedException e) {
                logger.error(e, "Waiting for reply queue interrupted by external process.");
            }

            while (replyQueue.peek() != null) {
                replyList.add(replyQueue.poll());
            }
        }
        
        return Collections.unmodifiableList(replyList);
    }

    /**
     * Returns the connection state.
     * 
     * @return <b>true</b> if the communication to a server is active, <b>false</b> if not.
     */
    public boolean isActive() {
        return !this.socket.isClosed();
    }

    /**
     * Waits for the four monitoring threads to die.
     * 
     * @see CommandReplyThread
     * @see ProcessExecutionThread
     * @see LogStreamHandlerThread
     * @see ErrorStreamHandlerThread
     */
    public void joinThreads() {
        try {
            this.inputStreamHandler.join();
            this.executionThread.join();
            this.logStreamHandler.join();
            this.errStreamHandler.join();
        } catch (Exception e) {
            logger.error(e, "Exception catched wile joining handler threads.");
        }
    }

    @Override
    public void close() throws IOException {
        joinThreads();
        this.socket.close();
    }

    private void initInputStreamHandler() throws IOException {
        this.inputStreamHandler = new CommandReplyThread(this.socket.getInputStream(),
                this.replyQueue);
        this.inputStreamHandler.start();
    }

    private void initExecutionThread() {
        this.executionThread = new ProcessExecutionThread(this.process);
        this.executionThread.start();
    }

    private void initLogStreamHandler() {
        this.logStreamHandler = new LogStreamHandlerThread(process.getInputStream());
        this.logStreamHandler.start();
    }

    private void initErrorStreamHandler() {
        this.errStreamHandler = new ErrorStreamHandlerThread(process.getErrorStream());
        this.errStreamHandler.start();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Communication :\n - Execution = ");
        builder.append(executionThread == null ? false : executionThread.isAlive());
        builder.append("\n - Input Stream = ");
        builder.append(inputStreamHandler == null ? false : inputStreamHandler.isAlive());
        builder.append("\n - Error Stream = ");
        builder.append(errStreamHandler == null ? false : errStreamHandler.isAlive());
        builder.append("\n - Log Stream = ");
        builder.append(logStreamHandler == null ? false : logStreamHandler.isAlive());
        builder.append("\n - Connection = ");
        builder.append(socket == null ? false : socket.isConnected());
        return builder.toString();
    }
}
