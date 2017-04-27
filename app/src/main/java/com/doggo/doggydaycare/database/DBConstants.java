package com.doggo.doggydaycare.database;

/**
 * Created by Meghan on 4/8/2017.
 */
public class DBConstants {

    public static final String TABLE_NAME = "step_progress";

    // this is the primary key
    public static final String COLUMN_NAME_ENTRY_ID = "id";

    //string
    public static final String COLUMN_NAME_ENTRY_DATE = "date";

    //bigint
    public static final String COLUMN_NAME_ENTRY_DATE_EPOCH = "epoch";

    //int
    public static final String COLUMN_NAME_TOTAL_VALUE = "steps_total";

    //smallint
    public static final String COLUMN_NAME_INCREMENT_VALUE = "steps_delta";

    //boolean
    public static final String COLUMN_NAME_STATUS = "synced";
}

