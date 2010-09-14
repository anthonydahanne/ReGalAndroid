/**
 *  Gallery3-java-client
 *  URLs: http://github.com/anthonydahanne/g3-java-client , http://blog.dahanne.net
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
package net.dahanne.gallery3.client.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Entity {

	private int id;
//	private long captured;
	private long created;
	private String description;
	private int height;
	private int level;
	private String mimeType;
	private String name;
	private int ownerId;
//	private float randKey;
	private int resizeHeight;
	private int resizeWidth;
	private String slug;
	private String sortColumn;
	private String sortOrder;
	private int thumbHeight;
	private int thumbWidth;
	private String title;
	private long updated;
	private int viewCount;
	private int width;
	//view_1, view2 ?
	private String parent;
	private String fileUrl;
	private String resizeUrl;
	private String thumbUrl;
	private boolean canEdit;
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
//	public long getCaptured() {
//		return captured;
//	}
//	public void setCaptured(long captured) {
//		this.captured = captured;
//	}
	public long getCreated() {
		return created;
	}
	public void setCreated(long created) {
		this.created = created;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	@XmlElement(name="mime_type") 
	public String getMimeType() {
		return mimeType;
	}
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@XmlElement(name="owner_id") 
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
//	@XmlElement(name="rand_key")
//	public float getRandKey() {
//		return randKey;
//	}
//	public void setRandKey(float randKey) {
//		this.randKey = randKey;
//	}
	@XmlElement(name="resize_height") 
	public int getResizeHeight() {
		return resizeHeight;
	}
	public void setResizeHeight(int resizeHeight) {
		this.resizeHeight = resizeHeight;
	}
	@XmlElement(name="resize_width")
	public int getResizeWidth() {
		return resizeWidth;
	}
	public void setResizeWidth(int resizeWidth) {
		this.resizeWidth = resizeWidth;
	}
	public String getSlug() {
		return slug;
	}
	public void setSlug(String slug) {
		this.slug = slug;
	}
	@XmlElement(name="sort_column")
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	@XmlElement(name="sort_order")
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	@XmlElement(name="thumb_height")
	public int getThumbHeight() {
		return thumbHeight;
	}
	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}
	@XmlElement(name="thumb_width")
	public int getThumbWidth() {
		return thumbWidth;
	}
	public void setThumbWidth(int thumbWidth) {
		this.thumbWidth = thumbWidth;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public long getUpdated() {
		return updated;
	}
	public void setUpdated(long updated) {
		this.updated = updated;
	}
	@XmlElement(name="view_count")
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	@XmlElement(name="resize_url")
	public String getResizeUrl() {
		return resizeUrl;
	}
	public void setResizeUrl(String resizeUrl) {
		this.resizeUrl = resizeUrl;
	}
	@XmlElement(name="thumb_url")
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumUrl) {
		this.thumbUrl = thumUrl;
	}
	@XmlElement(name="can_edit")
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	@XmlElement(name="file_url")
	public String getFileUrl() {
		return fileUrl;
	}
	
}
