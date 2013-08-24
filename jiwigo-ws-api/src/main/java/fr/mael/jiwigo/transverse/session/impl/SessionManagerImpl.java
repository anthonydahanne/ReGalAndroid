package fr.mael.jiwigo.transverse.session.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.exception.FileAlreadyExistsException;
import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;
import fr.mael.jiwigo.transverse.session.SessionManager;
import fr.mael.jiwigo.transverse.util.Tools;

/*
 *  jiwigo-ws-api Piwigo webservice access Api
 *  Copyright (c) 2010-2011 Mael mael@le-guevel.com
 *                All Rights Reserved
 *
 *  This library is free software. It comes without any warranty, to
 *  the extent permitted by applicable law. You can redistribute it
 *  and/or modify it under the terms of the Do What The Fuck You Want
 *  To Public License, Version 2, as published by Sam Hocevar. See
 *  http://sam.zoy.org/wtfpl/COPYING for more details.
 */
/**

 * @author mael
 * Gestionnaire de connexion
 */
public class SessionManagerImpl implements SessionManager {

    /**
     * Logger
     */
    private final Logger LOG = LoggerFactory.getLogger(SessionManagerImpl.class);
    /**
     * the entered login
     */
    private String login;
    /**
     * the entered password
     */
    private String password;
    /**
     * the url of the site
     */
    private String url;
    /**
     * the http client
     */
    private DefaultHttpClient client;

    /**
     * defines if the user uses a proxy
     */
    private boolean usesProxy;

    /**
     * url of the proxy
     */
    private String urlProxy;

    /**
     * port of the proxy
     */
    private int portProxy;

    /**
     * login for the proxy
     */
    private String loginProxy;

    /**
     * pass for the proxy
     */
    private String passProxy;

    /**
     * true : an error was found for the proxy
     */
    private boolean proxyError;

    public SessionManagerImpl() {
	client = new DefaultHttpClient();
    }

    /**
     * Constructor
     * @param login the login
     * @param password the password
     * @param url the url of the site
     * @param userAgent the user agent to use
     */
    public SessionManagerImpl(String login, String password, String url, String userAgent) {
	this.login = login;
	this.password = password;
	this.url = url + "/ws.php";
	//	MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	//	client = new HttpClient(connectionManager);
	//	ThreadSafeClientConnManager connectionManager = new ThreadSafeClientConnManager(null, null);
	client = new DefaultHttpClient();
	//Using of a Linux user agent. cause... it's better 8)
	client.getParams().setParameter("http.useragent", userAgent);

    }

    /**
     * Fonction used to set the user agent of the http client
     * @param userAgent
     */
    public void setUserAgent(String userAgent) {
	client.getParams().setParameter("http.useragent", userAgent);
    }

    /**
     * Connection method
     *
     * @return 0 if Ok, 1 if not Ok (reason not specified), 2 if proxy error
     * @throws JiwigoException 
     *
     *
     */
    public int processLogin() throws JiwigoException {
	Document doc;
	//configures the proxy
	if (usesProxy) {
	    //	    HostConfiguration config = client.getHostConfiguration();
	    //	    config.setProxy(urlProxy, portProxy);
	    if (!StringUtils.isEmpty(loginProxy) && !StringUtils.isEmpty(passProxy)) {
		//		Credentials credentials = new UsernamePasswordCredentials(loginProxy, passProxy);
		//		AuthScope authScope = new AuthScope(urlProxy, portProxy);
		//		client.getState().setProxyCredentials(authScope, credentials);
		HttpHost proxy = new HttpHost(urlProxy, portProxy);
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		client.getCredentialsProvider().setCredentials(new AuthScope(urlProxy, portProxy),
			new UsernamePasswordCredentials(loginProxy, passProxy));

	    }
	}
	doc = executeReturnDocument(MethodsEnum.LOGIN.getLabel(), "username", login, "password", password);
	if (LOG.isDebugEnabled()) {
	    LOG.debug("XML connection : " + Tools.documentToString(doc));
	}
	try {
	    return (Tools.checkOk(doc) ? 0 : 1);
	} catch (FileAlreadyExistsException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	}

    }

