


package com.example.mymanagement;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
import com.example.mymanagement.objects.Curator;
import com.example.mymanagement.objects.Programmer;
import com.example.mymanagement.objects.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.join;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {


    public static ArrayList<Programmer> programmers = new ArrayList<>();
    public static ArrayList<Project> projects = new ArrayList<>();
    public static ArrayList<Task> tasks = new ArrayList<>();

    private String projects_url = "http://vps758869.ovh.net/api/projects";
    private String login_url = "http://vps758869.ovh.net/api/loginMobileApp";
    private String tasks_url = "http://vps758869.ovh.net/api/tasks";
    private String attandanceListUrl = "http://vps758869.ovh.net/api/attandancelist";
    public static String token;
    public static Boolean isConnected = false;

    public static Curator curator1;

    public Group group;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    String[] programmersAttandance;
    String[] datesAttandance;
    String[] activitiesAttandance;

    // list of NFC technologies detected:
    private final String[][] techList = new String[][] {
            new String[] {
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcF.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(), Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("Curator", null);
        if(json !=null) {
            Type type = new TypeToken<Curator>() {
            }.getType();
            curator1 = gson.fromJson(json, type);
        }
        if(curator1 == null){
            Intent intentt = new Intent(this, RegisterActivity.class);
            startActivity(intentt);
        }else {
            curator1.projects = projects;
            if(isNetworkAvailable()){
                Toast.makeText(this, "Network available!", Toast.LENGTH_SHORT).show();
                isConnected = true;
                SharedPreferences sharedAttandance = getSharedPreferences("attandance", MODE_PRIVATE);
                Gson gson4 = new Gson();
                String jsonDates = sharedAttandance.getString("dates", null);
                Type type3 = new TypeToken<String[]>() {}.getType();
                datesAttandance = gson4.fromJson(jsonDates, type3);
                String jsonProgrammers = sharedAttandance.getString("programmers", null);
                programmersAttandance = gson4.fromJson(jsonProgrammers, type3);
                String jsonActivities = sharedAttandance.getString("activities", null);
                activitiesAttandance = gson4.fromJson(jsonActivities, type3);
                getToken();
            }else{
                Gson gson2 = new Gson();
                String jsonProjects = sharedPreferences.getString("projects", null);
                Type type1 = new TypeToken<ArrayList<Project>>() {}.getType();
                projects = gson2.fromJson(jsonProjects, type1);

                Gson gson3 = new Gson();
                String jsonTasks = sharedPreferences.getString("tasks", null);
                Type type2 = new TypeToken<ArrayList<Task>>() {}.getType();
                tasks = gson3.fromJson(jsonTasks, type2);
            }
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

    }

     boolean isNetworkAvailable() {
        // Checking internet connectivity
        ConnectivityManager connectivityMgr = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = null;
        if (connectivityMgr != null) {
            activeNetwork = connectivityMgr.getActiveNetworkInfo();
        }
        return activeNetwork != null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disabling foreground dispatch:
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tag = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            if (tag.equals(curator1.getTagId())) {
                Intent intentt = new Intent(this, SelectTask.class);
                startActivity(intentt);
            }
        }
    }

    public void addAttandance(final String[] datesPresence, final String[] programmersOnPresence, final String[] activitiesOnPresence) {
        Toast.makeText(this, String.valueOf(datesPresence.length), Toast.LENGTH_SHORT).show();
        for(int i = 0; i < datesPresence.length; i++) {
            final int finalI = i;
                StringRequest stringRequest = new StringRequest(Request.Method.POST, attandanceListUrl,
                        new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject obj = new JSONObject(response);
                                    String token = obj.getString("created");
                                    clearData();

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
                        headers.put("Authorization", "Bearer " + token);
                        return headers;
                    }

                    @Override
                    protected Map<String, String> getParams() {

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("data_presence", datesPresence[finalI]);
                        params.put("programmer_id", programmersOnPresence[finalI]);
                        params.put("activity_id", activitiesOnPresence[finalI]);

                        return params;
                    }
                };


                // Access the RequestQueue through your singleton class.
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //adding the string request to request queue
                requestQueue.add(stringRequest);
        }
    }

    private void clearData(){
        SharedPreferences sharedPreferences = getSharedPreferences("attandance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonDates = gson.toJson(null);
        editor.putString("dates", jsonDates);
        String jsonProgrammers = gson.toJson(null);
        editor.putString("programmers", jsonProgrammers);
        String jsonActivities = gson.toJson(null);
        editor.putString("activities", jsonActivities);
        editor.apply();
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";

        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    private void getToken() {
        if(curator1 !=null) {
            Toast.makeText(this,  curator1.getMobileToken(), Toast.LENGTH_SHORT).show();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                new Response.Listener<String>() {


                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            token = obj.getString("userToken");
                            loadTasks();
                            loadProjects();
                            if(datesAttandance != null){
                                addAttandance(datesAttandance, programmersAttandance, activitiesAttandance);
                            }


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
        })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("tagId", curator1.getTagId());
                params.put("mobile_token", curator1.getMobileToken());

                return params;
            }
        };

        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void loadTasks() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tasks_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray responseArray = obj.getJSONArray("data");

                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject ObjectData = responseArray.getJSONObject(i);

                                Integer taskId = ObjectData.getInt("task_id");
                                String taskName = ObjectData.getString("name");

                                JSONArray responseProjects = ObjectData.getJSONArray("projects");

                                ArrayList<Project> projectsTask = new ArrayList<>();
                                //Create String out of the Parsed JSON

                                for (int j = 0; j < responseProjects.length(); j++) {
                                    JSONObject ObjectDataProjects = responseProjects.getJSONObject(j);
                                    String projectName = ObjectDataProjects.getString("name");

                                    Project project = new Project(projectName, null);

                                    projectsTask.add(project);

                                }

                                Task task = new Task(taskName, projectsTask);
                                tasks.add(task);
                            }

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
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void loadProjects() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, projects_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray responseArray = obj.getJSONArray("data");

                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject ObjectData = responseArray.getJSONObject(i);

                                Integer projectId = ObjectData.getInt("id");
                                String projectName = ObjectData.getString("name");

                                JSONArray responseArrayGroup = ObjectData.getJSONArray("group");

                                JSONObject ObjectDataGroup = responseArrayGroup.getJSONObject(0);
                                Integer groupId = ObjectDataGroup.getInt("group_id");
                                String groupName = ObjectDataGroup.getString("name");


                                JSONArray responseArrayActivity = ObjectData.getJSONArray("activities");

                                ArrayList<Target> targets = new ArrayList<>();
                                //Create String out of the Parsed JSON

                                Target target = null;

                                for (int j = 0; j < responseArrayActivity.length(); j++) {
                                    JSONObject ObjectDataActivity = responseArrayActivity.getJSONObject(j);
                                    Integer activity_id = ObjectDataActivity.getInt("activity_id");
                                    String title = ObjectDataActivity.getString("title");
                                    String activityDate = ObjectDataActivity.getString("activityDate");
                                    String hour = ObjectDataActivity.getString("hour");
                                    Integer duration = ObjectDataActivity.getInt("duration");
                                    Integer checked = ObjectDataActivity.getInt("checked");

                                    target = new Target(activity_id, title, activityDate, hour, duration, checked);

                                    targets.add(target);

                                }

                                addProject(groupId, projectName, targets);

                            }

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
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }

    private void addProject(int groupId, final String projectName, final ArrayList<Target> targets) {
        String groups_url = "http://vps758869.ovh.net/api/groups/" + groupId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, groups_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);

                            JSONArray responseArray = obj.getJSONArray("data");

                            for (int i = 0; i < responseArray.length(); i++) {
                                JSONObject ObjectData = responseArray.getJSONObject(i);

                                String groupName = ObjectData.getString("name");

                                JSONArray responseArrayProgrammer = ObjectData.getJSONArray("programmers");

                                ArrayList<Programmer> programmersInGroup = new ArrayList<>();

                                //Create String out of the Parsed JSON

                                for (int j = 0; j < responseArrayProgrammer.length(); j++) {
                                    JSONObject ObjectDataProgrammer = responseArrayProgrammer.getJSONObject(j);
                                    Integer programmerId = ObjectDataProgrammer.getInt("programmer_id");
                                    String programmerName = ObjectDataProgrammer.getString("name");
                                    String tagId = ObjectDataProgrammer.getString("tagId");

                                    Programmer programmer = new Programmer(programmerName, programmerId.toString(),tagId, false, "");
                                    programmersInGroup.add(programmer);
                                }

                                group = new Group(groupName, programmersInGroup);

                                Project project = new Project(projectName,  group);
                                project.setTargets(targets);
                                projects.add(project);

                            }

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
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };

        // Access the RequestQueue through your singleton class.
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
