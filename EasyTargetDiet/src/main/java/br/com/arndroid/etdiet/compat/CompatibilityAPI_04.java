package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// DONUT = API 4
@TargetApi(Build.VERSION_CODES.DONUT)
public class CompatibilityAPI_04 extends CompatibilityAPI_03 {

    // Package only:
    CompatibilityAPI_04() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.DONUT;
    }
}
