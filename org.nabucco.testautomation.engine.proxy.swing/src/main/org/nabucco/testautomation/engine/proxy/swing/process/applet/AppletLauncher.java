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
package org.nabucco.testautomation.engine.proxy.swing.process.applet;

import java.applet.Applet;
import java.net.URL;
import java.net.URLClassLoader;

import javax.swing.JFrame;

/**
 * AppletLauncher
 * 
 * @author Steffen Schmidt, PRODYNA AG
 */
public class AppletLauncher extends JFrame {

	private static final long serialVersionUID = 1L;

	private String appletName;
	
	private URL[] urls;
	
	public AppletLauncher(String appletName, URL[] urls) {
		this.appletName = appletName;
		this.urls = urls;
	}
	
	public void launch() throws Exception {
		
		URLClassLoader cl = new URLClassLoader(urls, ClassLoader.getSystemClassLoader());
		Applet applet = (Applet) cl.loadClass(appletName).newInstance();
		applet.init();
		
		setTitle(appletName);
		setName(appletName);
		add(applet);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
		pack();		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {

		// read arguments
		String appletName = args[0];
		URL[] urls = new URL[args.length - 1];

		for (int i = 1; i < args.length; i++) {
			urls[i - 1] = new URL(args[i]);
		}
		
		// launch Applet
		new AppletLauncher(appletName, urls).launch();
	}

}
