/*
 * WCT³ (WIAI Course Timetabling Tool) is a software that strives to automate
 * the timetabling process at the WIAI faculty of the University of Bamberg.
 *
 * WCT³-GUI comprises functionality to view generated timetables, edit semester
 * data and to generate new timetables.
 *
 * Copyright (C) 2018 Nicolas Gross
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package wcttt.gui.controller;

import javafx.application.HostServices;
import wcttt.gui.WctttGuiFatalException;
import wcttt.gui.model.Model;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import wcttt.lib.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Controller for the tables that contain the selected timetable.
 */
public class MainTableController extends SubscriberController<Boolean> {
	
	@FXML
	private VBox timetableDaysVBox;

	private List<TableView<TimetablePeriod>> timetableDays = new ArrayList<>();
	private Timetable selectedTimetable = null;
	private ObservableList<TablePosition> selectedCells = null;
	private TableView selectedTableView = null;
	private String selectedCellData = null;
	private TablePosition selectedCell = null;
	private Teacher teacherFilter = null;
	private Chair chairFilter = null;
	private Course courseFilter = null;
	private Curriculum curriculumFilter = null;
	
	private ObservableList<String> selectedData = FXCollections.observableArrayList();

	@Override
	public void setup(Stage stage, HostServices hostServices, MainController mainController, Model model) {
		super.setup(stage, hostServices, mainController, model);
		model.subscribeSemesterChanges(this);
		Platform.runLater(this::updateGui);
	}

	@Override
	public void onNext(Boolean item) {
		Platform.runLater(this::updateGui);
		getSubscription().request(1);
	}

	public Timetable getSelectedTimetable() {
		return selectedTimetable;
	}

	private void updateGui() {
		System.out.println("update GUI");
		createTableViews();
		timetableDaysVBox.getChildren().setAll(timetableDays);
		createPeriodColumns();
		createRoomColumns(getModel().getInternalRooms());
		createRoomColumns(getModel().getExternalRooms());
		if (selectedTimetable != null) {
			System.out.println("create tables");
			setTimetable(selectedTimetable);
		}
	}
	
	public ObservableList<String> getSelectedData(){
		return selectedData;
	}

	private void createTableViews() {
		timetableDays.clear();
		for (int i = 0; i < getModel().getDaysPerWeek(); i++) {
			TableView<TimetablePeriod> tableView = new TableView<>();
			tableView.setPrefWidth(Region.USE_COMPUTED_SIZE);
			tableView.setPrefHeight(196);
			tableView.setEditable(false);
			tableView.setPlaceholder(new Label("No timetable selected"));
			tableView.getSelectionModel().setCellSelectionEnabled(true);
			tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

			TableColumn<TimetablePeriod, String> tableColumn = new TableColumn<>();
			tableColumn.setResizable(false);
			tableColumn.setSortable(false);
			tableColumn.setPrefWidth(100.0);
			tableView.getColumns().add(tableColumn);

			tableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				
				void sendDataToConstraintController(){
					if(selectedData != null) {
						int row = selectedCell.getRow();
						int col = selectedCell.getColumn();
						System.out.println("selected: "+selectedCellData + "col: " +col);
						
						int day = 0;
						for(int k = 0; k<timetableDays.size();k++) {
							if(timetableDays.get(k).equals(selectedTableView)) {
								day = k;
							};
						}
						System.out.println("Day"+day);
						
						//get selected period
						String s = Period.TIME_SLOT_NAMES[row];
						System.out.println(s);
						
						//get selected room
						ObservableList<? extends Room> inRooms = getModel().getInternalRooms();
						ObservableList<? extends Room> exRooms = getModel().getExternalRooms();
						Room selectedRoom;
						if(col > inRooms.size()) {
							col = col-inRooms.size();
							selectedRoom = exRooms.get(col-1);
						} else {
							if(col == 0) {
								selectedRoom = inRooms.get(col);
							} else {
								selectedRoom = inRooms.get(col-1);
							}
						}
						
						System.out.println(selectedCell.getRow());
						getMainController().getSideMenuController().getConstraintsController().updateListView(day, row, selectedRoom, selectedCellData);
					}
				}
				
				@Override
				public void handle(MouseEvent mouseEvent) {
					selectedTableView = tableView;
					selectedCells = tableView.getSelectionModel().getSelectedCells();
					if(selectedCells != null) {
							if (selectedCells.size() > 0) {
								selectedCell = selectedCells.get(0);
								TableColumn column = selectedCell.getTableColumn();
								int rowIndex = selectedCell.getRow();
								selectedCellData = (String) column.getCellObservableValue(rowIndex).getValue();
								if(selectedData.size()>0) {
									selectedData.remove(0);
								}
								selectedData.add(0, selectedCellData);
								sendDataToConstraintController();
							}
					}
					mouseEvent.consume();
				}
			});
			
