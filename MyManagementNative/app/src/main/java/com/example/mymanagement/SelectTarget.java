package com.example.mymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymanagement.objects.Project;
import com.example.mymanagement.objects.Target;
import com.example.mymanagement.objects.Curator;

import java.util.ArrayList;

public class SelectTarget extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    static Curator curator = MainActivity.curator1;

    static int projectSelected = SelectYourProject.projectSelected;
    public static ArrayList<Project> projects = MainActivity.projects;

    public static ArrayList<Target> targets = projects.get(projectSelected).getTargets();
    static int targetSelected;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_target);

        targetSelected = -1;

        //Toast.makeText(this, projects.get(projectSelected).targets.get(0).programmersSimpleActivity.get(0).getName(), Toast.LENGTH_SHORT).show();
        int countTargets = targets.size();

        if(countTargets == 0){
            intent = new Intent(this, Menu.class);
            startActivity(intent);
        }

        String[] targetName = new String[countTargets];

        for (int i = 0; i < countTargets; i++)    {
            targetName[i] = targets.get(i).activityDate;
        }

        final String[] paths = targetName;

        spinner = (Spinner) findViewById(R.id.spinnerTarget);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectTarget.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        targetSelected = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onContinueClicked( View v) {
        intent = new Intent(this, Menu.class);
        startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

