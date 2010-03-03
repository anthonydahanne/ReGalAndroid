package net.dahanne.android.g2android.utils.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * 
 * DOES NOT WORK
 * 
 * This file was used courtesy to :
 * http://www.delistage.net/blog/2009/04/20/java
 * -apache-httpclient-et-ssl-non-valide/
 * 
 * 
 * 
 */
public class MyTrustManager {
	protected static SSLSocketFactory _oldSSLSocketFactory = null;

	public static void enable() {
		if (_oldSSLSocketFactory != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(_oldSSLSocketFactory);
		}
	}

	public static void disable() {
		// Save the old SSLSocketFactory
		_oldSSLSocketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();

		try {
			HttpsURLConnection
					.setDefaultHostnameVerifier(new MyHostNameVerifier());
			// New X509TrustManager (our manager)
			X509TrustManager x509tm = new MyX509TrustManager();
			// Needed by SSLContext :
			KeyManager[] keyManager = null;
			// Set trust manager :
			TrustManager[] trustManager = { x509tm };
			// Get the SSLContext
			SSLContext sslContent = SSLContext.getInstance("TLS");
			// Initializaton of the SSLContext
			sslContent.init(keyManager, trustManager,
					new java.security.SecureRandom());
			// Get the SSLSocketFactory of our new SSLContext
			SSLSocketFactory socketFactory = sslContent.getSocketFactory();
			// Set this new SSLSocketFactory as default
			HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
		} catch (KeyManagementException exp) {
			exp.printStackTrace();
		} catch (NoSuchAlgorithmException exp) {
			exp.printStackTrace();
		}
	}
}
