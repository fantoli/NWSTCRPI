/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwstcrpi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.time.LocalTime;

/**
 *
 * @author JOliver
 */
public class clsPreferences {
    private final Properties _properties;
    private final String _propertiesFile;

    private Boolean _DHT22_Present;                    // True if there is a DHT22 attached to the RaspberryPi
    private Integer _DHT22_SensorNumber_Temperature;   // Number of the temperature sensor within all existing sensors
    private Integer _DHT22_SensorNumber_Humidity;      // Number of the humidity sensor within all existing sensors
    private Integer _DHT22_SensorPin;                  // GPIO pin number where the sensor is connected
    private Boolean _DHT22_Enabled;                    // True if DHT22 is enabled, false otherwise

    private Boolean _MQ2_Present;                      // True if there is a MQ2 attached to the RaspberryPi
    private Integer _MQ2_SensorNumber_LPG;             // Number of the LPG sensor within all existing sensors
    private Integer _MQ2_SensorNumber_CO;              // Number of the CO sensor within all existing sensors
    private Integer _MQ2_SensorNumber_SMOKE;           // Number of the SMOKE sensor within all existing sensors
    private String _MQ2_SensorPort;                    // Port used to communicate the RaspberryPi with Arduino
    private Integer _MQ2_BoardResistance;              // Board resistance in kohm
    private Double _MQ2_SensorResistance;              // Sensor resistance in kohm
    private Boolean _MQ2_Enabled;                      // True if MQ2 is enabled, false otherwise
    
    private Integer _readTimePeriod;                   // Elapsed time between each read (in seconds)
    
    private Boolean _FTPEnabled;                       // True if FTP upload is enabled, false otherwise
    private Boolean _FTPPassiveMode;                   // True if FTP connection must be passive, false otherwise
    private String _FTPUpdateDir;                      // Remote path where to deposit the reads
    private String _FTPUpdateServer;                   // URP or IP of the FTP Server
    private Boolean _FTPAnonimousConnection;           // True if the connection must be anonimous, false otherwise
    private String _FTPUsername;                       // Username to connect (if connection is not anonimous)
    private String _FTPPassword;                       // Password of previous username (if connection is not anonimous) 
    private Integer _FTPUploadPeriod;                  // Elapsed time between each upload (in seconds)

    private Boolean _mustReboot;                       // True if the RaspberryPi must reboot daily, false otherwise
    private LocalTime _rebootTime;                     // The RaspberryPi will reboot each day at that time

    public clsPreferences() throws Exception {
        _properties = new Properties();
        _propertiesFile = Paths.get(this.ExecutableDir(), this.getProductName() + ".config").toString();

        if (Files.exists(Paths.get(_propertiesFile))) {
            InputStream input = null;

            input = new FileInputStream(_propertiesFile);

            // load a properties file
            _properties.load(input);

            input.close();
        }
        this.SetDefaultValues();
    }

    public void SetDefaultValues() {
        _DHT22_Present = this.getDHT22_PresentCurrentSetting();
        _DHT22_SensorNumber_Temperature = this.getDHT22_SensorNumber_TemperatureCurrentSetting();
        _DHT22_SensorNumber_Humidity = this.getDHT22_SensorNumber_HumidityCurrentSetting();
        _DHT22_SensorPin = this.getDHT22_SensorPinCurrentSetting();
        _DHT22_Enabled = this.getDHT22_EnabledCurrentSetting();
        _MQ2_Present = this.getMQ2_PresentCurrentSetting();
        _MQ2_SensorNumber_LPG = this.getMQ2_SensorNumber_LPGCurrentSetting();
        _MQ2_SensorNumber_CO = this.getMQ2_SensorNumber_COCurrentSetting();
        _MQ2_SensorNumber_SMOKE = this.getMQ2_SensorNumber_SMOKECurrentSetting();
        _MQ2_SensorPort = this.getMQ2_SensorPortCurrentSetting();
        _MQ2_BoardResistance = this.getMQ2_BoardResistanceCurrentSetting();
        _MQ2_SensorResistance = this.getMQ2_SensorResistanceCurrentSetting();
        _MQ2_Enabled = this.getMQ2_EnabledCurrentSetting();
        _readTimePeriod = this.getReadTimePeriodCurrentSetting();
        
        _FTPEnabled = this.getFTPEnabledCurrentSetting();
        _FTPPassiveMode = this.getFTPPassiveModeCurrentSetting();
        _FTPUpdateDir = this.getFTPUpdateDirCurrentSetting();
        _FTPUpdateServer = this.getFTPUpdateServerCurrentSetting();
        _FTPAnonimousConnection = this.getFTPAnonimousConnectionCurrentSetting();
        _FTPUsername = this.getFTPUsernameCurrentSetting();
        _FTPPassword = this.getFTPPasswordCurrentSetting();
        _FTPUploadPeriod = this.getFTPUploadPeriodCurrentSetting();
        
        _mustReboot = this.getMustRebootCurrentSetting();
        _rebootTime = this.getRebootTimeCurrentSetting();
    }
    
