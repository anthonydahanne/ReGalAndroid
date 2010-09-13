package net.dahanne.gallery3.client.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RelationShips {
	private Tags tags;

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public Tags getTags() {
		return tags;
	}
}
