package fr.mael.jiwigo.service;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import fr.mael.jiwigo.dao.TagDao;
import fr.mael.jiwigo.transverse.session.SessionManager;

public class TagServiceTest  {

   private SessionManager sessionManager;

	@Before
	public void setUp(){
	    	sessionManager = new SessionManager("mael", "motdepasse", "http://mael.piwigo.com");
	    	sessionManager.processLogin();
	}

    /**
     *  Test of the listing method
     */
    @Test
    public void ListerTest() {
	try {
	    TagDao.getInstance(sessionManager).lister();
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
	try {
	    Assert.assertSame(true, TagService.getInstance(sessionManager).creer("Tag" + date.getTime()));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

}
