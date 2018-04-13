#include <SPI.h>
#include <Ethernet.h>

byte mac[] = {
  0xAE, 0xFE, 0xBC, 0xEE, 0xAC, 0xDF
};
IPAddress ip(192, 168, 1, 56);
IPAddress myDns(192, 168, 1, 1);
IPAddress gateway(192, 168, 1, 1);
IPAddress subnet(255, 255, 255, 0);
// telnet defaults to port 23
EthernetServer server(23);

int RELAY = 6;
int LED = 8;
int digitCounter = 0;
char inData = 0;                //Variable for storing received data
String receivedMessage = "";         //Variable for storing 4 digit receivedMessage
String passCode = "1550";
bool readyForreceivedMessage = false;

void setup() {
  // initialize the ethernet device
  Ethernet.begin(mac, ip, myDns, gateway, subnet);
  pinMode(RELAY, OUTPUT);        //Sets digital pin 4 as output pin
  pinMode(LED, OUTPUT);        //Sets digital pin 11 as output pin
  digitalWrite(RELAY, LOW);   //Turn off gate relay
  digitalWrite(LED, LOW);   //Turn off led
  // start listening for clients
  server.begin();
  // Open serial communications and wait for port to open:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }
}

void loop() {
  // wait for a new client:
  EthernetClient client = server.available();
  if (client.available()) {
    inData = client.read();      //Read the incoming data and store it into variable data
    if (readyForreceivedMessage)
    {
      receivedMessage += inData;
      digitCounter++;
      if (digitCounter == 4) {
        if (receivedMessage == passCode)   // If the receivedMessage is corrent then open gate
        {
          Serial.println("Valid receivedMessage, Opening Gate: " + receivedMessage);
          server.println("1");
          digitalWrite(RELAY, HIGH);  //Turn on gate relay
          digitalWrite(LED, HIGH);  //Turn on led
          delay(1000);
          digitalWrite(RELAY, LOW);   //Turn off gate relay
          digitalWrite(LED, LOW);   //Turn off led
        }
        else
        {
          Serial.println("Invalid receivedMessage: " + receivedMessage);
          server.println("0");
          for (int i = 0; i <= 3; i++)
          {
            digitalWrite(LED, HIGH);   // turn the LED on (HIGH is the voltage level)
            delay(100);                       // wait for a second
            digitalWrite(LED, LOW);    // turn the LED off by making the voltage LOW
            delay(100);
          }
        }
        client.flush();
        receivedMessage = "";
        digitCounter = 0;
        readyForreceivedMessage = false;
      }
    }
    if (inData == '$')
    {
      readyForreceivedMessage = true;
    }
  }
  if (!client.connected())
  {
    //Serial.println("Closing connection");
    client.stop();
  }
}
