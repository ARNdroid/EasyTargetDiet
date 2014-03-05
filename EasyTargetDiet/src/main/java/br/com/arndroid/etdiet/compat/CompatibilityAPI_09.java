package br.com.arndroid.etdiet.compat;

import android.annotation.TargetApi;
import android.os.Build;

// GINGERBREAD = API 9
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class CompatibilityAPI_09 extends CompatibilityAPI_08 {

    // Package only:
    CompatibilityAPI_09() {}

    @Override
    public int compatibilityLevel() {
        return Build.VERSION_CODES.GINGERBREAD;
    }
}
