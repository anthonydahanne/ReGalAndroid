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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class UriUtilsTest {
	@Test
	public void checkUriIsValidTest() {
		String url = "http://google.com";
		UriUtils.checkUrlIsValid(url);
		url = "https://google.com/test";
		UriUtils.checkUrlIsValid(url);
		url = "http://google.com:453/truc";
		UriUtils.checkUrlIsValid(url);
		url = "https://google.com:66/test";
		UriUtils.checkUrlIsValid(url);
		url = "http://läxhjälpen.se/";
		UriUtils.checkUrlIsValid(url);
		url = "http://xn--lxhjlpen-0zad.se/";
		UriUtils.checkUrlIsValid(url);
		url = "http://192.168.1.101:66/test";
		UriUtils.checkUrlIsValid(url);
		url = "https://192.168.1.101";
		UriUtils.checkUrlIsValid(url);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void spaceIsNotValidTest(){
		String url = "http://192.168.1.101:66/test ll";
		UriUtils.checkUrlIsValid(url);
	}	

	@Test(expected=IllegalArgumentException.class)
	public void noHttpIsNotValidTest(){
		String url = "badurl.net";
		UriUtils.checkUrlIsValid(url);
	}
	@Test(expected=IllegalArgumentException.class)
	public void plainStringIsNotValidTest(){
		String url = "toto";
		UriUtils.checkUrlIsValid(url);
	}
}
