package wcttt.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wcttt.gui.WctttGuiException;
import wcttt.lib.model.Curriculum;
import wcttt.lib.model.Period;
import wcttt.lib.model.Teacher;
import wcttt.lib.model.Timetable;
import wcttt.lib.model.WctttModelException;

public class ViewTeacherPeriodController extends Controller {

	private MainController mainController;
	
	@FXML
	private Button genButton;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private TextField xAxisName;
	
	@FXML
	private TextField yAxisName;
	
	@FXML
	private GridPane matrix;
	
	@FXML
	private ListView<Curriculum> conflictListView;
	
	@FXML
	protected void initialize() {
		
		Label label1 = new Label("A");
		Label label2 = new Label("B");
		Label label3 = new Label("C");
		matrix.add(label1, 0, 0, 2, 1);
		matrix.add(label2, 0, 1, 1, 1);
		genButton.setOnAction(event -> {
			generateMatrix();
		});
		
		//table = getMainController().getTimetableTableController().getSelectedTimetable();
		matrix.add(label3, 1, 1, 1, 1);
	}
	
	private void generateMatrix() {
		//Util.informationAlert("Model", "The Matrix for " + getModel().getName() + " was generated!");
		Util.informationAlert("Model2", "tt: "+getMainController().getTimetableTableController().getSelectedTimetable());
		
		ObservableList<Teacher> teachers = getModel().getTeachers();
		
		int x = 0;
		for(Teacher t : teachers) {
			Button btn = new Button(t.getName());
			matrix.add(btn, x, 0);
			
			int y = 0;
			for (Period p : getModel().getPeriods()) {
				Button btn2 = new Button("D: "+ p.getDay()+" Slot: "+p.getTimeSlot());
				matrix.add(btn2, 0, y);
				
				
				y++;
			}
			
			x++;
		}
		
		
	}
	
	private void addRows() {
		TextField parameterField = new TextField();
		parameterField.setText("no");
		parameterField.setMaxWidth(100);
		matrix.addRow(matrix.getRowCount(),
				new Label("okay"), parameterField);
	}
	
	public void setMainController(MainController table) {
		this.mainController = table;
	}
	
}
