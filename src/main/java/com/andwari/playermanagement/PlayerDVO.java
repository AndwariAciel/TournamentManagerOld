package com.andwari.playermanagement;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerDVO {
	
	private SimpleStringProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty dci;
	private SimpleBooleanProperty member;
	
	public PlayerDVO() {
		id = new SimpleStringProperty();
		name = new SimpleStringProperty();
		dci = new SimpleStringProperty();
		member = new SimpleBooleanProperty();
	}
	
	public String getId() {
		return id.get();
	}
	public void setId(String id) {
		this.id.set(id);;
	}
	public String getName() {
		return name.get();
	}
	public void setName(String name) {
		this.name.set(name);
	}
	public String getDci() {
		return dci.get();
	}
	public void setDci(String dci) {
		this.dci.set(dci);
	}

	public SimpleStringProperty idProperty() {
		return id;
	}
	
	public SimpleStringProperty nameProperty() {
		return name;
	}
	
	public SimpleStringProperty dciProperty() {
		return dci;
	}

	public SimpleBooleanProperty memberProperty() {
		return member;
	}

	public void setMember(boolean member) {
		this.member.set(member);
	}
	
	public Boolean getMember() {
		return member.get();
	}
}
