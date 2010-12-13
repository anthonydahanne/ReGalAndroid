package fr.mael.jiwigo.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.dao.ImageDao;
import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.util.ImagesUtil;
import fr.mael.jiwigo.transverse.util.Outil;

/**
 * 
   Copyright (c) 2010, Mael
   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of jiwigo nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   DISCLAIMED. IN NO EVENT SHALL Mael BE LIABLE FOR ANY
   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   
 * @author mael
 *
 */
public class ImageService extends ServiceBase {
    private final ImageDao imageDao;

	/**
     * Logger
     * @param sessionManager 
     */
//	private  final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    public ImageService(SessionManager sessionManager) {
    	setSessionManager(sessionManager);
    	this.imageDao=new ImageDao(sessionManager);

    }

    /**
     * Lists all images for a category
     * @param categoryId the id of the category
     * @param rafraichir true : refresh the list of images
     * @return the list of images
     * @throws IOException
     */
    public List<Image> listerParCategory(Integer categoryId, boolean rafraichir) throws IOException {
	return imageDao.listerParCategory(categoryId, rafraichir);
    }

    /**
     * Method called to send an image to the server.
     * @param filePath
     * @param idCategory
     * @return
     * @throws Exception
     */
    public boolean creer(String filePath, Integer idCategory, int privacyLevel, Double chunkSize) throws Exception {
	File fileToUpload = new File(filePath);
	//get the byte array of the original file, to keep metadata
	byte[] bytesFichierOriginal = Outil.getBytesFromFile(fileToUpload);

	int widthOriginale = 320;
	int heightOriginale = 240;
	//size taken from the user's preferences
//	int widthOriginale = Integer
//		.valueOf(PreferencesManagement.getValue(PreferencesEnum.WIDTH_ORIGINALE.getLabel()));
//	int heightOriginale = Integer.valueOf(PreferencesManagement
//		.getValue(PreferencesEnum.HEIGHT_ORIGINAL.getLabel()));
	//resize the picture (or not)
	boolean originaleRedimensionnee = ImagesUtil.scale(filePath, "originale.jpg", widthOriginale, heightOriginale);
	//create the thumbnail
	ImagesUtil.scale(filePath, "thumb.jpg", 120, 90);
	//get the thumbnail
	File thumbnail = new File(System.getProperty("java.io.tmpdir") + "/thumb.jpg");
	File originale = null;
	if (originaleRedimensionnee) {
	    originale = new File(System.getProperty("java.io.tmpdir") + "/originale.jpg");
	    //if the original file has been resized, we put the metadata in the resized file
	    //I use here a try catch because if the original file isn't a jpeg
	    //the methode Outil.enrich will fail, but the procedure has to continue
//	    MainFrame.getInstance().setMessage(
//		    Messages.getMessage("mainFrame_addMetadata") + " " + fileToUpload.getName());
	    try {
		byte[] fichierEnrichi = Outil.enrich(bytesFichierOriginal, Outil.getBytesFromFile(new File(System
			.getProperty("java.io.tmpdir")
			+ "/originale.jpg")));
		Outil.byteToFile(System.getProperty("java.io.tmpdir") + "/originale.jpg", fichierEnrichi);
	    } catch (Exception e) {
	    }
	} else {
	    originale = new File(filePath);

	}
	Image image = new Image();
	image.setName(getImageName(filePath));
	image.setThumbnail(thumbnail);
	image.setOriginale(originale);
	image.setIdCategory(idCategory);
//	MainFrame.getInstance()
//		.setMessage(Messages.getMessage("mainFrame_sendingFiles") + " " + fileToUpload.getName());
	//now we call the dao to send the image to the webservice
	return imageDao.creer(image, privacyLevel, chunkSize);
    }

    /**
     * Add tags to an existing image
     * @param image the image
     * @param tagId the ids of the tags
     * @return true if successful
     * @throws IOException
     */
    public boolean addTags(Image image, String tagId) throws IOException {
	return imageDao.addTags(image.getIdentifiant(), tagId);
    }

    /**
     * Search images from a string
     * @param queryString the string
     * @return images matching the string
     * @throws IOException
     */
    public List<Image> search(String queryString) throws IOException {
	return imageDao.search(queryString);
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

}
