package com.example.max.pdqfragment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;

class DictionaryDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "dictionary.db";
    private static final int DATABASE_VERSION = 1;

    DictionaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        System.out.println("MAX: DictionaryDatabase()");
    }

    ArrayList<String> getSuggestions(String pdq) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        ArrayList<String> suggestions = new ArrayList<>();

        String l1 = Character.toString(pdq.charAt(0));
        String l2 = Character.toString(pdq.charAt(1));
        String l3 = Character.toString(pdq.charAt(2));

        String[] sqlSelect = {"DISTINCT word"};
        String sqlTables = "entries";
        String sqlSelection = "word LIKE '" + l1 + "%" + l2 + "%" + l3 + "%'" +
                           "OR word LIKE '" + l3 + "%" + l2 + "%" + l1 + "%'";
        String sqlOrder = "word ASC";

        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, sqlSelection, null, null, null, sqlOrder);

        if(c != null) {
            if(c.moveToFirst()) {
                do {
                    suggestions.add(c.getString(c.getColumnIndex("word")).toLowerCase());
                } while(c.moveToNext());
            }
        }
        return suggestions;
    }
}
