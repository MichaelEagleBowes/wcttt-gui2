/*
 * WCT³ (WIAI Course Timetabling Tool) is a software that strives to automate
 * the timetabling process at the WIAI faculty of the University of Bamberg.
 *
 * WCT³-GUI comprises functionality to view generated timetables, edit semester
 * data and to generate new timetables.
 *
 * Copyright (C) 2018 Nicolas Gross
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package wcttt.gui.controller;

import javafx.application.HostServices;
import javafx.collections.ObservableList;
import wcttt.gui.model.Model;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller for the side menu, initializes the controllers for the timetable
 * selection and the filter functionality.
 */
public class MainSideMenuController extends Controller {

	@FXML
	private MainTimetablesController timetablesController;
	@FXML
	private MainFiltersController filtersController;
	@FXML
	private MainConstraintsController constraintsController;

	@FXML
	private VBox sideMenuVBox;
	@FXML
	private GridPane timetables;
	@FXML
	private Separator sideMenuVBoxSeparator;
	@FXML
	private VBox filters;
	@FXML
	private VBox constraints;
	@FXML
	private ToggleButton swapToggle;
	@FXML
	private ToggleButton timetablesToggle;
	@FXML
	private ToggleButton filtersToggle;
	
	private ListView<String> listView; // displays max. 2 assignments to be swapped.

	@FXML
	protected void initialize() {
		timetablesToggle.setOnAction(event -> {
			if (timetablesToggle.isSelected()) {
				sideMenuVBox.getChildren().add(0, timetables);
			} else {
				sideMenuVBox.getChildren().remove(timetables);
			}
			adjustSideMenuSeparators();
		});
		
		swapToggle.setOnAction(event -> {
			if (swapToggle.isSelected()) {
				sideMenuVBox.getChildren().add(constraints);
			} else {
				sideMenuVBox.getChildren().remove(constraints);
			}
			adjustSideMenuSeparators();
		});

		filtersToggle.setOnAction(event -> {
			if (filtersToggle.isSelected()) {
				sideMenuVBox.getChildren().add(filters);
			} else {
				sideMenuVBox.getChildren().remove(filters);
			}
			adjustSideMenuSeparators();
		});
	}

	@Override
	public void setup(Stage stage, HostServices hostServices,
	                  MainController mainController, Model model) {
		super.setup(stage, hostServices, mainController, model);
		timetablesController.setup(stage, hostServices, mainController, model);
		filtersController.setup(stage, hostServices, mainController, model);
		constraintsController.setup(stage, hostServices, mainController, model);
	}

	MainTimetablesController getTimetablesController() {
		return timetablesController;
	}

	MainFiltersController getFiltersController() {
		return filtersController;
	}

	private void adjustSideMenuSeparators() {
		if(!sideMenuVBox.getChildren().contains(sideMenuVBoxSeparator) &&
				sideMenuVBox.getChildren().contains(constraints) &&
				sideMenuVBox.getChildren().contains(timetables) &&
				sideMenuVBox.getChildren().contains(filters)) {
			sideMenuVBox.getChildren().add(1, sideMenuVBoxSeparator);
		} else {
			sideMenuVBox.getChildren().remove(sideMenuVBoxSeparator);
		}
	}

}
