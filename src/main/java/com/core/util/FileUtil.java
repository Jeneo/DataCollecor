package com.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

public class FileUtil {

	/**
	 * 
	 * 文件或目录是否存在
	 * 
	 */

	public static boolean exists(String path) {

		return new File(path).exists();
	}

	/**
	 * 
	 * 文件是否存在
	 * 
	 */

	public static boolean existsFile(String path) {

		File file = new File(path);
		return file.exists() && file.isFile();
	}

	/**
	 * 
	 * 文件或目录是否存在
	 * 
	 */

	public static boolean existsAny(String... paths) {

		return Arrays.stream(paths).anyMatch(path -> new File(path).exists());
	}

	/**
	 * 
	 * 清空目录
	 * 
	 * @throws IOException
	 * 
	 */
	public static void emptyPath(File file) throws IOException {
		if (!file.isFile()) {
			File[] files = file.listFiles();
			if (files != null && files.length > 0) {
				for (File temp : files) {
					deleteIfExists(temp);
				}
			}
		}
	}

	public static void emptyPath(String path) throws IOException {
		File file = new File(path);
		emptyPath(file);
	}

	/**
	 * 
	 * 删除文件或文件夹
	 * 
	 */
	public static void deleteIfExists(File file) throws IOException {

		if (file.exists()) {

			if (file.isFile()) {

				if (!file.delete()) {

					throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
				}

			} else {
				emptyPath(file);
				if (!file.delete()) {
					throw new IOException("Delete file failure,path:" + file.getAbsolutePath());
				}
			}
		}
	}

	/**
	 * 
	 * 删除文件或文件夹
	 * 
	 */

	public static void deleteIfExists(String path) throws IOException {
		deleteIfExists(new File(path));
	}

	/**
	 * 
	 * 创建文件，如果目标存在则删除
	 * 
	 */
	public static File createFile(String path) throws IOException {
		return createFile(path, false);
	}

	/**
	 * 
	 * 创建文件夹，如果目标存在则删除
	 * 
	 */

	public static File createDir(String path) throws IOException {
		return createDir(path, false);
	}

	/**
	 * 
	 * 创建文件，如果目标存在则删除
	 * 
	 */

	public static File createFile(String path, boolean isHidden) throws IOException {

		File file = createFileSmart(path);
		if (OsUtil.isWindows()) {

			Files.setAttribute(file.toPath(), "dos:hidden", isHidden);
		}

		return file;
	}

	/**
	 * 
	 * 创建文件夹，如果目标存在则删除
	 * 
	 */

	public static File createDir(String path, boolean isHidden) throws IOException {

		File file = new File(path);
		deleteIfExists(file);
		File newFile = new File(path);
		newFile.mkdir();
		if (OsUtil.isWindows()) {

			Files.setAttribute(newFile.toPath(), "dos:hidden", isHidden);
		}

		return file;
	}

	/**
	 * 
	 * 查看文件或者文件夹大小
	 * 
	 */

	public static long getFileSize(String path) {

		File file = new File(path);
		if (file.exists()) {

			if (file.isFile()) {

				return file.length();
			} else {

				long size = 0;
				File[] files = file.listFiles();
				if (files != null && files.length > 0) {

					for (File temp : files) {

						if (temp.isFile()) {

							size += temp.length();
						}

					}

				}
				return size;
			}
		}
		return 0;
	}
	


	
	public static File createFileSmart(String path) throws IOException {

		try {

			File file = new File(path);
			if (file.exists()) {

				file.delete();
				file.createNewFile();
			} else {

				createDirSmart(file.getParent());
				file.createNewFile();
			}

			return file;
		} catch (IOException e) {

			throw new IOException("createFileSmart=" + path, e);
		}

	}

