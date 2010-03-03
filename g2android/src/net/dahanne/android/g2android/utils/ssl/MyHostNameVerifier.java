package net.dahanne.android.g2android.utils.ssl;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * DOES NOT WORK
 * 
 * @author anthony.dahanne
 * 
 */
public class MyHostNameVerifier implements HostnameVerifier {

	@Override
	public boolean verify(String hostname, SSLSession session) {
		return true;
	}

}
