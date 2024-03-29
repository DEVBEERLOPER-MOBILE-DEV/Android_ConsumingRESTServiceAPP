package co.devbeerloper.consumingrestserviceapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.conn.*;


import javax.net.ssl.HttpsURLConnection;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class MainActivity extends AppCompatActivity {

    public static final String URL_SWAPI = "https://swapi.co/api/";
    private TextView tvName;
    String nameResponse = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvName = findViewById(R.id.tvName);
    }

    public void makeCall (View v) {
        callWebService("");
    }

    public void sendData (View v) {
        callPostService ();
    }

    public void callPostService () {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    URL urlService = new URL ("http://192.168.180.26:3000/aptos/changeAptoStatusBody" );
                    HttpURLConnection connection =  (HttpURLConnection) urlService.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);
                    DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
                    wr.writeBytes("{\"luzBano\":false,\"luzCocina\":true,\"luzHabitacion1\":false,\"luzHabitacion2\":false,\"luzHabitacion3\":true,\"luzHabitacion4\":true}");
                    wr.close();

                    InputStream responseBody = connection.getInputStream();
                    if (connection.getResponseCode() == 200) {
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    public void callWebService(String serviceEndPoint){

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    URL urlService = new URL (URL_SWAPI + "people/1/" );
                    HttpsURLConnection connection =  (HttpsURLConnection) urlService.openConnection();
                    connection.setRequestMethod("GET");
                    InputStream responseBody = connection.getInputStream();

                    if (connection.getResponseCode() == 200) {
                        // Success
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");

                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        jsonReader.beginObject(); // Start processing the JSON object
                        String key = jsonReader.nextName(); // Fetch the next key
                        String value = jsonReader.nextString();

                        Log.v("INFO",value);
                        nameResponse = value;

                        tvName.post(new Runnable() {
                            @Override
                            public void run() {
                                tvName.setText(nameResponse);

                            }
                        });

                    } else {
                        // Error handling code goes here
                        Log.v("ERROR", "ERROR");
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        tvName.setText(nameResponse);


    } // end callWebService()


}
