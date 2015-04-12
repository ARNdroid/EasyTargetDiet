package br.com.arndroid.etdiet.utils;

import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.Date;

import br.com.arndroid.etdiet.provider.Contract;

public class CurrentDateId {
    private String mCurrentDateId = null;

    private Date mCurrentDateForTests = null;

    public void setCurrentDateId(String currentDateId) {
        if (isDateIdCurrentDate(currentDateId)) {
            mCurrentDateId = null;
        } else {
            mCurrentDateId = currentDateId;
        }
    }

    private boolean isDateIdCurrentDate(String currentDateId) {
        return mCurrentDateForTests == null ? DateUtils.isDateIdCurrentDate(currentDateId) :
                DateUtils.dateToDateId(mCurrentDateForTests).equals(currentDateId);
    }

    public String getCurrentDateId() {
        return mCurrentDateId == null ? DateUtils.dateToDateId(getCurrentDate()) : mCurrentDateId;
    }

    private Date getCurrentDate() {
        return mCurrentDateForTests == null ? new Date() : mCurrentDateForTests;
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(Contract.Days.DATE_ID, mCurrentDateId);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        mCurrentDateId = savedInstanceState == null ? null :
                savedInstanceState.getString(Contract.Days.DATE_ID);
    }

    public void setCurrentDateForTests(Date currentDateForTests) {
        mCurrentDateForTests = currentDateForTests;
    }
}
