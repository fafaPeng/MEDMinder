package com.example.medminder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HelpContentEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        //get intent info and chosen entry by its db id
        Bundle bundle = getIntent().getExtras();
        String helpKey = bundle.getString(MedicationFragment.HELP_TYPE);
        if (helpKey.equals("medication")) {
            helpMedication();
        }
        if (helpKey.equals("today")) {
           helpToday();
        }
    }

    public void helpMedication(){

        //set up and fill in help page

        //Help page title
        TextView helpPageTitle = (TextView) findViewById(R.id.help_title);
        helpPageTitle.setText("Medication\n");
        //Help page content type
        TextView helpPageContent = (TextView) findViewById(R.id.help_content);
        helpPageContent.setText("\nHi! To create a reminder press start and you will be prompted to enter information such as the Data and Time you want to be reminded, the Medication Name, the Medication Type, and any Special Notes that you may find helpful. Press back to continue where you left off.\n");
    }

    public void helpToday(){

        //set up and fill in help page

        //Help page title
        TextView helpPageTitle = (TextView) findViewById(R.id.help_title);
        helpPageTitle.setText("Today\n");
        //Help page content type
        TextView helpPageContent = (TextView) findViewById(R.id.help_content);
        helpPageContent.setText("\nHere you can see all you reminders! All your reminders that you created for today will show up here. Click on the reminder to see the detailed view such as Medication Name, Type, and your Special Note. Press back to continue where you left off.\n");
    }
}