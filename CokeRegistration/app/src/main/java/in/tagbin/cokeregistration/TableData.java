package in.tagbin.cokeregistration;

import android.provider.BaseColumns;

/**
 * Created by admin pc on 04-04-2016.
 */
public class TableData {

    public TableData() {

    }

    public static abstract class Tableinfo implements BaseColumns {
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String NUMBER = "number";
        public static final String DATABASE_NAME = "userdb97";
        public static final String TABLE_NAME = "regtb97";

        public static final int database_version=2;
    }
}
