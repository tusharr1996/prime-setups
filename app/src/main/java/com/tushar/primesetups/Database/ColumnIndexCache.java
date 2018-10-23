package com.tushar.primesetups.Database;

import android.database.Cursor;
import android.util.ArrayMap;

public class ColumnIndexCache {
    private ArrayMap<String, Integer> mMap = new ArrayMap<>();
    public int getColumnIndex(Cursor cursor, String columnName) {
        if (!mMap.containsKey(columnName))
            mMap.put(columnName, cursor.getColumnIndex(columnName));
        return mMap.get(columnName);
    }
    public void clear() {
        mMap.clear();
    }
}