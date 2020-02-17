package com.progstock.controller;

import java.net.URL;
import java.util.logging.Level;

import com.progstock.model.LoggerWrapper;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>ScanProduct is the class that represents the controller of the scanning window.</b>
 */
public class ScanProduct {

	private InventoryPage inventoryPageCtrl;
	
	@FXML
	private TextField scan_Tf;
	@FXML
	private Button confirm_bt;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param inventoryPage
	 * 		The controller of the inventory page.
	 */
	public ScanProduct(InventoryPage inventoryPage)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/ScanProductUi.fxml"));
		loader.setController(this);
		
		try
		{
			Parent root = loader.load();
    		Scene scene = new Scene(root);
			
    		URL url = Main.class.getClassLoader().getResource("style.css");
    		root.getStylesheets().clear();
    		root.getStylesheets().add(url.toURI().toString());
			
			Stage stage = new Stage();
    		stage.setTitle("Scan produit");
    		stage.initModality(Modality.WINDOW_MODAL);
    		stage.setScene(scene);
    		stage.centerOnScreen();
    		stage.show();
			
			confirm_bt.defaultButtonProperty().bind(scan_Tf.focusedProperty());
			setInventoryPageCtrl(inventoryPage);
		}
		catch (Exception e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Displays the information of the item that has been scanned.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void applyButton(ActionEvent event)
	{
		this.getInventoryPageCtrl().setScan(scan_Tf.getText());
		this.getInventoryPageCtrl().getItemFocus();
		this.getInventoryPageCtrl().displayItems();
		
		// Retrieves the source of the event and closes the window that contains it.
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	public InventoryPage getInventoryPageCtrl() {
		return inventoryPageCtrl;
	}

	public void setInventoryPageCtrl(InventoryPage inventoryPage) {
		this.inventoryPageCtrl = inventoryPage;
	}
}
