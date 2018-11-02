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
import wcttt.lib.model.Curriculum;
import wcttt.lib.model.Session;
import wcttt.lib.util.SessionSessionConflict;

/**
 * 
 * @author Michael Bowes
 *
 */

public class ViewSessionSessionController extends Controller {
	
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
	
	private List<Session> getSessions(){
		List<Session> sessions = new ArrayList<>();
		for (Course course : getModel().getSemester().getCourses()) {
			for (Session lecture : course.getLectures()) {
				sessions.add(lecture);
			}
			for (Session practical : course.getPracticals()) {
				sessions.add(practical);
			}
		}

		return sessions;
	}
	
	private void generateMatrix() {
		/*
		ConflictMatrixCalculator calculator = new ConflictMatrixCalculator(getModel().getSemester());
		Map<Session, Map<Session, SessionSessionConflict>> conflicts = calculator.calcSessionSessionConflicts();
		*/
		List<Session> sessions = getSessions();
		
		int x = 0;
		for(Session session : sessions) {
			
			double number = 1/getModel().getTeachers().size();
			Button sessionColBtn = new Button(session.getName());
			ColumnConstraints col1 = new ColumnConstraints();
			col1.setPercentWidth(number);
			RowConstraints col2 = new RowConstraints();
			col2.setPercentHeight(number);
			matrix.getColumnConstraints().addAll(col1);
			matrix.getRowConstraints().addAll(col2);
			matrix.add(sessionColBtn, x+1, 0);
			
			int y = 0;
			for(Session otherSession : sessions) {
				
					Button sessionRowBtn = new Button(otherSession.getName());
					matrix.add(sessionRowBtn, 0, y+1);
					
					int conflictType = 0;
					SessionSessionConflict conflict = new SessionSessionConflict();
					
					if(session.equals(otherSession)) {
					conflictType = 0;
					} else {
						// Check for conflicts based on 2 courses belonging to the same curriculum
						for (Curriculum curriculum : getModel().getSemester().getCurricula()) {
							if (curriculum.getCourses().contains(session.getCourse()) &&
									curriculum.getCourses().contains(otherSession.getCourse())) {
								if (session.isLecture() || otherSession.isLecture() ||
										session.getCourse().getPracticals().size() == 1 ||
										otherSession.getCourse().getPracticals().size() == 1) {
									conflict.getCurricula().add(curriculum);
									conflictType = 1;
								}
							}
						}
						
						// Check for conflicts based on the same course:
						if(session.getCourse().equals(otherSession.getCourse())) {
							if (session.isLecture() || otherSession.isLecture()) {
								conflict.setSessionConflict(true);
								conflictType = 2;
							}
						}
						
						// Check for conflicts based on the same teacher:
						if(session.getTeacher().equals(otherSession.getTeacher())) {
							conflict.setTeacherConflict(true);
							conflictType = 2;
						}
					}
					
					Circle circle = drawCircle(conflictType);
					Tooltip.install(circle, createConflictTooltip(session, otherSession, conflict));
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
	private Tooltip createConflictTooltip(Session session, Session otherSession, SessionSessionConflict conflict) {
		Tooltip tip = new Tooltip();
		String curricula = "";
		for (Curriculum c : conflict.getCurricula()) {
			curricula = curricula+c.getName()+"\n";
		}
		
		String str = session.getName() + " - " + otherSession.getName()
		+"\n"+"Same lecturers: "+conflict.isTeacherConflict()+"\n"
		+"Same Course: "+conflict.isSessionConflict()+"\n"
		+"Curricula: "+"\n"+curricula;
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
