package fr.mael.jiwigo.service.impl;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.dao.ImageDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.service.ImageService;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.exception.WrongChunkSizeException;

/*
 *  jiwigo-ws-api Piwigo webservice access Api
 *  Copyright (c) 2010-2011 Mael mael@le-guevel.com
 *                All Rights Reserved
 *
 *  This library is free software. It comes without any warranty, to
 *  the extent permitted by applicable law. You can redistribute it
 *  and/or modify it under the terms of the Do What The Fuck You Want
 *  To Public License, Version 2, as published by Sam Hocevar. See
 *  http://sam.zoy.org/wtfpl/COPYING for more details.
 */
/**
 *

 * @author mael
 *
 */
public class ImageServiceImpl implements ImageService {

    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(ImageServiceImpl.class);

    private ImageDao dao;

    /**
     * Lists all images for a category
     * @param categoryId the id of the category
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> listByCategory(Integer categoryId, boolean rafraichir) throws JiwigoException {
	return dao.listByCategory(categoryId, rafraichir);
    }

    /**
     * Method called to send an image to the server.
     * @param filePath
     * @param idCategory
     * @param originalWidth width for the original image
     * @param originalHeight height for the original image
     * @return true if the image is created
     * @throws IOException
     * @throws WrongChunkSizeException
     * @throws ProxyAuthenticationException
     * @throws FileAlreadyExistsException
     * @throws NoSuchAlgorithmException
     * @throws JiwigoException 
     * @throws Exception
     */
    public boolean create(String filePath, Integer idCategory, Integer originalWidth, Integer originalHeight,
	    Double chunckSize, Integer privacyLevel) throws IOException, NoSuchAlgorithmException,
	    FileAlreadyExistsException, ProxyAuthenticationException, WrongChunkSizeException, JiwigoException {
	//	File originalFile = new File(filePath);
	//	//get the byte array of the original file, to keep metadata
	//	byte[] bytesFichierOriginal = Tools.getBytesFromFile(originalFile);
	//
	//	//resize the picture (or not)
	//	boolean originaleRedimensionnee = ImagesUtil.scale(filePath, "originale.jpg", originalWidth, originalHeight);
	//	//create the thumbnail
	//	ImagesUtil.scale(filePath, "thumb.jpg", 120, 90);
	//	//get the thumbnail
	//	File thumbnail = new File(System.getProperty("java.io.tmpdir") + "/thumb.jpg");
	//	File originale = null;
	//	if (originaleRedimensionnee) {
	//	    originale = new File(System.getProperty("java.io.tmpdir") + "/originale.jpg");
	//	    //if the original file has been resized, we put the metadata in the resized file
	//	    //I use here a try catch because if the original file isn't a jpeg
	//	    //the methode Outil.enrich will fail, but the procedure has to continue
	//	    try {
	//		byte[] fichierEnrichi = Tools.enrich(bytesFichierOriginal,
	//			Tools.getBytesFromFile(new File(System.getProperty("java.io.tmpdir") + "/originale.jpg")));
	//		Tools.byteToFile(System.getProperty("java.io.tmpdir") + "/originale.jpg", fichierEnrichi);
	//	    } catch (Exception e) {
	//	    }
	//	} else {
	//	    originale = originalFile;
	//
	//	}
	//	Image image = new Image();
	//	image.setName(getImageName(filePath));
	//	image.setThumbnail(thumbnail);
	//	image.setOriginale(originale);
	//	image.setIdCategory(idCategory);
	//	image.setPrivacyLevel(String.valueOf(privacyLevel));
	//	//now we call the dao to send the image to the webservice
	//	return dao.create(image, chunckSize * 1000000);
	return false;
    }

    /**
     * Add tags to an existing image
     * @param image the image
     * @param tagId the ids of the tags
     * @return true if successful
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean addTags(Image image, String tagId) throws JiwigoException {
	return dao.addTags(image.getIdentifier(), tagId);
    }

    /**
     * Search images from a string
     * @param queryString the string
     * @return images matching the string
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> search(String queryString) throws JiwigoException {
	return dao.search(queryString);
    }

    /**
     * Deletes the file extension
     * @param path
     * @return
     */
    private String getImageName(String path) {
	File fichier = new File(path);
	StringBuffer name = new StringBuffer(fichier.getName());
	return (name.delete(name.lastIndexOf("."), name.length())).toString();

    }

    public void addSimple(File file, Integer category, String title) throws JiwigoException {
	dao.addSimple(file, category, title, null);

    }

    public boolean delete(Image image) throws JiwigoException {
	if (image.getIdentifier() == null) {
	    throw new JiwigoException("The identifier of the image cannot be null");
	} else {
	    return dao.delete(image);
	}

    }

    public ImageDao getDao() {
	return dao;
    }

    public void setDao(ImageDao dao) {
	this.dao = dao;
    }

    public void addSimple(File file, Integer category, String title, Integer level) throws JiwigoException {
	dao.addSimple(file, category, title, level);
    }

}
