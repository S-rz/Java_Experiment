package application;

import java.awt.Label;

import javax.sound.sampled.LineUnavailableException;

import org.omg.CORBA.PUBLIC_MEMBER;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.Parent;

public class Main extends Application {
	public static Stage target_stage=null;
	public static Scene scene=null;
	public static Scene patient_scene=null;
	public static Scene doctor_scene=null;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			target_stage=primaryStage;	
			Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
			Parent patient_root = FXMLLoader.load(getClass().getResource("patient.fxml"));
			Parent doctor_root = FXMLLoader.load(getClass().getResource("doctor.fxml"));
			
			scene = new Scene(root,600,400);
			patient_scene= new Scene(patient_root,600,400);
			doctor_scene= new Scene(doctor_root,600,400);
			
			target_stage.setTitle("医院挂号系统");
			target_stage.setScene(scene);
			target_stage.setResizable(false);
			target_stage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
	public static void set_doctor() {
		target_stage.setScene(doctor_scene);
		target_stage.setResizable(false);
		target_stage.show();
	}
	public static void set_patient() {
		target_stage.setScene(patient_scene);
		target_stage.setResizable(false);
		target_stage.show();
	}
	public static void set_mainwindows() {
		target_stage.setScene(scene);
		target_stage.setResizable(false);
		target_stage.show();
	}
}
