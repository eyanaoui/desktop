package utils;

import models.Post;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.util.List;

public class ExportPDF {

    public void generatePDF(String filePath, List<Post> posts) throws IOException {
        // Create a new PDF document
        try (PDDocument document = new PDDocument()) {
            // Add a page to the document
            PDPage page = new PDPage();
            document.addPage(page);

            // Initialize a content stream for writing to the page
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set font and font size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                // Write table header
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700); // Set position
                contentStream.showText("Titre    Description    Catégorie    Image"); // Text content
                contentStream.endText();

                // Write table data
                int y = 680; // Initial Y position for the first row
                for (Post post : posts) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(100, y); // Set position
                    String rowData = post.getTitre() + "    " + post.getDescription() + "    " + post.getCategorie() + "    " + post.getImage_name();
                    contentStream.showText(rowData);
                    contentStream.endText();
                    y -= 20; // Adjust Y position for the next row
                }
            }

            // Save the document to the specified file path
            document.save(filePath);
        }
    }
}
