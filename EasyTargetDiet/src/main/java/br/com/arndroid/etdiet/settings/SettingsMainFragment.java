package br.com.arndroid.etdiet.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersEntity;
import br.com.arndroid.etdiet.provider.weekdayparameters.WeekdayParametersManager;
import br.com.arndroid.etdiet.util.PointPickerDialog;

public class SettingsMainFragment extends Fragment implements PointPickerDialog.OnPointSetListener {

    private TextView mTxtExerciseGoalActualValue;

    public static final String OWNER_TAG = SettingsMainFragment.class.getSimpleName();
    public static final String EXERCISE_GOAL_TAG = OWNER_TAG + ".EXERCISE_GOAL";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_main_fragment, container, false);

        attachScreen(rootView);
        setupScreen(rootView);
        refreshScreen();

        return rootView;
    }

    private void refreshScreen() {
        final WeekdayParametersManager weekdayParametersManager =
                new WeekdayParametersManager(getActivity().getApplicationContext());
        final WeekdayParametersEntity weekdayParametersEntity =
                weekdayParametersManager.weekdayParametersFromWeekday(Calendar.SUNDAY);
        final String units_actual_value_string = getResources().getString(R.string.units_actual_value);

        mTxtExerciseGoalActualValue.setText(String.format(
                units_actual_value_string, weekdayParametersEntity.getExerciseGoal()) +
                "\n(for all weekdays)");
    }

    private void attachScreen(View rootView) {
        mTxtExerciseGoalActualValue = (TextView) rootView.findViewById(R.id.txtExerciseGoalActualValue);
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(R.id.layExerciseGoal);
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: change to show the list view (where a parameter will be edited)
                final WeekdayParametersEntity entity = new WeekdayParametersManager(
                        getActivity().getApplicationContext()).weekdayParametersFromWeekday(Calendar.SUNDAY);

                final PointPickerDialog dialog = new PointPickerDialog();
                dialog.setTitle(getResources().getString(R.string.exercise));
                dialog.setMinIntegerValue(0);
                dialog.setMaxIntegerValue(99);
                dialog.setInitialValue(entity.getExerciseGoal());
                dialog.show(SettingsMainFragment.this.getFragmentManager(), EXERCISE_GOAL_TAG);
            }
        });
    }

    @Override
    public void onPointSet(String tag, float actualValue) {
        final WeekdayParametersManager manager = new WeekdayParametersManager(
                SettingsMainFragment.this.getActivity().getApplicationContext());
        for (int i = Calendar.SUNDAY; i <= Calendar.SATURDAY; i++) {
            final WeekdayParametersEntity entity = manager.weekdayParametersFromWeekday(i);
            entity.setExerciseGoal(actualValue);
            manager.refresh(entity);
        }
        refreshScreen();
    }
}
