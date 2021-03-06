package uk.ac.tees.aad.w9316578.SqliteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "fooddb";
    private static final String TABLE_FOOD = "food";
    private static final String KEY_ID = "id";
    Context context;


    public DataBaseHelper(Context context1) {
        super(context1, DB_NAME, null, DB_VERSION);
        context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_FOOD + "("
                + "id_auto" + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "date" + " TEXT,"
                + "foodId" + " TEXT,"
                + "foodName" + " TEXT,"
                + "foodPrice" + " TEXT,"
                + "foodDesc" + " TEXT, "
                + "foodItems" + " TEXT, "
                + "foodImageUri" + " TEXT " + ")";

        db.execSQL(CREATE_TABLE);
    }


    public boolean removeItem(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_FOOD, "id" + "=?", new String[]{id}) > 0;
        //return db.delete(TABLE_Users, id + "=" + "id", null) > 0;
    }

    public void removeAllItem() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_FOOD);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        // Create tables again
        onCreate(db);
    }

    public void insertData(String date, String foodId, String foodName, String foodPrice, String foodDesc, String foodItems, String foodImageUri) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("foodId", foodId);
        values.put("foodName", foodName);
        values.put("foodPrice", foodPrice);
        values.put("foodDesc", foodDesc);
        values.put("foodItems", foodItems);
        values.put("foodImageUri", foodImageUri);


        long insert = db.insert(TABLE_FOOD, null, values);

        db.close();
    }


}
