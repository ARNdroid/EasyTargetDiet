package br.com.arndroid.etdshr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: remove test.
public class Test {

    private static final Logger LOG = LoggerFactory.getLogger(Test.class);

    public static String showDependency() {
        LOG.warn("Inside Test.showDependency()");
        return "depends on etd-shr";
    }
}
