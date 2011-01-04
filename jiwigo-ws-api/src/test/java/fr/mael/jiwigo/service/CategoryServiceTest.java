package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mael.jiwigo.dao.CategoryDao;
import fr.mael.jiwigo.transverse.session.SessionManager;

public class CategoryServiceTest  {
    private SessionManager sessionManager;

	@Before
	public void setUp(){
	    	sessionManager =  new SessionManager("mael", "motdepasse", "http://mael.piwigo.com", "Unit Test");
	    	sessionManager.processLogin();
	}
    
    
    /**
     *  Test of the listing method
     */
    @Test
    public void ListerTest() {
	try {
	    CategoryDao categaryDao= new CategoryDao(sessionManager);
	    categaryDao.listing(true);
	    categaryDao.listing(false);
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    /**
     * Test of the creation method
     */
    @Test
    public void creerTest() {
	Date date = new Date();
	CategoryService categoryService = new CategoryService(sessionManager);
	Assert.assertSame(true, categoryService.creer("Category" + date.getTime()));
    }

}
