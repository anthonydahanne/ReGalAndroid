package fr.mael.jiwigo.dao;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.exception.WrongChunkSizeException;

public interface ImageDao {
    /**
     * Lists all images
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> list(boolean refresh) throws JiwigoException;

    /**
     * Listing of the images for a category
     * @param categoryId the id of the category
     * @return the list of images
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> listByCategory(Integer categoryId, boolean refresh) throws JiwigoException;

    /**
     * Creation of an image<br/>
     * Sequence : <br/>
     * <li>
     * <ul>sending of the thumbnail in base64, thanks to the method addchunk.</ul>
     * <ul>sending of the image in base64, thanks to the method addchunk</ul>
     * <ul>using of the add method to add the image to the database<ul>
     * </li>
     * Finally, the response of the webservice is checked
     *
     * @param image the image to create
     * @return true if the creation of the image was the successful
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws WrongChunkSizeException
     * @throws JiwigoException 
     * @throws Exception
     */
    //TODO ne pas continuer si une des reponses precedentes est negative
    public boolean create(Image image, Double chunkSize) throws FileAlreadyExistsException, IOException,
	    ProxyAuthenticationException, NoSuchAlgorithmException, WrongChunkSizeException, JiwigoException;

    /**
     * Add tags to an image
     * @param imageId id of the image
     * @param tagId ids of the tags
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean addTags(Integer imageId, String tagId) throws JiwigoException;

    /**
     * Search images
     * @param searchString the string to search
     * @return the list of images matching the string
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Image> search(String searchString) throws JiwigoException;

    //    /**
    //     * Uses the pwg.images.addsimple web API to add a new picture
    //     * http://piwigo.org/doc/doku.php?id=en:dev:webapi:pwg.images.addsimple
    //     * 
    //     * @param file
    //     * @param category
    //     * @param title
    //     * @throws IOException
    //     * @throws JiwigoException 
    //     */
    //    public void addSimple(File file, Integer category, String title) throws JiwigoException;

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
