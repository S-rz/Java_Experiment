package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;

import java.awt.event.KeyEvent;
import java.sql.*;

import com.sun.accessibility.internal.resources.accessibility;

public class patientcontroller {
	@FXML
	private TextField label_yjje;
	@FXML
	private TextField label_jkje;
	@FXML
	private TextField label_zlje;
	@FXML
	private TextField label_ghhm;
	@FXML
	private Button button_yes;
	@FXML
	private Button button_clear;
	@FXML
	private Button button_exit;
	@FXML
	private ComboBox<String> box_ksmc;
	@FXML
	private ComboBox<String> box_hzlb;
	@FXML
	private ComboBox<String> box_hzmc;
	@FXML
	private ComboBox<String> box_ysxm;
	@FXML
	private TitledPane tips_pane;
	@FXML
	private Button tips_yes;
	@FXML
	private Label tips_label;
	
	// Event Listener on Button[#button_yes].onAction
	@FXML
	public void click_yes(ActionEvent event) {
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		String jkje_text=label_jkje.getText();
		if(is_clear(ksmc_text) || is_clear(ysxm_text) || is_clear(hzlb_text) || is_clear(hzmc_text))
		{
			label_jkje.setDisable(false);
			tips_label.setText("请先填写其他信息！");
			tips_pane.setVisible(true);
		}
		else 
		{
			if(Link_MySQL.check_yes(ksmc_text, ysxm_text, hzlb_text, hzmc_text))
			{
				if(Link_MySQL.moneyenough(ksmc_text, ysxm_text, hzlb_text, hzmc_text))
				{
					label_jkje.setText(String.valueOf(Link_MySQL.money_need(ksmc_text, ysxm_text, hzlb_text, hzmc_text)));		
					label_yjje.setText(String.valueOf(Link_MySQL.money_need(ksmc_text, ysxm_text, hzlb_text, hzmc_text)));
					label_zlje.setText("预存金额剩余："+String.valueOf(Link_MySQL.money_remain(ksmc_text, ysxm_text, hzlb_text, hzmc_text)));
					label_ghhm.setText(Link_MySQL.get_gh(ksmc_text, ysxm_text, hzlb_text, hzmc_text));
					tips_label.setText("挂号成功！");
					tips_pane.setVisible(true);
				}
				else 
				{
					if(is_clear(jkje_text))
					{
						tips_label.setText("请填写付款信息！");
						tips_pane.setVisible(true);
					}
					else 
					{
						double money_get=Double.valueOf(jkje_text),money_need=Link_MySQL.money_need(ksmc_text, ysxm_text, hzlb_text, hzmc_text);
						if(money_get<money_need)
						{
							tips_label.setText("交款金额不足！");
							tips_pane.setVisible(true);
						}
						else 
						{
							label_zlje.setText(String.valueOf(money_get-money_need));
							label_ghhm.setText(Link_MySQL.get_gh(ksmc_text, ysxm_text, hzlb_text, hzmc_text));
							tips_label.setText("挂号成功！");
							tips_pane.setVisible(true);
						}
					}
				}
			}
			else 
			{
				label_jkje.setDisable(false);
				tips_label.setText("请正确填写信息！");
				tips_pane.setVisible(true);
			}
		}
	}
	// Event Listener on Button[#button_clear].onAction
	@FXML
	public void click_clear(ActionEvent event) {
		box_hzlb.setItems(FXCollections.observableArrayList(""));
		box_hzlb.setValue(null);
		box_hzmc.setItems(FXCollections.observableArrayList(""));
		box_hzmc.setValue(null);
		box_ksmc.setItems(FXCollections.observableArrayList(""));
		box_ksmc.setValue(null);
		box_ysxm.setItems(FXCollections.observableArrayList(""));
		box_ysxm.setValue(null);
		label_ghhm.setText("");
		label_jkje.setText("");
		label_yjje.setText("");
		label_zlje.setText("");
		label_jkje.setDisable(false);
	}
	// Event Listener on Button[#button_exit].onAction
	@FXML
	public void click_exit(ActionEvent event) {
		System.exit(0);
	}
	
