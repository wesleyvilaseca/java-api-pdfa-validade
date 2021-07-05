package pdfaproject.pdfarestapivalidate.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class PdftextController {
    @PostMapping(path = "/api/pdftextextract/")
    public @ResponseBody ResponseEntity<String> extractTextFromPDFFile(@RequestParam("file") MultipartFile file) {
        try {
            // load file
            PDDocument document = PDDocument.load(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            String strippedText = stripper.getText(document);

            if (strippedText.trim().isEmpty()) {
                strippedText = extractTextFromScannnerDocument(document);
            }

            JSONObject obj = new JSONObject();
            obj.put("fileName", file.getOriginalFilename());
            obj.put("text", strippedText.toString());

            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/api/pdftextextract/ping")
    public ResponseEntity<String> get() {
        return new ResponseEntity<String>("PONG", HttpStatus.OK);
    }

    private String extractTextFromScannnerDocument(PDDocument document) throws IOException, TesseractException {
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        _tesseract.setDatapath("/usr/share/tessdata/");
        _tesseract.setLanguage("pt-br");

        for(int page = 0; page < document.getNumberOfPages(); page++){
            BufferedImage bin = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            File temp = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bin, "png", temp);

            String result = _tesseract.doOCR(temp);
            out.append(result);
            temp.delete();
        }

        return out.toString();
    }
}
