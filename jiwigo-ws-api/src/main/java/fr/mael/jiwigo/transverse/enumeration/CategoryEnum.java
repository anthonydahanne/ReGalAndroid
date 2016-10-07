/**
 *
 */
package fr.mael.jiwigo.transverse.enumeration;

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
 *
 */
public enum CategoryEnum {

    ID("id"), NB_IMAGES("nb_images"), NB_TOTAL_IMAGES("total_nb_images"), URL("url"), NAME("name"), CAT_MERES(
	    "uppercats"), URL_THUMBNAIL("tn_url");

    protected String label;

    /** Constructeur */
    CategoryEnum(String pLabel) {
	this.label = pLabel;
    }

    public String getLabel() {
	return this.label;
    }
}
