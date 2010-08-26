/**
 * g2android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.g2android.utils;

import net.dahanne.android.g2android.G2AndroidApplication;
import net.dahanne.android.g2android.R;
import net.dahanne.android.g2android.utils.DBHelper.G2AndroidContext;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * @author Anthony Dahanne
 * 
 */
public class ShowUtils {

	private static ShowUtils showUtils = new ShowUtils();

	public static ShowUtils getInstance() {
		return showUtils;
	}

	private ShowUtils() {

	}

	public void alertConnectionProblem(String exceptionMessage,
			String galleryUrl, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// if there was an exception thrown, show it, or tell to verify
		// settings
		String message = context.getString(R.string.not_connected) + galleryUrl
				+ context.getString(R.string.exception_thrown)
				+ exceptionMessage;
		builder.setTitle(R.string.problem)
				.setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void alertFileProblem(String exceptionMessage, Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// if there was an exception thrown, show it, or tell to verify
		// settings
		String message = context.getString(R.string.exception_thrown)
				+ exceptionMessage;
		builder.setTitle(R.string.problem)
				.setMessage(message)
				.setPositiveButton(R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void toastAlbumSuccessfullyCreated(Context context) {
		Toast.makeText(context,
				context.getString(R.string.album_successfully_created),
				Toast.LENGTH_LONG).show();
	}

	public void toastImageSuccessfullyAdded(Context context, String imageName) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(context
				.getString(R.string.image_successfully_created));
		stringBuilder.append(" : ");
		stringBuilder.append(imageName);
		Toast.makeText(context, stringBuilder.toString(), Toast.LENGTH_LONG)
				.show();
	}

	public void toastImagesSuccessfullyAdded(Context context) {
		Toast.makeText(context, R.string.images_successfully_created,
				Toast.LENGTH_LONG).show();
	}

	public void toastCacheSuccessfullyCleared(Context context) {
		Toast.makeText(context, context.getString(R.string.cache_cleared),
				Toast.LENGTH_LONG).show();
	}

	/**
	 * 
	 * @param activity
	 * @return true if we need to login
	 */
	public boolean recoverContextFromDatabase(Activity activity) {
		DBHelper dbHelper = new DBHelper(activity);
		G2AndroidContext g2c = dbHelper.getLast();
		if (g2c != null) {
			((G2AndroidApplication) activity.getApplication())
					.setCurrentPosition(g2c.currentPosition);
			((G2AndroidApplication) activity.getApplication())
					.setRootAlbum(g2c.rootAlbum);
			((G2AndroidApplication) activity.getApplication())
					.setAlbumName(g2c.albumName);
			if (g2c.isLoggedIn == 1) {
				return true;
			}
		}
		return false;
	}

	public void saveContextToDatabase(Activity activity) {
		DBHelper dbHelper = new DBHelper(activity);
		dbHelper.insert(new G2AndroidContext(0,
				((G2AndroidApplication) activity.getApplication())
						.getCurrentPosition(), ((G2AndroidApplication) activity
						.getApplication()).getRootAlbum(),
				((G2AndroidApplication) activity.getApplication())
						.getAlbumName(), G2ConnectionUtils.getInstance()
						.getSessionCookies().size() > 1));
	}

}
