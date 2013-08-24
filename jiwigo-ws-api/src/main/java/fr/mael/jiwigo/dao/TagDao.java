package fr.mael.jiwigo.dao;

import java.util.List;

import fr.mael.jiwigo.om.Image;
import fr.mael.jiwigo.om.Tag;
import fr.mael.jiwigo.transverse.exception.JiwigoException;

public interface TagDao {
    /**
     * lists the tags
     * @return the list of tags
     * @throws JiwigoException
     */
    public List<Tag> list() throws JiwigoException;

    /**
     * Creation of a tag
     * @param tag the tag to create
     * @return true if the tag as been successfully created
     * @throws JiwigoException
     */
    public boolean create(Tag tag) throws JiwigoException;

    /**
     * Function that returns the tags for an image
     * @param image the image
     * @return the tags list
     * @throws JiwigoException
     */
    public List<Tag> tagsForImage(Image image) throws JiwigoException;
}
