package fr.mael.jiwigo.service;

import java.util.List;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.transverse.exception.JiwigoException;

public interface TagService {

    /**
     * Lists all tags
     * @return le list of tags
     * @throws JiwigoException
     */
    public List<Tag> list() throws JiwigoException;

    /**
     * Creates a tag
     * @param nom name of the tag
     * @return true if the tag is created
     * @throws JiwigoException
     */
    public boolean create(String nom) throws JiwigoException;

    /**
     * Returns all the tag for an image
     * @param image the image to check
     * @return the list of tags
     * @throws JiwigoException
     */
    public List<Tag> tagsForImage(Image image) throws JiwigoException;
}
