package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityActionCaller;
import br.com.arndroid.etdiet.action.FragmentMenuReplier;
import br.com.arndroid.etdiet.dialog.DateDialog;
import br.com.arndroid.etdiet.dialog.IntegerDialog;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;

public class JournalFragment extends Fragment implements
        VirtualWeek.ViewObserver,
        DateDialog.OnDateSetListener,
        IntegerDialog.OnIntegerSetListener,
        TextDialog.OnTextSetListener,
        FragmentMenuReplier {

    public static final String OWNER_TAG = JournalFragment.class.getSimpleName();
    public static final String LIQUID_DONE_TAG = OWNER_TAG + ".LIQUID_DONE_TAG";
    public static final String OIL_DONE_TAG = OWNER_TAG + ".OIL_DONE_TAG";
    public static final String SUPPLEMENT_DONE_TAG = OWNER_TAG + ".SUPPLEMENT_DONE_TAG";
    public static final String NOTE_EDIT_TAG = OWNER_TAG + ".NOTE_EDIT_TAG";
    public static final String DATE_EDIT_TAG = OWNER_TAG + ".DATE_EDIT_TAG";

    private String mCurrentDateId;
    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysShortNameArray;
    private VirtualWeek mVirtualWeek;

    private TextView mTxtDay;
    private TextView mTxtMonth;
    private TextView mTxtWeekday;
    private TextView mTxtPtsDay;
    private TextView mTxtPtsWeek;
    private TextView mTxtPtsExercise;
    private TextView mTxtExerciseGoal;
    private TextView mTxtLiquidGoal;
    private TextView mTxtOilGoal;
    private TextView mTxtSupplementGoal;
    private TextView mTxtBreakfastPts;
    private TextView mTxtBreakfastTime;
    private TextView mTxtBreakfastIdeal;
    private TextView mTxtBrunchPts;
    private TextView mTxtBrunchTime;
    private TextView mTxtBrunchIdeal;
    private TextView mTxtLunchPts;
    private TextView mTxtLunchTime;
    private TextView mTxtLunchIdeal;
    private TextView mTxtSneakPts;
    private TextView mTxtSneakTime;
    private TextView mTxtSneakIdeal;
    private TextView mTxtDinnerPts;
    private TextView mTxtDinnerTime;
    private TextView mTxtDinnerIdeal;
    private TextView mTxtSupperPts;
    private TextView mTxtSupperTime;
    private TextView mTxtSupperIdeal;
    private TextView mTxtNotes;
    private TextView mTxtEditNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_fragment, container, false);

        if (savedInstanceState != null) {
            mCurrentDateId = savedInstanceState.getString(Contract.Days.DATE_ID);
        } else {
            mCurrentDateId = DateUtil.dateToDateId(new Date());
        }

        mMonthsShortNameArray = getResources().getStringArray(R.array.months_short_name_list);
        mWeekdaysShortNameArray = getResources().getStringArray(R.array.weekdays_short_name_list);

        bindScreen(rootView);
        setupScreen(rootView);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(Contract.Days.DATE_ID, mCurrentDateId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof ActivityActionCaller)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    ActivityActionCaller.class.getSimpleName());
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        mVirtualWeek = VirtualWeek.getInstance(getActivity().getApplicationContext());
        mVirtualWeek.registerViewObserver(this);
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onPause() {
        mVirtualWeek.unregisterViewObserver(this);
        mVirtualWeek = null;
        super.onPause();
    }

    private void bindScreen(View rootView) {
        mTxtDay = (TextView) rootView.findViewById(R.id.txtDay);
        mTxtMonth = (TextView) rootView.findViewById(R.id.txtMonth);
        mTxtWeekday = (TextView) rootView.findViewById(R.id.txtWeekday);
        mTxtPtsDay = (TextView) rootView.findViewById(R.id.txtPtsDay);
        mTxtPtsWeek = (TextView) rootView.findViewById(R.id.txtPtsWeek);
        mTxtPtsExercise = (TextView) rootView.findViewById(R.id.txtPtsExercise);
        mTxtExerciseGoal = (TextView) rootView.findViewById(R.id.txtExerciseGoal);
        mTxtLiquidGoal = (TextView) rootView.findViewById(R.id.txtLiquidGoal);
        mTxtOilGoal = (TextView) rootView.findViewById(R.id.txtOilGoal);
        mTxtSupplementGoal = (TextView) rootView.findViewById(R.id.txtSupplementGoal);
        mTxtBreakfastPts = (TextView) rootView.findViewById(R.id.txtBreakfastPts);
        mTxtBreakfastTime = (TextView) rootView.findViewById(R.id.txtBreakfastTime);
        mTxtBreakfastIdeal = (TextView) rootView.findViewById(R.id.txtBreakfastIdeal);
        mTxtBrunchPts = (TextView) rootView.findViewById(R.id.txtBrunchPts);
        mTxtBrunchTime = (TextView) rootView.findViewById(R.id.txtBrunchTime);
        mTxtBrunchIdeal = (TextView) rootView.findViewById(R.id.txtBrunchIdeal);
        mTxtLunchPts = (TextView) rootView.findViewById(R.id.txtLunchPts);
        mTxtLunchTime = (TextView) rootView.findViewById(R.id.txtLunchTime);
        mTxtLunchIdeal = (TextView) rootView.findViewById(R.id.txtLunchIdeal);
        mTxtSneakPts = (TextView) rootView.findViewById(R.id.txtSneakPts);
        mTxtSneakTime = (TextView) rootView.findViewById(R.id.txtSneakTime);
        mTxtSneakIdeal = (TextView) rootView.findViewById(R.id.txtSneakIdeal);
        mTxtDinnerPts = (TextView) rootView.findViewById(R.id.txtDinnerPts);
        mTxtDinnerTime = (TextView) rootView.findViewById(R.id.txtDinnerTime);
        mTxtDinnerIdeal = (TextView) rootView.findViewById(R.id.txtDinnerIdeal);
        mTxtSupperPts = (TextView) rootView.findViewById(R.id.txtSupperPts);
        mTxtSupperTime = (TextView) rootView.findViewById(R.id.txtSupperTime);
        mTxtSupperIdeal = (TextView) rootView.findViewById(R.id.txtSupperIdeal);
        mTxtNotes = (TextView) rootView.findViewById(R.id.txtNotes);
        mTxtEditNotes = (TextView) rootView.findViewById(R.id.txtEditNotes);
    }

    private void setupScreen(View rootView) {
        final LinearLayout layDate = (LinearLayout) rootView.findViewById(R.id.layDate);
        layDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateDialog dialog = new DateDialog();
                dialog.setTitle(getString(R.string.date));
                dialog.setInitialValue(DateUtil.dateIdToDate(mCurrentDateId));
                dialog.show(getFragmentManager(), DATE_EDIT_TAG);
            }
        });

        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(
                R.id.layExerciseGoal);
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                QuickInsertFrag dialog = new QuickInsertFrag();

                dialog.setDateId(mCurrentDateId);

                final int timeHint = DateUtil.dateToTimeAsInt(new Date());
                dialog.setMeal(Meals.EXERCISE);
                dialog.setTime(timeHint);

                dialog.setValue(3.0f);

                dialog.show(getFragmentManager(), QuickInsertFrag.INSERT_TAG);

            }
        });
        layExerciseGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layMealAction(layExerciseGoal);
                return true;
            }
        });

        final RelativeLayout layLiquidGoal = (RelativeLayout) rootView.findViewById(
                R.id.layLiquidGoal);
        layLiquidGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setLiquidDone(entity.getLiquidDone() + 1);
                manager.refresh(entity);

            }
        });
        layLiquidGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.liquid));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getLiquidDone());
                dialog.show(getFragmentManager(), LIQUID_DONE_TAG);
                return true;
            }
        });

        final RelativeLayout layOilGoal = (RelativeLayout) rootView.findViewById(R.id.layOilGoal);
        layOilGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setOilDone(entity.getOilDone() + 1);
                manager.refresh(entity);

            }
        });
        layOilGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.oil));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getOilDone());
                dialog.show(getFragmentManager(), OIL_DONE_TAG);
                return true;
            }
        });

        final RelativeLayout laySupplementGoal = (RelativeLayout) rootView.findViewById(
                R.id.laySupplementGoal);
        laySupplementGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setSupplementDone(entity.getSupplementDone() + 1);
                manager.refresh(entity);

            }
        });
        laySupplementGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                IntegerDialog dialog = new IntegerDialog();
                dialog.setTitle(getString(R.string.supplement));
                dialog.setMinValue(0);
                dialog.setMaxValue(99);
                dialog.setInitialValue(entity.getSupplementDone());
                dialog.show(getFragmentManager(), SUPPLEMENT_DONE_TAG);
                return true;
            }
        });

        final RelativeLayout layBreakfast = (RelativeLayout) rootView.findViewById(R.id.layBreakfast);
        layBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layBreakfast);
            }
        });

        final RelativeLayout layBrunch = (RelativeLayout) rootView.findViewById(R.id.layBrunch);
        layBrunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layBrunch);
            }
        });

        final RelativeLayout layLunch = (RelativeLayout) rootView.findViewById(R.id.layLunch);
        layLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layLunch);
            }
        });

        final RelativeLayout laySnack = (RelativeLayout) rootView.findViewById(R.id.laySnack);
        laySnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(laySnack);
            }
        });

        final RelativeLayout layDinner = (RelativeLayout) rootView.findViewById(R.id.layDinner);
        layDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(layDinner);
            }
        });

        final RelativeLayout laySupper = (RelativeLayout) rootView.findViewById(R.id.laySupper);
        laySupper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layMealAction(laySupper);
            }
        });

        mTxtEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                TextDialog dialog = new TextDialog();
                dialog.setTitle(getString(R.string.notes));
                dialog.setInitialText(entity.getNote());
                dialog.show(getFragmentManager(), NOTE_EDIT_TAG);
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mTxtDay.setText(String.valueOf(DateUtil.getDayFromDateId(daySummary.getEntity().getDateId())));
        final int month = DateUtil.getMonthFromDateId(daySummary.getEntity().getDateId()) - 1;
        mTxtMonth.setText(mMonthsShortNameArray[month]);
        final int weekday = DateUtil.getWeekdayFromDateId(daySummary.getEntity().getDateId()) - 1;
        mTxtWeekday.setText(mWeekdaysShortNameArray[weekday]);

        mTxtPtsDay.setText(String.valueOf(daySummary.getDiaryAllowanceAfterUsage()));
        mTxtPtsWeek.setText(String.valueOf(daySummary.getWeeklyAllowanceAfterUsage()));
        mTxtPtsExercise.setText(String.valueOf(daySummary.getExerciseAfterUsage()));

        mTxtExerciseGoal.setText(String.valueOf(daySummary.getTotalExercise()) + "/"
                + String.valueOf(daySummary.getEntity().getExerciseGoal()));
        mTxtLiquidGoal.setText(String.valueOf(daySummary.getEntity().getLiquidDone()) + "/"
                + String.valueOf(daySummary.getEntity().getLiquidGoal()));
        mTxtOilGoal.setText(String.valueOf(daySummary.getEntity().getOilDone()) + "/"
                + String.valueOf(daySummary.getEntity().getOilGoal()));
        mTxtSupplementGoal.setText(String.valueOf(daySummary.getEntity().getSupplementDone()) + "/"
                + String.valueOf(daySummary.getEntity().getSupplementGoal()));

        mTxtBreakfastPts.setText(String.valueOf(daySummary.getUsage().getBreakfastUsed()));
        mTxtBreakfastTime.setText(DateUtil.timeToFormattedString(
                daySummary.getEntity().getBreakfastStartTime()) + " - " +
                DateUtil.timeToFormattedString(daySummary.getEntity().getBreakfastEndTime()));
        final String ideal = getResources().getString(R.string.ideal_values) + " ";
        mTxtBreakfastIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBreakfastGoal()));
        mTxtBrunchPts.setText(String.valueOf(daySummary.getUsage().getBrunchUsed()));
        mTxtBrunchTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getBrunchStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getBrunchEndTime()));
        mTxtBrunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBrunchGoal()));
        mTxtLunchPts.setText(String.valueOf(daySummary.getUsage().getLunchUsed()));
        mTxtLunchTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getLunchStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getLunchEndTime()));
        mTxtLunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getLunchGoal()));
        mTxtSneakPts.setText(String.valueOf(daySummary.getUsage().getSneakUsed()));
        mTxtSneakTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getSnackStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getSnackEndTime()));
        mTxtSneakIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSnackGoal()));
        mTxtDinnerPts.setText(String.valueOf(daySummary.getUsage().getDinnerUsed()));
        mTxtDinnerTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getDinnerStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getDinnerEndTime()));
        mTxtDinnerIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getDinnerGoal()));
        mTxtSupperPts.setText(String.valueOf(daySummary.getUsage().getSupperUsed()));
        mTxtSupperTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getSupperStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getSupperEndTime()));
        mTxtSupperIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSupperGoal()));

        if (TextUtils.isEmpty(daySummary.getEntity().getNote())) {
            mTxtNotes.setText(R.string.note_empty);
            final Spanned result = Html.fromHtml(getResources().getString((
                    R.string.note_create)));
            mTxtEditNotes.setText(result);
        } else {
            mTxtNotes.setText(daySummary.getEntity().getNote());
            final Spanned result = Html.fromHtml(getResources().getString((
                    R.string.note_edit)));
            mTxtEditNotes.setText(result);
        }
    }

    public void layMealAction(View view) {
        int meal;
        switch (view.getId()) {
            case R.id.layBreakfast:
                meal = Meals.BREAKFAST;
                break;
            case R.id.layBrunch:
                meal = Meals.BRUNCH;
                break;
            case R.id.layLunch:
                meal = Meals.LUNCH;
                break;
            case R.id.laySnack:
                meal = Meals.SNACK;
                break;
            case R.id.layDinner:
                meal = Meals.DINNER;
                break;
            case R.id.laySupper:
                meal = Meals.SUPPER;
                break;
            case R.id.layExerciseGoal:
                meal = Meals.EXERCISE;
                break;
            default:
                throw new IllegalStateException("Invalid View.id " + view.getId());
        }

        final Bundle data = new Bundle();
        data.putString(FoodsUsageListFragment.DATE_ID_ACTION_KEY, mCurrentDateId);
        data.putInt(FoodsUsageListFragment.MEAL_ACTION_KEY, meal);

        ((ActivityActionCaller)getActivity()).onCallAction(R.id.foods_usage_list_fragment,
                FoodsUsageActivity.class, null, data);
    }

    @Override
    public void onDayChanged(DaySummary summary) {
        // TODO: Request summary if necessary.
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onFoodsUsageChanged(DaySummary summary) {
        // TODO: Request summary if necessary.
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onParametersChanged() {
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    public void onSummaryRequested(DaySummary daySummary) {
        refreshScreen(daySummary);
    }

    @Override
    public void onDateSet(String tag, Date actualDate) {
        if (DATE_EDIT_TAG.equals(tag)) {
            mCurrentDateId = DateUtil.dateToDateId(actualDate);
            mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
    }

    @Override
    public void onIntegerSet(String tag, int actualValue) {
        final DaysManager manager = new DaysManager(getActivity().getApplicationContext());
        final DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
        if (LIQUID_DONE_TAG.equals(tag)) {
            entity.setLiquidDone(actualValue);
        } else if (OIL_DONE_TAG.equals(tag)) {
            entity.setOilDone(actualValue);
        } else if (SUPPLEMENT_DONE_TAG.equals(tag)) {
            entity.setSupplementDone(actualValue);
        } else {
            throw new IllegalArgumentException("Invalid tag=" + tag);
        }
        manager.refresh(entity);
    }

    @Override
    public void onTextSet(String tag, String actualText) {
        DaysManager manager = new DaysManager(getActivity().getApplicationContext());
        DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
        entity.setNote(actualText);
        manager.refresh(entity);
    }

    @Override
    public void onReplyMenuFromHolderActivity(int menuItemId) {
        switch (menuItemId) {
            case R.id.quick_add:
                FragmentManager manager = getFragmentManager();
                QuickInsertFrag dialog = new QuickInsertFrag();

                dialog.setDateId(mCurrentDateId);

                final Date currentDate = DateUtil.dateIdToDate(mCurrentDateId);
                final int timeHint = DateUtil.dateToTimeAsInt(new Date());
                final int mealHint = Meals.preferredMealForTimeInDate(
                        getActivity().getApplicationContext(), timeHint, currentDate);
                dialog.setMeal(mealHint);
                dialog.setTime(timeHint);

                final float usageHint = Meals.preferredUsageForMealInDate(
                        getActivity().getApplicationContext(), mealHint, currentDate);
                dialog.setValue(usageHint);

                dialog.show(manager, QuickInsertFrag.INSERT_TAG);
                break;
            default:
                throw new IllegalArgumentException("Invalid menuItemId=" + menuItemId);
        }
    }
}