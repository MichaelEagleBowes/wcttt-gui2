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
	private ListView<String> listView; // displays max. 2 assignments to be swapped.
	private Set<String> stringSet;
	private ObservableList<String> assgnmtList = FXCollections.observableArrayList();
	
	private Text assgnmt1, assgnmt2;
	
	@FXML
	protected void initialize() {
		listView = new ListView<String>();
		ObservableList<String> items = listView.getItems();
        items.add("One");
        items.add("Two");
        //assgnmtList.setAll(stringSet);
        //listView.setItems(assgnmtList);
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
	private void addAssignment(TimetableAssignment assign) {
		/*
		if(assgnmtList.size()==2) {
			assgnmtList.remove(0);
			assgnmtList.add(0, assign);
		} else {
			assgnmtList.add(assign);
		}
		*/
	}
	
	private void createSwapBox() {
		
	}

	private void updateGui() {
		Platform.runLater(() -> {
			createSwapBox();
		});
	}
}
