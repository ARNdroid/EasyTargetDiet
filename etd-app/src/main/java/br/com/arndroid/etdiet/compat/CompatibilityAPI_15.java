package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// ICE_CREAM_SANDWICH_MR1 = API 15
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
public class CompatibilityAPI_15 extends CompatibilityAPI_14 {

    // Package only:
    CompatibilityAPI_15() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }
}
