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
import java.util.List;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.GalleryOperationNotYetSupportedException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.remote.RemoteGallery;
import net.dahanne.gallery3.client.business.G3Client;
import net.dahanne.gallery3.client.business.exceptions.G3GalleryException;
import net.dahanne.gallery3.client.model.Item;
import net.dahanne.gallery3.client.utils.G3ConvertUtils;

public class G3Connection implements RemoteGallery {

	private final G3Client client;

	public G3Connection(String galleryUrl, String username, String password) {
		client = new G3Client(galleryUrl);
		client.setUsername(username);
		client.setPassword(password);

	}

	@Override
	public int createNewAlbum(String arg0, int arg1, String arg2, String arg3,
			String arg4) throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available for G3 yet");
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String arg0)
			throws GalleryConnectionException {

		throw new GalleryOperationNotYetSupportedException(
				"Not available for G3 yet");
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
	public Collection<Picture> getPicturesFromAlbum(int albumName) throws GalleryConnectionException {
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
	public int uploadPictureToGallery(String arg0, int arg1, File arg2,
			String arg3, String arg4, String arg5)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available for G3 yet");
	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(
			int parentAlbumId) throws GalleryConnectionException {

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
