package com.example.tristan.thunt_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;


public class CreateEntryActivity extends Activity {
    public static final String TAG = "CreateEntryActivity";

    public final static String STATION_TEXT = "cs.ualberta.thunt.STATION_TEXT";
    public final static String ODOMETER_FLOAT = "cs.ualberta.thunt.ODOMETER_FLOAT";
    public final static String FUEL_GRADE_TEXT = "cs.ualberta.thunt.FUEL_GRADE_TEXT";
    public final static String FUEL_AMOUNT_FLOAT = "cs.ualberta.thunt.FUEL_AMOUNT_FLOAT";
    public final static String UNIT_COST_FLOAT = "cs.ualberta.thunt.UNIT_COST_FLOAT";
    public final static String DATE_TEXT = "cs.ualberta.thunt.UNIT_DATE_TEXT";
    public final static String NEW_ENTRY_BOOL = "cs.ualberta.thunt.NEW_ENTRY_BOOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createEntry(View view){
        Log.i(TAG, "createEntry()....");

        Log.i(TAG, "creating the new intent...");
        Intent intent = new Intent(this, DisplayLogActivity.class);
        intent.putExtra(NEW_ENTRY_BOOL, true); // Let DisplayLogActivity know there's another entry

        Log.i(TAG, "Finding station....");
        EditText editText = (EditText) findViewById(R.id.edit_station);
        String station = editText.getText().toString();
        intent.putExtra(STATION_TEXT, station);

        // Find the odometer reading.
        Log.i(TAG, "Finding odometer reading....");
        editText = (EditText) findViewById(R.id.edit_odometer);
        String odometerText = editText.getText().toString();
        float odometer = Float.valueOf(odometerText);
        // TODO: check decimal places
        intent.putExtra(ODOMETER_FLOAT, odometer);

        // Alright next we have the fuel grade
        Log.i(TAG, "Finding fuel grade....");
        editText = (EditText) findViewById(R.id.edit_fuel_grade);
        String fuelGrade = editText.getText().toString();
        intent.putExtra(FUEL_GRADE_TEXT, fuelGrade);

        // Fuel amount
        Log.i(TAG, "Finding fuel amount....");
        editText = (EditText) findViewById(R.id.edit_fuel_amount);
        String fuelAmountText = editText.getText().toString();
        float fuelAmount = Float.valueOf(fuelAmountText);
        // TODO: check decimal places
        intent.putExtra(FUEL_AMOUNT_FLOAT, fuelAmount);

        // unit cost
        Log.i(TAG, "Finding unit cost....");
        editText = (EditText) findViewById(R.id.edit_unit_cost);
        String unitCostText = editText.getText().toString();
        float unitCost = Float.valueOf(unitCostText);
        // TODO: check decimal places
        intent.putExtra(UNIT_COST_FLOAT, unitCost);

        // date
        Log.i(TAG, "Finding the date....");
        editText = (EditText) findViewById(R.id.edit_date);
        String dateText = editText.getText().toString();
        intent.putExtra(DATE_TEXT, dateText);

        Log.i(TAG, "Creating a new Intent and then starting the DisplayLogActivity....");
        startActivity(intent); // Start the activity specified by the intent
    }


}
