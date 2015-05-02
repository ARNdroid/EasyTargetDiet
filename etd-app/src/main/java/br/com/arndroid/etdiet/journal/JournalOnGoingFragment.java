package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.compat.Compatibility;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalOnGoingFragment extends Fragment implements DateDialog.OnDateSetListener {

    public interface JournalFragmentListener {

        void onDateChanged(Date newDate);
    }

    public static final String OWNER_TAG = JournalOnGoingFragment.class.getSimpleName();
    public static final String DATE_EDIT_TAG = OWNER_TAG + ".DATE_EDIT_TAG";

    private String mCurrentDateId;
    private FoodsUsageEntity mQuickUsage;

    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysNameArray;

    private LinearLayout mLayDate;
    private LinearLayout mLayQuickUsage;

    private TextView mTxtDay;
    private TextView mTxtMonth;
    private TextView mTxtWeekday;
    private TextView mTxtHeader;
    private TextView mTxtDetails;
    private TextView mTxtFooter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_on_going_fragment, container, false);

        mMonthsShortNameArray = getResources().getStringArray(R.array.months_short_name_list);
        mWeekdaysNameArray = getResources().getStringArray(R.array.weekdays_short_name_list);

        bindScreen(rootView);
        setupScreen();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof JournalFragmentListener)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    JournalFragmentListener.class.getSimpleName());
        }
    }

    private void bindScreen(View rootView) {
        mLayDate = (LinearLayout) rootView.findViewById(R.id.layDate);
        mLayQuickUsage = (LinearLayout) rootView.findViewById(R.id.layQuickUsage);
        mTxtDay = (TextView) rootView.findViewById(R.id.txtDay);
        mTxtMonth = (TextView) rootView.findViewById(R.id.txtMonth);
        mTxtWeekday = (TextView) rootView.findViewById(R.id.txtWeekday);
        mTxtHeader = (TextView) rootView.findViewById(R.id.txtHeader);
        mTxtDetails = (TextView) rootView.findViewById(R.id.txtDetails);
        mTxtFooter = (TextView) rootView.findViewById(R.id.txtFooter);
    }

    private void setupScreen() {
        mLayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DateDialog dialog = new DateDialog();
                dialog.setTitle(getString(R.string.date));
                dialog.setCalendarViewShown(true);
                dialog.setSpinnerShown(false);
                dialog.setInitialValue(DateUtils.dateIdToDate(mCurrentDateId));
                dialog.show(getFragmentManager(), DATE_EDIT_TAG);
            }
        });

        mLayQuickUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mQuickUsage != null) {
                    mQuickUsage.setDateId(mCurrentDateId);
                    mQuickUsage.setTime(DateUtils.dateToTimeAsInt(new Date()));
                    final FoodsUsageManager manager = new FoodsUsageManager(getActivity().getApplicationContext());
                    manager.refresh(mQuickUsage);
                }
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        final String dateId = daySummary.getEntity().getDateId();
        mCurrentDateId = dateId;
        mTxtDay.setText(DateUtils.getFormattedDayFromDateId(dateId));
        final int month = DateUtils.getMonthFromDateId(dateId) - 1;
        mTxtMonth.setText(mMonthsShortNameArray[month]);

        final int weekday = DateUtils.getWeekdayFromDateId(dateId) - 1;
        mTxtWeekday.setText(mWeekdaysNameArray[weekday].toUpperCase());
        final Compatibility compatibility = Compatibility.getInstance();
        if (DateUtils.isDateIdCurrentDate(dateId)) {
            compatibility.setBackground(mLayDate,
                    getResources().getDrawable(R.drawable.card_selector));
        } else {
            compatibility.setBackground(mLayDate,
                    getResources().getDrawable(R.drawable.card_old_style_selector));
        }

        if (DateUtils.isDateIdCurrentDate(mCurrentDateId)) {
            final Date currentDate = new Date();
            final int currentTime = DateUtils.dateToTimeAsInt(currentDate);
            final int preferredMeal = Meals.preferredMealForTimeInDate(getActivity().getApplicationContext(), currentTime,
                    currentDate, false);
            final float preferredUse = Meals.preferredUsageForMealInDate(getActivity().getApplicationContext(), preferredMeal, currentDate);
            final String mealName = getString(Meals.getMealResourceNameIdFromMealId(preferredMeal));
            final String preferredDescription = String.format(getString(R.string.quick_usage_description), mealName.toLowerCase());
            mTxtHeader.setText(preferredDescription);
            mQuickUsage = new FoodsUsageEntity(null, null, preferredMeal, null, preferredDescription, preferredUse);
            mTxtDetails.setText(String.format(getString(R.string.quick_usage_value), String.valueOf(preferredUse)));
            mTxtFooter.setText(getString(R.string.quick_usage_do_it_now));
        } else {
            mQuickUsage = null;
            mTxtHeader.setText(getString(R.string.quick_usage_not_current_date));
            mTxtDetails.setText("");
            mTxtFooter.setText("");
        }
    }

    @Override
    public void onDateSet(String tag, Date actualDate) {
        if (DATE_EDIT_TAG.equals(tag)) {
            ((JournalFragmentListener) getActivity()).onDateChanged(actualDate);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }
}