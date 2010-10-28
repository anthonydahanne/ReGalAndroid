/**
 * g2android
 * Copyright (c) 2009 Anthony Dahanne
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package net.dahanne.android.regalandroid.utils;

/**
 * @author Anthony Dahanne
 * 
 */
public class GalleryConnectionException extends Exception {

	private static final long serialVersionUID = 7190018624500833576L;
	private final static String message = GalleryConnectionException.class
			.getName();

	/**
	 * @param msg
	 */
	public GalleryConnectionException(String msg) {
		super(message + " : " + msg);
	}

	public GalleryConnectionException(Throwable t) {
		super(t);
	}

}
