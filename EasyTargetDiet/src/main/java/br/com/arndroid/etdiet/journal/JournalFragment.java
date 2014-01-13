package br.com.arndroid.etdiet.journal;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.action.ActivityCaller;
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

public class JournalFragment extends Fragment implements VirtualWeek.ViewObserver {

    public static final String OWNER_TAG = JournalFragment.class.getSimpleName();
    public static final String LIQUID_USED_TAG = OWNER_TAG + ".LIQUID_USED_TAG";
    public static final String OIL_USED_TAG = OWNER_TAG + ".OIL_USED_TAG";
    public static final String SUPPLEMENT_USED_TAG = OWNER_TAG + ".SUPPLEMENT_USED_TAG";
    public static final String NOTE_EDIT_TAG = OWNER_TAG + ".NOTE_EDIT_TAG";

    private String mCurrentDateId;
    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysShortNameArray;
    private VirtualWeek mVirtualWeek;

    // TODO: change all variable names to mXxxx
    private Button btnDay;
    private TextView txtMonth;
    private TextView txtWeekday;
    private TextView txtPtsDay;
    private TextView txtPtsWeek;
    private TextView txtPtsExercise;
    private TextView txtExerciseGoal;
    private TextView txtLiquidGoal;
    private TextView txtOilGoal;
    private TextView txtSupplementGoal;
    private TextView txtBreakfastPts;
    private TextView txtBreakfastTime;
    private TextView txtBreakfastIdeal;
    private TextView txtBrunchPts;
    private TextView txtBrunchTime;
    private TextView txtBrunchIdeal;
    private TextView txtLunchPts;
    private TextView txtLunchTime;
    private TextView txtLunchIdeal;
    private TextView txtSneakPts;
    private TextView txtSneakTime;
    private TextView txtSneakIdeal;
    private TextView txtDinnerPts;
    private TextView txtDinnerTime;
    private TextView txtDinnerIdeal;
    private TextView txtSupperPts;
    private TextView txtSupperTime;
    private TextView txtSupperIdeal;
    private TextView txtNotes;
    private TextView txtEditNotes;

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

        if (!(activity instanceof ActivityCaller)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                    ActivityCaller.class.getSimpleName());
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
        btnDay = (Button) rootView.findViewById(R.id.btnDay);
        txtMonth = (TextView) rootView.findViewById(R.id.txtMonth);
        txtWeekday = (TextView) rootView.findViewById(R.id.txtWeekday);
        txtPtsDay = (TextView) rootView.findViewById(R.id.txtPtsDay);
        txtPtsWeek = (TextView) rootView.findViewById(R.id.txtPtsWeek);
        txtPtsExercise = (TextView) rootView.findViewById(R.id.txtPtsExercise);
        txtExerciseGoal = (TextView) rootView.findViewById(R.id.txtExerciseGoal);
        txtLiquidGoal = (TextView) rootView.findViewById(R.id.txtLiquidGoal);
        txtOilGoal = (TextView) rootView.findViewById(R.id.txtOilGoal);
        txtSupplementGoal = (TextView) rootView.findViewById(R.id.txtSupplementGoal);
        txtBreakfastPts = (TextView) rootView.findViewById(R.id.txtBreakfastPts);
        txtBreakfastTime = (TextView) rootView.findViewById(R.id.txtBreakfastTime);
        txtBreakfastIdeal = (TextView) rootView.findViewById(R.id.txtBreakfastIdeal);
        txtBrunchPts = (TextView) rootView.findViewById(R.id.txtBrunchPts);
        txtBrunchTime = (TextView) rootView.findViewById(R.id.txtBrunchTime);
        txtBrunchIdeal = (TextView) rootView.findViewById(R.id.txtBrunchIdeal);
        txtLunchPts = (TextView) rootView.findViewById(R.id.txtLunchPts);
        txtLunchTime = (TextView) rootView.findViewById(R.id.txtLunchTime);
        txtLunchIdeal = (TextView) rootView.findViewById(R.id.txtLunchIdeal);
        txtSneakPts = (TextView) rootView.findViewById(R.id.txtSneakPts);
        txtSneakTime = (TextView) rootView.findViewById(R.id.txtSneakTime);
        txtSneakIdeal = (TextView) rootView.findViewById(R.id.txtSneakIdeal);
        txtDinnerPts = (TextView) rootView.findViewById(R.id.txtDinnerPts);
        txtDinnerTime = (TextView) rootView.findViewById(R.id.txtDinnerTime);
        txtDinnerIdeal = (TextView) rootView.findViewById(R.id.txtDinnerIdeal);
        txtSupperPts = (TextView) rootView.findViewById(R.id.txtSupperPts);
        txtSupperTime = (TextView) rootView.findViewById(R.id.txtSupperTime);
        txtSupperIdeal = (TextView) rootView.findViewById(R.id.txtSupperIdeal);
        txtNotes = (TextView) rootView.findViewById(R.id.txtNotes);
        txtEditNotes = (TextView) rootView.findViewById(R.id.txtEditNotes);
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(R.id.layExerciseGoal);
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

