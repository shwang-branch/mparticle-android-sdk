package com.mparticle.internal.database.tables.mp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.mparticle.internal.database.tables.mp.BreadcrumbTable;

public abstract class MpIdDependentTable {
    public static final String MP_ID = "mp_id";

    public abstract String getTableName();

    public void updateMpId(SQLiteDatabase database, long oldMpId, long newMpId) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MP_ID, newMpId);
            database.update(getTableName(), contentValues, MP_ID + " = ?", new String[]{String.valueOf(oldMpId)});
    }
}
