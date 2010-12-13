package fr.mael.jiwigo.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.util.Base64Encoder;
import fr.mael.jiwigo.transverse.util.Outil;

/**
 * Copyright (c) 2010, Mael All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. Neither the name of jiwigo nor the names of its
 * contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL Mael BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Dao des images
 * 
 * @author mael
 * 
 */
public class ImageDao extends DaoBase {
	/**
	 * Logger
	 */
	private final Logger LOG = LoggerFactory.getLogger(ImageDao.class);

	/**
	 * Singleton
	 */
	private static ImageDao instance;

	/**
	 * cache to avoid downloading image for each access
	 */
	private final HashMap<Integer, List<Image>> cache;

	/**
     * 
     */
	private Integer firstCatInCache;

	/**
	 * Private singleton, to use a singleton
	 */
	public ImageDao(SessionManager sessionManager) {
		setSessionManager(sessionManager);
		cache = new HashMap<Integer, List<Image>>();
	}

	/**
	 * Lists all images
	 * 
	 * @return the list of images
	 * @throws IOException
	 */
	public List<Image> lister(boolean rafraichir) throws IOException {
		return listerParCategory(null, rafraichir);
	}

	/**
	 * Listing of the images for a category
	 * 
	 * @param categoryId
	 *            the id of the category
	 * @return the list of images
	 * @throws IOException
	 */
	public List<Image> listerParCategory(Integer categoryId, boolean rafraichir)
			throws IOException {
		if (rafraichir || cache.get(categoryId) == null) {
			Document doc = null;
			if (categoryId != null) {
				doc = getSessionManager().executerReturnDocument(
						MethodsEnum.LISTER_IMAGES.getLabel(), "cat_id",
						String.valueOf(categoryId));
			} else {
				doc = getSessionManager().executerReturnDocument(
						MethodsEnum.LISTER_IMAGES.getLabel());
			}
			Element element = doc.getRootElement().getChild("images");
			List<Image> images = getImagesFromElement(element);
			cache.remove(categoryId);
			cache.put(categoryId, images);
			if (firstCatInCache == null) {
				firstCatInCache = categoryId;
			}
			return images;
		} else {
			return cache.get(categoryId);
		}
	}

	/**
	 * Creation of an image<br/>
	 * Sequence : <br/>
	 * <li>
	 * <ul>
	 * sending of the thumbnail in base64, thanks to the method addchunk.
	 * </ul>
	 * <ul>
	 * sending of the image in base64, thanks to the method addchunk
	 * </ul>
	 * <ul>
	 * using of the add method to add the image to the database
	 * <ul></li> Finally, the response of the webservice is checked
	 * 
	 * @param image
	 *            to create
	 * @return true if the creation of the image was the successful
	 * @throws Exception
	 */
	// TODO ne pas continuer si une des réponses précédentes est négative
	public boolean creer(Image image, int privacyLevel, Double chunkSize)
			throws Exception {
		// thumbnail converted to base64

		String thumbnailBase64 = new String(Base64Encoder.encode(Outil
				.getBytesFromFile(image.getThumbnail())));
		// sends the thumbnail and gets the result
		Document reponseThumb = (getSessionManager().executerReturnDocument(
				"pwg.images.addChunk", "data", thumbnailBase64, "type",
				"thumb", "position", "1", "original_sum",
				Outil.getMD5Checksum(image.getOriginale().getAbsolutePath())));

		// begin feature:0001827
		int chunk = chunkSize.intValue();
		ArrayList<File> fichiersAEnvoyer = Outil.splitFile(
				image.getOriginale(), chunk);
		boolean echec = false;
		for (int i = 0; i < fichiersAEnvoyer.size(); i++) {
			File fichierAEnvoyer = fichiersAEnvoyer.get(i);
			String originaleBase64 = new String(Base64Encoder.encode(Outil
					.getBytesFromFile(fichierAEnvoyer)));
			Document reponseOriginale = (getSessionManager()
					.executerReturnDocument("pwg.images.addChunk", "data",
							originaleBase64, "type", "file", "position", String
									.valueOf(i), "original_sum", Outil
									.getMD5Checksum(image.getOriginale()
											.getAbsolutePath())));
			if (!Outil.checkOk(reponseOriginale)) {
				echec = true;
				break;
			}
		}
		// end

		// add the image in the database and get the result of the webservice
		Document reponseAjout = (getSessionManager().executerReturnDocument(
				"pwg.images.add", "file_sum",
				Outil.getMD5Checksum(image.getOriginale().getAbsolutePath()),
				"thumbnail_sum",
				Outil.getMD5Checksum(image.getThumbnail().getCanonicalPath()),
				"position", "1", "original_sum",
				Outil.getMD5Checksum(image.getOriginale().getAbsolutePath()),
				"categories", String.valueOf(image.getIdCategory()), "name",
				image.getName(), "author", getSessionManager().getLogin(),
				"level", String.valueOf(privacyLevel)));
		LOG.debug("Response add : " + Outil.documentToString(reponseAjout));
		// System.out.println(Main.sessionManager.executerReturnString("pwg.images.add",
		// "file_sum", Outil
		// .getMD5Checksum(image.getOriginale().getAbsolutePath()),
		// "thumbnail_sum", Outil.getMD5Checksum(image
		// .getThumbnail().getCanonicalPath()), "position", "1", "original_sum",
		// Outil.getMD5Checksum(image
		// .getOriginale().getAbsolutePath()), "categories",
		// String.valueOf(image.getIdCategory()), "name", image
		// .getName(), "author", Main.sessionManager.getLogin()));
		// Document reponsePrivacy = null;
		// if (Outil.checkOk(reponseAjout)) {
		// reponsePrivacy =
		// Main.sessionManager.executerReturnDocument(MethodsEnum.SET_PRIVACY_LEVEL.getLabel());
		// }
		boolean reussite = true;
		if (!Outil.checkOk(reponseThumb) || echec
				|| !Outil.checkOk(reponseAjout)) {
			reussite = false;
		}
		suppressionFichierTemporaires();
		return reussite;

	}

