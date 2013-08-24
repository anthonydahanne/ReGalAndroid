package fr.mael.jiwigo.service;

import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import fr.mael.jiwigo.dao.impl.CategoryDaoImpl;
import fr.mael.jiwigo.service.impl.CategoryServiceImpl;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.session.impl.SessionManagerImpl;

@Ignore
public class CategoryServiceTest {
    private SessionManager sessionManager;

    @Before
    public void setUp() throws JiwigoException {
	sessionManager = new SessionManagerImpl("mael", "motdepasse", "http://mael.piwigo.com", "Unit Test");
	sessionManager.processLogin();
    }

    /**
     * Test of the list method
     * 
     * @throws ProxyAuthenticationException
     */
    @Test
    public void ListerTest() throws JiwigoException {
	CategoryDaoImpl categaryDao = new CategoryDaoImpl();
	categaryDao.setSessionManager(sessionManager);
	categaryDao.list(true);
	categaryDao.list(false);
    }

    /**
     * Test of the creation method
     * 
     * @throws ProxyAuthenticationException
     */
    @Test
    public void creerTest() throws JiwigoException {
	Date date = new Date();
	CategoryServiceImpl categoryService = new CategoryServiceImpl();
	CategoryDaoImpl dao = new CategoryDaoImpl();
	dao.setSessionManager(sessionManager);
	categoryService.setDao(dao);
	Assert.assertSame(true, categoryService.create("Category" + date.getTime()));
    }

}
