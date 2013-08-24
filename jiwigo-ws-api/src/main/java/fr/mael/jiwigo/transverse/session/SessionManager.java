package fr.mael.jiwigo.transverse.session;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Document;

import fr.mael.jiwigo.transverse.exception.JiwigoException;
import fr.mael.jiwigo.transverse.exception.ProxyAuthenticationException;

public interface SessionManager {
    /**
     * Connection method
     *
     * @return 0 if Ok, 1 if not Ok (reason not specified), 2 if proxy error
     * @throws JiwigoException 
     *
     *
     */
    public int processLogin() throws JiwigoException;

    /**
     * Executes a method on the webservice and returns the result as a string
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     * @throws UnsupportedEncodingException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     * @throws IOException 
     * @throws ClientProtocolException 
     */
    public String executeReturnString(String methode, String... parametres) throws UnsupportedEncodingException,
	    ProxyAuthenticationException, JiwigoException, ClientProtocolException, IOException;

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @param parametres the parameters of the method. Must be even : the name of the parameter followed by its value
     * @return the result
     * @throws IOException
     * @throws ProxyAuthenticationException
     * @throws JiwigoException 
     */
    public Document executeReturnDocument(String methode, String... parametres) throws JiwigoException;

    /**
     * Executes a method on the webservice and returns the result as a Dom document
     * @param methode the method to execute
     * @return the result
     * @throws ProxyAuthenticationException
     */
    public Document executeReturnDocument(String methode) throws JiwigoException;

    /**
     * Getter of the login
     * @return the login
     */
    public String getLogin();

    /**
     * Setter of the login
     * @param login
     */
    public void setLogin(String login);

    /**
     * Setter of the user agent
     * @param userAgent
     */
    public void setUserAgent(String userAgent);

    /**
     * Setter of the password to access piwigo
     * @param password
     */
    public void setPassword(String password);

    /**
     * Setter of the proxy login
     * @param loginProxy
     */
    public void setLoginProxy(String loginProxy);

    /**
     * Setter of the proxy port
     * @param port
     */
    public void setPortProxy(int port);

    /**
     * Setter of the proxy url
     * @param urlProxy
     */
    public void setUrlProxy(String urlProxy);

    /**
     * Setter of the proxy pass
     * @param proxyPass
     */
    public void setPassProxy(String proxyPass);

    /**
     * Setter of the proxy url
     * @param url
     */
    public void setUrl(String url);

    /**
     * Setter of the boolean that tells to use a proxy or not
     * @param usesProxy
     */
    public void setUsesProxy(boolean usesProxy);

    /**
     * Function that returns true if there is a proxy error
     * @return
     */
    public boolean isProxyError();

    /**
     * @param url
     * @return
     * @throws Exception
     */
    public InputStream getInputStreamFromUrl(String url) throws Exception;

    /**
     * This method allows to get pwg_token. This string is necessary
     * to call web service functions like pwg.images.delete and pwg.categories.delete
     * @return pwg_token
     */
    public String getPwgToken() throws JiwigoException;

}
