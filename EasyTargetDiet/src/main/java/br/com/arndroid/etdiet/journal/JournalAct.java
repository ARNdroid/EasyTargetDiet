package br.com.arndroid.etdiet.journal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Date;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageActivity;
import br.com.arndroid.etdiet.foodsusage.FoodsUsageListFragment;
import br.com.arndroid.etdiet.meals.Meals;
import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.quickinsert.QuickInsertFrag;
import br.com.arndroid.etdiet.settings.SettingsMainActivity;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.util.OldIntegerPickerDialog;
import br.com.arndroid.etdiet.util.OldTextDialog;
import br.com.arndroid.etdiet.virtualweek.DaySummary;
import br.com.arndroid.etdiet.virtualweek.VirtualWeek;

public class JournalAct extends ActionBarActivity implements VirtualWeek.ViewObserver,
    DatePickerDialog.OnDateSetListener {

    private VirtualWeek mVirtualWeek;
    private String mCurrentDateId;
    private String[] mMonthsShortNameArray;
    private String[] mWeekdaysShortNameArray;
    private OldIntegerPickerDialog.OnIntegerSetListener mLiquidSetListener;
    private OldIntegerPickerDialog.OnIntegerSetListener mOilSetListener;
    private OldIntegerPickerDialog.OnIntegerSetListener mSupplementListener;
    private OldTextDialog.OnTextSetListener mNoteSetListener;

    private Button btnDay;
    private TextView txtMonth;
    private TextView txtWeekday;
    private TextView txtPtsDay;
    private TextView txtPtsWeek;
    private TextView txtPtsExercise;
    private RelativeLayout layExerciseGoal;
    private TextView txtExerciseGoal;
    private RelativeLayout layLiquidGoal;
    private TextView txtLiquidGoal;
    private RelativeLayout layOilGoal;
    private TextView txtOilGoal;
    private RelativeLayout laySupplementGoal;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.journal_act);
        mMonthsShortNameArray = getResources().getStringArray(R.array.months_short_name_list);
        mWeekdaysShortNameArray = getResources().getStringArray(R.array.weekdays_short_name_list);

        if (savedInstanceState != null) {
            mCurrentDateId = savedInstanceState.getString(Contract.Days.DATE_ID);
        } else {
            mCurrentDateId = DateUtil.dateToDateId(new Date());
        }

        setFieldsReferenceFromForm();
        setUpFields();
    }

    private void setUpFields() {
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getSupportFragmentManager();
                QuickInsertFrag dialog = new QuickInsertFrag();

                dialog.setDateId(mCurrentDateId);

                final int timeHint = DateUtil.dateToTimeAsInt(new Date());
                dialog.setMeal(Meals.EXERCISE);
                dialog.setTime(timeHint);

                dialog.setValue(3.0f);

                dialog.show(manager, QuickInsertFrag.INSERT_TAG);

            }
        });
        layExerciseGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                layMealAction(layExerciseGoal);
                return true;
            }
        });

        layLiquidGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setLiquidDone(entity.getLiquidDone() + 1);
                manager.refresh(entity);

            }
        });
        mLiquidSetListener = new OldIntegerPickerDialog.OnIntegerSetListener() {
            @Override
            public void onNumberSet(NumberPicker view, int value) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setLiquidDone(value);
                manager.refresh(entity);
            }
        };
        layLiquidGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                OldIntegerPickerDialog dialog = new OldIntegerPickerDialog(JournalAct.this, mLiquidSetListener,
                        getString(R.string.liquid), 0, 99, entity.getLiquidDone());
                dialog.show();
                return true;
            }
        });

        layOilGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setOilDone(entity.getOilDone() + 1);
                manager.refresh(entity);

            }
        });
        mOilSetListener = new OldIntegerPickerDialog.OnIntegerSetListener() {
            @Override
            public void onNumberSet(NumberPicker view, int value) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setOilDone(value);
                manager.refresh(entity);
            }
        };
        layOilGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                OldIntegerPickerDialog dialog = new OldIntegerPickerDialog(JournalAct.this, mOilSetListener,
                        getString(R.string.oil), 0, 99, entity.getOilDone());
                dialog.show();
                return true;
            }
        });

        laySupplementGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setSupplementDone(entity.getSupplementDone() + 1);
                manager.refresh(entity);

            }
        });
        mSupplementListener = new OldIntegerPickerDialog.OnIntegerSetListener() {
            @Override
            public void onNumberSet(NumberPicker view, int value) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setSupplementDone(value);
                manager.refresh(entity);
            }
        };
        laySupplementGoal.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                OldIntegerPickerDialog dialog = new OldIntegerPickerDialog(JournalAct.this, mSupplementListener,
                        getString(R.string.supplement), 0, 99, entity.getSupplementDone());
                dialog.show();
                return true;
            }
        });

        mNoteSetListener = new OldTextDialog.OnTextSetListener() {
            @Override
            public void onTextSet(EditText view, String text) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                entity.setNote(text);
                manager.refresh(entity);
            }
        };
        txtEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(JournalAct.this.getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtil.dateIdToDate(mCurrentDateId));
                OldTextDialog dialog = new OldTextDialog(JournalAct.this, mNoteSetListener,
                        getString(R.string.notes), entity.getNote());
                dialog.show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Contract.Days.DATE_ID, mCurrentDateId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVirtualWeek = VirtualWeek.getInstance(this.getApplicationContext());
        mVirtualWeek.registerViewObserver(this);
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }

    @Override
    protected void onPause() {
        mVirtualWeek.unregisterViewObserver(this);
        mVirtualWeek = null;
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.journal, menu);
        return true;
    }

    private void setFieldsReferenceFromForm() {
        btnDay = (Button) findViewById(R.id.btnDay);
        txtMonth = (TextView) findViewById(R.id.txtMonth);
        txtWeekday = (TextView) findViewById(R.id.txtWeekday);
        txtPtsDay = (TextView) findViewById(R.id.txtPtsDay);
        txtPtsWeek = (TextView) findViewById(R.id.txtPtsWeek);
        txtPtsExercise = (TextView) findViewById(R.id.txtPtsExercise);
        layExerciseGoal = (RelativeLayout) findViewById(R.id.layExerciseGoal);
        txtExerciseGoal = (TextView) findViewById(R.id.txtExerciseGoal);
        layLiquidGoal = (RelativeLayout) findViewById(R.id.layLiquidGoal);
        txtLiquidGoal = (TextView) findViewById(R.id.txtLiquidGoal);
        layOilGoal = (RelativeLayout) findViewById(R.id.layOilGoal);
        txtOilGoal = (TextView) findViewById(R.id.txtOilGoal);
        laySupplementGoal = (RelativeLayout) findViewById(R.id.laySupplementGoal);
        txtSupplementGoal = (TextView) findViewById(R.id.txtSupplementGoal);
        txtBreakfastPts = (TextView) findViewById(R.id.txtBreakfastPts);
        txtBreakfastTime = (TextView) findViewById(R.id.txtBreakfastTime);
        txtBreakfastIdeal = (TextView) findViewById(R.id.txtBreakfastIdeal);
        txtBrunchPts = (TextView) findViewById(R.id.txtBrunchPts);
        txtBrunchTime = (TextView) findViewById(R.id.txtBrunchTime);
        txtBrunchIdeal = (TextView) findViewById(R.id.txtBrunchIdeal);
        txtLunchPts = (TextView) findViewById(R.id.txtLunchPts);
        txtLunchTime = (TextView) findViewById(R.id.txtLunchTime);
        txtLunchIdeal = (TextView) findViewById(R.id.txtLunchIdeal);
        txtSneakPts = (TextView) findViewById(R.id.txtSneakPts);
        txtSneakTime = (TextView) findViewById(R.id.txtSneakTime);
        txtSneakIdeal = (TextView) findViewById(R.id.txtSneakIdeal);
        txtDinnerPts = (TextView) findViewById(R.id.txtDinnerPts);
        txtDinnerTime = (TextView) findViewById(R.id.txtDinnerTime);
        txtDinnerIdeal = (TextView) findViewById(R.id.txtDinnerIdeal);
        txtSupperPts = (TextView) findViewById(R.id.txtSupperPts);
        txtSupperTime = (TextView) findViewById(R.id.txtSupperTime);
        txtSupperIdeal = (TextView) findViewById(R.id.txtSupperIdeal);
        txtNotes = (TextView) findViewById(R.id.txtNotes);
        txtEditNotes = (TextView) findViewById(R.id.txtEditNotes);
    }

    private void updateFields(DaySummary daySummary) {
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

    public void btnDayAction(View view) {
        DatePickerDialog dialog = new DatePickerDialog(this,
                this, DateUtil.getYearFromDateId(mCurrentDateId),
                DateUtil.getMonthFromDateId(mCurrentDateId) - 1,
                DateUtil.getDayFromDateId(mCurrentDateId));
        dialog.show();

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

        FoodsUsageListFragment foodsUsageListFragment = (FoodsUsageListFragment)
                getSupportFragmentManager().findFragmentById(R.id.foods_usage_list_frag);

        if (foodsUsageListFragment != null) {
            foodsUsageListFragment.refreshScreen(mCurrentDateId, meal);
        } else {
            Intent intent = new Intent(this, FoodsUsageActivity.class);
            intent.putExtra(FoodsUsageActivity.DATE_ID_PARAMETER, mCurrentDateId);
            intent.putExtra(FoodsUsageActivity.MEAL_PARAMETER, meal);
            startActivity(intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.quick_add:
                FragmentManager manager = getSupportFragmentManager();
                QuickInsertFrag dialog = new QuickInsertFrag();

                dialog.setDateId(mCurrentDateId);

                final Date currentDate = DateUtil.dateIdToDate(mCurrentDateId);
                final int timeHint = DateUtil.dateToTimeAsInt(new Date());
                final int mealHint = Meals.preferredMealForTimeInDate(this.getApplicationContext(),
                        timeHint, currentDate);
                dialog.setMeal(mealHint);
                dialog.setTime(timeHint);

                final float usageHint = Meals.preferredUsageForMealInDate(this.getApplicationContext(),
                        mealHint, currentDate);
                dialog.setValue(usageHint);

                dialog.show(manager, QuickInsertFrag.INSERT_TAG);

                return true;
            case R.id.settings:
                Intent intent = new Intent(this, SettingsMainActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        updateFields(daySummary);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCurrentDateId = DateUtil.datePickerToDateId(view);
        mVirtualWeek.requestSummaryForDateId(this, mCurrentDateId);
    }
}
