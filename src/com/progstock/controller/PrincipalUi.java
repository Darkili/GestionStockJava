package com.progstock.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.progstock.model.DataBase;
import com.progstock.model.LoggerWrapper;
import com.progstock.model.Utilisateur;

import application.Main;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * <b>PrincipalUi is the class that represents the main window of the program.</b>
 * <p>PrincipalUi contains the different pages of the program, as well as their respective controller.</p>
 */
public class PrincipalUi implements Initializable {
	
	private Utilisateur currentUser;
	private DataBase database;
	
	private InventoryPage invPageCtrl;
	private MovementPage movPageCtrl;
	private OrdersPage ordPageCtrl;
	private UsersPage usersPageCtrl;
	private SettingsPage settingsPageCtrl;
	private InfoPage infoPageCtrl;
	private DetailsProduct detailsPageCtrl;
	
	private AnchorPane inventoryPage;
	private AnchorPane movementPage;
	private AnchorPane ordersPage;
	private AnchorPane usersPage;
	private AnchorPane settingsPage;
	private AnchorPane infoPage;
	private AnchorPane detailsPage;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private StackPane stackPane;
	@FXML
	private JFXHamburger hamburger;
	@FXML
	private JFXDrawer drawer;
	@FXML
	private Label title;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param currentUser
	 * 		The user currently logged in to the program.
	 */
	public PrincipalUi(Utilisateur currentUser)
	{
		this.setCurrentUser(currentUser);
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/PrincipalUi.fxml"));
		loader.setController(this);
        
        try 
        {
        	Parent root = loader.load();
    		Scene scene = new Scene(root);
    		anchorPane = (AnchorPane) root;
    		
    		URL url = Main.class.getClassLoader().getResource("style.css");
    		root.getStylesheets().clear();
    		root.getStylesheets().add(url.toURI().toString());
    		
    		Stage stage = new Stage();
    		stage.setTitle("GestionStock");
    		stage.initModality(Modality.WINDOW_MODAL);
    		stage.setScene(scene);
    		stage.centerOnScreen();
    		stage.show();
    		
    		root.requestFocus();
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
		database = new DataBase("jdbc:mysql://localhost:3306/gestionstock?useSSL=true","root","Secret123");
		
		try
		{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/DrawerContentUi.fxml"));
			DrawerContent drawCtrl = new DrawerContent();
			loader.setController(drawCtrl);
			VBox box = loader.load();
			
			drawCtrl.setPrincipalUiController(this);
			drawer.setSidePane(box);
			
			HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
	        transition.setRate(-1);
	        
	        menuHamburgerOpen(transition);
	        
	        hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> menuHamburgerOpen(transition));
	        
	        this.invPageCtrl = new InventoryPage(this);
	        this.movPageCtrl = new MovementPage(this);
	        this.ordPageCtrl = new OrdersPage(this);
	        this.usersPageCtrl = new UsersPage(this);
	        this.settingsPageCtrl = new SettingsPage(this);
	        this.infoPageCtrl = new InfoPage(this);
	        this.detailsPageCtrl = new DetailsProduct(this);
			
	        this.inventoryPage = invPageCtrl.getAnchorPane();
	        this.movementPage = movPageCtrl.getAnchorPane();
	        this.ordersPage = ordPageCtrl.getAnchorPane();
	        this.usersPage = usersPageCtrl.getAnchorPane();
	        this.settingsPage = settingsPageCtrl.getAnchorPane();
	        this.infoPage = infoPageCtrl.getAnchorPane();
	        this.detailsPage = detailsPageCtrl.getAnchorPane();
			
			stackPane.getChildren().addAll(detailsPage, infoPage, settingsPage, usersPage, movementPage, inventoryPage,ordersPage);
			
			if(!getCurrentUser().getRole().equals("Admin"))
				drawCtrl.userButtonDisabled();
			
			settingsPageCtrl.loadSettings();
			
			setTitle("GestionStock");
			
			drawCtrl.inventoriesButtonClick();
			
		}
		catch (IOException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
	    }
	}
	
	/**
	 * Manages the opening and closing of the dynamic menu.
	 * 
	 * @param transition
	 * 		The class that manages the transistion effect of the button.
	 */
	public void menuHamburgerOpen(HamburgerBackArrowBasicTransition transition) {
		
		transition.setRate(transition.getRate()*-1);
        transition.play();
        
        KeyValue widthValue = null;
        
        if(drawer.isShown())
        {
        	widthValue = new KeyValue(drawer.prefWidthProperty(), drawer.getWidth() - 200);
            drawer.close();
        }
        else
        {
        	widthValue = new KeyValue(drawer.prefWidthProperty(), drawer.getWidth() + 200);
            drawer.open();
        }
        
        KeyFrame frame = new KeyFrame(Duration.seconds(0.40), widthValue);
        Timeline timeline = new Timeline(frame);
        timeline.play();
	}
	
