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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

/**
 * 
 * @author Anthony Dahanne
 * 
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
