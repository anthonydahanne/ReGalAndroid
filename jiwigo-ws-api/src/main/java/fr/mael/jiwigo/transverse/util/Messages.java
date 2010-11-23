package fr.mael.jiwigo.transverse.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

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
 * management of the messages for the translations
 */
public class Messages {

    public static Locale usedLocale = Locale.getDefault();

    /**
     * returns a message
     * @param key the key of the message
     * @return the message
     */
    public static String getMessage(String key) {
	return ResourceBundle.getBundle("fr.mael.jiwigo.trad.messages", usedLocale).getString(key);
    }

    /**
     * Gets available translations
     * @return the list of available translations
     */
    public static List<Locale> getAvailableBundles() {
	ClassLoader loader = Thread.currentThread().getContextClassLoader();
	final String bundlepackage = "fr.mael.jiwigo.trad";
	final String bundlename = "messages";

	File root = new File(loader.getResource(bundlepackage.replace('.', '/')).getFile());
	//System.out.println(loader.getResource(bundlepackage.replace('.', '/')).getFile());
	File[] files = root.listFiles(new FilenameFilter() {
	    public boolean accept(File dir, String name) {
		return name.matches("^" + bundlename + "(_\\w{2}(_\\w{2})?)?\\.properties$");
	    }
	});

	Set<String> languages = new TreeSet<String>();
	for (File file : files) {
	    languages.add(file.getName().replaceAll("^" + bundlename + "(_)?|\\.properties$", ""));
	}

	List<Locale> availableLocales = new ArrayList<Locale>();
	for (Locale locale : Locale.getAvailableLocales()) {
	    for (String s : languages) {
		if (s.equals(locale.getLanguage())) {
		    availableLocales.add(locale);
		}
	    }
	}
	return availableLocales;
    }

}
