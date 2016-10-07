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
import net.dahanne.gallery.commons.utils.AlbumUtils;
import net.dahanne.gallery.g2.java.client.business.G2Client;
import net.dahanne.gallery.g2.java.client.model.G2Album;
import net.dahanne.gallery.g2.java.client.model.G2Picture;
import net.dahanne.gallery.g2.java.client.utils.G2ConvertUtils;

public class G2Connection implements RemoteGallery {

	private final G2Client client;
	private Album rootAlbum;
	private final String galleryUrl;
	private final String username;
	private final String password;

	public G2Connection(String galleryUrl, String username, String password, String userAgent) {
		client = new G2Client(userAgent);
		this.galleryUrl = galleryUrl;
		this.username = username;
		this.password = password;
	}

	@Override
	public Collection<Picture> getPicturesFromAlbum(int albumName)
			throws GalleryConnectionException {

		List<Picture> pictures = new ArrayList<Picture>();

		Collection<G2Picture> extractG2PicturesFromProperties = client
				.extractG2PicturesFromProperties(client.fetchImages(galleryUrl,
						albumName));

		for (G2Picture g2Picture : extractG2PicturesFromProperties) {
			pictures.add(G2ConvertUtils.g2PictureToPicture(g2Picture,
					galleryUrl));
		}

		return pictures;
	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(int parentAlbumId)
			throws GalleryConnectionException {
		if (rootAlbum == null) {
			// it means we already have the list of albums
			HashMap<String, String> fetchAlbums = client
					.fetchAlbums(galleryUrl);
			Map<Integer, G2Album> albumsFromProperties = client
					.extractAlbumFromProperties(fetchAlbums);
			rootAlbum = client.organizeAlbumsHierarchy(albumsFromProperties);
		}
		//no root album, the gallery did not answer correctly issue #24 
		if(rootAlbum==null){
			throw new GalleryConnectionException("The Gallery did not return properties; check your gallery installation and/or your settings" );
		}
		
		
		// if 0 is specified as the parentAlbumId, it means we have to return
		// the rootAlbum
		if (parentAlbumId == 0) {
			rootAlbum.getPictures().addAll(
					getPicturesFromAlbum( rootAlbum.getName()));
			return rootAlbum;
		}
		Album findAlbumFromAlbumName = AlbumUtils.findAlbumFromAlbumName(
				rootAlbum, parentAlbumId);
		if (findAlbumFromAlbumName.getPictures().size() > 0) {
			findAlbumFromAlbumName.getPictures().clear();
		}

		findAlbumFromAlbumName.getPictures().addAll(
				getPicturesFromAlbum(parentAlbumId));
		return findAlbumFromAlbumName;
	}

	@Override
	public void loginToGallery()
			throws ImpossibleToLoginException {
		client.loginToGallery(galleryUrl, username, password);

	}

	@Override
	public int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException {
		return client.createNewAlbum(galleryUrl, parentAlbumName, albumName,
				albumTitle, albumDescription);
	}

	@Override
	public int uploadPictureToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException {
		return client.sendImageToGallery(galleryUrl, albumName, imageFile,
				imageName, summary, description);
	}

	@Override
	public InputStream getInputStreamFromUrl(String imageUrl)
			throws GalleryConnectionException {
		return client.getInputStreamFromUrl(imageUrl);
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException {
		return client.getAllAlbums(galleryUrl);
	}


}
