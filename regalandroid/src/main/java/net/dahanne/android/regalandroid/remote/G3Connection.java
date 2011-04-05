/**
 *  ReGalAndroid, a gallery client for Android, supporting G2, G3, etc...
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

package net.dahanne.android.regalandroid.remote;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import net.dahanne.gallery3.client.business.G3Client;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.model.Entity;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.G3ConvertUtils;

public class G3Connection implements RemoteGallery {

	private final G3Client client;

	public G3Connection(String galleryUrl, String username, String password,
			String userAgent) {
		client = new G3Client(galleryUrl, userAgent);
		client.setUsername(username);
		client.setPassword(password);
	}

	@Override
	public int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException {
		try {
			Entity albumToCreate = new Entity();
			albumToCreate.setName(albumName);
			albumToCreate.setTitle(albumTitle);
			albumToCreate.setParent(parentAlbumName);
			return client.createItem(albumToCreate, null);
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException {
		Map<Integer, Album> map = new HashMap<Integer, Album>();
		try {
			List<Item> albumAndSubAlbums = client.getAlbumAndSubAlbums(1);
			for (Item item : albumAndSubAlbums) {
				Album itemToAlbum = G3ConvertUtils.itemToAlbum(item);
				map.put(itemToAlbum.getId(), itemToAlbum);
			}
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return map;

	}

	@Override
	public InputStream getInputStreamFromUrl(String umageUrl)
			throws GalleryConnectionException {
		try {
			return client.getPhotoInputStream(umageUrl);
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
	}

	@Override
	public Collection<Picture> getPicturesFromAlbum(int albumName)
			throws GalleryConnectionException {
		Collection<Picture> pictures = new ArrayList<Picture>();
		try {
			List<Item> picturesAsItems = client.getPictures(albumName);
			for (Item item : picturesAsItems) {
				pictures.add(G3ConvertUtils.itemToPicture(item));
			}

		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return pictures;
	}

	@Override
	public void loginToGallery() throws ImpossibleToLoginException {
		try {
			client.getApiKey();
		} catch (G3GalleryException e) {
			throw new ImpossibleToLoginException(e);
		}

	}

	@Override
	public int uploadPictureToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException {
		int createItem = 0;
		try {
			Entity photoToCreate = new Entity();
			photoToCreate.setName(imageFile.getName());
			photoToCreate.setTitle(imageName);
			photoToCreate.setParent(albumName);
			createItem = client.createItem(photoToCreate, imageFile);
		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return createItem;
	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(int parentAlbumId)
			throws GalleryConnectionException {

		// dirty hack to load the root album, id 1 in G3
		if (parentAlbumId == 0) {
			parentAlbumId = 1;
		}

		Album parentAlbum = null;
		try {
			List<Item> albumAndSubAlbums = client
					.getAlbumAndSubAlbumsAndPictures(parentAlbumId);
			// the first is item is an album
			parentAlbum = G3ConvertUtils.itemToAlbum(albumAndSubAlbums.get(0));
			for (Item item : albumAndSubAlbums) {
				if (item == albumAndSubAlbums.get(0)) {
					// no need to add the first one, it is the parent
					continue;
				}
				if (item.getEntity().getType().equals("album")) {
					Album itemToAlbum = G3ConvertUtils.itemToAlbum(item);
					itemToAlbum.setParentName(parentAlbum.getName());
					itemToAlbum.setParent(parentAlbum);
					parentAlbum.getSubAlbums().add(itemToAlbum);
				} else {
					parentAlbum.getPictures().add(
							G3ConvertUtils.itemToPicture(item));
				}
			}

		} catch (G3GalleryException e) {
			throw new GalleryConnectionException(e);
		}
		return parentAlbum;
	}

}
