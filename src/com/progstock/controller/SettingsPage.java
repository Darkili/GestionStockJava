package com.progstock.controller;

import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Level;

import javax.print.PrintService;

import com.progstock.model.LoggerWrapper;
import com.progstock.model.PrinterService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;

/**
 * <b>SettingsPage is the class that represents the information window controller.</b>
 */
public class SettingsPage implements Initializable {

	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	
	ObservableList<String> listPrinter = FXCollections.observableArrayList();
	
	@FXML
	private ComboBox<String> printer_Cb;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param principalUi
	 * 		The main window controller.
	 */
	public SettingsPage(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/SettingsPageUi.fxml"));
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
		PrinterService.setSelectedPrinter("DYMO LabelWriter 450");
		printer_Cb.setValue(PrinterService.getSelectedPrinter());
	}
	
	/**
	 * Updates the list of available printers.
	 */
	public void refreshPrinterList()
	{
		listPrinter.clear();
		
		PrintService[] printService = PrinterJob.lookupPrintServices();
		
		for (PrintService printer : printService)
		{
			listPrinter.add(printer.getName());
		}
		
		printer_Cb.setItems(listPrinter);
	}
	
	/**
	 * Saves the program parameters in a file.
	 */
	public void saveSettings()
	{
		FileWriter fileWriter = null;
		BufferedWriter bufferedWriter = null;
		
		try
		{
			File appData = new File(System.getProperty("user.home"),".GestionStock");
			
			fileWriter = new FileWriter(appData+"\\settings.txt");
			bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(PrinterService.getSelectedPrinter());
			bufferedWriter.close();
		}
		catch (IOException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Loads the program parameters from a file.
	 */
	public void loadSettings()
	{
		FileReader fileReader = null;
		BufferedReader bufferedReader = null;
		
		try
		{
			if(Files.exists(Paths.get("settings.txt")))
			{
				File appData = new File(System.getProperty("user.home"),".GestionStock");
				
				fileReader = new FileReader(appData+"\\settings.txt");
				bufferedReader = new BufferedReader(fileReader);
				PrinterService.setSelectedPrinter(bufferedReader.readLine());
				bufferedReader.close();
				
				printer_Cb.setValue(PrinterService.getSelectedPrinter());
			}
		}
		catch (IOException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Saves the printer selected in the combox.
	 */
	public void selectPrinter()
	{
		PrinterService.setSelectedPrinter(printer_Cb.getValue());
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
