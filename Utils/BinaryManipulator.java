package Utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The Class BinaryManipulator. This class handles loading and compressing of
 * byte arrays.
 * 
 * @author Joaquín Alejandro Pérez Fuentes
 */
public class BinaryManipulator {

	/**
	 * Reads a file to a byte array. Route must be a valid input for
	 * "FileInputStream"
	 * 
	 * @param file file
	 * 
	 * @return byte[]
	 */
	public static byte[] readByteArray(final String file) {
		try {
			FileInputStream fis = new FileInputStream(file);

			byte[] bytearray = new byte[fis.available()];
			fis.read(bytearray);

			fis.close();

			return bytearray;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Write byte array.
	 * 
	 * @param newFile newFile
	 * @param bytearray bytearray
	 */
	public static void writeByteArray(final String newFile,
			final byte[] bytearray) {
		try {
			long cantBytes = bytearray.length;
			FileOutputStream out = new FileOutputStream(newFile);

			for (int i = 0; i < cantBytes; i++) {
				out.write(bytearray[i]);
			}
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Compress byte array.
	 * 
	 * @param bytearray bytearray
	 * 
	 * @return byte[]
	 */
	public static byte[] compressByteArray(final byte[] bytearray) {
		// Compressor with highest level of compression
		Deflater compressor = new Deflater();
		compressor.setLevel(Deflater.BEST_COMPRESSION);

		// Give the compressor the data to compress
		compressor.setInput(bytearray);
		compressor.finish();

		// Create an expandable byte array to hold the compressed data.
		// It is not necessary that the compressed data will be smaller than
		// the uncompressed data.
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytearray.length);

		// Compress the data
		byte[] buf = new byte[1024];
		while (!compressor.finished()) {
			int count = compressor.deflate(buf);
			bos.write(buf, 0, count);
		}
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the compressed data
		return bos.toByteArray();
	}

	/**
	 * Decompress byte array.
	 * 
	 * @param bytearray bytearray
	 * 
	 * @return byte[]
	 */
	public static byte[] decompressByteArray(final byte[] bytearray) {
		Inflater decompressor = new Inflater();
		decompressor.setInput(bytearray);

		// Create an expandable byte array to hold the decompressed data
		ByteArrayOutputStream bos = new ByteArrayOutputStream(bytearray.length);

		// Decompress the data
		byte[] buf = new byte[1024];
		while (!decompressor.finished()) {
			try {
				int count = decompressor.inflate(buf);
				bos.write(buf, 0, count);
			} catch (DataFormatException e) {
				e.printStackTrace();
			}
		}
		try {
			bos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the decompressed data
		return bos.toByteArray();
	}
}
