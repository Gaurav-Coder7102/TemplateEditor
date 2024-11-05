package com.front;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class ReportService {

	// Method to dynamically add column headers and detail bands based on selected
	// fields, types, and widths
	public void updateColumnHeader(String filePath, List<String> fields, Map<String, String> requestParams)
			throws Exception {
		// Parse the DOM
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new File(filePath));

		// Remove existing columnHeader and detail sections
		NodeList columnHeaders = doc.getElementsByTagName("columnHeader");
		if (columnHeaders.getLength() > 0) {
			Node columnHeader = columnHeaders.item(0);
			columnHeader.getParentNode().removeChild(columnHeader);
		}

		NodeList details = doc.getElementsByTagName("detail");
		if (details.getLength() > 0) {
			Node detail = details.item(0);
			detail.getParentNode().removeChild(detail);
		}

		// Add new columnHeader section
		Element newColumnHeader = doc.createElement("columnHeader");
		Element band = doc.createElement("band");
		band.setAttribute("height", "15");

		int xPosition = 0;
		for (String field : fields) {
			// Extract width and type for each field
			String widthParam = requestParams.get("width_" + field);
			String typeParam = requestParams.get("type_" + field);
			int width = (widthParam != null) ? Integer.parseInt(widthParam) : 50;
			String type = (typeParam != null) ? typeParam : "String";

			// Create textField for columnHeader with dynamic width and type
			Element textField = createTextFieldElement(doc, field, xPosition, width);
			band.appendChild(textField);
			xPosition += width;
		}

		newColumnHeader.appendChild(band);
		NodeList pageHeaders = doc.getElementsByTagName("pageHeader");
		if (pageHeaders.getLength() > 0) {
			Node pageHeader = pageHeaders.item(0);
			Node nextSibling = pageHeader.getNextSibling();
			if (nextSibling != null) {
				doc.getDocumentElement().insertBefore(newColumnHeader, nextSibling);
			} else {
				doc.getDocumentElement().appendChild(newColumnHeader);
			}
		}

		// Add new detail section
		Element newDetail = doc.createElement("detail");
		Element bandDetail = doc.createElement("band");
		bandDetail.setAttribute("height", "15");

		xPosition = 0;
		for (String field : fields) {
			String widthParam = requestParams.get("width_" + field);
			String typeParam = requestParams.get("type_" + field);
			int width = (widthParam != null) ? Integer.parseInt(widthParam) : 50;
			String type = (typeParam != null) ? typeParam : "String";

			// Create textField for detail with dynamic width and type
			Element textFieldDetail = createTextFieldElement(doc, field, xPosition, width, type, "detail");
			bandDetail.appendChild(textFieldDetail);
			xPosition += width;
		}

		newDetail.appendChild(bandDetail);
		Node nextSibling = newColumnHeader.getNextSibling();
		if (nextSibling != null) {
			doc.getDocumentElement().insertBefore(newDetail, nextSibling);
		} else {
			doc.getDocumentElement().appendChild(newDetail);
		}

		// Save the modified document
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "no");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4"); // Set indentation amount
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(filePath));
		transformer.transform(source, result);
	}

	private Element createTextFieldElement(Document doc, String fieldName, int xPosition, int width, String type,
			String sectionType) {
		// Create the main textField element
		Element textField = doc.createElement("textField");
		textField.setAttribute("isStretchWithOverflow", "true");
		textField.setAttribute("isBlankWhenNull", "true");

		// If the field type is Date, set the pattern
		if ("Date".equals(type)) {
			textField.setAttribute("pattern", "dd/MM/yyyy");
		}

		// Create the reportElement with specific styling and position attributes
		Element reportElement = doc.createElement("reportElement");
		reportElement.setAttribute("x", String.valueOf(xPosition));
		reportElement.setAttribute("y", "0");
		reportElement.setAttribute("width", String.valueOf(width));
		reportElement.setAttribute("height", "15");
		reportElement.setAttribute("mode", "Opaque");
		reportElement.setAttribute("uuid", UUID.randomUUID().toString());
		textField.appendChild(reportElement);

		// Add <box> with border settings
		Element box = doc.createElement("box");
		String[] penPositions = { "topPen", "leftPen", "bottomPen", "rightPen" };
		for (String pos : penPositions) {
			Element pen = doc.createElement(pos);
			pen.setAttribute("lineWidth", "1.0");
			pen.setAttribute("lineStyle", "Solid");
			pen.setAttribute("lineColor", "#000000");
			box.appendChild(pen);
		}
		textField.appendChild(box);

		// Add <textElement> for text alignment based on section type
		Element textElement = doc.createElement("textElement");
		textElement.setAttribute("textAlignment", "header".equals(sectionType) ? "Center" : "Left");
		textElement.setAttribute("verticalAlignment", "Middle");
		textField.appendChild(textElement);

		// Add <textFieldExpression> with the field name as its expression
		Element textFieldExpression = doc.createElement("textFieldExpression");
		textFieldExpression.appendChild(doc.createCDATASection("$F{" + fieldName + "}"));
		textField.appendChild(textFieldExpression);

		return textField;
	}
	private Element createTextFieldElement(Document doc, String fieldName, int xPosition, int width) {
        // Create the main textField element with attributes
        Element textField = doc.createElement("textField");
        textField.setAttribute("isStretchWithOverflow", "true");
        textField.setAttribute("isBlankWhenNull", "true");

        // Create the reportElement with specific styling and position attributes
        Element reportElement = doc.createElement("reportElement");
        reportElement.setAttribute("style", "ReportFieldLabel_9");
        reportElement.setAttribute("stretchType", "ContainerHeight");
        reportElement.setAttribute("mode", "Opaque");
        reportElement.setAttribute("x", String.valueOf(xPosition));
        reportElement.setAttribute("y", "0");
        reportElement.setAttribute("width", String.valueOf(width));
        reportElement.setAttribute("height", "15");
        reportElement.setAttribute("isRemoveLineWhenBlank", "true");
        reportElement.setAttribute("isPrintWhenDetailOverflows", "true");
        reportElement.setAttribute("backcolor", "#80D5FF");
        reportElement.setAttribute("uuid", UUID.randomUUID().toString());
         System.out.println(String.valueOf(width));
        // Append reportElement to textField
        textField.appendChild(reportElement);

        // Create and append <box> with <pen> elements
        Element box = doc.createElement("box");
        textField.appendChild(box);

        String[] penPositions = {"topPen", "leftPen", "bottomPen", "rightPen"};
        for (String pos : penPositions) {
            Element pen = doc.createElement(pos);
            pen.setAttribute("lineWidth", "1.0");
            pen.setAttribute("lineStyle", "Solid");
            pen.setAttribute("lineColor", "#000000");
            box.appendChild(pen);
        }

        // Add <textElement> for text alignment
        Element textElement = doc.createElement("textElement");
        textElement.setAttribute("textAlignment", "Center");
        textElement.setAttribute("verticalAlignment", "Middle");
        textField.appendChild(textElement);

        // Add <textFieldExpression> with the field name as its expression
        Element textFieldExpression = doc.createElement("textFieldExpression");
        textFieldExpression.appendChild(doc.createCDATASection("\"" + fieldName + "\""));
        textField.appendChild(textFieldExpression);

        return textField;
    }
}
