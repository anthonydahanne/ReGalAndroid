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
import java.io.IOException;
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
import net.dahanne.gallery.commons.utils.AlbumUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.service.CategoryService;
import fr.mael.jiwigo.service.ImageService;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.util.JiwigoConvertUtils;

public class PiwigoConnection implements RemoteGallery {

	private final Logger logger = LoggerFactory
			.getLogger(PiwigoConnection.class);
	private final SessionManager sessionManager;
	private final CategoryService categoryService;
	private final ImageService imageService;
	private Album rootAlbum;

	public PiwigoConnection(String galleryUrl, String username, String password) {
		sessionManager = new SessionManager(username, password, galleryUrl);
		categoryService = new CategoryService(sessionManager);
		imageService =  new ImageService(sessionManager);

	}

	@Override
	public int createNewAlbum(String arg0, int arg1, String arg2,
			String arPiwigo, String arg4) throws GalleryConnectionException {
		logger.debug("createNewAlbum");
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String arg0)
			throws GalleryConnectionException {
		logger.debug("getAllAlbums");
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public InputStream getInputStreamFromUrl(String umageUrl)
			throws GalleryConnectionException {
		logger.debug("getInputStreamFromUrl");
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public Collection<Picture> getPicturesFromAlbum(int albumName)
			throws GalleryConnectionException {
		logger.debug("getPicturesFromAlbum");
		List<Picture> pictures = new ArrayList<Picture>();
		try {
			List<Image> pictureInAlbum = imageService.listerParCategory(albumName, true);
			for (Image image : pictureInAlbum) {
				pictures.add( JiwigoConvertUtils.jiwigoImageToPicture(image));
			}
			return pictures;
		} catch (IOException e) {
			logger.debug("getPicturesFromAlbum : {}", e.getStackTrace());
			throw new GalleryConnectionException(e);
		}
	}

	@Override
	public void loginToGallery() throws ImpossibleToLoginException {
		logger.debug("loginToGallery");
		sessionManager.processLogin();

	}

	@Override
	public int uploadPictureToGallery(String arg0, int arg1, File arg2,
			String arPiwigo, String arg4, String arg5)
			throws GalleryConnectionException {
		logger.debug("uploadPictureToGallery");
		throw new GalleryOperationNotYetSupportedException(
				"Not available for Piwigo yet");
	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(int parentAlbumId)
			throws GalleryConnectionException {
		logger.debug("getAlbumAndSubAlbumsAndPictures");
		if(rootAlbum==null){
			try {
				List<Category> categoriesList = categoryService.construireArbre();
				rootAlbum =  JiwigoConvertUtils.categoriesToAlbum(categoriesList);
			} catch (IOException e) {
				logger.debug("listing categories failed : {}", e.getStackTrace());
				throw new GalleryConnectionException(e);
			}
			
		}
		
		Album findAlbumFromAlbumName = AlbumUtils.findAlbumFromAlbumName(
				rootAlbum, parentAlbumId);
//		findAlbumFromAlbumName.getPictures().addAll(
//				getPicturesFromAlbum( parentAlbumId));
		return findAlbumFromAlbumName;
		
	}
}
