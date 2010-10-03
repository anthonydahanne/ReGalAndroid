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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import net.dahanne.android.g2android.utils.ssl.FakeSocketFactory;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class G2ConnectionUtils {

	private static final String UPLOAD_FAILED = "Upload Failed";
	private static final String FAILED = "failed";
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
	private static final String USER_AGENT_VALUE = "G2Android Version 1.6.1";
	private static final String USER_AGENT = "User-Agent";
	private static final BasicHeader BASIC_HEADER = new BasicHeader(USER_AGENT,
			USER_AGENT_VALUE);
	private static final String TAG = "G2Utils";
	static final String GR_STAT_SUCCESS = "0";

	/**
	 * instance variables
	 */
	private String authToken;
	private final CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
	private final List<Cookie> sessionCookies = new ArrayList<Cookie>();

	private static G2ConnectionUtils g2ConnectionUtils = new G2ConnectionUtils();
	private final DefaultHttpClient defaultHttpClient;

	private G2ConnectionUtils() {
		sessionCookies.add(new BasicClientCookie("", ""));
		// the httpclient initialization is heavy, we create one for
		// G2ConnectionUtils
		defaultHttpClient = createHttpClient();

	}

	public static G2ConnectionUtils getInstance() {
		return g2ConnectionUtils;
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
	public HashMap<String, String> fetchImages(String galleryUrl, int albumName)
			throws GalleryConnectionException {
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
	public HashMap<String, String> fetchAlbums(String galleryUrl)
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
	public boolean checkGalleryUrlIsValid(String galleryUrl)
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
	public String loginToGallery(String galleryUrl, String user, String password)
			throws GalleryConnectionException {
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

	public InputStream getInputStreamFromUrl(String url)
			throws GalleryConnectionException {
		InputStream content = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(BASIC_HEADER);
			// Setting a cookie header
			httpGet.setHeader(getCookieHeader(cookieSpecBase));
			// Execute HTTP Get Request
			HttpResponse response = defaultHttpClient.execute(httpGet);
			// System.out.println(response.getEntity().getContentLength());
			content = response.getEntity().getContent();
		} catch (Exception e) {
			throw new GalleryConnectionException(e.getMessage());
		}
		return content;
	}

	private Header getCookieHeader(CookieSpecBase cookieSpecBase) {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.addAll(sessionCookies);
		List<Header> cookieHeader = cookieSpecBase.formatCookies(cookies);
		return cookieHeader.get(0);
	}

	private MultipartEntity createMultiPartEntityForSendImageToGallery(
			int albumName, File imageFile, String imageName, String summary,
			String description) throws GalleryConnectionException {
		if (imageName == null) {
			imageName = imageFile.getName().substring(0,
					imageFile.getName().indexOf("."));
		}

		MultipartEntity multiPartEntity;
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("g2_controller", new StringBody(
					"remote.GalleryRemote", UTF_8));
			multiPartEntity.addPart("g2_form[cmd]", new StringBody("add-item",
					UTF_8));
			multiPartEntity.addPart("g2_form[set_albumName]", new StringBody(""
					+ albumName, UTF_8));
			multiPartEntity.addPart("g2_authToken", new StringBody(authToken,
					UTF_8));
			multiPartEntity.addPart("g2_form[caption]", new StringBody(
					imageName, UTF_8));
			multiPartEntity.addPart("g2_form[extrafield.Summary]",
					new StringBody(summary, UTF_8));
			multiPartEntity.addPart("g2_form[extrafield.Description]",
					new StringBody(description, UTF_8));
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
	public int createNewAlbum(String galleryUrl, int parentAlbumName,
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
	 * @param imageName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public int sendImageToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException {

		int imageCreatedName = 0;

		MultipartEntity multiPartEntity = createMultiPartEntityForSendImageToGallery(
				albumName, imageFile, imageName, summary, description);
		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				null, multiPartEntity);

		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().equals(ITEM_NAME)) {
				imageCreatedName = new Integer(entry.getValue()).intValue();
				break;
			}
			if (entry.getValue().contains(FAILED)) {
				throw new GalleryConnectionException(UPLOAD_FAILED);
			}
		}
		return imageCreatedName;
	}

	public String getAuthToken() {
		return authToken;
	}

	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}

	public List<Cookie> getSessionCookies() {
		return sessionCookies;
	}

	private DefaultHttpClient createHttpClient() {
		// avoid instanciating
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		// http scheme
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// https scheme
		schemeRegistry.register(new Scheme("https", new FakeSocketFactory(),
				443));

		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(30));
		params.setParameter(HttpProtocolParams.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);

		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);

		return new DefaultHttpClient(cm, params);
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
	private HashMap<String, String> sendCommandToGallery(String galleryUrl,
			List<NameValuePair> nameValuePairsForThisCommand,
			HttpEntity multiPartEntity) throws GalleryConnectionException {
		HashMap<String, String> properties = new HashMap<String, String>();
		try {

			// retrieve previous cookies
			List<Cookie> cookies = defaultHttpClient.getCookieStore()
					.getCookies();
			// if (cookies.isEmpty()) {
			// System.out.println("None");
			// } else {
			// for (int i = 0; i < cookies.size(); i++) {
			// System.out.println("- " + cookies.get(i).toString());
			// }
			// }

			// disable expect-continue handshake (lighttpd doesn't supportit)
			defaultHttpClient.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);

			// bug #25 : for embedded gallery, should not add main.php
			String correctedGalleryUrl = galleryUrl;
			if (!UriUtils.isEmbeddedGallery(galleryUrl)) {
				correctedGalleryUrl = galleryUrl + "/" + MAIN_PHP;
			}

			HttpPost httpPost = new HttpPost(correctedGalleryUrl);

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
			// add apache patterns for cookies
			String[] patternsArray = new String[2];
			patternsArray[0] = "EEE, dd MMM-yyyy-HH:mm:ss z";
			patternsArray[1] = "EEE, dd MMM yyyy HH:mm:ss z";
			// be extremely careful here, android httpclient needs it to be an
			// array of string, not an arraylist
			defaultHttpClient.getParams().setParameter(
					CookieSpecPNames.DATE_PATTERNS, patternsArray);
			HttpResponse response = null;
			// Execute HTTP Post Request and retrieve content
			try {
				response = defaultHttpClient.execute(httpPost);
			} catch (ClassCastException e) {
				// happens if using the http client not from android
				List<String> patternsList = Arrays.asList(patternsArray);
				defaultHttpClient.getParams().setParameter(
						CookieSpecPNames.DATE_PATTERNS, patternsList);
				response = defaultHttpClient.execute(httpPost);
			}
			InputStream content = response.getEntity().getContent();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					content), 4096);
			// do not forget the cookies
			sessionCookies.addAll(cookies);

			String line;
			boolean gr2ProtoStringWasFound = false;
			while ((line = rd.readLine()) != null) {
				if (line.contains(GR2PROTO)) {
					gr2ProtoStringWasFound = true;
				}
				if (line.contains(EQUALS) && gr2ProtoStringWasFound) {
					String key = line.substring(0, line.indexOf(EQUALS));
					String value = line.substring(line.indexOf(EQUALS) + 1);
					if (key.equals(STATUS) && value.equals("403")) {
						new GalleryConnectionException(
								"The file was received, but could not be processed or added to the album.");
					}
					properties.put(key, value);
				}
			}
			rd.close();
		} catch (IOException e) {
			// something went wrong, let's throw the info to the UI
			// Log.e(TAG, "IOException" + e.getMessage());
			throw new GalleryConnectionException("IOException" + e.getMessage());
		} catch (IllegalArgumentException e) {
			// the url is not correct
			throw new GalleryConnectionException("Url is not correct : "
					+ e.getMessage());
		}
		return properties;
	}

}
