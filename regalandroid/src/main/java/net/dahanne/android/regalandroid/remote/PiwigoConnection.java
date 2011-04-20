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
import net.dahanne.gallery.jiwigo.converter.JiwigoConvertUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.dao.impl.CategoryDaoImpl;
import fr.mael.jiwigo.dao.impl.ImageDaoImpl;
import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.service.impl.CategoryServiceImpl;
import fr.mael.jiwigo.service.impl.ImageServiceImpl;
import fr.mael.jiwigo.transverse.exception.PiwigoConnectionException;
import fr.mael.jiwigo.transverse.session.impl.SessionManagerImpl;

public class PiwigoConnection implements RemoteGallery {

	private final Logger logger = LoggerFactory
			.getLogger(PiwigoConnection.class);
	private final SessionManagerImpl sessionManager;
	private final CategoryServiceImpl categoryService;
	private final ImageServiceImpl imageService;
	private Album rootAlbum;

	public PiwigoConnection(String galleryUrl, String username,
			String password, String userAgent) {
		sessionManager = new SessionManagerImpl(username, password, galleryUrl,
				userAgent);
		categoryService = new CategoryServiceImpl();
		CategoryDaoImpl categoryDao = new CategoryDaoImpl();
		categoryDao.setSessionManager(sessionManager);
		categoryService.setDao(categoryDao);
		imageService = new ImageServiceImpl();
		ImageDaoImpl imageDao = new ImageDaoImpl();
		imageDao.setSessionManager(sessionManager);
		imageService.setDao(imageDao);

	}

	@Override
	public int createNewAlbum(String galleryUrl, int parentAlbumName,
			String albumName, String albumTitle, String albumDescription)
			throws GalleryConnectionException {
		logger.debug("createNewAlbum");
		try {
			categoryService.create(albumName, parentAlbumName);
		} catch (PiwigoConnectionException e) {
			throw new GalleryConnectionException(e);
		}
		return parentAlbumName;
	}

	@Override
	public Map<Integer, Album> getAllAlbums(String arg0)
			throws GalleryConnectionException {
		logger.debug("getAllAlbums");
		Map<Integer, Album> map = new HashMap<Integer, Album>();
		List<Category> categoriesList;
		try {
			categoriesList = categoryService.makeTree();
			for (Category category : categoriesList) {
				Album jiwigoCategoryToAlbum = JiwigoConvertUtils
						.jiwigoCategoryToAlbum(category);
				map.put(jiwigoCategoryToAlbum.getId(), jiwigoCategoryToAlbum);
			}
			return map;
		} catch (PiwigoConnectionException e) {
			logger.debug("getAllAlbums : {}", e.getStackTrace());
			throw new GalleryConnectionException(e);
		} 
	}

	@Override
	public Collection<Picture> getPicturesFromAlbum(int albumName)
			throws GalleryConnectionException {
		logger.debug("getPicturesFromAlbum");
		List<Picture> pictures = new ArrayList<Picture>();
		try {
			List<Image> pictureInAlbum = imageService.listByCategory(albumName,
					true);
			for (Image image : pictureInAlbum) {
				pictures.add(JiwigoConvertUtils.jiwigoImageToPicture(image));
			}
			return pictures;
		} catch (PiwigoConnectionException e) {
			logger.debug("getPicturesFromAlbum : {}", e.getStackTrace());
			throw new GalleryConnectionException(e);
		}
	}

	@Override
	public void loginToGallery() throws ImpossibleToLoginException {
		logger.debug("loginToGallery");
		try {
			sessionManager.processLogin();
		} catch (PiwigoConnectionException e) {
			logger.debug("loginToGallery : {}", e.getStackTrace());
			throw new ImpossibleToLoginException(e);
		}

	}

	@Override
	public int uploadPictureToGallery(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary, String description)
			throws GalleryConnectionException {
		logger.debug("uploadPictureToGallery");

		 try {
			 imageService.addSimple(imageFile, albumName, imageName);
		 } catch (PiwigoConnectionException e) {
			 logger.debug("uploadPictureToGallery : {}", e.getStackTrace());
			 throw new GalleryConnectionException(e);
		 }
		 return albumName;

	}

	@Override
	public Album getAlbumAndSubAlbumsAndPictures(int parentAlbumId)
			throws GalleryConnectionException {
		logger.debug("getAlbumAndSubAlbumsAndPictures");
		if (rootAlbum == null) {
			try {
				List<Category> categoriesList = categoryService
						.makeTree();
				rootAlbum = JiwigoConvertUtils
						.categoriesToAlbum(categoriesList);
			} catch (PiwigoConnectionException e) {
				logger.debug("listing categories failed : {}",
						e.getStackTrace());
				throw new GalleryConnectionException(e);
			} 

		}

		Album findAlbumFromAlbumName = AlbumUtils.findAlbumFromAlbumName(
				rootAlbum, parentAlbumId);
		findAlbumFromAlbumName.getPictures().addAll(
				getPicturesFromAlbum(parentAlbumId));
		return findAlbumFromAlbumName;

	}
	
	@Override
	public InputStream getInputStreamFromUrl(String url) throws GalleryConnectionException {
		logger.debug("getInputStreamFromUrl");
		InputStream content = null;
		DefaultHttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			// Execute HTTP Get Request
			HttpResponse response = client.execute(httpGet);
			content = response.getEntity().getContent();
		} catch (Exception e) {
			httpGet.abort();
			throw new GalleryConnectionException(e.getMessage());
		}
		return content;
	}
}
