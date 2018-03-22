package com.jdum.booking.webface.util;

import com.jdum.booking.common.dto.*;

import java.util.Date;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Optional.ofNullable;

/**
 * @author idumchykov
 * @since 2/26/18
 */
public class TestDataCreator {

    public static String FIRST_NAME = "TestName";
    public static String LAST_NAME = "TestSurname";
    public static String GENDER = "Male";
    public static String BUS_NUMBER = "BH100";
    public static String ORIGIN = "SFO";
    public static String DESTINATION = "NYC";
    public static String TRIP_DATE = "22-JAN-16";
    public static String PRICE_AMOUNT = "100";
    public static String CURRENCY = "USD";
    public static Long BOOK_ID = 1L;
    public static Long CHECK_IN_ID = 1L;

    public static List<TripDTO> constructTrips() {
        return newArrayList(constructTrip());
    }

    public static TripDTO constructTrip() {

        return TripDTO.builder()
                .busNumber(BUS_NUMBER)
                .origin(ORIGIN)
                .destination(DESTINATION)
                .tripDate(TRIP_DATE)
                .price(PriceDTO.builder()
                        .busNumber(BUS_NUMBER)
                        .tripDate(TRIP_DATE)
                        .priceAmount(PRICE_AMOUNT)
                        .currency(CURRENCY)
                        .build())
                .build();
    }

    public static BookingRecordDTO constructBookingDTO() {

        BookingRecordDTO bookingRecordDTO = new BookingRecordDTO()
                .setBusNumber(BUS_NUMBER)
                .setOrigin(ORIGIN)
                .setDestination(DESTINATION)
                .setBookingDate(new Date())
                .setPrice(PRICE_AMOUNT);

        PassengerDTO passenger = constructPassenger(bookingRecordDTO);
        bookingRecordDTO.setPassengers(newHashSet(passenger));

        return bookingRecordDTO;
    }

    public static PassengerDTO constructPassenger(BookingRecordDTO bookingRecordDTO) {

        PassengerDTO passenger = new PassengerDTO()
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setGender(GENDER);

        ofNullable(bookingRecordDTO).ifPresent(booking -> passenger.setBookingRecord(bookingRecordDTO));

        return passenger;
    }

    public static TripDTO constructTrip(BookingRecordDTO bookingRecordDTO) {
        return new TripDTO(bookingRecordDTO);
    }

    public static CheckInRecordDTO constructCheckInRecord() {
        return new CheckInRecordDTO()
                .setSeatNumber("28C")
                .setBookingId(BOOK_ID)
                .setBusNumber(BUS_NUMBER)
                .setCheckInTime(new Date())
                .setFirstName(FIRST_NAME)
                .setLastName(LAST_NAME)
                .setTripDate(TRIP_DATE);
    }
}
