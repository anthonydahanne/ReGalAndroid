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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

	private int id;
	private int name;
	private String title;
	private String summary;
	private int parentName;
	private boolean add;
	private boolean write;
	private boolean deleteAlbum;
	private boolean createSubAlbum;
	private String extrafields;
	private String albumUrl;
	private Album parent;

	private List<Album> children = new ArrayList<Album>();

	public Album() {
		super();
	}

	private static final long serialVersionUID = -671355798682957050L;

	public Album(int id, int name, String title, String summary, int parent,
			boolean add, boolean write, boolean deleteAlbum,
			boolean createSubAlbum, String extrafields) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
		this.summary = summary;
		this.parentName = parent;
		this.add = add;
		this.write = write;
		this.deleteAlbum = deleteAlbum;
		this.createSubAlbum = createSubAlbum;
		this.extrafields = extrafields;
	}

	public Album(int id) {
		this.id = id;
	}

	public int getName() {
		return name;
	}

	public void setName(int name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getParentName() {
		return parentName;
	}

	public void setParentName(int parentName) {
		this.parentName = parentName;
	}

	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isWrite() {
		return write;
	}

	public void setWrite(boolean write) {
		this.write = write;
	}

	public boolean isDeleteAlbum() {
		return deleteAlbum;
	}

	public void setDeleteAlbum(boolean deleteAlbum) {
		this.deleteAlbum = deleteAlbum;
	}

	public boolean isCreateSubAlbum() {
		return createSubAlbum;
	}

	public void setCreateSubAlbum(boolean createSubAlbum) {
		this.createSubAlbum = createSubAlbum;
	}

	public String getExtrafields() {
		return extrafields;
	}

	public void setExtrafields(String extrafields) {
		this.extrafields = extrafields;
	}

	public void setAlbumUrl(String albumUrl) {
		this.albumUrl = albumUrl;
	}

	public String getAlbumUrl() {
		return albumUrl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Album getParent() {
		return parent;
	}

	public void setParent(Album parent) {
		this.parent = parent;
	}

	public List<Album> getChildren() {
		return children;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (name == 0 ? 0 : prime);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Album other = (Album) obj;

		if (name == 0) {
			if (other.name != 0) {
				return false;
			}
		} else if (name != other.name) {
			return false;
		}

		return true;
	}

	@Override
	public String toString() {
		// return new StringBuilder().append("name : ").append(name).append(
		// " title : ").append(title).append(" parentId : ").append(
		// parentName).toString();

		return new StringBuilder().append(title).toString();
	}

	/**
	 * @param myAlbum
	 */
	public byte[] serialize() {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(this);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output.toByteArray();

	}

	public static Album unserializeAlbum(byte[] serializedAlbum) {
		Album album = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(
					serializedAlbum);
			ObjectInputStream ois = new ObjectInputStream(bais);
			album = (Album) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			// Log.e("PLOUF", e.getLocalizedMessage());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return album;

	}

}
