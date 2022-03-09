
import models.Ticket;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class App {

    public static void readJson(String path) throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        BufferedReader bufferedReader = java.nio.file.Files.newBufferedReader(Paths.get(path));
        JSONObject list = (JSONObject) parser.parse(bufferedReader);
        JSONArray msg = (JSONArray) list.get("tickets");
        List<Long> diffs = new ArrayList<>();
        for (JSONObject object : (Iterable<JSONObject>) msg) {
            Ticket ticket = new Ticket(object);
            if (ticket.getOrigin_name().equals("Владивосток") && ticket.getDestination_name().equals("Тель-Авив")) {
                diffs.add(TimeUnit.MILLISECONDS.toMinutes(
                        ticket.getArrival_date().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() -
                                ticket.getDeparture_date().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                );
            }
        }
        double avgtime = diffs.stream().mapToDouble(a -> a).average().getAsDouble();
        System.out.printf("Среднее время в пути %.2f минут%n", avgtime);
        diffs.sort(Long::compareTo);
        long n = Math.round(0.9*diffs.size());
        long p1 = diffs.get((int) (n -1));
        int x = (int) (0.9 * (diffs.size() + 1));
        double x1 = 0.9 * (diffs.size() + 1) - x;
        double p2 = diffs.get(x -1) + x1 * (diffs.get(x) - diffs.get(x-1));
        System.out.printf("90-перцентиль по методу ближайшего ранга: %d минут\n", p1);
        System.out.printf("90-перцентиль по методу линейной интерполяции между ближайшими рангами (3 вариант расчетов): %.2f минут\n", p2);

    }

    public static void main(String[] args){
        try {
            readJson("src/main/java/tickets.json");
        } catch (ParseException e) {
            System.out.println("Не можем прочитать JSON, ошибка: ");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Не нашли файл");
        }
    }
}
