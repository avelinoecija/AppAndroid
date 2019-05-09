package com.example.proyecto_3;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.OutputStream;
public class FinalActivity extends AppCompatActivity {
    private String TAG = FinalActivity.class.getSimpleName();
    final String status = null;
    final String status2 = null;
    final String status3 = null;
    final String status4 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        Intent i_survey1 = getIntent();
        Intent i_survey2 = getIntent();
        Intent i_survey3 = getIntent();
        String message = i_survey1.getStringExtra("respuesta");
        String message2 = i_survey2.getStringExtra("respuesta2");
        String message3 = i_survey3.getStringExtra("idusuario");
        Bundle bundle1 = i_survey1.getExtras();
        Bundle bundle2 = i_survey2.getExtras();
        Bundle bundle3 = i_survey3.getExtras();
        String status = bundle1.getString("status");
        String status2 = bundle2.getString("status2");
        String status3 = bundle3.getString("status3");
        String status4 = bundle2.getString("status4");






        Toast toast = Toast.makeText(this, "su voto ha sido registrado", Toast.LENGTH_LONG);
        toast.show();
        Toast toast2 = Toast.makeText(this, status3, Toast.LENGTH_LONG);
        toast2.show();
        Toast toast3 = Toast.makeText(this, status, Toast.LENGTH_LONG);
        toast3.show();
        Toast toast4 = Toast.makeText(this, status2, Toast.LENGTH_LONG);
        toast4.show();
        Toast toast5 = Toast.makeText(this, status4, Toast.LENGTH_LONG);
        toast5.show();




        // Click this button to send response result data to source activity.
        Button passDataTargetReturnDataButton = (Button) findViewById(R.id.passDataTargetReturnDataButton);
        passDataTargetReturnDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("message_return", "This data is returned when user click button in target activity.");
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        new CallAPI().execute();
    }




    public class CallAPI extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... arg0) {
            String urlString = "http://192.168.202.191:8000/api/votos";; // URL to call
            String data1 = status3; //data to post
            String data2 = status4;
            String data3 = status;
            String data4 = status2;
            OutputStream out = null;
            if (urlString != null) {
                try {
                    URL url = new URL(urlString);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    out = new BufferedOutputStream(urlConnection.getOutputStream());
                    if (status == null) {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                        writer.write(data1);
                        writer.write(data2);
                        writer.write(data4);
                        writer.flush();
                        writer.close();
                        out.close();


                    } else if (status2 == null) {
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
                        writer.write(data1);
                        writer.write(data2);
                        writer.write(data3);
                        writer.flush();
                        writer.close();
                        out.close();


                        urlConnection.connect();
                    }

                } catch (Exception e) {
                    Log.e(TAG, "No funciona lumen.");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "No funciona Lumen", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });
            }


            return null;
        }
    }
}

