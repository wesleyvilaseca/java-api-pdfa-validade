package pdfaproject.pdfarestapivalidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController

@CrossOrigin(origins = "http://localhost:8080")
public class PdfaRestApiValidateApplication {

    @GetMapping("/")
    public String start(){
        return "{ Bem vindo a api de validação de PDF/A }";
    }

	public static void main(String[] args) {
		SpringApplication.run(PdfaRestApiValidateApplication.class, args);
	}
}
