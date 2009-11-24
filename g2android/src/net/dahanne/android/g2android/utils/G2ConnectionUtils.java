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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
public class G2ConnectionUtils {

	private static final Charset UTF_8 = Charset.forName("US-ASCII");
	private static final String EQUALS = "=";
	/**
	 * Final static constants
	 */
	private static final String SET_COOKIE = "Set-Cookie";
	private static final BasicNameValuePair LOGIN_CMD_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[cmd]", "login");
	private static final BasicNameValuePair FETCH_ALBUMS_CMD_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[cmd]", "fetch-albums");
	private static final BasicNameValuePair FETCH_ALBUMS_IMAGES_CMD_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[cmd]", "fetch-album-images");
	private static final BasicNameValuePair CREATE_ALBUM_CMD_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[cmd]", "new-album");
	private static final String GR2PROTO = "#__GR2PROTO__";
	private static final String STATUS = "status";
	private static final String ITEM_NAME = "item_name";
	private static final String ALBUM_NAME = "album_name";

	private static final BasicNameValuePair G2_CONTROLLER_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_controller", "remote.GalleryRemote");
	private static final BasicNameValuePair PROTOCOL_VERSION_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[protocol_version]", "2.0");
	private static final String MAIN_PHP = "main.php";
	private static final String USER_AGENT_VALUE = "G2Android Version 1.3.0";
	private static final String USER_AGENT = "User-Agent";
	private static final BasicHeader BASIC_HEADER = new BasicHeader(USER_AGENT,
			USER_AGENT_VALUE);
	private static final String TAG = "G2Utils";
	static final String GR_STAT_SUCCESS = "0";

	/**
	 * Static variables
	 */
	private static String authToken;
	private static final CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
	private static List<Cookie> sessionCookies = new ArrayList<Cookie>();
	static {
		sessionCookies.add(new BasicClientCookie("", ""));
		// we disable the SSL Trust Manager
		// MyTrustManager.disable();
	}

