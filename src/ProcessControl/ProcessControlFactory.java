/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProcessControl;

/**
 *
 * @author JOliver
 */
public class ProcessControlFactory {
    private static iProcessMgr _processMgr;
    
    public static iProcessMgr ProcessMgr() throws Exception {
        if (_processMgr == null) {
            String _OS = System.getProperty("os.name");
            if (_OS.toLowerCase().contains("windows")) {
                _processMgr = new WindowsProcessMgr();
            } else {
                if (_OS.toLowerCase().contains("mac")) {
                    throw new Exception("Sistema operativo no soportado: " + _OS);
                } else {
                    if (_OS.toLowerCase().contains("linux")) {
                        _processMgr = new LinuxProcessMgr();
                    } else {
                        throw new Exception("Sistema operativo no soportado: " + _OS);
                    }
                }
            }
        }
        return _processMgr;
    }
}
