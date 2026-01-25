package com.sufaltalukder.Utils;

import com.sufaltalukder.DTOs.CategoryDTO;
import com.sufaltalukder.DTOs.RequestCategoryDTO;
import com.sufaltalukder.Models.CategoryModel;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import org.springframework.stereotype.Component;
import java.io.*;
import java.util.*;

@Component
public class CsvCategoryUtils {
	public void writeCategoriesToCsv(List<CategoryDTO> categories, OutputStream outputStream) throws IOException {
		try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(outputStream))) {
			String[] header = { "Category", "Status" };
			writer.writeNext(header);
			for (CategoryDTO category : categories) {
				String[] data = { category.getCategoryName(), category.getCategoryActive().name() };
				writer.writeNext(data);
			}
		}
	}

	public List<RequestCategoryDTO> readCategoriesFromCsv(InputStream inputStream) throws IOException {
		List<RequestCategoryDTO> categories = new ArrayList<>();
		try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
			String[] headers = reader.readNext(); // Read the header line
			// Check if headers are present and valid
			if (headers == null || headers.length < 2 || !headers[0].equals("Category")
					|| !headers[1].equals("Status")) {
				throw new IllegalArgumentException("Header not present or invalid");
			}
			// Read data lines
			String[] nextLine;
			while ((nextLine = reader.readNext()) != null) {
				RequestCategoryDTO category = new RequestCategoryDTO();
				category.setCategoryName(nextLine[0]);
				category.setCategoryActive(CategoryModel.CategoryActive.valueOf(nextLine[1]));
				categories.add(category);
			}
		} catch (CsvValidationException e) {
			e.printStackTrace();
			throw new IOException("Error reading CSV file", e);
		} catch (IllegalArgumentException e) {
			throw new IOException(e.getMessage(), e); // Rethrow as IOException
		}
		return categories;
	}
}