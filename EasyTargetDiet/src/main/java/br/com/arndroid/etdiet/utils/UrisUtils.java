package br.com.arndroid.etdiet.utils;

import android.net.Uri;

public class UrisUtils {

    public static Uri withAppendedId(Uri contentUri, String id) {
        return appendId(contentUri.buildUpon(), id).build();
    }

    // TODO: this methods may be not necessary. For example I suppose that appendId may be replaced by Uri.withAppendedPath(...)
    public static Uri.Builder appendId(Uri.Builder builder, String id) {
        return builder.appendEncodedPath(id);
    }

    public static String pathForUriMatcherFromUri(Uri uri) {
        final String uriPath = uri.getPath();
        if(uriPath.startsWith("/")) {
            return uriPath.substring(1);
        } else {
            return uriPath;
        }
    }
}
