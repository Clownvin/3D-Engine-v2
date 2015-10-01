package com.engine.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.engine.util.BinaryOperations;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class FileIO {
	// TODO Document
	/**
	 * 
	 * @param path
	 */
	public static void checkFilePath(String path) {
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
	}

	// TODO Document
	/**
	 * 
	 * @return
	 */
	public static FileIO getSingleton() {
		return SINGLETON;
	}
	
	// TODO revise so len is added in writeFile, not as a convention required by
	// FMWs
	public static <T extends FileIOFormatted<T>> T readFile(T template, String fileName) {
		if (template == null)
			throw new IllegalArgumentException("Template must be a valid, instantiated object of the desired type.");
		File file = new File(template.getFileType().getPath() + fileName + template.getFileType().getExtension());
		if (file.exists() && !file.isDirectory()) {
			try {
				FileInputStream in = new FileInputStream(file);
				byte[] lenBytes = new byte[4];
				in.read(lenBytes);
				int len = BinaryOperations.bytesToInteger(lenBytes);
				byte[] bytes = new byte[len];
				bytes[0] = lenBytes[0];
				bytes[1] = lenBytes[1];
				bytes[2] = lenBytes[2];
				bytes[3] = lenBytes[3];
				in.read(bytes, 4, len - 4);
				in.close();
				return template.fromBytes(bytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	// TODO Document
	/**
	 * 
	 * @param object
	 */
	public static <T extends FileIOFormatted<T>> void writeFile(T object) {
		if (object == null)
			throw new IllegalArgumentException("Exception @ FileManager.writeFile: Object cannot be null!");
		File file = new File(
				object.getFileType().getPath() + object.getFileName() + object.getFileType().getExtension());
		checkFilePath(object.getFileType().getPath());
		try {
			FileOutputStream out = new FileOutputStream(file);
			out.write(object.toBytes());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static final FileIO SINGLETON = new FileIO();

	// TODO Document
	/**
	 * 
	 */
	private FileIO() {
		// So can't be instantiated
	}
}
