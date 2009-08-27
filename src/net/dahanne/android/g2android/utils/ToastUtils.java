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

import android.content.Context;
import android.widget.Toast;

/**
 * @author Anthony Dahanne This class is used to centralize the Toast messages
 *         of the different exceptions
 */
public class ToastUtils {
	public static final void toastGalleryException(Context context, Exception e) {
		Toast.makeText(context,
				"Could not connect to the gallery ! Java Exception was : "
						+ e.getMessage(), 3);
	}

	public static final void toastNumberFormatException(Context context,
			Exception e) {
		Toast.makeText(context,
				"Port Number must be set a number set between 1 and 65000"
						+ e.getMessage(), 3);
	}

	public static final void toastAlbumSuccessfullyCreated(Context context,
			String albumTitle) {
		Toast.makeText(context, "Album was successfully created " + albumTitle,
				3);
	}
}
