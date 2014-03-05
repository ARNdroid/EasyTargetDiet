package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// ECLAIR_MR1 = API 7
@TargetApi(Build.VERSION_CODES.ECLAIR_MR1)
public class CompatibilityAPI_07 extends CompatibilityAPI_06 {

    // Package only:
    CompatibilityAPI_07() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.ECLAIR_MR1;
    }
}
