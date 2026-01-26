package com.sufaltalukder.Utils;

import com.sufaltalukder.DTOs.RequestSubCategoryDTO;
import com.sufaltalukder.DTOs.SubCategoryDTO;
import com.sufaltalukder.Models.SubCategoryModel;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelUtils {
	// Method to write SubCategories to an Excel file
	public void writeSubCategoriesToExcel(List<SubCategoryDTO> subCategories, OutputStream outputStream)
			throws IOException {
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("SubCategories");
		// Create header row
		Row headerRow = sheet.createRow(0);
		String[] headers = { "Category Id", "Sub Category", "Status" };
		for (int i = 0; i < headers.length; i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers[i]);
		}
		// Populate data
		for (int i = 0; i < subCategories.size(); i++) {
			SubCategoryDTO subCategory = subCategories.get(i);
			Row dataRow = sheet.createRow(i + 1);
			dataRow.createCell(0).setCellValue(subCategory.getSubCategoryId());
			dataRow.createCell(1).setCellValue(subCategory.getSubCategoryName());
			dataRow.createCell(2).setCellValue(subCategory.getSubCategoryActive().name());
		}
		workbook.write(outputStream);
		workbook.close();
	}

	// Method to read SubCategories from an Excel file
	public List<RequestSubCategoryDTO> readSubCategoriesFromExcel(InputStream inputStream) throws IOException {
		List<RequestSubCategoryDTO> subCategories = new ArrayList<>();
		try (Workbook workbook = new XSSFWorkbook(inputStream)) {
			Sheet sheet = workbook.getSheetAt(0);
			for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Skip header row
				Row row = sheet.getRow(i);
				if (row != null) {
					RequestSubCategoryDTO subCategory = new RequestSubCategoryDTO();
					subCategory.setSubCategoryName(row.getCell(1).getStringCellValue());
					subCategory.setSubCategoryActive(
							SubCategoryModel.SubCategoryActive.valueOf(row.getCell(2).getStringCellValue()));
					subCategories.add(subCategory);
				}
			}
		}
		return subCategories;
	}
}