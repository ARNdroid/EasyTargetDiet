package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

// JELLY_BEAN = API 16
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class CompatibilityAPI_16 extends CompatibilityAPI_15 {

    // Package only:
    CompatibilityAPI_16() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.JELLY_BEAN;
    }

    @Override
    public void setBackground(View view, Drawable drawable) {
        view.setBackground(drawable);
    }
}
