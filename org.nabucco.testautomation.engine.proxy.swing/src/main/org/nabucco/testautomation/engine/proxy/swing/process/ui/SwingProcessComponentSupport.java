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

import java.awt.Component;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.extensions.jfcunit.JFCTestCase;
import junit.extensions.jfcunit.JFCTestHelper;
import junit.extensions.jfcunit.WindowMonitor;

import org.apache.log4j.Level;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.ExceptionReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.FinalReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.LogReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.PropertyReply;
import org.nabucco.testautomation.engine.proxy.swing.process.ui.finder.SwingComponentFinder;

import org.nabucco.testautomation.facade.datatype.property.BooleanProperty;
import org.nabucco.testautomation.facade.datatype.property.PropertyList;
import org.nabucco.testautomation.facade.datatype.property.base.Property;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingProcessComponentSupport
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
abstract class SwingProcessComponentSupport extends JFCTestCase implements SwingProcessComponent,
        Serializable {

    private static final long serialVersionUID = 1L;

    private List<CommandReply> replyList = new ArrayList<CommandReply>();

    public SwingProcessComponentSupport() {
        super("Simulation");
    }

    @Override
    public void execute(PropertyList propertyList, List<Metadata> metadataList,
            SwingActionType actionType) {

        initJFC();

        try {
            SwingProcessComponentTestThread swingThread = new SwingProcessComponentTestThread(
                    propertyList, metadataList, actionType, this);

            super.runCode(swingThread);

        } catch (Throwable t) {
            this.log("Error during test simulation.", new Exception(t), Level.ERROR, false);
            this.raiseException(new Exception(t));
        }
    }

    /**
     * Initializes JFC components.
     */
    private void initJFC() {
        setName("Simulation");
        setHelper(new JFCTestHelper());

        WindowMonitor.start();
    }

    abstract void internalExecute(PropertyList propertyList, List<Metadata> metadataList,
            SwingActionType actionType);

    @Override
    public List<CommandReply> getReplyList() {
        return Collections.unmodifiableList(replyList);
    }

    /**
     * Adds a {@link PropertyReply} to the reply list.
     * 
     * @param property
     *            the property for the {@link PropertyReply}
     */
    void addProperty(Property property) {
        PropertyReply reply = new PropertyReply();
        reply.setProperty(property);

        this.replyList.add(reply);
    }

    /**
     * Adds a {@link LogReply} to the reply list.
     * 
     * @param message
     *            the message to log
     * @param e
     *            the exception to log
     * @param level
     *            the log level
     * @param success
     *            defines the test result, <b>true</b> for a successful execution, <b>false</b> for
     *            a failed execution
     */
    void log(String message, Exception e, Level level, boolean success) {
        LogReply reply = new LogReply();
        reply.setMessage(message);
        reply.setException(e);
        reply.setLevel(level.toInt());
        reply.setSuccess(success);

        if (e != null) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                stackTrace.append(stackTraceElement);
                stackTrace.append("\n");
            }
            reply.setStacktrace(stackTrace.toString());
        }

        this.replyList.add(reply);
    }
    
    void failure(String message) {
    	FinalReply reply = new FinalReply();
    	reply.setMessage(message);
    	reply.setSuccess(Boolean.FALSE);
    	this.replyList.add(reply);
    }

    /**
     * Adds a {@link LogReply} to the reply list.
     * 
     * @param message
     *            the message to log
     * @param e
     *            the exception to log
     * @param level
     *            the log level
     * @param success
     *            defines the test result, <b>true</b> for a successful execution, <b>false</b> for
     *            a failed execution
     */
    void raiseException(Exception e) {
        ExceptionReply reply = new ExceptionReply();
        reply.setException(e);
        this.replyList.add(reply);
    }
    
    void checkAvailability(List<Metadata> metadataList, BooleanProperty prop) {
    	
    	try {
			Component component = SwingComponentFinder.getInstance().findComponent(metadataList);
			
			if (component == null) {
				prop.setValue(Boolean.FALSE);
			} else if (isAvailable(component)) {
				prop.setValue(Boolean.FALSE);
			} else {
				prop.setValue(Boolean.TRUE);
			}
		} catch (Exception e) {
			this.log("Eror while checking availability", e, Level.ERROR, false);
			prop.setValue(Boolean.FALSE);
		}
    	this.addProperty(prop);
	}
    
    abstract boolean isAvailable(Component component);

}
