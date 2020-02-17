package com.progstock.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.CustomDeplacementTableView;
import com.progstock.model.LoggerWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * <b>MovementPage is the class that represents the window controller for managing the movement of articles.</b>
 */
public class MovementPage implements Initializable {
	
	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	
	private ObservableList<String> listCodeBarre = FXCollections.observableArrayList();
	private ObservableList<String> listObject = FXCollections.observableArrayList();
	private ObservableList<String> listPersonne = FXCollections.observableArrayList();
	private ObservableList<String> listEtat = FXCollections.observableArrayList();
	private ObservableList<CustomDeplacementTableView> listCustomDeplacementTV = FXCollections.observableArrayList();
	
	@FXML
	private TableView<CustomDeplacementTableView> tableView_Tv;
	@FXML
	private ComboBox<String> codeBarre_Cb;
	@FXML
	private ComboBox<String> objet_Cb;
	@FXML
	private ComboBox<String> personne_Cb;
	@FXML
	private ComboBox<String> etat_Cb;
	@FXML
	private DatePicker date_Cb;
	@FXML
	private TextField codeObjet_Tf;
	@FXML
	private TextField codePersonne_Tf;
	
    /**
     * Constructor with some initial data.
     * 
     * @param principalUi
     * 		The main window controller.
     */
	public MovementPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/MovementPageUi.fxml"));
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
	public void initialize(URL url, ResourceBundle rb)
	{
		initCustomListTableView();
	}
	
	/**
	 * Reset all comboboxes.
	 */
	public void resetCombo()
	{
		codeBarre_Cb.setValue(null);
		objet_Cb.setValue(null);
		personne_Cb.setValue(null);
		etat_Cb.setValue(null);
		date_Cb.setValue(null);
		
		initCustomListTableView();
	}
	
	/**
	 * Initialize the barcode combobox.
	 */
	public void initComboBoxCodeBarre()
	{
		listCodeBarre.clear();
		this.getPrincipalUiCtrl().getDatabase().connexion();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("codebarre",String.format("codebarre.DESIGN_CODEBARRE like '%s%%'",codeBarre_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listCodeBarre.add(result.getString("DESIGN_CODEBARRE"));
			}
		}
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		codeBarre_Cb.setItems(listCodeBarre);
	}
	
	/**
	 * Initializes the combobox of articles
	 */
	public void initComboBoxObjet()
	{
		listObject.clear();
		this.getPrincipalUiCtrl().getDatabase().connexion();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("article",String.format("article.NOM_ARTICLE like '%s%%'",objet_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listObject.add(result.getString("NOM_ARTICLE"));
			}
		} 
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		objet_Cb.setItems(listObject);
	}
	
	/**
	 * Initializes the user combobox
	 */
	public void initComboBoxPersonne()
	{
		listPersonne.clear();
		this.getPrincipalUiCtrl().getDatabase().connexion();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("utilisateur",String.format("utilisateur.NOM_UTILISATEUR like '%s%%'",personne_Cb.getEditor().getText().toUpperCase())); 
		
		try
		{
			while(result.next())
			{
				listPersonne.add(result.getString("NOM_UTILISATEUR"));
			}
		} 
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		personne_Cb.setItems(listPersonne);
	}
	
	/**
	 * Initializes the combobox of the states
	 */
	public void initComboBoxEtat()
	{
		listEtat.clear();
		this.getPrincipalUiCtrl().getDatabase().connexion();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll("deplacement",String.format("deplacement.ETAT like '%s%%' group by deplacement.ETAT",etat_Cb.getEditor().getText().toUpperCase()));    
		
		try
		{
			while(result.next())
			{
				listEtat.add(result.getString("ETAT"));
			}
		} 
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		etat_Cb.setItems(listEtat);
	}
	
	/**
	 * Updates the movements display.
	 */
	public void initCustomListTableView()
	{	
		listCustomDeplacementTV.clear();
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery((String.format(
				"CALL selectCumstomDeplacement(%s,%s,%s,%s,%s)",
				parceurNull(String.valueOf(codeBarre_Cb.getValue())),
				parceurNull(String.valueOf(objet_Cb.getValue())),
				parceurNull(String.valueOf(personne_Cb.getValue())),
				parceurNull(String.valueOf(etat_Cb.getValue())),
				parceurNull(String.valueOf(date_Cb.getValue()))
		)));
		
		try {
			while(result.next())
			{
				CustomDeplacementTableView custom = new CustomDeplacementTableView(); /* Process the data from the Database into a Custom Object for display */
				custom.setCodeBarre(result.getString("DESIGN_CODEBARRE"));
				custom.setObjet(result.getString("NOM_ARTICLE"));
				custom.setPersonne(result.getString("NOM_UTILISATEUR"));
				custom.setEtat(result.getString("ETAT"));
				custom.setDate(result.getString("DATE_DEPLACEMENT"));
				
				listCustomDeplacementTV.add(custom);
			}
		}
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
	tableView_Tv.setItems(listCustomDeplacementTV);
	
	}
	
	/**
	 * Adds a move to the database.
	 */
	public void addButton() {
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		this.getPrincipalUiCtrl().getDatabase().executionQuery((String.format("CALL createDeplacement(\"%s\",\"%s\")",codePersonne_Tf.getText(),codeObjet_Tf.getText())));
        
        initCustomListTableView();
        
        codePersonne_Tf.clear();
        codeObjet_Tf.clear();
	}
	
	/**
	 * Displays only objects that have never returned.
	 */
	public void stillNotReturned()
	{
		listCustomDeplacementTV.clear();
		
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery("call stillNotReturned()");
		
		try
		{
			while(result.next())
			{
				CustomDeplacementTableView custom = new CustomDeplacementTableView();
				custom.setCodeBarre(result.getString("DESIGN_CODEBARRE"));
				custom.setObjet(result.getString("NOM_ARTICLE"));
				custom.setPersonne(result.getString("NOM_UTILISATEUR"));
				custom.setEtat(result.getString("ETAT"));
				custom.setDate(result.getString("DATE_DEPLACEMENT"));
				
				listCustomDeplacementTV.add(custom);
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
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
