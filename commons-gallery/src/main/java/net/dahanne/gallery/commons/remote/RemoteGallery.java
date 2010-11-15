package net.dahanne.gallery.commons.remote;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.dahanne.gallery.commons.model.Album;
import net.dahanne.gallery.commons.model.Picture;

public interface RemoteGallery {

	public Collection<Picture> getPicturesFromAlbum(String galleryUrl,
			int albumName) throws GalleryConnectionException;

	/**
	 * Retrieve all the albums infos from the gallery
	 * 
	 * @param galleryUrl
	 * @return
	 * @throws GalleryConnectionException
	 */
	public abstract HashMap<String, String> fetchAlbums(String galleryUrl)
			throws GalleryConnectionException;

	/**
	 * The login method, cookie handling is located in sendCommandToGallery
	 * method
	 * 
	 * @param galleryUrl
	 * @param user
	 * @param password
	 * @return
	 * @throws ImpossibleToLoginException
	 */
	public abstract void loginToGallery(String galleryUrl, String user,
			String password) throws ImpossibleToLoginException;

	/**
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
	 * @param galleryUrl
	 * @param parentAlbumName
	 * @param albumName
	 * @param imageName
	 * @param albumTitle
	 * @param albumDescription
	 * @return number of the new album
	 * @throws GalleryConnectionException
	 */
	public abstract int sendImageToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException;

	public abstract InputStream getInputStreamFromUrl(String imageUrl)
			throws GalleryConnectionException;



	public abstract Map<Integer, Album> getAllAlbums(String galleryUrl)
			throws GalleryConnectionException;

	public Album getAlbumAndSubAlbumsAndPictures(String galleryUrl,int parentAlbumId) throws GalleryConnectionException;
	
	

}