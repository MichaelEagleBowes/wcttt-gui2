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
		vbox.setPrefWidth(200);
		vbox.setPrefHeight(70);
		assgnmtList = FXCollections.observableArrayList ("None");
		view.setItems(assgnmtList);
		vbox.getChildren().add(view);
		
		vbox.setOnDragDropped(new EventHandler<DragEvent>() {
		    @Override
			public void handle(DragEvent event) {
		        /* data dropped */
		        /* if there is a string data on dragboard, read it and use it */
		        Dragboard db = event.getDragboard();
		        boolean success = false;
		        if (db.hasString()) {
		        	//add text to textfield
		           success = true;
		        }
		        /* let the source know whether the string was successfully 
		         * transferred and used */
		        event.setDropCompleted(success);
		        
		        event.consume();
		     }
		});

        /*
        listView.setCellFactory(new Callback<ListView<String>, javafx.scene.control.ListCell<String>>()
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
	 */
	public void updateListView(String s) {
		//view.setItems(assgnmtList);
		
		
		if(assgnmtList.size()==2) {
			assgnmtList.remove(0);
			assgnmtList.add(s);
		} else {
			assgnmtList.add(s);
		}
		
	}

	private void updateGui() {
		Platform.runLater(() -> {
			
		});
	}
}
