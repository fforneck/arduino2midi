void setup() {
  pinMode(LED_BUILTIN, OUTPUT);
  Serial.begin(9600);
}

int val;
byte buf[27];

void loop() {

  for (int pin = 0; pin < 16; pin++) {
    val = analogRead(pin);
    for (int bit = 0; bit < 10; bit++) {
      int bytePos = ((pin * 10) + bit) / 8;
      int bitPos = ((pin * 10) + bit) % 8;
      bitWrite(buf[bytePos], bitPos, bitRead(val, bit));
    }
  }
  
  for (byte pin = 0; pin < 54; pin++) {
    val = digitalRead(pin);
    int bytePos = 20 + (pin / 8);
    int bitPos = pin % 8;    
    bitWrite(buf[bytePos], bitPos, val == HIGH ? 1 : 0);
  }

  //Serial.write(buf, 27);
  for (byte b = 20; b < 27; b++) {
    Serial.print(buf[b]);
    Serial.print(" ");
  }
  Serial.println("");

  delay(2000); // TODO: needed?
  
}
