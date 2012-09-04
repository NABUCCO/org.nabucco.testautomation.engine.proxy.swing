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
package org.nabucco.testautomation.engine.proxy.swing.ui;

import java.net.URL;
import java.util.List;

import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.engine.base.context.TestContext;
import org.nabucco.testautomation.engine.base.util.TestResultHelper;
import org.nabucco.testautomation.engine.proxy.SubEngineActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingActionType;
import org.nabucco.testautomation.engine.proxy.swing.SwingApplet;
import org.nabucco.testautomation.engine.proxy.swing.SwingButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingCheckbox;
import org.nabucco.testautomation.engine.proxy.swing.SwingComboBox;
import org.nabucco.testautomation.engine.proxy.swing.SwingJComponent;
import org.nabucco.testautomation.engine.proxy.swing.SwingDialog;
import org.nabucco.testautomation.engine.proxy.swing.SwingFrame;
import org.nabucco.testautomation.engine.proxy.swing.SwingLabel;
import org.nabucco.testautomation.engine.proxy.swing.SwingMenu;
import org.nabucco.testautomation.engine.proxy.swing.SwingPassword;
import org.nabucco.testautomation.engine.proxy.swing.SwingPrint;
import org.nabucco.testautomation.engine.proxy.swing.SwingRadioButton;
import org.nabucco.testautomation.engine.proxy.swing.SwingTab;
import org.nabucco.testautomation.engine.proxy.swing.SwingTable;
import org.nabucco.testautomation.engine.proxy.swing.SwingTextInput;
import org.nabucco.testautomation.engine.proxy.swing.SwingTree;
import org.nabucco.testautomation.engine.proxy.swing.config.SwingProxyConfiguration;
import org.nabucco.testautomation.engine.proxy.swing.process.JavaProcess;
import org.nabucco.testautomation.engine.proxy.swing.process.ProcessCommunication;
import org.nabucco.testautomation.engine.proxy.swing.process.command.ExitCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.command.InitCommand;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReply;
import org.nabucco.testautomation.engine.proxy.swing.process.reply.CommandReplyEvaluator;
import org.nabucco.testautomation.property.facade.datatype.PropertyList;
import org.nabucco.testautomation.property.facade.datatype.TextProperty;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;
import org.nabucco.testautomation.script.facade.datatype.metadata.Metadata;

