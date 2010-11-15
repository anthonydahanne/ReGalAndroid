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

package net.dahanne.gallery.commons.model;

import java.io.Serializable;

public class Picture implements Serializable {

	private static final long serialVersionUID = 4719347243965813169L;
	private long id;
	private String title;
	private String name;

	private String thumbUrl;
	private int thumbWidth;
	private int thumbHeight;
	private int thumbSize;
	private String thumbImageCachePath;

	private String resizedUrl;
	private int resizedWidth;
	private int resizedHeight;
	private int resizedSize;
	private String resizedImageCachePath;

	private String fileUrl;
	private int width;
	private int height;
	private int fileSize;

	private String caption;
	private String forceExtension;
	private boolean hidden;

//	private int imageClicks;

	// private String captureDateYear;
	// private String captureDateMonth;
	// private String captureDateDay;
	// private String captureDateHour;
	// private String captureDateMinute;
	// private String captureDateSecond;

//	public int getImageClicks() {
//		return imageClicks;
//	}
//
//	public void setImageClicks(int imageClicks) {
//		this.imageClicks = imageClicks;
//	}

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

	public String getThumbUrl() {
		return thumbUrl;
	}

	public void setThumbUrl(String thumbName) {
		this.thumbUrl = thumbName;
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

	public String getResizedUrl() {
		return resizedUrl;
	}

	public void setResizedUrl(String resizedName) {
		this.resizedUrl = resizedName;
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

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int rawFilesize) {
		this.fileSize = rawFilesize;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int rawWidth) {
		this.width = rawWidth;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int rawHeight) {
		this.height = rawHeight;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setResizedImageCachePath(String resizedImagePath) {
		this.resizedImageCachePath = resizedImagePath;
	}

	public String getResizedImageCachePath() {
		return resizedImageCachePath;
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

	public void setThumbImageCachePath(String thumbImagePath) {
		this.thumbImageCachePath = thumbImagePath;
	}

	public String getThumbImageCachePath() {
		return thumbImageCachePath;
	}


	public int getThumbSize() {
		return thumbSize;
	}

	public void setThumbSize(int thumbSize) {
		this.thumbSize = thumbSize;
	}

	public int getResizedSize() {
		return resizedSize;
	}

	public void setResizedSize(int resizeSize) {
		this.resizedSize = resizeSize;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	
	
	
	@Override
	public String toString() {
		return new StringBuilder().append(title).append(name).toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fileSize;
		result = prime * result + ((fileUrl == null) ? 0 : fileUrl.hashCode());
		result = prime * result + height;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + resizedHeight;
		result = prime * result + resizedSize;
		result = prime * result
				+ ((resizedUrl == null) ? 0 : resizedUrl.hashCode());
		result = prime * result + resizedWidth;
		result = prime * result + thumbHeight;
		result = prime * result + thumbSize;
		result = prime * result
				+ ((thumbUrl == null) ? 0 : thumbUrl.hashCode());
		result = prime * result + thumbWidth;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Picture other = (Picture) obj;
		if (fileSize != other.fileSize)
			return false;
		if (fileUrl == null) {
			if (other.fileUrl != null)
				return false;
		} else if (!fileUrl.equals(other.fileUrl))
			return false;
		if (height != other.height)
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (resizedHeight != other.resizedHeight)
			return false;
		if (resizedSize != other.resizedSize)
			return false;
		if (resizedUrl == null) {
			if (other.resizedUrl != null)
				return false;
		} else if (!resizedUrl.equals(other.resizedUrl))
			return false;
		if (resizedWidth != other.resizedWidth)
			return false;
		if (thumbHeight != other.thumbHeight)
			return false;
		if (thumbSize != other.thumbSize)
			return false;
		if (thumbUrl == null) {
			if (other.thumbUrl != null)
				return false;
		} else if (!thumbUrl.equals(other.thumbUrl))
			return false;
		if (thumbWidth != other.thumbWidth)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (width != other.width)
			return false;
		return true;
	}
	

}
