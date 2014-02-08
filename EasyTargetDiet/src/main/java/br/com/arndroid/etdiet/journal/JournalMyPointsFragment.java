package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.QuickInsertAutoDialog;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.foodsusage.FoodsUsageEntity;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalMyPointsFragment extends Fragment implements
        DateDialog.OnDateSetListener,
        FragmentMenuReplier {

    public interface JournalFragmentListener {
        public void onDateChanged(Date newDate);
    }


    public static final String OWNER_TAG = JournalMyPointsFragment.class.getSimpleName();
    public static final String DATE_EDIT_TAG = OWNER_TAG + ".DATE_EDIT_TAG";

    private String mCurrentDateId;
    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysShortNameArray;

    private TextView mTxtDay;
    private TextView mTxtMonth;
    private TextView mTxtWeekday;
    private TextView mTxtPtsDay;
    private TextView mTxtPtsWeek;
    private TextView mTxtPtsExercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_my_points_fragment, container, false);

        mMonthsShortNameArray = getResources().getStringArray(R.array.months_short_name_list);
        mWeekdaysShortNameArray = getResources().getStringArray(R.array.weekdays_short_name_list);

        bindScreen(rootView);
        setupScreen(rootView);

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
        mTxtDay = (TextView) rootView.findViewById(R.id.txtDay);
        mTxtMonth = (TextView) rootView.findViewById(R.id.txtMonth);
        mTxtWeekday = (TextView) rootView.findViewById(R.id.txtWeekday);
        mTxtPtsDay = (TextView) rootView.findViewById(R.id.txtPtsDay);
        mTxtPtsWeek = (TextView) rootView.findViewById(R.id.txtPtsWeek);
        mTxtPtsExercise = (TextView) rootView.findViewById(R.id.txtPtsExercise);
    }

    private void setupScreen(View rootView) {
        final LinearLayout layDate = (LinearLayout) rootView.findViewById(R.id.layDate);
        layDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog dialog = new DateDialog();
                dialog.setTitle(getString(R.string.date));
                dialog.setInitialValue(DateUtils.dateIdToDate(mCurrentDateId));
                dialog.show(getFragmentManager(), DATE_EDIT_TAG);
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mCurrentDateId = daySummary.getEntity().getDateId();
        mTxtDay.setText(String.valueOf(DateUtils.getDayFromDateId(daySummary.getEntity().getDateId())));
        final int month = DateUtils.getMonthFromDateId(daySummary.getEntity().getDateId()) - 1;
        mTxtMonth.setText(mMonthsShortNameArray[month]);
        final int weekday = DateUtils.getWeekdayFromDateId(daySummary.getEntity().getDateId()) - 1;
        mTxtWeekday.setText(mWeekdaysShortNameArray[weekday]);

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
                final int timeHint = DateUtils.dateToTimeAsInt(new Date());
                final Date currentDate = DateUtils.dateIdToDate(mCurrentDateId);
                final int mealHint = Meals.preferredMealForTimeInDate(
                        getActivity().getApplicationContext(), timeHint, currentDate);
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