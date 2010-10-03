package net.dahanne.android.g2android.utils;

import net.dahanne.android.g2android.model.Album;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper {

	public static final String DB_NAME = "g2android_context";
	public static final String DB_TABLE = "g2android_context_table";
	public static final int DB_VERSION = 3;

	private static final String CLASSNAME = DBHelper.class.getSimpleName();
	private static final String[] COLS = new String[] { "_id",
			"current_position", "root_album", "album_name", "is_logged_in" };

	private SQLiteDatabase db;
	private final DBOpenHelper dbOpenHelper;

	//
	// inner classes
	//

	public static class G2AndroidContext {

		public long id;
		public int currentPosition;
		public Album rootAlbum;
		public int albumName;
		public short isLoggedIn;

		public G2AndroidContext() {
		}

		public G2AndroidContext(final long id, final int currentPosition,
				final Album rootAlbum, final int albumName,
				final boolean isLoggedIn) {
			this.id = id;
			this.currentPosition = currentPosition;
			this.rootAlbum = rootAlbum;
			this.albumName = albumName;
			this.isLoggedIn = isLoggedIn ? (short) 1 : 0;
		}

		@Override
		public String toString() {
			return this.id + " " + this.currentPosition + " " + this.albumName;
		}
	}

	private static class DBOpenHelper extends SQLiteOpenHelper {

		private static final String DB_CREATE = "CREATE TABLE "
				+ DBHelper.DB_TABLE
				+ " (_id INTEGER PRIMARY KEY, current_position INTEGER, root_album BLOB, album_name INTEGER, is_logged_in SHORT );";

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
		this.dbOpenHelper = new DBOpenHelper(context);
		establishDb();
	}

	private void establishDb() {
		if (this.db == null) {
			this.db = this.dbOpenHelper.getWritableDatabase();
		}
	}

	public void cleanup() {
		if (this.db != null) {
			this.db.close();
			this.db = null;
		}
	}

	public void insert(final G2AndroidContext g2AndroidContext) {
		ContentValues values = new ContentValues();
		values.put("current_position", g2AndroidContext.currentPosition);
		values.put("root_album", g2AndroidContext.rootAlbum.serialize());
		values.put("album_name", g2AndroidContext.albumName);
		values.put("is_logged_in", g2AndroidContext.isLoggedIn);

		this.db.insert(DBHelper.DB_TABLE, null, values);
		
		cleanup();
	}

	public void update(final G2AndroidContext g2AndroidContext) {
		ContentValues values = new ContentValues();
		values.put("current_position", g2AndroidContext.currentPosition);
		values.put("root_album", g2AndroidContext.rootAlbum.serialize());
		values.put("album_name", g2AndroidContext.albumName);
		values.put("is_logged_in", g2AndroidContext.isLoggedIn);
		this.db.update(DBHelper.DB_TABLE, values, "_id=" + g2AndroidContext.id,
				null);
		
		cleanup();
	}

	public void delete(final long id) {
		this.db.delete(DBHelper.DB_TABLE, "_id=" + id, null);
	}

	public void deleteAll() {
		this.db.delete(DBHelper.DB_TABLE, null, null);
	}

	// public void delete(final String zip) {
	// this.db.delete(DBHelper.DB_TABLE, "zip='" + zip + "'", null);
	// }

	public G2AndroidContext getLast() {
		Cursor c = null;
		G2AndroidContext g2AndroidContext = null;
		try {
			c = this.db.query(true, DBHelper.DB_TABLE, DBHelper.COLS, null,
					null, null, null, "_id DESC", null);
			if (c.getCount() > 0) {
				c.moveToFirst();
				g2AndroidContext = new G2AndroidContext();
				g2AndroidContext.id = c.getLong(0);
				g2AndroidContext.currentPosition = c.getInt(1);
				g2AndroidContext.rootAlbum = Album.unserializeAlbum(c
						.getBlob(2));
				g2AndroidContext.albumName = c.getInt(3);
				g2AndroidContext.isLoggedIn = c.getShort(4);

			}
		} catch (SQLException e) {
			// Log.v(Constants.LOGTAG, DBHelper.CLASSNAME, e);
		} finally {
			c.close();
			cleanup();
			
//			if (c != null && !c.isClosed()) {
//				c.close();
//			}
		}
		return g2AndroidContext;
	}

}