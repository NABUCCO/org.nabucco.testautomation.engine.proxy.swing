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
package org.nabucco.testautomation.engine.proxy.swing.process.reply;

import java.util.List;

import org.apache.log4j.Level;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLogger;
import org.nabucco.framework.base.facade.datatype.logger.NabuccoLoggingFactory;
import org.nabucco.testautomation.property.facade.datatype.base.Property;
import org.nabucco.testautomation.result.facade.datatype.ActionResponse;
import org.nabucco.testautomation.result.facade.datatype.status.ActionStatusType;

/**
 * CommandReplyEvaluator
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class CommandReplyEvaluator {

    private static NabuccoLogger logger = NabuccoLoggingFactory.getInstance().getLogger(
            CommandReplyEvaluator.class);

    /**
     * Singleton instance.
     */
    private static CommandReplyEvaluator instance = new CommandReplyEvaluator();

    /**
     * Private constructor.
     */
    private CommandReplyEvaluator() {
    }

    /**
     * Singleton access.
     * 
     * @return the CommandReplyEvaluator instance.
     */
    public static CommandReplyEvaluator getInstance() {
        return instance;
    }

    /**
     * Evaluates a list of {@link CommandReply} instances. A {@link LogReply} will be logged
     * directly, the property of a {@link PropertyReply} will be returned and an
     * {@link ExceptionReply} will result in a raised exception.
     * 
     * @param replyList
     *            the list of {@link CommandReply} instances.
     * @param result
     *            the current execution result
     * 
     * @return a property if one exists.
     * 
     * @throws Exception
     *             if an {@link ExceptionReply} exists.
     */
    public Property evaluateReply(List<CommandReply> replyList, ActionResponse result)
            throws Exception {

        Property property = null;

        Boolean success = null;

        for (CommandReply reply : replyList) {

            switch (reply.getType()) {

            case LOG: {
                LogReply logReply = (LogReply) reply;
                this.log(logReply.getLevel(), logReply.getMessage(), logReply.getException());
                break;
            }

            case EXCEPTION: {
                ExceptionReply exceptionReply = (ExceptionReply) reply;
                Exception exception = exceptionReply.getException();
                logger.fatal(exception, "Error in external process.");
                throw new Exception("Error in external process.", exception);
            }

            case PROPERTY: {
                PropertyReply propertyReply = (PropertyReply) reply;
                property = propertyReply.getProperty();
                break;
            }

            case FINAL: {
                FinalReply finalReply = (FinalReply) reply;
                success = finalReply.getSuccess();
                
                if (success) {
                	logger.info(finalReply.getMessage());
                	
                	if (result != null) {
                		result.setMessage(finalReply.getMessage());
                	}
                } else {
                	logger.error(finalReply.getMessage());
                	
                	if (result != null) {
                		result.setErrorMessage(finalReply.getMessage());
                	}
                }
                break;
            }

            case NOR: {
                break; // Do nothing
            }

            default:
                logger.warning("CommandReplyType is not supported " + reply);
            }
        }

        evaluateResult(result, success);

        return property;
    }

    private void evaluateResult(ActionResponse result, Boolean success) {

        if (success == null) {
            logger.warning("Execution timeout.");
            
            if (result != null) {
                result.setActionStatus(ActionStatusType.FAILED);
                result.setErrorMessage("Execution timeout.");
            }
            return;
        }

        if (success) {
            logger.info("Execution completed successfully.");
        } else {
            logger.warning("Execution completed with failure.");
            if (result != null) {
            	result.setActionStatus(ActionStatusType.FAILED);
            }
        }
    }

    /**
     * Loggs the appropriate {@link LogReply} information.
     * 
     * @param level
     *            the log level
     * @param msg
     *            the log message
     * @param e
     *            the exception (optional)
     * @param result
     *            the current execution result
     * 
     * @return <b>true</b> if the execution was successful, <b>false</b> otherwise
     */
    private void log(Integer level, String msg, Exception e) {

        if (e == null) {
            switch (level) {
            case Level.FATAL_INT:
                logger.fatal(msg);
                break;
            case Level.ERROR_INT:
                logger.error(msg);
                break;
            case Level.WARN_INT:
                logger.warning(msg);
                break;
            case Level.INFO_INT:
                logger.info(msg);
                break;
            case Level.DEBUG_INT:
                logger.debug(msg);
                break;
            case Level.TRACE_INT:
                logger.trace(msg);
                break;
            }

        } else {
            switch (level) {
            case Level.FATAL_INT:
                logger.fatal(e, msg);
                break;
            case Level.ERROR_INT:
                logger.error(e, msg);
                break;
            case Level.WARN_INT:
                logger.warning(e, msg);
                break;
            case Level.INFO_INT:
                logger.info(e, msg);
                break;
            case Level.DEBUG_INT:
                logger.debug(e, msg);
                break;
            case Level.TRACE_INT:
                logger.trace(msg);
            }
        }
    }

}
