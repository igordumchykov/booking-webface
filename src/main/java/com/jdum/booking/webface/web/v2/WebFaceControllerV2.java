package com.jdum.booking.webface.web.v2;

import com.google.common.collect.Iterables;
import com.jdum.booking.common.dto.*;
import com.jdum.booking.webface.client.BookClient;
import com.jdum.booking.webface.client.CheckInClient;
import com.jdum.booking.webface.client.SearchClient;
import com.jdum.booking.webface.dto.UIData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.google.common.collect.Sets.newHashSet;
import static com.jdum.booking.webface.constants.Constants.*;
import static com.jdum.booking.webface.constants.REST.*;

@RestController
@RequestMapping(API_V2)
@Slf4j
@RequiredArgsConstructor
public class WebFaceControllerV2 {

    @Autowired
    private CheckInClient checkInClient;

    @Autowired
    private SearchClient searchClient;

    @Autowired
    private BookClient bookClient;

    @GetMapping(INDEX_PATH)
    public UIData index() {
        return new UIData(SearchQuery.getDefault());
    }

    @PostMapping(TRIP_SEARCH_PATH)
    public UIData searchTrip(@RequestBody UIData uiData) {

        List<TripDTO> trips = searchClient.getTrips(uiData.getSearchQuery());
        uiData.setTrips(trips);

        return uiData;
    }

    @PostMapping(BOOKING_BOOK_PATH)
    public UIData book(@RequestBody  TripDTO trip) {
        return new UIData(trip, new PassengerDTO());
    }

    @PostMapping(BOOKING_CONFIRM_PATH)
    public ResponseEntity<String> confirmBooking(@RequestBody UIData uiData) {

        TripDTO trip = uiData.getSelectedTrip();
        BookingRecordDTO booking = new BookingRecordDTO(trip);

        PassengerDTO passenger = uiData.getPassenger();
        passenger.setBookingRecord(booking);
        booking.setPassengers(newHashSet(passenger));

        Long bookingId = bookClient.create(booking);

        String msg = BOOKING_CONFIRMED_MSG + bookingId;
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @GetMapping(BOOKING_GET_PATH)
    public UIData searchBooking() {

        UIData uIData = new UIData();
        uIData.setBookingId(BOOK_ID_DISPLAY_DEFAULT);//will be displayed

        return uIData;
    }

    @PostMapping(BOOKING_GET_DETAILS_PATH)
    public UIData getBookingDetails(@RequestBody UIData uiData) {

        Long id = uiData.getBookingId();
        BookingRecordDTO booking = bookClient.getBookingRecord(id);
        TripDTO trip = new TripDTO(booking);

        PassengerDTO passenger = Iterables.getFirst(booking.getPassengers(), null);
        uiData.setPassenger(passenger);
        uiData.setSelectedTrip(trip);
        uiData.setBookingId(id);

        return uiData;
    }

    @PostMapping(BOOKING_CHECK_IN_PATH)
    public ResponseEntity<String> checkInBookingRecord(@RequestBody CheckInRecordDTO checkIn) {

        checkIn.setSeatNumber(SEAT_NUMBER);// TODO: 2/14/18 add logic to generate seat number automatically
        Long checkInId = checkInClient.create(checkIn);

        String msg = BOOKING_CHECK_IN_MSG + checkInId;
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}