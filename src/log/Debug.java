/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package log;

import com.smartfoxserver.v2.extensions.ExtensionLogLevel;
import com.smartfoxserver.v2.extensions.SFSExtension;

/**
 *
 * @author daz
 */
public class Debug {

    private static SFSExtension ext;
    public static boolean isDebug = true;

    public static void setExt(SFSExtension extension) {
        System.out.println("ext :" + extension.getName());
        ext = extension;
    }

    public static void d(String message) {
        if (ext != null) {
            ext.trace(ExtensionLogLevel.DEBUG, "================================" + message);
        } else {
            System.out.println(message);
        }
    }

    public static void console(String message) {
        System.out.println(message);
    }

    public static void i(String message) {
        if (ext != null) {
            ext.trace(ExtensionLogLevel.INFO, message);
        } else {
            System.out.println(message);
        }
    }

    public static void line(String message) {
        if (ext != null) {
            ext.trace(ExtensionLogLevel.INFO, "******************" + message + "********************");
        } else {
            System.out.println(message);
        }
    }

    public static void n(String string) {
        System.out.print(string + ",");
    }
}
