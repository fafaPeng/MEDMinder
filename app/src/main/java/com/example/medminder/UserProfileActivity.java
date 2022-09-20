package com.example.medminder;

import androidx.appcompat.app.AppCompatActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


public class UserProfileActivity extends AppCompatActivity {

    //SharedPreferences object, UI widgets, file names + Uris
    private SharedPreferences sharedPref;
    private EditText nameField;
    private EditText ageField;
    private EditText phoneField;
    private EditText addressField;
    private EditText happyField;
    private RadioButton maleButton;
    private RadioButton femaleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set content view
        setContentView(R.layout.activity_user_profile);

        nameField = (EditText) findViewById(R.id.name_field);
        ageField = (EditText) findViewById(R.id.age_field);
        phoneField = (EditText) findViewById(R.id.phone_field);
        addressField = (EditText) findViewById(R.id.address_field);
        happyField = (EditText) findViewById(R.id.happy_field);
        maleButton = (RadioButton) findViewById(R.id.male_button);
        femaleButton = (RadioButton) findViewById(R.id.female_button);

        //Load saved profile
        loadProfile();
    }

    //Load profile
    public void loadProfile() {
        sharedPref = this.getPreferences(MODE_PRIVATE);
        nameField = ((EditText) findViewById(R.id.name_field));
        nameField.setText(sharedPref.getString(getString(R.string.user_name_hint), ""));
        //input all saved field data
        ageField.setText(sharedPref.getString(getString(R.string.user_age_hint), ""));
        phoneField.setText(sharedPref.getString(getString(R.string.user_phone_hint), ""));
        maleButton.setChecked(sharedPref.getBoolean(getString(R.string.user_gender_male), false));
        femaleButton.setChecked(sharedPref.getBoolean(getString(R.string.user_gender_female), false));
        addressField.setText(sharedPref.getString(getString(R.string.user_address_hint), ""));
        happyField.setText(sharedPref.getString(getString(R.string.user_happy_hint), ""));
    }


    //Buttons
    //Save Button
    public void onSaveClicked(View view) {
        //store in shared preferences
        sharedPref = this.getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        //store all the input values in the shared preferences object
        editor.putString(getString(R.string.user_name_hint), nameField.getText().toString());
        editor.putString(getString(R.string.user_age_hint), ageField.getText().toString());
        editor.putString(getString(R.string.user_phone_hint), phoneField.getText().toString());
        editor.putString(getString(R.string.user_address_hint), addressField.getText().toString());
        editor.putString(getString(R.string.user_happy_hint), happyField.getText().toString());
        editor.putBoolean(getString(R.string.user_gender_female), femaleButton.isChecked());
        editor.putBoolean(getString(R.string.user_gender_male), maleButton.isChecked());
        editor.apply();
        //Toast saying saved
        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show();
        //Kill the activity
        this.finish();
    }

    public void onCancelClicked(View view) {
        this.finish();
    }

}