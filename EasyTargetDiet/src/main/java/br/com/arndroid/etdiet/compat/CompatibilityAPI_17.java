package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// JELLY_BEAN_MR1 = API 17
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
public class CompatibilityAPI_17 extends CompatibilityAPI_16 {

    // Package only:
    CompatibilityAPI_17() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.JELLY_BEAN_MR1;
    }
}
