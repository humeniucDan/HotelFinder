package HotelFinder.hotelFinder;

import HotelFinder.hotelFinder.DAO.HotelDAO;
import HotelFinder.hotelFinder.Model.Hotel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class HotelFinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelFinderApplication.class, args);

		List<Hotel> hotels = new HotelDAO().findAll();
		System.out.println(hotels);
	}

	@Component
	public static class ServerPortCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
		@Override
		public void customize(ConfigurableWebServerFactory factory) {
			factory.setPort(8000);
		}
	}

}
