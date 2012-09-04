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
package org.nabucco.testautomation.engine.proxy.swing.process.ui;

import java.io.Serializable;
import java.util.List;

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessComponent
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public interface SwingProcessComponent extends Serializable, SwingProperties {

    /**
     * Executes an action on a particular {@link SwingProcessComponent} instance.
     * 
     * @param propertyList
     *            the list of properties used to execute the action.
     * @param metadataList
     *            the list of metadata to find the related Swing component.
     * @param actionType
     *            the type of action to execute.
     */
    void execute(PropertyList propertyList, Metadata metadata,
            SwingActionType actionType);

    /**
     * Returns the list of reply objects collected during the execution.
     * 
     * @return the list of {@link CommandReply} instances
     */
    List<CommandReply> getReplyList();

}
