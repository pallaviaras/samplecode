package com.ticketservice.bo;

import java.util.List;

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
