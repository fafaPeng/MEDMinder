package com.example.medminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import androidx.preference.PreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodayFragment extends ListFragment implements View.OnClickListener {
    public static ActivityEntriesAdapter adapter;
    public static String ENTRY_ID = "entry id";
    public static String FROM_HISTORY = "from history";
    private ReminderEntryDBHelper mDBHelper;
    private Intent mHelpIntent;
    private HelpButtonCustom mHelpButton;

    public static final String HELP_TYPE = "help_type";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d("lifecycle", "onCreate: called");

        //instantiate db helper and bind a new, empty adapter upon creation
        if (getActivity() != null) {

            mDBHelper = new ReminderEntryDBHelper(getContext());
            adapter = new ActivityEntriesAdapter(getContext(), R.layout.reminder_list_items, new ArrayList<>());
            Log.d("adapter created", "adapter created");

            setListAdapter(adapter);

        }
    }

    /**
     * Create view
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState saved instance state
     * @return view of adapter (list object)
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("lifecycle", "onCreateView: called");
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        mHelpButton = (HelpButtonCustom) view.findViewById(R.id.help_button_today);
        mHelpButton.setOnClickListener(this);

        return view;
    }

    /**
     * If this fragment is visible, load in all db entries
     *
     * @param isVisibleToUser is visible?
     */
    public void setMenuVisibility(boolean isVisibleToUser) {
        super.setMenuVisibility(isVisibleToUser);
        if (isVisibleToUser) {
            Log.d("TAG", "setMenuVisibility: is visible");
            LoaderThread loaderThread = new LoaderThread();
            loaderThread.start();
//            setListAdapter(adapter);
        } else Log.d("TAG", "setMenuVisibility: not visible");
    }

    /**
     * Instantiate db helper if null
     */
    public void onResume() {
        super.onResume();
        if (mDBHelper == null) mDBHelper = new ReminderEntryDBHelper(getContext());
        mDBHelper.fetchAllEntries();
        if (adapter == null )
            adapter = new ActivityEntriesAdapter(getContext(), R.layout.reminder_list_items, new ArrayList<>());
    }

    /**
     * Close db helper
     */
    @Override
    public void onPause() {
        super.onPause();
        mDBHelper.close();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.help_button_today:
                mHelpIntent = new Intent(getActivity(), HelpContentEntryActivity.class);
                mHelpIntent.putExtra(HELP_TYPE, "today");
                getActivity().startActivity(mHelpIntent);
                break;
            default:
                break;
        }
    }

        /**
         * Open display entry activity for selected list item
         *
         * @param listView ListView
         * @param view     View
         * @param position Position in list
         * @param id       id of selection
         */
    @Override
    public void onListItemClick(@NonNull ListView listView, @NonNull View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        //get chosen list item's DB ID
        long entryID = (Long) view.getTag();
        Log.d("id", "id from tag: " + view.getTag());
        ReminderEntry eEntry = mDBHelper.fetchEntryByIndex(entryID);
        //intent to go to new activity
        Intent intent = null;
        if (eEntry.getmReminderType() == 0) {
            intent = new Intent(getActivity(), DisplayEntryActivity.class);
        }
//        else intent = new Intent(getActivity(), MapDisplayActivity.class);
        intent.putExtra(ENTRY_ID, entryID);
        intent.putExtra(FROM_HISTORY,true);
        getActivity().startActivity(intent);
    }

