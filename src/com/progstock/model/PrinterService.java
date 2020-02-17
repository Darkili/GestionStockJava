package com.progstock.model;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.print.PrintService;
 
/**
 * <b>PrinterService is the class representing a printing service.</b>
 * <p>This service is used to print barcodes.</p>
 */
public class PrinterService implements Printable {
	
	private static String selectedPrinter;
	
	/**
	 * Default constructor.
	 */
	public PrinterService() {
		setSelectedPrinter("DYMO LabelWriter 450");
	}
	
	/**
	 * Returns the printer selected in the settings.
	 * 
	 * @return An instance of the selected printer.
	 */
	public static PrintService getPrinter()
	{
		for(PrintService printer : PrinterJob.lookupPrintServices())
		{
			if(printer.getName().equals(getSelectedPrinter()))
			{
				return printer;
			}
		}
		
		return null;
	}
	
	/**
	 * Triggers barcode printing.
	 * <p>
	 * This function retrieves the previously generated barcode. She centers it and sends it to the printer.
	 * </p>
	 */
	@Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
	{    
    	int result = NO_SUCH_PAGE;
    	
        if (pageIndex == 0)
        {
            graphics.translate((int)pageFormat.getImageableX(), (int)pageFormat.getImageableY());
            result = PAGE_EXISTS;

            BufferedImage read;
            
			try
			{
				File appData = new File(System.getProperty("user.home"),".GestionStock");
				
				read = ImageIO.read(new FileInputStream(appData+"\\Barcode.png"));
				int centerX = ((int)pageFormat.getPaper().getWidth() - (int)read.getWidth()) / 2;
				graphics.drawImage(read, centerX, 0, (int)pageFormat.getImageableWidth(), (int)pageFormat.getImageableHeight(), null);
			}
			catch (IOException e)
			{
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
        }
        
        return result;
    }

	public static String getSelectedPrinter() {
		return selectedPrinter;
	}

	public static void setSelectedPrinter(String selectedPrinter) {
		PrinterService.selectedPrinter = selectedPrinter;
	}
}