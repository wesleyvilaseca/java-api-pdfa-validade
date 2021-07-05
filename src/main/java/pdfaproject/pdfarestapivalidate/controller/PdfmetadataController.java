package pdfaproject.pdfarestapivalidate.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.PDMetadata;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class PdfmetadataController {

    @PostMapping(path = "/api/pdfmetadata/")
    public @ResponseBody ResponseEntity<String> extractMetadataFromPDFFile(@RequestParam("file") MultipartFile file) {
        try {
            // load file
            PDDocument document = PDDocument.load(file.getBytes());
            PDDocumentCatalog catalog = document.getDocumentCatalog();
            PDMetadata meta = catalog.getMetadata();

            JSONObject obj = new JSONObject();
            obj.put("fileName", file.getOriginalFilename());
            obj.put("metadata", getMetadata(meta));

            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static Optional<List<String>> getDataFromStream(InputStream in) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            List<String> data = new ArrayList<>();
            String str;

            while ((str = br.readLine()) != null) {
                data.add(str);
            }
            return Optional.of(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<List<String>> getMetadata(PDMetadata metadata) {
        if (metadata == null) {
            System.out.println("There is no meta data associated");
            return Optional.empty();
        }

        try (InputStream in = metadata.createInputStream()) {
            return getDataFromStream(in);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

}
