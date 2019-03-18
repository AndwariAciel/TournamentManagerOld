package com.andwari.playermanagement;

import javafx.beans.property.SimpleStringProperty;

public class PlayerDVO {
	
	private SimpleStringProperty id;
	private SimpleStringProperty name;
	private SimpleStringProperty dci;
	
	public PlayerDVO() {
		id = new SimpleStringProperty();
		name = new SimpleStringProperty();
		dci = new SimpleStringProperty();
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
}
