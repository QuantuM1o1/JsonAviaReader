import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gsonAdapters.LocalDateAdapter;
import gsonAdapters.LocalTimeAdapter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Main
{
    public static void main(String[] args)
    {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                .create();
        File file = new File("src/main/resources/tickets.json").getAbsoluteFile();
        Map<String, Duration> minTime = new HashMap<>();
        List<Integer> prices = new ArrayList<>();
        try (Reader reader = new FileReader(file))
        {
            TicketArray ticketArray = gson.fromJson(reader, TicketArray.class);
            ticketArray.tickets().stream()
                    .filter(ticket -> ticket.origin().equals("VVO"))
                    .filter(ticket -> ticket.destination().equals("TLV"))
                    .forEach(ticket ->
                    {
                        LocalDateTime departure = LocalDateTime.of(ticket.departureDate(), ticket.departureTime());
                        LocalDateTime arrival = LocalDateTime.of(ticket.arrivalDate(), ticket.arrivalTime());
                        Duration duration = Duration.between(departure, arrival);
                        if (minTime.putIfAbsent(ticket.carrier(), duration) != null)
                        {
                            if (duration.toMinutes() < minTime.get(ticket.carrier()).toMinutes())
                            {
                                minTime.put(ticket.carrier(), duration);
                            }
                        }

                        prices.add(ticket.price());
                    });

            System.out.println("Минимальное время полёта для каждого перевозчика:");
            for (String key : minTime.keySet())
            {
                System.out.println(key + " - " + minTime.get(key).toMinutes() + " минут");
            }

            int averagePrice = 0;
            for (int price : prices)
            {
                averagePrice += price;
            }
            averagePrice = averagePrice / prices.size();
            System.out.println("Средняя цена: " + averagePrice);

            prices.sort(Comparator.naturalOrder());
            int median = prices.get(prices.size() / 2);
            System.out.println("Медианная цена: " + median);

            System.out.println("Разница между средней и медианой: " + Math.abs(averagePrice - median));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
        }
    }
}