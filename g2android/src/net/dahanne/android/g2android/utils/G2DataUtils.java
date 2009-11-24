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
public class G2DataUtils {

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
						picture.setHidden(Boolean.valueOf(entry.getValue()));
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

	public static Album findAlbumFromAlbumName(Album rootAlbum, int i) {
		if (rootAlbum.getName() == i) {
			return rootAlbum;
		}
		for (Album album : rootAlbum.getChildren()) {
			if (album.getName() == i) {
				return album;
			}
			Album fromAlbumName = findAlbumFromAlbumName(album, i);
			if (fromAlbumName != null) {
				return fromAlbumName;
			}

		}
		return null;
	}

	public static Album retrieveRootAlbumAndItsHierarchy(String galleryUrl)
			throws GalleryConnectionException {
		HashMap<String, String> albumsProperties = G2ConnectionUtils.fetchAlbums(galleryUrl);

		Map<Integer, Album> nonSortedAlbums = extractAlbumFromProperties(albumsProperties);
		Album rootAlbum = organizeAlbumsHierarchy(nonSortedAlbums);
		return rootAlbum;
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
