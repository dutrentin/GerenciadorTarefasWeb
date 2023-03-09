package br.com.avaliacao.utils;

import java.util.ResourceBundle;

import javax.faces.application.Application;
import javax.faces.context.FacesContext;

public class UtilResource {
	
	public static String getResourceProperty(String resource, String label) {
		FacesContext fc = FacesContext.getCurrentInstance();
		Application application = fc.getApplication();
		ResourceBundle bundle = application.getResourceBundle(fc, resource);

		return bundle.getString(label);
	}

}
