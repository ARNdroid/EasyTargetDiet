package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// HONEYCOMB_MR1 = API 12
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class CompatibilityAPI_12 extends CompatibilityAPI_11 {

    // Package only:
    CompatibilityAPI_12() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.HONEYCOMB_MR1;
    }
}
