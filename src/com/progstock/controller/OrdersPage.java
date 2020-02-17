package com.progstock.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.jfoenix.controls.JFXTextField;
import com.progstock.model.Barcode;
import com.progstock.model.CustomCommandeTableView;
import com.progstock.model.LoggerWrapper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/**
 * <b>OrdersPage is the class that represents the controller of the orders management window.</b>
 */
public class OrdersPage implements Initializable {

	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	
	private ObservableList<String> listFamilyProduct = FXCollections.observableArrayList();
	private ObservableList<String> listProduct = FXCollections.observableArrayList();
	private ObservableList<String> listFournisseur = FXCollections.observableArrayList();
	private ObservableList<String> listBarcode = FXCollections.observableArrayList();
	private ObservableList<String> listReference = FXCollections.observableArrayList();
	private ObservableList<AnchorPane> listMovementProductUi = FXCollections.observableArrayList();
	private ObservableList<AddMovementProduct> listMovementProduct = FXCollections.observableArrayList();
	private ObservableList<CustomCommandeTableView> listCustomCommandeTV = FXCollections.observableArrayList();
	
	@FXML
	private TableView<CustomCommandeTableView> commande_Tv;
	@FXML
	private JFXTextField userName_Tf;
	@FXML
	private VBox vBox;
	@FXML
	private DatePicker date_Dp;
	@FXML
	private ComboBox<String> familyProduct_Cb;
	@FXML
	private ComboBox<String> product_Cb;
	@FXML
	private ComboBox<String> fournisseur_Cb;
	@FXML
	private ComboBox<String> reference_Cb;
	@FXML
	private TextField refCommande_Tf;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param principalUi
	 * 		The main window controller.
	 */
	public OrdersPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/OrdersPageUi.fxml"));
		loader.setController(this);
		
		try
		{
			this.setPrincipalUiCtrl(principalUi);
			setAnchorPane(loader.load());
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
		
		initCustomListTableView();
		addFormMovement(6);
	}
	
	/**
	 * Reset the comboboxes of each command addition line.
	 */
	public void refreshMovementProduct() {
		for(AddMovementProduct item : listMovementProduct)
		{
			item.refreshComboBox();
		}
	}
	
	/**
	 * Adds a single line in the add commands form.
	 */
	public void addFormMovementButtonClick()
	{
		addFormMovement(1);
	}
	
	
	/**
	 * Adds a defined number of lines in the add commands form.
	 * 
	 * @param nbr
	 * 		The number of lines to add.
	 */
	public void addFormMovement(int nbr)
	{
		for(int i = 0 ; i < nbr ; i++)
		{
			AddMovementProduct amp = new AddMovementProduct(this);
			
			listMovementProduct.add(amp);
			listMovementProductUi.add(amp.getAnchorPane());
		}
		
		vBox.getChildren().setAll(listMovementProductUi);
	}
	
	/**
	 * Deletes all command lines.
	 */
	public void clearFormMovement()
	{
		listMovementProduct.clear();
		listMovementProductUi.clear();
	}
	
	/**
	 * Reset all comboboxes.
	 */
	public void resetCombo()
	{
		date_Dp.setValue(null);
		familyProduct_Cb.setValue(null);
		product_Cb.setValue(null);
		fournisseur_Cb.setValue(null);
		reference_Cb.setValue(null);
	}
	
	/**
	 * Deletes one of the command lines.
	 * 
	 * @param item
	 * 		The command line to delete.
	 */
	public void deleteSelectedRow(AddMovementProduct item) {
		for (AnchorPane childs : listMovementProductUi) {
		    
			if(childs.equals(item.getAnchorPane()))
			{
				listMovementProductUi.remove(childs);
				vBox.getChildren().remove(childs);
				break;
			}
		}
		
		listMovementProduct.remove(item);
	}
	
	/**
	 * Initializes the combobox of the articles.
	 */
	public void initComboBoxProduct()
	{
		listProduct.clear();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("article",String.format("article.NOM_ARTICLE like '%s%%'",product_Cb.getEditor().getText().toUpperCase()));    
		
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
		
		product_Cb.setItems(listProduct);
	}
	
