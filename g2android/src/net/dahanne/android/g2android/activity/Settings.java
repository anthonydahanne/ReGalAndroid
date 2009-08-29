/*
 * G2Android
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
package net.dahanne.android.g2android.activity;

import net.dahanne.android.g2android.R;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Settings extends PreferenceActivity {

	// Option keys and default values
	// TODO : it is too bad I can't load getString from the context : there must
	// be a way not to repeat the key values already in strings.xml
	private static String OPT_GALLERY_URL_KEY = "galleryUrl";
	private static String OPT_USERNAME_KEY = "username";
	private static String OPT_PASSWORD_KEY = "password";
	private static String OPT_PORT_KEY = "port";
	private static String OPT_GALLERY_URL_DEF = "http://g2.dahanne.net";
	private static String OPT_PORT_DEF = "80";

	private static final String OPT_BASE_URL_DEF = "main.php?g2_view=core.DownloadItem&g2_itemId=";
	private static final String TAG = "Settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings);

	}

	/** Get the current value of the galleryUrl option */
	public static String getGalleryUrl(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_GALLERY_URL_KEY, OPT_GALLERY_URL_DEF);
	}

	/** Get the baseUrl */
	public static String getBaseUrl(Context context) {
		return getGalleryUrl(context) + "/" + OPT_BASE_URL_DEF;
	}

	/** Get the galleryHost */
	public static String getGalleryHost(Context context) {
		String galleryUrl = getGalleryUrl(context);
		String galleryHost = "";
		if (galleryUrl != null || StringUtils.isNotBlank(galleryUrl)) {
			int indexSlashSlash = galleryUrl.indexOf("//");
			String galleryUrlWithoutHttp = galleryUrl
					.substring(indexSlashSlash + 2);
			int indexSlash = galleryUrlWithoutHttp.indexOf("/");
			// Log.d(TAG, "index : " + indexSlash);
			if (indexSlash == -1) {
				// galleryUrl just compound of host name
				galleryHost = galleryUrlWithoutHttp;
			} else {

				galleryHost = galleryUrlWithoutHttp.substring(0, indexSlash);
			}
			// Log.d(TAG, "host : " + galleryHost);
			return galleryHost;
		}
		return null;
	}

	/** Get the galleryPath */
	public static String getGalleryPath(Context context) {
		String galleryUrl = getGalleryUrl(context);
		String galleryPath = "/";
		if (galleryUrl != null || StringUtils.isNotBlank(galleryUrl)) {
			int indexSlashSlash = galleryUrl.indexOf("//");
			String galleryUrlWithoutHttp = galleryUrl
					.substring(indexSlashSlash + 2);
			int indexSlash = galleryUrlWithoutHttp.indexOf("/");
			// Log.d(TAG, "index : " + indexSlash);
			if (indexSlash == -1) {
				// galleryUrl just compound of host name
				galleryPath = "/";
			} else {
				galleryPath = galleryUrlWithoutHttp.substring(indexSlash);
			}
			// Log.d(TAG, "path : " + galleryPath);
		}
		return galleryPath;
	}

	/** Get the current value of the galleryUrl option */
	public static String getUsername(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_USERNAME_KEY, "");
	}

	/** Get the current value of the galleryUrl option */
	public static String getPassword(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_PASSWORD_KEY, "");
	}

	/** Get the current value of the galleryUrl option */
	public static int getGalleryPort(Context context)
			throws NumberFormatException {
		String port = PreferenceManager.getDefaultSharedPreferences(context)
				.getString(OPT_PORT_KEY, OPT_PORT_DEF);
		int portAsInt = new Integer(port).intValue();
		return portAsInt;
	}
}
