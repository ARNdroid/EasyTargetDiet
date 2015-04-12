package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

// HONEYCOMB = API 11
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CompatibilityAPI_11 extends Compatibility {

    // Package only:
    CompatibilityAPI_11() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.HONEYCOMB;
    }


    @Override
    @SuppressWarnings("deprecation")
    public void setBackground(View view, Drawable drawable) {
        view.setBackgroundDrawable(drawable);
    }
}
