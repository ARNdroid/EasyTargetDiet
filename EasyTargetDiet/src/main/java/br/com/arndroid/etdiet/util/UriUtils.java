package br.com.arndroid.etdiet.util;

import android.net.Uri;

public class UriUtils {

    // TODO: this methods may be not necessary. For example I suppose that appendId may be replaced
    // by Uri.withAppendedPath(...)
    public static Uri.Builder appendId(Uri.Builder builder, String id) {
        return builder.appendEncodedPath(id);
    }

    public static Uri withAppendedId(Uri contentUri, String id) {
        return appendId(contentUri.buildUpon(), id).build();
    }
}
