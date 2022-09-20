package com.example.medminder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CustomStreakDialog extends AppCompatActivity {
    private ReminderEntryDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view
        setContentView(R.layout.dialog_streak);
        //instantiate the db helper
        mDBHelper = new ReminderEntryDBHelper(this);
        ArrayList<ReminderEntry> reminders = mDBHelper.fetchAllEntries();
        int totalReminders = reminders.size();
        int confirmedReminders = this.getConfirmedReminders(reminders);
        TextView streakTextView = (TextView) findViewById(R.id.streak_confirmed_total);
        String streakInformation = "You have completed " + confirmedReminders + " medication reminders out of " + totalReminders + " this week.";
        streakTextView.setText(streakInformation);
    }
    
    private int getConfirmedReminders(ArrayList<ReminderEntry> reminderEntries) {
        if (reminderEntries.size() == 0) return 0;
        int count = 0;
        for (ReminderEntry reminder:reminderEntries) {
            if (reminder.getmConfirmed() > 0) count++;
        }
        return count;
    }
}
