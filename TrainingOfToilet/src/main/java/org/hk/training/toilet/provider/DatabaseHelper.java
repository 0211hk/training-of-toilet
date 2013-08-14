package org.hk.training.toilet.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.hk.training.toilet.util.Lists;
import org.hk.training.toilet.util.Util;

import java.util.List;

/**
 * DBを管理するクラス
 *
 * @author administrator
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * カラムタイプの列挙
     *
     * @author kasugahiroyuku
     */
    public static enum ColumnType {
        TEXT("TEXT"),
        INT("INT"),
        LONG("LONG"),
        BLOB("BLOB"),
        INTEGER_PRIMARY_KEY_NOT_NULL("INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT");
        private final String type;

        private ColumnType(final String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type;
        }
    }

    // 現在のVersion
    private static final int DATABASE_VERSION = 1;

    private static String getDatabaseName(final Context context) {
        return "toilet_training";
    }

    /**
     * カラムの型定義
     *
     * @param columnName
     * @param type
     * @return
     */
    public static String format(final String columnName, final ColumnType type) {
        return String.format("%s %s", columnName, type.toString());
    }

    /**
     * order descのついたカラムのフォーマット
     *
     * @param column
     * @return
     */
    public static String formatOrderDesc(final String column) {
        return String.format("%s desc", column);
    }

    /**
     * order ascのついたカラムのフォーマット
     *
     * @param column
     * @return
     */
    public static String formatOrderAsc(final String column) {
        return String.format("%s asc", column);
    }

    /**
     * equal( column = ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatEqual(final String column) {
        return String.format("%s = ? ", column);
    }

    /**
     * not equal( column != ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatNotEqual(final String column) {
        return String.format("%s != ? ", column);
    }

    /**
     * greater than( column > ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatGreaterThan(final String column) {
        return String.format("%s > ? ", column);
    }

    /**
     * greater than equal( column >= ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatGreaterThanEqual(final String column) {
        return String.format("%s >= ? ", column);
    }

    /**
     * less than( column < ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatLessThan(final String column) {
        return String.format("%s < ? ", column);
    }

    /**
     * less than equal( column <= ? )のフォーマット
     *
     * @param column
     * @return
     */
    public static String formatLessThanEqual(final String column) {
        return String.format("%s <= ? ", column);
    }

    /**
     * andのフォーマット
     *
     * @param strings
     * @return
     */
    public static String formatAnd(final String... strings) {
        return Util.join(strings, " and ");
    }

    /**
     * or のフォーマット
     *
     * @param strings
     * @return
     */
    public static String formatOr(final String... strings) {
        return Util.join(strings, " or ");
    }

    /**
     * Drop tableのフォーマット
     *
     * @param tableName
     * @return
     */
    public static String dropTableFormat(final String tableName) {
        return String.format("DROP TABLE %s", tableName);
    }

    /**
     * Create tableのフォーマット.
     * <p/>
     * <pre>
     * createTableFormat(_TABLE_NAME, format(_ID, LONG_PRIMARY_KEY),
     * 		format(_KEY, TEXT), format(_VALUE, TEXT), format(_CREATED_AT, LONG))
     * </pre>
     *
     * @param tableName
     * @param colums
     * @return
     */
    public static String createTableFormat(final String tableName, final String... colums) {
        final StringBuilder create = new StringBuilder();
        create.append("CREATE TABLE ");
        create.append(tableName);
        create.append(" ( ");
        for (int i = 0; i < colums.length; i++) {
            create.append(colums[i]);
            if (colums.length != (i + 1)) {
                create.append(",");
            }
        }
        create.append(");");
        return create.toString();
    }

    DatabaseHelper(final Context context) {
        super(context, getDatabaseName(context), null, DATABASE_VERSION);
    }

    /**
     * DB作成時の処理
     */
    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(TrainingProvider.create());
    }

    /**
     * DBのVersionUp時の処理
     */
    @Override
    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        List<String> sql = Lists.newEmptyArrayList();
        sql.addAll(TrainingProvider.upgrade(oldVersion, newVersion));
        for (String s : sql) {
            db.execSQL(s);
        }
    }
}
