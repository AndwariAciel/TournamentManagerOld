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
		final SeContainer container = initializer
//				.disableDiscovery()
//				.addBeanClasses(SeatingsPageController.class, App.class, TabPlayerController.class,
//						TabEventController.class, TabPlayerService.class, PlayerSelectionController.class,
//						SeatingsDvoConverter.class, PlayerConverter.class, PlayerService.class,
//						FXMLLoaderProducer.class, MainWindowController.class, MatchesPageController.class,
//						MatchDvoConverter.class, MatchListCellView.class, MatchesPageService.class,
//						MatchListCallback.class, MatchViewController.class, RankingsDvoConverter.class,
//
//						MatchFactory.class, PlayerController.class, PlayerRepository.class, PlayerService.class,
//						MatchService.class, StandingsService.class, StandingsCalculator.class, MatchValidator.class,
//						EventService.class, MatchResultService.class, RoundService.class, RoundRepository.class,
//						MatchRepository.class, MatchResultRepository.class, MatchMaker.class)
				.initialize();
		container.getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
