package com.andwari;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.andwari.event.playerselection.PlayerSelectionController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class FxmlPageManager {
	
	@Inject
	private Instance<FXMLLoader> instance;
	
	private	Logger logger = Logger.getLogger(this.getClass());
		
	public URL findFxmlResource(String res) {
		JarFile jar = null;
		try {
			URL url = getClass().getProtectionDomain().getCodeSource().getLocation();
			File file = new File(url.getFile());
			File classfolder = new File(file.getParent() + "/classes/");
			jar = new JarFile(file);
			Enumeration<JarEntry> i = jar.entries();
			while (i.hasMoreElements()) {
				JarEntry e = i.nextElement();
				if (e.getName().endsWith(res)) {
					classfolder = new File(classfolder, e.getName());
					jar.close();
					return classfolder.toURI().toURL();
				}
			}
		} catch (FileNotFoundException e) {
			//Jar File Not Found
			URL url = getClass().getResource(res); 
			return url;
		} catch (IOException e1) {
			logger.error(e1);
		} finally {
			try {
				if(jar != null)
					jar.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}
	
	public Object openNewWindow(String xhtmlPath) {
		PlayerSelectionController controller = null;
		try {
			FXMLLoader loader = instance.get();
			URL fxmlRes = findFxmlResource("event/EventPlayerSelection.fxml");
			loader.setLocation(fxmlRes);
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			Stage newWindow = new Stage();
			newWindow.setScene(scene);
			controller = loader.getController();
			controller.setStage(newWindow);
			newWindow.show();
		} catch (Exception e) {
			logger.error(e);
		}
		return controller;
	}
	
}
