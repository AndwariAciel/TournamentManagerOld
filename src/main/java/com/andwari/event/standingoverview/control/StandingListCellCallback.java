package com.andwari.event.standingoverview.control;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.andwari.event.rankings.dvos.RankingsDvo;

import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Callback;

public class StandingListCellCallback implements Callback<TableView<RankingsDvo>, TableRow<RankingsDvo>> {

	@Inject
	private Instance<StandingListRowView> instance;
	
	@Override
	public TableRow<RankingsDvo> call(TableView<RankingsDvo> param) {
		return instance.get();
	}

}
