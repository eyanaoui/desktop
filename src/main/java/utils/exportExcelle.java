package utils;

import models.Post;
import org.apache.poi.ss.usermodel.*;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class exportExcelle {

    public static void exportPostsToExcel(List<Post> posts, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Posts");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] columns = { "Titre", "Description", "Image", "Categorie", "Updated_at"};
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Fill data rows
        int rowNum = 1;
        for (Post post : posts) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(post.getTitre());
            row.createCell(1).setCellValue(post.getDescription());
            row.createCell(2).setCellValue(post.getImage_name());
            row.createCell(3).setCellValue(post.getCategorie());
            // Assuming 'updated_at' is a Timestamp or Date object; modify as needed
            Cell dateCell = row.createCell(4);
            dateCell.setCellValue(post.getUpdated_at().toString()); // Convert to String representation
        }

        // Write to file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        }

        // Close workbook
        workbook.close();
    }
}
