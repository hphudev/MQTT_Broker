package com.example.mqtt_broker;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;


public class MainActivity extends AppCompatActivity {
    MqttAndroidClient client;
    MqttConnectOptions mqttConnectOptions;
    String username = "chienthuatido";
    String password = "Lehoangphu13012001";
    String clientID = "xxx";
    String host = "tcp://broker.hivemq.com";
    String topic = "testtopic/phu";
    String TAG = "MQTT_MESSAGE";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.txtMessage);
        init();
    }

    private void init()
    {
        client = new MqttAndroidClient(this.getApplicationContext(), host,
                        clientID);
        Button btnSubcribe = (Button)findViewById(R.id.btnSubcribe);
        btnSubcribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectX();
            }
        });

        Button btnPublish = (Button)findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
    }

    private void connectX()
    {
        textView.setText("Connecting...");
        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d(TAG, "onSuccess");
                    textView.setText("Connected");
                    sub();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");
                    textView.setText("Unconnected");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void sub()
    {
        try {
            client.subscribe(topic, 0);
            client.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {

                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    Log.d(TAG, "topic:" + topic);
                    Log.d(TAG, "message:" + new String(message.getPayload()));
                    textView.setText("message:" + new String(message.getPayload()));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {

                }
            });
        }
        catch (MqttException e)
        {

        }
    }

    private void publish()
    {
        String topic = this.topic;
        String payload = ((EditText)findViewById(R.id.edtPublish)).getText().toString();
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
}