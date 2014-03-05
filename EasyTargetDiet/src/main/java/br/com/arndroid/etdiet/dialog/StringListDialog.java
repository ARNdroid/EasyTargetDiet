package br.com.arndroid.etdiet.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;

public class StringListDialog extends DialogFragment {
    /**
     * The callback used to indicate the user is done selecting a item in list.
     */
    public interface OnStringSelectedListener {
        void onStringSelected(String tag, int chosenIndex);
    }

    private static final String TITLE_KEY = "TITLE_KEY";
    private static final String INITIAL_INDEX_KEY = "INITIAL_INDEX_KEY";
    private static final String ACTUAL_INDEX_KEY = "ACTUAL_INDEX_KEY";
    private static final String STRING_LIST_ID_KEY = "STRING_LIST_ID_KEY";

    private String mTitle;
    private int mInitialIndex;
    private int mActualIndex;
    private int mStringListId;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        mActualIndex = getInitialIndex();
        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString(TITLE_KEY));
            setInitialIndex(savedInstanceState.getInt(INITIAL_INDEX_KEY));
            setStringListId(savedInstanceState.getInt(STRING_LIST_ID_KEY));
            mActualIndex = savedInstanceState.getInt(ACTUAL_INDEX_KEY);
        }

        builder.setSingleChoiceItems(getStringListId(), getActualIndex(), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                mActualIndex = index;
            }
        });

        builder.setTitle(getTitle());
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                ((OnStringSelectedListener)getActivity()).onStringSelected(
                        StringListDialog.this.getTag(), getActualIndex());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(@SuppressWarnings("NullableProblems") Bundle outState) {
        outState.putString(TITLE_KEY, getTitle());
        outState.putInt(INITIAL_INDEX_KEY, getInitialIndex());
        outState.putInt(ACTUAL_INDEX_KEY, getActualIndex());
        outState.putInt(STRING_LIST_ID_KEY, mStringListId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        /* We didn't use a callback like:
             OnPointListener mListener
           because we couldn't save the listener with Bundle.setXxx to restore the dialog after,
           for example, a screen rotation.
           Due to it, the attached activity must implement the interface.
         */
        super.onAttach(activity);
        if (!(activity instanceof OnStringSelectedListener)) {
            throw new ClassCastException(activity.toString() +
                    " must implement StringListDialog.OnStringSelectedListener");
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public int getInitialIndex() {
        return mInitialIndex;
    }

    public void setInitialIndex(int currentValue) {
        this.mInitialIndex = currentValue;
    }

    public int getActualIndex() {
        return mActualIndex;
    }

    public int getStringListId() {
        return mStringListId;
    }

    public void setStringListId(int stringListId) {
        mStringListId = stringListId;
    }
}
