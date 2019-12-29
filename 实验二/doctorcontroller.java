package application;

import javafx.fxml.FXML;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.mysql.cj.conf.StringProperty;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.event.Event;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class doctorcontroller {
	@FXML
	private Button button_exit;
	@FXML
	private Tab table_brlb;
	@FXML
	private TableView<table_brlb> brlb_tableview;
	@FXML
	private TableColumn<table_brlb,String> brlb_ghbh;
	@FXML
	private TableColumn<table_brlb,String> brlb_brmc;
	@FXML
	private TableColumn<table_brlb,String> brlb_ghrq;
	@FXML
	private TableColumn<table_brlb,String> brlb_hzlb;
	@FXML
	private Tab table_srlb;
	@FXML
	private TableView<table_srlb> srlb_tableview;
	@FXML
	private TableColumn<table_srlb,String> srlb_ksmc;
	@FXML
	private TableColumn<table_srlb,String> srlb_ysbh;
	@FXML
	private TableColumn<table_srlb,String> srlb_ysmc;
	@FXML
	private TableColumn<table_srlb,String> srlb_hzlb;
	@FXML
	private TableColumn<table_srlb,String> srlb_ghrc;
	@FXML
	private TableColumn<table_srlb,String> srlb_srhj;
	@FXML
	private Label label_zzsj;
	@FXML
	private Label label_qssj;
	@FXML
	private Label text_zzsj;
	@FXML
	private Label text_qssj;
	@FXML
	private Label tips_label;
	@FXML
	private Button tips_yes;
	
	// Event Listener on Button[#button_exit].onMouseClicked
	@FXML
	public void click_exit(MouseEvent event) {
		System.exit(0);
	}
	// Event Listener on Tab[#table_brlb].onSelectionChanged
	@FXML
	public void click_brlb(Event event) {
		String ghbhString=null,brmcString=null,ghrqString=null,hzlbString=null;
		final ObservableList<table_brlb> data = FXCollections.observableArrayList();
		for(int i=1;;i++)
		{
			ghbhString=Link_MySQL.get_doctor_brlb(i,"GHBH");
			brmcString=Link_MySQL.get_doctor_brlb(i,"BRBH");
			ghrqString=Link_MySQL.get_doctor_brlb(i,"RQSJ");
			hzlbString=Link_MySQL.get_doctor_brlb(i,"SFZJ");
			if(!ghbhString.equals("-1"))
			{
				data.add(new table_brlb(ghbhString, brmcString, ghrqString, hzlbString));
			}
			else
			{
				break;
			}
		}
		if(label_qssj!=null)
		{
			label_qssj.setVisible(false);
		}
		if(label_zzsj!=null)
		{
			label_zzsj.setVisible(false);
		}
		if(text_qssj!=null)
		{
			text_qssj.setVisible(false);
		}
		if(text_zzsj!=null)
		{
			text_zzsj.setVisible(false);
		}
		brlb_ghbh.setCellValueFactory(new PropertyValueFactory("ghbh"));
		brlb_brmc.setCellValueFactory(new PropertyValueFactory("brmc"));
		brlb_ghrq.setCellValueFactory(new PropertyValueFactory("ghrq"));
		brlb_hzlb.setCellValueFactory(new PropertyValueFactory("hzlb"));
		brlb_tableview.setItems(data);
	}
	// Event Listener on Tab[#table_srlb].onSelectionChanged
	@FXML
	public void click_srlb(Event event) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String target_date=df.format(new Date());
        label_qssj.setText(target_date+" 00:00:00");
        label_zzsj.setText(target_date+" 23:59:59");
		label_qssj.setVisible(true);
		label_zzsj.setVisible(true);
		text_qssj.setVisible(true);
		text_zzsj.setVisible(true);
		String ksmcString=null,ysbhString=null,ysmcString=null,hzlbString=null,ghrcString=null,srhjString=null;
		final ObservableList<table_srlb> data = FXCollections.observableArrayList();
		for(int i=1;;i++)
		{
			ysmcString=Link_MySQL.get_doctor_srlb(i, "YSMC");
			ysbhString=Link_MySQL.get_doctor_srlb(i, "YSBH");
			ksmcString=Link_MySQL.get_ksmc(ysmcString);
			if(!ysmcString.equals("-1"))
			{
				if(Link_MySQL.is_zj(ysmcString))
				{
					hzlbString="0";
					ghrcString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "GHRC", ysbhString));
					srhjString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "SRHJ", ysbhString));
					data.add(new table_srlb(ksmcString, ysbhString, ysmcString, hzlbString, ghrcString, srhjString));
					hzlbString="1";
					ghrcString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "GHRC", ysbhString));
					srhjString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "SRHJ", ysbhString));
					data.add(new table_srlb(ksmcString, ysbhString, ysmcString, hzlbString, ghrcString, srhjString));
				}
				else 
				{
					hzlbString="0";
					ghrcString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "GHRC", ysbhString));
					srhjString=String.valueOf(Link_MySQL.get_ghrc_srhj(hzlbString, "SRHJ", ysbhString));
					data.add(new table_srlb(ksmcString, ysbhString, ysmcString, hzlbString, ghrcString, srhjString));
				}
			}
			else
			{
				break;
			}
		}
		srlb_srhj.setCellValueFactory(new PropertyValueFactory("srhj"));
		srlb_ghrc.setCellValueFactory(new PropertyValueFactory("ghrc"));
		srlb_hzlb.setCellValueFactory(new PropertyValueFactory("hzlb"));
		srlb_ksmc.setCellValueFactory(new PropertyValueFactory("ksmc"));
		srlb_ysbh.setCellValueFactory(new PropertyValueFactory("ysbh"));
		srlb_ysmc.setCellValueFactory(new PropertyValueFactory("ysmc"));
		srlb_tableview.setItems(data);
	}
	// Event Listener on Button[#button_yes].onMouseClicked
	@FXML
	public void click_yes(MouseEvent event) {
		// TODO Autogenerated
	}
	// Event Listener on Button[#tips_yes].onMouseClicked
	@FXML
	public void click_tips_yes(MouseEvent event) {
		// TODO Autogenerated
	}
	
	public static class table_srlb{
		private final SimpleStringProperty ksmc;
		private final SimpleStringProperty ysbh;
		private final SimpleStringProperty ysmc;
		private final SimpleStringProperty hzlb;
		private final SimpleStringProperty ghrc;
		private final SimpleStringProperty srhj;
		
		public table_srlb(String ksmc_str,String ysbh_str,String ysmc_str,String hzlb_str,String ghrq_str,String srhj_str) {
			this.ksmc=new SimpleStringProperty(ksmc_str);
			this.ysbh=new SimpleStringProperty(ysbh_str);
			this.ysmc=new SimpleStringProperty(ysmc_str);
			if(hzlb_str.equals("1"))
			{
				this.hzlb=new SimpleStringProperty("专家号");
			}
			else 
			{
				this.hzlb=new SimpleStringProperty("普通号");	
			}
			this.ghrc=new SimpleStringProperty(ghrq_str);
			this.srhj=new SimpleStringProperty(srhj_str);
		}
		
		public String getKsmc() {
            return ksmc.get();
        }
        public void setKsmc(String str) {
            ksmc.set(str);
        }
        public SimpleStringProperty ksmcProperty(){
            return ksmc;
        }

        
        public String getYsbh() {
            return ysbh.get();
        }
        public void setYsbh(String str) {
            ysbh.set(str);
        }
        public SimpleStringProperty ysbhProperty(){
            return ysbh;
        }
        
        public String getYsmc() {
            return ysmc.get();
        }
        public void setYsmc(String str) {
            ysmc.set(str);
        }
        public SimpleStringProperty ysmcProperty(){
            return ysmc;
        }
        
        public String getHzlb() {
            return hzlb.get();
        }
        public void setHzlb(String str) {
        	hzlb.set(str);
        }
        public SimpleStringProperty hzlbProperty(){
            return hzlb;
        }
        
        public String getGhrc() {
            return ghrc.get();
        }
        public void setGhrc(String str) {
            ghrc.set(str);
        }
        public SimpleStringProperty ghrcProperty(){
            return ghrc;
        }
        
        public String getSrhj() {
            return srhj.get();
        }
        public void setSrhj(String str) {
            srhj.set(str);
        }
        public SimpleStringProperty srhjProperty(){
            return srhj;
        }
	}
	
	public static class table_brlb{
		private final SimpleStringProperty ghbh;
		private final SimpleStringProperty brmc;
		private final SimpleStringProperty ghrq;
		private final SimpleStringProperty hzlb;
		
		public table_brlb(String ghbh_str,String brmc_str,String ghrq_str,String hzlb_str) {
			this.ghbh=new SimpleStringProperty(ghbh_str);
			this.brmc=new SimpleStringProperty(brmc_str);
			this.ghrq=new SimpleStringProperty(ghrq_str);
			if(hzlb_str.equals("1"))
			{
				this.hzlb=new SimpleStringProperty("专家号");
			}
			else 
			{
				this.hzlb=new SimpleStringProperty("普通号");	
			}
		}
		public String getGhbh() {
            return ghbh.get();
        }
        public void setGhbh(String str) {
            ghbh.set(str);
        }
        public SimpleStringProperty ghbhProperty(){
            return ghbh;
        }

        
        public String getBrmc() {
            return brmc.get();
        }
        public void setBrmc(String str) {
            brmc.set(str);
        }
        public SimpleStringProperty brmcProperty(){
            return brmc;
        }
        
        public String getGhrq() {
            return ghrq.get();
        }
        public void setGhrq(String str) {
            ghrq.set(str);
        }
        public SimpleStringProperty ghrqProperty(){
            return ghrq;
        }
        
        public String getHzlb() {
            return hzlb.get();
        }
        public void setHzlb(String str) {
        	hzlb.set(str);
        }
        public SimpleStringProperty hzlbProperty(){
            return hzlb;
        }
	}
}
