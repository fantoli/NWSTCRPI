/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessControl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;

/**
 *
 * @author JOliver
 */
public class WindowsProcessMgr implements iProcessMgr {
             
    public String getOS() {
        return "windows";
    }

    public void executeCommand(String cmd, String tempDir) throws Exception {
        Process proc = null;

        proc = Runtime.getRuntime().exec("cmd " + cmd);
        while (proc.isAlive()) {
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch(InterruptedException ex) {
                throw ex;
            }
        };
        Integer _sts = proc.exitValue();
         if (_sts != 0) {
             // 0 indica finalización normal
             throw new Exception("El comando finalizó con error " + _sts.toString());
         }
    }
}
