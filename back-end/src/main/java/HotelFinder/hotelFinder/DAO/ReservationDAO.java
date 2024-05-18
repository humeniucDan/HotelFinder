package HotelFinder.hotelFinder.DAO;

import HotelFinder.hotelFinder.DBConnection.ConnectionFactory;
import HotelFinder.hotelFinder.Model.Reservation;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;

public class ReservationDAO extends AbstractDAO<Reservation> {

    String createInsertQuery(Reservation reservation) {
        String checkIn = reservation.getCheckIn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String checkOut = reservation.getCheckOut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder insertQuery =
                new StringBuilder("INSERT INTO [Reservation] (hotelId, roomNumber, name, checkIn, checkOut) VALUES ('")
                .append(reservation.getHotelId()).append("', '")
                .append(reservation.getRoomNumber()).append("', '")
                .append(reservation.getName()).append("', '")
                .append(checkIn).append("', '")
                .append(checkOut).append("');");


        return insertQuery.toString();
    }

    private String createCheckReservationValidityQuery(Reservation reservation) {
        String checkIn = reservation.getCheckIn().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String checkOut = reservation.getCheckOut().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder sb = new StringBuilder();
        sb
          .append("SELECT count(*) FROM [Reservation] where checkIn <'").append(checkOut).append("' ")
          .append("and checkOut > '").append(checkIn).append("' ")
          .append("and hotelId = '").append(reservation.getHotelId().toString()).append("' ")
          .append("and roomNumber = '").append(reservation.getRoomNumber().toString()).append("';");

        System.out.println(sb);
        return sb.toString();
    }

    public Boolean isReservationValid(Reservation reservation){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createCheckReservationValidityQuery(reservation);
        System.out.println(query);
        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            resultSet.next();
            return (resultSet.getString(1).equals("0"));

        } catch (Exception e) { e.printStackTrace();
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
        return false;
    }
}
