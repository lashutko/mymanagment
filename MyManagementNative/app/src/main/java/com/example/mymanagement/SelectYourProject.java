package com.example.mymanagement;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SelectYourProject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    static Curator curator = MainActivity.curator1;
    static ArrayList<Project> projects = MainActivity.projects;
    static ArrayList<Task> tasks = MainActivity.tasks;
    static int taskSelected = SelectTask.taskSelected;

    private String activities_free_url = "http://vps758869.ovh.net/api/activitiesfree";

    public static int projectSelected;
    Intent intent;

    int countProjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selct_your_project);

        Task task = tasks.get(taskSelected);
        ArrayList<Project> projectsTemp = new ArrayList<>();;
        for(int i = 0; i < task.projects.size(); i++ ){
            for(int j = 0; j < projects.size(); j++ ){
                if(task.projects.get(i).name.equals(projects.get(j).name)){
                    projectsTemp.add(projects.get(j));
                }
            }
        }
        countProjects = projectsTemp.size();

        String[] projectName = new String[countProjects+1];

        for (int i = 0; i < countProjects; i++) {
            projectName[i]= projectsTemp.get(i).name;
        }

        projectName[countProjects] = "Simple Activities";

        final String[] paths = projectName;

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectYourProject.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        if(position == countProjects && MainActivity.isConnected){
                StringRequest stringRequest = new StringRequest(Request.Method.GET, activities_free_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject obj = new JSONObject(response);
                                    JSONArray responseArray = obj.getJSONArray("data");

                                    Project projectFreeActivity = new Project("Free activity", null);

                                    ArrayList<Target> targets = new ArrayList<>();
                                    Group group;

                                    for (int i = 0; i < responseArray.length(); i++) {
                                        JSONObject ObjectData = responseArray.getJSONObject(i);

                                        //Create String out of the Parsed JSON

                                        Integer activity_id = ObjectData.getInt("id");
                                        String title = ObjectData.getString("title");
                                        String activityDate = ObjectData.getString("activityDate");
                                        String hour = ObjectData.getString("hour");
                                        Integer duration = ObjectData.getInt("duration");
                                        Integer checked = ObjectData.getInt("checked");

                                        JSONArray responseArrayGroup = ObjectData.getJSONArray("group");
                                            JSONObject ObjectGroupData = responseArrayGroup.getJSONObject(0);

                                            ArrayList<Programmer> programmersInGroup = new ArrayList<>();

                                            JSONArray responseArrayProgrammer = ObjectGroupData.getJSONArray("programmers");

                                            for (int j = 0; j < responseArrayProgrammer.length(); j++) {
                                                JSONObject ObjectDataProgrammer = responseArrayProgrammer.getJSONObject(j);
                                                Integer programmerId = ObjectDataProgrammer.getInt("programmer_id");
                                                String programmerName = ObjectDataProgrammer.getString("name");
                                                String tagId = ObjectDataProgrammer.getString("tagId");

                                                Programmer programmer = new Programmer(programmerName, programmerId.toString(),tagId, false, "");
                                                programmersInGroup.add(programmer);
                                            }


                                        Target target = null;
                                        target = new Target(activity_id, title, activityDate, hour, duration, checked);
                                        target.setProgrammersSimpleActivity(programmersInGroup);

                                        targets.add(target);

                                    }
                                    projectFreeActivity.setTargets(targets);
                                    projects.add(projectFreeActivity);

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
                        headers.put("Authorization", "Bearer " + MainActivity.token);
                        return headers;
                    }
                };

                // Access the RequestQueue through your singleton class.
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //adding the string request to request queue
                requestQueue.add(stringRequest);
            }

        projectSelected = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onContinueClicked( View v) {
        intent = new Intent(this, SelectTarget.class);
        startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
