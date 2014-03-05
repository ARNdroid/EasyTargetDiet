package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// HONEYCOMB = API 11
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CompatibilityAPI_11 extends CompatibilityAPI_10 {

    // Package only:
    CompatibilityAPI_11() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.HONEYCOMB;
    }
}
