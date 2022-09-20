package com.example.medminder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

public class DisplayEntryActivity extends AppCompatActivity {
    //instance vars
    private long mEntryID;
    private ReminderEntry currentEntry;
    private ReminderEntryDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_entry);

        //instantiate the db helper
        mDBHelper = new ReminderEntryDBHelper(this);

        //get intent info and chosen entry by its db id
        Bundle bundle = getIntent().getExtras();
        mEntryID = bundle.getLong(TodayFragment.ENTRY_ID);
        ReminderEntry eEntry = mDBHelper.fetchEntryByIndex(mEntryID);
        this.currentEntry = eEntry;

        //set up and fill in widgets (edit texts)

        //Medication time
        EditText dateTimeText = (EditText) findViewById(R.id.medication_time);
        dateTimeText.setText(TodayFragment.formatDateTime(eEntry.getmDateTime()));

        //Medication name
        EditText medicationNameText = (EditText) findViewById(R.id.medication_name);
        medicationNameText.setText(eEntry.getmMedicationName());
        //Medication type
        EditText medicationQuantityText = (EditText) findViewById(R.id.medication_quantity);
        medicationQuantityText.setText(eEntry.getmMedicationQuantity());
        //Medication notes
        EditText medicationNotesText = (EditText) findViewById(R.id.medication_notes);
        medicationNotesText.setText(eEntry.getmMedicationNotes());
    }

    /**
     * create "DELETE" button in top menu
     * @param menu  Menu where DELETE button resides
     * @return      true
     */
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        menu.add(Menu.NONE,0,0,"DELETE").
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        String secondaryAction = this.currentEntry.getmConfirmed() == 1 ? "UNCONFIRM" : "CONFIRM";
        menu.add(Menu.NONE,1,1,secondaryAction).
                setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    /**
     * remove item from database when DELETE is pressed
     * @param item  the DELETE button
     * @return      true
     */
    public boolean onOptionsItemSelected(MenuItem item){
        super.onOptionsItemSelected(item);
        if(item.getTitle().equals("DELETE")) {
            DeleteThread deleteThread = new DeleteThread();
            deleteThread.start();
        }
        if(item.getTitle().toString().contains("CONFIRM")) {
            ConfirmThread confirmThread = new ConfirmThread();
            confirmThread.start();
        }
        this.finish();
        return true;
    }

    public void onResume(){
        super.onResume();
        //rebind db helper
        if(mDBHelper == null) mDBHelper = new ReminderEntryDBHelper(this);
    }
    public void onPause(){
        //close the db helper
        mDBHelper.close();
        super.onPause();
    }

    /**
     * Worker thread to run deletion on
     */
    private class DeleteThread extends Thread{
        //runnable to update adapter and UI in history fragment and show toast
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TodayFragment.adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Entry deleted.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        Handler handler  = new Handler(Looper.getMainLooper());
        //run the deletion on a worker thread
        public void run(){
            if(mDBHelper != null) mDBHelper.removeEntry(mEntryID);
            handler.post(runnable);
        }
    }
    /**
     * Worker thread to run update confirmation on
     */
    private class ConfirmThread extends Thread{
        //runnable to update adapter and UI in history fragment and show toast
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                TodayFragment.adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Entry #"+mEntryID+" confirmed.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        Handler handler  = new Handler(Looper.getMainLooper());
        //run the confirmation on a worker thread
        public void run(){
            if(mDBHelper != null) {
                ReminderEntry eEntry = mDBHelper.fetchEntryByIndex(mEntryID);
                eEntry.setmConfirmed(eEntry.getmConfirmed() == 1 ? 0 : 1);
                mDBHelper.updateEntry(eEntry);
            }
            handler.post(runnable);
        }
    }

}