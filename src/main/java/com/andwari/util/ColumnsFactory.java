package com.andwari.util;

import java.util.ArrayList;
import java.util.List;

import com.andwari.main.player.PlayerDVO;

import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ColumnsFactory<T> {

	public static List<TableColumn<PlayerDVO, String>> getColumnsPlayerWithId() {
		TableColumn<PlayerDVO, String> playerId = new TableColumn<PlayerDVO, String>("Id");

		playerId.setCellValueFactory(new PropertyValueFactory<>("id"));

		List<TableColumn<PlayerDVO, String>> list = getColumnsPlayer();
		list.add(playerId);

		return list;
	}
	
	public static List<TableColumn<PlayerDVO, String>> getColumnsPlayer() {
		TableColumn<PlayerDVO, String> playerName = new TableColumn<PlayerDVO, String>("Player");
		TableColumn<PlayerDVO, String> playerDci = new TableColumn<PlayerDVO, String>("DCI");

		playerName.setCellValueFactory(new PropertyValueFactory<>("name"));
		playerDci.setCellValueFactory(new PropertyValueFactory<>("dci"));

		ArrayList<TableColumn<PlayerDVO, String>> list = new ArrayList<>();
		list.add(playerName);
		list.add(playerDci);

		return list;
	}
	
	public List<TableColumn<T, String>> getColumns(Object[] ...objects) {
		ArrayList<TableColumn<T, String>> list = new ArrayList<>();
		for(Object[] object : objects) {
			list.add(getColumn((String)object[0], (Double)object[1]));
		}
		return list;
	}
	
	private TableColumn<T, String> getColumn(String name, Double width) {
		TableColumn<T, String> column = new TableColumn<>(name);
		column.setMinWidth(width);
		return column;
	}
	
}
