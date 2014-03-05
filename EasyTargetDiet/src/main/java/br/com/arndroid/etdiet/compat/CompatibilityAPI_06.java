package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// ECLAIR_0_1 = API 6
@TargetApi(Build.VERSION_CODES.ECLAIR_0_1)
public class CompatibilityAPI_06 extends CompatibilityAPI_05 {

    // Package only:
    CompatibilityAPI_06() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.ECLAIR_0_1;
    }
}