    /**
     * Executes a method on the webservice and returns the result as a string
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public String executeReturnString(String methode, String... parametres) throws ProxyAuthenticationException,
	    JiwigoException, ClientProtocolException, IOException {
	if (parametres.length % 2 != 0 && !(parametres == null)) {
	    LOG.error("parameters number should be even");
	    throw new IllegalArgumentException("parameters number should be even");
	}
	HttpPost method = new HttpPost(url);
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("method", methode));
	for (int i = 0; i < parametres.length; i += 2) {
	    nameValuePairs.add(new BasicNameValuePair(parametres[i], parametres[i + 1]));
	}
	method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	method.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
	HttpResponse response = client.execute(method);
	int statusCode = response.getStatusLine().getStatusCode();
	InputStream inputStream = response.getEntity().getContent();
	String toReturn = Tools.readInputStreamAsString(inputStream);
	if (statusCode == 407) {
	    throw new ProxyAuthenticationException("Error while trying to auth on the proxy");
	} else if (statusCode >= 400) {
	    throw new JiwigoException("Piwigo server returned an error code " + toReturn);
	}

	return toReturn;

    }

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public Document executeReturnDocument(String methode, String... parametres) throws JiwigoException {
	try {
	    String returnedString = executeReturnString(methode, parametres);
	    return Tools.stringToDocument(returnedString);
	} catch (ProxyAuthenticationException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	} catch (SAXException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	} catch (ParserConfigurationException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	} catch (UnsupportedEncodingException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	} catch (IOException e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	}
    }

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @return the result
     * @throws JiwigoException
     */
    public Document executeReturnDocument(String methode) throws JiwigoException {
	try {
	    return Tools.stringToDocument(executeReturnString(methode));
	} catch (Exception e) {
	    LOG.error(Tools.getStackTrace(e));
	    throw new JiwigoException(e);
	}
    }

    public InputStream getInputStreamFromUrl(String url) throws Exception {
	InputStream content = null;
	HttpGet httpGet = new HttpGet(url);
	HttpResponse response = client.execute(httpGet);
	content = response.getEntity().getContent();
	return content;
    }

    /**
     * @see fr.mael.jiwigo.transverse.session.SessionManager#getPwgToken()
     */
    public String getPwgToken() throws JiwigoException {
	Document docStatus = executeReturnDocument(MethodsEnum.SESSION_STATUS.getLabel());
	String pwgToken = Tools.getStringValueDom(docStatus.getDocumentElement(), "pwg_token");
	if ("".equals(pwgToken)) {
	    throw new JiwigoException("Error getting pwg_token. Returned document was : "
		    + Tools.documentToString(docStatus));
	}
	return pwgToken;
    }

    /**
     * @return the login
     */
    public String getLogin() {
	return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
	this.login = login;
    }

    /**
     * @return the url
     */
    public String getUrl() {
	return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
	this.url = url;
    }

    /**
     * @param usesProxy the usesProxy to set
     */
    public void setUsesProxy(boolean usesProxy) {
	this.usesProxy = usesProxy;
    }

    /**
     * @param urlProxy the urlProxy to set
     */
    public void setUrlProxy(String urlProxy) {
	this.urlProxy = urlProxy;
    }

    /**
     * @param portProxy the portProxy to set
     */
    public void setPortProxy(int portProxy) {
	this.portProxy = portProxy;
    }

    /**
     * @param loginProxy the loginProxy to set
     */
    public void setLoginProxy(String loginProxy) {
	this.loginProxy = loginProxy;
    }

    /**
     * @param passProxy the passProxy to set
     */
    public void setPassProxy(String passProxy) {
	this.passProxy = passProxy;
    }

    public String getPassword() {
	return password;
    }

    public void setPassword(String password) {
	this.password = password;
    }

    public boolean isProxyError() {
	return proxyError;
    }

    public void setProxyError(boolean proxyError) {
	this.proxyError = proxyError;
    }

    public DefaultHttpClient getClient() {
	return client;
    }

}
