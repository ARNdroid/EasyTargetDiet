package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// KITKAT = API 19
@TargetApi(Build.VERSION_CODES.KITKAT)
public class CompatibilityAPI_19 extends CompatibilityAPI_18 {

    // Package only:
    CompatibilityAPI_19() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.KITKAT;
    }
}
