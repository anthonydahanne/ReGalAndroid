/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.android.regalandroid.utils;

import net.dahanne.gallery.commons.model.Album;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {

	public static final String DB_NAME = "regalandroid_context";
	public static final String DB_TABLE = "regalandroid_context_table";
	public static final int DB_VERSION = 3;

	private static final String CLASSNAME = DBHelper.class.getSimpleName();
	private static final String[] COLS = new String[] { "_id",
			"current_position", "current_album" };

	private SQLiteDatabase db;
	private final DBOpenHelper dbOpenHelper;

	//
	// inner classes
	//

	public static class G2AndroidContext {

		public long id;
		public int currentPosition;
		public Album currentAlbum;

		public G2AndroidContext() {
		}

		public G2AndroidContext(final long id, final int currentPosition,
				final Album currentAlbum) {
			this.id = id;
			this.currentPosition = currentPosition;
			this.currentAlbum = currentAlbum;
		}

		@Override
		public String toString() {
			return id + " " + currentPosition + " " + currentAlbum;
		}
	}

	private static class DBOpenHelper extends SQLiteOpenHelper {

		private static final String DB_CREATE = "CREATE TABLE "
				+ DBHelper.DB_TABLE
				+ " (_id INTEGER PRIMARY KEY, current_position INTEGER, current_album BLOB );";

		public DBOpenHelper(final Context context) {
			super(context, DBHelper.DB_NAME, null, DBHelper.DB_VERSION);
		}

		@Override
		public void onCreate(final SQLiteDatabase db) {
			try {
				db.execSQL(DBOpenHelper.DB_CREATE);
			} catch (SQLException e) {
				// Log.e(Constants.LOGTAG, DBHelper.CLASSNAME, e);
			}
		}

		@Override
		public void onOpen(final SQLiteDatabase db) {
			super.onOpen(db);
		}

		@Override
		public void onUpgrade(final SQLiteDatabase db, final int oldVersion,
				final int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DBHelper.DB_TABLE);
			onCreate(db);
		}
	}

	//
	// end inner classes
	//

	public DBHelper(final Context context) {
		dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	private void establishDb() {
		if (db == null) {
			db = dbOpenHelper.getWritableDatabase();
		}
	}

	public void cleanup() {
		if (db != null) {
			db.close();
			db = null;
		}
	}

	public void insert(final G2AndroidContext g2AndroidContext) {
		ContentValues values = new ContentValues();
		values.put("current_position", g2AndroidContext.currentPosition);
		if(g2AndroidContext.currentAlbum!=null){
			values.put("current_album", g2AndroidContext.currentAlbum.serialize());
		}

		db.insert(DBHelper.DB_TABLE, null, values);

		cleanup();
	}

	public void update(final G2AndroidContext g2AndroidContext) {
		ContentValues values = new ContentValues();
		values.put("current_position", g2AndroidContext.currentPosition);
		values.put("current_album", g2AndroidContext.currentAlbum.serialize());
		db.update(DBHelper.DB_TABLE, values, "_id=" + g2AndroidContext.id, null);
		cleanup();
	}

	public void delete(final long id) {
		db.delete(DBHelper.DB_TABLE, "_id=" + id, null);
	}

	public void deleteAll() {
		db.delete(DBHelper.DB_TABLE, null, null);
	}


	public G2AndroidContext getLast() {
		Cursor c = null;
		G2AndroidContext g2AndroidContext = null;
		try {
			c = db.query(true, DBHelper.DB_TABLE, DBHelper.COLS, null, null,
					null, null, "_id DESC", null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				g2AndroidContext = new G2AndroidContext();
				g2AndroidContext.id = c.getLong(0);
				g2AndroidContext.currentPosition = c.getInt(1);
				byte[] blob = c
						.getBlob(2);
				if(blob!=null){
					g2AndroidContext.currentAlbum = Album.unserializeAlbum(blob);
				}

			}
		} catch (SQLException e) {
			// TODO we should catch this exception instead of killing it !!!
		} finally {
			c.close();
			cleanup();
		}
		return g2AndroidContext;
	}

}