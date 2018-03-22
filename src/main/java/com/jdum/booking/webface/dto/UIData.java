package com.jdum.booking.webface.dto;

import com.jdum.booking.common.dto.PassengerDTO;
import com.jdum.booking.common.dto.SearchQuery;
import com.jdum.booking.common.dto.TripDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class UIData {
    private SearchQuery searchQuery;
    private List<TripDTO> trips;
    private TripDTO selectedTrip;
    private PassengerDTO passenger;
    private Long bookingId;

    public UIData(TripDTO selectedTrip, PassengerDTO passenger) {
        this.selectedTrip = selectedTrip;
        this.passenger = passenger;
    }

    public UIData(SearchQuery searchQuery) {
        this.searchQuery = searchQuery;
    }

}