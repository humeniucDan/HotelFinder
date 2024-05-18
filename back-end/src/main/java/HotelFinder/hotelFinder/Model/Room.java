package HotelFinder.hotelFinder.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Getter @Setter private Integer hotelId;
    @Getter @Setter private Integer roomNumber;
    @Getter @Setter private Integer type;
    @Getter @Setter private BigDecimal price;

    @Override
    public String toString() {
        return "Room{" +
                "hotelId=" + hotelId +
                ", roomNumber=" + roomNumber +
                ", type=" + type +
                ", price=" + price +
                '}';
    }
}
