package com.hvn.test;

import android.net.Uri;
import android.provider.BaseColumns;

public final class RememberMe {
	public static final String AUTHORITY = "com.hvn.test.SimpleProvider";

	private RememberMe() {
	}

	public static final class Notes implements BaseColumns {
		private Notes() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ AUTHORITY + "/notes");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.hvn.note";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.hvn.note";

		public static final String TITLE = "title";
		public static final String NOTE = "note";

		public static final String DEFAULT_SORT_ORDER = "_id ASC";
	}

}
