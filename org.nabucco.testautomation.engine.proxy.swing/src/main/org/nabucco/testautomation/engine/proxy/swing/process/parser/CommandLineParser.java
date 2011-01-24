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
package org.nabucco.testautomation.engine.proxy.swing.process.parser;

import org.nabucco.testautomation.engine.proxy.swing.process.command.NopCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ProcessCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.ser.CommandAndReplySerializer;

/**
 * CommandLineParser
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public class CommandLineParser {

    private CommandAndReplySerializer commandSerializer;

    private static CommandLineParser instance;

    /**
     * Private constructor.
     */
    private CommandLineParser() {
        this.commandSerializer = CommandAndReplySerializer.getInstance();
    }

    /**
     * Singleton access.
     * 
     * @return the {@link CommandLineParser} instance.
     */
    public static final synchronized CommandLineParser getInstance() {
        if (instance == null) {
            instance = new CommandLineParser();
        }
        return instance;
    }

    /**
     * Parses a command line and deserializes the particular command. If none exist, null is
     * returned.
     * 
     * @param commandLine
     *            the line to parse
     * 
     * @return the deserialized {@link ProcessCommand} instance.
     */
    public ProcessCommand parseCommandLine(String commandLine) {

        ProcessCommand command = this.commandSerializer.deserializeCommand(commandLine);

        if (command == null) {
            command = new NopCommand();
        }

        return command;

    }

}
