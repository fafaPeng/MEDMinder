package com.example.medminder;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

public class InformationFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //Load preferences from xml
        setPreferencesFromResource(R.xml.preference, rootKey);
    }
}
