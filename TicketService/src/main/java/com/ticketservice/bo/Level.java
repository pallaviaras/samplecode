package com.ticketservice.bo;

public class Level {
 private int levelId;
 private String levelName;
 private String ticketPrice;
 private int numberOfRows;
 private int numOfSeatsPerRow;
public int getLevelId() {
	return levelId;
}
public void setLevelId(int levelId) {
	this.levelId = levelId;
}
public String getLevelName() {
	return levelName;
}
public void setLevelName(String levelName) {
	this.levelName = levelName;
}
public String getTicketPrice() {
	return ticketPrice;
}
public void setTicketPrice(String ticketPrice) {
	this.ticketPrice = ticketPrice;
}


public int getNumberOfRows() {
	return numberOfRows;
}
public void setNumberOfRows(int numberOfRows) {
	this.numberOfRows = numberOfRows;
}
public int getNumOfSeatsPerRow() {
	return numOfSeatsPerRow;
}
public void setNumOfSeatsPerRow(int numOfSeatsPerRow) {
	this.numOfSeatsPerRow = numOfSeatsPerRow;
}
@Override
public String toString() {
	return "Level [levelId=" + levelId + ", levelName=" + levelName
			+ ", ticketPrice=" + ticketPrice + ", numBerOfRows=" + numberOfRows
			+ ", numOfSeatsPerRow=" + numOfSeatsPerRow + "]";
}

}
