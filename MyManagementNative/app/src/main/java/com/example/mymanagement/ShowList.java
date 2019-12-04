package com.example.mymanagement;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mymanagement.objects.Project;
import com.example.mymanagement.objects.Target;
import com.example.mymanagement.objects.Group;
import com.example.mymanagement.objects.Programmer;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class ShowList extends AppCompatActivity {

    int targetSelected = SelectTarget.targetSelected;
    Target target = SelectTarget.targets.get(targetSelected);
    AttandanceListAdapter adapter;
    ListView mListView;
    ArrayList<Programmer> programmersOnList = new ArrayList<>();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        mListView = (ListView) findViewById(R.id.listView);

        /// GET FROM SERVER
        String activity_url = "http://vps758869.ovh.net/api/activities/" + target.activity_id;
        if(MainActivity.isConnected) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, activity_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject obj = new JSONObject(response);

                                JSONArray responseArray = obj.getJSONArray("data");
                                JSONObject ObjectData = responseArray.getJSONObject(0);
                                JSONArray responseArrayProgrammer = ObjectData.getJSONArray("programmers");

                                //Create String out of the Parsed JSON

                                for (int j = 0; j < responseArrayProgrammer.length(); j++) {
                                    JSONObject ObjectDataProgrammer = responseArrayProgrammer.getJSONObject(j);
                                    Integer programmerId = ObjectDataProgrammer.getInt("id");
                                    String programmerName = ObjectDataProgrammer.getString("name");
                                    String tagId = ObjectDataProgrammer.getString("tagId");
                                    String datePresence = ObjectDataProgrammer.getString("datePresence");
                                    Programmer programmer = new Programmer(programmerName, programmerId.toString(), tagId, false, datePresence);
                                    programmersOnList.add(programmer);
                                }

                                ShowList();


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    //Display error message whenever an error occurs
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Authorization", "Bearer " + MainActivity.token);
                    return headers;
                }
            };

            // Access the RequestQueue through your singleton class.
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            //adding the string request to request queue
            requestQueue.add(stringRequest);
        }else{
            Toast.makeText(this, "You could only show list only when you have acces to Internet", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Menu.class);
            startActivity(intent);
        }

    }

    public void ShowList(){
        adapter = new AttandanceListAdapter(this, R.layout.adapter_view_layout, programmersOnList);
        mListView.setAdapter(adapter);
    }


}
