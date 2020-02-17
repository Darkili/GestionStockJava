package com.progstock.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.Barcode;
import com.progstock.model.CustomObjectTableView;
import com.progstock.model.LoggerWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * <b>InventoryPage is the class that represents the inventory management window controller.</b>
 */
public class InventoryPage implements Initializable {
	
	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	private String scan;
	private Integer noteSize;
	private CustomObjectTableView previousObject;
	
	ObservableList<String> listLocation = FXCollections.observableArrayList();
	ObservableList<String> listProduct = FXCollections.observableArrayList();
	ObservableList<String> listRefCommande = FXCollections.observableArrayList();
	ObservableList<String> listBarcode = FXCollections.observableArrayList();
	ObservableList<String> listFamilyProduct = FXCollections.observableArrayList();
	ObservableList<CustomObjectTableView> listCustomObjectTV = FXCollections.observableArrayList();
	
	@FXML
	private TableView<CustomObjectTableView> tableView_Tv;
	@FXML
	private ComboBox<String> location_Cb;
	@FXML
	private ComboBox<String> product_Cb;
	@FXML
	private ComboBox<String> refCommande_Cb;
	@FXML
	private ComboBox<String> barcode_Cb;
	@FXML
	private ComboBox<String> familyProduct_Cb;
	@FXML
	private TextField product_Tf;
	@FXML
	private TextField productType_Tf;
	@FXML
	private TextField location_Tf;
	@FXML
	private TextField fournisseur_Tf;
	@FXML
	private TextField etat_Tf;
	@FXML
	private TextField barcode_Tf;
	@FXML
	private TextField reference_Tf;
	@FXML
	private TextField prix_Tf;
	@FXML
	private TextArea note_Ta;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param principalUi
	 * 		The main window controller.
	 */
	public InventoryPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/InventoryPageUi.fxml"));
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
		
		this.initCustomListTableView();
		this.scan = "";
		this.noteSize = 0;
		
