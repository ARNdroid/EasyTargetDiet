package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import java.util.Calendar;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.util.DateUtil;
import br.com.arndroid.etdiet.util.UriUtil;

public class UriUtilTest extends TestCase {

    public void testPathForUriMatcherFromUri() {
        assertEquals(Contract.Days.TABLE_NAME, UriUtil.pathForUriMatcherFromUri(Contract.Days.CONTENT_URI));
        assertEquals(Contract.FoodsUsage.TABLE_NAME, UriUtil.pathForUriMatcherFromUri(Contract.FoodsUsage.CONTENT_URI));
    }
}