	@FXML
	public void click_combo_ksmc() {
		box_ksmc.setItems(FXCollections.observableArrayList(""));
		box_ksmc.setValue(null);
		String str="";
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		for(int i=1,insert=1;;i++)
		{
			str=Link_MySQL.get_information(i, "t_ksxx", "KSMC" ,null,ysxm_text,hzlb_text,hzmc_text);
			if(!str.equals("-1") && !str.equals("-2"))
			{
				if(insert==1)
				{
					box_ksmc.setItems(FXCollections.observableArrayList(str));
					insert++;
				}
				else
				{
					box_ksmc.getItems().add(str);
				}
			}
			else if(str.equals("-2"))
			{

			}
			else
			{
				break;
			}
		}
	}
	@FXML
	public void click_combo_ysxm() {
		box_ysxm.setItems(FXCollections.observableArrayList(""));
		box_ysxm.setValue(null);
		String str="";
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		for(int i=1,insert=1;;i++)
		{
			str=Link_MySQL.get_information(i, "t_ksys", "YSMC" ,ksmc_text,null,hzlb_text,hzmc_text);
			if(!str.equals("-1") && !str.equals("-2"))
			{
				if(insert==1)
				{
					insert++;
					box_ysxm.setItems(FXCollections.observableArrayList(str));
				}
				else
				{
					box_ysxm.getItems().add(str);
				}
			}
			else if(str.equals("-2"))
			{
				
			}
			else
			{
				break;
			}
		}
	}
	@FXML
	public void click_combo_hzlb() {
		box_hzlb.setItems(FXCollections.observableArrayList(""));
		box_hzlb.setValue(null);
		String str="";
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		
		str=Link_MySQL.get_hzxx(ksmc_text, ysxm_text, null, hzmc_text);
		if(str.equals("2"))
		{
			box_hzlb.setItems(FXCollections.observableArrayList("专家"));
			box_hzlb.getItems().add("普通");
		}
		else if(str.equals("1") || str.equals("3")) 
		{
			box_hzlb.setItems(FXCollections.observableArrayList("普通"));
		}
		else if(str.equals("4"))
		{
			box_hzlb.setItems(FXCollections.observableArrayList("专家"));
		}
		else if(str.equals("-1")) 
		{
			box_hzlb.setItems(FXCollections.observableArrayList(""));

		}
	}
	@FXML
	public void click_combo_hzmc() {
		box_hzmc.setItems(FXCollections.observableArrayList(""));
		box_hzmc.setValue(null);
		String str="";
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		for(int i=1,insert=1;;i++)
		{
			str=Link_MySQL.get_information(i, "t_hzxx", "HZMC" ,ksmc_text,ysxm_text,hzlb_text,null);
			if(!str.equals("-1") && !str.equals("-2"))
			{
				if(insert==1)
				{
					insert++;
					box_hzmc.setItems(FXCollections.observableArrayList(str));
				}
				else
				{
					box_hzmc.getItems().add(str);
				}
			}
			else if(str.equals("-2"))
			{

			}
			else
			{
				break;
			}
		}
	}
	
	@FXML
	public void action_label_jkje() {
		String ksmc_text=box_ksmc.getValue();
		String ysxm_text=box_ysxm.getValue();
		String hzlb_text=box_hzlb.getValue();
		String hzmc_text=box_hzmc.getValue();
		if(is_clear(ksmc_text) || is_clear(ysxm_text) || is_clear(hzlb_text) || is_clear(hzmc_text))
		{
			tips_label.setText("请先填写其他信息！");
			tips_pane.setVisible(true);
		}
		else 
		{
			if(Link_MySQL.moneyenough(ksmc_text, ysxm_text, hzlb_text, hzmc_text))
			{
				tips_label.setText("预存金额足够，无需缴费!");
				tips_pane.setVisible(true);
				label_jkje.setDisable(true);
			}
			else 
			{
				label_jkje.setDisable(false);
			}
			label_yjje.setText(String.valueOf(Link_MySQL.money_need(ksmc_text, ysxm_text, hzlb_text, hzmc_text)));
		}
	}	
	
	@FXML
	public void key_ksmc() {
		String str;
		box_ksmc.setItems(FXCollections.observableArrayList(""));
		box_ksmc.setValue(null);
		for(int i=1,insert=1;;i++)
		{
			String ksmc_text=box_ksmc.getEditor().getText();
			str=Link_MySQL.key_input(i, "t_ksxx", "KSMC", ksmc_text);
			System.out.println("STR"+str);
			if(!str.equals("-1") && !str.equals("-2"))
			{
				if(insert==1)
				{
					insert++;
					box_hzmc.setItems(FXCollections.observableArrayList(str));
				}
				else
				{
					box_hzmc.getItems().add(str);
				}
			}
			else if(str.equals("-2"))
			{

			}
			else
			{
				break;
			}
		}
		box_ksmc.show();
	}
	
	@FXML
	public void key_ysxm() {
		
	}
	
	@FXML
	public void key_hzlb() {
		
	}
	
	@FXML
	public void key_hzmc() {
		
	}
	
	
	@FXML
	public void click_tips_yes(ActionEvent event) {
		tips_pane.setVisible(false);
	}
	
	private static boolean is_clear(String str) {
    	if(str==null || str.equals(""))
    	{
    		return true;
    	}
    	return false;
    }
}
