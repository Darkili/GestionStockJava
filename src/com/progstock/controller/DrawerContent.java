package com.progstock.controller;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.LoggerWrapper;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <b>DrawerContent is the class that represents the controller of the menu management window.</b>
 */
public class DrawerContent implements Initializable {
	
	final String BASIC_BUTTON = "-fx-text-fill: #AFAFAF; -fx-background-color: transparent";
	final String SELECTED_BUTTON = "-fx-text-fill: #FFF; -fx-background-color: #3E3E3E";
	
	private PrincipalUi principalUiController;
	
	@FXML
	private ImageView imgInventories;
	@FXML
	private Button inventoriesButton;
	@FXML
	private ImageView imgOrders;
	@FXML
	private Button ordersButton;
	@FXML
	private ImageView imgMovement;
	@FXML
	private Button movementButton;
	@FXML
	private ImageView imgUser;
	@FXML
	private Button userButton;
	@FXML
	private ImageView imgPreferences;
	@FXML
	private Button preferencesButton;
	@FXML
	private ImageView imgInformation;
	@FXML
	private Button informationButton;
	@FXML
	private ImageView imgHelp;
	@FXML
	private Button helpButton;
	@FXML
	private ImageView imgDeconnexion;
	@FXML
	private Button deconnexionButton;
	
	/**
	 * Default constructor.
	 */
	public DrawerContent()
	{
		// The drawer is not displayed if an empty constructor is not declared.
	}

	/**
	 * Initialization function of the class.
	 * <p>This is automatically called when the fxml is loaded.</p>
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb)
	{
		URL urlImg1 = Main.class.getClassLoader().getResource("Images/box-package.png");
		URL urlImg2 = Main.class.getClassLoader().getResource("Images/delivery.png");
		URL urlImg3 = Main.class.getClassLoader().getResource("Images/delivery-packages.png");
		URL urlImg4 = Main.class.getClassLoader().getResource("Images/multiple-users-silhouette.png");
		URL urlImg5 = Main.class.getClassLoader().getResource("Images/settings-gears.png");
		URL urlImg6 = Main.class.getClassLoader().getResource("Images/info.png");
		URL urlImg7 = Main.class.getClassLoader().getResource("Images/question-mark.png");
		URL urlImg8 = Main.class.getClassLoader().getResource("Images/logout.png");
		
		try
		{
			this.imgInventories.setImage(new Image(urlImg1.toURI().toString()));
	        this.imgOrders.setImage(new Image(urlImg2.toURI().toString()));
	        this.imgMovement.setImage(new Image(urlImg3.toURI().toString()));
	        this.imgUser.setImage(new Image(urlImg4.toURI().toString()));
	        this.imgPreferences.setImage(new Image(urlImg5.toURI().toString()));
	        this.imgInformation.setImage(new Image(urlImg6.toURI().toString()));
	        this.imgHelp.setImage(new Image(urlImg7.toURI().toString()));
	        this.imgDeconnexion.setImage(new Image(urlImg8.toURI().toString()));
		}
		catch (URISyntaxException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Bring the inventory page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void inventoriesButtonClick() {
		principalUiController.inventoriesPageToFront();
		resetButton();
		inventoriesButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the orders page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void ordersButtonClick() {
		principalUiController.ordersPageToFront();
		resetButton();
		ordersButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the movements page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void movementButtonClick() {
		principalUiController.movementPageToFront();
		resetButton();
		movementButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the users page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void userButtonClick() {
		principalUiController.usersPageToFront();
		resetButton();
		userButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the settings page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void preferencesButtonClick() {
		principalUiController.settingsPageToFront();
		resetButton();
		preferencesButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the information page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 */
	public void informationButtonClick() {
		principalUiController.infoPageToFront();
		resetButton();
		informationButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Bring the help page to the foreground.
	 * <p>Also resets the effects applied to the other buttons.</p>
	 * <p>The button exists, but is hidden by default in the fxml.</p>
	 */
	public void helpButtonClick() {
		resetButton();
		helpButton.setStyle(SELECTED_BUTTON);
	}
	
	/**
	 * Disconnects the current user and returns to the login page.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	public void deconnexionButtonClick(ActionEvent event) {
		principalUiController.deconnexion(event);
	}
	
	/**
	 * Reset the effects applied to all buttons.
	 */
	public void resetButton() {
		inventoriesButton.setStyle(BASIC_BUTTON);
		movementButton.setStyle(BASIC_BUTTON);
		userButton.setStyle(BASIC_BUTTON);
		ordersButton.setStyle(BASIC_BUTTON);
		preferencesButton.setStyle(BASIC_BUTTON);
		informationButton.setStyle(BASIC_BUTTON);
		helpButton.setStyle(BASIC_BUTTON);
	}
	
	/**
	 * Disables access to the user management page.
	 */
	public void userButtonDisabled() {
		userButton.setDisable(true);
	}
	
	public void setPrincipalUiController(PrincipalUi puc) {
		principalUiController = puc;
	}
}
