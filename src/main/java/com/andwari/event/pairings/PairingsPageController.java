package com.andwari.event.pairings;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.andwari.FxmlPageManager;
import com.andwari.core.tournamentcore.event.EventRepository;
import com.andwari.core.tournamentcore.event.boundary.EventService;
import com.andwari.core.tournamentcore.event.entity.Event;
import com.andwari.core.tournamentcore.event.entity.Match;
import com.andwari.core.tournamentcore.event.entity.Round;
import com.andwari.core.tournamentcore.matches.MatchFactory;
import com.andwari.core.tournamentcore.player.control.PlayerController;
import com.andwari.core.tournamentcore.player.entity.Player;
import com.andwari.event.matches.MatchesPageController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.TextField;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PairingsPageController {

	public static final DataFormat dndKey = new DataFormat("dndKey");

	@FXML
	private ListView<Player> playersLeft, playersRight, playersList;

	@FXML
	private Button btnNext;

	@FXML
	private TextField byeText;

	@FXML
	private Label byeLabel;

	private ObservableList<Player> listLeft, listRight, listPlayers;

	private Event event;

	private Stage stage;

	@Inject
	private Instance<FXMLLoader> fxmlLoaderInst;

	@Inject
	private FxmlPageManager finder;

	@Inject
	private MatchFactory matchFactory;

	@Inject
	private EventRepository eventRepos;

	@Inject
	private EventService eventService;

	@Inject
	private PlayerController playerControl;

	public void init() {

		/*
		 * Populate lists with empty players depending on actual number of players add
		 * drag and drop events
		 */

		listPlayers = FXCollections.observableArrayList(getPlayersFromEvent());
		playersList.setItems(listPlayers);
		playersList.setCellFactory(new PlayerListCellFactory());

		listRight = FXCollections.observableArrayList(getEmptyPlayers(listPlayers.size()));
		playersRight.setItems(listRight);
		playersRight.setCellFactory(new PairingsListCellFactory(playersList));

		listLeft = FXCollections.observableArrayList(getEmptyPlayers(listPlayers.size()));
		playersLeft.setItems(listLeft);
		playersLeft.setCellFactory(new PairingsListCellFactory(playersList));

		disableByeFieldIfUnevenPlayers(listPlayers.size());
		setDnDEventsToTextField(byeText);
	}

	private void disableByeFieldIfUnevenPlayers(int size) {
		if (size % 2 == 0) {
			byeText.setVisible(false);
			byeLabel.setVisible(false);
		}

	}

	public void linkViews() {
		Node n1 = playersRight.lookup(".scroll-bar:vertical");
		if (n1 instanceof ScrollBar) {
			final ScrollBar bar1 = (ScrollBar) n1;
			Node n2 = playersLeft.lookup(".scroll-bar");
			if (n2 instanceof ScrollBar) {
				final ScrollBar bar2 = (ScrollBar) n2;
				bar1.valueProperty().bindBidirectional(bar2.valueProperty());
			}
		}

	}

	public void nextPage() {
		Round round1 = matchFactory.createManuelPairings(event, listLeft, listRight, listPlayers,
				byeText.getText() != null && !byeText.getText().isEmpty() ? parsePlayer(byeText.getText()) : null);
		reloadPlayerIds(round1);
		eventService.createSeatings(event);
		event.getRounds().add(round1);
		event.setCurrentRound(1);
		eventRepos.update(event);

		try {
			FXMLLoader fxmlLoader = fxmlLoaderInst.get();
			fxmlLoader.setLocation(finder.findFxmlResource("event/matches/EventMatches.fxml"));
			BorderPane root = (BorderPane) fxmlLoader.load();
			MatchesPageController controller = fxmlLoader.getController();
			controller.initialize(round1);

			stage.getScene().setRoot(root);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void reloadPlayerIds(Round round1) {
		round1.getMatches().stream().forEach(this::reloadIds);
		if (round1.getBye() != null) {
			round1.setBye(playerControl.load(round1.getBye()));
		}
	}

	private void reloadIds(Match m) {
		m.setPlayer1(playerControl.load(m.getPlayer1()));
		m.setPlayer2(playerControl.load(m.getPlayer2()));
	}

	private List<Player> getEmptyPlayers(int size) {
		return IntStream.range(0, size / 2).mapToObj(i -> new EmptyPlayer()).collect(Collectors.toList());
	}

	private List<Player> getPlayersFromEvent() {
		return event.getRankings().stream().map(r -> r.getPlayer()).collect(Collectors.toList());
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	private void setDnDEventsToTextField(TextField field) {
		field.setOnDragDetected((MouseEvent event) -> {
			if (field.getText() == null || field.getText().isEmpty()) {
				event.consume();
				return;
			}
			Dragboard db = field.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(PairingsPageController.dndKey, parsePlayer(field.getText()));
			db.setContent(content);
			event.consume();
		});

		field.setOnDragDone(e -> {
			if (e.getTransferMode() != null) {
				field.setText(null);
			}
			e.consume();
		});

		field.setOnDragOver(e -> {
			if (e.getTransferMode().equals(TransferMode.MOVE)) {
				e.acceptTransferModes(TransferMode.MOVE);
			}
			e.consume();
		});

		field.setOnDragDropped(e -> {
			Player player = (Player) e.getDragboard().getContent(PairingsPageController.dndKey);
			field.setText(player.getPlayerName());
			e.setDropCompleted(true);
			e.consume();
		});
	}

	private Player parsePlayer(String playerName) {
		return getPlayersFromEvent().stream().filter(p -> p.getPlayerName().equals(playerName)).findFirst()
				.orElseThrow(() -> new IllegalStateException("No Player found in Event."));
	}

}
