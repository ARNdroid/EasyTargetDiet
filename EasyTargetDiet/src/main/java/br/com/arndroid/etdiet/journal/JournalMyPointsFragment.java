package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.compat.Compatibility;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.QuickInsertAutoDialog;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyPointsFragment extends Fragment implements
        DateDialog.OnDateSetListener,
        FragmentMenuReplier {

    private static final String MONTH_AND_YEAR_FORMAT = "%s %s";

    public interface JournalFragmentListener {
        public void onDateChanged(Date newDate);
    }


    public static final String OWNER_TAG = JournalMyPointsFragment.class.getSimpleName();
    public static final String DATE_EDIT_TAG = OWNER_TAG + ".DATE_EDIT_TAG";

    private String mCurrentDateId;
    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysNameArray;

    private LinearLayout mLayDate;
    private TextView mTxtDay;
    private TextView mTxtMonthAndYear;
    private TextView mTxtWeekday;
    private TextView mTxtToday;
    private TextView mTxtPtsDay;
    private TextView mTxtPtsWeek;
    private TextView mTxtPtsExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_my_points_fragment, container, false);

        mMonthsShortNameArray = getResources().getStringArray(R.array.months_short_name_list);
        mWeekdaysNameArray = getResources().getStringArray(R.array.weekdays_name_list);

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
        mTxtDay = (TextView) rootView.findViewById(R.id.txtDay);
        mTxtMonthAndYear = (TextView) rootView.findViewById(R.id.txtMonthAndYear);
        mTxtWeekday = (TextView) rootView.findViewById(R.id.txtWeekday);
        mTxtToday = (TextView) rootView.findViewById(R.id.txtToday);
        mTxtPtsDay = (TextView) rootView.findViewById(R.id.txtPtsDay);
        mTxtPtsWeek = (TextView) rootView.findViewById(R.id.txtPtsWeek);
        mTxtPtsExercise = (TextView) rootView.findViewById(R.id.txtPtsExercise);
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
    }

    public void refreshScreen(DaySummary daySummary) {
        final String dateId = daySummary.getEntity().getDateId();
        mCurrentDateId = dateId;
        mTxtDay.setText(DateUtils.getFormattedDayFromDateId(dateId));
        final int month = DateUtils.getMonthFromDateId(dateId) - 1;
        mTxtMonthAndYear.setText(String.format(MONTH_AND_YEAR_FORMAT, mMonthsShortNameArray[month],
                DateUtils.getFormattedYearFromDateId(dateId)));
        final int weekday = DateUtils.getWeekdayFromDateId(dateId) - 1;
        mTxtWeekday.setText(mWeekdaysNameArray[weekday].toUpperCase());
        final int textColor;
        final Compatibility compatibility = Compatibility.getInstance();
        if (DateUtils.isDateIdCurrentDate(dateId)) {
            textColor = getResources().getColor(R.color.accent_dark);
            compatibility.setBackground(mLayDate,
                    getResources().getDrawable(R.drawable.card_selector));
            mTxtToday.setText(getString(R.string.today));
        } else {
            textColor = getResources().getColor(R.color.card_old_style_foreground);
            compatibility.setBackground(mLayDate,
                    getResources().getDrawable(R.drawable.card_old_style_selector));
            mTxtToday.setText(getString(R.string.not_today));
        }
        mTxtWeekday.setTextColor(textColor);
        mTxtDay.setTextColor(textColor);
        mTxtMonthAndYear.setTextColor(textColor);
        mTxtToday.setTextColor(textColor);

        mTxtPtsDay.setText(String.valueOf(daySummary.getDiaryAllowanceAfterUsage()));
        mTxtPtsWeek.setText(String.valueOf(daySummary.getWeeklyAllowanceAfterUsage()));
        mTxtPtsExercise.setText(String.valueOf(daySummary.getExerciseAfterUsage()));
    }

    @Override
    public void onDateSet(String tag, Date actualDate) {
        if (DATE_EDIT_TAG.equals(tag)) {
            ((JournalFragmentListener) getActivity()).onDateChanged(actualDate);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {

        switch (menuItemId) {
            case R.id.quick_add:

                final Date now = new Date();
                final boolean isCurrentDateToday = DateUtils.dateToDateId(now).equals(mCurrentDateId);

                final int timeHint, mealHint;
                final Date currentDate;
                if (isCurrentDateToday) {
                    currentDate = now;
                    timeHint = DateUtils.dateToTimeAsInt(currentDate);
                    mealHint = Meals.preferredMealForTimeInDate(getActivity().getApplicationContext(),
                            timeHint, currentDate, false);
                } else {
                    final Calendar calendar = Calendar.getInstance();
                    calendar.setTime(DateUtils.dateIdToDate(mCurrentDateId));
                    currentDate = calendar.getTime();
                    mealHint = Meals.preferredMealForTimeInDate(getActivity().getApplicationContext(),
                            -1, currentDate, true);
                    final WeekdayParametersEntity entity = new WeekdayParametersManager(getActivity().getApplicationContext()).weekdayParametersFromWeekday(calendar.get(Calendar.DAY_OF_WEEK));
                    switch (mealHint) {
                        case Meals.BREAKFAST:
                            timeHint = entity.getBreakfastStartTime();
                            break;
                        case Meals.BRUNCH:
                            timeHint = entity.getBrunchStartTime();
                            break;
                        case Meals.LUNCH:
                            timeHint = entity.getLunchStartTime();
                            break;
                        case Meals.SNACK:
                            timeHint = entity.getSnackStartTime();
                            break;
                        case Meals.DINNER:
                            timeHint = entity.getDinnerStartTime();
                            break;
                        case Meals.SUPPER:
                            timeHint = entity.getSupperStartTime();
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid meal=" + mealHint + ".");
                    }
                }

                final float usageHint = Meals.preferredUsageForMealInDate(
                        getActivity().getApplicationContext(), mealHint, currentDate);
                final FoodsUsageEntity entity = new FoodsUsageEntity(null, mCurrentDateId,
                        mealHint, timeHint, null, usageHint);
                final QuickInsertAutoDialog dialog = new QuickInsertAutoDialog();
                dialog.setFoodsUsageEntity(entity);
                dialog.show(getFragmentManager(), null);
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }
}