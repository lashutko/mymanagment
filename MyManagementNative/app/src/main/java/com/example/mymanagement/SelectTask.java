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
import com.example.mymanagement.objects.Task;

import java.util.ArrayList;

public class SelectTask extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;

    public static ArrayList<Task> tasks = MainActivity.tasks;

    static int taskSelected;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_task);

        taskSelected = -1;

        //Toast.makeText(this, projects.get(projectSelected).targets.get(0).programmersSimpleActivity.get(0).getName(), Toast.LENGTH_SHORT).show();
        int countTasks = tasks.size();

        String[] taskName = new String[countTasks];

        for (int i = 0; i < countTasks; i++)    {
            taskName[i] = tasks.get(i).getName();
        }

        final String[] paths = taskName;

        spinner = (Spinner) findViewById(R.id.spinnerTask);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectTask.this,
                android.R.layout.simple_spinner_item, paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
        taskSelected = position;

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onContinueClicked( View v) {
        intent = new Intent(this, SelectYourProject.class);
        startActivity(intent);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}


