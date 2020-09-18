package ru.javawebinar.topjava.util;

public class MealBooleanWrapper {

    private boolean exceed;

    MealBooleanWrapper() {}

    MealBooleanWrapper(boolean arg) {
        exceed = arg;
    }

    public void setExceed(boolean exceed) {
        this.exceed = exceed;
    }

    public boolean isExceed() {
        return exceed;
    }
}
