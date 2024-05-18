package HotelFinder.hotelFinder;

import HotelFinder.hotelFinder.BusinessLogic.GPS;
import HotelFinder.hotelFinder.DAO.*;
import HotelFinder.hotelFinder.Model.Area;
import HotelFinder.hotelFinder.Model.Hotel;
import HotelFinder.hotelFinder.Model.Reservation;
import HotelFinder.hotelFinder.Model.Room;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
public class WebController {

    HotelDAO hotelDAO = new HotelDAO();
    RoomDAO roomDAO = new RoomDAO();
    ReservationDAO reservationDAO = new ReservationDAO();

    @PostMapping(value = "/hotels/retall", produces = "application/json")
    public String retHotelPositionsAndIdsInRange(@RequestBody Area area){
        System.out.println("Range is:" + area);
        Area degVar = GPS.degreeVariation(area);

        List<Hotel> entries = hotelDAO.findAll(new ObjectFilter(new FieldFilter[]{
                new FieldFilter("lat", false, new Object[]{
                    area.getLat().subtract(degVar.getLat()),
                    area.getLat().add(degVar.getLat()),
                }),
                new FieldFilter("lng", false, new Object[]{
                        area.getLng().subtract(degVar.getLng()),
                        area.getLng().add(degVar.getLng()),
                }),
        }));
        ArrayList<Hotel> inAreaHotels = new ArrayList<>();

        for(var entry: entries){
            if(GPS.dist(entry.getLat(), entry.getLng(), area.getLat(), area.getLng()) < area.getRange().doubleValue()){
                inAreaHotels.add(entry);
            }
        }

        String jsonString = new String("JSON building ERROR");
        try {
            jsonString = new ObjectMapper().writeValueAsString(inAreaHotels);
        } catch (Exception e){ e.printStackTrace();}

        return jsonString;
    }

    @GetMapping(value = "/hotels/retRooms-{hotelId}", produces = "application/json")
    public String retHotelRooms(@PathVariable("hotelId") int hotelId){
        System.out.println("HotelId is:" + hotelId);

        ObjectFilter filter = new ObjectFilter(new FieldFilter[]{
           new FieldFilter("hotelId", true, new Object[]{hotelId})
        });

        List<Room> rooms = roomDAO.findAll(filter);

        String jsonString = new String("JSON building ERROR");
        try {
            jsonString = new ObjectMapper().writeValueAsString(rooms);
        } catch (Exception e){ e.printStackTrace();}

        return jsonString;
    }

    @PostMapping(value = "/reservations", produces = "application/json")
    public String insReservationIfPossible(@RequestBody Reservation reservation){
        System.out.println("Received reservation:" + reservation);
        System.out.println("Reservation Valid: " + reservationDAO.isReservationValid(reservation));

        Reservation gracedReservation = new Reservation(
                reservation.getHotelId(),
                reservation.getRoomNumber(),
                reservation.getName(),
                reservation.getCheckIn().minusHours(4),
                reservation.getCheckOut().plusHours(4)
        );

        if(reservationDAO.isReservationValid(gracedReservation)){
            reservationDAO.insert(reservation);
            return "true";
        }
        else return "false";
    }
}
