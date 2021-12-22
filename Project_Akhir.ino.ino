#include <FirebaseESP32.h>
#include <DHT.h>
#include <Wire.h>
#include <Adafruit_MLX90614.h>
#include <ESP32Tone.h>

#define FIREBASE_HOST "https://iot-uas-71407-default-rtdb.firebaseio.com/"
#define FIREBASE_AUTH "p7wP12idb5YxwpDuNmYvajccb6P5aaz5xadlF6vV"
#define WIFI_SSID "speedy@9e70"
#define WIFI_PASSWORD "joeLberuanG"

#define DHTTYPE DHT11

Adafruit_MLX90614 mlx = Adafruit_MLX90614();

short DHTPIN = 27, buzzer = 26;
float sRuangan, sObjek;

uint8_t on_off, alert, paramRuangan, paramObjek;
String path = "/node1";

DHT dht(DHTPIN, DHTTYPE);
FirebaseData firebaseData;

void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);
    initWifi();
  dht.begin();
  pinMode(buzzer,OUTPUT);
  mlx.begin();
}

void loop() {
  Firebase.getInt(firebaseData, path + "/statusSistem");
  on_off = firebaseData.intData();
  Serial.print("kondisi sistem ");
  Serial.println(on_off);
  updateParam();
  if(on_off == 1){
    bacaSuhuRuangan();
    bacaSuhuObjek();
    
    if(cekKondisi()){
      bunyi();
    }
  }else{
    delay(1000);
  }
  Serial.println();Serial.println();
}

void bacaSuhuObjek(){
  sObjek = mlx.readObjectTempC();
  if(isnan(sObjek)){
    Firebase.setInt(firebaseData, path + "/suhuObjek", 0);
    Serial.println("suhu objek: error");
    return;
  }
  Firebase.setInt(firebaseData, path + "/suhuObjek", sObjek);
  Serial.print("suhu objek:"); Serial.println(sObjek);
}

void bacaSuhuRuangan(){
  sRuangan = dht.readTemperature();      //read temperature in Celcius

  if(isnan(sRuangan)){                   //jika suhu tidak terbaca
    Firebase.setInt(firebaseData, path + "/suhuRuangan", 0);
    Serial.println("suhu ruangan: error");
    return;
  }
  Firebase.setInt(firebaseData, path + "/suhuRuangan", sRuangan);
  Serial.print("suhu ruangan:"); Serial.println(sRuangan);
}

void bunyi(){
  tone(buzzer, 2000);
  delay(5000);
  noTone(buzzer);
}

bool cekKondisi(){
  if(paramRuangan <= sRuangan || paramObjek <= sObjek){
    Firebase.setInt(firebaseData, path + "/alert", 1);
    return true;
  }
  Firebase.setInt(firebaseData, path + "/alert", 0);
  return false;
}
void updateParam(){
  Firebase.getInt(firebaseData, path + "/paramRuangan");
  paramRuangan = firebaseData.intData();
  Firebase.getInt(firebaseData, path + "/paramObjek");
  paramObjek = firebaseData.intData();
  Serial.println(paramRuangan);
  Serial.println(paramObjek);
}
void initWifi(){
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print(". \n");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  while (Firebase.ready() != true)
  {
    Serial.print("Firebase belum terhubung");
 
  }
  Firebase.ready();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
  //Set database read timeout to 1 minute (max 15 minutes)
  Firebase.setReadTimeout(firebaseData, 1000 * 60);
  //tiny, small, medium, large and unlimited.
  //Size and its write timeout e.g. tiny (1s), small (10s), medium (30s) and large (60s).
  Firebase.setwriteSizeLimit(firebaseData, "tiny");
}
