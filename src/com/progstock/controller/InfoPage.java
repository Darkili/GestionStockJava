package com.progstock.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.LoggerWrapper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;

/**
 * <b>InfoPage is the class that represents the controller of the information window.</b>
 */
public class InfoPage implements Initializable {

	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	
	@FXML
	Hyperlink hyperlinkAuthorOne;
	@FXML
	Hyperlink hyperlinkAuthorTwo;
	@FXML
	Hyperlink hyperlinkAuthorThree;
	@FXML
	Hyperlink hyperlinkWebSiteOne;
	@FXML
	Hyperlink hyperlinkWebSiteTwo;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param principalUi
	 * 		The main window controller.
	 */
	public InfoPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/InfoPageUi.fxml"));
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
		// Creates a hyperlink to the specified address.
		// Opens the default browser to the address specified when clicking the link.
		
		hyperlinkAuthorOne.setOnAction(event -> {
		    try {
				Desktop.getDesktop().browse(URI.create("https://www.flaticon.com/authors/freepik"));
			} catch (IOException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		});
		
		hyperlinkAuthorTwo.setOnAction(event -> {
		    try {
				Desktop.getDesktop().browse(URI.create("https://www.flaticon.com/authors/chanut"));
			} catch (IOException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		});
		
		hyperlinkAuthorThree.setOnAction(event -> {
		    try {
				Desktop.getDesktop().browse(URI.create("https://www.fontsquirrel.com/fonts/cuprum"));
			} catch (IOException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		});
		
		hyperlinkWebSiteOne.setOnAction(event -> {
		    try {
				Desktop.getDesktop().browse(URI.create("https://www.flaticon.com/"));
			} catch (IOException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		});
		
		hyperlinkWebSiteTwo.setOnAction(event -> {
		    try {
				Desktop.getDesktop().browse(URI.create("https://www.fontsquirrel.com/"));
			} catch (IOException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		});
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
