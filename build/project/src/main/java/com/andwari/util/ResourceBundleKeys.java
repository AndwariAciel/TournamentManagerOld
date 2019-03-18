package com.andwari.util;

public enum ResourceBundleKeys {
	
	EVENT("event");
	
	private String key;
	
	ResourceBundleKeys(String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	@Override
	public String toString() {
		return key;
	}

}
