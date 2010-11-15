/**
 *  commons-gallery, a common API module for ReGalAndroid
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

package net.dahanne.gallery.commons.remote;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;

public interface RemoteGallery {

	/**
	 * The login method, cookie handling is located in sendCommandToGallery
	 * method. The credentials used (username/password) are fields in the implementation
	 * 
	 * @return
	 * @throws ImpossibleToLoginException
	 */
	public abstract void loginToGallery() throws ImpossibleToLoginException;

	/**
	 * Create a new subAlbum albumName in the parent album parentAlbumName, with
	 * the given albumTitle and albumDescription
	 * 
	 * @param galleryUrl
	 * @param parentAlbumName
	 * @param albumName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public abstract int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException;

	/**
	 * Uploads a picture to the album albumName, with the given imageName,
	 * summary and description
	 * 
	 * @param galleryUrl
	 * @param parentAlbumName
	 * @param albumName
	 * @param imageName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public abstract int uploadPictureToGallery(String galleryUrl,
			int albumName, File imageFile, String imageName, String summary,
			String description) throws GalleryConnectionException;

	/**
	 * 
	 * Return the InputStream associated to the imageUrl specified
	 * 
	 * @param imageUrl
	 * @return
	 * @throws GalleryConnectionException
	 */
	public abstract InputStream getInputStreamFromUrl(String imageUrl)
			throws GalleryConnectionException;

	/**
	 * 
	 * Returns all the pictures of a given album
	 * 
	 * @param albumName
	 * @return
	 * @throws GalleryConnectionException
	 */
	public Collection<Picture> getPicturesFromAlbum(int albumName)
			throws GalleryConnectionException;

	/**
	 * 
	 * Returns all the gallery albums, useful for the photo upload activity
	 * 
	 * 
	 * @param galleryUrl
	 * @return
	 * @throws GalleryConnectionException
	 */
	public abstract Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException;

	/**
	 * 
	 * Returns the album associated to the parentAlbumId, and its direct
	 * subalbums and pictures
	 * 
	 * @param galleryUrl
	 * @param parentAlbumId
	 * @return
	 * @throws GalleryConnectionException
	 */
	public abstract Album getAlbumAndSubAlbumsAndPictures(int parentAlbumId)
			throws GalleryConnectionException;

}