    public void Save() throws Exception {
        _properties.setProperty("DHT22_Present", _DHT22_Present.toString());
        _properties.setProperty("DHT22_SensorNumber_Temperature", _DHT22_SensorNumber_Temperature.toString());
        _properties.setProperty("DHT22_SensorNumber_Humidity", _DHT22_SensorNumber_Humidity.toString());
        _properties.setProperty("DHT22_SensorPin", _DHT22_SensorPin.toString());
        _properties.setProperty("DHT22_Enabled", _DHT22_Enabled.toString());
        _properties.setProperty("MQ2_Present", _MQ2_Present.toString());
        _properties.setProperty("MQ2_SensorNumber_LPG", _MQ2_SensorNumber_LPG.toString());
        _properties.setProperty("MQ2_SensorNumber_CO", _MQ2_SensorNumber_CO.toString());
        _properties.setProperty("MQ2_SensorNumber_SMOKE", _MQ2_SensorNumber_SMOKE.toString());
        _properties.setProperty("MQ2_SensorPort", _MQ2_SensorPort);
        _properties.setProperty("MQ2_BoardResistance", _MQ2_BoardResistance.toString());
        _properties.setProperty("MQ2_SensorResistance", _MQ2_SensorResistance.toString());
        _properties.setProperty("MQ2_Enabled", _MQ2_Enabled.toString());
        _properties.setProperty("ReadTimePeriod", _readTimePeriod.toString());
        
        _properties.setProperty("FTPEnabled", _FTPEnabled.toString());
        _properties.setProperty("FTPPassiveMode", _FTPPassiveMode.toString());
        _properties.setProperty("FTPUpdateDir", _FTPUpdateDir);
        _properties.setProperty("FTPUpdateServer", _FTPUpdateServer);
        _properties.setProperty("FTPAnonimousConnection", _FTPAnonimousConnection.toString());
        _properties.setProperty("FTPUsername", _FTPUsername);        
        _properties.setProperty("FTPPassword", _FTPPassword);   
        _properties.setProperty("FTPUploadPeriod", _FTPUploadPeriod.toString());

        _properties.setProperty("MustReboot", _mustReboot.toString());   
        _properties.setProperty("RebootTime", _rebootTime.toString());

        File yourFile = new File(_propertiesFile);
        yourFile.createNewFile(); // if file already exists will do nothing         
	OutputStream output = null;
        output = new FileOutputStream(_propertiesFile);

        // save a properties file
        _properties.store(output, null);

        output.close();
    }

    // Current settings
    
