package com.ticketservice.bo;

import java.util.List;

/**
 * Class that hold information about each Seat. 
 * Class has a synchronized method that checks the availabilty of that instance and sets itself as unavailable.
 * 
 * @author apallavi
 *
 */
public class Seat {
 private int levelId;
 private int rowId;
 private int seatNumber;

 boolean available;
public int getRowId() {
	return rowId;
}
public void setRowId(int rowId) {
	this.rowId = rowId;
}

public int getSeatNumber() {
	return seatNumber;
}
public void setSeatNumber(int seatNumber) {
	this.seatNumber = seatNumber;
}
public boolean isAvailable() {
	return available;
}

// This method is synchronized so two threads cannot hold same set of seats.
public synchronized void selectSeatIfAvailable(List<Seat> heldSeats) {
	if (this.isAvailable()){
		heldSeats.add(this);
		this.available=false;
	}
}

public void setAvailable(boolean available) {
	this.available = available;
}
public int getLevelId() {
	return levelId;
}
public void setLevelId(int levelId) {
	this.levelId = levelId;
}

@Override
public String toString() {
	return "Seat [levelId=" + levelId + ", rowId=" + rowId + ", seatNumber="
			+ seatNumber + ", available=" + available + "]";
}

 
}
