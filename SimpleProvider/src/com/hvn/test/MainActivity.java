package com.hvn.test;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.hvn.test.RememberMe.Notes;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */

	private static final String[] PROJECTION = new String[] { Notes._ID,
			Notes.TITLE, Notes.NOTE };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		ContentValues values = new ContentValues();

		// insert empty ContentValues to use check code in "provider insert"
		// values.put(Notes.NOTE, "no title");
		getContentResolver().insert(Notes.CONTENT_URI, values);

		// Show toast
		Toast.makeText(getBaseContext(), "Inserted, check your log",
				Toast.LENGTH_LONG).show();

		// Get all record from provider
		Cursor cursor = managedQuery(Notes.CONTENT_URI, PROJECTION, null, null,
				Notes.DEFAULT_SORT_ORDER);

		if (cursor.moveToFirst()) {
			int cols = cursor.getColumnCount();
			Log.d("com.hvn.test", "Cols " + cols);
			String row = "";
			do {
				row = "";
				for (int i = 0; i < cols; i++) {
					row += cursor.getString(i);
					row += ",";
				}
				Log.d("com.hvn.test", "MainActivity:onCreate " + row);

			} while (cursor.moveToNext());
		}// end if
	}// onCreate
}