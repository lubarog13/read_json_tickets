package models;

import org.json.simple.JSONObject;

import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDateTime;

public class Ticket {
    private String origin;
    private String origin_name;
    private String destination;
    private String destination_name;
    private LocalDateTime departure_date;
    private LocalDateTime arrival_date;
    private String carrier;
    private long stops;
    private long price;


    public Ticket(JSONObject object) {
        this.origin = (String) object.get("origin");
        this.origin_name = (String) object.get("origin_name");
        this.destination = (String) object.get("destination");
        this.destination_name = (String) object.get("destination_name");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yy H:mm", Locale.getDefault());
        this.departure_date = LocalDateTime.parse(object.get("departure_date") + " " + object.get("departure_time"), formatter);
        this.arrival_date = LocalDateTime.parse(object.get("arrival_date") + " " + object.get("arrival_time"), formatter);
        this.carrier = (String) object.get("carrier");
        this.stops = (long) object.get("stops");
        this.price = (long) object.get("price");

    }


    public String getOrigin_name() {
        return origin_name;
    }

    public String getDestination_name() {
        return destination_name;
    }


    public LocalDateTime getDeparture_date() {
        return departure_date;
    }


    public LocalDateTime getArrival_date() {
        return arrival_date;
    }

}
