package wcttt.gui.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import wcttt.gui.model.Model;
import wcttt.lib.model.Room;
import wcttt.lib.model.TimetableAssignment;

public class MainConstraintsController extends Controller {

	@FXML
	private VBox dropBox;
	
	@FXML
	private Button swapButton; // swaps 2 assignments
	
	@FXML
	private Button violationsButton; // shows the penalty change from constraint violations if switched
	@FXML
	private VBox vbox = new VBox(); // displays max. 2 assignments to be swapped.
	private ObservableList<String> assgnmtList = FXCollections.observableArrayList();
	ListView<String> view;
	
	@FXML
	protected void initialize() {
		view = new ListView<String>();
		vbox.setPrefWidth(300);
		vbox.setPrefHeight(70);
		assgnmtList = FXCollections.observableArrayList ("Select session to swap");
		view.setItems(assgnmtList);
		vbox.getChildren().add(view);
		
		vbox.setOnDragDropped(new EventHandler<DragEvent>() {
		    @Override
			public void handle(DragEvent event) {
		        Dragboard db = event.getDragboard();
		        boolean success = false;
		        if (db.hasString()) {
		        	//add text to textfield
		           success = true;
		        }
		        event.setDropCompleted(success);
		        event.consume();
		     }
		});

        /*
        view.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<String>>()
        {
            @Override
            public ListCell<String> call(ListView<String> listView)
            {
                return new ListCell();
            }
        });*/
		
		swapButton.setOnAction(event -> {
			getMainController().getTimetableTableController().getSelectedData();
		});

		violationsButton.setOnAction(event -> {
			
		});

	}
	
	@Override
	public void setup(Stage stage, HostServices hostServices, MainController mainController, Model model) {
		super.setup(stage, hostServices, mainController, model);
		Platform.runLater(this::updateGui);
	}
	
	/**
	 * Adds an assignment's data for the purpose of swapping them with another assignment.
	 * In case the assignment list is full (max. 2), the assignment that was added first is swapped out.
	 * @param day - the day that the selected assignment is in.
	 * @param timeslot - the timeslot within the day, consisting of 2 hours
	 * @param room - the room that the course is held in
	 * @s - String containing the name of the course
	 */
	public void updateListView(int day, int timeslot, Room room, String s) {

		String dayString = "";
		switch(day) {
		case 0:
			dayString = "Mon";
			break;
		case 1:
			dayString = "Di";
			break;
		case 2:
			dayString = "Mi";
			break;
		case 3:
			dayString = "Do";
			break;
		case 4:
			dayString = "Fr";
			break;
		}
		String slotString = "";
		switch(timeslot) {
		case 0:
			slotString = "8:00";
			break;
		case 1:
			slotString = "10:00";
			break;
		case 2:
			slotString = "12:00";
			break;
		case 3:
			slotString = "14:00";
			break;
		case 4:
			slotString = "16:00";
			break;
		case 5:
			slotString = "18:00";
			break;
		}
		
		if(s == "") {
			s = "Free Slot";
		}
		
		String assgnmtTxt = dayString + ", " + slotString + ", " + s +", " + room.getName();
		
		System.out.println(room.getName());
		
		if(assgnmtList.size()==2) {
			assgnmtList.remove(0);
			assgnmtList.add(assgnmtTxt);
		} else {
			assgnmtList.add(assgnmtTxt);
		}
		
	}
	
	public void clearListView() {
		view.getItems().clear();
	}

	private void updateGui() {
		Platform.runLater(() -> {
			
		});
	}
}
