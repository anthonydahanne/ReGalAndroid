/*
 * G2Android
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
package net.dahanne.android.g2android.model;

import java.io.Serializable;

public class G2Picture implements Serializable {

	private static final long serialVersionUID = 4719347243965813169L;
	private long id;
	private String title;

	private String thumbName;
	private int thumbWidth;
	private int thumbHeight;

	private String resizedName;
	private int resizedWidth;
	private int resizedHeight;

	private String name;
	private int rawFilesize;
	private int rawWidth;
	private int rawHeight;

	public G2Picture() {
		super();
	}

	public G2Picture(String title, String thumbName, int thumbWidth,
			int thumbHeight, String resizedName, int resizedWidth,
			int resizedHeight, String name, int rawFilesize, int rawWidth,
			int rawHeight, long id) {
		super();
		this.title = title;
		this.thumbName = thumbName;
		this.thumbWidth = thumbWidth;
		this.thumbHeight = thumbHeight;
		this.resizedName = resizedName;
		this.resizedWidth = resizedWidth;
		this.resizedHeight = resizedHeight;
		this.name = name;
		this.rawFilesize = rawFilesize;
		this.rawWidth = rawWidth;
		this.rawHeight = rawHeight;
		this.setId(id);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getThumbName() {
		return thumbName;
	}

	public void setThumbName(String thumbName) {
		this.thumbName = thumbName;
	}

	public int getThumbWidth() {
		return thumbWidth;
	}

	public void setThumbWidth(int thumbWidth) {
		this.thumbWidth = thumbWidth;
	}

	public int getThumbHeight() {
		return thumbHeight;
	}

	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getResizedName() {
		return resizedName;
	}

	public void setResizedName(String resizedName) {
		this.resizedName = resizedName;
	}

	public int getResizedWidth() {
		return resizedWidth;
	}

	public void setResizedWidth(int resizedWidth) {
		this.resizedWidth = resizedWidth;
	}

	public int getResizedHeight() {
		return resizedHeight;
	}

	public void setResizedHeight(int resizedHeight) {
		this.resizedHeight = resizedHeight;
	}

	public int getRawFilesize() {
		return rawFilesize;
	}

	public void setRawFilesize(int rawFilesize) {
		this.rawFilesize = rawFilesize;
	}

	public int getRawWidth() {
		return rawWidth;
	}

	public void setRawWidth(int rawWidth) {
		this.rawWidth = rawWidth;
	}

	public int getRawHeight() {
		return rawHeight;
	}

	public void setRawHeight(int rawHeight) {
		this.rawHeight = rawHeight;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	@Override
	public String toString() {

		return new StringBuilder().append(title).append(name).toString();
	}

}
