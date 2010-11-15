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

import net.dahanne.android.regalandroid.R;
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
							@Override
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
							@Override
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
	
}
