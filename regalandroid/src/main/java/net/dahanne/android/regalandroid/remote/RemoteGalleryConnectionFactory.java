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
package net.dahanne.android.regalandroid.remote;

import net.dahanne.android.regalandroid.activity.Settings;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import android.content.Context;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class RemoteGalleryConnectionFactory {

	private static final int GALLERY2 = 0;
	private static final int GALLERY3 = 1;
	private static final int PIWIGO = 2;
	private static RemoteGallery remoteGallery;
	private static Context context;

	private RemoteGalleryConnectionFactory() {

	}

	public static RemoteGallery getInstance() {
		if (remoteGallery == null) {
			String connectionType = Settings.getGalleryConnectionType(context);
			//if we have "" then let's assume we can go to defaults
			int galleryConnectionType = Settings
					.getGalleryConnectionType(context) .equals("") ? 0 : Integer.valueOf(connectionType);
			switch (galleryConnectionType) {
			case GALLERY2:
				remoteGallery = new G2Connection(Settings
						.getGalleryUrl(context),Settings
						.getUsername(context),Settings
						.getPassword(context));
				break;
			case GALLERY3:
				remoteGallery = new G3Connection(Settings
					.getGalleryUrl(context),Settings
					.getUsername(context),Settings
					.getPassword(context));
				break;
//			case PIWIGO:
//				remoteGallery = new G2ConnectionUtils();
//				break;

			}
		}
		return remoteGallery;
	}

	public RemoteGallery getRemoteGallery() {
		return remoteGallery;
	}

	public static void setContext(Context context) {
		RemoteGalleryConnectionFactory.context = context;
	}
	
	/**
	 * iif the user changes the gallery type, we reset it
	 */
	public static void resetInstance(){
		remoteGallery = null;
	}
}
