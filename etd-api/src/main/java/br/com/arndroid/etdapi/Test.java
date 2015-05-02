package br.com.arndroid.etdapi;

// TODO: remove test.
public class Test {

    public static String showDependency() {
        return "depends on etd-api (that " + br.com.arndroid.etdshr.Test.showDependency() + ")";
    }
}
