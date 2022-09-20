package com.example.medminder;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;


public class ReminderDialogFragment extends DialogFragment {

    private static final String DIALOG_ID_KEY = "dialog id";

    //dialog ids and titles
    public static final int NAME_PICKER_ID = 0;
    public static final int QUANTITY_PICKER_ID = 1;
    public static final int NOTES_PICKER_ID = 2;
    public static final int[] ID_LABEL = {
            R.string.medication_entry_name,
            R.string.medication_entry_quantity,
            R.string.medication_special_notes};

    //storing EditText data
    public static final String TEXT_TAG = "text";
    private EditText mEditText;

    /**
     * Create a new instance of DialogFragment
     *
     * @param dialog_id Which category DialogFragment is being created for
     * @return DialogFragment for the specified category
     */
    public static ReminderDialogFragment newInstance(int dialog_id) {
        ReminderDialogFragment dialogFrag = new ReminderDialogFragment();
        Bundle inputBundle = new Bundle();
        inputBundle.putInt(DIALOG_ID_KEY, dialog_id);
        dialogFrag.setArguments(inputBundle);

        return dialogFrag;
    }

    /**
     * Save the text in the editText, if there is any, and put it in the outState Bundle
     *
     * @param outState outState Bundle
     */
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Dialog dialog = getDialog();
        if (dialog != null && mEditText != null) {
            Log.d("MY_TAG", "DialogFragment-onSaveInstanceState: text here = " + mEditText.getText().toString());
            outState.putString(TEXT_TAG, mEditText.getText().toString());
        }
    }

    /**
     * Determine which dialog to create (photo picker or editText)
     *
     * @param savedInstanceState Saved instance state Bundle
     * @return Either a photo dialog to choose source of profile photo or editText dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        int dialog_id = getArguments().getInt(DIALOG_ID_KEY);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        //save the text that was in the EditText, if there was any, so it can be passed to new dialog
        String prevText = "";
        if (savedInstanceState != null) {
            prevText = savedInstanceState.getString(TEXT_TAG);
        }

        //create edit text dialog
        if (dialog_id >= NAME_PICKER_ID && dialog_id <= NOTES_PICKER_ID) {
            buildEditTextDialog(dialogBuilder, prevText, dialog_id);
            return dialogBuilder.create();
        } else {
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        Dialog dialog = getDialog();
        //don't dismiss the dialog box on rotation
        if (dialog != null && getRetainInstance()) {
            dialog.setDismissMessage(null);
        }
        //erase any instance of the editText now that the dialog is dismissed
        mEditText = null;
        super.onDestroyView();
    }


    /**
     * Build the edit number or text dialog
     *
     * @param dialogBuilder DialogBuilder
     * @param prevText      Text previously in the EditText
     * @param id            Which category was chosen
     */
    private void buildEditTextDialog(final AlertDialog.Builder dialogBuilder, final String prevText, int id) {
        //set up title
        dialogBuilder.setTitle(getString(ID_LABEL[id]));

        //create editText and fill it with prevText
        mEditText = new EditText(getContext());
        mEditText.setText(prevText);

        if (id == QUANTITY_PICKER_ID) {
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditText.setHint(R.string.medication_quantity_hint);
        }
         else if (id == NAME_PICKER_ID) {
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditText.setHint(R.string.medication_name_hint);
        }
        else if (id == NOTES_PICKER_ID) {
            mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            mEditText.setHint(R.string.medication_notes_hint);
        }

        //put the editText into the dialog's view
        dialogBuilder.setView(mEditText);

        //callback for clicking "OK"
        dialogBuilder.setPositiveButton(R.string.ok_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int buttonId) {
                String input = mEditText.getText().toString();

                //set the current entry's attributes when "OK" is clicked
                if (id == NAME_PICKER_ID) {
                    ManualEntryActivity.entry.setmMedicationName(input);
                } else if (id == QUANTITY_PICKER_ID) {
                    ManualEntryActivity.entry.setmMedicationQuantity(input);
                } else if (id == NOTES_PICKER_ID) {
                    ManualEntryActivity.entry.setmMedicationNotes(input);
                }
            }
        });

        //callback for clicking "cancel"
        dialogBuilder.setNegativeButton(R.string.cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int buttonId) {
            }
        });
    }
}
