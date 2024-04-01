package server.services;

import java.time.Instant;

public class Test {
    public static void main(String[] args) {
        CurrencyConverterService ccs = new CurrencyConverterService();

        double conversion = ccs.convert(100, "RON", "EUR", Instant.now());
        System.out.println(conversion);
    }
}
