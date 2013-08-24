package fr.mael.jiwigo.dao;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

public interface CommentDao {
    /**
     * Listing of the comments for the given image
     * @param idImage id of the  image
     * @return list of comments
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Comment> list(Integer idImage) throws JiwigoException;

    /**
     * Gets the key essential to add a comment
     * @param idImage the id of the image
     * @return the key
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public String getKey(Integer idImage) throws JiwigoException;

    /**
     * Creates a comment
     * @param commentaire the comment to add
     * @return true if the comment is successfully added
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean create(Comment commentaire) throws JiwigoException;
}
