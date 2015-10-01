package com.engine.io;

import com.engine.util.ByteFormatted;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public interface FileIOFormatted<T> extends ByteFormatted<T> {
	public String getFileName();

	public FileType getFileType();
}
