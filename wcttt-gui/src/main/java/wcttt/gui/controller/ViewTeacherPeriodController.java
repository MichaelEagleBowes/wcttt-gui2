package wcttt.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import wcttt.lib.model.Curriculum;
import wcttt.lib.model.Timetable;

public class ViewTeacherPeriodController extends Controller {

	private MainController mainController;
	
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
		
		Timetable table;
		getModel().getSemester();
		//table = list.get(0);
		//table = getMainController().getTimetableTableController().getSelectedTimetable();
		matrix.add(label3, 1, 1, 1, 1);
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
