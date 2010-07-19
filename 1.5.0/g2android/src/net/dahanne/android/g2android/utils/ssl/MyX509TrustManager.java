package net.dahanne.android.g2android.utils.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * DOES NOT WORK
 * 
 * @author anthony.dahanne
 * 
 */
public class MyX509TrustManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {

	}

	public X509Certificate[] getAcceptedIssuers() {
		return new X509Certificate[0];
	}

}
