package HotelFinder.hotelFinder.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @Getter @Setter Integer hotelId;
    @Getter @Setter Integer roomNumber;
    @Getter @Setter String name;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    @Getter @Setter LocalDateTime checkIn;
    @DateTimeFormat(fallbackPatterns = "yyyy-MM-dd HH:mm:ss")
    @Getter @Setter LocalDateTime checkOut;

    @Override
    public String toString() {
        return "Reservation{" +
                "hotelId=" + hotelId +
                ", roomNr=" + roomNumber +
                ", name='" + name + '\'' +
                ", checkIn=" + checkIn +
                ", checkOut=" + checkOut +
                '}';
    }
}