			// Handles the individual Cells in the tableViews
			tableView.setOnMouseEntered(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent mouseEvent) {
					selectedCells = tableView.getSelectionModel().getSelectedCells();
					selectedCells.addListener((ListChangeListener.Change<? extends TablePosition> change) -> {
						if (selectedCells.size() > 0) {
							selectedCell = selectedCells.get(0);
							TableColumn column = selectedCell.getTableColumn();
							int rowIndex = selectedCell.getRow();
							selectedCellData = (String) column.getCellObservableValue(rowIndex).getValue();
						}
					});
					mouseEvent.consume();
				}
			});

			tableView.setOnDragDetected(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					Dragboard db = tableView.startDragAndDrop(TransferMode.ANY);

					ClipboardContent content = new ClipboardContent();
					// Object cellData = tableView.getSelectionModel().getSelectedItem();
					// String dragTxt;

					// dragTxt = ((TimetablePeriod)cellData).getAssignments().
					// get(getSelectedCellIndex((TimetablePeriod)cellData)).getSession().toString();
					content.putString(selectedCellData);
					if (selectedCellData == "") {
						System.err.println("The selected cell contains no data.");
					} else {
						System.out.println("You are dragging " + selectedCellData);
					}

					db.setContent(content);

					event.consume();
				}
			});

			timetableDays.add(tableView);
		}
	}

	private void createPeriodColumns() {
		for (int i = 0; i < getModel().getDaysPerWeek(); i++) {
			TableView<TimetablePeriod> tableView = timetableDays.get(i);
			@SuppressWarnings("unchecked")
			TableColumn<TimetablePeriod, String> periodColumn = (TableColumn<TimetablePeriod, String>) tableView
					.getColumns().get(0);
			periodColumn.setText(Period.WEEK_DAY_NAMES[i]);
			periodColumn.setCellValueFactory(
					param -> new SimpleStringProperty(Period.TIME_SLOT_NAMES[param.getValue().getTimeSlot() - 1]));
			periodColumn.setReorderable(false);
		}
	}

	private void createRoomColumns(List<? extends Room> rooms) {
		for (Room room : rooms) {
			for (TableView<TimetablePeriod> tableView : timetableDays) {
				TableColumn<TimetablePeriod, String> tableColumn = new TableColumn<>();
				tableColumn.setText(room.getName());
				tableColumn.setId(room.getId());
				tableColumn.setResizable(false);
				tableColumn.setSortable(false);
				tableColumn.setReorderable(false);
				tableColumn.setPrefWidth(150.0);
				
				tableColumn.setCellValueFactory(new PropertyValueFactory<>("default"));

				tableColumn.setCellValueFactory(param -> {
					for (TimetableAssignment assignment : param.getValue().getAssignments()) {
						if (assignment.getRoom().getId().equals(param.getTableColumn().getId())) {
							return new SimpleStringProperty(assignment.getSession().toString());
						}
					}
					return new SimpleStringProperty("");
				});
				
				tableView.getColumns().add(tableColumn);
			}
		}
	}

	private boolean filtersActive() {
		return teacherFilter != null || chairFilter != null || courseFilter != null || curriculumFilter != null;
	}

	private void updateSelectedFilters(Teacher teacher, Chair chair, Course course, Curriculum curriculum) {
		teacherFilter = teacher;
		chairFilter = chair;
		courseFilter = course;
		curriculumFilter = curriculum;
	}

	void setTimetable(Timetable timetable) {
		selectedTimetable = timetable;
		if (filtersActive() && selectedTimetable != null) {
			filter(teacherFilter, chairFilter, courseFilter, curriculumFilter);
		} else {
			setTableData(timetable);
		}
		getMainController().getSideMenuController().getConstraintsController().clearListView();
	}

	private void setTableData(Timetable timetable) {
		Platform.runLater(() -> {
			if (timetable == null) {
				for (TableView<TimetablePeriod> tableView : timetableDays) {
					tableView.setItems(FXCollections.observableArrayList());
				}
			} else {
				for (int i = 0; i < getModel().getDaysPerWeek(); i++) {
					timetableDays.get(i).setItems(timetable.getDays().get(i).getPeriods());
				}
			}
		});
	}

	void filter(Teacher teacher, Chair chair, Course course, Curriculum curriculum) {
		updateSelectedFilters(teacher, chair, course, curriculum);
		if (selectedTimetable != null) {
			if (!filtersActive()) {
				setTableData(selectedTimetable);
			} else {
				setTableData(createFilteredTimetable());
			}
		}
	}

	private Timetable createFilteredTimetable() {
		Timetable filteredTimetable = new Timetable("filteredTimetable");
		for (TimetableDay originalDay : selectedTimetable.getDays()) {
			try {
				TimetableDay filteredDay = new TimetableDay(originalDay.getDay());
				for (TimetablePeriod originalPeriod : originalDay.getPeriods()) {
					TimetablePeriod filteredPeriod = new TimetablePeriod(originalPeriod.getDay(),
							originalPeriod.getTimeSlot());
					addFilteredAssignments(originalPeriod, filteredPeriod);
					filteredDay.addPeriod(filteredPeriod);
				}
				filteredTimetable.addDay(filteredDay);
			} catch (WctttModelException e) {
				throw new WctttGuiFatalException("Implementation error in day/time slot numbering", e);
			}
		}
		return filteredTimetable;
	}

	private void addFilteredAssignments(TimetablePeriod original, TimetablePeriod filtered) {
		Predicate<TimetableAssignment> teacherCheck = t -> {
			if (teacherFilter != null) {
				return t.getSession().getTeacher().equals(teacherFilter);
			} else {
				return true;
			}
		};
		Predicate<TimetableAssignment> chairCheck = t -> {
			if (chairFilter != null) {
				return t.getSession().getCourse().getChair().equals(chairFilter);
			} else {
				return true;
			}
		};
		Predicate<TimetableAssignment> courseCheck = t -> {
			if (courseFilter != null) {
				return t.getSession().getCourse().equals(courseFilter);
			} else {
				return true;
			}
		};
		Predicate<TimetableAssignment> curriculumCheck = t -> {
			if (curriculumFilter != null) {
				for (Course course : curriculumFilter.getCourses()) {
					if (t.getSession().getCourse().equals(course)) {
						return true;
					}
				}
				return false;
			} else {
				return true;
			}
		};
		Predicate<TimetableAssignment> combinedFilter = teacherCheck
				.and(chairCheck.and(courseCheck.and(curriculumCheck)));

		filtered.getAssignments()
				.setAll(original.getAssignments().stream().filter(combinedFilter).collect(Collectors.toList()));
	}
}
