package com.example.proyecto_3;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements OnClickListener {

    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.my_button)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onClick(View arg0) {

        Button b = findViewById(R.id.my_button);

        b.setClickable(false);

        new LongRunningGetIO().execute();
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


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.inject(this);

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

        public void login() {

            if (!validate()) {
                onLoginFailed();
                return;
            } else {
                Intent intent2 = new Intent(getApplicationContext(), PollActivity.class);
                startActivityForResult(intent2, REQUEST_SIGNUP);
            }
        }

        protected void onPostExecute(String results) {
            if (results != null) {

                EditText et = (EditText) findViewById(R.id.my_button);

                et.setText(results);

            }

            Button b = (Button) findViewById(R.id.my_button);

            b.setClickable(true);

        }

        public class LongRunningGetIO extends AsyncTask<Void, Void, String> {
            public String getASCIIContentFromEntity(InputStream entity) throws IllegalStateException, IOException {

                InputStream in = entity;

                StringBuffer out = new StringBuffer();
                int n = 1;
                while (n > 0) {
                    byte[] b = new byte[4096];

                    n = in.read(b);

                    if (n > 0) out.append(new String(b, 0, n));

                }

                return out.toString();

            }

            public String doInBackground(Void... params) {
                BufferedReader reader = null;
                String correo = null;
                String clave = null;
                JSONObject c = null;
                try {
                    URL githubEndpoint = new URL("http://192.168.202.191:8000/api/usuarios");
                    HttpURLConnection myConnection = (HttpURLConnection) githubEndpoint.openConnection();
                    InputStream responseBody = myConnection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(responseBody));
                    StringBuffer bufer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        bufer.append(line);
                    }
                    String finalJson = bufer.toString();
                    JSONObject json = new JSONObject(finalJson);
                    JSONArray lista = json.getJSONArray("data");
                    //correo = lista.getJSONArray(0);
                    for (int i = 0; i < lista.length(); i++) {
                        c = lista.getJSONObject(i);
                        correo = c.getString("correo");
                        clave = c.getString("contrasena");
                    }


                } catch (Exception e) {
                    return e.getLocalizedMessage();

                }

                return correo;

            }

        }
    }




