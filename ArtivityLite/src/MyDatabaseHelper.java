

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ArtivityLite.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION); 
    }


    @Override 
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kommentare (id_kommentar INTEGER PRIMARY KEY AUTOINCREMENT, fid_post INTEGER, fid_user INTEGER, body TEXT, created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY(fid_post) REFERENCES posts (id_post), FOREIGN KEY(fid_user) REFERENCES users (id_user)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kommentare");
        onCreate(db);
    }

}


public class Main {
    public static void main(String[] args) throws Exception {
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("fid_post", "1");
        values.put("fid_user", "2");
        values.put("body", "I want a perfect body-eeeeh!");
        db.insert("kommentare", null, values);
    }
}

