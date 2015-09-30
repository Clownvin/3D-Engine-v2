package com.engine.util;

import java.math.BigInteger;

/**
 * 
 * @author Calvin Gene Hall
 *
 */

public final class BinaryOperations {
	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static BigInteger bigIntegerFromBytes(byte[] bytes) {
		BigInteger bigInt = new BigInteger(bytes);
		return bigInt;
	}

	// TODO Document
	/**
	 * 
	 * @param byteArray
	 * @return
	 */
	public static char[] byteArrayToCharacterArray(byte[] byteArray) {
		char[] charArray = new char[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) {
			charArray[i] = (char) (unsignedByteToShort(byteArray[i]));
		}
		return charArray;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static double[] byteArrayToDoubleArray(byte[] bytes) {
		if (bytes.length % 8 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 8 is 0 when converting to doubles.");
		double[] doubles = new double[bytes.length / 8];
		for (int i = 0; i < bytes.length; i += 8) {
			doubles[i / 8] = Double.longBitsToDouble(
					((long) unsignedByteToShort(bytes[i]) << 56) | ((long) unsignedByteToShort(bytes[i + 1]) << 48)
							| ((long) unsignedByteToShort(bytes[i + 2]) << 40)
							| ((long) unsignedByteToShort(bytes[i + 3]) << 32)
							| (unsignedByteToShort(bytes[i + 4]) << 24) | (unsignedByteToShort(bytes[i + 5]) << 16)
							| (unsignedByteToShort(bytes[i + 6]) << 8) | unsignedByteToShort(bytes[i + 7]));
		}
		return doubles;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static double[] byteArrayToDoubleArray(byte[] bytes, int startIdx) {
		if ((bytes.length - startIdx) % 8 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 8 is 0 when converting to doubles.");
		double[] doubles = new double[(bytes.length - startIdx) / 8];
		for (int i = startIdx; i < bytes.length; i += 8) {
			doubles[(i - startIdx) / 8] = Double.longBitsToDouble(
					((long) unsignedByteToShort(bytes[i]) << 56) | ((long) unsignedByteToShort(bytes[i + 1]) << 48)
							| ((long) unsignedByteToShort(bytes[i + 2]) << 40)
							| ((long) unsignedByteToShort(bytes[i + 3]) << 32)
							| (unsignedByteToShort(bytes[i + 4]) << 24) | (unsignedByteToShort(bytes[i + 5]) << 16)
							| (unsignedByteToShort(bytes[i + 6]) << 8) | unsignedByteToShort(bytes[i + 7]));
		}
		return doubles;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static float[] byteArrayToFloatArray(byte[] bytes) {
		if (bytes.length % 4 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 4 is 0 when converting to floats.");
		float[] floats = new float[bytes.length / 4];
		for (int i = 0; i < bytes.length; i += 4) {
			floats[i / 4] = Float
					.intBitsToFloat((unsignedByteToShort(bytes[i]) << 24) | (unsignedByteToShort(bytes[i + 1]) << 16)
							| (unsignedByteToShort(bytes[i + 2]) << 8) | unsignedByteToShort(bytes[i + 3]));
		}
		return floats;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static float[] byteArrayToFloatArray(byte[] bytes, int startIdx) {
		if ((bytes.length - startIdx) % 4 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 4 is 0 when converting to floats.");
		float[] floats = new float[(bytes.length - startIdx) / 4];
		for (int i = startIdx; i < bytes.length; i++) {
			floats[(i - startIdx) / 4] = Float
					.intBitsToFloat((unsignedByteToShort(bytes[i++]) << 24) | (unsignedByteToShort(bytes[i++]) << 16)
							| (unsignedByteToShort(bytes[i++]) << 8) | unsignedByteToShort(bytes[i]));
		}
		return floats;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int[] byteArrayToIntegerArray(byte[] bytes) {
		if (bytes.length % 4 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 4 is 0 when converting to integers.");
		int[] ints = new int[bytes.length / 4];
		for (int i = 0; i < bytes.length; i++) {
			ints[i / 4] = (unsignedByteToShort(bytes[i++]) << 24) | (unsignedByteToShort(bytes[i++]) << 16)
					| (unsignedByteToShort(bytes[i++]) << 8) | unsignedByteToShort(bytes[i]);
		}
		return ints;
	}

	public static int[] byteArrayToIntegerArray(byte[] bytes, int startIdx) {
		if ((bytes.length - startIdx) % 4 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 4 is 0 when converting to integers.");
		int[] ints = new int[(bytes.length - startIdx) / 4];
		for (int i = startIdx; i < bytes.length; i += 4) {
			ints[(i - startIdx) / 4] = (unsignedByteToShort(bytes[i]) << 24) | (unsignedByteToShort(bytes[i + 1]) << 16)
					| (unsignedByteToShort(bytes[i + 2]) << 8) | unsignedByteToShort(bytes[i + 3]);
		}
		return ints;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static long[] byteArrayToLongArray(byte[] bytes) {
		if (bytes.length % 8 != 0)
			throw new NumberFormatException(
					"Byte array length must be such that length modulo 8 is 0 when converting to longs.");
		long[] longs = new long[bytes.length / 8];
		for (int i = 0; i < bytes.length; i += 8) {
			longs[i / 8] = ((long) unsignedByteToShort(bytes[i]) << 56)
					| ((long) unsignedByteToShort(bytes[i + 1]) << 48)
					| ((long) unsignedByteToShort(bytes[i + 2]) << 40)
					| ((long) unsignedByteToShort(bytes[i + 3]) << 32) | (unsignedByteToShort(bytes[i + 4]) << 24)
					| (unsignedByteToShort(bytes[i + 5]) << 16) | (unsignedByteToShort(bytes[i + 6]) << 8)
					| unsignedByteToShort(bytes[i + 7]);
		}
		return longs;
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static byte bytesToByte(byte[] bytes) {
		if (bytes.length < 1)
			throw new NumberFormatException("Byte array length must be at least 1 when converting to byte.");
		return bytes[0];
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static byte bytesToByte(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 1)
			throw new NumberFormatException("Byte array length must be at least 1 when converting to byte.");
		return bytes[startIdx];
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static double bytesToDouble(byte[] bytes) {
		if (bytes.length < 8)
			throw new NumberFormatException("Byte array length must be at least 8 when converting to double.");
		return Double.longBitsToDouble(
				((long) unsignedByteToShort(bytes[0]) << 56) | ((long) unsignedByteToShort(bytes[1]) << 48)
						| ((long) unsignedByteToShort(bytes[2]) << 40) | ((long) unsignedByteToShort(bytes[3]) << 32)
						| (unsignedByteToShort(bytes[4]) << 24) | (unsignedByteToShort(bytes[5]) << 16)
						| (unsignedByteToShort(bytes[6]) << 8) | unsignedByteToShort(bytes[7]));
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static double bytesToDouble(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 8)
			throw new NumberFormatException("Byte array length must be at least 8 when converting to double.");
		return Double.longBitsToDouble(((long) unsignedByteToShort(bytes[startIdx]) << 56)
				| ((long) unsignedByteToShort(bytes[startIdx + 1]) << 48)
				| ((long) unsignedByteToShort(bytes[startIdx + 2]) << 40)
				| ((long) unsignedByteToShort(bytes[startIdx + 3]) << 32)
				| (unsignedByteToShort(bytes[startIdx + 4]) << 24) | (unsignedByteToShort(bytes[startIdx + 5]) << 16)
				| (unsignedByteToShort(bytes[startIdx + 6]) << 8) | unsignedByteToShort(bytes[7]));
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static float bytesToFloat(byte[] bytes) {
		if (bytes.length < 4)
			throw new NumberFormatException("Byte array length must be at least 4 when converting to float.");
		return Float.intBitsToFloat((unsignedByteToShort(bytes[0]) << 24) | (unsignedByteToShort(bytes[1]) << 16)
				| (unsignedByteToShort(bytes[2]) << 8) | unsignedByteToShort(bytes[3]));
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static float bytesToFloat(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 4)
			throw new NumberFormatException("Byte array length must be at least 4 when converting to float.");
		return Float.intBitsToFloat(
				(unsignedByteToShort(bytes[startIdx]) << 24) | (unsignedByteToShort(bytes[startIdx + 1]) << 16)
						| (unsignedByteToShort(bytes[startIdx + 2]) << 8) | unsignedByteToShort(bytes[startIdx + 3]));
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static int bytesToInteger(byte[] bytes) {
		if (bytes.length < 4)
			throw new NumberFormatException("Byte array length must be at least 4 when converting to integer.");
		return (unsignedByteToShort(bytes[0]) << 24) | (unsignedByteToShort(bytes[1]) << 16)
				| (unsignedByteToShort(bytes[2]) << 8) | unsignedByteToShort(bytes[3]);
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static int bytesToInteger(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 4) {
			for (byte b : bytes)
				System.out.print((Integer.toHexString(unsignedByteToShort(b)).length() < 2
						? "0" + Integer.toHexString(unsignedByteToShort(b))
						: Integer.toHexString(unsignedByteToShort(b))) + " ");
			System.out.println();
			throw new NumberFormatException("Byte array length must be at least 4 when converting to integer.");
		}
		return (unsignedByteToShort(bytes[startIdx]) << 24) | (unsignedByteToShort(bytes[startIdx + 1]) << 16)
				| (unsignedByteToShort(bytes[startIdx + 2]) << 8) | unsignedByteToShort(bytes[startIdx + 3]);
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static long bytesToLong(byte[] bytes) {
		if (bytes.length < 8)
			throw new NumberFormatException("Byte array length must be at least 8 when converting to long.");
		return ((long) unsignedByteToShort(bytes[0]) << 56) | ((long) unsignedByteToShort(bytes[1]) << 48)
				| ((long) unsignedByteToShort(bytes[2]) << 40) | ((long) unsignedByteToShort(bytes[3]) << 32)
				| (unsignedByteToShort(bytes[4]) << 24) | (unsignedByteToShort(bytes[5]) << 16)
				| (unsignedByteToShort(bytes[6]) << 8) | unsignedByteToShort(bytes[7]);
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static long bytesToLong(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 8)
			throw new NumberFormatException("Byte array length must be at least 8 when converting to long.");
		return ((long) unsignedByteToShort(bytes[startIdx]) << 56)
				| ((long) unsignedByteToShort(bytes[startIdx + 1]) << 48)
				| ((long) unsignedByteToShort(bytes[startIdx + 2]) << 40)
				| ((long) unsignedByteToShort(bytes[startIdx + 3]) << 32)
				| (unsignedByteToShort(bytes[startIdx + 4]) << 24) | (unsignedByteToShort(bytes[startIdx + 5]) << 16)
				| (unsignedByteToShort(bytes[startIdx + 6]) << 8) | unsignedByteToShort(bytes[startIdx + 7]);
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @return
	 */
	public static short bytesToShort(byte[] bytes) {
		if (bytes.length < 2)
			throw new NumberFormatException("Byte array length must be at least 2 when converting to short.");
		return (short) ((unsignedByteToShort(bytes[0]) << 8) | unsignedByteToShort(bytes[1]));
	}

	// TODO Document
	/**
	 * 
	 * @param bytes
	 * @param startIdx
	 * @return
	 */
	public static short bytesToShort(byte[] bytes, int startIdx) {
		if (bytes.length - startIdx < 2)
			throw new NumberFormatException("Byte array length must be at least 2 when converting to short.");
		return (short) ((unsignedByteToShort(bytes[startIdx]) << 8) | unsignedByteToShort(bytes[startIdx + 1]));
	}

	// TODO Document
	/**
	 * 
	 * @param charArray
	 * @return
	 */
	public static byte[] characterArrayToByteArray(char[] charArray) {
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		return byteArray;
	}

	// TODO Document
	/**
	 * 
	 * @param thisDouble
	 * @return
	 */
	public static byte[] toBytes(double thisDouble) {
		byte[] bytes = new byte[8];
		long doubleLong = Double.doubleToLongBits(thisDouble);
		bytes[0] = (byte) ((doubleLong >> 56) & 0xFF);
		bytes[1] = (byte) ((doubleLong >> 48) & 0xFF);
		bytes[2] = (byte) ((doubleLong >> 40) & 0xFF);
		bytes[3] = (byte) ((doubleLong >> 32) & 0xFF);
		bytes[4] = (byte) ((doubleLong >> 24) & 0xFF);
		bytes[5] = (byte) ((doubleLong >> 16) & 0xFF);
		bytes[6] = (byte) ((doubleLong >> 8) & 0xFF);
		bytes[7] = (byte) (doubleLong & 0xFF);
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param theseDoubles
	 * @return
	 */
	public static byte[] toBytes(double[] theseDoubles) {
		if (theseDoubles == null)
			throw new NullPointerException("Double array was null.");
		byte[] bytes = new byte[theseDoubles.length * 8];
		int index = 0;
		for (double thisDouble : theseDoubles) {
			long doubleLong = Double.doubleToLongBits(thisDouble);
			bytes[index] = (byte) ((doubleLong >> 56) & 0xFF);
			bytes[index + 1] = (byte) ((doubleLong >> 48) & 0xFF);
			bytes[index + 2] = (byte) ((doubleLong >> 40) & 0xFF);
			bytes[index + 3] = (byte) ((doubleLong >> 32) & 0xFF);
			bytes[index + 4] = (byte) ((doubleLong >> 24) & 0xFF);
			bytes[index + 5] = (byte) ((doubleLong >> 16) & 0xFF);
			bytes[index + 6] = (byte) ((doubleLong >> 8) & 0xFF);
			bytes[index + 7] = (byte) (doubleLong & 0xFF);
			index += 8;
		}
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param thisFloat
	 * @return
	 */
	public static byte[] toBytes(float thisFloat) {
		byte[] bytes = new byte[4];
		int floatInt = Float.floatToIntBits(thisFloat);
		bytes[0] = (byte) ((floatInt >> 24) & 0xFF);
		bytes[1] = (byte) ((floatInt >> 16) & 0xFF);
		bytes[2] = (byte) ((floatInt >> 8) & 0xFF);
		bytes[3] = (byte) (floatInt & 0xFF);
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param theseFloats
	 * @return
	 */
	public static byte[] toBytes(float[] theseFloats) {
		if (theseFloats == null)
			throw new NullPointerException("Float array was null.");
		byte[] bytes = new byte[theseFloats.length * 4];
		int index = 0;
		for (float thisFloat : theseFloats) {
			int floatInt = Float.floatToIntBits(thisFloat);
			bytes[index] = (byte) ((floatInt >> 24) & 0xFF);
			bytes[index + 1] = (byte) ((floatInt >> 16) & 0xFF);
			bytes[index + 2] = (byte) ((floatInt >> 8) & 0xFF);
			bytes[index + 3] = (byte) (floatInt & 0xFF);
			index += 4;
		}
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param thisInteger
	 * @return
	 */
	public static byte[] toBytes(int thisInteger) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) ((thisInteger >> 24) & 0xFF);
		bytes[1] = (byte) ((thisInteger >> 16) & 0xFF);
		bytes[2] = (byte) ((thisInteger >> 8) & 0xFF);
		bytes[3] = (byte) (thisInteger & 0xFF);
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param theseIntegers
	 * @return
	 */
	public static byte[] toBytes(int[] theseIntegers) {
		if (theseIntegers == null)
			throw new NullPointerException("Integer array was null.");
		byte[] bytes = new byte[theseIntegers.length * 4];
		int index = 0;
		for (int integer : theseIntegers) {
			bytes[index] = (byte) ((integer >> 24) & 0xFF);
			bytes[index + 1] = (byte) ((integer >> 16) & 0xFF);
			bytes[index + 2] = (byte) ((integer >> 8) & 0xFF);
			bytes[index + 3] = (byte) (integer & 0xFF);
			index += 4;
		}
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param thisLong
	 * @return
	 */
	public static byte[] toBytes(long thisLong) {
		byte[] bytes = new byte[8];
		bytes[0] = (byte) ((thisLong >> 56) & 0xFF);
		bytes[1] = (byte) ((thisLong >> 48) & 0xFF);
		bytes[2] = (byte) ((thisLong >> 40) & 0xFF);
		bytes[3] = (byte) ((thisLong >> 32) & 0xFF);
		bytes[4] = (byte) ((thisLong >> 24) & 0xFF);
		bytes[5] = (byte) ((thisLong >> 16) & 0xFF);
		bytes[6] = (byte) ((thisLong >> 8) & 0xFF);
		bytes[7] = (byte) (thisLong & 0xFF);
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param theseLongs
	 * @return
	 */
	public static byte[] toBytes(long[] theseLongs) {
		if (theseLongs == null)
			throw new NullPointerException("Long array was null.");
		byte[] bytes = new byte[theseLongs.length * 8];
		int index = 0;
		for (long thisLong : theseLongs) {
			bytes[index] = (byte) ((thisLong >> 56) & 0xFF);
			bytes[index + 1] = (byte) ((thisLong >> 48) & 0xFF);
			bytes[index + 2] = (byte) ((thisLong >> 40) & 0xFF);
			bytes[index + 3] = (byte) ((thisLong >> 32) & 0xFF);
			bytes[index + 4] = (byte) ((thisLong >> 24) & 0xFF);
			bytes[index + 5] = (byte) ((thisLong >> 16) & 0xFF);
			bytes[index + 6] = (byte) ((thisLong >> 8) & 0xFF);
			bytes[index + 7] = (byte) (thisLong & 0xFF);
			index += 8;
		}
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param thisShort
	 * @return
	 */
	public static byte[] toBytes(short thisShort) {
		byte[] bytes = new byte[2];
		bytes[0] = (byte) ((thisShort >> 8) & 0xFF);
		bytes[1] = (byte) (thisShort & 0xFF);
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param theseShorts
	 * @return
	 */
	public static byte[] toBytes(short[] theseShorts) {
		if (theseShorts == null)
			throw new NullPointerException("Short array was null.");
		byte[] bytes = new byte[theseShorts.length * 2];
		int index = 0;
		for (short thisShort : theseShorts) {
			bytes[index] = (byte) ((thisShort >> 8) & 0xFF);
			bytes[index + 1] = (byte) (thisShort & 0xFF);
			index += 2;
		}
		return bytes;
	}

	// TODO Document
	/**
	 * 
	 * @param thisByte
	 * @return
	 */
	// Slightly unnecessary, now that Java supports unsigned conversions.
	public static short unsignedByteToShort(byte thisByte) {
		return (short) (thisByte & 0xFF);
	}

	// TODO Document
	/**
	 * 
	 */
	private BinaryOperations() {
		// Private to prevent instantiation
	}
}
