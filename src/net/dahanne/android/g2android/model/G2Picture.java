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
	private String thumbImagePath;

	private String resizedName;
	private int resizedWidth;
	private int resizedHeight;
	private String resizedImagePath;

	private String name;
	private int rawFilesize;
	private int rawWidth;
	private int rawHeight;

	private String caption;
	private String forceExtension;
	private boolean hidden;

	private int imageClicks;

	// private String captureDateYear;
	// private String captureDateMonth;
	// private String captureDateDay;
	// private String captureDateHour;
	// private String captureDateMinute;
	// private String captureDateSecond;

	public int getImageClicks() {
		return imageClicks;
	}

	public void setImageClicks(int imageClicks) {
		this.imageClicks = imageClicks;
	}

	//
	// public String getCaptureDateYear() {
	// return captureDateYear;
	// }
	//
	// public void setCaptureDateYear(String captureDateYear) {
	// this.captureDateYear = captureDateYear;
	// }
	//
	// public String getCaptureDateMonth() {
	// return captureDateMonth;
	// }
	//
	// public void setCaptureDateMonth(String captureDateMonth) {
	// this.captureDateMonth = captureDateMonth;
	// }
	//
	// public String getCaptureDateDay() {
	// return captureDateDay;
	// }
	//
	// public void setCaptureDateDay(String captureDateDay) {
	// this.captureDateDay = captureDateDay;
	// }
	//
	// public String getCaptureDateHour() {
	// return captureDateHour;
	// }
	//
	// public void setCaptureDateHour(String captureDateHour) {
	// this.captureDateHour = captureDateHour;
	// }
	//
	// public String getCaptureDateMinute() {
	// return captureDateMinute;
	// }
	//
	// public void setCaptureDateMinute(String captureDateMinute) {
	// this.captureDateMinute = captureDateMinute;
	// }
	//
	// public String getCaptureDateSecond() {
	// return captureDateSecond;
	// }
	//
	// public void setCaptureDateSecond(String captureDateSecond) {
	// this.captureDateSecond = captureDateSecond;
	// }

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

	public void setResizedImagePath(String resizedImagePath) {
		this.resizedImagePath = resizedImagePath;
	}

	public String getResizedImagePath() {
		return resizedImagePath;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getForceExtension() {
		return forceExtension;
	}

	public void setForceExtension(String forceExtension) {
		this.forceExtension = forceExtension;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public void setThumbImagePath(String thumbImagePath) {
		this.thumbImagePath = thumbImagePath;
	}

	public String getThumbImagePath() {
		return thumbImagePath;
	}

	@Override
	public String toString() {

		return new StringBuilder().append(title).append(name).toString();
	}

}
