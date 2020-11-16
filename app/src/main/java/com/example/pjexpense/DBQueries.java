package com.example.pjexpense;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBQueries {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper dbHelper;

    public DBQueries(Context context) {
        this.context = context;
    }

    public DBQueries open() throws SQLException {
        dbHelper = new SQLiteHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

//    // Users
//    public boolean insertUser(Users users) {
//        ContentValues values = new ContentValues();
//        values.put(DBConstants.CONTACT_PERSON_NAME, users.getContactPersonName());
//        values.put(DBConstants.CONTACT_NO, users.getContactNumber());
//        values.put(DBConstants.CONTACT_PHOTO, users.getContactPhoto());
//        return database.insert(DBConstants.USER_TABLE, null, values) > -1;
//    }
//
//    public ArrayList<Users> readUsers() {
//        ArrayList<Users> list = new ArrayList<>();
//        try {
//            Cursor cursor;
//            database = dbHelper.getReadableDatabase();
//            cursor = database.rawQuery(DBConstants.SELECT_QUERY, null);
//            list.clear();
//            if (cursor.getCount() > 0) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        String contactId = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_ID));
//                        String conPerson = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_PERSON_NAME));
//                        String conNo = cursor.getString(cursor.getColumnIndex(DBConstants.CONTACT_NO));
//                        byte[] conPhoto = cursor.getBlob(cursor.getColumnIndex(DBConstants.CONTACT_NO));
//                        Users users = new Users(contactId, conPerson, conNo, conPhoto);
//                        list.add(users);
//                    } while (cursor.moveToNext());
//                }
//            }
//            cursor.close();
//        } catch (Exception e) {
//            Log.v("Exception", e.getMessage());
//        }
//        return list;
//    }

}