/**
 * SwingAppletImpl
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class SwingAppletImpl implements SwingApplet {

    private static final String APPLET_LAUNCHER = "org.nabucco.testautomation.engine.proxy.swing.process.applet.AppletLauncher";

	private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            SwingAppletImpl.class);

    private static final long serialVersionUID = 1L;

    private JavaProcess process;

    private SwingProxyConfiguration config;

    private ProcessCommunication communication;

    private ActionStatusType status;

    private SwingButton swingButton;

    private SwingCheckbox swingCheckBox;

    private SwingRadioButton swingRadioButton;

    private SwingDialog swingDialog;

    private SwingFrame swingFrame;

    private SwingPassword swingPassword;

    private SwingTable swingTable;

    private SwingTab swingTab;

    private SwingTextInput swingTextInput;

    private SwingTree swingTree;

    private SwingPrint swingPrint;

    private SwingJComponent swingComponent;

    private SwingMenu swingMenu;

    private SwingComboBox swingComboBox;
    
    private SwingLabel swingLabel;

    /**
     * Creates a new SwingApplet instance
     * 
     * @param process
     *            the process to start the applet in
     * @param config
     *            the configuration for the proxy
     */
    SwingAppletImpl(JavaProcess process,  SwingProxyConfiguration config) {
    	this.process = process;
        this.config = config;
    }

    /**
     * Getter for the current communication
     * 
     * @return the communication or null if none exist
     */
    public ProcessCommunication getCommunication() {
        if (communication == null) {
            logger.warning("Communication is not established.");
        }

        return communication;
    }

    @Override
    public ActionResponse execute(TestContext context, PropertyList propertyList,
            List<Metadata> metadataList, SubEngineActionType actionType) {

        ActionResponse result = TestResultHelper.createActionResponse();
        result.setMessage("Executing SwingAppletImpl, with action='" + actionType + "'");

        if (!(actionType instanceof SwingActionType)) {
            String msg = "Action type not valid for Swing execution.";
            logger.warning(msg);
            result.setActionStatus(ActionStatusType.FAILED);
            result.setErrorMessage(msg);
            return result;
        }
        
        switch ((SwingActionType) actionType) {

        case START:
        	if (metadataList == null || metadataList.isEmpty()) {
        		String msg = "No metadata found to perform SwingAction " + actionType;
        		logger.warning(msg);
        		result.setActionStatus(ActionStatusType.FAILED);
        		result.setErrorMessage(msg);
        		return result;
        	}
        	
        	TextProperty applicationProperty = (TextProperty) metadataList.get(0).getPropertyList().getPropertyList().get(0).getProperty();
            startApplet(applicationProperty.getValue().getValue());
            break;

        case STOP:
            stopApplet();
            break;
            
        default:
            logger.warning("Action type is not supported for SwingApplet.");
            status = ActionStatusType.FAILED;
        }

        if (status == null) {
            status = ActionStatusType.EXECUTED;
        }

        result.setActionStatus(status);
        return result;
    }

    private void startApplet(String appletName) {

        if (process == null) {
            logger.warning("Cannot start Swing Applet. Process not configured correctly.");
            status = ActionStatusType.FAILED;
            return;
        }
        
        if (communication != null && communication.isActive()) {
            logger.error("An Swing Applet instance is already running.");
            status = ActionStatusType.FAILED;
            return;
        }
        
        try {
        	List<URL> classpathUrls = config.getClassloaderURLs(appletName);
        	String classpath = process.prepareClasspath(classpathUrls);
        	String applet = config.getMainClass(appletName);
            
        	communication = process.startJavaProcess(classpath);

            InitCommand command = new InitCommand();
            command.setClassName(APPLET_LAUNCHER);
            
            String[] args = new String[classpathUrls.size() + 1];
            args[0] = applet;
            int i = 1;
            for (URL url : classpathUrls) {
				args[i++] = url.toString();
			}
            command.setArguments(args);

            List<CommandReply> replyList = communication.executeCommand(command);
            CommandReplyEvaluator.getInstance().evaluateReply(replyList, null);
            initComponents();
        } catch (Exception e) {
            logger.error(e, "Error starting external java process.");
            status = ActionStatusType.FAILED;
        }
    }

    private void stopApplet() {

        if (communication == null || !communication.isActive()) {
            logger.info("Applet is already down.");
            return;
        }

        ExitCommand command = new ExitCommand();
        command.setExitCode(0);

        try {
            List<CommandReply> replyList = communication.executeCommand(command);
            CommandReplyEvaluator.getInstance().evaluateReply(replyList, null);
            this.communication.close();
            destroyComponents();
        } catch (Exception e) {
            logger.error(e, "Error stopping external java process.");
            this.communication.joinThreads();
            status = ActionStatusType.FAILED;
        }
    }

    private void initComponents() {

        SwingComponentFactory scf = SwingComponentFactory.getInstance();

        this.swingButton = scf.createSwingButton(communication);
        this.swingCheckBox = scf.createSwingCheckbox(communication);
        this.swingRadioButton = scf.createSwingRadioButton(communication);
        this.swingDialog = scf.createSwingDialog(communication);
        this.swingFrame = scf.createSwingFrame(communication);
        this.swingPassword = scf.createSwingPassword(communication);
        this.swingTable = scf.createSwingTable(communication);
        this.swingTab = scf.createSwingTab(communication);
        this.swingTextInput = scf.createSwingTextInput(communication);
        this.swingTree = scf.createSwingTree(communication);
        this.swingPrint = scf.createSwingPrint(communication);
        this.swingComponent = scf.createSwingComponent(communication);
        this.swingMenu = scf.createSwingMenu(communication);
        this.swingComboBox = scf.createSwingComboBox(communication);
        this.swingLabel = scf.createSwingLabel(communication);
    }
    
    private void destroyComponents() {

        this.swingButton = null;
        this.swingCheckBox = null;
        this.swingRadioButton = null;
        this.swingDialog = null;
        this.swingFrame = null;
        this.swingPassword = null;
        this.swingTable = null;
        this.swingTab = null;
        this.swingTextInput = null;
        this.swingTree = null;
        this.swingPrint = null;
        this.swingComponent = null;
        this.swingMenu = null;
        this.swingComboBox = null;
        this.swingLabel = null;
    }

    public SwingButton getSwingButton() {
        return swingButton;
    }

    public SwingCheckbox getSwingCheckBox() {
        return swingCheckBox;
    }

    public SwingRadioButton getSwingRadioButton() {
        return swingRadioButton;
    }

    public SwingDialog getSwingDialog() {
        return swingDialog;
    }

    public SwingFrame getSwingFrame() {
    	return swingFrame;
    }

    public SwingPassword getSwingPassword() {
        return swingPassword;
    }

    public SwingTable getSwingTable() {
        return swingTable;
    }

    public SwingTab getSwingTab() {
        return swingTab;
    }

    public SwingTextInput getSwingTextInput() {
        return swingTextInput;
    }

    public SwingTree getSwingTree() {
        return swingTree;
    }

    public SwingMenu getSwingMenu() {
        return swingMenu;
    }

    public SwingComboBox getSwingComboBox() {
        return swingComboBox;
    }

	public SwingPrint getSwingPrint() {
		return swingPrint;
	}

	public SwingJComponent getSwingComponent() {
		return swingComponent;
	}
	
	public SwingLabel getSwingLabel() {
		return swingLabel;
	}

}
