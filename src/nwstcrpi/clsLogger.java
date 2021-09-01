/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwstcrpi;

/**
 *
 * @author JoanOliver
 */
import java.io.*;
import java.nio.file.Paths;
//import org.apache.commons.io.FileUtils;
import java.text.*;

public class clsLogger {
    private String _appName = "";
    private String _logDir = "";
    
    public clsLogger(String appName, String logDir) throws Exception {
        _appName = appName;
        _logDir = logDir;
        // Por el tipo de programa, borramos el contenido del directorio de log
        //FileUtils.cleanDirectory(new File(_logDir));
        this.deleteFilesOlderThanNdays(30, _logDir);
    }
    
    public void deleteFilesOlderThanNdays(int daysBack, String dirWay) {
        File directory = new File(dirWay);
        if(directory.exists()){
            File[] listFiles = directory.listFiles();           
            long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
            for(File listFile : listFiles) {
                if(listFile.lastModified() < purgeTime) {
                    if(!listFile.delete()) {
                        System.err.println("Unable to delete file: " + listFile);
                    }
                }
            }
        }
    }
    
    public void log(String msg) {
        try {
            long _timestamp = System.currentTimeMillis();
            String _today = new SimpleDateFormat("yyyyMMdd").format(_timestamp);
            String _now = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(_timestamp);

            String _logFilename = Paths.get(_logDir, _appName + "_" + _today + ".log").toString();
            File _logFile = new File(_logFilename);

            FileWriter _writer;
            _writer = new FileWriter(_logFile, true);
            PrintWriter _printer = new PrintWriter(_writer);
            _printer.append("[" + _now + "] > " + msg + "\r\n");
            _printer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void log(Exception ex) {
        try {
            StringWriter _error = new StringWriter();
            ex.printStackTrace(new PrintWriter(_error));
            this.log(_error.toString());
        } catch (Exception e) {
            // El log no puede escribirse, realizamos el dump por consola
            e.printStackTrace();
        }
    }
    
    public String getLogFolder() {
        return _logDir;
    }
}
