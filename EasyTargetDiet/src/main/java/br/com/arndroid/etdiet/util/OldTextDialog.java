package br.com.arndroid.etdiet.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import br.com.arndroid.etdiet.R;

/*
 Build by example of Android DatePickerDialog class
 */
public class OldTextDialog extends AlertDialog implements DialogInterface.OnClickListener {

    private static final String CURRENT_VALUE_KEY = "CURRENT_VALUE_KEY";
    private EditText mEdtText = null;
    private final OnTextSetListener mCallBack;

    /**
     * The callback used to indicate the user is done filling in the text.
     */
    public interface OnTextSetListener {
        void onTextSet(EditText view, String text);
    }

    public OldTextDialog(Context context,
                         OnTextSetListener callBack,
                         String title,
                         String text) {
        this(context, 0, callBack, title, text);
    }

    public OldTextDialog(Context context,
                         int theme,
                         OnTextSetListener callBack,
                         String title,
                         String text) {
        super(context, theme);

        mCallBack = callBack;

        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, themeContext.getText(R.string.done), this);
        setIcon(0);

        LayoutInflater inflater =
                (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.text_dialog, null);
        setView(view);
        setTitle(title);

        attachScreen(view);
        refreshScreen(text);
    }

    private void refreshScreen(String text) {
        mEdtText.setText(text);
    }

    private void attachScreen(View rootView) {
        mEdtText = (EditText) rootView.findViewById(R.id.edtText);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        tryNotifyDateSet();
    }

    private void tryNotifyDateSet() {
        if (mCallBack != null) {
            mEdtText.clearFocus();
            mCallBack.onTextSet(mEdtText, mEdtText.getText().toString());
        }
    }

    @Override
    protected void onStop() {
        tryNotifyDateSet();
        super.onStop();
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putString(CURRENT_VALUE_KEY, mEdtText.getText().toString());
        return state;
    }

    @Override
    public void onRestoreInstanceState(@SuppressWarnings("NullableProblems") Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mEdtText.setText(savedInstanceState.getString(CURRENT_VALUE_KEY));
    }
}
