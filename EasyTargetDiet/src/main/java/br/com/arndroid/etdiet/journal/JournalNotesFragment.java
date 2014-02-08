package br.com.arndroid.etdiet.journal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.arndroid.etdiet.R;
import br.com.arndroid.etdiet.dialog.TextDialog;
import br.com.arndroid.etdiet.provider.days.DaysEntity;
import br.com.arndroid.etdiet.provider.days.DaysManager;
import br.com.arndroid.etdiet.utils.DateUtils;
import br.com.arndroid.etdiet.virtualweek.DaySummary;

public class JournalNotesFragment extends Fragment implements
        TextDialog.OnTextSetListener {

    public static final String OWNER_TAG = JournalNotesFragment.class.getSimpleName();
    public static final String NOTE_EDIT_TAG = OWNER_TAG + ".NOTE_EDIT_TAG";

    private String mCurrentDateId;

    private TextView mTxtNotes;
    private TextView mTxtEditNotes;

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
        mTxtEditNotes = (TextView) rootView.findViewById(R.id.txtEditNotes);
    }

    private void setupScreen() {
        mTxtEditNotes.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onTextSet(String tag, String actualText) {
        DaysManager manager = new DaysManager(getActivity().getApplicationContext());
        DaysEntity entity = manager.dayFromDate(DateUtils.dateIdToDate(mCurrentDateId));
        entity.setNote(actualText);
        manager.refresh(entity);
    }
}