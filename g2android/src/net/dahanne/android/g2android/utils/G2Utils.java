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
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.dahanne.android.g2android.model.Album;
import net.dahanne.android.g2android.model.G2Picture;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * @author Anthony Dahanne
 * 
 */
public class G2Utils {

	private static final String TAG = "G2Utils";

	public static HashMap<String, String> fetchImages(String galleryUrl,
			int albumName) {
		HashMap<String, String> properties = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		sb.append("g2_form[cmd]=fetch-album-images&g2_form[set_albumName]="
				+ albumName);

		URL url;
		try {
			url = new URL(galleryUrl + "/"
					+ "main.php?g2_controller=remote:GalleryRemote");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");

			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(sb.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()), 4096);
			String line;
			while ((line = rd.readLine()) != null) {
				// Log.d(TAG, line);
				if (line.contains("=")) {
					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=") + 1);
					properties.put(key, value);
				}
			}
			wr.close();
			rd.close();
		} catch (Exception e) {

			// Log.d(TAG,e.getMessage());
		}
		return properties;
	}

	public static HashMap<String, String> fetchAlbums(String galleryUrl) {
		HashMap<String, String> properties = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		URL url;
		try {
			url = new URL(
					galleryUrl
							+ "/"
							+ "main.php?g2_controller=remote:GalleryRemote&g2_form[cmd]=fetch-albums");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("user-agent", "G2Android");

			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(sb.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()), 4096);
			String line;
			while ((line = rd.readLine()) != null) {
				// Log.d(TAG, line);
				if (line.contains("=")) {
					String key = line.substring(0, line.indexOf("="));
					String value = line.substring(line.indexOf("=") + 1);
					properties.put(key, value);
				}
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
			// Log.d(TAG,e.getMessage());
		}
		return properties;

	}

	public static Collection<G2Picture> extractG2PicturesFromProperties(
			HashMap<String, String> fetchImages) {
		Map<Integer, G2Picture> picturesMap = new HashMap<Integer, G2Picture>();
		List<Integer> tmpImageNumbers = new ArrayList<Integer>();
		long depart = System.currentTimeMillis();
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
						picture.setName(entry.getValue());
					} else if (entry.getKey().contains("image.raw_width.")) {
						picture.setRawWidth(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_height.")) {
						picture.setRawHeight(new Integer(entry.getValue()));
					} else if (entry.getKey().contains("image.raw_filesize.")) {
						picture.setRawFilesize(new Integer(entry.getValue()));
					}

				} catch (NumberFormatException nfe) {
					System.out.println("problem dealing with imageNumber :"
							+ imageNumber);

				}
			}
		}
		long temps = System.currentTimeMillis() - depart;
		System.out.println("temps : " + temps);

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
					System.out.println("problem dealing with albumNumber :"
							+ albumNumber);

				}
			}
		}

		return albumsMap;

	}

	/**
	 * Check whether the galleryUrl provided is valid or not
	 * 
	 * @param galleryUrl
	 * @return boolean
	 */
	public static boolean checkGalleryUrlIsValid(String galleryUrl) {
		StringBuffer sb = new StringBuffer();
		URL url;
		try {
			url = new URL(
					galleryUrl
							+ "/"
							+ "main.php?g2_controller=remote:GalleryRemote&g2_form[cmd]=fetch-albums");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "text/xml");
			conn.setRequestProperty("user-agent", "G2Android");

			OutputStreamWriter wr = new OutputStreamWriter(conn
					.getOutputStream());
			wr.write(sb.toString());
			wr.flush();

			BufferedReader rd = new BufferedReader(new InputStreamReader(conn
					.getInputStream()), 4096);
			String line;
			while ((line = rd.readLine()) != null) {
				// Log.d(TAG, line);
				if (line.contains("#__GR2PROTO__")) {
					return true;
				}
			}
			wr.close();
			rd.close();
		} catch (Exception e) {
			return false;
		}
		return false;

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
}
