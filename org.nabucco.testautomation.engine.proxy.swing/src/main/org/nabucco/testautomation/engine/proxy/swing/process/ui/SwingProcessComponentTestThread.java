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

import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * Thread for SwingProcessComponent test simulation based on JFCUnit.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
final class SwingProcessComponentTestThread implements Runnable {

    private PropertyList propertyList;

    private Metadata metadata;

    private SwingActionType actionType;

    private SwingProcessComponentSupport swingComponent;

    /**
     * Creates a new instance.
     * 
     * @param propertyList
     *            the list of properties
     * @param metadata
     *            the list of metadata
     * @param actionType
     *            the action type
     * @param swingComponent
     *            the swing component to execute
     */
    public SwingProcessComponentTestThread(PropertyList propertyList,
            Metadata metadata, SwingActionType actionType,
            SwingProcessComponentSupport swingComponent) {

        this.propertyList = propertyList;
        this.metadata = metadata;
        this.actionType = actionType;

        if (swingComponent == null) {
            throw new IllegalArgumentException("SwingProcessComponent must be defined.");
        }

        this.swingComponent = swingComponent;
    }

    /**
     * Runs the test simulation.
     */
    @Override
    public void run() {
        swingComponent.internalExecute(propertyList, metadata, actionType);
        swingComponent.flushAWT();
    }

}
