package com.example.medminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.preference.PreferenceManager;

import com.example.medminder.databinding.ActivityMainBinding;

import java.util.Calendar;
import java.util.HashMap;

public class ManualEntryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, TimePickerDialog.OnTimeSetListener,
        DatePickerDialog.OnDateSetListener {

    //labels for ListView
    public static final String[] MANUAL_OPTIONS = new String[]{"Date", "Time", "Medication Name", "Quantity", "Special Notes"};

    //positions in the ListView
    private static final int DATE_POS = 0;
    private static final int TIME_POS = 1;
    private static final int NAME_POS = 2;
    private static final int QUANTITY_POS = 3;
    private static final int COMMENT_POS = 4;


    //tags to restore date and time selections
    private final String DATE_TIME_TAG = "date time tag";
    private static final String TIME_PICKER_TAG = "time picker";
    private static final String HOUR_TAG = "hour";
    private static final String MINUTE_TAG = "minute";
    private static final String DATE_PICKER_TAG = "date picker";
    private static final String YEAR_TAG = "year";
    private static final String MONTH_TAG = "month";
    private static final String DAY_TAG = "day";

    // Alarm
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Calendar calendar;

//    public SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss aa MMM dd yyyy");

    //widgets
    public Calendar mDateAndTime;
    public ListView mListView;
    private MyTimePicker mTimePicker;
    private MyDatePicker mDatePicker;
    private DialogFragment mDialogFragment;

    //flags for visibility of date and time picker dialogs
    private boolean mTimePickerVisible;
    private boolean mDatePickerVisible;

    //database
    public static ReminderEntry entry;
    private ReminderEntryDBHelper mDBHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        mDateAndTime = Calendar.getInstance();
        //create and bind adapter
        mListView = (ListView) findViewById(R.id.manual_list_view);
        ArrayAdapter<String> manualAdapter = new ArrayAdapter<>(this, R.layout.manual_list_items, MANUAL_OPTIONS);
        mListView.setAdapter(manualAdapter);
        //bind the onClickListener
        mListView.setOnItemClickListener(this);
        //initialize an entry and the db helper
        entry = new ReminderEntry();

        createNotificationChannel();

        Bundle bundle = getIntent().getExtras();
        entry.setmReminderType(bundle.getInt(MedicationFragment.REMINDER_TYPE,0));
        entry.setmMReminderMedicationType(bundle.getInt(MedicationFragment.MEDICATION_TYPE,0));

        if (savedInstanceState != null) {
            mDateAndTime.setTimeInMillis(savedInstanceState.getLong(DATE_TIME_TAG));
            //if the TimePickerDialog last state was open, restore its state
            if (savedInstanceState.getBoolean(TIME_PICKER_TAG)) {
                mDateAndTime.set(Calendar.HOUR_OF_DAY, savedInstanceState.getInt(HOUR_TAG));
                mDateAndTime.set(Calendar.MINUTE, savedInstanceState.getInt(MINUTE_TAG));
                onTimeClicked();
            }
            //if the DatePickerDialog last state was open, restore its state
            if (savedInstanceState.getBoolean(DATE_PICKER_TAG)) {
                mDateAndTime.set(Calendar.YEAR, savedInstanceState.getInt(YEAR_TAG));
                mDateAndTime.set(Calendar.MONTH, savedInstanceState.getInt(MONTH_TAG));
                mDateAndTime.set(Calendar.DAY_OF_MONTH, savedInstanceState.getInt(DAY_TAG));
                onDateClicked();
            }
        }

//        startAlert();

    }

//    public void startAlert(){
//        int i = 5;
//        Intent intent = new Intent(this, MyBroadcastReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                this.getApplicationContext(), 234324243, intent, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (i * 1000), pendingIntent);
//        Toast.makeText(this, "Alarm set in " + i + " seconds",Toast.LENGTH_LONG).show();
//    }

    private void setAlarm() {
        Log.d("DEBUG","In setAlarm");
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,mDateAndTime.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,pendingIntent);
        Toast.makeText(this, "Alarm set Successfully", Toast.LENGTH_SHORT).show();
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("DEBUG","In createNotificationChannel");
            CharSequence name = "Alarm Channel";
            String description = "Channel For Alarm Manager";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Alarm", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TIME_PICKER_TAG, mTimePickerVisible);
        outState.putBoolean(DATE_PICKER_TAG, mDatePickerVisible);
