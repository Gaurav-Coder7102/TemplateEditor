package com.front;

import net.sf.jasperreports.engine.JasperCompileManager;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class JasperCompiler {

	/**
	 * Compiles a .jrxml file to a .jasper file.
	 *
	 * @param jrxmlPath Path to the .jrxml file
	 * @param outputDir Directory where the compiled .jasper file should be saved
	 * @return The path of the compiled .jasper file
	 * @throws Exception If there is an error during compilation
	 */
	public String compileReport(String jrxmlPath, String outputDir) throws Exception {
		// Load the .jrxml file
		File jrxmlFile = new File(jrxmlPath);
		if (!jrxmlFile.exists()) {
			throw new IllegalArgumentException("The specified .jrxml file does not exist: " + jrxmlPath);
		}

		// Define the output path for the compiled .jasper file
		String jasperFileName = jrxmlFile.getName().replace(".jrxml", ".jasper");
		String jasperFilePath = outputDir + File.separator + jasperFileName;

		// Compile the .jrxml file directly to the specified .jasper file path
		JasperCompileManager.compileReportToFile(jrxmlFile.getAbsolutePath(), jasperFilePath);

		System.out.println("Compiled report saved to: " + jasperFilePath);
		return jasperFilePath;
	}
}
