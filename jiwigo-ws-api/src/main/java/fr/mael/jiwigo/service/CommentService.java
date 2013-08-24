package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.om.Comment;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

public interface CommentService {
    /**
     * Lists all comments for an image
     * @param imageId the id of the image
     * @return the list of comments
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Comment> list(Integer imageId) throws JiwigoException;

    /**
     * Creates a comment for an image
     * @param content the comment
     * @param imageId the id of the image
     * @param auteur the author of the comment
     * @return true if successful
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public boolean create(String content, Integer imageId, String auteur) throws JiwigoException;

}
