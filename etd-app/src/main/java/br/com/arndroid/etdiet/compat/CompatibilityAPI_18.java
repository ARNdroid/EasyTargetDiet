package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// JELLY_BEAN_MR2 = API 18
@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class CompatibilityAPI_18 extends CompatibilityAPI_17 {

    // Package only:
    CompatibilityAPI_18() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
}
