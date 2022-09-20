package com.example.medminder;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;

public class ReminderEntry {

    private long id;
    private int mReminderType;  // Medication, General, Special Events
    private int mMReminderMedicationType;     // 1,2,3,4,5
    private long mDateTime = Calendar.getInstance().getTimeInMillis();    // When does this entry happen
    private String mMedicationName;         // Medication name
    private String mMedicationQuantity;
    private String mMedicationNotes;       // Comments
    private int mConfirmed;
    private ArrayList<LatLng> mLatLngs;

    /**
     * Construct a default Exercise Entry object
     */
    public ReminderEntry() {
        mReminderType = 0;
        mMReminderMedicationType = 0;
        mMedicationQuantity = "1 tablet";
        mLatLngs = new ArrayList<>();
    }

    //id getter and setter
    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }


    //Reminder type getter and setter
    public void setmReminderType(int mReminderType) {
        this.mReminderType = mReminderType;
    }
    public int getmReminderType() {
        return mReminderType;
    }


    //Medication type getter and setter
    public void setmMReminderMedicationType(int mMReminderMedicationType) {
        this.mMReminderMedicationType = mMReminderMedicationType;
    }
    public int getmMReminderMedicationType() {
        return mMReminderMedicationType;
    }


    //dateTime getter and setter
    public void setmDateTime(long mDateTime) {
        this.mDateTime = mDateTime;
    }
    public long getmDateTime() {
        return this.mDateTime;
    }


    //Medication Name getter and setter
    public void setmMedicationName(String mMedicationName) {
        this.mMedicationName = mMedicationName;
    }
    public String getmMedicationName() {
        return mMedicationName;
    }

    //Medication Quantity getter and setter
    public void setmMedicationQuantity (String mMedicationQuantity) {
        this.mMedicationQuantity = mMedicationQuantity;
    }
    public String getmMedicationQuantity() {
        return mMedicationQuantity;
    }


    //Medication notes getter and setter
    public void setmMedicationNotes(String mMedicationNotes) {
        this.mMedicationNotes = mMedicationNotes;
    }
    public String getmMedicationNotes() {
        return mMedicationNotes;
    }

    public void setmConfirmed(int confirmed) {
        this.mConfirmed = confirmed;
    }
    public int getmConfirmed() {
        return mConfirmed;
    }

    //latlngs setter getter and add
    public ArrayList<LatLng> getmLatLngs() { return mLatLngs; }

    public void setmLatLngs(ArrayList<LatLng> mLatLngs) { this.mLatLngs = mLatLngs; }

    public void addLatLng(LatLng latLng) { mLatLngs.add(latLng);}


    //toString
    @Override
    public String toString() {
        return mReminderType + ": " + mMReminderMedicationType + ", " + mDateTime;
    }
}
