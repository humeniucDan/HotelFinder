package HotelFinder.hotelFinder.BusinessLogic;

import HotelFinder.hotelFinder.Model.Area;

import java.math.BigDecimal;

public class GPS {
    private static final int EarthRadius = 6371;
    private static final double varLat = 1.0/111;
    private static final double degreePerCircumference = 360/(2 * Math.PI * EarthRadius);

    public static Area degreeVariation(Area area){
        return new Area(
                new BigDecimal(0),
                new BigDecimal(area.getRange().doubleValue() * varLat).setScale(10, BigDecimal.ROUND_UP),
                new BigDecimal(area.getRange().doubleValue() * degreePerCircumference / Math.cos(Math.toRadians(area.getLat().doubleValue())))
                        .setScale(10, BigDecimal.ROUND_UP)
        );
    }

    public static double dist(BigDecimal lat1, BigDecimal lng1, BigDecimal lat2, BigDecimal lng2){
        final double fi1 = Math.toRadians(lat1.doubleValue());
        final double fi2 = Math.toRadians(lat2.doubleValue());
        final double dFi = Math.toRadians(lat2.subtract(lat1).doubleValue());
        final double dLambda = Math.toRadians(lng2.subtract(lng1).doubleValue());

        double partialDistance = 1 - Math.cos(dFi) + Math.cos(fi1) * Math.cos(fi2) * (1 - Math.cos(dLambda));

        return 2 * EarthRadius * Math.asin(Math.sqrt(partialDistance/2));
    }
}
