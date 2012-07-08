package com.hvn.test;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.hvn.test.RememberMe.Notes;

public class SimpleProvider extends ContentProvider {
	private static final String DATABASE_NAME = "reme.db";
	private static final String NOTES_TABLE_NAME = "notes";
	private static final int DATABASE_VERSION = 2;

	private static HashMap<String, String> sNoteProjectionMap;

	private static final int NOTES = 1;
	private static final int NOTE_ID = 2;

	private static final UriMatcher sUriMatcher;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + NOTES_TABLE_NAME + " ( " + Notes._ID
					+ " INTEGER PRIMARY KEY," + Notes.TITLE + " TEXT, "
					+ Notes.NOTE + " TEXT " + ");");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.d("com.hvn.test", "upgrade from " + oldVersion + " TO "
					+ newVersion);
			db.execSQL("DROP TABLE IF EXISTS " + NOTES_TABLE_NAME);
			onCreate(db);
		}
	}// DatabaseHelper

	private DatabaseHelper mOpenHelper;

	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		Log.d("com.hvn.test", "SimpleProvider:insert" + "in this");
		if (sUriMatcher.match(uri) != NOTES) {
			throw new IllegalArgumentException("Uknown URI" + uri);
		}

		ContentValues contentValues;
		if (values != null) {
			contentValues = new ContentValues(values);
		} else {
			contentValues = new ContentValues();
		}

		if (contentValues.containsKey(RememberMe.Notes.TITLE) == false) {
			contentValues.put(RememberMe.Notes.TITLE, "You hadnot named title");
		}

		if (contentValues.containsKey(RememberMe.Notes.NOTE) == false) {
			contentValues.put(RememberMe.Notes.NOTE, "Empty content");
		}
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		long rowId = db.insert(NOTES_TABLE_NAME, Notes.NOTE, contentValues);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(Notes.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}

		// TODO WHY HAVE TO TRY CATCH.
		// throw new SQLException();
		return uri;
	}

	@Override
	public boolean onCreate() {
		mOpenHelper = new DatabaseHelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		switch (sUriMatcher.match(uri)) {
		case NOTES:
			qb.setTables(NOTES_TABLE_NAME);
			qb.setProjectionMap(sNoteProjectionMap);
			break;

		case NOTE_ID:
			qb.setTables(NOTES_TABLE_NAME);
			qb.setProjectionMap(sNoteProjectionMap);
			qb.appendWhere(Notes._ID + "=" + uri.getPathSegments().get(1));

		default:
			throw new IllegalArgumentException("Unkown uri" + uri);
		}

		String orderBy;
		if (TextUtils.isEmpty(sortOrder)) {
			orderBy = Notes.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}

		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, orderBy);

		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}// query

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}

	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(RememberMe.AUTHORITY, "notes", NOTES);
		sUriMatcher.addURI(RememberMe.AUTHORITY, "notes/#", NOTE_ID);

		sNoteProjectionMap = new HashMap<String, String>();
		sNoteProjectionMap.put(Notes._ID, Notes._ID);
		sNoteProjectionMap.put(Notes.TITLE, Notes.TITLE);
		sNoteProjectionMap.put(Notes.NOTE, Notes.NOTE);
	}
}