//        Log.d("save state", "date time: "+sdf.format(mDateAndTime.getTimeInMillis()));
        outState.putLong(DATE_TIME_TAG, mDateAndTime.getTimeInMillis());
        if (mDateAndTime != null) {
            outState.putInt(HOUR_TAG, mDateAndTime.get(Calendar.HOUR_OF_DAY));
            outState.putInt(MINUTE_TAG, mDateAndTime.get(Calendar.MINUTE));
            outState.putInt(YEAR_TAG, mDateAndTime.get(Calendar.YEAR));
            outState.putInt(MONTH_TAG, mDateAndTime.get(Calendar.MONTH));
            outState.putInt(DAY_TAG, mDateAndTime.get(Calendar.DAY_OF_MONTH));
        }
    }

    /**
     * Instantiate DB Helper, if null
     */
    @Override
    public void onResume() {
        super.onResume();
        if(mDBHelper == null) mDBHelper = new ReminderEntryDBHelper(this);
    }

    /**
     * Close DB Helper on pause
     */
    @Override
    public void onPause() {
        mDBHelper.close();
        super.onPause();
    }

//*********INTERFACE FUNCTIONS*********//

    /**
     * onClickListener callback to create the corresponding dialog fragment
     *
     * @param parent   Parent View
     * @param view     Current View
     * @param position Which item was clicked
     * @param id       ID
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == DATE_POS) {
            onDateClicked();
        } else if (position == TIME_POS) {
            onTimeClicked();
        } else if (position == NAME_POS) {
            mDialogFragment =
                    ReminderDialogFragment.newInstance(ReminderDialogFragment.NAME_PICKER_ID);
            mDialogFragment.show(getSupportFragmentManager(), getString(R.string.medication_entry_name));
        } else if (position == QUANTITY_POS) {
            mDialogFragment =
                    ReminderDialogFragment.newInstance(ReminderDialogFragment.QUANTITY_PICKER_ID);
            mDialogFragment.show(getSupportFragmentManager(), getString(R.string.medication_entry_quantity));
        } else if (position == COMMENT_POS) {
            mDialogFragment =
                    ReminderDialogFragment.newInstance(ReminderDialogFragment.NOTES_PICKER_ID);
            mDialogFragment.show(getSupportFragmentManager(), getString(R.string.medication_special_notes));
        }
    }

    /**
     * If "Time" was clicked, display a custom TimePickerDialog
     */
    public void onTimeClicked() {
        //set flag of time picker dialog's visibility to true
        mTimePickerVisible = true;
        mTimePicker = new MyTimePicker(this, this,
                mDateAndTime.get(Calendar.HOUR_OF_DAY),
                mDateAndTime.get(Calendar.MINUTE), false);
        //"cancel" click callback to reset clock
        mTimePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Log.d("onCancel", "cancel callback");
                Calendar now = Calendar.getInstance();
                mTimePicker.updateTime(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            }
        });
        mTimePicker.show();
    }

    /**
     * TimePicker Listener to save the set time
     *
     * @param view      TimePicker
     * @param hourOfDay Hour chosen
     * @param minute    Minute chosen
     */
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDateAndTime.set(Calendar.MINUTE, minute);
        entry.setmDateTime(mDateAndTime.getTimeInMillis());
//        Log.d("onTimeSet", "date time: "+sdf.format(entry.getmDateTime()));
    }

    /**
     * If "Date" was clicked, display a custom DatePickerDialog
     */
    public void onDateClicked() {
        mDatePickerVisible = true;
        mDatePicker = new MyDatePicker(this, this,
                mDateAndTime.get(Calendar.YEAR), mDateAndTime.get(Calendar.MONTH),
                mDateAndTime.get(Calendar.DAY_OF_MONTH));
        //"cancel" click callback to reset calendar
        mDatePicker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                Log.d("onCancel", "cancel callback");
                Calendar now = Calendar.getInstance();
                mDatePicker.updateDate(now.get(Calendar.YEAR), now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH));
            }
        });
        mDatePicker.show();
    }

    /**
     * DatePicker Listener to save the set date
     *
     * @param view       TimePicker
     * @param year       Year chosen
     * @param month      Month chosen
     * @param dayOfMonth Day chosen
     */
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Log.d("onDateSet", "month: "+month);
        mDateAndTime.set(Calendar.YEAR, year);
        mDateAndTime.set(Calendar.MONTH, month);
        mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        entry.setmDateTime(mDateAndTime.getTimeInMillis());
