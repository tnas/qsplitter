package io.github.tnas.qsplitter.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import io.github.tnas.qsplitter.exception.IORuntimeException;

public class Tracer {

	public void traceInFile(String operation, int data, long elapsedTime) {
		
		var fileName = operation.concat(".txt");
		var file = new File(fileName);
		var fileExists = true;
		
		try {
			if (file.exists()) {
				Files.write(Paths.get(fileName), System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
			} else {
				fileExists = file.createNewFile();
			}
			
			if (fileExists) {
				Files.write(Paths.get(fileName), String.valueOf(data).getBytes(), StandardOpenOption.APPEND);
				Files.write(Paths.get(fileName), ";".getBytes(), StandardOpenOption.APPEND);
				Files.write(Paths.get(fileName), String.valueOf(elapsedTime).getBytes(), StandardOpenOption.APPEND);
			}
		}
		catch (IOException e) {
			throw new IORuntimeException(e);
		}
	}
}
