package com.progstock.controller;

import java.net.URL;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.progstock.model.DataBase;
import com.progstock.model.LoggerWrapper;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>AddProduct is the class that represents the controller of the add product window.</b>
 */
public class AddProduct {
	
	private AddMovementProduct addMovementProductCtrl;
	
	@FXML
	private TextField familyProduct_Tf;
	@FXML
	private TextField product_Tf;
	
	/**
	 * Default constructor.
	 */
	public AddProduct()
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/AddProductUi.fxml"));
		loader.setController(this);
        
        try
        {
        	Parent root = loader.load();
    		Scene scene = new Scene(root);
    		
    		URL url = Main.class.getClassLoader().getResource("style.css");
    		root.getStylesheets().clear();
    		root.getStylesheets().add(url.toURI().toString());
    		
    		Stage stage = new Stage();
    		stage.setTitle("GestionStock");
    		stage.initModality(Modality.WINDOW_MODAL);
    		stage.setScene(scene);
    		stage.centerOnScreen();
    		stage.show();
        }
        catch (Exception e)
        {
        	LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
        }
	}

	/**
	 * Adds a new product to the database.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void addButton(ActionEvent event) {
		
		if(validInput(familyProduct_Tf.getText()) && validInput(product_Tf.getText()))
		{
			DataBase database = this.getAddMovementProductCtrl().getOrdersPageCtrl().getPrincipalUiCtrl().getDatabase();
			database.executionQuery(String.format("Call createProduct(\"%s\",\"%s\");",familyProduct_Tf.getText(),product_Tf.getText()));
			
			this.getAddMovementProductCtrl().selectProductAfterAdd(product_Tf.getText());
			
			((Node)event.getSource()).getScene().getWindow().hide();
		}
	}
	
	/**
	 * Close the window.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void cancelButton(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	/**
	 * Verifies that the value entered is not empty or only composed of white space.
	 * 
	 * @param str
	 * 		The string to check.
	 * 
	 * @return True if the conditions are satisfied, false if not.
	 */
	public boolean validInput(String str)
	{
		Pattern pattern = Pattern.compile("^\\s+$");
		Matcher matcher = pattern.matcher(str);
		
		return (str.length() != 0 && !matcher.matches());
	}

	/**
	 * Retrieves the controller from the add movement window.
	 * 
	 * @return The controller's instance.
	 */
	public AddMovementProduct getAddMovementProductCtrl() {
		return addMovementProductCtrl;
	}

	/**
	 * Sends the controller of the add movement window.
	 * 
	 * @param addMovementProductCtrl
	 * 		The controller's instance.
	 */
	public void setAddMovementProductCtrl(AddMovementProduct addMovementProductCtrl) {
		this.addMovementProductCtrl = addMovementProductCtrl;
	}
}
