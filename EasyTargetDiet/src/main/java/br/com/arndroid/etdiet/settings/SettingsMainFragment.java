package br.com.arndroid.etdiet.settings;

import android.app.Activity;
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

    public interface SettingsMainFragmentListener {
        public void onExerciseGoalSettingsSelected();
    }
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (!(activity instanceof SettingsMainFragmentListener)) {
            throw new ClassCastException(activity.toString() + " must implement " +
                SettingsMainFragmentListener.class.getSimpleName());
        }
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
                ((SettingsMainFragmentListener)getActivity()).onExerciseGoalSettingsSelected();
            }
        });
    }

    @Override
    public void onPointSet(String tag, float actualValue) {
        // TODO: Implement.
        refreshScreen();
    }
}
