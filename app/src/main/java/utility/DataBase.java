package utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

public class DataBase {

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context ctx) {
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_0_CREATE);
            db.execSQL(TABLE_1_CREATE);
            db.execSQL(TABLE_2_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + category_master);
            db.execSQL("DROP TABLE IF EXISTS " + basket);
            db.execSQL("DROP TABLE IF EXISTS " + basket_items);
            onCreate(db);
        }
    }

    private DatabaseHelper dbHelper;
    private SQLiteDatabase sqLiteDb;
    private Context HCtx = null;
    private static final String DATABASE_NAME = "dbGlamour";
    private static final int DATABASE_VERSION = 1;

    public static final String category_master = "category_master";
    public static final int category_master_int = 0;
    public static final String basket = "basket";
    public static final int basket_int = 1;
    public static final String basket_items = "basket_items";
    public static final int basket_items_int = 2;

    String[][] tables = new String[][]{{"_ID", "id", "parent_id", "category_name", "category_description", "category_for", "image"},
            {"_ID", "is_order_place", "is_order_place_successfully", "user_id", "createdOn","sentOn","order_number"}, {"_ID", "basket_id", "product_id", "name", "desc",
            "sizeId", "size", "quantity", "price", "image"}};

    private static final String TABLE_0_CREATE = "create table "
            + category_master
            + "(_ID integer primary key autoincrement,"
            + "id text not null,parent_id text not null, category_name text not null," +
            "category_description text not null,category_for text not null,image text not null);";

    private static final String TABLE_1_CREATE = "create table "
            + basket
            + "(_ID integer primary key autoincrement,"
            + "is_order_place text not null,is_order_place_successfully text not null, user_id text not null, createdOn text not null,sentOn text not null,order_number text not null);";

    private static final String TABLE_2_CREATE = "create table "
            + basket_items
            + "(_ID integer primary key autoincrement,"
            + "basket_id text not null,product_id text not null, name text not null,desc text not null,sizeId text not null," +
            "size text not null,quantity text not null,price text not null,image text not null);";

    public DataBase(Context ctx) {
        HCtx = ctx;
    }

    public DataBase open() throws SQLException {
        dbHelper = new DatabaseHelper(HCtx);
        sqLiteDb = dbHelper.getWritableDatabase();
        return this;
    }

    public void cleanWhileLogout() {
        sqLiteDb.delete(category_master, null, null);
        Cursor curBasket = fetch(DataBase.basket, "is_order_place_successfully='Y'");
        if (curBasket != null && curBasket.getCount() > 0) {
            curBasket.moveToFirst();
            do {
                int basket_id = curBasket.getInt(0);
                sqLiteDb.delete(DataBase.basket, "_ID=" + basket_id, null);
                sqLiteDb.delete(DataBase.basket_items, "basket_id=" + basket_id, null);
            } while (curBasket.moveToNext());
        }
        curBasket.close();
    }
    public void cleanWhileCloseApp() {
        sqLiteDb.delete(category_master, null, null);
    }

    public void cleanTable(int tableNo) {
        switch (tableNo) {
            case category_master_int:
                sqLiteDb.delete(category_master, null, null);
                break;
            case basket_int:
                sqLiteDb.delete(basket, null, null);
                break;
            case basket_items_int:
                sqLiteDb.delete(basket_items, null, null);
                break;
            default:
                break;
        }
    }

    public void close() {
        dbHelper.close();
    }

    public synchronized long insert(String DATABASE_TABLE, int tableNo,
                                    String[] values) {
        ContentValues vals = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            vals.put(tables[tableNo][i + 1], values[i]);
        }
        return sqLiteDb.insert(DATABASE_TABLE, null, vals);
    }

    public synchronized long insert(String DATABASE_TABLE, ContentValues cv) {
        return sqLiteDb.insert(DATABASE_TABLE, null, cv);
    }

    public synchronized long insertWithSR_NO(String DATABASE_TABLE,
                                             int tableNo, String[] values, String srno) {
        ContentValues vals = new ContentValues();
        for (int i = 0; i < values.length; i++) {
            vals.put(tables[tableNo][i + 1], values[i]);
        }
        vals.put(tables[tableNo][0], srno);
        return sqLiteDb.insert(DATABASE_TABLE, null, vals);
    }

    public boolean delete(String DATABASE_TABLE, int tableNo, long rowId) {
        return sqLiteDb.delete(DATABASE_TABLE,
                tables[tableNo][0] + "=" + rowId, null) > 0;
    }

    public synchronized boolean delete(String DATABASE_TABLE, int tableNo,
                                       String whereCause) {
        return sqLiteDb.delete(DATABASE_TABLE, whereCause, null) > 0;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     long rowId) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][0] + "=" + rowId, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized int getCounts(String DATABASE_TABLE, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where,
                null, null, null, null);
        if (ret != null) {
            return ret.getCount();
        }
        ret.close();
        return 0;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int[] colindex, String where) throws SQLException {

        String[] cols = new String[colindex.length];
        for (int i = 0; i < colindex.length; i++)
            cols[i] = tables[tableNo][colindex[i]];

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, cols, where, null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     String[] cols, String where) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, cols, where, null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "'", null, null,
                null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval, int colIndex2, String colval2)
            throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "' and "
                        + tables[tableNo][colIndex2] + "='" + colval2 + "'",
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        strSelection = strSelection.substring(0, strSelection.length() - 5);
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchQ(String DATABASE_TABLE, int tableNo,
                                      int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        strSelection = strSelection
                + "stackid in (select stack_id from stacks where isarchieve='No')";
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchFollowUp(String DATABASE_TABLE,
                                             int tableNo, int[] colIndex, String[] colval) throws SQLException {

        String strSelection = "";
        for (int i = 0; i < colIndex.length; i++) {
            strSelection = strSelection + tables[tableNo][colIndex[i]] + "='"
                    + colval[i] + "' and ";
        }
        // strSelection = strSelection.substring(0,strSelection.length() - 5);
        strSelection = strSelection
                + "stackid in (select stack_id from stacks where isarchieve='No')";
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                strSelection, null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, int tableNo,
                                     int colIndex, String colval, String orderByval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][colIndex] + "='" + colval + "'", null, null,
                null, orderByval);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetch(String DATABASE_TABLE, String where, String orderByval) throws SQLException {

        Cursor ret = sqLiteDb.query(DATABASE_TABLE, null, where, null, null,
                null, orderByval);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], null, null,
                    null, null, null);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo,
                                        String orderByval) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], null, null,
                    null, null, orderByval);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public synchronized Cursor fetchAll(String DATABASE_TABLE, int tableNo,
                                        String orderByval, String where) {
        try {
            return sqLiteDb.query(DATABASE_TABLE, tables[tableNo], where, null,
                    null, null, orderByval);

        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public synchronized Cursor fetchDistinctSupplier(String DATABASE_TABLE) {
        try {
            return sqLiteDb.query(DATABASE_TABLE,
                    new String[]{"DISTINCT categoryID", "categoryName"}, null, null, null,
                    null, null);
        } catch (Exception e) {
            e.printStackTrace();
            Logger.writeToCrashlytics(e);
            return null;
        }
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          ContentValues vc) {
        return sqLiteDb.update(DATABASE_TABLE, vc, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, String where,
                          ContentValues cv) {
        return sqLiteDb.update(DATABASE_TABLE, cv, where, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, String where, String[] args,
                          ContentValues cv) {
        return sqLiteDb.update(DATABASE_TABLE, cv, where, args) > 0;
    }

    public boolean updatePage(String DATABASE_TABLE, int tableNo,
                              int flashCardID, int pageno, String val) {
        ContentValues vals = new ContentValues();
        // for (int i = 0; i < values.length; i++)
        // vals.put(tables[tableNo][i + 1], values[i]);
        vals.put(tables[tableNo][3], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, " flashcardid='"
                + flashCardID + "' and pageno='" + pageno + "'", null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          int colIndex, int val) {
        ContentValues vals = new ContentValues();
        vals.put(tables[tableNo][colIndex], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public boolean update(String DATABASE_TABLE, int tableNo, long rowId,
                          int colIndex, String val) {
        ContentValues vals = new ContentValues();
        vals.put(tables[tableNo][colIndex], val);
        return sqLiteDb.update(DATABASE_TABLE, vals, tables[tableNo][0] + "="
                + rowId, null) > 0;
    }

    public Cursor fetch(String DATABASE_TABLE, int tableNo, int i,
                        String string, int j) {
        Cursor ret = sqLiteDb.query(DATABASE_TABLE, tables[tableNo],
                tables[tableNo][i] + "='" + string + "' and mediatypeid<>" + j,
                null, null, null, null);
        if (ret != null) {
            ret.moveToFirst();
        }
        return ret;
    }

}

