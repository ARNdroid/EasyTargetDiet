package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// ICE_CREAM_SANDWICH = API 14
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CompatibilityAPI_14 extends CompatibilityAPI_13 {

    // Package only:
    CompatibilityAPI_14() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }
}
