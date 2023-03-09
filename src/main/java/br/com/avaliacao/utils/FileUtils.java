package br.com.avaliacao.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

public class FileUtils {

	private static ThreadLocal<String> tokenLocal = new ThreadLocal<String>();

	public static void setToken(String token) {
		if (tokenLocal.get() == null) {
			tokenLocal.set(token);
		}
	}

	public static String createRandomName(String name) {
		return tokenLocal.get() + "_" + randomNumber() + "_" + name;
	}

	private static Integer randomNumber() {
		return new Random().nextInt();
	}

	public static byte[] toByteArray(String filename) throws IOException {
		File file = new File(filename);
		return toByteArray(file);
	}

	public static byte[] toByteArray(File file) throws IOException {
		return Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	}

	public static byte[] toByteArray(FileUploadEvent event) throws IOException {
		UploadedFile file = event.getFile();
		InputStream inputstream = file.getInputstream();
		return IOUtils.toByteArray(inputstream);
	}
	
	public static void deleteFromSource(String tempImageName) {
		tempImageName = sourcePath() + File.separator + tempImageName;
		new File(tempImageName).delete();
	}
	
	public static void mkDir(String filename) {
		File file = new File(filename);
		if(!file.exists()){
			file.mkdirs();
		}
	}
	

	public static String getTempFilename(String imageName) {
		return sourcePath() + File.separator + "temp" + File.separator + imageName;
	}

	private static String sourcePath() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext context = (ServletContext) facesContext.getExternalContext().getContext();
		return context.getRealPath("resources");
	}
}
