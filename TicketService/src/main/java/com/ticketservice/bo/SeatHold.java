package com.ticketservice.bo;

import java.util.List;
/**
 * 
 * Class that hold information about the list of seats held as a part of a booking. It contains customers emails and hold id associated with the reservation
 * Class also contains a unblock method to unblock the seats that it contains.
 * 
 * @author apallavi
 *
 */
public class SeatHold {
	private long holdId;
	String customerEmail;
private List<Seat> seats;
public List<Seat> getSeats() {
	return seats;
}
public void setSeats(List<Seat> seats) {
	this.seats = seats;
}

public long getHoldId() {
	return holdId;
}
public void setHoldId(long holdId) {
	this.holdId = holdId;
}
public String getCustomerEmail() {
	return customerEmail;
}
public void setCustomerEmail(String customerEmail) {
	this.customerEmail = customerEmail;
}

@Override
public String toString() {
	return " Customer: "  + customerEmail +  " hold ID: " + holdId
			+ ", seats=" + seats + "]";
}
public void unblock(){
	//System.out.println("Remove from Hold:  "  + this);
	for (Seat eachSeat: seats){
		eachSeat.setAvailable(true);
	}
	System.out.println("Remove from Hold:  "  + this);
}


}