	public static File createDirSmart(String path) throws IOException {

		try {

			File file = new File(path);
			if (!file.exists()) {

				Stack<File> stack = new Stack<>();
				File temp = new File(path);
				while (temp != null) {

					stack.push(temp);
					temp = temp.getParentFile();
				}

				while (stack.size() > 0) {

					File dir = stack.pop();
					if (!dir.exists()) {

						dir.mkdir();
					}

				}

			}

			return file;
		} catch (Exception e) {

			throw new IOException("createDirSmart=" + path, e);
		}

	}

	/**
	 * 
	 * 获取目录所属磁盘剩余容量
	 * 
	 */

	public static long getDiskFreeSize(String path) {

		File file = new File(path);
		return file.getFreeSpace();
	}

	public static void unmap(MappedByteBuffer mappedBuffer) throws IOException {

		try {

			Class<?> clazz = Class.forName("sun.nio.ch.FileChannelImpl");
			Method m = clazz.getDeclaredMethod("unmap", MappedByteBuffer.class);
			m.setAccessible(true);
			m.invoke(clazz, mappedBuffer);
		} catch (Exception e) {

			throw new IOException("LargeMappedByteBuffer close", e);
		}

	}

	/**
	 * 
	 * 去掉后缀名
	 * 
	 */

	public static String getFileNameNoSuffix(String fileName) {

		int index = fileName.lastIndexOf(".");
		if (index != -1) {

			return fileName.substring(0, index);
		}

		return fileName;
	}

	public static void initFile(String path, boolean isHidden) throws IOException {

		initFile(path, null, isHidden);
	}

	public static void initFile(String path, InputStream input, boolean isHidden) throws IOException {

		if (exists(path)) {

			try (

					RandomAccessFile raf = new RandomAccessFile(path, "rws")

			) {

				raf.setLength(0);
			}

		} else {

			FileUtil.createFile(path, isHidden);
		}

		if (input != null) {

			try (

					RandomAccessFile raf = new RandomAccessFile(path, "rws")

			) {

				byte[] bts = new byte[8192];
				int len;
				while ((len = input.read(bts)) != -1) {

					raf.write(bts, 0, len);
				}

			} finally {

				input.close();
			}

		}

	}

	public static boolean canWrite(String path) {

		File file = new File(path);
		File test;
		if (file.isFile()) {

			test = new File(

					file.getParent() + File.separator + UUID.randomUUID().toString() + ".test");
		} else {

			test = new File(file.getPath() + File.separator + UUID.randomUUID().toString() + ".test");
		}

		try {

			test.createNewFile();
			test.delete();
		} catch (IOException e) {

			return false;
		}

		return true;
	}

