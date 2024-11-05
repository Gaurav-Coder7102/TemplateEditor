package com.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReportCompileController {

    @Autowired
    private JasperCompiler jasperCompiler;

    @GetMapping("/compile-report")
    public String compileReport(@RequestParam String jrxmlPath, @RequestParam String outputDir) {
        try {
            String compiledPath = jasperCompiler.compileReport(jrxmlPath, outputDir);
            return "Compiled report saved to: " + compiledPath;
        } catch (Exception e) {
            e.printStackTrace();
            return "Compilation failed: " + e.getMessage();
        }
    }
}
