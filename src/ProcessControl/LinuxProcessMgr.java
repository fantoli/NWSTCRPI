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
public class LinuxProcessMgr implements iProcessMgr {
      
    public String getOS() {
        return "linux";
    }
    
    public void executeCommand(String cmd, String tempDir) throws Exception {
            // Creamos el fichero de comandos a ejecutar
        String _filename = this.getCommandFilename(tempDir);
        File _file = new File(_filename);

        FileWriter _writer;
        _writer = new FileWriter(_file, false);
        PrintWriter _printer = new PrintWriter(_writer);
        _printer.append(cmd);
        _printer.close();
        _writer.close();
        _file.setExecutable(true);

        // Y lo ejecutamos
        Process proc = null;
        String[] myCommand = {"/bin/bash", _filename};

        proc = Runtime.getRuntime().exec(myCommand);
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
         _file.delete();
    }

    private String getCommandFilename(String tempDir) {
        long _timestamp = System.currentTimeMillis();
        String _now = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(_timestamp);

        return Paths.get(tempDir, "ExecuteCommnand_" + _now).toString();
    }
}
