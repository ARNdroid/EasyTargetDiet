package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// FROIO = API 8
@TargetApi(Build.VERSION_CODES.FROYO)
public class CompatibilityAPI_08 extends CompatibilityAPI_07 {

    // Package only:
    CompatibilityAPI_08() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.FROYO;
    }
}
