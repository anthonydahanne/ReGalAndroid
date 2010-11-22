/**
 *  commons-gallery, a common API module for ReGalAndroid
 *  URLs: https://github.com/anthonydahanne/ReGalAndroid , http://blog.dahanne.net
 *  Copyright (c) 2010 Anthony Dahanne
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.dahanne.gallery.commons.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UriUtils {

	public final static String URL_PATTERN = "^(http|https):\\/\\/(?:\\P{M}\\p{M}*)+([\\-\\.]{1}(?:\\P{M}\\p{M}*)+)*\\.[a-z]{2,5}(:[0-9]{1,5})?(\\/.*)?$";
	public final static String IP_ADDRESS_PATTERN = "^(http|https):\\/\\/\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(:[0-9]{1,5})?(\\/.*)?$";
	private final static Logger logger = LoggerFactory.getLogger(UriUtils.class);


	public static boolean checkUrlIsValid(String url) {
		logger.debug("Url is : {}",url);
		boolean urlIsValid;
		Pattern p = Pattern.compile(URL_PATTERN);
		Matcher m = p.matcher(url);
		if (!m.matches()) {
			// not an url ? maybe an ip address
			p = Pattern.compile(IP_ADDRESS_PATTERN);
			m = p.matcher(url);
			urlIsValid =  m.matches();
		}else{
			urlIsValid =  true;
		}
		logger.debug("urlIsValid : {}",urlIsValid);
		return urlIsValid;
	}


}
