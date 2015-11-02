package com.ticketservice.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.ticketservice.bo.Level;
import com.ticketservice.bo.Seat;
import com.ticketservice.bo.SeatHold;

/**
 * Basic service to create data model of Levels and seat Map. In a real life
 * this will be implemented by a DB process that writes and retrieves from
 * a Database. The seat Map will be a DB table .
 * 
 * This class is the main class that executes various actions on the SeatMap.
 * It allows the seats to be put on hold for a customer, reserve seats and to remove seat hold after reservation time expires.
 * 
 * 
 * @author apallavi
 * 
 */
public class DataService {
	private int reservationHoldTimeInSec = 10;
	private static Map<Integer, List<List<Seat>>> seatMap = new HashMap<Integer, List<List<Seat>>>();
	private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(
			10);
	private static List<Level> venueLevels = new ArrayList<Level>();
	private Map<Long, ScheduledFuture<?>> futureMap = new HashMap<Long, ScheduledFuture<?>>();
	private Map<Long, SeatHold> seatHeld = new HashMap<Long, SeatHold>();
	private String levels;

	public DataService(String levels) {
		this.levels = levels;
		init();
	}

	private void init() {
		initLevels();
		initSeatMap();

	}

	public int getReservationHoldTimeInSec() {
		return reservationHoldTimeInSec;
	}

	public void setReservationHoldTimeInSec(int reservationHoldTimeInSec) {
		this.reservationHoldTimeInSec = reservationHoldTimeInSec;
	}

	private void initLevels() {
		// System.out.println("Intialize Levels==>" + levels);
		String[] levelArr = levels.split("\\|");
		for (String s : levelArr) {
			// System.out.println("eachLevel:" + s);
			String[] eachLevelarr = s.split(":");

			Level level = new Level();
			level.setLevelId(Integer.valueOf(eachLevelarr[0]));
			level.setLevelName(eachLevelarr[1]);
			level.setTicketPrice(eachLevelarr[2]);
			level.setNumberOfRows(Integer.valueOf(eachLevelarr[3]));
			level.setNumOfSeatsPerRow(Integer.valueOf(eachLevelarr[4]));
			venueLevels.add(level);
		}

	}

	// would be a table in real life. Dummy method to load data in absence of
	// DB.
	private void initSeatMap() {
		// System.out.println("Initialize VenueLevels==>" + venueLevels.size());
		for (Level level : venueLevels) {
			// System.out.println("Each Level==>" + level);
			List<List<Seat>> levelList = new ArrayList<List<Seat>>();
			for (int i = 1; i <= level.getNumberOfRows(); i++) {
				List<Seat> rowList = new LinkedList<Seat>();
				for (int j = 1; j <= level.getNumOfSeatsPerRow(); j++) {
					Seat eachSeat = new Seat();
					eachSeat.setAvailable(true);
					eachSeat.setRowId(i);
					eachSeat.setLevelId(level.getLevelId());
					eachSeat.setSeatNumber(j);
					rowList.add(eachSeat);
				}// end NumRow
				levelList.add(rowList);
			}// end NumberSeat
			seatMap.put(level.getLevelId(), levelList);
		}// end Level
			//System.out.println("Initialized seatMap==>" + seatMap);
			//System.out.println("");

	}// end Init

	// better implentation with 1.8 Java
	public int numSeatsAvailable(Integer venueLevel) {
		int totalAvailable = 0;

		if (venueLevel != null) {
			List<List<Seat>> levelList = seatMap.get(venueLevel);
			for (List<Seat> eachRow : levelList) {
				for (Seat eachSeat : eachRow) {
					if (eachSeat.isAvailable()) {
						totalAvailable++;
					}
				}
			}
		}// if

		return totalAvailable;

	}

	/**
	 * 
	 * Choose best available seats. Strategy is to find seats closest to the
	 * stage which translates to the lowest level posssible and the lowest row
	 * 
	 * @param numSeats
	 * @param minLevel
	 * @param maxLevel
	 * @param customerEmail
	 * @return
	 */
	public SeatHold findAndHoldSeats(int numSeats, Integer minLevel,
			Integer maxLevel, String customerEmail) {
		System.out
				.println("Booking order  for-->" + customerEmail
						+ " Number of Seats: " + numSeats + " startLevel : "
						+ minLevel);

		final SeatHold seatHold = new SeatHold();
		List<Seat> heldSeats = new ArrayList<Seat>();
		seatHold.setSeats(heldSeats);
		seatHold.setCustomerEmail(customerEmail);
		seatHold.setHoldId(Long.valueOf(System.currentTimeMillis() + (int)(500000* Math.random())));
		
		// From Min to max level speciied, search for available seats
		if (minLevel != null && maxLevel != null) {
			outer : while (maxLevel > minLevel) {
				List<List<Seat>> levelList = seatMap.get(minLevel);
			   // System.out.println("Each level==>" + minLevel +  " level list: " + levelList);
				for (List<Seat> eachRow : levelList) {
					for (Seat eachSeat : eachRow) {
						eachSeat.selectSeatIfAvailable(heldSeats);
						// If  number of seats that we want to be held has reached then no need to search.
						if (heldSeats.size() >= numSeats) {
							break outer;
						}
					}
				}
				//System.out.println("Last level " + minLevel + " Moving to next level " + ++minLevel );
			}
		}

		// Put the seathold's unblock in a runnable such that the seathold can be expired in a configurable time duration.
		Runnable unblockTask = new Runnable() {
			public void run() {
				seatHeld.remove(seatHold.getHoldId());
				seatHold.unblock();
			}
		};
        // Pass runnable to a executor with a configurable delay.
		ScheduledFuture<?> future = executor.schedule(unblockTask,
				reservationHoldTimeInSec, TimeUnit.SECONDS);
		
		// A map to indicate which seat hold are to be expired
		futureMap.put(seatHold.getHoldId(), future);
		seatHeld.put(seatHold.getHoldId(), (seatHold));
		return seatHold;

	}
	/**
	 * Method to reserve seats
	 * 
	 * 
	 * @param seatHoldId
	 * @param customerEmail
	 * @return
	 */
	public String reserveSeats(long seatHoldId, String customerEmail) {
		
		//When a confirmation order comes, the corresponding unblock future task is cleared.
		ScheduledFuture<?> future = futureMap.get(seatHoldId);
		future.cancel(true);
		SeatHold seatheld = seatHeld.get(seatHoldId);
		String confirmationString = "";
		// Confirmation code is generated.
		if (seatheld != null) {
			confirmationString = "confirm" + seatHoldId;
		} else {
			confirmationString = " Booking expired: " + seatHoldId;
		}
		return confirmationString;
	}

	public void shutDown() {
		executor.shutdownNow();

	}
	/**
	 * 
	 * Method retrieves the booking based in ConfirmId.
	 * 
	 * @param confirmId
	 */
	public String retreiveFinalBooking(String confirmId) {
		String bookingInfo= "";
		
		if (confirmId!=null && confirmId.contains("confirm")) {
			String holdId = confirmId.replaceAll("confirm", "");
			SeatHold seathold = seatHeld.get(Long.valueOf(holdId));
			bookingInfo = seathold.toString();
			
		} else {
			bookingInfo =" No booking to retrieve "  ;
		}
		return bookingInfo;
	}

	public void finalState() {
		System.out.println("seatHeld" + seatHeld);
		System.out.println("");
	}

}// end Class

