package net.dahanne.android.regalandroid.utils;

import net.dahanne.android.regalandroid.RegalAndroidApplication;
import net.dahanne.android.regalandroid.utils.DBHelper.G2AndroidContext;
import android.app.Activity;

public class DBUtils {
	private static DBUtils dbUtils = new DBUtils();

	public static DBUtils getInstance() {
		return dbUtils;
	}

	private DBUtils() {

	}
	
	/**
	 * This methods allows to load a previously saved state into the current session
	 * @param activity
	 */
	public void recoverContextFromDatabase(Activity activity) {
		RegalAndroidApplication application = (RegalAndroidApplication) activity.getApplication();
		DBHelper dbHelper = new DBHelper(activity);
		G2AndroidContext g2c = dbHelper.getLast();
		if (g2c != null) {
			application
					.setCurrentPosition(g2c.currentPosition);
			application
					.setCurrentAlbum(g2c.currentAlbum);
		}
	}
	
	/**
	 * This methods allows to save the current state into the DB
	 * @param activity
	 */
	public void saveContextToDatabase(Activity activity) {
		RegalAndroidApplication application = (RegalAndroidApplication) activity.getApplication();
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.insert(new G2AndroidContext(0,
				application
						.getCurrentPosition(),
						application
						.getCurrentAlbum()));
	}
	
	/**
	 * This methods allows destroy the context
	 * @param activity
	 */
	public void destroyContextFromDataBase(Activity activity) {
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.deleteAll();
	}
	
	
}
