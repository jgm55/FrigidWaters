package edu.drexel.cci.hiyh.has.driver.insteon;

public class Command {
	private String name;
	private char house;
	private int id;

	public Command(String name, char house, int id) {
		this.name = name;
		this.house = house;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public char getHouse() {
		return house;
	}

	public int getId() {
		return id;
	}
}