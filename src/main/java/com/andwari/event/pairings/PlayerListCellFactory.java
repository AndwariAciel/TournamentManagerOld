package com.andwari.event.pairings;

import com.andwari.core.tournamentcore.player.entity.Player;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

class PlayerListCellFactory implements Callback<ListView<Player>, ListCell<Player>> {

	@Override
	public ListCell<Player> call(ListView<Player> list) {
		ListCell<Player> cell = new ListCell<Player>() {
			@Override
			protected void updateItem(Player item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					setText(item.getPlayerName());
				} else {
					setText(null);
				}
			}
		};

		cell.setOnDragDetected((MouseEvent event) -> {
			Dragboard db = cell.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(PairingsPageController.dndKey, cell.getItem());
			db.setContent(content);
			event.consume();
		});

		cell.setOnDragDone(e -> {
			if (e.getTransferMode() != null) {
				Player player = cell.getItem();
				list.getItems().remove(player);
			}
			e.consume();
		});

		cell.setOnDragOver(e -> {
			if (e.getTransferMode().equals(TransferMode.MOVE)) {
				e.acceptTransferModes(TransferMode.MOVE);
			}
			e.consume();
		});

		cell.setOnDragDropped(e -> {
			Player player = (Player) e.getDragboard().getContent(PairingsPageController.dndKey);
			list.getItems().add(player);
			e.setDropCompleted(true);
			e.consume();
		});

		return cell;
	}

}
