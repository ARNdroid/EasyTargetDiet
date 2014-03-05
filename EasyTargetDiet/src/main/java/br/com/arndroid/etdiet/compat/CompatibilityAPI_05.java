package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// ECLAIR = API 5
@TargetApi(Build.VERSION_CODES.ECLAIR)
public class CompatibilityAPI_05 extends CompatibilityAPI_04 {

    // Package only:
    CompatibilityAPI_05() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.ECLAIR;
    }
}
