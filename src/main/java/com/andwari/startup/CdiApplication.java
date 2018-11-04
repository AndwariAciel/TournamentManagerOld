package com.andwari.startup;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

import javafx.application.Application;
import javafx.stage.Stage;

public class CdiApplication extends Application {
	
	@SuppressWarnings("serial")
	@Override
	public void start(Stage primaryStage) {
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();		
		final SeContainer container = initializer.initialize();
		container.getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