	/**
	 * fetchImages methods : retrieves all the infos needed to fetch images from
	 * the gallery
	 * 
	 * @param galleryUrl
	 * @param albumName
	 * @param galleryHost
	 * @param galleryPath
	 * @param galleryPort
	 * @return
	 * @throws GalleryConnectionException
	 */
	public static HashMap<String, String> fetchImages(String galleryUrl,
			int albumName) throws GalleryConnectionException {
		List<NameValuePair> nameValuePairsFetchImages = new ArrayList<NameValuePair>();
		nameValuePairsFetchImages.add(FETCH_ALBUMS_IMAGES_CMD_NAME_VALUE_PAIR);
		nameValuePairsFetchImages.add(new BasicNameValuePair(
				"g2_form[set_albumName]", "" + albumName));

		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchImages, null);
		return properties;
	}

	/**
	 * Retrieve all the albums infos from the gallery
	 * 
	 * @param galleryUrl
	 * @return
	 * @throws GalleryConnectionException
	 */
	public static HashMap<String, String> fetchAlbums(String galleryUrl)
			throws GalleryConnectionException {
		List<NameValuePair> nameValuePairsFetchAlbums = new ArrayList<NameValuePair>();
		nameValuePairsFetchAlbums.add(FETCH_ALBUMS_CMD_NAME_VALUE_PAIR);

		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchAlbums, null);
		return properties;

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
	public static boolean checkGalleryUrlIsValid(String galleryUrl)
			throws GalleryConnectionException {
		boolean checkUrlIsValid = UriUtils.checkUrlIsValid(galleryUrl);
		if (checkUrlIsValid == false) {
			return false;
		}
		List<NameValuePair> nameValuePairsFetchAlbums = new ArrayList<NameValuePair>();
		// an empty command should return a default string "no command passed" :
		// enough to know that it is ok !
		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchAlbums, null);

		return properties.isEmpty() ? false : true;

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
	public static String loginToGallery(String galleryUrl, String user,
			String password) throws GalleryConnectionException {
		// we reset the last login
		sessionCookies.clear();
		sessionCookies.add(new BasicClientCookie("", ""));

		List<NameValuePair> sb = new ArrayList<NameValuePair>();
		sb.add(LOGIN_CMD_NAME_VALUE_PAIR);
		sb.add(new BasicNameValuePair("g2_form[uname]", "" + user));
		sb.add(new BasicNameValuePair("g2_form[password]", "" + password));
		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				sb, null);
		// auth_token retrieval, used by G2 against cross site forgery
		authToken = null;
		if (properties.get("status") != null
				&& properties.get("status").equals(GR_STAT_SUCCESS)) {
			authToken = properties.get("auth_token");
		}

		return authToken;

	}

	/**
	 * This is the central method for each command sent to the gallery
	 * 
	 * @param galleryUrl
	 * @param nameValuePairsForThisCommand
	 * @param multiPartEntity
	 *            (for sendImageToGallery only)
	 * @return
	 * @throws GalleryConnectionException
	 */
	private static HashMap<String, String> sendCommandToGallery(
			String galleryUrl,
			List<NameValuePair> nameValuePairsForThisCommand,
			HttpEntity multiPartEntity) throws GalleryConnectionException {
		HashMap<String, String> properties = new HashMap<String, String>();
		// URL url;
		try {

			// HttpParams parameters = new BasicHttpParams();
			// SchemeRegistry schemeRegistry = new SchemeRegistry();
			// SSLSocketFactory sslSocketFactory = SSLSocketFactory
			// .getSocketFactory();
			// sslSocketFactory
			// .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// schemeRegistry.register(new Scheme("https", sslSocketFactory,
			// 443));
			// ClientConnectionManager manager = new
			// ThreadSafeClientConnManager(
			// parameters, schemeRegistry);
			// HttpClient httpclient = new DefaultHttpClient(manager,
			// parameters);

			HttpClient httpclient = new DefaultHttpClient();
			// disable expect-continue handshake (lighttpd doesn't supportit)
			httpclient.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);
			HttpPost httpPost = new HttpPost(galleryUrl + "/" + MAIN_PHP);
			// if we send an image to the gallery, we pass it to the gallery
			// through multipartEntity
			httpPost.setHeader(BASIC_HEADER);
			// Setting the cookie
			httpPost.setHeader(getCookieHeader(cookieSpecBase));

			if (multiPartEntity != null) {
				((HttpEntityEnclosingRequestBase) httpPost)
						.setEntity(multiPartEntity);
			}
			// otherwise, UrlEncodedFormEntity is used
			else {
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(G2_CONTROLLER_NAME_VALUE_PAIR);
				nameValuePairs.add(new BasicNameValuePair("g2_authToken",
						authToken));

				nameValuePairs.add(PROTOCOL_VERSION_NAME_VALUE_PAIR);
				nameValuePairs.addAll(nameValuePairsForThisCommand);
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			}

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httpPost);
			InputStream content = response.getEntity().getContent();

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					content), 4096);

			Header[] allHeaders = response.getAllHeaders();
			int galleryPort = getPortFromUrl(galleryUrl);
			String galleryHost = getHostFromUrl(galleryUrl);
			String galleryPath = getPathFromUrl(galleryUrl);
			CookieOrigin origin = new CookieOrigin(galleryHost, galleryPort,
					galleryPath, false);
			// let's find the cookie
			findSessionCookieAmongHeadersAndSaveIt(cookieSpecBase, allHeaders,
					origin);
			String line;
			boolean gr2ProtoStringWasFound = false;
			while ((line = rd.readLine()) != null) {
				// System.out.println(line);
				if (line.contains(GR2PROTO)) {
					gr2ProtoStringWasFound = true;
				}
				if (line.contains(EQUALS) && gr2ProtoStringWasFound) {
					String key = line.substring(0, line.indexOf(EQUALS));
					String value = line.substring(line.indexOf(EQUALS) + 1);
					if (key.equals(STATUS) && value.equals("403")) {
						// something went wrong
						throw new GalleryConnectionException(
								"Operation Failed !");
					}
					properties.put(key, value);
				}
			}
			rd.close();
		} catch (IOException e) {
			// something went wrong, let's throw the info to the UI
			throw new GalleryConnectionException(e.getMessage());
			// Log.d(TAG,e.getMessage());
		} catch (MalformedCookieException e) {
			// something went wrong, let's throw the info to the UI
			throw new GalleryConnectionException(e.getMessage());
			// Log.d(TAG,e.getMessage());
		}
		return properties;
	}

	static String getPathFromUrl(String galleryUrl) {
		String galleryPath = "/";
		if (galleryUrl != null && StringUtils.isNotBlank(galleryUrl)) {
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
		}
		return galleryPath;
	}

	static String getHostFromUrl(String galleryUrl) {
		String galleryHost = "";
		if (galleryUrl != null && StringUtils.isNotBlank(galleryUrl)) {
			int indexSlashSlash = galleryUrl.indexOf("//");
			String galleryUrlWithoutHttp = galleryUrl
					.substring(indexSlashSlash + 2);
			int indexSlash = galleryUrlWithoutHttp.indexOf("/");
			if (indexSlash == -1) {
				// galleryUrl just compound of host name
				galleryHost = galleryUrlWithoutHttp;
			} else {

				galleryHost = galleryUrlWithoutHttp.substring(0, indexSlash);
			}
		}
		// remove the port, if any specified
		if (galleryHost.contains(":")) {
			galleryHost = galleryHost.substring(0, galleryHost.indexOf(":"));
		}
		return galleryHost;
	}

	static int getPortFromUrl(String galleryHost) {
		boolean isHttps = galleryHost.contains("https://");

		int indexOfColumn;
		if (isHttps) {
			indexOfColumn = galleryHost.indexOf(":", 8);
		} else {
			indexOfColumn = galleryHost.indexOf(":", 7);
		}
		if (indexOfColumn == -1) {
			if (!isHttps) {
				return 80;
			}
			return 443;
		}
		String galleryHostFromColumnToEnd = galleryHost
				.substring(indexOfColumn + 1);
		int indexOfSlashAfterColumn = galleryHostFromColumnToEnd.indexOf("/");
		if (indexOfSlashAfterColumn == -1) {
			return new Integer(galleryHostFromColumnToEnd);
		}
		String portAsString = galleryHostFromColumnToEnd.substring(0,
				indexOfSlashAfterColumn);
		return new Integer(portAsString);

	}

	public static InputStream getInputStreamFromUrl(String url)
			throws GalleryConnectionException {
		InputStream content = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			HttpClient httpclient = new DefaultHttpClient();
			httpGet.setHeader(BASIC_HEADER);
			// Setting a cookie header
			httpGet.setHeader(getCookieHeader(cookieSpecBase));
			// Execute HTTP Get Request
			HttpResponse response = httpclient.execute(httpGet);
			// System.out.println(response.getEntity().getContentLength());
			content = response.getEntity().getContent();
		} catch (Exception e) {
			throw new GalleryConnectionException(e.getMessage());
		}
		return content;
	}

	private static void findSessionCookieAmongHeadersAndSaveIt(
			CookieSpecBase cookieSpecBase, Header[] allHeaders,
			CookieOrigin origin) throws MalformedCookieException {
		for (Header header : allHeaders) {
			if (header.getName().equals(SET_COOKIE)) {
				List<Cookie> parse = cookieSpecBase.parse(header, origin);
				for (Cookie cookie : parse) {
					// THE cookie
					if (StringUtils.isNotBlank(cookie.getValue())) {
						sessionCookies.add(cookie);
					}
				}
			}

		}
	}

	private static Header getCookieHeader(CookieSpecBase cookieSpecBase) {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.addAll(sessionCookies);
		List<Header> cookieHeader = cookieSpecBase.formatCookies(cookies);
		return cookieHeader.get(0);
	}

	private static MultipartEntity createMultiPartEntityForSendImageToGallery(
			int albumName, File imageFile, String name)
			throws GalleryConnectionException {
		MultipartEntity multiPartEntity;
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("g2_controller", new StringBody(
					"remote.GalleryRemote", UTF_8));
			multiPartEntity.addPart("g2_form[cmd]", new StringBody("add-item",
					UTF_8));
			multiPartEntity.addPart("g2_form[set_albumName]", new StringBody(""
					+ albumName, UTF_8));
			multiPartEntity.addPart("g2_userfile_name", new StringBody(name,
					UTF_8));
			multiPartEntity.addPart("g2_authToken", new StringBody(authToken,
					UTF_8));
			multiPartEntity.addPart("g2_form[force_filename]", new StringBody(
					name, UTF_8));
			multiPartEntity.addPart("g2_form[caption]", new StringBody(name,
					UTF_8));
			multiPartEntity.addPart("g2_userfile", new FileBody(imageFile));
		} catch (Exception e) {
			throw new GalleryConnectionException(e.getMessage());
		}
		return multiPartEntity;
	}

	/**
	 * @param galleryUrl
	 * @param parentAlbumName
	 * @param albumName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public static int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException {
		int newAlbumName = 0;

		List<NameValuePair> nameValuePairsFetchImages = new ArrayList<NameValuePair>();
		nameValuePairsFetchImages.add(CREATE_ALBUM_CMD_NAME_VALUE_PAIR);
		nameValuePairsFetchImages.add(new BasicNameValuePair(
				"g2_form[set_albumName]", "" + parentAlbumName));
		nameValuePairsFetchImages.add(new BasicNameValuePair(
				"g2_form[newAlbumName]", albumName));
		nameValuePairsFetchImages.add(new BasicNameValuePair(
				"g2_form[newAlbumTitle]", albumTitle));
		nameValuePairsFetchImages.add(new BasicNameValuePair(
				"g2_form[newAlbumDesc]", albumDescription));

		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchImages, null);
		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().equals(ALBUM_NAME)) {
				newAlbumName = new Integer(entry.getValue()).intValue();
			}
		}
		return newAlbumName;
	}

	/**
	 * @param galleryUrl
	 * @param parentAlbumName
	 * @param albumName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public static int sendImageToGallery(String galleryUrl, int albumName,
			File imageFile) throws GalleryConnectionException {
		int imageCreatedName = 0;
		String name = imageFile.getName().substring(0,
				imageFile.getName().indexOf("."));
		MultipartEntity multiPartEntity = createMultiPartEntityForSendImageToGallery(
				albumName, imageFile, name);
		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				null, multiPartEntity);

		List<NameValuePair> nameValuePairsFetchImages = new ArrayList<NameValuePair>();
		nameValuePairsFetchImages.add(CREATE_ALBUM_CMD_NAME_VALUE_PAIR);
		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().equals(ITEM_NAME)) {
				imageCreatedName = new Integer(entry.getValue()).intValue();
			}
		}
		return imageCreatedName;
	}
}
