package br.com.arndroid.etdiet.action;

import android.os.Bundle;

public interface FragmentMenuReplier {

    public static String MENU_ITEM_ID_KEY = FragmentMenuReplier.class.getSimpleName()
            + ".MENU_ITEM_ID_KEY";

    public void onReplyMenuFromHolderActivity(int menuItemId);
}
