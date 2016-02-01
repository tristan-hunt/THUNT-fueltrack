package com.example.tristan.thunt_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DisplayLogActivity extends Activity implements AdapterView.OnItemClickListener {

    public static final String TAG = "DisplayLogActivity";
    public static final String FILENAME = "log_entries.sav";
    private ListView entryList;

    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayAdapter<Entry> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_log);

        entryList = (ListView) findViewById(R.id.entryList);
    }


    @Override
    protected void onStart(){
        super.onStart();
        loadFromFile();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, entries);
        entryList.setAdapter(adapter);
        entryList.setOnItemClickListener(this);

        Intent intent = getIntent();
        Boolean newEntry = intent.getBooleanExtra(CreateEntryActivity.NEW_ENTRY_BOOL, false);
        Boolean saveEntry = intent.getBooleanExtra(EditEntryActivity.SAVE_BOOL, false);
        Boolean deleteEntry = intent.getBooleanExtra(EditEntryActivity.DELETE_BOOL, false);
        int entryIndex = intent.getIntExtra(EditEntryActivity.ENTRY_INDEX, -1);
        if (newEntry) addEntryFromIntent(intent);
        if (saveEntry) saveChanges(entryIndex, intent);
        if (deleteEntry) delete(entryIndex);

        TextView totalFuelCost = (TextView) findViewById(R.id.totalFuelCost);
        totalFuelCost.setText("Total fuel cost: " + getTotalCost());
    }


    // code from http://stackoverflow.com/questions/13281197/android-how-to-create-clickable-listview
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Log.i(TAG, "You clicked Item: " + id + " at position:" + position);

        // Get the information from the entry that was clicked
        Entry entry = entries.get(position);

        Log.i(TAG, "You clicked on entry: " + entry.toString());

        // Start a new Activity via intent
        Intent intent = new Intent();
        intent.setClass(this, EditEntryActivity.class);

        // Pass the entry info in the intent:
        intent.putExtra(CreateEntryActivity.DATE_TEXT, entry.getDate());
        intent.putExtra(CreateEntryActivity.UNIT_COST_FLOAT, entry.getUnit_cost());
        intent.putExtra(CreateEntryActivity.FUEL_AMOUNT_FLOAT, entry.getFuel_amount());
        intent.putExtra(CreateEntryActivity.FUEL_GRADE_TEXT, entry.getFuel_grade());
        intent.putExtra(CreateEntryActivity.ODOMETER_FLOAT, entry.getOdometer());
        intent.putExtra(CreateEntryActivity.STATION_TEXT, entry.getStation());
        intent.putExtra(EditEntryActivity.ENTRY_INDEX, position);

        startActivity(intent);



     }

    private void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));
            Gson gson = new Gson();

            Type listType = new TypeToken<ArrayList<Entry>>() {}.getType();
            entries = gson.fromJson(in, listType);

        } catch (FileNotFoundException e) {
            entries = new ArrayList<>();
        }
    }


    private void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(FILENAME, 0);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(entries, out);
            out.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
    public Entry getEntryFromIntent(Intent intent) throws InvalidEntryException{
        Log.i(TAG, "Getting entry information from the intent");
        String station = intent.getStringExtra(CreateEntryActivity.STATION_TEXT);
        float odometer = intent.getFloatExtra(CreateEntryActivity.ODOMETER_FLOAT, -1);
        String fuelGrade = intent.getStringExtra(CreateEntryActivity.FUEL_GRADE_TEXT);
        float fuelAmount = intent.getFloatExtra(CreateEntryActivity.FUEL_AMOUNT_FLOAT, -1);
        float unitCost = intent.getFloatExtra(CreateEntryActivity.UNIT_COST_FLOAT, -1);
        String dateText = intent.getStringExtra(CreateEntryActivity.DATE_TEXT);
        if (odometer == -1 || fuelAmount == -1 || unitCost == -1){
            throw new InvalidEntryException();
        }

        Entry entry =  new Entry(station, odometer, fuelGrade, fuelAmount, unitCost, dateText);
        return entry;
    }


    public void addEntryFromIntent(Intent intent){
        Log.i(TAG, "Creating new Entry....");
        try {
            entries.add(getEntryFromIntent(intent));
            adapter.notifyDataSetChanged();
            saveInFile();
        }
        catch (InvalidEntryException e){
            return;
        }

    }

    public void saveChanges(int entryIndex, Intent intent){
        Log.i(TAG, "Saving changes to entry...");
        try{
            Entry entry = getEntryFromIntent(intent);
            entries.set(entryIndex, entry);
            adapter.notifyDataSetChanged();
            saveInFile();
        }
        catch (InvalidEntryException e) {
            return;
        }
    }

    public void delete(int entryIndex){
        Log.i(TAG, "Deleting entry!!!");
        entries.remove(entryIndex);
        adapter.notifyDataSetChanged();
        saveInFile();
    }

    public void onNewEntryClick(View view) {
        Intent intent = new Intent(this, CreateEntryActivity.class);
        startActivity(intent); // Start the activity specified by the intent
    }

    public String getTotalCost(){
        // Compute the total cost
        double total = 0;
        for (int i = 0; i < entries.size(); i++){
            total = total + entries.get(i).getFuel_cost();
        }

        // Convert value to a string and return it
        String totalText = Double.toString(total);
        return totalText;
    }

}
