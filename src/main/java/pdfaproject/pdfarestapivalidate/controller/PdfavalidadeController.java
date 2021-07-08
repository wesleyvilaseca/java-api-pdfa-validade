package pdfaproject.pdfarestapivalidate.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.json.JSONObject;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pdfaproject.pdfarestapivalidate.support.Filepersist;



@RestController
public class PdfavalidadeController {
    private String filename;
    private ApplicationHome home = new ApplicationHome(Filepersist.class);

    @PostMapping(path = "/api/pdfavalidate/")
    public @ResponseBody ResponseEntity<String> get(@RequestParam("file") MultipartFile file) {
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        this.setFilename(home.getDir() + "\\" + dateFormat.format(d) + file.getOriginalFilename());
        try {
            File pdf = new File(this.getFilename());
            file.transferTo(pdf);
            JSONObject obj = new JSONObject();
            obj.put("valid", isValidPDF(this.getFilename()));
            this.fileDelete(pdf);
            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static Optional<ValidationResult> getValidationResult(String fileName) {
        if (Objects.isNull(fileName)) {
            throw new NullPointerException("fileName shouldn't be null");
        }

        try {
            PreflightParser parser = new PreflightParser(fileName);
            parser.parse();
            try (PreflightDocument document = parser.getPreflightDocument()) {
                document.validate();
                ValidationResult result = document.getResult();
                return Optional.of(result);
            }

        } catch (IOException e) {
            return Optional.empty();
        }

    }

    public static boolean isValidPDF(String fileName) {
        Optional<ValidationResult> validationResult = getValidationResult(fileName);

        if (!validationResult.isPresent()) {
            return false;
        }

        ValidationResult result = validationResult.get();
        if (result.isValid()) {
            return true;
        }

        return false;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilename() {
        return this.filename;
    }

    public void fileDelete(File file){
        file.delete();
    }

}