	/**
	 * Initializes the reference combobox.
	 */
	public void initComboBoxReference()
	{
		listReference.clear();	
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("commande",String.format("commande.REFERENCE_COMMANDE like '%s%%'",reference_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listReference.add(result.getString("REFERENCE_COMMANDE"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		reference_Cb.setItems(listReference);
	}
	
	/**
	 * Initializes the combobox of the article types.
	 */
	public void initComboBoxFamilyProduct()
	{
		listFamilyProduct.clear();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("famillearticle",String.format("famillearticle.nom_typearticle like '%s%%'",familyProduct_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listFamilyProduct.add(result.getString("nom_typearticle"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		familyProduct_Cb.setItems(listFamilyProduct);
	}
	
	/**
	 * Initializes the combobox of the suppliers.
	 */
	public void initComboBoxFournisseur()
	{
		listFournisseur.clear();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("fournisseur",String.format("fournisseur.NOM_FOURNISSEUR like '%s%%'",fournisseur_Cb.getEditor().getText().toUpperCase()));    
		
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
	}
	
	/**
	 * Adds a new order in the database.
	 */
	public void addMovementButtonClick() {
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		
		for(AddMovementProduct amp : listMovementProduct)
		{
			if(!amp.isEmpty())
			{
				this.getPrincipalUiCtrl().getDatabase().executionQuery(
				String.format("call createMouvement(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")",
				"Entrée",this.getPrincipalUiCtrl().getCurrentUser().getUser(),amp.getFournisseur(),amp.getArticle(),amp.getQuantite(),amp.getEmplacement(),refCommande_Tf.getText())
				);
			}
		}
		
		getPrincipalUiCtrl().getInvPageCtrl().initCustomListTableView();
		initCustomListTableView();
		clearFormMovement();
		addFormMovement(6);
		refCommande_Tf.clear();
	}
	
	/**
	 * Updates the orders display.
	 */
	public void initCustomListTableView()
	{	
		listCustomCommandeTV.clear();
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery((String.format(
				"CALL selectMouvement(%s,%s,%s,%s,%s)",
				parceurNull(String.valueOf(date_Dp.getValue())),
				parceurNull(String.valueOf(product_Cb.getValue())),
				parceurNull(String.valueOf(fournisseur_Cb.getValue())),
				parceurNull(String.valueOf(familyProduct_Cb.getValue())),
				parceurNull(String.valueOf(reference_Cb.getValue()))
		)));
		try
		{
			while(result.next())
			{
				CustomCommandeTableView custom = new CustomCommandeTableView(); /* Process the data from the Database into a Custom Object for display */
				custom.setId(result.getInt("id_mouvement"));
				custom.setDateFormat(result.getString("date_mouvement"));
				custom.setType(result.getString("nom_typearticle"));
				custom.setFournisseur(result.getString("nom_fournisseur"));
				custom.setArticle(result.getString("nom_article"));
				custom.setReference(result.getString("reference_commande"));
				custom.setQuantite(result.getString("nombre"));
				
				listCustomCommandeTV.add(custom);
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		commande_Tv.setItems(listCustomCommandeTV);
	}
	
	/**
	 * Prints the barcode of the currently selected order.
	 */
	public void printBarcodeButtonClick()
	{
		listBarcode.clear();
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		
		
		if(!commande_Tv.getSelectionModel().isEmpty())
		{
			ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call selectMouvementBarcode(\"%d\")",commande_Tv.getSelectionModel().getSelectedItem().getId()));
		
			try
			{
				while(result.next())
				{
					String barcode = result.getString("design_codebarre");
					
					File appData = new File(System.getProperty("user.home"),".GestionStock");
					
					Barcode bc = new Barcode(barcode);
					bc.saveToImage("png",appData+"\\Barcode.png");
					bc.print();
				}
			}
			catch (SQLException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
	/**
	 * Check the string and add quotes if necessary.
	 * 
	 * @param string
	 * 		The string that needs to be parsed.
	 * 
	 * @return The modified chain.
	 */
	public String parceurNull(String string)
    {
        String tempo = String.valueOf(string);
        
        if(!tempo.equals("null"))
        {
            tempo = "'"+string+"'";
        }
        
        return tempo;
    }
	
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}

	public void setAnchorPane(AnchorPane anchorPane) {
		this.anchorPane = anchorPane;
	}

	public PrincipalUi getPrincipalUiCtrl() {
		return principalUiCtrl;
	}

	public void setPrincipalUiCtrl(PrincipalUi principalUiCtrl) {
		this.principalUiCtrl = principalUiCtrl;
	}
}