		// Stores the second last object selected when selecting a new object in the view table.
		tableView_Tv.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			previousObject = oldValue;
		});
		
		// Save notes when the textArea loses focus and only if the text has been modified.
		note_Ta.focusedProperty().addListener(event -> {
			if(!note_Ta.isFocused() && noteSize != note_Ta.getLength())
			{
				if(!tableView_Tv.isFocused())
					previousObject = tableView_Tv.getSelectionModel().getSelectedItem();
				
				updateNote();
			}
		});
	}
	
	/**
	 * Reset all comboboxes.
	 */
	public void resetCombo()
	{
		listLocation.clear();
		listProduct.clear();
		listRefCommande.clear();
		listBarcode.clear();
		listFamilyProduct.clear();
	}
	
	/**
	 * Initializes the combobox of the locations.
	 */
	public void initComboBoxLocation()
	{
		listLocation.clear();		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("emplacement",String.format("emplacement.DESCRIPTION like '%s%%'",location_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
				listLocation.add(result.getString("DESCRIPTION"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		location_Cb.setItems(listLocation);		
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
				listProduct.add(result.getString("NOM_ARTICLE"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		product_Cb.setItems(listProduct);
	}
	
	/**
	 * Initializes the combobox of command references.
	 */
	public void initComboBoxRefCommande()
	{	
		listRefCommande.clear();		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("commande",String.format("commande.REFERENCE_COMMANDE like '%s%%'",refCommande_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
				listRefCommande.add(result.getString("REFERENCE_COMMANDE"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		refCommande_Cb.setItems(listRefCommande);
	}
	
	/**
	 * Initialize the barcode combobox.
	 */
	public void initComboBoxBarcode()
	{
		listBarcode.clear();		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("codebarre",String.format("codebarre.design_codebarre like '%s%%'",barcode_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
				listBarcode.add(result.getString("design_codebarre"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		barcode_Cb.setItems(listBarcode);
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
				listFamilyProduct.add(result.getString("nom_typearticle"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		familyProduct_Cb.setItems(listFamilyProduct);
	}	
	
	/**
	 * Updates the display of articles.
	 */
	public void initCustomListTableView()
	{	
		listCustomObjectTV.clear();		
		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery((String.format(
				"CALL selectionCustom(%s,%s,%s,%s,%s)",
				parceurNull(String.valueOf(location_Cb.getValue())),
				parceurNull(String.valueOf(familyProduct_Cb.getValue())),
				parceurNull(String.valueOf(product_Cb.getValue())),
				parceurNull(String.valueOf(refCommande_Cb.getValue())),
				parceurNull(String.valueOf(barcode_Cb.getValue()))
		)));
		
		try 
		{
			while(result.next())
			{
				String temp = result.getString("DESACTIVER_ELEMENTUNIQUE");
				temp = (temp.equals("0"))?"false":"true";
				
				if(!Boolean.valueOf(temp))
				{
					// Process the data from the Database into a Custom Object for display.
					CustomObjectTableView custom = new CustomObjectTableView();
					custom.setIdProduct(result.getInt("ID_ELEMENTUNIQUE"));
					custom.setIdInfoSupp(result.getInt("ID_INFOSUPPLÉMENTAIRE"));
					custom.setNameProduct(result.getString("NOM_ARTICLE"));
					custom.setCodeProduct(result.getString("Code_Article"));
					custom.setLocation(result.getString("DESCRIPTION"));
					custom.setFamilyProduct(result.getString("NOM_TYPEARTICLE"));
					custom.setBarcode(result.getString("design_codebarre"));
					custom.setRefCommande(result.getString("REFERENCE_COMMANDE"));
					custom.setNote(result.getString("NOTE"));
					custom.setDisable(Boolean.valueOf(temp));
					
					listCustomObjectTV.add(custom);
				}
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		tableView_Tv.setItems(listCustomObjectTV);
	
	}
	
	/**
	 * Focus on the article that corresponds to the last stored barcode.
	 */
	public void getItemFocus() {
		
		for(CustomObjectTableView customObjectTV : tableView_Tv.getItems())
		{
			if(customObjectTV.getBarcode().equals(scan))
			{
				tableView_Tv.getSelectionModel().select(customObjectTV);
				break;
			}
		}
	}
	
	/**
	 * Opens the additional details page for the selected item.
	 */
	public void openDetailsPage() {
		if(tableView_Tv.getSelectionModel().getSelectedItem() != null)
		{
			this.getPrincipalUiCtrl().getDetailsPageCtrl().loadData(tableView_Tv.getSelectionModel().getSelectedItem().getIdInfoSupp());
			this.getPrincipalUiCtrl().detailsPageToFront();
		}

	}
	
	/**
	 * Displays the basic information of the selected item.
	 */
	public void displayItems()
	{
		if(this.scan.isEmpty() && !tableView_Tv.getSelectionModel().isEmpty())
		{
			this.setScan(tableView_Tv.getSelectionModel().getSelectedItem().getBarcode());
		}
		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call selectionProduct(\"%s\")",this.getScan()));
		
		tableView_Tv.requestFocus();
			
		try
		{
			if(result.next() || ! tableView_Tv.getSelectionModel().isEmpty())
			{
				product_Tf.setText(result.getString("nom_Article"));
				productType_Tf.setText(result.getString("nom_TypeArticle"));
				location_Tf.setText(result.getString("description"));
				fournisseur_Tf.setText(result.getString("nom_fournisseur"));
				barcode_Tf.setText(result.getString("barcode"));
				etat_Tf.setText(result.getString("etat"));
				reference_Tf.setText(result.getString("REFERENCE_COMMANDE"));
				prix_Tf.setText(result.getString("prix"));
				note_Ta.setText(result.getString("NOTE"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		this.scan = "";
		this.noteSize = this.note_Ta.getLength();
	}
	
	/**
	 * Calls up the window for scanning an article.
	 */
	public void scanButton()
	{
		new ScanProduct(this);
	}
	
	/**
	 * Deletes a unique element from the program.
	 */
	public void deleteButtonClick()
	{
		this.getPrincipalUiCtrl().getDatabase().executionQuery(
				String.format("call deleteElement(\"%s\")",tableView_Tv.getSelectionModel().getSelectedItem().getIdProduct())
		);
		
		initCustomListTableView();
	}
	
	/**
	 * Updates notes for a unique item.
	 * <p>Corresponds to a short text allowing data information about the object.</p>
	 */
	public void updateNote()
	{
		noteSize = note_Ta.getLength();
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(null);
		alert.setContentText("Voulez-vous modfier les notes ?");

		Optional<ButtonType> result = alert.showAndWait();
		
		if(result.get() == ButtonType.OK)
		{
			this.getPrincipalUiCtrl().getDatabase().executionQuery(
					String.format("call updateNote(\"%s\",\"%s\")",
							previousObject.getIdProduct(),
							note_Ta.getText()));
		}
		
		displayItems();
	}
	
	/**
	 * Prints the barcode of the selected item.
	 */
	public void printBarcodeButtonClick()
	{
		if(tableView_Tv.getSelectionModel().getSelectedIndex() >= 0)
		{
			String barcode = tableView_Tv.getSelectionModel().getSelectedItem().getBarcode();
			
			File appData = new File(System.getProperty("user.home"),".GestionStock");
			
			Barcode bc = new Barcode(barcode);
			bc.saveToImage("png",appData+"\\Barcode.png");
			bc.print();
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
	
	public String getScan() {
		return scan;
	}
	
	public void setScan(String scan) {
		this.scan = scan;
	}
	
	public PrincipalUi getPrincipalUiCtrl() {
		return principalUiCtrl;
	}
	
	public void setPrincipalUiCtrl(PrincipalUi principalUiCtrl) {
		this.principalUiCtrl = principalUiCtrl;
	}
	
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}
	
	public void setAnchorPane(AnchorPane anchorPane) {
		this.anchorPane = anchorPane;
	}

	public TableView<CustomObjectTableView> getTableView_Tv() {
		return tableView_Tv;
	}

	public void setTableView_Tv(TableView<CustomObjectTableView> tableView_Tv) {
		this.tableView_Tv = tableView_Tv;
	}
}
