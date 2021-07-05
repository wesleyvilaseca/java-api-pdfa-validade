package pdfaproject.pdfarestapivalidate.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class StatusController {

    @GetMapping(path = "/api/status")
    public String check(){
        return "online";
    }
}
