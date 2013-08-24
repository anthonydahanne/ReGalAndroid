package fr.mael.jiwigo.om;

/*
DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
        Version 2, December 2004

Copyright (C) 2011 Mael <mael@le-guevel.com>

Everyone is permitted to copy and distribute verbatim or modified
copies of this license document, and changing it is allowed as long
as the name is changed.

DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE
TERMS AND CONDITIONS FOR COPYING, DISTRIBUTION AND MODIFICATION

0. You just DO WHAT THE FUCK YOU WANT TO.

*/
/**

* @author mael
*
*/
public class Tag {
    /**
     * Name of the tag
     */
    private String name;

    /**
     * Id of the tag
     */
    private Integer identifier;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Integer getIdentifier() {
	return identifier;
    }

    public void setIdentifier(Integer identifier) {
	this.identifier = identifier;
    }

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return name;
    }
}
