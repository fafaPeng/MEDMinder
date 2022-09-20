package com.example.medminder;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import java.util.HashMap;
import java.util.Map;

public class MedicationFragment extends Fragment implements View.OnClickListener{
    //widgets + intent
    private Button mStartButton;
    private Spinner mReminderSpinner, mMedicationSpinner;
    private Intent mIntent;
    private Intent mHelpIntent;
    private HelpButtonCustom mHelpButton;

    //input types
    public static final String MEDICATION_ENTRY = "Medication";
    public static final String GENERAL_ENTRY = "General";
    public static final String SPECIAL_ENTRY = "Special Events";
    public static final Map<String,Integer> INPUT_TO_ID_MAP;
    static{
        INPUT_TO_ID_MAP = new HashMap<>();
        INPUT_TO_ID_MAP.put(MEDICATION_ENTRY,0);
        INPUT_TO_ID_MAP.put(GENERAL_ENTRY,1);
        INPUT_TO_ID_MAP.put(SPECIAL_ENTRY,2);
    }
    public static final String[] INPUT_TO_ID = {"Medication Entry", "General Entry", "Special Entry"};
    public static final Map<String,Integer> ACTIVITY_TO_ID_MAP;
    static{
        ACTIVITY_TO_ID_MAP = new HashMap<>();
        ACTIVITY_TO_ID_MAP.put("Tablet", 0);
        ACTIVITY_TO_ID_MAP.put("Capsule", 1);
        ACTIVITY_TO_ID_MAP.put("Liquid", 2);
        ACTIVITY_TO_ID_MAP.put("Drops", 3);
        ACTIVITY_TO_ID_MAP.put("Injection", 4);
    }
    public static final String[] ACTIVITY_TO_ID = {"Tablet", "Capsule", "Liquid",
            "Drops", "Injection"};

    //intent keys
    public static final String REMINDER_TYPE = "reminder_type";
    public static final String MEDICATION_TYPE = "medication_type";
    public static final String HELP_TYPE = "help_type";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //inflate layout
        View view = inflater.inflate(R.layout.fragment_medication, container, false);
        //get buttons and spinners
        //get buttons and spinners
        mStartButton = (Button) view.findViewById(R.id.start_start_button);
        mHelpButton = (HelpButtonCustom) view.findViewById(R.id.help_button_medication);
        mReminderSpinner = (Spinner) view.findViewById(R.id.medication_input_spinner);
        mMedicationSpinner = (Spinner) view.findViewById(R.id.medication_medication_spinner);
        //bind click listener
        mStartButton.setOnClickListener(this);
        mHelpButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_start_button:
                String reminderType = mReminderSpinner.getSelectedItem().toString();
                mIntent = new Intent(getActivity(), ManualEntryActivity.class);
                mIntent.putExtra(REMINDER_TYPE, INPUT_TO_ID_MAP.get(reminderType));
                getActivity().startActivity(mIntent);
                break;
            case R.id.help_button_medication:
                mHelpIntent = new Intent(getActivity(), HelpContentEntryActivity.class);
                mHelpIntent.putExtra(HELP_TYPE, "medication");
                getActivity().startActivity(mHelpIntent);
                break;
            default:
                break;
        }

    }
}