//*****ADAPTER FOR THE LIST VIEW*****//
    public class ActivityEntriesAdapter extends ArrayAdapter<ReminderEntry> {
        private final Context mContext;
        private final List<ReminderEntry> entries;

        /**
         * Constructor for custom adapter
         *
         * @param mContext   Context
         * @param resourceID Layout file
         * @param entries    List of ExerciseEntries to pass to ListView
         */
        public ActivityEntriesAdapter(Context mContext, int resourceID, List<ReminderEntry> entries) {
            super(mContext, resourceID, entries);
            this.mContext = mContext;
            this.entries = entries;
        }

        /**
         * Format the two lines of each entry in the list view
         *
         * @param position    position in list
         * @param convertView view to show
         * @param parent      parent view group
         * @return view of final list object
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.reminder_list_items, parent, false);
            }

            ReminderEntry eEntry = entries.get(position);

            //set the first line of the view
            TextView firstLine = (TextView) convertView.findViewById(R.id.history_list_first_line);
            firstLine.setText(formatFirstLine(eEntry));

            //set the second line of the view
            TextView secondLine = (TextView) convertView.findViewById(R.id.history_list_second_line);
            secondLine.setText(formatSecondLine(eEntry));

            //Set colour if confirmed
            LinearLayout reminderItemContainer = (LinearLayout) convertView.findViewById(R.id.reminder_item_container);
            if (eEntry.getmConfirmed() == 1) {
                reminderItemContainer.setBackgroundColor(Color.parseColor("#4ab57c"));
            } else {
                reminderItemContainer.setBackgroundColor(Color.parseColor("#dddfdf"));
            }


            //set the id of each view
            convertView.setTag(eEntry.getId());

            return convertView;
        }

        /**
         * Re-fetch all entries when the db is changed so the list view is updated
         */
        @Override
        public void notifyDataSetChanged() {
            Log.d("notifyDataSetChanged", "notifyDataSetChanged: called");
            Log.d("notifyDataSetChanged", "is db helper null: " + (mDBHelper == null));
            if (mDBHelper != null) {
                //get all the entries now in the db on worker thread
                LoaderThread loaderThread = new LoaderThread();
                loaderThread.start();
            }
            super.notifyDataSetChanged();
        }

        /**
         * Returns item at position
         *
         * @param position position in list
         * @return item at position
         */
        @Override
        public ReminderEntry getItem(int position) {
            return entries.get(position);
        }

        /**
         * Get item id from position in list
         *
         * @param position position in list
         * @return item's id
         */
        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * Get number of items in adapter
         *
         * @return number of items in adapter
         */
        @Override
        public int getCount() {
            return entries.size();
        }



//****FORMATTING DISPLAY FUNCTIONS****//

        /**
         * Gets and formats the first line of the history entry
         */
        private String formatFirstLine(ReminderEntry entry) {

            String input = MedicationFragment.INPUT_TO_ID[entry.getmReminderType()];
            String activity = MedicationFragment.ACTIVITY_TO_ID[entry.getmMReminderMedicationType()];
            String dateTime = formatDateTime(entry.getmDateTime());
            return input + ": " + activity + ", " + dateTime;
        }

        /**
         * Gets and formats the second line of the history entry
         */
        private String formatSecondLine(ReminderEntry entry) {

            String medicationName = entry.getmMedicationName();
            Log.d("TodayFragment", "medicationName failed?");
            return medicationName;
        }
    }

//*******FORMATTING HELPER FUNCTIONS*******//

    /**
     * Convert the date and time from milliseconds to readable format
     *
     * @param dateTime dateTime in millis
     * @return date in hour:minute:second AM/PM month day year
     */
    public static String formatDateTime(long dateTime) {
        Date date = new Date(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa MMM dd yyyy");
        return sdf.format(date);
    }

//*******THREAD FOR LOADING IN ALL THE ENTRIES*******//
    private class LoaderThread extends Thread {
        List<ReminderEntry> list = null;

        /**
         * Create a new adapter with the updated list of db entries
         * Bind this new adapter
         */
        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                adapter = new ActivityEntriesAdapter(getContext(), R.layout.reminder_list_items, list);
                setListAdapter(adapter);
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());

        /**
         * Fetch all the entries from db
         */
        public void run() {
//            if (mDBHelper == null) mDBHelper = new ExerciseEntryDbHelper();
            if (mDBHelper != null) list = mDBHelper.fetchAllEntries();
            Log.d("thread run", "is list null " + (list == null));
            handler.post(runnable);
            Log.d("loader thread done", "loader thread done ");
        }
    }
}