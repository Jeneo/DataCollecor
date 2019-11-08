package com.collector.util.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class GzTarHw {

	public static String unGzTarFile(String decompressFilePath, String outputDirectory) throws Exception {
		String filnameString = null;
		FileInputStream fis = null;
		ArchiveInputStream in = null;
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream;
		String zipfileName = decompressFilePath;
		try {
			fis = new FileInputStream(zipfileName);
			GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
			in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);
			bufferedInputStream = new BufferedInputStream(in);
			TarArchiveEntry entry = (TarArchiveEntry) in.getNextEntry();
			while (entry != null) {
				String name = entry.getName();
				name=new String(name.getBytes("gbk"),"utf-8");
				String[] names = name.split("/");
				String fileName = outputDirectory;
				for (int i = 0; i <= names.length - 1; i++) {
					String str = names[i];
					fileName = fileName + File.separator + str;
				}
				if (name.endsWith("/")) {
					mkFolder(fileName);
				} else {
					File file = mkFile(fileName);
					bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
					int b;
					while ((b = bufferedInputStream.read()) != -1) {
						bufferedOutputStream.write(b);
					}
					bufferedOutputStream.flush();
					bufferedOutputStream.close();
					if (file.getName().indexOf(".txt") > 0) {
						filnameString = file.getName();
					}
				}
				entry = (TarArchiveEntry) in.getNextEntry();
			}
		} finally {
			try {
				if (bufferedInputStream != null) {
					bufferedInputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return filnameString;
	}

	private static void mkFolder(String fileName) {
		File f = new File(fileName);
		if (!f.exists()) {
			f.mkdir();
		}
	}

	private static File mkFile(String fileName) {
		File f = new File(fileName);
		try {
			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}
}
