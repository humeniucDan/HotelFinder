package HotelFinder.hotelFinder.Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
public class Area {
    @Setter @Getter private BigDecimal range;
    @Setter @Getter private BigDecimal lat;
    @Setter @Getter private BigDecimal lng;

    @Override
    public String toString() {
        return "Area{" +
                "range=" + range +
                ", lan=" + lat +
                ", lng=" + lng +
                '}';
    }
}
