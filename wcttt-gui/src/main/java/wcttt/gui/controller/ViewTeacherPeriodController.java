package wcttt.gui.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import wcttt.lib.model.Curriculum;
import wcttt.lib.model.Teacher;

public class ViewTeacherPeriodController extends Controller {

	private MainController mainController;

	@FXML
	private Pane matrixPane;
	
	@FXML
	private ScrollPane scroll;
	
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
		
		matrixPane = new Pane();
		scroll = new ScrollPane(matrix);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		rootPane.setCenter(scroll);
		Label label1 = new Label("A");
		matrix.add(label1, 0, 0, 2, 1);
		genButton.setOnAction(event -> {
			generateMatrix();
		});
	}
	
	private void generateMatrix() {
		//Util.informationAlert("Model", "The Matrix for " + getModel().getName() + " was generated!");
		Util.informationAlert("Model2", "tt: "+getMainController().getTimetableTableController().getSelectedTimetable());
		
		ObservableList<Teacher> teachers = getModel().getTeachers();
		
		int x = 0;
		for(Teacher t : teachers) {
			/*
			Line line = getGridLine(x, 0, x, matrixPane.getHeight());
			matrixPane.getChildren().add(line);
			*/
			
			double s = 1/getModel().getTeachers().size();
			Button btn = new Button(t.getName());
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(s);
			RowConstraints col2 = new RowConstraints();
			col2.setPercentHeight(s);
			matrix.getColumnConstraints().addAll(col1);
			matrix.getRowConstraints().addAll(col2);
			matrix.add(btn, x+1, 0);
			
			//getModel().getTeachers().getUnvaforablePeriods();
			
			int y = 0;
			int slots = getModel().getTimeSlotsPerDay();
			for (int i = 0; i<slots;i++) {
				/*
				Line line2 = getGridLine(0, y, matrixPane.getWidth(), y);
				matrixPane.getChildren().add(line2);
				*/
				Button btn2 = new Button("Slot "+i);
				matrix.add(btn2, 0, y+1);
				
				y++;
			}
			
			x++;
		}
		
	}
	
	/**
	 * Returns a line for the grid with the given start and end coordinates.
	 *
	 * @param x1
	 *            the x value of the start coordinate.
	 * @param y1
	 *            the y value of the start coordinate.
	 * @param x2
	 *            the x value of the end coordinate.
	 * @param y2
	 *            the y value of the end coordinate.
	 * @return the line.
	 */
	private Line getGridLine(double x1, double y1, double x2, double y2) {
		Line line = new Line(x1, y1, x2, y2);
		line.setStroke(Color.LIGHTGRAY);
		line.getStrokeDashArray().addAll(5d);
		return line;
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
