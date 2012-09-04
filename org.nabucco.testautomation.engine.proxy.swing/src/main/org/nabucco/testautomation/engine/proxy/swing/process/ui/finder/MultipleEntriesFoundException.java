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


/**
 * MultipleEntriesFoundException
 * 
 * @author Florian Schmidt, PRODYNA AG
 */
public class MultipleEntriesFoundException extends Exception {

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long	serialVersionUID	= 835055189084632584L;
	
	
	/**
	 * Creates a new {@link MultipleEntriesFoundException} instance.
	 *
	 */
	public MultipleEntriesFoundException() {
		super();
	}
	
	public MultipleEntriesFoundException(String message) {
		super(message);
	}
	
	public MultipleEntriesFoundException(Throwable cause) {
		super(cause);
	}
	
	public MultipleEntriesFoundException(String message, Throwable cause) {
		super(message, cause);
	}
	
	
}