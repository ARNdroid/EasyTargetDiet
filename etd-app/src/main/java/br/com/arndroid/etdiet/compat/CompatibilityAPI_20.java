package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// KITKAT_WATCH = API 20
@TargetApi(Build.VERSION_CODES.KITKAT_WATCH)
public class CompatibilityAPI_20 extends CompatibilityAPI_19 {
    // Package only:
    CompatibilityAPI_20() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.KITKAT_WATCH;
    }
}
