package com.progstock.controller;

import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.Barcode;
import com.progstock.model.LoggerWrapper;
import com.progstock.model.Utilisateur;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

/**
 * <b>UsersPage is the class that represents the user management window controller.</b>
 */
public class UsersPage implements Initializable {
	
	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	
	private ObservableList<Utilisateur> tableViewUtilisateur = FXCollections.observableArrayList();
	
	@FXML
	private TextField nom_Tf;
	@FXML
	private TextField prenom_Tf;
	@FXML
	private TextField password_Tf;
	@FXML
	private TableView<Utilisateur> tableView_Tv;
	
    /**
     * Constructor with some initial data.
     * 
     * @param principalUi
     * 		The main window controller.
     */
	public UsersPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/UsersPageUi.fxml"));
		loader.setController(this);
		
		try
		{
			this.setPrincipalUiCtrl(principalUi);
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
		
		this.initTableView();
	}
	
	/**
	 * Calls a window to encode a user.
	 */
	public void add()
	{
		new AddUtilisateur(this,null);
	}
	
	/**
	 * Delete the currently selected user.
	 */
	public void delete()
	{
		this.getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call deleteUser(\"%s\")",tableView_Tv.getSelectionModel().getSelectedItem().getNom()));
		this.initTableView();
	}
	
	/**
	 * Calls a window to modify a user's data.
	 */
	public void update()
	{
		if(tableView_Tv.getSelectionModel().getSelectedIndex() >= 0)
		{
			Utilisateur user = tableView_Tv.getSelectionModel().getSelectedItem();
			new AddUtilisateur(this,user);
		}
	}
	
	/**
	 * Prints the barcode of the currently selected user.
	 */
	public void printBarcode()
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
	 * Updates the user display.
	 */
	public void initTableView()
	{		
		this.getTableViewUtilisateur().clear();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery("call getAllUsers()");
		
		try
		{
			while(result.next())
			{		
				String temp = result.getString("DESACTIVER_UTILISATEUR");
				temp = (temp.equals("0"))?"false":"true";
				
				if(!Boolean.valueOf(temp))
				{
					Utilisateur user = new Utilisateur();
					user.setId(result.getInt("ID_UTILISATEUR"));
					user.setNom(result.getString("NOM_UTILISATEUR"));
					user.setPrenom(result.getString("PRENOM_UTILISATEUR"));
					user.setEmail(result.getString("EMAIL_UTILISATEUR"));
					user.setUser(result.getString("USER_UTILISATEUR"));
					user.setPassword(result.getString("PASSWORD_UTILISATEUR"));
					user.setRole(result.getString("NOM_ROLEUTILISATEUR"));
					user.setBarcode(result.getString("DESIGN_CODEBARRE"));
					user.setDisable(Boolean.valueOf(temp));
					
					tableViewUtilisateur.add(user);
				}
			}
		}
		catch(SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		tableView_Tv.setItems(tableViewUtilisateur);
	}

	public ObservableList<Utilisateur> getTableViewUtilisateur() {
		return tableViewUtilisateur;
	}

	public void setTableViewUtilisateur(ObservableList<Utilisateur> tableViewUtilisateur) {
		this.tableViewUtilisateur = tableViewUtilisateur;
	}
	
	public AnchorPane getAnchorPane() {
		return anchorPane;
	}

	public PrincipalUi getPrincipalUiCtrl() {
		return principalUiCtrl;
	}

	public void setPrincipalUiCtrl(PrincipalUi principalUiCtrl) {
		this.principalUiCtrl = principalUiCtrl;
	}

	public TableView<Utilisateur> getTableView_Tv() {
		return tableView_Tv;
	}

	public void setTableView_Tv(TableView<Utilisateur> tableView_Tv) {
		this.tableView_Tv = tableView_Tv;
	}
}
