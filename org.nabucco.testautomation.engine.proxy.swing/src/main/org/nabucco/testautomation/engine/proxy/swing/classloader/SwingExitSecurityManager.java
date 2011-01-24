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
package org.nabucco.testautomation.engine.proxy.swing.classloader;

import java.io.FileDescriptor;
import java.net.InetAddress;
import java.security.Permission;

/**
 * SwingExitSecurityManager
 * 
 * @author Nicolas Moser, PRODYNA AG
 */
class SwingExitSecurityManager extends SecurityManager {

    /**
     * Original Security manager.
     */
    private SecurityManager delegate;

    /**
     * Constructor.
     * 
     * @param delegate
     *            the security manager delegate.
     */
    public SwingExitSecurityManager(final SecurityManager delegate) {
        super();
        this.delegate = delegate;
    }

    @Override
    public void checkExit(final int status) {
        if (delegate != null) {
            delegate.checkExit(status);
        } else {
            throw new SwingExitException("System.exit() called during simulation.");
        }
    }

    @Override
    public void checkPermission(final Permission perm) {
        if (delegate != null) {
            delegate.checkPermission(perm);
        }
    }

    @Override
    public void checkAccept(final String host, final int port) {
        if (delegate != null) {
            delegate.checkAccept(host, port);
        }
    }

    @Override
    public void checkAccess(final Thread t) {
        if (delegate != null) {
            delegate.checkAccess(t);
        }
    }

    @Override
    public void checkAccess(final ThreadGroup g) {
        if (delegate != null) {
            delegate.checkAccess(g);
        }
    }

    @Override
    public void checkAwtEventQueueAccess() {
        if (delegate != null) {
            delegate.checkAwtEventQueueAccess();
        }
    }

    @Override
    public void checkConnect(final String host, final int port) {
        if (delegate != null) {
            delegate.checkConnect(host, port);
        }
    }

    @Override
    public void checkConnect(final String host, final int port, final Object context) {
        if (delegate != null) {
            delegate.checkConnect(host, port, context);
        }
    }

    @Override
    public void checkCreateClassLoader() {
        if (delegate != null) {
            delegate.checkCreateClassLoader();
        }
    }

    @Override
    public void checkDelete(final String file) {
        if (delegate != null) {
            delegate.checkDelete(file);
        }
    }

    @Override
    public void checkExec(final String cmd) {
        if (delegate != null) {
            delegate.checkExec(cmd);
        }
    }

    @Override
    public void checkLink(final String lib) {
        if (delegate != null) {
            delegate.checkLink(lib);
        }
    }

    @Override
    public void checkListen(final int port) {
        if (delegate != null) {
            delegate.checkListen(port);
        }
    }

    @Override
    public void checkMemberAccess(final Class<?> clazz, final int which) {
        if (delegate != null) {
            delegate.checkMemberAccess(clazz, which);
        }
    }

    @Override
    public void checkMulticast(final InetAddress maddr) {
        if (delegate != null) {
            delegate.checkMulticast(maddr);
        }
    }

    @Override
    public void checkPackageAccess(final String pkg) {
        if (delegate != null) {
            delegate.checkPackageAccess(pkg);
        }
    }

    @Override
    public void checkPackageDefinition(final String pkg) {
        if (delegate != null) {
            delegate.checkPackageDefinition(pkg);
        }
    }

    @Override
    public void checkPermission(final Permission perm, final Object context) {
        if (delegate != null) {
            delegate.checkPermission(perm, context);
        }
    }

    @Override
    public void checkPrintJobAccess() {
        if (delegate != null) {
            delegate.checkPrintJobAccess();
        }
    }

    @Override
    public void checkPropertiesAccess() {
        if (delegate != null) {
            delegate.checkPropertiesAccess();
        }
    }

    @Override
    public void checkPropertyAccess(final String key) {
        if (delegate != null) {
            delegate.checkPropertyAccess(key);
        }
    }

    @Override
    public void checkRead(final FileDescriptor fd) {
        if (delegate != null) {
            delegate.checkRead(fd);
        }
    }

    @Override
    public void checkRead(final String file) {
        if (delegate != null) {
            delegate.checkRead(file);
        }
    }

    @Override
    public void checkRead(final String file, final Object context) {
        if (delegate != null) {
            delegate.checkRead(file, context);
        }
    }

    @Override
    public void checkSecurityAccess(final String target) {
        if (delegate != null) {
            delegate.checkSecurityAccess(target);
        }
    }

    @Override
    public void checkSetFactory() {
        if (delegate != null) {
            delegate.checkSetFactory();
        }
    }

    @Override
    public void checkSystemClipboardAccess() {
        if (delegate != null) {
            delegate.checkSystemClipboardAccess();
        }
    }

    @Override
    public boolean checkTopLevelWindow(final Object window) {
        if (delegate != null) {
            return delegate.checkTopLevelWindow(window);
        }
        return false;
    }

    @Override
    public void checkWrite(final FileDescriptor fd) {
        if (delegate != null) {
            delegate.checkWrite(fd);
        }
    }

    @Override
    public void checkWrite(final String file) {
        if (delegate != null) {
            delegate.checkWrite(file);
        }
    }

    @Override
    public Object getSecurityContext() {
        if (delegate != null) {
            return delegate.getSecurityContext();
        }
        return "";
    }

    @Override
    public ThreadGroup getThreadGroup() {
        if (delegate != null) {
            return delegate.getThreadGroup();
        }
        return null;
    }

    /**
     * Getter for the delegate security manager.
     * 
     * @return the delegate
     */
    public SecurityManager getDelegate() {
        return delegate;
    }

}
