package com.example.proyecto_3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements OnClickListener {

    private static final int REQUEST_SIGNUP = 0;
    private String TAG = MainActivity.class.getSimpleName();


    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.my_button)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    private ListView lv;

    ArrayList<HashMap<String, String>> contactList;
    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        lv = (ListView) findViewById(R.id.list);
        contactList = new ArrayList<>();



        new GetContacts().execute();

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });


    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.202.191:8000/api/usuarios";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);


                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("data");


                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);
                        String email = c.getString("correo");
                        String pass = c.getString("contrasena");
                        String iduser = c.getString("idusuario");


                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();


                        // adding each child node to HashMap key => value

                        contact.put("correo", email);
                        contact.put("contrasena", pass);
                        contact.put("idusuario", iduser);


                        // adding contact to contact list
                        contactList.add(contact);


                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

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

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Toast.makeText(MainActivity.this, "cargando...", Toast.LENGTH_LONG).show();



        }
       @Override public void onPostExecute(Void results) {
           super.onPostExecute(results);

            Button b = (Button) findViewById(R.id.my_button);
            b.setClickable(true);




       }

    }

    @Override
    public void onClick(View arg0) {

        Button b = findViewById(R.id.my_button);

        b.setClickable(false);



    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Inicio de sesion fallido", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }


        public boolean validate() {
            boolean valid = true;
            String email = _emailText.getText().toString();
            String pass = _passwordText.getText().toString();

            if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _emailText.setError("Introduzca una dirección de correo válida");
                valid = false;
            } else {
                _emailText.setError(null);
            }
            if (pass.isEmpty() || pass.length() < 4 || pass.length() > 10) {
                _passwordText.setError("la contraseña tiene que tener entre 4 y 10 caracteres alfanuméricos");
                valid = false;
            } else {
                _passwordText.setError(null);
            }

            return valid;

        }



        public void login() {



            if (!validate()) {
                onLoginFailed();
                return;
            } else {
                Intent intent2 = new Intent(getApplicationContext(), PollActivity.class);
                startActivityForResult(intent2, REQUEST_SIGNUP);
            }
        }



    }





