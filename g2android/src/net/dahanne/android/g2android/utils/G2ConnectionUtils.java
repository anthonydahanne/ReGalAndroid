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
public class G2ConnectionUtils {

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
	private static final BasicNameValuePair IMAGE_PROPERTIES_CMD_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[cmd]", "image-properties");
	private static final String GR2PROTO = "#__GR2PROTO__";
	private static final BasicNameValuePair G2_CONTROLLER_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_controller", "remote.GalleryRemote");
	private static final BasicNameValuePair PROTOCOL_VERSION_NAME_VALUE_PAIR = new BasicNameValuePair(
			"g2_form[protocol_version]", "2.0");
	private static final String MAIN_PHP = "main.php";
	private static final String USER_AGENT_VALUE = "G2Android Version 1.2.1";
	private static final String USER_AGENT = "User-Agent";
	private static final String TAG = "G2Utils";
	static final String GR_STAT_SUCCESS = "0";

	/**
	 * Static variables
	 */
	private static String authToken;
	private static final BasicNameValuePair G2_AUTHTOKEN_VALUE_PAIR = new BasicNameValuePair(
			"g2_authToken", authToken);
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
				nameValuePairsFetchImages);
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
				nameValuePairsFetchAlbums);
		return properties;

	}

	/**
	 * This is where we convert the infos from the gallery to G2Picture objects
	 * 
	 * @param fetchImages
	 * @return
	 */
	public static Collection<G2Picture> extractG2PicturesFromProperties(
			HashMap<String, String> fetchImages) {
		Map<Integer, G2Picture> picturesMap = new HashMap<Integer, G2Picture>();
		List<Integer> tmpImageNumbers = new ArrayList<Integer>();
		int imageNumber = 0;
		for (Entry<String, String> entry : fetchImages.entrySet()) {
			if (entry.getKey().contains("image")
					&& !entry.getKey().contains("image_count")) {
				// what is the picture id of this field ?
				imageNumber = new Integer(entry.getKey().substring(
						entry.getKey().lastIndexOf(".") + 1));
				G2Picture picture = null;
				// a new picture, let's create it!
				if (!tmpImageNumbers.contains(imageNumber)) {
					picture = new G2Picture();
					picture.setId(imageNumber);
					picturesMap.put(imageNumber, picture);
					tmpImageNumbers.add(imageNumber);

				}
				// a known picture, let's get it back
				else {
					picture = picturesMap.get(imageNumber);
				}

				// TODO : change this, using album_count, loop on it, as we know
				// that the number at the end is between 1 and album_count
				try {
					if (entry.getKey().contains("image.title.")) {
						picture.setTitle(entry.getValue());
					} else if (entry.getKey().contains("image.thumbName.")) {
						picture.setThumbName(entry.getValue());
					} else if (entry.getKey().contains("image.thumb_width.")) {
						picture.setThumbWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.thumb_height.")) {
						picture.setThumbHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.resizedName.")) {
						picture.setResizedName(entry.getValue());
					} else if (entry.getKey().contains("image.resized_width.")) {
						picture.setResizedWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.resized_height.")) {
						picture.setResizedHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.name.")) {
						picture.setName(entry.getValue());
					} else if (entry.getKey().contains("image.raw_width.")) {
						picture.setRawWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_height.")) {
						picture.setRawHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_filesize.")) {
						picture.setRawFilesize(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.caption.")) {
						picture.setCaption(entry.getValue());
					} else if (entry.getKey().contains("image.forceExtension.")) {
						picture.setForceExtension(entry.getValue());
					} else if (entry.getKey().contains("image.hidden.")) {
						picture.setHidden(new Boolean(entry.getValue()));
					} else if (entry.getKey().contains("image.clicks.")) {
						picture.setImageClicks(new Integer(entry.getValue()));
					}
					// else if (entry.getKey().contains(
					// "image.capturedate.year.")) {
					// picture.setCaptureDateYear(entry.getValue());
					// } else if (entry.getKey()
					// .contains("image.capturedate.mon.")) {
					// picture.setCaptureDateMonth(entry.getValue());
					// } else if (entry.getKey().contains(
					// "image.capturedate.mday.")) {
					// picture.setCaptureDateDay(entry.getValue());
					// } else if (entry.getKey().contains(
					// "image.capturedate.hours.")) {
					// picture.setCaptureDateHour(entry.getValue());
					// } else if (entry.getKey().contains(
					// "image.capturedate.minutes.")) {
					// picture.setCaptureDateMinute(entry.getValue());
					// } else if (entry.getKey().contains(
					// "image.capturedate.seconds.")) {
					// picture.setCaptureDateSecond(entry.getValue());
					// }

				} catch (NumberFormatException nfe) {
					// System.out.println("problem dealing with imageNumber :"
					// + imageNumber);

				}
			}
		}
		return picturesMap.values();

	}

	/**
	 * 
	 * From an HashMap of albumProperties (obtained from the remote gallery),
	 * returns a List of Albums
	 * 
	 * @param albumsProperties
	 * @return List<Album>
	 */
	public static Map<Integer, Album> extractAlbumFromProperties(
			HashMap<String, String> albumsProperties) {
		int albumNumber = 0;
		Map<Integer, Album> albumsMap = new HashMap<Integer, Album>();
		List<Integer> tmpAlbumNumbers = new ArrayList<Integer>();

		for (Entry<String, String> entry : albumsProperties.entrySet()) {
			if (entry.getKey().contains("album")
					&& !entry.getKey().contains("debug")
					&& !entry.getKey().contains("album_count")) {
				// what is the album id of this field ?
				albumNumber = new Integer(entry.getKey().substring(
						entry.getKey().lastIndexOf(".") + 1));
				Album album = null;
				// a new album, let's create it!
				if (!tmpAlbumNumbers.contains(albumNumber)) {
					album = new Album();
					album.setId(albumNumber);
					albumsMap.put(albumNumber, album);
					tmpAlbumNumbers.add(albumNumber);

				}
				// a known album, let's get it back
				else {
					album = albumsMap.get(albumNumber);
				}
				// TODO : use album_count for the loop
				try {
					if (entry.getKey().contains("album.title.")) {

						String title = StringEscapeUtils.unescapeHtml(entry
								.getValue());
						title = StringEscapeUtils.unescapeJava(title);
						album.setTitle(title);
					} else if (entry.getKey().contains("album.name.")) {
						album.setName(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("album.summary.")) {
						album.setSummary(entry.getValue());
					} else if (entry.getKey().contains("album.parent.")) {
						album.setParentName(new Integer(entry.getValue()));
					} else if (entry.getKey().contains(
							"album.info.extrafields.")) {
						album.setExtrafields(entry.getValue());
					}

				} catch (NumberFormatException nfe) {
					// System.out.println("problem dealing with albumNumber :"
					// + albumNumber);

				}
			}
		}

		return albumsMap;

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
				nameValuePairsFetchAlbums);

		return properties.isEmpty() ? false : true;

	}

	/**
	 * @param albums
	 * @return
	 */
	public static Album organizeAlbumsHierarchy(Map<Integer, Album> albums) {
		Album rootAlbum = null;

		for (Album album : albums.values()) {
			// set the root album as soon as we discover it
			if (album.getParentName() == 0) {
				rootAlbum = album;
			}

			int parentName = album.getParentName();
			// look for the parent id
			int parentId = 0;
			for (Album album2 : albums.values()) {
				if (album2.getName() == parentName) {
					parentId = album2.getId();
					break;
				}
			}
			Album parent = albums.get(parentId);
			album.setParent(parent);
			if (parent != null) {
				parent.getChildren().add(album);
			}
		}

		return rootAlbum;

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
				sb);
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
	 * @param galleryHost
	 * @param galleryPath
	 * @param galleryPort
	 * @param nameValuePairsForThisCommand
	 * @return
	 * @throws GalleryConnectionException
	 */
	private static HashMap<String, String> sendCommandToGallery(
			String galleryUrl, List<NameValuePair> nameValuePairsForThisCommand)
			throws GalleryConnectionException {
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
			HttpPost httpPost = new HttpPost(galleryUrl + "/" + MAIN_PHP);
			httpPost.setHeader(new BasicHeader(USER_AGENT, USER_AGENT_VALUE));
			// Setting the cookie
			httpPost.setHeader(getCookieHeader(cookieSpecBase));
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(G2_CONTROLLER_NAME_VALUE_PAIR);
			nameValuePairs
					.add(new BasicNameValuePair("g2_authToken", authToken));
			nameValuePairs.add(PROTOCOL_VERSION_NAME_VALUE_PAIR);
			nameValuePairs.addAll(nameValuePairsForThisCommand);
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

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
				// Log.d(TAG, line);
				// System.out.println(line);
				if (line.contains(GR2PROTO)) {
					gr2ProtoStringWasFound = true;
				}
				if (line.contains("=") && gr2ProtoStringWasFound) {
					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=") + 1);
					properties.put(key, value);
				}
			}
			rd.close();
		} catch (Exception e) {
			// something went wrong, let's throw the info to the UI
			throw new GalleryConnectionException(e.getMessage());
			// Log.d(TAG,e.getMessage());
		}
		return properties;
	}

	static String getPathFromUrl(String galleryUrl) {
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
		}
		return galleryPath;
	}

	static String getHostFromUrl(String galleryUrl) {
		String galleryHost = "";
		if (galleryUrl != null || StringUtils.isNotBlank(galleryUrl)) {
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
			httpGet.setHeader(new BasicHeader(USER_AGENT, USER_AGENT_VALUE));
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

	/**
	 * @param imageFile
	 * @return
	 * @throws GalleryConnectionException
	 */
	public static int sendImageToGallery(String galleryUrl, int albumName,
			File imageFile) throws GalleryConnectionException {

		int itemName = 0;
		try {
			PostMethod filePost = new PostMethod(galleryUrl + "/" + MAIN_PHP);
			String name = imageFile.getName().substring(0,
					imageFile.getName().indexOf("."));
			Part[] parts = {
					new StringPart("g2_controller", "remote.GalleryRemote"),
					new StringPart("g2_form[cmd]", "add-item"),
					new StringPart("g2_form[set_albumName]", "" + albumName),
					new StringPart("g2_userfile_name", name),
					new StringPart("g2_authToken", authToken),
					new StringPart("g2_form[force_filename]", name),
					new StringPart("g2_form[caption]", name),
					new FilePart("g2_userfile", imageFile) };
			filePost.setRequestEntity(new MultipartRequestEntity(parts,
					filePost.getParams()));
			String inlineCookies = new String();
			for (Cookie cookie : sessionCookies) {
				inlineCookies = inlineCookies + cookie.getName() + "="
						+ cookie.getValue() + ";";
			}
			filePost.setRequestHeader("Cookie", inlineCookies);
			org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
			int status = client.executeMethod(filePost);

			String string = filePost.getResponseBodyAsString();
			// System.out.println(string);
			if (string == null || string.contains("status=403")) {
				throw new Exception("Upload Failed");
			}

			InputStream responseBodyAsStream = filePost
					.getResponseBodyAsStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					responseBodyAsStream), 4096);

			String line;
			int statusCode = 0;
			String statusText;
			boolean gr2ProtoStringWasFound = false;
			while ((line = rd.readLine()) != null) {
				// Log.d(TAG, line);
				if (line.contains(GR2PROTO)) {
					gr2ProtoStringWasFound = true;
				}
				if (line.contains("=") && gr2ProtoStringWasFound) {

					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=") + 1);
					if ("item_name".equals(key)) {
						itemName = new Integer(value).intValue();
					}
					if ("status".equals(key)) {
						statusCode = new Integer(value).intValue();
					}
					// something went wrong
					if (statusCode != 0 && "status_text".equals(key)) {
						statusText = value;
						throw new Exception(value);
					}
				}
			}
			rd.close();

		} catch (Exception e) {
			// something went wrong, let's throw the info to the UI
			throw new GalleryConnectionException(e.getMessage());
		}
		return itemName;
	}

	/**
	 * @param galleryHost
	 * @param galleryPath
	 * @param galleryPort
	 * @param i
	 * @param albumName
	 * @param albumTitle
	 * @param albumDescription
	 * @return
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
				nameValuePairsFetchImages);
		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().equals("album_name")) {
				newAlbumName = new Integer(entry.getValue()).intValue();
			}
		}
		return newAlbumName;
	}

	public static G2Picture getImageProperties(String galleryUrl, long itemId)
			throws GalleryConnectionException {
		List<NameValuePair> nameValuePairsFetchImages = new ArrayList<NameValuePair>();
		nameValuePairsFetchImages.add(IMAGE_PROPERTIES_CMD_NAME_VALUE_PAIR);
		nameValuePairsFetchImages.add(new BasicNameValuePair("g2_form[id]", ""
				+ itemId));

		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchImages);
		G2Picture pictureFromProperties = extractG2PicturePropertiesFromProperties(
				properties, itemId);
		return pictureFromProperties;

	}

	public static G2Picture extractG2PicturePropertiesFromProperties(
			HashMap<String, String> properties, long itemId) {
		G2Picture picture = null;
		picture = new G2Picture();
		picture.setId(itemId);

		for (Entry<String, String> entry : properties.entrySet()) {
			if (entry.getKey().contains("image")) {
				// that the number at the end is between 1 and album_count
				try {
					if (entry.getKey().contains("image.title")) {
						picture.setTitle(entry.getValue());
					} else if (entry.getKey().contains("image.thumbName")) {
						picture.setThumbName(entry.getValue());
					} else if (entry.getKey().contains("image.thumb_width")) {
						picture.setThumbWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.thumb_height")) {
						picture.setThumbHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.resizedName")) {
						picture.setResizedName(entry.getValue());
					} else if (entry.getKey().contains("image.resized_width")) {
						picture.setResizedWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.resized_height")) {
						picture.setResizedHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.name")) {
						picture.setName(entry.getValue());
					} else if (entry.getKey().contains("image.raw_width")) {
						picture.setRawWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_height")) {
						picture.setRawHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_filesize")) {
						picture.setRawFilesize(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.caption")) {
						picture.setCaption(entry.getValue());
					} else if (entry.getKey().contains("image.forceExtension")) {
						picture.setForceExtension(entry.getValue());
					} else if (entry.getKey().contains("image.hidden")) {
						picture.setHidden(Boolean.getBoolean(entry.getValue()));
					}

				} catch (NumberFormatException nfe) {
					// System.out.println("problem dealing with imageNumber :"
					// + imageNumber);

				}

			}
		}
		return picture;
	}

}
