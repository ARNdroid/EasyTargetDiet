package br.com.arndroid.etdiet.virtualweek;

import android.content.Context;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.provider.parametershistory.ParametersHistoryManager;

public class WeekPeriod {

    private Date mInitialDate;
    private Date mFinalDate;
    final private Date mReferenceDate;

    public WeekPeriod(Context context, Date referenceDate) {

        mReferenceDate = referenceDate;
        setInitialAndFinalDates(context, referenceDate);
    }

    private void setInitialAndFinalDates(Context context, Date referenceDate) {

        int trackingWeekday = new ParametersHistoryManager(context).getTrackingWeekdayForDate(referenceDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(mReferenceDate);
        int referenceDateWeekday = calendar.get(Calendar.DAY_OF_WEEK);
        int deltaDays;

        switch (trackingWeekday) {
            case Calendar.SUNDAY:
                deltaDays = Calendar.SATURDAY - referenceDateWeekday;
                break;
            case Calendar.MONDAY:
                deltaDays = Calendar.SUNDAY - referenceDateWeekday;
                break;
            case Calendar.TUESDAY:
                deltaDays = Calendar.MONDAY - referenceDateWeekday;
                break;
            case Calendar.WEDNESDAY:
                deltaDays = Calendar.TUESDAY - referenceDateWeekday;
                break;
            case Calendar.THURSDAY:
                deltaDays = Calendar.WEDNESDAY - referenceDateWeekday;
                break;
            case Calendar.FRIDAY:
                deltaDays = Calendar.THURSDAY - referenceDateWeekday;
                break;
            case Calendar.SATURDAY:
                deltaDays = Calendar.FRIDAY - referenceDateWeekday;
                break;
            default:
                throw new IllegalStateException("Invalid trackingWeekday = " + trackingWeekday);
        }

        if(deltaDays < 0) {
            deltaDays += 7;
        }

        calendar.add(Calendar.DAY_OF_MONTH, deltaDays);
        mFinalDate = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        mInitialDate = calendar.getTime();
    }

    public Date getInitialDate() {
        return mInitialDate;
    }

    public Date getFinalDate() {
        return mFinalDate;
    }

    public Date getReferenceDate() {
        return mReferenceDate;
    }
}