	/**
	 * Add tags to an image
	 * 
	 * @param imageId
	 *            id of the image
	 * @param tagId
	 *            ids of the tags
	 * @throws IOException
	 */
	public boolean addTags(Integer imageId, String tagId) throws IOException {
		Document doc = getSessionManager().executerReturnDocument(
				MethodsEnum.SET_INFO.getLabel(), "image_id",
				String.valueOf(imageId), "tag_ids", tagId);
		return Outil.checkOk(doc);

	}

	/**
	 * parse an element to find images
	 * 
	 * @param element
	 *            the element to parse
	 * @return the list of images
	 */
	private List<Image> getImagesFromElement(Element element) {
		List<Element> listElement = element.getChildren("image");
		ArrayList<Image> images = new ArrayList<Image>();
		for (Element im : listElement) {
			Image myImage = new Image();
			myImage.setMiniature(im.getAttributeValue("tn_url"));
			myImage.setUrl(im.getAttributeValue("element_url"));
			myImage.setWidth(Integer.valueOf(im.getAttributeValue("width")));
			myImage.setHeight(Integer.valueOf(im.getAttributeValue("height")));
			myImage.setFile(im.getAttributeValue("file"));
			myImage.setVue(Integer.valueOf(im.getAttributeValue("hit")));
			myImage.setIdentifiant(Integer.valueOf(im.getAttributeValue("id")));
			myImage.setName(im.getChildText("name"));
			if (myImage.getName() == null) {
				myImage.setName(myImage.getFile());
			}
			images.add(myImage);

		}
		return images;
	}

	/**
	 * Search images
	 * 
	 * @param searchString
	 *            the string to search
	 * @return the list of images matching the string
	 * @throws IOException
	 */
	public List<Image> search(String searchString) throws IOException {
		Document doc = getSessionManager().executerReturnDocument(
				MethodsEnum.SEARCH.getLabel(), "query", searchString);
		LOG.debug(doc.toString());
		Element element = doc.getRootElement().getChild("images");
		return getImagesFromElement(element);

	}

	private void suppressionFichierTemporaires() {
		File file = new File(System.getProperty("java.io.tmpdir")
				+ "/originale.jpg");
		file.delete();
		file = new File(System.getProperty("java.io.tmpdir") + "/thumb.jpg");
		file.delete();

	}

}
