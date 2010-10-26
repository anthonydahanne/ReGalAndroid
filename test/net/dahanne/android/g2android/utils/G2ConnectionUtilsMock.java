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
package net.dahanne.android.g2android.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

/**
 * 
 * @author Anthony Dahanne NOT USED AT THE MOMENT
 */
public class G2ConnectionUtilsMock {

	static final String GR_STAT_SUCCESS = "0";

	/**
	 * Static variables
	 */
	private static List<Cookie> sessionCookies = new ArrayList<Cookie>();
	static {
		sessionCookies.add(new BasicClientCookie("", ""));
	}

	/**
	 * Check whether the galleryUrl provided is valid or not
	 * 
	 * @param galleryHost
	 * @param galleryPath
	 * @param galleryPort
	 * @return boolean
	 * @throws GalleryConnectionException
	 */
	public static boolean checkGalleryUrlIsValid(String galleryHost,
			String galleryPath, int galleryPort)
			throws GalleryConnectionException {

		return true;

	}

	/**
	 * The login method, cookie handling is located in sendCommandToGallery
	 * method
	 * 
	 * @param galleryUrl
	 * @param user
	 * @param password
	 * @return
	 * @throws GalleryConnectionException
	 */
	public static String loginToGallery(String galleryHost, String galleryPath,
			int galleryPort, String user, String password)
			throws GalleryConnectionException {

		return "89789HJKLHKLU7890";

	}

}
