package org.hk.training.toilet.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.hk.training.toilet.util.KeyListPair;
import org.hk.training.toilet.util.Lists;

import java.util.ArrayList;

public abstract class AbstractProvider<IN, OUT> {

    abstract String tableName();

    abstract String[] columns();

    abstract OUT createResult(final Cursor cur, final String[] columns);

    private DatabaseHelper databaseHelper;
    private final Context mContext;
    private String mSelection = null;
    private String[] mSelectionArgs = null;
    private String mOrder = null;
    private String mLimit = null;
    private String mOffset = null;
    private String mGroupBy = null;
    private boolean mDistinct = false;
    private String[] mColumns = columns();

    /**
     * equal(column = val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair equal(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatEqual(column), val);
    }

    /**
     * not equal(column != val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair notEqual(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatNotEqual(column), val);
    }

    /**
     * less than(column < val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair lessThan(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatLessThan(column), val);
    }

    /**
     * less than equla (column <= val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair lessThanEqual(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatLessThanEqual(column), val);
    }

    /**
     * greater than(column > val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair greaterThan(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatGreaterThan(column), val);
    }

    /**
     * greater than equal(column >= val)表現を返す
     *
     * @param column
     * @param val
     * @return
     */
    public static KeyListPair greaterThanEqual(final String column, final String val) {
        return new KeyListPair(DatabaseHelper.formatGreaterThanEqual(column), val);
    }

    /**
     * and表現を返す
     *
     * @param maps
     * @return
     */
    public static KeyListPair and(final KeyListPair... maps) {
        return formatCondition(" and ", maps);
    }

    /**
     * or 表現を返す
     *
     * @param maps
     * @return
     */
    public static KeyListPair or(final KeyListPair... maps) {
        return formatCondition(" or ", maps);
    }

    /**
     * フォーマットされた文字列を返す
     *
     * @param with
     * @param maps
     * @return
     */
    private static KeyListPair formatCondition(final String with, final KeyListPair... maps) {
        final KeyListPair dest = maps[0];
        for (final KeyListPair k : maps) {
            if (!k.equals(dest)) {
                dest.add(k, with);
            }
        }
        return dest;
    }

    /**
     * where 表現
     *
     * @param maps
     * @return
     */
    public final AbstractProvider<IN, OUT> where(final KeyListPair... maps) {
        final KeyListPair dest = formatCondition(" and ", maps);
        mSelection = dest.key;
        mSelectionArgs = dest.toValueStrings();
        return this;
    }

    /**
     * order by asc 表現
     *
     * @param order
     * @return
     */
    public final AbstractProvider<IN, OUT> orderByAsc(final String order) {
        mOrder = DatabaseHelper.formatOrderAsc(order);
        return this;
    }

    /**
     * order by desc 表現
     *
     * @param order
     * @return
     */
    public final AbstractProvider<IN, OUT> orderByDesc(final String order) {
        mOrder = DatabaseHelper.formatOrderDesc(order);
        return this;
    }

    public final AbstractProvider<IN, OUT> distinct(final boolean distinct) {
        mDistinct = distinct;
        return this;
    }

    public final AbstractProvider<IN, OUT> columns(final String...columns){
        mColumns = columns;
        return this;
    }

    /**
     * リミット
     *
     * @param limit
     * @return
     */
    public final AbstractProvider<IN, OUT> limit(final String limit) {
        mLimit = limit;
        return this;
    }

    public final AbstractProvider<IN, OUT> offset(final String offset) {
        mOffset = offset;
        return this;
    }

    public final AbstractProvider<IN, OUT> groupBy(final String groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    /**
     * 現在のコンディションをクリアする
     */
    private final void clearCondition() {
        mLimit = null;
        mOrder = null;
        mSelection = null;
        mSelectionArgs = null;
        mOffset = null;
        mGroupBy = null;
        mDistinct = false;
        mColumns = columns();
    }

    /**
     * コンストラクタ
     *
     * @param context
     */
    public AbstractProvider(final Context context) {
        mContext = context;
    }

    public long insert(final DatabaseOperationIf databaseOperationIf) {
        return insert(databaseOperationIf.getContentValues());
    }

    public void transactionStart(){
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();
    }

    public void transactionEnd(boolean isCommit){
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        if(isCommit){
            db.setTransactionSuccessful();
        }
        db.endTransaction();
    }

    /**
     * 挿入する。
     *
     * @param v
     */
    protected long insert(final ContentValues v) {
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final long id = db.insert(tableName(), null, v);
        return id;
    }

    public void update(final DatabaseOperationIf databaseOperationIf) {
        this.update(databaseOperationIf.getContentValues());
    }

    /**
     * 更新する。
     *
     * @param v
     */
    public void update(final ContentValues v) {
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.update(tableName(), v, mSelection, mSelectionArgs);
        this.clearCondition();
    }

    /**
     * 削除する。
     */
    public void delete() {
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(tableName(), mSelection, mSelectionArgs);
        this.clearCondition();
    }

    /**
     * 一つの結果を返す
     *
     * @return
     */
    public OUT getSingleResult() {
        databaseHelper = new DatabaseHelper(mContext);
        String offsetLimit = null;
        if (mOffset != null && mLimit != null) {
            offsetLimit = mOffset + "," + mLimit;
        } else if (mLimit != null) {
            offsetLimit = mLimit;
        }

        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final Cursor cur = db.query(
                mDistinct,
                tableName(),
                mColumns,
                mSelection,
                mSelectionArgs,
                mGroupBy,
                null,
                mOrder,
                offsetLimit);
        OUT t = null;
        if (cur.moveToFirst()) {
            t = createResult(cur, mColumns);
        }
        cur.close();
        clearCondition();
        return t;
    }

    /**
     * 複数の結果を返す
     *
     * @return
     */
    public ArrayList<OUT> getResults() {
        final ArrayList<OUT> list = Lists.newEmptyArrayList();
        databaseHelper = new DatabaseHelper(mContext);
        String offsetLimit = null;
        if (mOffset != null && mLimit != null) {
            offsetLimit = mOffset + "," + mLimit;
        } else if (mLimit != null) {
            offsetLimit = mLimit;
        }
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        final Cursor cur = db.query(
                mDistinct,
                tableName(),
                mColumns,
                mSelection,
                mSelectionArgs,
                mGroupBy,
                null,
                mOrder,
                offsetLimit);
        while (cur.moveToNext()) {
            list.add(createResult(cur, mColumns));
        }
        cur.close();
        clearCondition();
        return list;
    }

    private String COUNT_SQL = "SELECT COUNT(_id) FROM %s %s";
    private String WHERE = "WHERE %s";

    public int getCount() {
        databaseHelper = new DatabaseHelper(mContext);
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String where = "";
        if (mSelection != null && mSelection.length() != 0) {
            where = String.format(WHERE, mSelection);
        }
        String sql = String.format(COUNT_SQL, tableName(), where);
        final Cursor cur = db.rawQuery(sql, mSelectionArgs);
        cur.moveToFirst();
        int count = cur.getInt(0);
        cur.close();
        clearCondition();
        return count;
    }
}
