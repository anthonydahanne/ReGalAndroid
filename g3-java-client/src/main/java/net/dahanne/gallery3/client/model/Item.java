/**
 *  g3-java-client, a Menalto Gallery3 Java Client API
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

package net.dahanne.gallery3.client.model;

import java.util.Collection;
import java.util.HashSet;


public class Item {

	private String url;
	private Entity entity;
	private RelationShips relationships;
	private final Collection<String> members = new HashSet<String>();

	public Entity getEntity() {
		return entity;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public RelationShips getRelationships() {
		return relationships;
	}

	public void setRelationships(RelationShips relationships) {
		this.relationships = relationships;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	/**
	 * members are the sub items urls of the current item(which has to be an
	 * album, as only albums have members)
	 * 
	 * @return
	 */
	public Collection<String> getMembers() {
		return members;
	}
	@Override
	public String toString() {
		return new StringBuilder().append(" url : ").append(url)
		.append(" entity : ").append(entity.toString() )
		.toString();
	}
	

}
