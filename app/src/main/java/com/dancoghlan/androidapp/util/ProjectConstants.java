package com.dancoghlan.androidapp.util;

import java.util.Arrays;
import java.util.List;

public class ProjectConstants {
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String TIME = "time";
    public static final String DISTANCE = "distance";
    public static final String PACE = "pace";

    public static final String RUN_KEY = "RUN_KEY";

    public static final List<String> MANDATORY_FIELDS = Arrays.asList(TITLE, DATE, TIME, DISTANCE, PACE);

    String[] maintitle = {
            "Title 1",
            "Title 2",
            "Title 3",
            "Title 4",
            "Title 5",
    };

    public static final String MSG_SAVED_VALUES = "Saved values!";
}
