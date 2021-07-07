/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwstcrpi;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author JOliver
 */
public class clsPreferences {
     
    public String getProductName() {
        String _defaultValue = "NWSTCRPI";
       
        return _defaultValue;
    }

    public String LogDir() throws Exception {
        String _path = this.RootDir();

        if (Files.notExists(Paths.get(_path, "Logs"))) {
            Files.createDirectory(Paths.get(_path, "Logs"));
        }
        return Paths.get(_path, "Logs").toString();        
    }
    
    public String TempDir() throws Exception {
        String _path = this.RootDir();

        if (Files.notExists(Paths.get(_path, "Temp"))) {
            Files.createDirectory(Paths.get(_path, "Temp"));
        }
        return Paths.get(_path, "Temp").toString();        
    }
    
    public String RootDir() throws Exception {
        String rootPath = new File(this.ExecutableDir()).getParent();
        return rootPath;
    }
    
    public String ExecutableDir() throws Exception {
        String path = Factory.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        String decodedPath = URLDecoder.decode(path, "UTF-8");
        String execDir = new File(decodedPath).getParent();
        return execDir;
    }
    
    public Integer PinSensor(){
        return 12;
    }
}