package org.example;


public record PathData(String medallion, String hack_license, Long pickup_datetime, Long dropoff_datetime,
                       int trip_time_in_secs, double trip_distance_in_miles, double pickup_longitude,
                       double pickup_latitude, double dropoff_longitude, double dropoff_latitude, String payment_type,
                       double fare_amount, double surcharge, double mta_tax, double tip_amount, double tolls_amount,
                       double total_amount,long readTimestamp) {

    public static PathData createPathData(String value) {
        String[] values = value.split(",");
        String medallion = values[0];
        String hack_license = values[1];
        Long pickup_datetime = Long.parseLong(values[2]);
        Long dropoff_datetime = Long.parseLong(values[3]);
        int trip_time_in_secs = Integer.parseInt(values[4]);
        double trip_distance_in_miles = Double.parseDouble(values[5]);
        double pickup_longitude = Double.parseDouble(values[6]);
        double pickup_latitude = Double.parseDouble(values[7]);
        double dropoff_longitude = Double.parseDouble(values[8]);
        double dropoff_latitude = Double.parseDouble(values[9]);
        String payment_type = values[10];
        double fare_amount = Double.parseDouble(values[11]);
        double surcharge = Double.parseDouble(values[12]);
        double mta_tax = Double.parseDouble(values[13]);
        double tip_amount = Double.parseDouble(values[14]);
        double tolls_amount = Double.parseDouble(values[15]);
        double total_amount = Double.parseDouble(values[16]);
        long readTimestamp = Long.parseLong(values[17]);
        return new PathData(medallion, hack_license, pickup_datetime, dropoff_datetime,
                trip_time_in_secs, trip_distance_in_miles, pickup_longitude,
                pickup_latitude, dropoff_longitude, dropoff_latitude, payment_type,
                fare_amount, surcharge, mta_tax, tip_amount, tolls_amount,
                total_amount,readTimestamp);

    }
}
