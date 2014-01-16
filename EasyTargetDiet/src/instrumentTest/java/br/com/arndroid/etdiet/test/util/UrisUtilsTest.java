package br.com.arndroid.etdiet.test.util;

import junit.framework.TestCase;

import br.com.arndroid.etdiet.provider.Contract;
import br.com.arndroid.etdiet.utils.UrisUtils;

public class UrisUtilsTest extends TestCase {

    public void testPathForUriMatcherFromUri() {
        assertEquals(Contract.Days.TABLE_NAME, UrisUtils.pathForUriMatcherFromUri(Contract.Days.CONTENT_URI));
        assertEquals(Contract.FoodsUsage.TABLE_NAME, UrisUtils.pathForUriMatcherFromUri(Contract.FoodsUsage.CONTENT_URI));
    }
}
