package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import fr.mael.jiwigo.dao.impl.TagDaoImpl;
import fr.mael.jiwigo.service.impl.TagServiceImpl;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.session.impl.SessionManagerImpl;

@Ignore
public class TagServiceTest {

    private SessionManager sessionManager;

    @Before
    public void setUp() throws JiwigoException {
	sessionManager = new SessionManagerImpl("mael", "motdepasse", "http://mael.piwigo.com", "Unit Test");
	sessionManager.processLogin();
    }

    /**
     * Test of the listing method
     * @throws JiwigoException 
     * 
     * @throws ProxyAuthenticationException
     */
    @Test
    @Ignore
    public void listTest() throws JiwigoException {
	TagDaoImpl tagDao = new TagDaoImpl();
	tagDao.setSessionManager(sessionManager);
	// TODO something weird happens here,
	// java.lang.ClassCastException: com.sun.org.apache.xerces.internal.dom.DeepNodeListImpl cannot be cast to
	// org.w3c.dom.Element
	// at fr.mael.jiwigo.dao.impl.TagDaoImpl.list(TagDaoImpl.java:58)
	tagDao.list();
    }

    /**
     * Test of the creation method
     * @throws JiwigoException 
     * 
     * @throws ProxyAuthenticationException
     * @throws IOException
     */
    @Test
    public void createTest() throws JiwigoException {
	Date date = new Date();
	TagServiceImpl tagService = new TagServiceImpl();
	TagDaoImpl tagDao = new TagDaoImpl();
	tagDao.setSessionManager(sessionManager);
	tagService.setDao(tagDao);
	Assert.assertSame(true, tagService.create("Tag" + date.getTime()));
    }

}
