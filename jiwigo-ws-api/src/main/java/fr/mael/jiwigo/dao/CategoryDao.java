package fr.mael.jiwigo.dao;

import java.io.IOException;
import java.util.List;

import fr.mael.jiwigo.om.Category;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

public interface CategoryDao {

    /**
     * Lists the categories
     * @param recursive true : recursive listing of the categories
     * @return the list of categories
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public List<Category> list(boolean recursive) throws JiwigoException;

    /**
     * Creation of a category
     * @param category the category to create
     * @return true if the category is successfully created
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public boolean create(Category category) throws JiwigoException;

    /**
     * Function that deletes a given category
     * @param category the category to delete
     * @return true if the category is successfully deleted
     * @throws JiwigoException
     */
    public boolean delete(Category category) throws JiwigoException;
}