	/**
	 * Disconnects the current user and returns to the login page.
	 * 
	 * @param event
	 * 		Automatically generate when a button is clicked.
	 */
	@FXML
	public void deconnexion(ActionEvent event) {
		
		new ConnectionUi();
		
		// Retrieves the source of the event and closes the window that contains it.
        ((Node)event.getSource()).getScene().getWindow().hide();
	}
	
	/**
	 * Bring the inventory page to the foreground.
	 */
	public void inventoriesPageToFront() {
		fadeEffect(inventoryPage);
		inventoryPage.toFront();
	}
	
	/**
	 * Bring the orders page to the foreground.
	 */
	public void ordersPageToFront() {
		fadeEffect(ordersPage);
		ordersPage.toFront();
	}
	
	/**
	 * Bring the movements page to the foreground.
	 */
	public void movementPageToFront() {
		fadeEffect(movementPage);
		movementPage.toFront();
	}
	
	/**
	 * Bring the users page to the foreground.
	 */
	public void usersPageToFront() {
		fadeEffect(usersPage);
		usersPage.toFront();
	}
	
	/**
	 * Bring the settings page to the foreground.
	 */
	public void settingsPageToFront() {
		fadeEffect(settingsPage);
		settingsPage.toFront();
	}
	
	/**
	 * Bring the information page to the foreground.
	 */
	public void infoPageToFront() {
		fadeEffect(infoPage);
		infoPage.toFront();
	}
	
	/**
	 * Bring the additional details page to the foreground.
	 */
	public void detailsPageToFront() {
		fadeEffect(detailsPage);
		detailsPage.toFront();
	}
	
	/**
	 * Generates a transition effect when switching pages.
	 * 
	 * @param page
	 * 		The pane to which you want to apply the effect.
	 */
	public void fadeEffect(AnchorPane page) {
		
		ObservableList<Node> childs = stackPane.getChildren();
    	Node topNode = childs.get(childs.size()-1);
    	
		if(!topNode.equals(page))
		{
			FadeTransition ft = new FadeTransition(Duration.millis(300), page);
		     ft.setFromValue(0);
		     ft.setToValue(1);
		     ft.setCycleCount(1);
		     ft.play();
		}
	}
	
	public void setTitle(String value) {
		title.setText(value);
	}
	
	public PrincipalUi getController() {
		return this;
	}

	public AnchorPane getOrdersPage() {
		return ordersPage;
	}

	public void setOrdersPage(AnchorPane ordersPage) {
		this.ordersPage = ordersPage;
	}

	public DataBase getDatabase() {
		return database;
	}

	public void setDatabase(DataBase database) {
		this.database = database;
	}

	public Utilisateur getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(Utilisateur currentUser) {
		this.currentUser = currentUser;
	}

	public InventoryPage getInvPageCtrl() {
		return invPageCtrl;
	}

	public void setInvPageCtrl(InventoryPage invPageCtrl) {
		this.invPageCtrl = invPageCtrl;
	}

	public MovementPage getMovPageCtrl() {
		return movPageCtrl;
	}

	public void setMovPageCtrl(MovementPage movPageCtrl) {
		this.movPageCtrl = movPageCtrl;
	}

	public OrdersPage getOrdPageCtrl() {
		return ordPageCtrl;
	}

	public void setOrdPageCtrl(OrdersPage ordPageCtrl) {
		this.ordPageCtrl = ordPageCtrl;
	}

	public UsersPage getUsersPageCtrl() {
		return usersPageCtrl;
	}

	public void setUsersPageCtrl(UsersPage usersPageCtrl) {
		this.usersPageCtrl = usersPageCtrl;
	}

	public SettingsPage getSettingsPageCtrl() {
		return settingsPageCtrl;
	}

	public void setSettingsPageCtrl(SettingsPage settingsPageCtrl) {
		this.settingsPageCtrl = settingsPageCtrl;
	}

	public InfoPage getInfoPageCtrl() {
		return infoPageCtrl;
	}

	public void setInfoPageCtrl(InfoPage infoPageCtrl) {
		this.infoPageCtrl = infoPageCtrl;
	}

	public DetailsProduct getDetailsPageCtrl() {
		return detailsPageCtrl;
	}

	public void setDetailsPageCtrl(DetailsProduct detailsPageCtrl) {
		this.detailsPageCtrl = detailsPageCtrl;
	}
}
