package com.progstock.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.DataBase;
import com.progstock.model.LoggerWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * <b>AddMovementProduct is the class that represents the command line controller to be added in a command.</b>
 */
public class AddMovementProduct implements Initializable {

	private DataBase database;
	private AnchorPane anchorPane;
	private OrdersPage ordersPageCtrl;
	private ObservableList<String> listProduct = FXCollections.observableArrayList();
	private ObservableList<String> listLocation = FXCollections.observableArrayList();
	private ObservableList<String> listFournisseur = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> article_Cb;
	@FXML
	private ComboBox<String> emplacement_Cb;
	@FXML
	private ComboBox<String> fournisseur_Cb;
	@FXML
	private TextField quantite_Tf;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param ordersPageCtrl
	 * 		The controller of the commands page.
	 */
	public AddMovementProduct(OrdersPage ordersPageCtrl)
	{
		this.setOrdersPageCtrl(ordersPageCtrl);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/AddMovementProductUi.fxml"));
		loader.setController(this);
		
		try
		{
			anchorPane = loader.load();
		}
		catch (IOException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Initialization function of the class.
	 * <p>This is automatically called when the fxml is loaded.</p>
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		article_Cb.valueProperty().set("");
		emplacement_Cb.valueProperty().set("");
		fournisseur_Cb.valueProperty().set("");
		database = this.getOrdersPageCtrl().getPrincipalUiCtrl().getDatabase();
	}
	
	/**
	 * Reset all comboboxes.
	 */
	public void refreshComboBox() {
		initComboBoxProduct();
		initComboBoxLocation();
		initComboBoxFournisseur();
	}
	
	/**
	 * Initializes the combobox of the articles.
	 */
	public void initComboBoxProduct()
	{
		listProduct.clear();
		ResultSet result = database.querySelectAll("article",String.format("article.NOM_ARTICLE like '%s%%'",article_Cb.getEditor().getText().toUpperCase()));  
		
		try
		{
			while(result.next())
			{
				listProduct.add(result.getString("NOM_ARTICLE"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		article_Cb.setItems(listProduct);
		article_Cb.setValue("");
	}
	
	/**
	 * Initializes the combobox of the locations.
	 */
	public void initComboBoxLocation()
	{
		listLocation.clear();
		ResultSet result = database.querySelectAll("emplacement",String.format("emplacement.DESCRIPTION like '%s%%'",emplacement_Cb.getEditor().getText().toUpperCase()));
		
		try
		{
			while(result.next())
			{
				listLocation.add(result.getString("DESCRIPTION"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		emplacement_Cb.setItems(listLocation);
		emplacement_Cb.setValue("");
	}
	
	/**
	 * Initializes the combobox of the suppliers.
	 */
	public void initComboBoxFournisseur()
	{
		listFournisseur.clear();
		ResultSet result = database.querySelectAll("fournisseur",String.format("fournisseur.NOM_FOURNISSEUR like '%s%%'",fournisseur_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listFournisseur.add(result.getString("NOM_FOURNISSEUR"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		fournisseur_Cb.setItems(listFournisseur);
		fournisseur_Cb.setValue("");
	}
	
	/**
	 * After adding a new product to the list, select it.
	 * 
	 * @param value
	 * 		The product to be selected.
	 */
	public void selectProductAfterAdd(String value) {
		
		initComboBoxProduct();
		
		if(value.isEmpty())
		{
			article_Cb.setValue("");
		}
		else
		{
			ResultSet result = database.querySelectAll("article",String.format("article.NOM_ARTICLE like '%s%%'",value));
			
			try
			{
				while(result.next())
				{
					article_Cb.setValue(result.getString("NOM_ARTICLE"));
				}
			}
			catch (SQLException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
	/**
	 * After adding a new location to the list, select it.
	 * 
	 * @param value
	 * 		The location to be selected.
	 */
	public void selectLocationAfterAdd(String value) {
		
		initComboBoxLocation();
		
		if(value.isEmpty())
		{
			emplacement_Cb.setValue("");
		}
		else
		{
			ResultSet result = database.querySelectAll("emplacement",String.format("emplacement.DESCRIPTION like '%s%%'",value));
			
			try
			{
				while(result.next())
				{
					emplacement_Cb.setValue(result.getString("DESCRIPTION"));
				}
			}
			catch (SQLException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
	/**
	 * After adding a new supplier to the list, select it.
	 * 
	 * @param value
	 * 		The supplier to be selected.
	 */
	public void selectFournisseurAfterAdd(String value) {
		
		initComboBoxFournisseur();
		
		if(value.isEmpty())
		{
			fournisseur_Cb.setValue("");
		}
		else
		{
			ResultSet result = database.querySelectAll("fournisseur",String.format("fournisseur.NOM_FOURNISSEUR like '%s%%'",value));
			
			try
			{
				while(result.next())
				{
					fournisseur_Cb.setValue(result.getString("NOM_FOURNISSEUR"));
				}
			}
			catch (SQLException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
	/**
	 * Checks if the command line fields are not empty.
	 * 
	 * @return True if they are empty, false if they are not.
	 */
	public boolean isEmpty() {
		return (article_Cb.getValue().isEmpty() &&
				emplacement_Cb.getValue().isEmpty() &&
				fournisseur_Cb.getValue().isEmpty() &&
				quantite_Tf.getText().isEmpty());
	}
	
	/**
	 * Calls up the window for adding new articles.
	 */
	public void addProductPage() {
		AddProduct addProd = new AddProduct();
		addProd.setAddMovementProductCtrl(this);
	}
	
	/**
	 * Calls the window to add new locations.
	 */
	public void addLocationPage() {
		AddLocation addLoc = new AddLocation();
		addLoc.setAddMovementProductCtrl(this);
	}
	
	/**
	 * Calls up the window for adding new suppliers.
	 */
	public void addFournisseurPage() {
		AddFournisseur addFourni = new AddFournisseur();
		addFourni.setAddMovementProductCtrl(this);
	}
	
	/**
	 * Delete the current command line.
	 */
	public void deleteRow() {
		this.getOrdersPageCtrl().deleteSelectedRow(this);
	}
	
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}
	
	public String getArticle() {
		return article_Cb.getValue();
	}
	
	public String getEmplacement() {
		return emplacement_Cb.getValue();
	}
	
	public String getFournisseur() {
		return fournisseur_Cb.getValue();
	}
	
	public String getQuantite() {
		return quantite_Tf.getText();
	}

	public OrdersPage getOrdersPageCtrl() {
		return ordersPageCtrl;
	}

	public void setOrdersPageCtrl(OrdersPage ordersPageCtrl) {
		this.ordersPageCtrl = ordersPageCtrl;
	}
}
