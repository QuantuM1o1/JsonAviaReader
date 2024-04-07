import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record TicketArray(List<Ticket> tickets)
{
    public record Ticket(
            String origin,
            String destination,
            @SerializedName("departure_date") LocalDate departureDate,
            @SerializedName("departure_time") LocalTime departureTime,
            @SerializedName("arrival_date") LocalDate arrivalDate,
            @SerializedName("arrival_time") LocalTime arrivalTime,
            String carrier,
            int price
    )
    {
    }
}
