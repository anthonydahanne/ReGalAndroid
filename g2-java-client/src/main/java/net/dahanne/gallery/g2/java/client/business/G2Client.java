/**
 *  g2-java-client, a Menalto Gallery2 Java Client API
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

package net.dahanne.gallery.g2.java.client.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;
import net.dahanne.gallery.g2.java.client.ssl.FakeSocketFactory;
import net.dahanne.gallery.g2.java.client.utils.G2ConvertUtils;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class G2Client {

	private static final String UPLOAD_FAILED = "Upload Failed";
	private static final String FAILED = "failed";
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final String EQUALS = "=";
	/**
	 * Final static constants
	 */
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
	private static final String USER_AGENT = "User-Agent";
	static final String GR_STAT_SUCCESS = "0";

	/**
	 * instance variables
	 */
	private String authToken;
	private final CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
	private final List<Cookie> sessionCookies = new ArrayList<Cookie>();

	private final DefaultHttpClient defaultHttpClient;
	private G2Album rootAlbum;
	private final Logger logger = LoggerFactory.getLogger(G2Client.class);
	private final BasicHeader basicHeader;


	public G2Client(String userAgent) {
		this.basicHeader = new BasicHeader(USER_AGENT,userAgent);
		sessionCookies.add(new BasicClientCookie("", ""));
		// the httpclient initialization is heavy, we create one for
		// G2ConnectionUtils
		defaultHttpClient = createHttpClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.dahanne.android.regalandroid.utils.RemoteGallery#fetchImages(java
	 * .lang.String, int)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.dahanne.android.regalandroid.utils.RemoteGallery#fetchAlbums(java
	 * .lang.String)
	 */
	public HashMap<String, String> fetchAlbums(String galleryUrl)
			throws GalleryConnectionException {
		List<NameValuePair> nameValuePairsFetchAlbums = new ArrayList<NameValuePair>();
		nameValuePairsFetchAlbums.add(FETCH_ALBUMS_CMD_NAME_VALUE_PAIR);

		HashMap<String, String> properties = sendCommandToGallery(galleryUrl,
				nameValuePairsFetchAlbums, null);
		return properties;

	}

	public void loginToGallery(String galleryUrl, String user, String password)
			throws ImpossibleToLoginException {
		// we reset the last login
		sessionCookies.clear();
		sessionCookies.add(new BasicClientCookie("", ""));

		List<NameValuePair> sb = new ArrayList<NameValuePair>();
		sb.add(LOGIN_CMD_NAME_VALUE_PAIR);
		sb.add(new BasicNameValuePair("g2_form[uname]", "" + user));
		sb.add(new BasicNameValuePair("g2_form[password]", "" + password));
		HashMap<String, String> properties;
		try {
			properties = sendCommandToGallery(galleryUrl, sb, null);
		} catch (GalleryConnectionException e) {
			throw new ImpossibleToLoginException(e);
		}
		// auth_token retrieval, used by G2 against cross site forgery
		authToken = null;
		if (properties.get("status") != null) {
			if (properties.get("status").equals(GR_STAT_SUCCESS)) {
				authToken = properties.get("auth_token");
			} else {
				throw new ImpossibleToLoginException(
						properties.get("status_text"));
			}
		}
		//the login request did not return properties; issue #24 
		if(properties.isEmpty()){
			throw new ImpossibleToLoginException("The Gallery did not return login properties; check your gallery installation and/or your settings" );
		}

	}

	public InputStream getInputStreamFromUrl(String url)
			throws GalleryConnectionException {
		InputStream content = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			httpGet.setHeader(basicHeader);
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
			if(authToken!=null){
				multiPartEntity.addPart("g2_authToken", new StringBody(authToken,
						UTF_8));
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.dahanne.android.regalandroid.utils.RemoteGallery#createNewAlbum(java
	 * .lang.String, int, java.lang.String, java.lang.String, java.lang.String)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.dahanne.android.regalandroid.utils.RemoteGallery#sendImageToGallery
	 * (java.lang.String, int, java.io.File, java.lang.String, java.lang.String,
	 * java.lang.String)
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
		logger.debug("galleryUrl : {} -- nameValuePairsForThisCommand : {}",galleryUrl,nameValuePairsForThisCommand);
		
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
			if (!G2ConvertUtils.isEmbeddedGallery(galleryUrl)) {
				correctedGalleryUrl = galleryUrl + "/" + MAIN_PHP;
			}

			HttpPost httpPost = new HttpPost(correctedGalleryUrl);

			// if we send an image to the gallery, we pass it to the gallery
			// through multipartEntity
			httpPost.setHeader(basicHeader);
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
			int status = response.getStatusLine().getStatusCode();
			if(status>=400 && status<=500){
				logger.debug("status is an error : {}",status);
				throw new GalleryConnectionException("The server returned an error : "+status);
			}
			
			
			InputStream content = response.getEntity().getContent();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					content), 4096);
			// do not forget the cookies
			sessionCookies.addAll(cookies);
			logger.debug("Beginning reading the response");
			String line;
			boolean gr2ProtoStringWasFound = false;
			while ((line = rd.readLine()) != null) {
				logger.debug(line);
				if (line.contains(GR2PROTO)) {
					gr2ProtoStringWasFound = true;
				}
				if (line.contains(EQUALS) && gr2ProtoStringWasFound) {
					String key = line.substring(0, line.indexOf(EQUALS));
					String value = line.substring(line.indexOf(EQUALS) + 1);
					if (key.equals(STATUS) && value.equals("403")) {
						throw new GalleryConnectionException(
								"The file was received, but could not be processed or added to the album.");
					}
					properties.put(key, value);
				}
			}
			logger.debug("Ending reading the response");
			rd.close();
		} catch (IOException e) {
			// something went wrong, let's throw the info to the UI
			throw new GalleryConnectionException(e);
		} catch (IllegalArgumentException e) {
			// the url is not correct
			throw new GalleryConnectionException(e);
		}
		return properties;
	}

	/**
	 * This is where we convert the infos from the gallery to G2Picture objects
	 * 
	 * @param fetchImages
	 * @return
	 * @throws GalleryConnectionException 
	 */
	public Collection<G2Picture> extractG2PicturesFromProperties(
			HashMap<String, String> fetchImages) throws GalleryConnectionException {
		Map<Integer, G2Picture> picturesMap = new TreeMap<Integer, G2Picture>();
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
						picture.setName( entry.getValue());
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
						picture.setHidden(Boolean.valueOf(entry.getValue()));
					} 

				} catch (NumberFormatException nfe) {
					throw new GalleryConnectionException(nfe);
				}
			}
		}
		return picturesMap.values();

	}


	public Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException {
		HashMap<String, String> albumsProperties = this.fetchAlbums(galleryUrl);

		Map<Integer, G2Album> nonSortedAlbums = extractAlbumFromProperties(albumsProperties);
		
		
		Map<Integer, Album> albumss  = new HashMap<Integer, Album>();
		for (Integer key : nonSortedAlbums.keySet()) {
			albumss.put(key, G2ConvertUtils.g2AlbumToAlbum(nonSortedAlbums.get(key)));
		}
		
		
		return albumss;
	}

	/**
	 * 
	 * From an HashMap of albumProperties (obtained from the remote gallery),
	 * returns a List of Albums
	 * 
	 * @param albumsProperties
	 * @return List<Album>
	 * @throws GalleryConnectionException 
	 */
	public Map<Integer, G2Album> extractAlbumFromProperties(
			HashMap<String, String> albumsProperties) throws GalleryConnectionException {
		int albumNumber = 0;
		Map<Integer, G2Album> albumsMap = new HashMap<Integer, G2Album>();
		List<Integer> tmpAlbumNumbers = new ArrayList<Integer>();

		for (Entry<String, String> entry : albumsProperties.entrySet()) {
			if (entry.getKey().contains("album")
					&& !entry.getKey().contains("debug")
					&& !entry.getKey().contains("album_count")) {
				// what is the album id of this field ?
				albumNumber = new Integer(entry.getKey().substring(
						entry.getKey().lastIndexOf(".") + 1));
				G2Album album = null;
				// a new album, let's create it!
				if (!tmpAlbumNumbers.contains(albumNumber)) {
					album = new G2Album();
					album.setId(albumNumber);
					albumsMap.put(albumNumber, album);
					tmpAlbumNumbers.add(albumNumber);

				}
				// a known album, let's get it back
				else {
					album = albumsMap.get(albumNumber);
				}
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
					throw new GalleryConnectionException(nfe);
				}
			}
		}

		return albumsMap;

	}

	/**
	 * This method converts G2Album into Album and then organize the album hierarchy
	 * 
	 * @param albums
	 * @return
	 */
	public Album organizeAlbumsHierarchy(Map<Integer, G2Album> albums) {
		Album rootAlbum = null;
		Map<Integer, Album> albumss  = new TreeMap<Integer, Album>();
		for (Integer key : albums.keySet()) {
			albumss.put(key, G2ConvertUtils.g2AlbumToAlbum(albums.get(key)));
		}
		
		
		for (Album album : albumss.values()) {
			
			// set the root album as soon as we discover it
			if (album.getParentName() == 0) {
				rootAlbum = album;
			}

			int parentName = album.getParentName();
			// look for the parent id
			int parentId = 0;
			for (Album album2 : albumss.values()) {
				if (album2.getName() == parentName) {
					parentId = album2.getId();
					break;
				}
			}
			Album parent = albumss.get(parentId);
			album.setParent(parent);
			if (parent != null) {
				parent.getSubAlbums().add(album);
			}
		}

		return rootAlbum;

	}


	public void setRootAlbum(G2Album rootAlbum) {
		this.rootAlbum = rootAlbum;
	}

	public G2Album getRootAlbum() {
		return rootAlbum;
	}

	
	
}