	public static void unzip(String zipPath, String toPath, String... unzipFile) throws IOException {

		try (

				ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipPath))

		) {

			toPath = toPath == null ? new File(zipPath).getParent() : toPath;
			ZipEntry entry;
			while ((entry = zipInputStream.getNextEntry()) != null) {

				final String entryName = entry.getName();
				if (entry.isDirectory() || (unzipFile != null && unzipFile.length > 0

						&& Arrays.stream(unzipFile)

								.noneMatch((file) -> entryName.equalsIgnoreCase(file)))) {

					zipInputStream.closeEntry();
					continue;
				}

				File file = createFileSmart(toPath + File.separator + entryName);
				try (

						FileOutputStream outputStream = new FileOutputStream(file)

				) {

					byte[] bts = new byte[8192];
					int len;
					while ((len = zipInputStream.read(bts)) != -1) {

						outputStream.write(bts, 0, len);
					}

				}

			}

		}

	}

	/**
	 * 
	 * 判断文件存在是重命名
	 * 
	 */

	public static String renameIfExists(String path) {

		File file = new File(path);
		if (file.exists() && file.isFile()) {

			int index = file.getName().lastIndexOf(".");
			String name = file.getName().substring(0, index);
			String suffix = index == -1 ? "" : file.getName().substring(index);
			int i = 1;
			String newName;
			do {

				newName = name + "(" + i + ")" + suffix;
				i++;
			}

			while (existsFile(file.getParent() + File.separator + newName));
			return newName;
		}

		return file.getName();
	}

	/**
	 * 
	 * 创建指定大小的Sparse File
	 * 
	 */

	public static void createFileWithSparse(String filePath, long length) throws IOException {

		Path path = Paths.get(filePath);
		try {

			Files.deleteIfExists(path);
			try (

					SeekableByteChannel channel = Files.newByteChannel(path, StandardOpenOption.CREATE_NEW,
							StandardOpenOption.WRITE, StandardOpenOption.SPARSE)

			) {

				channel.position(length - 1);
				channel.write(ByteBuffer.wrap(new byte[] { 0 }));
			}

		} catch (IOException e) {

			throw new IOException("create spares file fail,path:" + filePath + " length:" + length, e);
		}

	}

	/**
	 * 
	 * 使用RandomAccessFile创建指定大小的File
	 * 
	 */

	public static void createFileWithDefault(String filePath, long length) throws IOException {

		Path path = Paths.get(filePath);
		try {

			Files.deleteIfExists(path);
			try (

					RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "rw");) {

				randomAccessFile.setLength(length);
			}

		} catch (IOException e) {

			throw new IOException("create spares file fail,path:" + filePath + " length:" + length, e);
		}

	}

	public static String getSystemFileType(String filePath) throws IOException {

		File file = new File(filePath);
		if (!file.exists()) {

			file = file.getParentFile();
		}

		return Files.getFileStore(file.toPath()).type();
	}

	public static Date[] getFilCreAndModiTime(String filename) throws Exception {
		Path path = Paths.get(filename);
		BasicFileAttributeView basicview = Files.getFileAttributeView(path, BasicFileAttributeView.class,
				LinkOption.NOFOLLOW_LINKS);
		BasicFileAttributes attr;
		attr = basicview.readAttributes();
		Date lastmodfiyTimeDate = new Date(attr.lastModifiedTime().toMillis());
		Date CreateTimeDate = new Date(attr.creationTime().toMillis());
		return new Date[] { CreateTimeDate, lastmodfiyTimeDate };

	}

	public static void copyFile(File sourceFile, File descFile) throws IOException {
		if (!descFile.exists())
			Files.copy(sourceFile.toPath(), descFile.toPath());
	}

	public static void copyFile(String sourceString, String descString) throws IOException {
		File sourceFile = new File(sourceString);
		File descFile = new File(descString);
		FileUtil.copyFile(sourceFile, descFile);
	}

	public static List<File> showTimeoutFile(String dirPath, long ms, boolean subdir) {
		// 该日期之前的文件
		List<File> list = new ArrayList<File>();
		Date pointDate = new Date(System.currentTimeMillis() - ms);
		// 文件过滤条件
		IOFileFilter timeFileFilter = FileFilterUtils.ageFileFilter(pointDate, true);
		IOFileFilter fileFiles = new AndFileFilter(FileFileFilter.FILE, timeFileFilter);
		File directory = new File(dirPath);
		Iterator<File> itFile = null;
		if (subdir)
			itFile = FileUtils.iterateFiles(directory, fileFiles, TrueFileFilter.INSTANCE);
		else
			itFile = FileUtils.iterateFiles(directory, fileFiles, null);

		// 删除符合条件的文件
		while (itFile.hasNext()) {
			list.add(itFile.next());
		}
		return list;
	}

	public static String formatFileSize(long size) {
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSize = "";
		if (size < 1024) {
			fileSize = df.format((double) size) + "B";
		} else if (size < 1024 * 1024) {
			fileSize = df.format((double) size / 1024) + "K";
		} else if (size < 1024 * 1024 * 1024) {
			fileSize = df.format((double) size / 1024 / 1024) + "M";
		} else if (size < 1024 * 1024 * 1024 * 1024) {
			fileSize = df.format((double) size / 1024 / 1024 / 1024) + "G";
		}
		return fileSize;
	}

}
