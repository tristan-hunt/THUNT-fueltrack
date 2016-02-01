package com.example.tristan.thunt_fueltrack;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EditEntryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    //http://stackoverflow.com/questions/10903754/input-text-dialog-android

    public static final String TAG = "EditEntryActivity";
    public final static String SAVE_BOOL = "cs.ualberta.thunt.SAVE_BOOL";
    public final static String DELETE_BOOL = "cs.ualberta.thunt.DELETE_BOOL";
    public final static String ENTRY_INDEX = "cs.ualberta.thunt.ENTRY_INDEX";

    private ListView entryListView;
    private ArrayList<String> attributes = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private ListView labelsListView;
    private ArrayList<String> labels = new ArrayList<>();
    private ArrayAdapter<String> labelsAdapter;

    private int entry_index;
    private String m_Text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_entry);
        this.entry_index = getIntent().getIntExtra(ENTRY_INDEX, -1);

        if (entry_index == -1 ){
            // If we don't have an index, just go back to displaying the log
            Log.i(TAG, "Entry index = -1");
            Intent intent = new Intent();
            intent.setClass(this, DisplayLogActivity.class);
            startActivity(intent);

        }
        entryListView = (ListView) findViewById(R.id.entryAttributes);
        entryListView.setItemsCanFocus(true);
        labelsListView = (ListView) findViewById(R.id.labels_listView);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button saveButton = (Button) findViewById(R.id.save_button);
        Button deleteButton = (Button) findViewById(R.id.delete_button);
        Button cancelButton = (Button) findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Log.i(TAG, "Save clicked");
                Intent intent = new Intent();
                intent.putExtra(SAVE_BOOL, true);

                // Get the information currently in the adapter
                String station = adapter.getItem(0);
                float odometer = Float.valueOf(adapter.getItem(1));
                String fuel_grade = adapter.getItem(2);
                float fuel_amount = Float.valueOf((adapter.getItem(3)));
                float unit_cost = Float.valueOf((adapter.getItem(4)));
                String date = adapter.getItem(5);

                intent.putExtra(CreateEntryActivity.STATION_TEXT, station);
                intent.putExtra(CreateEntryActivity.ODOMETER_FLOAT, odometer);
                intent.putExtra(CreateEntryActivity.FUEL_GRADE_TEXT, fuel_grade);
                intent.putExtra(CreateEntryActivity.FUEL_AMOUNT_FLOAT, fuel_amount);
                intent.putExtra(CreateEntryActivity.UNIT_COST_FLOAT, unit_cost);
                intent.putExtra(CreateEntryActivity.DATE_TEXT, date);

                onButtonClick(intent);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                setResult(RESULT_OK);
                Log.i(TAG, "Delete clicked");
                Intent intent = new Intent();
                intent.putExtra(DELETE_BOOL, true);
                onButtonClick(intent);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(RESULT_OK);
                Log.i(TAG, "Cancel clicked");
                Intent intent = new Intent();
                onButtonClick(intent);
            }
        });

    }

    public void onStart(){
        super.onStart();

        adapter = new ArrayAdapter<>(this, R.layout.list_item, attributes);
        entryListView.setAdapter(adapter);
        entryListView.setOnItemClickListener(this);
        fillEntryAttributes();

        labelsAdapter = new ArrayAdapter<>(this, R.layout.list_item, labels);
        labelsListView.setAdapter(labelsAdapter);
        fillLabelsListView();

    }

    public void onButtonClick(Intent intent){
        intent.putExtra(ENTRY_INDEX, this.entry_index);
        intent.setClass(this, DisplayLogActivity.class);
        startActivity(intent);
    }



    public void fillLabelsListView(){
        labels.add("Station: ");
        labels.add("Odometer: ");
        labels.add("Fuel Grade: ");
        labels.add("Fuel Amount: ");
        labels.add("Unit Cost: ");
        labels.add("Date: ");

        labelsAdapter.notifyDataSetChanged();
    }

    public void fillEntryAttributes(){

        // Get the entry's attributes from the intent
        Intent intent = getIntent();
        String station = intent.getStringExtra(CreateEntryActivity.STATION_TEXT);
        float odometer = intent.getFloatExtra(CreateEntryActivity.ODOMETER_FLOAT, 0);
        String fuelGrade = intent.getStringExtra(CreateEntryActivity.FUEL_GRADE_TEXT);
        float fuelAmount = intent.getFloatExtra(CreateEntryActivity.FUEL_AMOUNT_FLOAT, 0);
        float unitCost = intent.getFloatExtra(CreateEntryActivity.UNIT_COST_FLOAT, 0);
        String date = intent.getStringExtra(CreateEntryActivity.DATE_TEXT);

        // Then put them in the listView entryAttributes
        attributes.add(station);
        attributes.add(String.valueOf(odometer));
        attributes.add(fuelGrade);
        attributes.add(String.valueOf(fuelAmount));
        attributes.add(String.valueOf(unitCost));
        attributes.add(date);
        adapter.notifyDataSetChanged();
    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i(TAG, "You clicked Item: " + id + " at position:" + position);
        final View view = v;
        final int i = position;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit");

        // Set up the input
        final EditText input = new EditText(this);

        // Specify the type of input expected
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                ((TextView) view).setText(m_Text);
                attributes.set(i, m_Text);
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }
}