    private Boolean getDHT22_PresentCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("DHT22_Present") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("DHT22_Present"));
       }
    }

    private Integer getDHT22_SensorNumber_TemperatureCurrentSetting() {
       Integer _defaultValue = 1;
       
       if (_properties.getProperty("DHT22_SensorNumber_Temperature") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("DHT22_SensorNumber_Temperature"));
       }
       
       
    }
    
    private Integer getDHT22_SensorNumber_HumidityCurrentSetting() {
       Integer _defaultValue = 2;
       
       if (_properties.getProperty("DHT22_SensorNumber_Humidity") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("DHT22_SensorNumber_Humidity"));
       }
    }

    private Integer getDHT22_SensorPinCurrentSetting() {
       Integer _defaultValue = 12;
       
       if (_properties.getProperty("DHT22_SensorPin") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("DHT22_SensorPin"));
       }
    }
    
    private Boolean getDHT22_EnabledCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("DHT22_Enabled") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("DHT22_Enabled"));
       }
    }
    
    private Boolean getMQ2_PresentCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("MQ2_Present") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("MQ2_Present"));
       }
    }

    private Integer getMQ2_SensorNumber_LPGCurrentSetting() {
       Integer _defaultValue = 3;
       
       if (_properties.getProperty("MQ2_SensorNumber_LPG") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("MQ2_SensorNumber_LPG"));
       }
    }

    private Integer getMQ2_SensorNumber_COCurrentSetting() {
       Integer _defaultValue = 4;
       
       if (_properties.getProperty("MQ2_SensorNumber_CO") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("MQ2_SensorNumber_CO"));
       }
    }

    private Integer getMQ2_SensorNumber_SMOKECurrentSetting() {
       Integer _defaultValue = 5;
       
       if (_properties.getProperty("MQ2_SensorNumber_SMOKE") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("MQ2_SensorNumber_SMOKE"));
       }
    }
    
    private String getMQ2_SensorPortCurrentSetting() {
       String _defaultValue = "ttyUSB0";
       
       if (_properties.getProperty("MQ2_SensorPort") == null) {
           return _defaultValue;
       } else {
           return _properties.getProperty("MQ2_SensorPort");
       }
    }
 
    private Integer getMQ2_BoardResistanceCurrentSetting() {
       Integer _defaultValue = 5;
       
       if (_properties.getProperty("MQ2_BoardResistance") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("MQ2_BoardResistance"));
       }
    }
 
    private Double getMQ2_SensorResistanceCurrentSetting() {
       Double _defaultValue = 10.0;
       
       if (_properties.getProperty("MQ2_SensorResistance") == null) {
           return _defaultValue;
       } else {
           return Double.parseDouble(_properties.getProperty("MQ2_SensorResistance"));
       }
    }
     
    private Boolean getMQ2_EnabledCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("MQ2_Enabled") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("MQ2_Enabled"));
       }
    }

    private Integer getReadTimePeriodCurrentSetting() {
       Integer _defaultValue = 5;
       
       if (_properties.getProperty("ReadTimePeriod") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("ReadTimePeriod"));
       }
    }

    private Boolean getFTPEnabledCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("FTPEnabled") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("FTPEnabled"));
       }
    }
    
    private String getFTPUpdateServerCurrentSetting() {
       String _defaultValue = "";
       
       if (_properties.getProperty("FTPUpdateServer") == null) {
           return _defaultValue;
       } else {
           return _properties.getProperty("FTPUpdateServer");
       }
    }
    
    private String getFTPUpdateDirCurrentSetting() {
       String _defaultValue = "/" + this.getProductName();
       
       if (_properties.getProperty("FTPUpdateDir") == null) {
           return _defaultValue;
       } else {
           return _properties.getProperty("FTPUpdateDir");
       }
    }
    
    private Boolean getFTPPassiveModeCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("FTPPassiveMode") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("FTPPassiveMode"));
       }
    }

    private Boolean getFTPAnonimousConnectionCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("FTPAnonimousConnection") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("FTPAnonimousConnection"));
       }
    }
        
    private String getFTPUsernameCurrentSetting() {
       String _defaultValue = "";
       
       if (_properties.getProperty("FTPUsername") == null) {
           return _defaultValue;
       } else {
           return _properties.getProperty("FTPUsername");
       }
    }
            
    private String getFTPPasswordCurrentSetting() {
       String _defaultValue = "";
       
       if (_properties.getProperty("FTPPassword") == null) {
           return _defaultValue;
       } else {
           return _properties.getProperty("FTPPassword");
       }
    }

    private Integer getFTPUploadPeriodCurrentSetting() {
       Integer _defaultValue = 30;
       
       if (_properties.getProperty("FTPUploadPeriod") == null) {
           return _defaultValue;
       } else {
           return Integer.parseInt(_properties.getProperty("FTPUploadPeriod"));
       }
    }
    
    private Boolean getMustRebootCurrentSetting() {
       Boolean _defaultValue = true;
       
       if (_properties.getProperty("MustReboot") == null) {
           return _defaultValue;
       } else {
           return Boolean.parseBoolean(_properties.getProperty("MustReboot"));
       }
    }
    
        
    private LocalTime getRebootTimeCurrentSetting() {
       LocalTime _defaultValue = LocalTime.parse("00:00:00");
       
       if (_properties.getProperty("RebootTime") == null) {
           return _defaultValue;
       } else {
           return LocalTime.parse(_properties.getProperty("RebootTime"));
       }
    }

    // Functions
    
    public String getProductName() {
        String _defaultValue = "NWSTCRPI";
       
        return _defaultValue;
    }

    public String getVersion() {
        String _defaultValue = "v1.0.0";
       
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
    
    public String DataDir() throws Exception {
        String _path = this.RootDir();

        if (Files.notExists(Paths.get(_path, "Data"))) {
            Files.createDirectory(Paths.get(_path, "Data"));
        }
        return Paths.get(_path, "Data").toString();        
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
    
    // Properties
   
    public Boolean getDHT22_Present() {
        return _DHT22_Present;
    }
    public void setDHT22_Present(Boolean value) {
        _DHT22_Present = value;
    }
    
    public Integer getDHT22_SensorNumber_Temperature(){
        return _DHT22_SensorNumber_Temperature;
    }
    
    public void setDHT22_SensorNumber_Temperature(Integer temp) {
        _DHT22_SensorNumber_Temperature = temp;
    }
    
    public Integer getDHT22_SensorNumber_Humidity(){
        return _DHT22_SensorNumber_Humidity;
    }
    
    public void setDHT22_SensorNumber_Humidity(Integer humidity) {
        _DHT22_SensorNumber_Humidity = humidity;
    }
    
    public Integer getDHT22_SensorPin(){
        return _DHT22_SensorPin;
    }
    public void setDHT22_SensorPin(Integer sensorPin) throws Exception {
        if (sensorPin <= 0) {
            throw new Exception("Sensor pin number must be a positive value");
        }
        _DHT22_SensorPin = sensorPin;
    }
    
    public Boolean getDHT22_Enabled() {
        return _DHT22_Enabled;
    }
    public void setDHT22_Enabled(Boolean enabled) {
        _DHT22_Enabled = enabled;
    }
    
    public Boolean getMQ2_Present() {
        return _MQ2_Present;
    }
    
    public void setMQ2_Present(Boolean present){
        _MQ2_Present = present;
    }
    
    public Integer getMQ2_SensorNumber_LPG(){
        return _MQ2_SensorNumber_LPG;
    }
    
    public void setMQ2_SensorNumber_LPG(Integer sensorLPG){
        _MQ2_SensorNumber_LPG = sensorLPG;
    }
    
    public Integer getMQ2_SensorNumber_CO(){
        return _MQ2_SensorNumber_CO;
    }
    
    public void setMQ2_SensorNumber_CO(Integer sensorCO){
        _MQ2_SensorNumber_CO = sensorCO;
    }
    
    public Integer getMQ2_SensorNumber_SMOKE(){
        return _MQ2_SensorNumber_SMOKE;
    }
    
    public void setMQ2_SensorNumber_SMOKE(Integer sensorSMOKE){
        _MQ2_SensorNumber_SMOKE = sensorSMOKE;
    }

    public String getMQ2_SensorPort() {
        return _MQ2_SensorPort;
    }
    
    public void setMQ2_SensorPort(String port) {
        _MQ2_SensorPort = port;
    }
    
    public Integer getMQ2_BoardResistance(){
        return _MQ2_BoardResistance;
    }
    
    public void setMQ2_BoardResistance(Integer board){
        _MQ2_BoardResistance = board;
    }
    
    public Double getMQ2_SensorResistance(){
        return _MQ2_SensorResistance;
    }
    public void setMQ2_SensorResistance(Double value) throws Exception {
        if (value <= 0) {
            throw new Exception("Sensor resistance must be greater than zero");
        }
        _MQ2_SensorResistance = value;
    }
    
    public Boolean getMQ2_Enabled() {
        return _MQ2_Enabled;
    }
    public void setMQ2_Enabled(Boolean enabled) {
        _MQ2_Enabled = enabled;
    }
    
    public Integer getReadTimePeriod() {
        // Time between reads of sensor values
        return _readTimePeriod;
    }
    
    public void setReadTimePeriod(Integer readTime){
        _readTimePeriod = readTime;
    }
    
    public Boolean getFTPEnabled() {
        return _FTPEnabled;
    }
    
    public void setFTPEnabled(Boolean enabled){
        _FTPEnabled = enabled;
    }
    
    public Boolean getFTPPassiveMode() {
        return _FTPPassiveMode;
    }
    
    public void setFTPPassiveMode(Boolean passive) {
        _FTPPassiveMode = passive;
    }
    
    public String getFTPUpdateDir() {
        return _FTPUpdateDir;
    }
    
    public void setFTPUpdateDir(String updateDir){
        _FTPUpdateDir = updateDir;
    }
    
    public String getFTPUpdateServer() {
        return _FTPUpdateServer;
    }
    
    public void setFTPUpdateServer(String updateServer){
        _FTPUpdateServer = updateServer;
    }
    
    public Boolean getFTPAnonimousConnection() {
        return _FTPAnonimousConnection;
    }
    
    public void setFTPAnonimousConnection(Boolean anonimous){
        _FTPAnonimousConnection = anonimous;
    }
    
    public String getFTPUsername() {
        return _FTPUsername;
    }
    
    public void setFTPUsername(String username){
        _FTPUsername = username;
    }
    
    public String getFTPPassword() {
        return _FTPPassword;
    }
    
    public void setFTPPassword(String pass){
        _FTPPassword = pass;
    }
    
    public Integer getFTPUploadPeriod() {
        return _FTPUploadPeriod;
    }
    
    public void setFTPUploadPeriod(Integer uploadPeriod){
        _FTPUploadPeriod = uploadPeriod;
    }
    
    public Boolean getMustReboot() {
        return _mustReboot;
    }
    
    public void setMustReboot(Boolean mustReboot){
        _mustReboot = mustReboot;
    }
    
    public LocalTime getRebootTime() {
        return _rebootTime;
    }   
    
    public void setReboot(LocalTime rebootTime){
        _rebootTime = rebootTime;
    }
}