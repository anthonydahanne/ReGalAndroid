package net.dahanne.gallery.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UriUtils {

	public final static String URL_PATTERN = "^(http|https):\\/\\/(?:\\P{M}\\p{M}*)+([\\-\\.]{1}(?:\\P{M}\\p{M}*)+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
	public final static String IP_ADDRESS_PATTERN = "^(http|https):\\/\\/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:[0-9]{1,5})?(\\/.*)?$";



	public static boolean checkUrlIsValid(String url) {
		Pattern p = Pattern.compile(URL_PATTERN);
		Matcher m = p.matcher(url);
		if (!m.matches()) {
			// not an url ? maybe an ip address
			p = Pattern.compile(IP_ADDRESS_PATTERN);
			m = p.matcher(url);
			return m.matches();
		}
		return true;
	}


}
