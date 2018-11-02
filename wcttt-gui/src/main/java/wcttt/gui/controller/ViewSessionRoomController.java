package wcttt.gui.controller;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import wcttt.lib.model.Course;
import wcttt.lib.model.InternalRoom;
import wcttt.lib.model.InternalSession;
import wcttt.lib.model.Session;
import wcttt.lib.util.SessionRoomConflict;

/**
 * 
 * @author Michael Bowes
 *
 */

public class ViewSessionRoomController extends Controller {
	
	
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
	protected void initialize() {
		
		scroll = new ScrollPane(matrix);
		scroll.setVbarPolicy(ScrollBarPolicy.ALWAYS);
		scroll.setHbarPolicy(ScrollBarPolicy.ALWAYS);
		rootPane.setCenter(scroll);
		
		genButton.setOnAction(event -> {
			generateMatrix();
		});
	}
	
	private List<InternalSession> getSessions(){
		
		List<InternalSession> sessions = new ArrayList<>();
		
		for (Course course : getModel().getSemester().getCourses()) {
			
			for (Session lecture : course.getLectures()) {
				if(lecture instanceof InternalSession) {
					sessions.add((InternalSession)lecture);
				}
			}
			for (Session practical : course.getPracticals()) {
				if(practical instanceof InternalSession) {
					sessions.add((InternalSession)practical);
				}
			}
		}

		return sessions;
	}
	
	private void generateMatrix() {
		/*
		ConflictMatrixCalculator calculator = new ConflictMatrixCalculator(getModel().getSemester());
		Map<Session, Map<Session, SessionSessionConflict>> conflicts = calculator.calcSessionSessionConflicts();
		*/
		List<InternalSession> sessions = getSessions();
		
		int x = 0;
		for(InternalRoom room : getModel().getInternalRooms()) {
			
			double number = 1/getModel().getTeachers().size();
			Button sessionColBtn = new Button(room.getName());
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(number);
			RowConstraints col2 = new RowConstraints();
			col2.setPercentHeight(number);
			matrix.getColumnConstraints().addAll(col1);
			matrix.getRowConstraints().addAll(col2);
			matrix.add(sessionColBtn, x+1, 0);
			
			int y = 0;
			for(InternalSession session : sessions) {
				
					Button sessionRowBtn = new Button(session.getName());
					matrix.add(sessionRowBtn, 0, y+1);
					
					int conflictType = 0;
					SessionRoomConflict conflict = new SessionRoomConflict();
					conflict.setCapacityDeviation(room.getCapacity() - session.getStudents());
					if (session.getRoomRequirements().compareTo(room.getFeatures()) <= 0) {
						conflict.setFullfillsFeatures(true);
					}
					
					Circle circle = drawCircle(conflictType);
					Tooltip.install(circle, createConflictTooltip(room, session, conflict));
					matrix.add(circle, x+1, y+1);
					GridPane.setHalignment(circle, HPos.CENTER);
					y++;
			}
			x++;
		}
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

		return circle;
	}
	
	/**
	 * Creates tooltip for hover over the conflict circles.
	 * @param conflictType
	 * @return
	 */
	private Tooltip createConflictTooltip(InternalRoom room, Session session, SessionRoomConflict conflict) {
		Tooltip tip = new Tooltip();
		
		//Text that tells whether capacity is not enough or too much
		String deviationText;
		int deviation = conflict.getCapacityDeviation();
		if(deviation<0) {
			deviationText = "Capacity: "+deviation+" slots missing";
		} else {
			deviationText = "Capacity: "+deviation+" slots unused";
		}
		
		String str = room.getName() + " - " + session.getName()+"\n"
		+"Same lecturers: "+conflict.fullfillsFeatures()+"\n"
		+deviationText;
		tip.setText(str);
		tip.prefWidth(100);
		tip.setWrapText(true);
		tip.setHideOnEscape(true);
		tip.setStyle("-fx-background-color: linear-gradient(#E4EAA2, #9CD672);");
		
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
