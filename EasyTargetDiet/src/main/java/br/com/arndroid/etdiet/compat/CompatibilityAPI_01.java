package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

// BASE = API 1
@TargetApi(Build.VERSION_CODES.BASE)
public class CompatibilityAPI_01 extends Compatibility {

    // Package only:
    CompatibilityAPI_01() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.BASE;
    }

    @Override
    public void setBackground(View view, Drawable drawable) {
        //noinspection deprecation
        view.setBackgroundDrawable(drawable);
    }
}
