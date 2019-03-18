package com.andwari.event.standingoverview.control;

import com.andwari.event.rankings.dvos.RankingsDvo;

import javafx.scene.control.Labeled;
import javafx.scene.control.TableRow;
import javafx.scene.paint.Color;

public class StandingListRowView extends TableRow<RankingsDvo> {
	
	@Override
	protected void updateItem(RankingsDvo standingItem, boolean empty) {
		super.updateItem(standingItem, empty);

		if (empty || standingItem == null) {
			setText(null);
			setGraphic(null);
		} else {
			if(standingItem.isDropped()) {
				getChildren().stream().forEach(c -> ((Labeled) c).setTextFill(Color.GRAY));
			} else {
				getChildren().stream().forEach(c -> ((Labeled) c).setTextFill(Color.BLACK));
			}
		}
	
	}


}
