package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// CUPCAKE = API 3
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class CompatibilityAPI_03 extends CompatibilityAPI_02 {

    // Package only:
    CompatibilityAPI_03() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.CUPCAKE;
    }
}
