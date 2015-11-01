package com.ticketservice.services;

import com.ticketservice.bo.SeatHold;
import com.ticketservice.interfaces.TicketService;

public class TicketServiceImpl implements TicketService{ 
	
	private DataService dataService ;
	/**
	* The number of seats in the requested level that are neither held nor reserved
	*
	* @param venueLevel a numeric venue level identifier to limit the search
	* @return the number of tickets available on the provided level
	*/
	  public int numSeatsAvailable(Integer venueLevel){
		  return dataService.numSeatsAvailable(venueLevel);
	  }
	/**
	* Find and hold the best available seats for a customer
	*
	* @param numSeats the number of seats to find and hold
	* @param minLevel the minimum venue level
	* @param maxLevel the maximum venue level
	* @param customerEmail unique identifier for the customer
	* @return a SeatHold object identifying the specific seats and related
	information
	*/
	  public SeatHold findAndHoldSeats(int numSeats, Integer minLevel,
	Integer maxLevel, String customerEmail){
		  return dataService.findAndHoldSeats(numSeats, minLevel,
					maxLevel,  customerEmail);
		  
		  
	  }
	/**
	* Commit seats held for a specific customer
	*
	* @param seatHoldId the seat hold identifier
	* @param customerEmail the email address of the customer to which the seat hold
	is assigned
	* @return a reservation confirmation code
	*/
	public String reserveSeats(long seatHoldId, String customerEmail){
		 return dataService.reserveSeats(seatHoldId,customerEmail);
	}
	
	public SeatHold retreiveFinalBooking(String confirmId){
		return dataService.retreiveFinalBooking(confirmId);
	}
	public DataService getDataService() {
		return dataService;
	}
	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}
	
}
