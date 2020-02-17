package com.progstock.controller;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.ResultSet;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.DataBase;
import com.progstock.model.LoggerWrapper;
import com.progstock.model.Utilisateur;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>ConnectionUi is the class that represents the controller of the connection window.</b>
 */
public class ConnectionUi implements Initializable
{
	private Utilisateur currentUser;
	
	@FXML
	private ImageView imgLogo;
	@FXML
	private ImageView imgUser;
	@FXML
	private ImageView imgPasse;
	@FXML
	private TextField username;
	@FXML
	private PasswordField password;
	@FXML
	private Button login_bt;
	
	/**
	 * Default constructor.
	 */
	public ConnectionUi()
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/ConnectionUi.fxml"));
		loader.setController(this);
		
		try 
        {
        	Parent root = loader.load();
    		Scene scene = new Scene(root);
    		
    		URL url = Main.class.getClassLoader().getResource("style.css");
    		root.getStylesheets().clear();
    		root.getStylesheets().add(url.toURI().toString());
    		
    		Stage stage = new Stage();
    		stage.setTitle("Connection");
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
	 * Initialization function of the class.
	 * <p>This is automatically called when the fxml is loaded.</p>
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		// Generates the folder used to store temporary program data.
		File appData = new File(System.getProperty("user.home"),".GestionStock");
		
		if(!(appData.exists()))
			appData.mkdir();
		
		// Generates the folder used to store logs.
		File logsFile = new File(appData+"\\logs");
		
		if(!(logsFile.exists()))
			logsFile.mkdir();
					
		login_bt.defaultButtonProperty().bind(password.focusedProperty());
		
		URL urlImg1 = Main.class.getClassLoader().getResource("Images/logoPerso.png");
		URL urlImg2 = Main.class.getClassLoader().getResource("Images/user-silhouette.png");
		URL urlImg3 = Main.class.getClassLoader().getResource("Images/locked-padlock.png");
		
		try
		{
			this.imgLogo.setImage(new Image(urlImg1.toURI().toString()));
			this.imgUser.setImage(new Image(urlImg2.toURI().toString()));
			this.imgPasse.setImage(new Image(urlImg3.toURI().toString()));
		}
		catch (URISyntaxException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
        
        currentUser = new Utilisateur();
	}
	
	/**
	 * Checks in the database if the user exists, if the password is correct and gives access to the rest of the program.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	@FXML
	public void checkLogin(ActionEvent event) 
	{
		try
		{
			DataBase database = new DataBase("jdbc:mysql://localhost:3306/gestionstock?useSSL=true","root","Secret123");
			
			ResultSet result = database.executionQuery(String.format("call selectUser(\"%s\",\"%s\")",username.getText(),password.getText()));
			
			if(result.next())
			{
				if(result.getString("DESACTIVER_UTILISATEUR").equals("0"))
				{
					String temp = result.getString("DESACTIVER_UTILISATEUR");
					temp = (temp.equals("0"))?"false":"true";
					
					this.getCurrentUser().setId(result.getInt("ID_UTILISATEUR"));
					this.getCurrentUser().setNom(result.getString("NOM_UTILISATEUR"));
					this.getCurrentUser().setPrenom(result.getString("PRENOM_UTILISATEUR"));
					this.getCurrentUser().setEmail(result.getString("EMAIL_UTILISATEUR"));
					this.getCurrentUser().setUser(result.getString("USER_UTILISATEUR"));
					this.getCurrentUser().setPassword(result.getString("PASSWORD_UTILISATEUR"));
					this.getCurrentUser().setRole(result.getString("NOM_ROLEUTILISATEUR"));
					this.getCurrentUser().setBarcode(result.getString("DESIGN_CODEBARRE"));
					this.getCurrentUser().setDisable(Boolean.valueOf(temp));
					
					new PrincipalUi(this.getCurrentUser());
					
					// Retrieves the source of the event and closes the window that contains it.
					((Node)event.getSource()).getScene().getWindow().hide();
				}
				else
				{
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Erreur...");
					alert.setHeaderText(null);
					alert.setContentText("L'utilisateur n'existe pas ou à été supprimé.");
					alert.showAndWait();
				}
			}
		}
		catch (Exception e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	public Utilisateur getCurrentUser() {
		return currentUser;
	}
	
	public void setCurrentUser(Utilisateur currentUser) {
		this.currentUser = currentUser;
	}
}
