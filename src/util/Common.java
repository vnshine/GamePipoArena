/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author daz
 */
public class Common {

    private static Random random = new Random();

    public static int nextInt(int limit) {
        return random.nextInt(limit);
    }

    public static int nextInt(int x, int y) {
        int r = 0;
        do {
            r = random.nextInt(y);
        } while (r < x);
        return random.nextInt(r);
    }

    public static String readFile(String filename) throws IOException {
        File file = new File(filename);
        int len = (int) file.length();
        byte[] bytes = new byte[len];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            assert len == fis.read(bytes);
        } catch (IOException e) {
            close(fis);
            throw e;
        }
        return new String(bytes, "UTF-8");
    }

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException ignored) {
        }
    }

    private static void copyfile(String srFile, String dtFile) {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);

            //For Append the file.
//  OutputStream out = new FileOutputStream(f2,true);

            //For Overwrite the file.
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addToFile(String content, String name) {
        BufferedReader bf = null;
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File("temp_" + name))));
            bf = new BufferedReader(new InputStreamReader(new FileInputStream(name), "UTF-8"));
            String str;
            while ((str = bf.readLine()) != null) {
                pw.println(str);
            }
            pw.println(content);
            pw.flush();
            pw.close();
            copyfile("temp_" + name, name);

        } catch (FileNotFoundException ex) {
            try {
                PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(name))));
                pw.println(content);
                pw.flush();
                pw.close();
            } catch (IOException ex1) {
                Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
        } finally {
            try {
                if (bf != null) {
                    bf.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        addToFile("aaaaa", "report.txt");
    }
}
