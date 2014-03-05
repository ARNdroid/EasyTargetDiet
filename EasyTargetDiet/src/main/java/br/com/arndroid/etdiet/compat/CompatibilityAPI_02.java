package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// BASE_1_1 = API 2
@TargetApi(Build.VERSION_CODES.BASE_1_1)
public class CompatibilityAPI_02 extends CompatibilityAPI_01 {

    // Package only:
    CompatibilityAPI_02() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.BASE_1_1;
    }
}
