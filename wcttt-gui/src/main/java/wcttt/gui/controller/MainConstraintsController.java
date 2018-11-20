package wcttt.gui.controller;

import java.util.LinkedList;
import java.util.Queue;

import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import wcttt.gui.model.Model;
import wcttt.lib.model.TimetableAssignment;

public class MainConstraintsController extends SubscriberController<Boolean> {

	@FXML
	private VBox dropBox;
	
	@FXML
	private Button swapButton; // swaps 2 assignments
	
	@FXML
	private Button violationsButton; // shows the penalty change from constraint violations if switched
	
	//private ListView assignments;
	
	private ObservableList assgnmtList = null;
	
	private Text assgnmt1, assgnmt2;
	
	
	@Override
	public void setup(Stage stage, HostServices hostServices, MainController mainController, Model model) {
		super.setup(stage, hostServices, mainController, model);
		model.subscribeSemesterChanges(this);
		Platform.runLater(this::updateGui);
	}
	
	/**
	 * Adds an assignment's data for the purpose of swapping them with another assignment.
	 * In case the assignment list is full (max. 2), the assignment that was added first is swapped out.
	 */
	private void addAssignment(TimetableAssignment assign) {
		if(assgnmtList.size()==2) {
			assgnmtList.remove(0);
			assgnmtList.add(0, assign);
		} else {
			assgnmtList.add(assign);
		}
	}
	
	private void createSwapBox() {
		//assignments = new ListView();
		//assignments.getItems().addAll(assgnmt1, assgnmt2);
		

		
		/*
		violationsButton.setOnAction(event -> {
			
		});
		
		swapButton.setOnAction(event -> {
			
		});*/
		
	}

	@Override
	public void onNext(Boolean item) {
		updateGui();
		getSubscription().request(1);
	}

	private void updateGui() {
		Platform.runLater(() -> {
			createSwapBox();
		});
	}
}
