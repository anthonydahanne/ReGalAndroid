package net.dahanne.gallery3.client.model;

import java.util.Collection;
import java.util.HashSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Tags {
	public String url;
	public final Collection<String> members = new HashSet<String>();
}
