package br.com.arndroid.etdiet.compat;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Compatibility {

    private static final Logger LOG = LoggerFactory.getLogger(Compatibility.class);

    public abstract int compatibilityLevel();
    
    public abstract void setBackground(View view, Drawable background);
    
    private static Compatibility instance = null;

    public static Compatibility getInstance() {

        LOG.trace("Compatibility.getInstance() entry point.");
        LOG.trace("Build.VERSION.SDK_INT=={}", Build.VERSION.SDK_INT);

        // OK, OK... We are not afraid of multi-threading issues.
        if (instance == null) {
            LOG.trace("instance == null");
            switch (Build.VERSION.SDK_INT) {
                case Build.VERSION_CODES.BASE:
                    instance =  new CompatibilityAPI_01();
                    break;
                case Build.VERSION_CODES.BASE_1_1:
                    instance =  new CompatibilityAPI_02();
                    break;
                case Build.VERSION_CODES.CUPCAKE:
                    instance =  new CompatibilityAPI_03();
                    break;
                case Build.VERSION_CODES.DONUT:
                    instance =  new CompatibilityAPI_04();
                    break;
                case Build.VERSION_CODES.ECLAIR:
                    instance =  new CompatibilityAPI_05();
                    break;
                case Build.VERSION_CODES.ECLAIR_0_1:
                    instance =  new CompatibilityAPI_06();
                    break;
                case Build.VERSION_CODES.ECLAIR_MR1:
                    instance =  new CompatibilityAPI_07();
                    break;
                case Build.VERSION_CODES.FROYO:
                    instance =  new CompatibilityAPI_08();
                    break;
                case Build.VERSION_CODES.GINGERBREAD:
                    instance =  new CompatibilityAPI_09();
                    break;
                case Build.VERSION_CODES.GINGERBREAD_MR1:
                    instance =  new CompatibilityAPI_10();
                    break;
                case Build.VERSION_CODES.HONEYCOMB:
                    instance =  new CompatibilityAPI_11();
                    break;
                case Build.VERSION_CODES.HONEYCOMB_MR1:
                    instance =  new CompatibilityAPI_12();
                    break;
                case Build.VERSION_CODES.HONEYCOMB_MR2:
                    instance =  new CompatibilityAPI_13();
                    break;
                case Build.VERSION_CODES.ICE_CREAM_SANDWICH:
                    instance =  new CompatibilityAPI_14();
                    break;
                case Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1:
                    instance =  new CompatibilityAPI_15();
                    break;
                case Build.VERSION_CODES.JELLY_BEAN:
                    instance =  new CompatibilityAPI_16();
                    break;
                case Build.VERSION_CODES.JELLY_BEAN_MR1:
                    instance =  new CompatibilityAPI_17();
                    break;
                case Build.VERSION_CODES.JELLY_BEAN_MR2:
                    instance =  new CompatibilityAPI_18();
                    break;
                case Build.VERSION_CODES.KITKAT:
                default:
                    // Here (and only here) the last compatibility version
                    instance =  new CompatibilityAPI_19();
                    break;
            }
            LOG.trace("instance configured.");
        }
        LOG.trace("Returning instance with compatibility level=={}", instance.compatibilityLevel());
        return instance;
    }
}
