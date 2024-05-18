package HotelFinder.hotelFinder.DAO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;

@NoArgsConstructor
@AllArgsConstructor
public class FieldFilter {
    @Setter private String fieldName; // may or may not need to be of type "Class<t>"
    @Setter private Boolean isSetFilter;
    @Setter private Object[] values; // may or may not need to be renamed

    @Override
    public String toString() {
        return "FieldFilter{" +
                "fieldName='" + fieldName + '\'' +
                ", isSetFilter=" + isSetFilter +
                ", values=" + Arrays.toString(values) +
                '}';
    }

    public String sqlCondition(){
        StringBuilder sqlQuery = new StringBuilder("(");

        if(!isSetFilter){
            if(values[0] != null) {
                sqlQuery
                        .append("'")
                        .append(values[0])
                        .append("' <= ")
                        .append(fieldName)
                        .append(" and ");
            }
            if(values.length > 1 && values[1] != null) {
                sqlQuery
                        .append(fieldName)
                        .append(" <= '")
                        .append(values[1])
                        .append("'")
                        .append(" and ");
            }
        }
        else {
            for (Object value : values) {
                sqlQuery
                        .append("'")
                        .append(value)
                        .append("' = ")
                        .append(fieldName)
                        .append(" or ");
            }
        }

        sqlQuery.replace(sqlQuery.length() - 4, sqlQuery.length(), ") ");
        return sqlQuery.toString();
    }
}
