/*
 * Copyright 2012 PRODYNA AG
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
package org.nabucco.testautomation.engine.proxy.swing.process.ui.finder;

import java.awt.Component;

import junit.extensions.jfcunit.finder.AbstractWindowFinder;


/**
 * AllWindowLikeElementFinder
 * 
 * @author Florian Schmidt, PRODYNA AG
 */
public class AllWindowLikeElementFinder extends AbstractWindowFinder {
    
    /**
     * Constructor accepting all arguments needed to filter component.
     * Since a JWindow does not have any attribute that can be used for filtering,
     * the "default" constructor inherited from AbstractWindowFinder is "hidden".
     */
    public AllWindowLikeElementFinder() {
        super(null, true);
    }

    /**
     * Method that returns true if the given component matches the search
     * criteria.
     *
     * @param comp   The component to test
     * @return true if this component is a match
     */
    public boolean testComponent(final Component comp) {
        return ((comp != null) && (comp.isShowing()));
    }
    
}
