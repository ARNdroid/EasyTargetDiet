package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// GINGERBREAD_MR1 = API 10
@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class CompatibilityAPI_10 extends CompatibilityAPI_09 {

    // Package only:
    CompatibilityAPI_10() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.GINGERBREAD_MR1;
    }
}
