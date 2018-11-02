package wcttt.gui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import wcttt.lib.model.Curriculum;
import wcttt.lib.model.Period;
import wcttt.lib.model.Teacher;
import wcttt.lib.util.ConflictMatrixCalculator;

/**
 * 
 * @author Michael Bowes
 *
 */

public class ViewTeacherPeriodController extends Controller {
	
	@FXML
	private ScrollPane scroll;
	
	@FXML
	private GridPane matrix;
	
	@FXML
	private Button genButton;
	
	@FXML
	private BorderPane rootPane;
	
	@FXML
	private TextField xAxisName;
	
	@FXML
	private TextField yAxisName;
	
	@FXML
	private ListView<Curriculum> conflictListView;
	
	Map <Integer, String>days = new <Integer, String>HashMap();
	Map <Integer, String>timeslots = new <Integer, String>HashMap();
	
	@FXML
	protected void initialize() {
		
		scroll = new ScrollPane(matrix);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		rootPane.setCenter(scroll);
		genButton.setOnAction(event -> {
			generateMatrix();
		});
		
		days.put(1, "Mo");
		days.put(2, "Di");
		days.put(3, "Mi");
		days.put(4, "Do");
		days.put(5, "Fr");
		timeslots.put(1, "8:15 - 9:45");
		timeslots.put(2, "10:15 - 11:45");
		timeslots.put(3, "12:15 - 13:45");
		timeslots.put(4, "14:15 - 15:45");
		timeslots.put(5, "16:15 - 17:45");
		timeslots.put(6, "18:15 - 19:45");
	}
	
	private void generateMatrix() {
		Util.informationAlert("Model2", "tt: "+getMainController().getTimetableTableController().getSelectedTimetable());
		
		ConflictMatrixCalculator calculator = new ConflictMatrixCalculator(getModel().getSemester());
		ObservableList<Teacher> teachers = getModel().getTeachers();
		
		
		int x = 0;
		for(Teacher t : teachers) {
			/*
			Line line = getGridLine(x, 0, x, matrixPane.getHeight());
			matrixPane.getChildren().add(line);
			*/
			
			double s = 1/getModel().getTeachers().size();
			Button teacherBtn = new Button(t.getName());
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(s);
			RowConstraints col2 = new RowConstraints();
			col2.setPercentHeight(s);
			matrix.getColumnConstraints().addAll(col1);
			matrix.getRowConstraints().addAll(col2);
			matrix.add(teacherBtn, x+1, 0);
			
			//getModel().getTeachers().getUnvaforablePeriods();
			
			int y = 0;
			for(Period p : getModel().getPeriods()) {
				Button periodBtn = new Button(days.get(p.getDay())+timeslots.get(p.getTimeSlot()));
				matrix.add(periodBtn, 0, y+1);
				/*
				Line line2 = getGridLine(0, y, matrixPane.getWidth(), y);
				matrixPane.getChildren().add(line2);
				*/
				int conflictType;
				if(t.getUnavailablePeriods().contains(p)) {
					conflictType = 2;
				} else if (t.getUnfavorablePeriods().contains(p)) {
					conflictType = 1;
				} else {
					conflictType = 0;
				}
				Circle circle = drawCircle(conflictType);
				matrix.add(circle, x+1, y+1);
				GridPane.setHalignment(circle, HPos.CENTER);
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
	
	/**
	 * Draws a circle with attached tooltip for the specific conflict.
	 * @param conflictType
	 * @return
	 */
	private Circle drawCircle(int conflictType) {
		Circle circle = new Circle();
		circle.setFill(getConflictColor(conflictType));
		circle.setRadius(25);
		circle.setCenterX(0);
		circle.setCenterY(0);
		circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// event?
			}
		});

		Tooltip.install(circle, createConflictTooltip(conflictType));

		return circle;
	}
	
	private Tooltip createConflictTooltip(int conflictType) {
		Tooltip tip = null;
		switch(conflictType) {
		case 0:
			tip = new Tooltip("No Conflict");
			break;
		case 1:
			tip = new Tooltip("Unfavorable");
			break;
		case 2:
			tip = new Tooltip("Unavailable");
			break;
		}
		
		return tip;
	}
	
	/**
	 * 
	 * Chooses either white, yellow or red as Paint color depending on whether the conflict is
	 * non-existant, soft or hard constraint.
	 * @param conflict
	 * @return
	 */
	private Paint getConflictColor(int conflictType) {
		Color color = null;
		switch(conflictType) {
		case 0:
			color = Color.WHITE;
			break;
		case 1:
			color = Color.YELLOW;
			break;
		case 2:
			color = Color.RED;
			break;
		}
		return color;
	}
	
}
