package HotelFinder.hotelFinder.DAO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
public class ObjectFilter {
    @Setter private FieldFilter[] fieldFilters;

    @Override
    public String toString() {
        return "ObjectFilter{" +
                "fieldFilters=" + Arrays.toString(fieldFilters) +
                '}';
    }

    public String generateConditionQuery(){
        StringBuilder query = new StringBuilder();

        for(var fieldFilter: fieldFilters){
            query
                    .append(" and ")
                    .append(fieldFilter.sqlCondition());
        }

        return query.toString();
    }
}