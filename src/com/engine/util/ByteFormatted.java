package com.engine.util;

public interface ByteFormatted<T> {
	public int sizeOf();

	public byte[] toBytes();

	public T fromBytes(byte[] bytes);
}
