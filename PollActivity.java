package com.example.proyecto_3;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PollActivity extends AppCompatActivity {

    private String TAG = PollActivity.class.getSimpleName();
    private ListView lv2;
    private ListView lv3;
    private TextView item;


    ArrayList<HashMap<String, String>> questionList;
    ArrayList<HashMap<String, String>> userList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll);

        //Nothing fancy here. Plain old simple buttons....

        Button button_survey_example_1 = (Button) findViewById(R.id.show);
        Button button_survey_example_2 = (Button) findViewById(R.id.hide);
        final String answer1 = "si";
        final String answer2 = "no";

        button_survey_example_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_survey1 = new Intent(PollActivity.this, FinalActivity.class);
                Bundle extras = new Bundle();
                extras.putString("status", "Si");
                extras.putString("status3", Collections.singletonList(userList.get(1)).toString().substring(12, 13));
                extras.putString("status4", Collections.singletonList(questionList.get(0)).toString().substring(52, 53));
                i_survey1.putExtra("idusuario", "Usuario numero");
                i_survey1.putExtras(extras);
                startActivity(i_survey1);




            }

        });



        button_survey_example_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_survey2 = new Intent(PollActivity.this, FinalActivity.class);
                Bundle extras2 = new Bundle();
                extras2.putString("status2", "No");
                extras2.putString("status3", Collections.singletonList(userList.get(1)).toString().substring(12, 13));
                extras2.putString("status4", Collections.singletonList(questionList.get(0)).toString());
                i_survey2.putExtra("Respuesta2", "Su respuesta es no");
                i_survey2.putExtras(extras2);
                startActivity(i_survey2);


            }
        });

        questionList = new ArrayList<>();
        userList = new ArrayList<>();
        lv2 = (ListView) findViewById(R.id.list);
        lv3 = (ListView) findViewById(R.id.list2);



        new GetContacts().execute();
        new GetUsers().execute();

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(PollActivity.this, "Cargando preguntas", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "http://192.168.202.191:8000/api/propuestas";
            String jsonStr2 = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr2);
            if (jsonStr2 != null) {
                try {
                    JSONObject jsonObj2 = new JSONObject(jsonStr2);

                    // Getting JSON Array node
                    JSONArray questions = jsonObj2.getJSONArray("data");

                    // looping through All Contacts
                    for (int i = 0; i < questions.length(); i++) {
                        JSONObject d = questions.getJSONObject(i);
                        String pregunta = d.getString("propuesta");
                        String idpropuesta = d.getString("idpropuesta");

                        // tmp hash map for single contact
                        HashMap<String, String> question = new HashMap<>();

                        // adding each child node to HashMap key => value
                        question.put("propuesta", pregunta);
                        question.put("idpropuesta", idpropuesta);

                        // adding contact to contact list
                        questionList.add(question);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        private class GetUsers extends AsyncTask<Void, Void, Void> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Toast.makeText(PollActivity.this, "Cargando usuarios", Toast.LENGTH_LONG).show();

            }

            @Override
            protected Void doInBackground(Void... arg0) {
                HttpHandler sh2 = new HttpHandler();
                // Making a request to url and getting response
                String url2 = "http://192.168.202.191:8000/api/usuarios";
                String jsonStr3 = sh2.makeServiceCall(url2);

                Log.e(TAG, "Response from url: " + jsonStr3);
                if (jsonStr3 != null) {
                    try {
                        JSONObject jsonObj3 = new JSONObject(jsonStr3);

                        // Getting JSON Array node
                        JSONArray users = jsonObj3.getJSONArray("data");

                        // looping through All Contacts
                        for (int i = 0; i < users.length(); i++) {
                            JSONObject d = users.getJSONObject(i);
                            String id = d.getString("idusuario");

                            // tmp hash map for single contact
                            HashMap<String, String> user = new HashMap<>();

                            // adding each child node to HashMap key => value
                            user.put("idusuario", id);

                            // adding contact to contact list
                            userList.add(user);
                        }
                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show();
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
        @Override protected void onPostExecute (Void result){
            super.onPostExecute(result);
            ListAdapter adapter = new SimpleAdapter(PollActivity.this, questionList, R.layout.list_item, new String[]{"propuesta"}, new int[]{R.id.propuesta});
            lv2.setAdapter(adapter);
            ListAdapter adapter2 = new SimpleAdapter(PollActivity.this, Collections.singletonList(userList.get(1)), R.layout.list_item, new String[]{"idusuario"}, new int[]{R.id.usuario});
            lv3.setAdapter(adapter2);


        }
    }


}



//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == SURVEY_REQUEST) {
//            if (resultCode == RESULT_OK) {
//
//                String answers_json = data.getExtras().getString("answers");
//                Log.d("****", "****************** WE HAVE ANSWERS ******************");
//                Log.v("ANSWERS JSON", answers_json);
//                Log.d("****", "*****************************************************");
//
 //               //do whatever you want with them...
  //          }
   //     }
    //}


    //json stored in the assets folder. but you can get it from wherever you like.
//    private String loadSurveyJson(String filename) {
//        try {
//            InputStream is = getAssets().open(filename);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            return new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//    }



