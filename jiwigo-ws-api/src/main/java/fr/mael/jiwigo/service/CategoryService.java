package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

public interface CategoryService {
    /**
     * Lists all categories
     * @param recursive true : recursive listing
     * @return the list of categories
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Category> list(boolean recursive) throws JiwigoException;

    /**
     * Allows to create the categories tree
     * @return list of categories
     * @throws IOException
     * @throws ProxyAuthenticationException
     */
    public List<Category> makeTree() throws JiwigoException;

    /**
     * creation of a category
     * @param nom name of the category
     * @param parent parent category
     * @return true if successful
     * @throws ProxyAuthenticationException
     */
    public boolean create(String nom, Integer parent) throws JiwigoException;

    /**
     * creation of a category without parent
     * @param nom name of the category
     * @return true if successful
     * @throws ProxyAuthenticationException
     */
    public boolean create(String nom) throws JiwigoException;

    /**
     * Deletes a category
     * @param id id of the category
     * @return
     * @throws JiwigoException
     */
    public boolean delete(Category category) throws JiwigoException;
}
