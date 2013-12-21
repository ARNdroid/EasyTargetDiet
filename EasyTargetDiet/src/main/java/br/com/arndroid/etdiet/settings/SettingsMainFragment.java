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
        public void onLiquidGoalSettingsSelected();
        public void onOilGoalSettingsSelected();
        public void onSupplementGoalSettingsSelected();
    }

    public static final String OWNER_TAG = SettingsMainFragment.class.getSimpleName();

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
        // TODO: may be necessary to implement for others settings...
    }

    private void attachScreen(View rootView) {
        // TODO: may be necessary to implement for others settings...
    }

    private void setupScreen(View rootView) {
        final RelativeLayout layExerciseGoal = (RelativeLayout) rootView.findViewById(R.id.layExerciseGoal);
        layExerciseGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingsMainFragmentListener)getActivity()).onExerciseGoalSettingsSelected();
            }
        });

        final RelativeLayout layLiquidGoal = (RelativeLayout) rootView.findViewById(R.id.layLiquidGoal);
        layLiquidGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingsMainFragmentListener)getActivity()).onLiquidGoalSettingsSelected();
            }
        });

        final RelativeLayout layOilGoal = (RelativeLayout) rootView.findViewById(R.id.layOilGoal);
        layOilGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingsMainFragmentListener) getActivity()).onOilGoalSettingsSelected();
            }
        });

        final RelativeLayout laySupplementGoal = (RelativeLayout) rootView.findViewById(R.id.laySupplementGoal);
        laySupplementGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SettingsMainFragmentListener) getActivity()).onSupplementGoalSettingsSelected();
            }
        });
    }

    @Override
    public void onPointSet(String tag, float actualValue) {
        // May be necessary for other settings implementations...
        refreshScreen();
    }
}
