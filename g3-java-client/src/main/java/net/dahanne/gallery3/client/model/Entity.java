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

public class Entity {

	private int id;
	private long captured;
	private long created;
	private String description;
	private int height;
	private int level;
	private String mimeType;
	private String name;
	private int ownerId;
	private float randKey;
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
	private int view1;
	private int view2;
	private int parent;
	private String fileUrl;
	private String resizeUrl;
	private String thumbUrl;
	private boolean canEdit;
	private String albumCover;
	private String webUrl;
	private String thumbUrlPublic;
	private int thumbSize;
	private String type;
	private int fileSize;
	private int resizeSize;
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getResizeSize() {
		return resizeSize;
	}
	public void setResizeSize(int resizeSize) {
		this.resizeSize = resizeSize;
	}
	public String getFileUrlPublic() {
		return fileUrlPublic;
	}
	public void setFileUrlPublic(String fileUrlPublic) {
		this.fileUrlPublic = fileUrlPublic;
	}
	public String getResizeUrlPublic() {
		return resizeUrlPublic;
	}
	public void setResizeUrlPublic(String resizeUrlPublic) {
		this.resizeUrlPublic = resizeUrlPublic;
	}
	private String fileUrlPublic;
	private String resizeUrlPublic;
	
	
	
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getAlbumCover() {
		return albumCover;
	}
	public void setAlbumCover(String albumCover) {
		this.albumCover = albumCover;
	}
	public String getWebUrl() {
		return webUrl;
	}
	public void setWebUrl(String webUrl) {
		this.webUrl = webUrl;
	}
	public String getThumbUrlPublic() {
		return thumbUrlPublic;
	}
	public void setThumbUrlPublic(String thumbUrlPublic) {
		this.thumbUrlPublic = thumbUrlPublic;
	}
	public int getThumbSize() {
		return thumbSize;
	}
	public void setThumbSize(int thumbSize) {
		this.thumbSize = thumbSize;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	public long getCaptured() {
		return captured;
	}
	public void setCaptured(long captured) {
		this.captured = captured;
	}
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
	public int getOwnerId() {
		return ownerId;
	}
	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}
	public int getView1() {
		return view1;
	}
	public void setView1(int view1) {
		this.view1 = view1;
	}
	public int getView2() {
		return view2;
	}
	public void setView2(int view2) {
		this.view2 = view2;
	}
	public float getRandKey() {
		return randKey;
	}
	public void setRandKey(float randKey) {
		this.randKey = randKey;
	}
	public int getResizeHeight() {
		return resizeHeight;
	}
	public void setResizeHeight(int resizeHeight) {
		this.resizeHeight = resizeHeight;
	}
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
	public String getSortColumn() {
		return sortColumn;
	}
	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}
	public String getSortOrder() {
		return sortOrder;
	}
	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}
	public int getThumbHeight() {
		return thumbHeight;
	}
	public void setThumbHeight(int thumbHeight) {
		this.thumbHeight = thumbHeight;
	}
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
	public int getParent() {
		return parent;
	}
	public void setParent(int parent) {
		this.parent = parent;
	}
	public String getResizeUrl() {
		return resizeUrl;
	}
	public void setResizeUrl(String resizeUrl) {
		this.resizeUrl = resizeUrl;
	}
	public String getThumbUrl() {
		return thumbUrl;
	}
	public void setThumbUrl(String thumUrl) {
		this.thumbUrl = thumUrl;
	}
	public boolean isCanEdit() {
		return canEdit;
	}
	public void setCanEdit(boolean canEdit) {
		this.canEdit = canEdit;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Entity other = (Entity) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Entity [id=");
		builder.append(id);
		builder.append(", captured=");
		builder.append(captured);
		builder.append(", created=");
		builder.append(created);
		builder.append(", description=");
		builder.append(description);
		builder.append(", height=");
		builder.append(height);
		builder.append(", level=");
		builder.append(level);
		builder.append(", mimeType=");
		builder.append(mimeType);
		builder.append(", name=");
		builder.append(name);
		builder.append(", ownerId=");
		builder.append(ownerId);
		builder.append(", randKey=");
		builder.append(randKey);
		builder.append(", resizeHeight=");
		builder.append(resizeHeight);
		builder.append(", resizeWidth=");
		builder.append(resizeWidth);
		builder.append(", slug=");
		builder.append(slug);
		builder.append(", sortColumn=");
		builder.append(sortColumn);
		builder.append(", sortOrder=");
		builder.append(sortOrder);
		builder.append(", thumbHeight=");
		builder.append(thumbHeight);
		builder.append(", thumbWidth=");
		builder.append(thumbWidth);
		builder.append(", title=");
		builder.append(title);
		builder.append(", updated=");
		builder.append(updated);
		builder.append(", viewCount=");
		builder.append(viewCount);
		builder.append(", width=");
		builder.append(width);
		builder.append(", view1=");
		builder.append(view1);
		builder.append(", view2=");
		builder.append(view2);
		builder.append(", parent=");
		builder.append(parent);
		builder.append(", fileUrl=");
		builder.append(fileUrl);
		builder.append(", resizeUrl=");
		builder.append(resizeUrl);
		builder.append(", thumbUrl=");
		builder.append(thumbUrl);
		builder.append(", canEdit=");
		builder.append(canEdit);
		builder.append(", albumCover=");
		builder.append(albumCover);
		builder.append(", webUrl=");
		builder.append(webUrl);
		builder.append(", thumbUrlPublic=");
		builder.append(thumbUrlPublic);
		builder.append(", thumbSize=");
		builder.append(thumbSize);
		builder.append(", type=");
		builder.append(type);
		builder.append(", fileSize=");
		builder.append(fileSize);
		builder.append(", resizeSize=");
		builder.append(resizeSize);
		builder.append(", fileUrlPublic=");
		builder.append(fileUrlPublic);
		builder.append(", resizeUrlPublic=");
		builder.append(resizeUrlPublic);
		builder.append("]");
		return builder.toString();
	}
	
	
	
	
}
