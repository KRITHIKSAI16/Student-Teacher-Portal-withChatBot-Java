package org.example;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PDFTextExtractor {
    public static String extractTextFromPDF(String pdfFilePath) {
        String extractedText = "";

        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            if (!document.isEncrypted()) {
                PDFTextStripper pdfStripper = new PDFTextStripper();
                extractedText = pdfStripper.getText(document);
            } else {
                System.out.println("The document is encrypted and cannot be read.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return extractedText;
    }

}
