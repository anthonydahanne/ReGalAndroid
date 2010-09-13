package net.dahanne.gallery3.client.model;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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

	public Collection<String> getMembers() {
		return members;
	}

}
