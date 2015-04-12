package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// LOLLIPOP = API 21
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class CompatibilityAPI_21 extends CompatibilityAPI_20 {
    // Package only:
    CompatibilityAPI_21() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.LOLLIPOP;
    }
}
