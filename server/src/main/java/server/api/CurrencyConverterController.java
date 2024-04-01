package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import server.services.CurrencyConverterService;

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
        return ResponseEntity.internalServerError().build();
    }
}
