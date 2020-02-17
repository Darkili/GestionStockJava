package com.progstock.controller;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;

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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <b>AddUtilisateur is the class that represents the controller of the add and modify users window.</b>
 */
public class AddUtilisateur implements Initializable {

	private Utilisateur utilisateur;
	private UsersPage usersPageCtrl;
	
	@FXML
	private TextField surname_Tf;
	@FXML
	private TextField name_Tf;
	@FXML
	private TextField email_Tf;
	@FXML
	private TextField adresse_Tf;
	@FXML
	private TextField tel_Tf;
	@FXML
	private TextField user_Tf;
	@FXML
	private PasswordField password_Tf;
	@FXML
	private TextField role_Tf;
	@FXML
	private ChoiceBox<String> role_Cb;
	@FXML
	private Label error_Lb;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param usersPageCtrl
	 * 		The controller of the users page.
	 * @param utilisateur
	 * 		The selected user.
	 * 		
	 */
	public AddUtilisateur(UsersPage usersPageCtrl, Utilisateur utilisateur)
	{
		this.setUsersPageCtrl(usersPageCtrl);
		this.setUtilisateur(utilisateur);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/AddUtilisateurUi.fxml"));
		loader.setController(this);
		
		try
		{
			Parent root = loader.load();
    		Scene scene = new Scene(root);
    		
    		URL url = Main.class.getClassLoader().getResource("style.css");
    		root.getStylesheets().clear();
    		root.getStylesheets().add(url.toURI().toString());
    		
    		Stage stage = new Stage();
    		stage.setTitle("Ajout Utilisateur");
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
		this.initChoixBox();
		
		if(this.getUtilisateur() != null)
		{
			ResultSet result = this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call checkUser(\"%s\")", this.getUtilisateur().getUser()));
			
			try
			{
				if(result.next())
				{
					role_Cb.setValue(result.getString("NOM_ROLEUTILISATEUR"));
					surname_Tf.setText(result.getString("NOM_UTILISATEUR"));
					name_Tf.setText(result.getString("PRENOM_UTILISATEUR"));
					email_Tf.setText(result.getString("EMAIL_UTILISATEUR"));
					adresse_Tf.setText(result.getString("ADRESSE_UTILISATEUR"));
					tel_Tf.setText(result.getString("TEL_UTILISATEUR"));
					user_Tf.setText(result.getString("USER_UTILISATEUR"));
					password_Tf.setText(result.getString("PASSWORD_UTILISATEUR"));
					role_Cb.setValue(result.getString("NOM_ROLEUTILISATEUR"));
				}
			}
			catch (SQLException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
	}
	
	/**
	 * Initialize the roles choicebox.
	 */
	public void initChoixBox()
	{
		ResultSet result = this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().querySelectAll("roleutilisateur");
		
		try
		{
			while(result.next())
				role_Cb.getItems().add(result.getString("NOM_ROLEUTILISATEUR"));
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Check that the important fields have been correctly completed.
	 * 
	 * @return True if the fields are correctly completed, false if not.
	 */
	public boolean checkTexfield()
	{
		error_Lb.setText("");
		String tempoSurname = String.valueOf(surname_Tf.getText());
		String tempoUser = String.valueOf(user_Tf.getText());
		String tempoPassword = String.valueOf(password_Tf.getText());
		String tempoRole = String.valueOf(role_Cb.getSelectionModel().getSelectedItem());
		
		if(tempoSurname.equals("") || tempoUser.equals("") || tempoPassword.equals("") || tempoRole.equals("") )
		{
			error_Lb.setText("Vous avez oubliez un champ important !");
			return false;
		}
		
		return true;
	}
	
	/**
	 * Adds or modifies a user according to the parameters previously sent to the window.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void validation(ActionEvent event)
	{
		error_Lb.setText("");
		
		if(this.getUtilisateur() == null)
		{
			addUser(event);
		}
		else
		{
			updateUser(event);
		}
	}
	
	/**
	 * Add a new user to the database.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void addUser(ActionEvent event)
	{
		ResultSet result = this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call checkUser(\"%s\")", user_Tf.getText()));
		
		try
		{
			if(!result.next())
			{
				if(this.checkTexfield())
				{
					this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().executionQuery(
							String.format("call createUtilisateur(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")",
							role_Cb.getSelectionModel().getSelectedItem(),
							surname_Tf.getText(),
							name_Tf.getText(),
							email_Tf.getText(),
							adresse_Tf.getText(),
							tel_Tf.getText(),
							user_Tf.getText(),
							password_Tf.getText()
							));
					
					this.getUsersPageCtrl().initTableView();
					((Node)event.getSource()).getScene().getWindow().hide();
				}
			}
			else
			{
				error_Lb.setText("L'utilisateur existe déjà veuillez changer de user merci.");
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Modify the selected user.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void updateUser(ActionEvent event)
	{
		ResultSet result = this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call checkUser(\"%s\")", user_Tf.getText()));
		
		try
		{
			if(!result.next())
			{
				if(this.checkTexfield())
				{
					this.getUsersPageCtrl().getPrincipalUiCtrl().getDatabase().executionQuery(
							String.format("call updateUtilisateur(\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",\"%s\")",
							this.getUtilisateur().getId(),
							role_Cb.getSelectionModel().getSelectedItem(),
							surname_Tf.getText(),
							name_Tf.getText(),
							email_Tf.getText(),
							adresse_Tf.getText(),
							tel_Tf.getText(),
							user_Tf.getText(),
							password_Tf.getText()
							));
					
					this.getUsersPageCtrl().initTableView();
					((Node)event.getSource()).getScene().getWindow().hide();
				}
			}
			else
			{
				error_Lb.setText("L'utilisateur existe déjà veuillez changer de user merci.");
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Cancel the action and return to the user management window.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void cancel(ActionEvent event) {
		((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	public UsersPage getUsersPageCtrl() {
		return usersPageCtrl;
	}
	
	public void setUsersPageCtrl(UsersPage gestionUtiisateurUiController) {
		this.usersPageCtrl = gestionUtiisateurUiController;
	}

	public Utilisateur getUtilisateur() {
		return utilisateur;
	}

	public void setUtilisateur(Utilisateur utilisateur) {
		this.utilisateur = utilisateur;
	}
}
