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
import java.util.Collection;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;
import net.dahanne.gallery.commons.remote.GalleryConnectionException;
import net.dahanne.gallery.commons.remote.GalleryOperationNotYetSupportedException;
import net.dahanne.gallery.commons.remote.ImpossibleToLoginException;
import net.dahanne.gallery.commons.remote.RemoteGallery;

public class PiwigoConnection implements RemoteGallery {


	public PiwigoConnection(String galleryUrl, String username, String password) {

	}

	@Override
	public int createNewAlbum(String arg0, int arg1, String arg2, String arPiwigo,
			String arg4) throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String arg0)
			throws GalleryConnectionException {

		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public InputStream getInputStreamFromUrl(String umageUrl)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
		"Not available for Piwigo yet");
	}

	@Override
	public Collection<Picture> getPicturesFromAlbum(int albumName) throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
		"Not available for Piwigo yet");
	}

	@Override
	public void loginToGallery() throws ImpossibleToLoginException {
		throw new ImpossibleToLoginException(
		"Not available for Piwigo yet");

	}

	@Override
	public int uploadPictureToGallery(String arg0, int arg1, File arg2,
			String arPiwigo, String arg4, String arg5)
			throws GalleryConnectionException {
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(
			int parentAlbumId) throws GalleryConnectionException {

		throw new GalleryOperationNotYetSupportedException(
		"Not available for Piwigo yet");
	}

}
