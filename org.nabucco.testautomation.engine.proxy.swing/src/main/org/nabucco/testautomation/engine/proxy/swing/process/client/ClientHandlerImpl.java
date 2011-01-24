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
import org.nabucco.testautomation.engine.proxy.swing.process.ui.SwingProcessComponent;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.SwingProcessComponentFactory;


/**
 * Default implementation instance of the {@link ClientHandler} interface.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class ClientHandlerImpl implements ClientHandler {

    private static final int START_TIME = 2000;

    @Override
    public void init(String className, String[] args) throws ProcessInvocationException {

        try {
        	ClientApplicationThread thread;
        	
        	if (args == null) {
        		thread = new ClientApplicationThread(className);
        	} else {
        		thread = new ClientApplicationThread(className, args);
        	}

            thread.start();

            Thread.sleep(START_TIME);

            thread.join(START_TIME);

            if (thread.isAlive()) {
                System.err.println("ApplicationThread is still running.");
            }

        } catch (Exception e) {
            throw new ProcessInvocationException("Error starting application '" + className + "'.", e);
        }
    }

    @Override
    public List<CommandReply> execute(ExecutionCommand exec) throws Exception {

        SwingProcessComponent component = SwingProcessComponentFactory.getInstance()
                .getSwingProcessComponent(exec.getComponentType());

        component.execute(exec.getPropertyList(), exec.getMetadataList(), exec.getActionType());

        return component.getReplyList();
    }

}
