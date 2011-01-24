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

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunicationConstants;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ExecutionCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ExitCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.InitCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommandType;
import org.nabucco.testautomation.engine.proxy.swing.process.parser.CommandLineParser;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReplyType;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReplyWriter;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.FinalReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.LogReply;


/**
 * Socket communication from external process side.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class ClientCommunication implements Closeable {

    private BufferedReader commandReader;

    private CommandReplyWriter replyWriter;

    private ClientHandler handler;

    private Socket clientSocket;

    private ServerSocket serverSocket;

    /**
     * Creates a new {@link ClientCommunication} instance.
     * 
     * @param clientSocket
     *            the client socket.
     * @param serverSocket
     *            the server socket.
     * 
     * @throws IOException
     *             if an I/O error occurs creating the streams or if the socket is not connected.
     */
    public ClientCommunication(Socket clientSocket, ServerSocket serverSocket) throws IOException {
        this.commandReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.replyWriter = new CommandReplyWriter(clientSocket.getOutputStream());

        this.handler = ClientHandlerFactory.getInstance().getClientHandler();

        this.clientSocket = clientSocket;
        this.serverSocket = serverSocket;
    }

    @Override
    public void close() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    /**
     * Processes server commands and delegates them to the appropriate {@link ClientHandler}.
     * 
     * @return the exit code of the application
     */
    public synchronized int processCommands() {

        String line;
        StringBuilder encodedCommand = null;

        CommandLineParser parser = CommandLineParser.getInstance();

        try {

            while ((line = commandReader.readLine()) != null) {

                if (line.equals(ProcessCommunicationConstants.START_COMMAND)) {
                    encodedCommand = new StringBuilder();

                } else if (line.equals(ProcessCommunicationConstants.QUIT_COMMAND)) {

                    ProcessCommand command = parser.parseCommandLine(encodedCommand.toString());
                    try {
                        ExitCommand exit = executeCommand(command);
                        if (exit != null) {
                            return exit.getExitCode();
                        }

                    } catch (Exception e) {
                        replyWriter.replyLog("Exception during command execution.", e, Level.WARN);
                    }

                } else {
                    if (encodedCommand != null) {
                        encodedCommand.append(line);
                    }
                }
            }

        } catch (Exception e) {
            replyWriter.replyLog("Error during command processing.", e, Level.FATAL);
        }

        return -1;
    }

    /**
     * Executes a command.
     * 
     * @param command
     *            the command to execute
     * 
     * @return the exit command to shutdown the process or null to go on.
     * 
     * @throws Exception
     */
    private ExitCommand executeCommand(ProcessCommand command) throws Exception {

        ProcessCommandType type = command.getType();

        boolean success = true;

        switch (type) {

        case INIT: {
            InitCommand init = (InitCommand) command;
            handler.init(init.getClassName(), init.getArguments());
            break;
        }

        case EXEC: {
            ExecutionCommand exec = (ExecutionCommand) command;
            List<CommandReply> replyList = handler.execute(exec);

            for (CommandReply reply : replyList) {
                replyWriter.reply(reply);

                if (reply.getType() == CommandReplyType.LOG) {
                    success &= ((LogReply) reply).getSuccess();
                } else if (reply.getType() == CommandReplyType.FINAL) {
                    success &= ((FinalReply) reply).getSuccess();
                }
            }

            break;
        }

        case EXIT: {
            ExitCommand exit = (ExitCommand) command;

            replyWriter.replyFinal("Shutdown.", true);

            return exit;
        }

        case NOP: {
            // DO NOTHING!
            break;
        }

        }

        replyWriter.replyFinal(command + " executed.", success);
        return null;
    }

}
