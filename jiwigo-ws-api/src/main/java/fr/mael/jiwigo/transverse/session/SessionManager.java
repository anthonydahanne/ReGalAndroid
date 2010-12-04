package fr.mael.jiwigo.transverse.session;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import net.dahanne.gallery.commons.remote.GalleryConnectionException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mael.jiwigo.transverse.enumeration.MethodsEnum;
import fr.mael.jiwigo.transverse.util.Outil;

/**
   Copyright (c) 2010, Mael
   All rights reserved.

   Redistribution and use in source and binary forms, with or without
   modification, are permitted provided that the following conditions are met:
    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of jiwigo nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   DISCLAIMED. IN NO EVENT SHALL Mael BE LIABLE FOR ANY
   DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
   ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
   
 * @author mael
 * Gestionnaire de connexion
 */
public class SessionManager {
    /**
     * Logger
     */
	private  final Logger LOG = LoggerFactory.getLogger(SessionManager.class);
    /**
     * the entered login
     */
    private String login;
    /**
     * the entered password
     */
    private String motDePasse;
    /**
     * the url of the site
     */
    private String url;
    /**
     * the http client
     */
    private final DefaultHttpClient client;

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
    private boolean erreurProxy;

    /**
     * Constructor
     * @param login the login
     * @param motDePasse the password
     * @param url the url of the site
     */
    public SessionManager(String login, String motDePasse, String url) {
	this.login = login;
	this.motDePasse = motDePasse;
	this.url = url + "/ws.php";
//	MultiThreadedHttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
	client = new DefaultHttpClient();
	//Using of a Linux user agent. cause... it's better 8)
	client.getParams().setParameter("http.useragent",
		"Mozilla/5.0 (X11; U; Linux i686; fr; rv:1.9.1.1) Gecko/20090715 Firefox/3.5.1");

    }

    /**
     * Connection method
     * @return true if successful
     */
    public boolean processLogin() {
	Document doc;
	//configures the proxy, no proxy in android
//	if (usesProxy) {
//	    HostConfiguration config = client.getHostConfiguration();
//	    config.setProxy(urlProxy, portProxy);
//	    if (!StringUtils.isEmpty(loginProxy) && !StringUtils.isEmpty(passProxy)) {
//		Credentials credentials = new UsernamePasswordCredentials(loginProxy, passProxy);
//		AuthScope authScope = new AuthScope(urlProxy, portProxy);
//		client.getState().setProxyCredentials(authScope, credentials);
//	    }
//	}
	try {
	    doc = executerReturnDocument(MethodsEnum.LOGIN.getLabel(), "username", login, "password", motDePasse);
	    return Outil.checkOk(doc);
	} catch (Exception e) {
	    LOG.error(Outil.getStackTrace(e));
	}
	return false;

    }

    /**
     * Executes a method on the webservice and returns the result as a string
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     */
    public String executerReturnString(String methode, String... parametres) {
	if (parametres.length % 2 != 0 && !(parametres == null)) {
	    try {
		throw new Exception("Le nombre de parametres doit etre pair");
	    } catch (Exception e) {
		LOG.error(Outil.getStackTrace(e));
		return null;
	    }
	}
	HttpPost method = new HttpPost(url);
//	PostMethod method = new PostMethod(url);
	List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	nameValuePairs.add(new BasicNameValuePair("method",
			methode));
//	method.addParameter("method", methode);
	for (int i = 0; i < parametres.length; i += 2) {
		nameValuePairs.add(new BasicNameValuePair(parametres[i],
				 parametres[i + 1]));
//	    method.addParameter(parametres[i], parametres[i + 1]);
	}
	
//	method.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
	//end

	try {
		method.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		//begin bug:0001833
		Header header   =new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
		method.setHeader(header);
	    HttpResponse response = client.execute(method);
//	    InputStream streamResponse = method.getResponseBodyAsStream();
	    InputStream content = response.getEntity().getContent();
	    //	    System.out.println(Outil.readInputStreamAsString(streamResponse));
	    //	    String toReturn = method.getResponseBodyAsString();
	    String toReturn = Outil.readInputStreamAsString(content);
	    LOG.debug(toReturn);
	    return toReturn;
	} catch (ConnectException e) {
	    // TODO Auto-generated catch block
	    LOG.error(Outil.getStackTrace(e));
//	    JOptionPane.showMessageDialog(null, Messages.getMessage("connectionRefusedError"), Messages
//		    .getMessage("error"), JOptionPane.ERROR_MESSAGE);
	    erreurProxy = true;
//	} catch (HttpException e) {
//	    // TODO Auto-generated catch block
//	    LOG.error(Outil.getStackTrace(e));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    LOG.error(Outil.getStackTrace(e));
	} catch (IllegalArgumentException e) {
	    LOG.error(Outil.getStackTrace(e));
//	    JOptionPane.showMessageDialog(null, Messages.getMessage("connexionDialog_connexionError"), Messages
//		    .getMessage("error"), JOptionPane.ERROR_MESSAGE);
	} finally {
//		response.releaseConnection();
	}
	return null;

    }

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     * @throws IOException 
     */
    public Document executerReturnDocument(String methode, String... parametres) throws IOException {
	try {
	    return Outil.stringToDocument(executerReturnString(methode, parametres));
	} catch (JDOMException e) {
	    // TODO Auto-generated catch block
	    LOG.error(Outil.getStackTrace(e));
	}
	return null;

    }

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @return the result
     */
    public Document executerReturnDocument(String methode) {
	try {
	    return Outil.stringToDocument(executerReturnString(methode));
	} catch (JDOMException e) {
	    // TODO Auto-generated catch block
	    LOG.error(Outil.getStackTrace(e));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    LOG.error(Outil.getStackTrace(e));
	}
	return null;

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
     * @return the motDePasse
     */
    public String getMotDePasse() {
	return motDePasse;
    }

    /**
     * @param motDePasse the motDePasse to set
     */
    public void setMotDePasse(String motDePasse) {
	this.motDePasse = motDePasse;
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

    /**
     * @return the erreurProxy
     */
    public boolean isErreurProxy() {
	return erreurProxy;
    }
    
    
    public InputStream getInputStreamFromUrl(String url) throws GalleryConnectionException {
		InputStream content = null;
		try {
			HttpGet httpGet = new HttpGet(url);
			// Execute HTTP Get Request
			HttpResponse response = client.execute(httpGet);
			content = response.getEntity().getContent();
		} catch (Exception e) {
			throw new GalleryConnectionException(e.getMessage());
		}
		return content;
	}

}
