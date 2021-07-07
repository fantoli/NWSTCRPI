/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DHT22;

import com.pi4j.io.gpio.Pin;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import ProcessControl.*;

/**
 *
 * @author JOliver
 */
public class clsDHT22 {
    private static Integer MAX_READ_FREQUENCY_IN_MILLISECONDS = 5000;
    private Integer _pinNumber;
    private String _tempDir;
    private Timestamp  _lastRead;
    private Double _temperature;
    private Double _humidity;
    private Boolean _reading;
    
    public clsDHT22(Pin pin, String tempDir) throws Exception {
        _pinNumber = pin.getAddress();
        _tempDir = tempDir;
        _lastRead = this.subtractMilliseconds(new Timestamp(new Date().getTime()), MAX_READ_FREQUENCY_IN_MILLISECONDS);
        _reading = false;
        this.performRead();
    }
    
    private String getTempFilename() {
        // Devuelve el nombre del fichero python que se ejecutará
        String _tempFilename = Paths.get(_tempDir, "ReadSensor" + _pinNumber.toString() + ".py").toString();
        return _tempFilename;
    }

    private String getResultFilename() {
        // Devuelve el nombre del fichero en el que se dejarán los resultados
        String _tempFilename = Paths.get(_tempDir, "ReadSensor" + _pinNumber.toString() + ".read").toString();
        return _tempFilename;
    }
    
    private Timestamp subtractMilliseconds(Timestamp timestamp, Integer milliseconds) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timestamp.getTime());

        // subtract "seconds" seconds
        cal.add(Calendar.MILLISECOND, milliseconds*-1);
        Timestamp result = new Timestamp(cal.getTime().getTime());
        return result;
    }
    
    private long timeDifference(Timestamp date1, Timestamp date2) {
        // Returns the time difference in milliseconds
        long _timeDifference = date2.getTime() - date1.getTime(); 
        
        return _timeDifference;
    }
    
    public Double getTemperature() throws Exception {
        this.performRead();
        return _temperature;
    }
    
    public Double getHumidity() throws Exception {
        this.performRead();
        return _humidity;
    }
    
    private void performRead() throws Exception {
        if (!_reading) {
        _reading = true;

        // Calculate the elapsed time from last read
        Calendar cal = Calendar.getInstance();
        long _elapsedTimeFromLastRead = this.timeDifference(_lastRead, new Timestamp(cal.getTime().getTime()));
        if (_elapsedTimeFromLastRead > MAX_READ_FREQUENCY_IN_MILLISECONDS) {
            // The read is obsolete, a new one will be performed
            String _tempFilename = this.getTempFilename();
            String _resultFilename = this.getResultFilename();
            this.createCMD(_tempFilename);
            String _pythonPrg = "";
            if (ProcessControlFactory.ProcessMgr().getOS().equals("windows")) {
                // Windows
                _pythonPrg = "python";
            } else {
                // Linux
                _pythonPrg = "python3";
            }
            ProcessControlFactory.ProcessMgr().executeCommand(_pythonPrg + " " +_tempFilename + " " + _pinNumber.toString() + " " + _resultFilename, _tempDir);
            File _file = new File(_tempFilename);
            _file.delete();
            this.readResultFile(_resultFilename);
            _lastRead = new Timestamp(cal.getTime().getTime());
        }
        _reading = false;            
        }
    }

   
    private void createCMD(String fileName) throws Exception {
        //import sys
        //import Adafruit_DHT
        //from time import sleep
        //
        //# Read sensor routine
        //def readSensor():
        //	global humidity, temperature
        //	humidity, temperature = Adafruit_DHT.read_retry(sensor,pin)
        //
        //sensor = Adafruit_DHT.DHT22
        //
        //humidity = 0
        //temperature = 0
        //
        //# Get pin and resultFileName
        //if len(sys.argv) - 1 != 2:
        //	print("Usage %s <pinNumber> <resultFileName>" % (sys.argv[0]))
        //	exit()
        //pin = int(sys.argv[1])
        //resultFileName = sys.argv[2]
        //print("Monitoring temperature and humidity @pin %i and leaving the result in %s" % (pin, resultFileName))
        //
        //readSensor()
        //f = open(resultFileName,"wt")
        //f.write("{0:0.1f}|{1:0.1f}".format(temperature, humidity))
        //f.close()
        
        File _file = new File(fileName);

        FileWriter _writer;
        _writer = new FileWriter(_file, false);
        PrintWriter _printer = new PrintWriter(_writer);
        _printer.append("import sys\r\n");
        _printer.append("import Adafruit_DHT\r\n");
        _printer.append("from time import sleep\r\n");
        _printer.append("\r\n");
        _printer.append("# Read sensor routine\r\n");
        _printer.append("def readSensor():\r\n");
        _printer.append("	global humidity, temperature\r\n");
        _printer.append("	humidity, temperature = Adafruit_DHT.read_retry(sensor,pin)\r\n");
        _printer.append("\r\n");
        _printer.append("sensor = Adafruit_DHT.DHT22\r\n");
        _printer.append("\r\n");
        _printer.append("humidity = 0\r\n");
        _printer.append("temperature = 0\r\n");
        _printer.append("\r\n");
        _printer.append("# Get pin and resultFileName\r\n");
        _printer.append("if len(sys.argv) - 1 != 2:\r\n");
        _printer.append("	print(\"Usage %s <pinNumber> <resultFileName>\" % (sys.argv[0]))\r\n");
        _printer.append("	exit()\r\n");
        _printer.append("pin = int(sys.argv[1])\r\n");
        _printer.append("resultFileName = sys.argv[2]\r\n");
        _printer.append("print(\"Monitoring temperature and humidity @pin %i and leaving the result in %s\" % (pin, resultFileName))\r\n");
        _printer.append("\r\n");
        _printer.append("readSensor()\r\n");
        _printer.append("f = open(resultFileName,\"wt\")\r\n");
        _printer.append("f.write(\"{0:0.1f}|{1:0.1f}\".format(temperature, humidity))\r\n");
        _printer.append("f.close()\r\n");
        _printer.close();
    }
    
    private void readResultFile(String filename) throws Exception {
        BufferedReader _br = new BufferedReader(new FileReader(filename));
    
        try {
            String _line = _br.readLine();
            // La línea contiene las lecturas de temperatura y humedad separadas por |
            String[] _values = _line.split("\\|");
            String _temperatureString = _values[0];
            String _humidityString = _values[1];
            _temperature = Double.parseDouble(_temperatureString);
            _humidity = Double.parseDouble(_humidityString);
        } finally {
            _br.close();
            File _file = new File(filename);
            _file.delete();
        }
    }
}