//        Log.d("onDateSet", "date time: "+sdf.format(entry.getmDateTime()));
    }

//*********BOTTOM BUTTON HANDLERS*********//

    /**
     * "Cancel" button click callback
     *
     * @param view View
     */
    public void onCancelClick(View view) {
        Toast.makeText(this, getString(R.string.manual_cancel_msg),
                Toast.LENGTH_SHORT).show();
        this.finish();
    }

    /**
     * "Save" button click callback
     *
     * @param view View
     */
    public void onSaveClick(View view) {
        DBWriterThread dbWriterThread = new DBWriterThread(entry);
        dbWriterThread.start();
        this.finish();
    }

    public void onSetAlarm(View view) {
        setAlarm();
    }

//********CUSTOM DATE/TIME PICKER DIALOGS********//

    /**
     * A custom TimePickerDialog that can handle retaining the dialog's instance state on interruption
     */
    private class MyTimePicker extends TimePickerDialog {

        /**
         * Construct TimePickerDialog (same as super class)
         * @param context       Context
         * @param listener      OnTimeSetListener
         * @param hourOfDay     Hour
         * @param minute        Minute
         * @param is24HourView  false
         */
        public MyTimePicker(Context context, OnTimeSetListener listener, int hourOfDay,
                            int minute, boolean is24HourView) {
            super(context, listener, hourOfDay, minute, is24HourView);
        }

        /**
         * Retain the edited time for rotations and other interruptions
         *
         * @param view      TimePicker
         * @param hourOfDay Hour set on the Dialog
         * @param minute    Minute set on the Dialog
         */
        public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
            mDateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            mDateAndTime.set(Calendar.MINUTE, minute);
        }

        @Override
        public void dismiss() {
            //switch time picker visibility flag to false when dialog dismissed
            mTimePickerVisible = false;
            super.dismiss();
        }
    }

    /**
     * A custom DatePickerDialog that can handle retaining the dialog's instance state on interruption
     */
    private class MyDatePicker extends DatePickerDialog {
        /**
         * Construct DatePickerDialog (same as super class)
         * @param context       Context
         * @param listener      onDateSetListener
         * @param year          Year
         * @param month         Month
         * @param dayOfMonth    Day
         */
        public MyDatePicker(Context context, OnDateSetListener listener, int year, int month, int dayOfMonth) {
            super(context, listener, year, month, dayOfMonth);
        }

        /**
         * Retain the edited month for rotations and other interruptions
         *
         * @param view       TimePicker
         * @param year       Year set on the Dialog
         * @param month      Month set on the Dialog
         * @param dayOfMonth Day set on the Dialog
         */
        public void onDateChanged(DatePicker view, int year, int month, int dayOfMonth) {
            mDateAndTime.set(Calendar.YEAR, year);
            mDateAndTime.set(Calendar.MONTH, month);
            mDateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }

        @Override
        public void dismiss() {
            //switch date picker visibility flag to false when dialog dismissed
            mDatePickerVisible = false;
            super.dismiss();
        }

    }
    //*****THREAD TO SAVE ENTRY TO DB*****
    private class DBWriterThread extends Thread{
        //local instance vars
        ReminderEntry eEntry;
        long id;

        /**
         * Construct a thread to writer to db in background
         * @param eEntry    ExerciseEntry being inserted
         */
        public DBWriterThread(ReminderEntry eEntry){
            this.eEntry = eEntry;
        }

        /**
         * Tell the history fragment's adapter to update the adapter
         * Make toast saying the entry of given id was saved
         */
        private final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(TodayFragment.adapter != null) TodayFragment.adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), "Entry #"+ id +" saved.",
                        Toast.LENGTH_SHORT).show();
            }
        };

        Handler handler = new Handler(Looper.getMainLooper());

        /**
         * Insert a new ExerciseEntry and get its db ID
         */
        public void run(){
            if(mDBHelper == null) mDBHelper = new ReminderEntryDBHelper(getApplicationContext());
            id = mDBHelper.insertEntry(eEntry);
            handler.post(runnable);
//            Log.d("writing to db", "entry was added with id "+id);
        }
    }
}