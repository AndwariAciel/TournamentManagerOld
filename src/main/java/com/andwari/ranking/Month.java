package com.andwari.ranking;

public enum Month {

	NA("<no month>", -1), JAN("January", 0), FEB("February", 1), MAR("March", 2), APR("April", 3), MAY("May", 4),
	JUN("June", 5), JUL("July", 6), AUG("August", 7), SEP("September", 8), OCT("October", 9), NOV("November", 10),
	DEC("December", 11);

	private String name;
	private int number;

	private Month(String name, int number) {
		this.name = name;
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public int getNumber() {
		return number;
	}

	public String toString() {
		return name;
	}
}
