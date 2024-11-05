package com.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/generate-report")
    public String generateReport(
            @RequestParam("fields") List<String> fields,
            @RequestParam Map<String, String> requestParams) {

        String filePath = "src/main/resources/static/rptrfrepo_detail_shop.jrxml";

        // Extract widths and types from request parameters
        try {
            // Pass fields, widths, and types to the service
            reportService.updateColumnHeader(filePath, fields, requestParams);
            JrxmlFormattter.format();
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "redirect:/compile-report?jrxmlPath=" + filePath + "&outputDir=src/main/resources/static";
    }
}
