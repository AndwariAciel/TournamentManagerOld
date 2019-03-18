package com.andwari.event.matches.control;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.andwari.event.matches.MatchListCellView;
import com.andwari.event.matches.dvos.MatchListDvo;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class MatchListCallback implements Callback<ListView<MatchListDvo>, ListCell<MatchListDvo>> {

	@Inject
	private Instance<MatchListCellView> cell;
	
    @Override
    public ListCell<MatchListDvo> call(ListView<MatchListDvo> studentListView) {
        return cell.get();
    }
}
