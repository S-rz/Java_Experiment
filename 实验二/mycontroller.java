package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.PasswordField;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.sql.*;

import com.sun.accessibility.internal.resources.accessibility;

public class mycontroller {
	@FXML
	private Button button_yes;
	@FXML
	private Button button_exit;
	@FXML
	private CheckBox checkbutton_ys;
	@FXML
	private CheckBox checkbutton_br;
	@FXML
	private TitledPane tips_pane;
	@FXML
	private Button tips_yes;
	@FXML
	private Label tips_label;
	@FXML
	private TextField usename_text;
	@FXML
	private PasswordField password_text;
	
	public boolean ys_flag=false,br_flag=false,login_suscess=false;
	
	// Event Listener on Button[#button_yes].onAction
	@FXML
	public void click_buttonyes() {
		if(ys_flag&&br_flag)
		{
			tips_label.setText("请单选医生或病人！");
			tips_pane.setVisible(true);
		}
		else if(ys_flag)
		{
			String usename,password;
			usename=usename_text.getText();
			password=password_text.getText();
			login_suscess=Link_MySQL.login_check(true, usename, password);

			if(login_suscess)
			{
				Main.set_doctor();
			}
			else
			{
				tips_label.setText("用户名或密码输入错误！");
				tips_pane.setVisible(true);
			}
		}
		else if(br_flag)
		{
			String usename,password;
			usename=usename_text.getText();
			password=password_text.getText();
			login_suscess=Link_MySQL.login_check(false, usename, password);
			if(login_suscess)
			{
				Main.set_patient();
			}
			else
			{
				tips_label.setText("用户名或密码输入错误！");
				tips_pane.setVisible(true);
			}
		}
		else
		{
			tips_label.setText("请单选医生或病人！");
			tips_pane.setVisible(true);
		}
	}
	// Event Listener on Button[#button_exit].onAction
	@FXML
	public void click_buttonexit(ActionEvent event) {
		System.exit(0);
	}
	// Event Listener on CheckBox[#checkbutton_ys].onAction
	@FXML
	public void isys(ActionEvent event) {
		ys_flag=!ys_flag;
	}
	// Event Listener on CheckBox[#checkbutton_br].onAction
	@FXML
	public void isbr(ActionEvent event) {
		br_flag=!br_flag;
	}
	@FXML
	public void click_tips_yes(ActionEvent event) {
		tips_pane.setVisible(false);
	}
}
