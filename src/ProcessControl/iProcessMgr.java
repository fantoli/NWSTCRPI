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
public interface iProcessMgr {
    public void executeCommand(String cmd, String tempDir) throws Exception;
    
    public String getOS();
}
