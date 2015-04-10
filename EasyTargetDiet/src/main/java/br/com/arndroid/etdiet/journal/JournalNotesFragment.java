package br.com.arndroid.etdiet.journal;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.utils.StringUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalNotesFragment extends Fragment implements
        TextDialog.OnTextSetListener {

    public static final String OWNER_TAG = JournalNotesFragment.class.getSimpleName();
    public static final String NOTE_EDIT_TAG = OWNER_TAG + ".NOTE_EDIT_TAG";

    private String mCurrentDateId;

    private TextView mTxtNotes;
    private Button mBtnEditNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.journal_notes_fragment, container, false);

        bindScreen(rootView);
        setupScreen();

        return rootView;
    }

    private void bindScreen(View rootView) {
        mTxtNotes = (TextView) rootView.findViewById(R.id.txtNotes);
        mBtnEditNotes = (Button) rootView.findViewById(R.id.btnEditNotes);
    }

    private void setupScreen() {
        mBtnEditNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DaysManager manager = new DaysManager(getActivity().getApplicationContext());
                DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
                TextDialog dialog = new TextDialog();
                dialog.setTitle(getString(R.string.notes));
                dialog.setInitialText(entity.getNote());
                dialog.show(getFragmentManager(), NOTE_EDIT_TAG);
            }
        });
    }

    public void refreshScreen(DaySummary daySummary) {
        mCurrentDateId = daySummary.getEntity().getDateId();

        if (StringUtils.isEmptyOrOnlySpaces(daySummary.getEntity().getNote())) {
            mTxtNotes.setText(R.string.note_empty);
            mBtnEditNotes.setText(getString(R.string.note_add));
        } else {
            mTxtNotes.setText(daySummary.getEntity().getNote());
            mBtnEditNotes.setText(getString(R.string.note_edit));
        }
    }

    @Override
    public void onTextSet(String tag, String actualText) {
        DaysManager manager = new DaysManager(getActivity().getApplicationContext());
        DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
        entity.setNote(actualText);
        manager.refresh(entity);
    }
}