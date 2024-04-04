package server.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
public class CurrencyConverterService {
    private static final String APP_ID = "5e0ce6c240024d09a9131b11070b3df2";
    private static final String HISTORICAL = "historical/%04d-%02d-%02d.json?app_id=%s";
    private static final String LATEST = "latest.json?app_id=%s";
    private final static String OER_URL = "https://openexchangerates.org/api/";

    private final ObjectMapper mapper = new ObjectMapper();

    private Map<String, BigDecimal> updateExchangeRates(String downloadPath) {
        JsonNode node=null;
        HttpURLConnection conn=null;
        try {
            Map<String, BigDecimal> exchangeRates = new HashMap<>();
            String urlString = String.format(OER_URL + downloadPath, APP_ID);
            URL url = URI.create(urlString).toURL();
            conn = (HttpURLConnection) url.openConnection();

            node = mapper.readTree(conn.getInputStream());
            Iterator<Map.Entry<String, JsonNode>> fieldNames = node.get("rates").fields();

            fieldNames.forEachRemaining(e -> exchangeRates.put(e.getKey(), e.getValue().decimalValue()));
            return exchangeRates;
        } catch (IOException e) {
            try {
                node= mapper.readTree(conn.getErrorStream());
                System.out.println(node.get("message").textValue());
                if (node.get("message").textValue().equals("not_available")){
                    return latest();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Map<String, BigDecimal> latest() {
        return updateExchangeRates(LATEST);
    }

    public Map<String, BigDecimal> historical(Calendar date) {
        int day = date.get(Calendar.DAY_OF_MONTH);
        int month = date.get(Calendar.MONTH) + 1;
        int year = date.get(Calendar.YEAR);

        String historical = String.format(HISTORICAL, year, month, day, APP_ID);
        return updateExchangeRates(historical);

    }

    public BigDecimal currency(String currency) {
        return latest().get(currency);
    }

    public BigDecimal historicalCurrency(String currency, Calendar date) {
        return historical(date).get(currency);
    }

    public double convert(double amount, String from, String to, Instant when) {
        Calendar time = instantToPastCalendar(when);


        String resource = Objects.requireNonNull(getClass().getClassLoader().getResource("rates/")).getPath();
        String dest = resource + stringifyCalendar(time) + "/" + from + "/";
        if (System.getProperty("os.name").startsWith("Windows")) {
            dest = dest.substring(1);
        }
        File cache = new File(dest + to + ".txt");

        try {
            if (!cache.exists()) {
                Files.createDirectories(Path.of(dest));
                try {
                    cache.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException("Created directories, but failed to create .txt file: " + e.getMessage());
                }
                BigDecimal prevFrom = historicalCurrency(from, time);
                BigDecimal prevTo = historicalCurrency(to, time);
                double rate = prevTo.doubleValue() / prevFrom.doubleValue();
                double ret = amount * rate;

                FileWriter writer = new FileWriter(cache, false);
                writer.write(Double.toString(rate));
                writer.flush();
                writer.close();
                System.out.println("FROM API");
                return ret;
            } else {
                Scanner reader = new Scanner(cache);
                if (reader.hasNext()) {
                    System.out.println("FROM CACHE");
                    return amount * Double.parseDouble(reader.next());
                } else {
                    throw new RuntimeException("Failed to read from caching file.");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to create caching file for currency rates: " + e.getMessage());
        }
    }

    public Calendar instantToPastCalendar(Instant when) {
        if (when.isAfter(Instant.now())) {
            when = Instant.now();
        }

        ZonedDateTime zdt = ZonedDateTime.ofInstant(when, ZoneId.systemDefault());
        return GregorianCalendar.from(zdt);
    }

    public String stringifyCalendar(Calendar time) {
        String day = String.valueOf(time.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(time.get(Calendar.MONTH) + 1);
        String year = String.valueOf(time.get(Calendar.YEAR));

        if (day.length() == 1) {
            day = "0" + day;
        }
        if (month.length() == 1) {
            month = "0" + month;
        }

        return day + "-" + month + "-" + year;
    }
}
