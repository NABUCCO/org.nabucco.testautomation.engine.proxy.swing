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

import org.nabucco.testautomation.facade.datatype.property.base.Property;


/**
 * This {@link CommandReply} contains a {@link Property} to evaluate on receiver side.
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
public class PropertyReply extends CommandReplySupport implements CommandReply {

    private static final long serialVersionUID = 1L;

    /**
     * Creates an {@link PropertyReply} instance.
     */
    public PropertyReply() {
        setType(CommandReplyType.PROPERTY);
    }

    private Property property;

    /**
     * @return the property
     */
    public Property getProperty() {
        return property;
    }

    /**
     * @param property
     *            the property to set
     */
    public void setProperty(Property property) {
        this.property = property;
    }

}
