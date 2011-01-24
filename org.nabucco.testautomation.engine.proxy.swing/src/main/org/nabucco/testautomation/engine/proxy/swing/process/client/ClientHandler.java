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

import java.util.List;

import org.nabucco.testautomation.engine.proxy.swing.process.command.ExecutionCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;


/**
 * Handles commands send to the external process.
 * 
 * @author Frank Ratschinski, PRODYNA AG
 */
public interface ClientHandler {

    /**
     * Initializes the application by invocating the main method of the given class.
     * 
     * @param className
     *            the main class.
     * @param args
     * 			  arguments for the main-method 
     * 
     * @throws Exception
     */
    void init(String className, String[] args) throws Exception;

    /**
     * Executes a command on the application.
     * 
     * @param exec
     *            the command to execute.
     * 
     * @return a list of reply objects.
     * 
     * @throws Exception
     */
    List<CommandReply> execute(ExecutionCommand exec) throws Exception;
}
