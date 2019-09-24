package com.andwari.event.pairings;

import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

class PairingsListCellFactory implements Callback<ListView<Player>, ListCell<Player>> {

	private ListView<Player> playerList;

	public PairingsListCellFactory(ListView<Player> playerList) {
		this.playerList = playerList;
	}

	@Override
	public ListCell<Player> call(ListView<Player> list) {
		ListCell<Player> cell = new ListCell<Player>() {

			@Override
			protected void updateItem(Player item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					setText(item.getPlayerName());
					setStyle(
							"-fx-padding: 10px; -fx-background-color: transparent, #e3e3e3; -fx-background-insets: 0px, 5px;");
				} else {
					setText(null);
					setStyle("-fx-padding: 10px; -fx-background-color: white;");
				}
			}
		};

		cell.setOnDragDetected(e -> {
			Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(PairingsPageController.dndKey, cell.getItem());
			db.setContent(content);
			e.consume();
		});

		cell.setOnDragEntered(e -> {
			if (!cell.isEmpty())
				cell.setStyle(
						"-fx-padding: 10px; -fx-background-color: transparent, #bdbdbd; -fx-background-insets: 0px, 5px;");
		});

		cell.setOnDragExited(e -> {
			if (!cell.isEmpty())
				cell.setStyle(
						"-fx-padding: 10px; -fx-background-color: transparent, #e3e3e3; -fx-background-insets: 0px, 5px;");
		});

		cell.setOnDragOver(e -> {
			if (e.getTransferMode().equals(TransferMode.MOVE) && !cell.isEmpty()
					&& !((Player) e.getDragboard().getContent(PairingsPageController.dndKey)).getPlayerName()
							.equals(cell.getItem().getPlayerName())) {
				e.acceptTransferModes(TransferMode.MOVE);
			}
			e.consume();
		});

		cell.setOnDragDropped(e -> {
			Player player = (Player) e.getDragboard().getContent(PairingsPageController.dndKey);
			int index = cell.getIndex();
			if (index >= list.getItems().size()) {
				list.getItems().add(player);
			} else {
				if (isToBeAdded(list.getItems().get(index), player)) {
					playerList.getItems().add(list.getItems().get(index));
				}
				list.getItems().remove(index);
				list.getItems().add(index, player);
			}
			e.setDropCompleted(true);
			e.consume();
		});

		cell.setOnDragDone(e -> {
			if (e.getTransferMode() != null) {
				int index = cell.getIndex();
				list.getItems().remove(index);
				list.getItems().add(index, new EmptyPlayer());
			}
			e.consume();
		});

		return cell;
	}

	private boolean isToBeAdded(Player playerToAdd, Player droppedPlayer) {
		if (playerToAdd instanceof EmptyPlayer)
			return false;
		return true;
	}

}
