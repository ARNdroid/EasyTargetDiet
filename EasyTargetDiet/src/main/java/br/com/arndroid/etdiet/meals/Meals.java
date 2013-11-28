package br.com.arndroid.etdiet.meals;

public final class Meals {

    // Utility class
    private Meals() {

    }

    // TODO: This is a big problem. Actually the real values are loaded from a string array resource!
    // TODO: we need to think a solution more secure!
    // The value of this constants can NOT
    // be changed since their values are stored
    // in database
    static final public int EXERCISE = -1;
    static final public int BREAKFAST = 0;
    static final public int BUNCH = 1;
    static final public int LUNCH = 2;
    static final public int SNEAK = 3;
    static final public int DINNER = 4;
    static final public int SUPPER = 5;
}
