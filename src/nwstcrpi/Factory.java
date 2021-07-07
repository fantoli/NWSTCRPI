/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwstcrpi;

/**
 *
 * @author JOliver
 */
public class Factory {
    private static clsLogger _logger;
    private static clsPreferences _preferences;

    public static clsLogger GetLogger() {
        if (_logger == null) {
            try {
                _logger = new clsLogger(Factory.GetPreferences().getProductName(), Factory.GetPreferences().LogDir());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return _logger;
    }

    public static clsPreferences GetPreferences() {
        if (_preferences == null) {
            try {
                _preferences = new clsPreferences();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return _preferences;
    }
 }
