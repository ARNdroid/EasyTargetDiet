package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// HONEYCOMB_MR2 = API 13
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
public class CompatibilityAPI_13 extends CompatibilityAPI_12 {

    // Package only:
    CompatibilityAPI_13() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.HONEYCOMB_MR2;
    }
}
