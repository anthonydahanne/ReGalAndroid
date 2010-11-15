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
