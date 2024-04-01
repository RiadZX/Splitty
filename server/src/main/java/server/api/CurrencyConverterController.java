package server.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.services.CurrencyConverterService;

import java.time.Instant;

@Controller
@RequestMapping("/api/convert")
public class CurrencyConverterController {
    private final CurrencyConverterService converter;

    @Autowired
    public CurrencyConverterController(CurrencyConverterService converter) {
        this.converter = converter;
    }

    @GetMapping(path = {"", "/"})
    public ResponseEntity<Double> convert(@RequestBody String contents) {
        JsonObject body = JsonParser.parseString(contents).getAsJsonObject();
        double ret = converter.convert(Double.parseDouble(body.get("amount").toString()),
                body.get("from").toString(),
                body.get("to").toString(),
                Instant.parse(body.get("when").toString()));
        return ResponseEntity.ok(ret);
    }
}
