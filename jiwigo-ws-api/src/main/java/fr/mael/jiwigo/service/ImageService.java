package fr.mael.jiwigo.service;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.exception.WrongChunkSizeException;

public interface ImageService {

    /**
     * Lists all images for a category
     * @param categoryId the id of the category
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> listByCategory(Integer categoryId, boolean rafraichir) throws JiwigoException;

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
	    FileAlreadyExistsException, ProxyAuthenticationException, WrongChunkSizeException, JiwigoException;

    /**
     * Add tags to an existing image
     * @param image the image
     * @param tagId the ids of the tags
     * @return true if successful
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean addTags(Image image, String tagId) throws JiwigoException;

    /**
     * Search images from a string
     * @param queryString the string
     * @return images matching the string
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> search(String queryString) throws JiwigoException;

    /**
     * Uses the pwg.images.addsimple web API to add a new picture
     * http://piwigo.org/doc/doku.php?id=en:dev:webapi:pwg.images.addsimple
     * 
     * @param file
     * @param category
     * @param title
     * @throws IOException
     * @throws JiwigoException 
     */
    public void addSimple(File file, Integer category, String title) throws JiwigoException;

    /**
     * Uses the pwg.images.addsimple web API to add a new picture
     * http://piwigo.org/doc/doku.php?id=en:dev:webapi:pwg.images.addsimple
     * 
     * @param file
     * @param category
     * @param title
     * @param level
     * @throws IOException
     * @throws JiwigoException 
     */
    public void addSimple(File file, Integer category, String title, Integer level) throws JiwigoException;

    /**
     * Deletes an image
     * @param image the image to delete
     * @throws JiwigoException
     */
    public boolean delete(Image image) throws JiwigoException;

}
