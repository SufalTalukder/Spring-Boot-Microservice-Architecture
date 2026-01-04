package com.sufaltalukder.Utils;

import com.sufaltalukder.Models.CheckOutHistoryModel;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.*;
import java.util.List;

public class PdfGenerator {
	public static byte[] generatePdf(List<CheckOutHistoryModel> purchases) throws DocumentException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, byteArrayOutputStream);
		document.open();
		// Create HTML structure
		StringBuilder html = new StringBuilder();
		html.append("<h2>Customer Purchase History</h2>");
		html.append("<table border='1' style='width:100%; border-collapse:collapse; padding-top:5px;'>");
		html.append("<tr>" + "<th>Purchase ID</th>" + "<th>Customer Info</th>" + "<th>Product Info</th>"
				+ "<th>Purchase Amount</th>" + "<th>Payment Method</th>" + "<th>Status</th>" + "<th>Created At</th>"
				+ "<th>Updated At</th>" + "</tr>");
		for (CheckOutHistoryModel purchase : purchases) {
			html.append("<tr>");
			html.append("<td>").append(purchase.getCheckOutHistoryId()).append("</td>");
			html.append("<td>").append("<span>ID: ").append(purchase.getUserInfo().getUserId()).append("</span></br>")
					.append("<span>Name: ").append(purchase.getUserInfo().getFullName()).append("</span></br>")
					.append("<span>Email address: ").append(purchase.getUserInfo().getEmailAddress())
					.append("</span></br>").append("<span>Phone number: ")
					.append(purchase.getUserInfo().getPhoneNumber()).append("</span>").append("</td>");
			html.append("<td>").append("<span>ID(s): ").append(purchase.getAddToCartIds()).append("</span></br>")
					.append("<span>Price: ").append(purchase.getPaymentAmount()).append("</span>").append("</td>");
			html.append("<td>").append(purchase.getPaymentAmount()).append("</td>");
			html.append("<td>").append(purchase.getPaymentMethod()).append("</td>");
			html.append("<td>").append(purchase.getPaymentStatus()).append("</td>");
			html.append("<td>").append(purchase.getCheckOutHistoryCreatedAt()).append("</td>");
			html.append("</tr>");
		}
		html.append("</table>");
		// Parse HTML and add it to the PDF
		com.lowagie.text.html.simpleparser.HTMLWorker htmlWorker = new com.lowagie.text.html.simpleparser.HTMLWorker(
				document);
		try {
			htmlWorker.parse(new StringReader(html.toString()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		document.close();
		return byteArrayOutputStream.toByteArray();
	}
}