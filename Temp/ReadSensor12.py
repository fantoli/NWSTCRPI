import sys
import Adafruit_DHT
from time import sleep

# Read sensor routine
def readSensor():
	global humidity, temperature
	humidity, temperature = Adafruit_DHT.read_retry(sensor,pin)

sensor = Adafruit_DHT.DHT22

humidity = 0
temperature = 0

# Get pin and resultFileName
if len(sys.argv) - 1 != 2:
	print("Usage %s <pinNumber> <resultFileName>" % (sys.argv[0]))
	exit()
pin = int(sys.argv[1])
resultFileName = sys.argv[2]
print("Monitoring temperature and humidity @pin %i and leaving the result in %s" % (pin, resultFileName))

readSensor()
f = open(resultFileName,"wt")
f.write("{0:0.1f}|{1:0.1f}".format(temperature, humidity))
f.close()
