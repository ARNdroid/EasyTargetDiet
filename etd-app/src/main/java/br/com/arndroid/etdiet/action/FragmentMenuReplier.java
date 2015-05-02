package br.com.arndroid.etdiet.action;

import android.os.Bundle;

public interface FragmentMenuReplier {

    String MENU_ITEM_ID_KEY = FragmentMenuReplier.class.getSimpleName()
            + ".MENU_ITEM_ID_KEY";

    void onReplyMenuFromHolderActivity(int menuItemId);
}