        final RelativeLayout layLiquidGoal = (RelativeLayout) rootView.findViewById(R.id.layLiquidGoal);
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
                dialog.show(getFragmentManager(), LIQUID_USED_TAG);
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
                dialog.show(getFragmentManager(), OIL_USED_TAG);
                return true;
            }
        });

        final RelativeLayout laySupplementGoal = (RelativeLayout) rootView.findViewById(R.id.laySupplementGoal);
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
                dialog.show(getFragmentManager(), SUPPLEMENT_USED_TAG);
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

        txtEditNotes.setOnClickListener(new View.OnClickListener() {
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
        btnDay.setText(String.valueOf(DateUtil.getDayFromDateId(daySummary.getEntity().getDateId())));
        final int month = DateUtil.getMonthFromDateId(daySummary.getEntity().getDateId()) - 1;
        txtMonth.setText(mMonthsShortNameArray[month]);
        final int weekday = DateUtil.getWeekdayFromDateId(daySummary.getEntity().getDateId()) - 1;
        txtWeekday.setText(mWeekdaysShortNameArray[weekday]);

        txtPtsDay.setText(String.valueOf(daySummary.getDiaryAllowanceAfterUsage()));
        txtPtsWeek.setText(String.valueOf(daySummary.getWeeklyAllowanceAfterUsage()));
        txtPtsExercise.setText(String.valueOf(daySummary.getExerciseAfterUsage()));

        txtExerciseGoal.setText(String.valueOf(daySummary.getTotalExercise()) + "/"
                + String.valueOf(daySummary.getEntity().getExerciseGoal()));
        txtLiquidGoal.setText(String.valueOf(daySummary.getEntity().getLiquidDone()) + "/"
                + String.valueOf(daySummary.getEntity().getLiquidGoal()));
        txtOilGoal.setText(String.valueOf(daySummary.getEntity().getOilDone()) + "/"
                + String.valueOf(daySummary.getEntity().getOilGoal()));
        txtSupplementGoal.setText(String.valueOf(daySummary.getEntity().getSupplementDone()) + "/"
                + String.valueOf(daySummary.getEntity().getSupplementGoal()));

        txtBreakfastPts.setText(String.valueOf(daySummary.getUsage().getBreakfastUsed()));
        txtBreakfastTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getBreakfastStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getBreakfastEndTime()));
        final String ideal = getResources().getString(R.string.ideal_values) + " ";
        txtBreakfastIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBreakfastGoal()));
        txtBrunchPts.setText(String.valueOf(daySummary.getUsage().getBrunchUsed()));
        txtBrunchTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getBrunchStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getBrunchEndTime()));
        txtBrunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getBrunchGoal()));
        txtLunchPts.setText(String.valueOf(daySummary.getUsage().getLunchUsed()));
        txtLunchTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getLunchStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getLunchEndTime()));
        txtLunchIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getLunchGoal()));
        txtSneakPts.setText(String.valueOf(daySummary.getUsage().getSneakUsed()));
        txtSneakTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getSnackStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getSnackEndTime()));
        txtSneakIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSnackGoal()));
        txtDinnerPts.setText(String.valueOf(daySummary.getUsage().getDinnerUsed()));
        txtDinnerTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getDinnerStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getDinnerEndTime()));
        txtDinnerIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getDinnerGoal()));
        txtSupperPts.setText(String.valueOf(daySummary.getUsage().getSupperUsed()));
        txtSupperTime.setText(DateUtil.timeToFormattedString(daySummary.getEntity().getSupperStartTime())
                + " - " + DateUtil.timeToFormattedString(daySummary.getEntity().getSupperEndTime()));
        txtSupperIdeal.setText(ideal + String.valueOf(daySummary.getEntity().getSupperGoal()));

        if (TextUtils.isEmpty(daySummary.getEntity().getNote())) {
            txtNotes.setText(R.string.note_empty);
            final Spanned result = Html.fromHtml(getResources().getString((R.string.note_create)));
            txtEditNotes.setText(result);
        } else {
            txtNotes.setText(daySummary.getEntity().getNote());
            final Spanned result = Html.fromHtml(getResources().getString((R.string.note_edit)));
            txtEditNotes.setText(result);
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

        ((ActivityCaller)getActivity()).onCallAction(R.id.foods_usage_list_frag,
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
}
