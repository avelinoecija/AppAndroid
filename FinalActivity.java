package com.example.proyecto_3;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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
        final String status = bundle1.getString("status");
        final String status2 = bundle2.getString("status2");
        final String status3 = bundle3.getString("status3");
        final String status4 = bundle2.getString("status4");






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


        class insertData extends AsyncTask<Void, Void, Void> {
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                HttpHandler sh = new HttpHandler();
                // Making a request to url and getting response
                if (status != null) {
                    String url = "http://10.10.2.2:8000/api/votos?idusuario=" + status3 + "&idpropuesta=" + status4 + "&voto=" + status + "";
                    String jsonStr = sh.makeServiceCall(url);
                    Log.e(TAG, "Response from url: " + jsonStr);
                    if (jsonStr != null) {
                        Toast.makeText(getApplicationContext(),
                                "datos insertados",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    String url2 = "http://10.10.2.2:8000/api/votos?idusuario=" + status3 + "&idpropuesta=" + status4 + "&voto=" + status2 + "";
                    String jsonStr2 = sh.makeServiceCall(url2);

                    Log.e(TAG, "Response from url: " + jsonStr2);
                    if (jsonStr2 != null) {
                        Toast.makeText(getApplicationContext(),
                                "datos insertados",
                                Toast.LENGTH_LONG).show();

                    } else {
                        Log.e(TAG, "Couldn't get json from server.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),
                                        "Couldn't get json from server. Check LogCat for possible errors!",
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }

                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();




            }
            @Override public void onPostExecute(Void results) {
                super.onPostExecute(results);



            }



        }
        new insertData().execute();

    }


    }







