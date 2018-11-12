package com.andwari.startup;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.enterprise.util.AnnotationLiteral;

import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.matches.MatchFactory;
import com.andwari.core.tournamentcore.matches.MatchRepository;
import com.andwari.core.tournamentcore.matches.MatchService;
import com.andwari.core.tournamentcore.matches.MatchValidator;
import com.andwari.core.tournamentcore.matchresult.MatchResultRepository;
import com.andwari.core.tournamentcore.matchresult.MatchResultService;
import com.andwari.core.tournamentcore.player.boundary.PlayerService;
import com.andwari.core.tournamentcore.player.control.PlayerController;
import com.andwari.core.tournamentcore.player.repos.PlayerRepository;
import com.andwari.core.tournamentcore.rounds.RoundRepository;
import com.andwari.core.tournamentcore.rounds.RoundService;
import com.andwari.core.tournamentcore.standings.StandingsCalculator;
import com.andwari.core.tournamentcore.standings.StandingsService;
import com.andwari.event.matches.MatchListCellView;
import com.andwari.event.matches.MatchViewController;
import com.andwari.event.matches.MatchesPageController;
import com.andwari.event.matches.control.MatchListCallback;
import com.andwari.event.matches.control.MatchesPageService;
import com.andwari.event.matches.converter.MatchDvoConverter;
import com.andwari.event.playerselection.PlayerSelectionController;
import com.andwari.event.rankings.converter.RankingsDvoConverter;
import com.andwari.event.seatings.SeatingsDvoConverter;
import com.andwari.event.seatings.SeatingsPageController;
import com.andwari.main.MainWindowController;
import com.andwari.main.TabEventController;
import com.andwari.main.TabPlayerController;
import com.andwari.playermanagement.PlayerConverter;
import com.andwari.playermanagement.TabPlayerService;

import javafx.application.Application;
import javafx.stage.Stage;

public class CdiApplication extends Application {

	@SuppressWarnings("serial")
	@Override
	public void start(Stage primaryStage) {
		SeContainerInitializer initializer = SeContainerInitializer.newInstance();
		final SeContainer container = initializer.disableDiscovery()
				.addBeanClasses(SeatingsPageController.class, App.class, TabPlayerController.class,
						TabEventController.class, TabPlayerService.class, PlayerSelectionController.class,
						SeatingsDvoConverter.class, PlayerConverter.class, PlayerService.class,
						FXMLLoaderProducer.class, MainWindowController.class, MatchesPageController.class,
						MatchDvoConverter.class, MatchListCellView.class, MatchesPageService.class,
						MatchListCallback.class, MatchViewController.class, RankingsDvoConverter.class,

						MatchFactory.class, PlayerController.class, PlayerRepository.class, PlayerService.class,
						MatchService.class, StandingsService.class, StandingsCalculator.class, MatchValidator.class,
						EventService.class, MatchResultService.class, RoundService.class, RoundRepository.class,
						MatchRepository.class, MatchResultRepository.class)
				.initialize();
		container.getBeanManager().fireEvent(primaryStage, new AnnotationLiteral<StartupScene>() {
		});
	}

	public static void main(String[] args) {
		launch(args);
	}

}
