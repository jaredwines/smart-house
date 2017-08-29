#include <IRremote.h>

IRsend irsend;

void setup()
{
  Serial.begin(9600);
}

void loop()
{
  if (Serial.available())
  {
    command(Serial.read() - '0');
  }
}

void command(int n)
{
  //TV Power
  if (n == 0)
  {
    irsend.sendNEC(0x20DF10EF, 32);
  }

  //TV Volume Up
  else if (n == 1)
  {
    for (int i = 0; i < 5; i++)
    {
    irsend.sendNEC(0x20DF40BF, 32);
    delay(200);
    }
  }

  //TV Volume Down
  else if (n == 2)
  {
    for (int i = 0; i < 5; i++)
    {
    irsend.sendNEC(0x20DFC03F, 32);
    delay(200);
    }
  }

  else if (n == 3)
  {

  }

  else if (n == 4)
  {

  }
}
