package HotelFinder.hotelFinder.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Getter @Setter private Integer id;
    @Getter @Setter private String name;
    @Getter @Setter private BigDecimal lat;
    @Getter @Setter private BigDecimal lng;

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", latitude=" + lat +
                ", longitude=" + lng +
                '}';
    }
}
