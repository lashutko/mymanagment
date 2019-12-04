package com.example.mymanagement;

import android.app.PendingIntent;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProgrammerAttandanceActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    AttandanceListAdapter adapter;

    public static ArrayList<Programmer> presentProgrammers = new ArrayList<>();
    int projectSelected = SelectYourProject.projectSelected;
    int targetSelected = SelectTarget.targetSelected;

    private String attandanceListUrl = "http://vps758869.ovh.net/api/attandancelist";

    Group group = MainActivity.projects.get(projectSelected).getGroup();

    public ArrayList<Target> targets = projects.get(projectSelected).getTargets();
    ArrayList<Programmer> programmers;
    public static ArrayList<Project> projects = MainActivity.projects;


    String[] datesPresence;
    String[] programmersOnPresence;
    String[] activitiesOnPresence;


    //public static Target target1;

    Intent intent;
    ListView mListView;

    private final String[][] techList = new String[][]{
            new String[]{
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
        setContentView(R.layout.addandance_list);

        if(projects.get(projectSelected).name == "Free activity"){
            programmers = projects.get(projectSelected).targets.get(targetSelected).programmersSimpleActivity;
        }else{
            programmers = group.getProgrammers();
        }

        //presentProgrammers.clear();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);

        if(strDate.equals(targets.get(targetSelected).activityDate.toString())){
            Toast.makeText(this,  "Tak", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,  "You could only check attandance list on the same day", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Menu.class);
            startActivity(intent);
        }
        if(targets.get(targetSelected).checked == 1){
            Toast.makeText(this,  "This target was checked before", Toast.LENGTH_SHORT).show();
            intent = new Intent(this, Menu.class);
            startActivity(intent);
        }




        //target1 = new Target(1, "1", "2019-04-22", "12:30", 120, 0);

        mListView = (ListView) findViewById(R.id.listView);

        adapter = new AttandanceListAdapter(this, R.layout.adapter_view_layout, presentProgrammers);
        mListView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
    }

    @Override
    protected void onPause() {
        super.onPause();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tag = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            Toast.makeText(this, tag, Toast.LENGTH_SHORT).show();
            int searchListLength = programmers.size();
            for (int i = 0; i < searchListLength; i++) {
                if (programmers.get(i).getTagId().contains(tag)) {
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = dateFormat.format(date);
                    programmers.get(i).setTagData(strDate);
                    presentProgrammers.add(programmers.get(i));
                    mListView.setAdapter(adapter);
                }
            }
        }
    }

    private String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";

        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }

    public void addAttandanceList( View v) {
        targets.get(targetSelected).setProgrammersPresence(presentProgrammers);
        final ArrayList<Programmer> programmersPresence = targets.get(targetSelected).programmersPresence;
        programmersOnPresence = new String[programmersPresence.size()];
        activitiesOnPresence = new String[programmersPresence.size()];
        datesPresence = new String[programmersPresence.size()];
        for(int i = 0; i < programmersPresence.size(); i++) {
            final int finalI = i;
            if (!isNetworkAvailable()) {
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = dateFormat.format(date);
                programmersOnPresence[i] = String.valueOf(presentProgrammers.get(finalI).getID());
                activitiesOnPresence[i] = String.valueOf(targets.get(targetSelected).activity_id);
                datesPresence[i] = strDate;
            } else {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, attandanceListUrl,
                        new Response.Listener<String>() {


                            @Override
                            public void onResponse(String response) {
                                try {

                                    JSONObject obj = new JSONObject(response);
                                    String token = obj.getString("created");

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

                    @Override
                    protected Map<String, String> getParams() {
                        Date date = Calendar.getInstance().getTime();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String strDate = dateFormat.format(date);

                        Map<String, String> params = new HashMap<String, String>();
                        params.put("data_presence", strDate);
                        params.put("programmer_id", String.valueOf(presentProgrammers.get(finalI).getID()));
                        params.put("activity_id", String.valueOf(targets.get(targetSelected).activity_id));

                        return params;
                    }
                };


                // Access the RequestQueue through your singleton class.
                RequestQueue requestQueue = Volley.newRequestQueue(this);

                //adding the string request to request queue
                requestQueue.add(stringRequest);
            }
        }

        if(!isNetworkAvailable()){
            saveAttandance(datesPresence, programmersOnPresence, activitiesOnPresence);
        }

        MainActivity.projects.get(projectSelected).addTarget(targets.get(targetSelected));
        saveData();
        Toast.makeText(this, "Attandancelist was added!", Toast.LENGTH_SHORT).show();
        intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    private void saveData() {
        // projects.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonProjects = gson.toJson(projects);
        editor.putString("projects", jsonProjects);
        String jsonTasks = gson.toJson(MainActivity.tasks);
        editor.putString("tasks", jsonTasks);
        editor.apply();
    }

    private void saveAttandance(String[] dates, String[] programmers, String[] activities) {
        // projects.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("attandance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonDates = gson.toJson(dates);
        editor.putString("dates", jsonDates);
        String jsonProgrammers = gson.toJson(programmers);
        editor.putString("programmers", jsonProgrammers);
        String jsonActivities = gson.toJson(activities);
        editor.putString("activities", jsonActivities);
        editor.apply();
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
}