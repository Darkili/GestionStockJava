package com.progstock.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;

import javax.imageio.ImageIO;
import javax.print.PrintService;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * <b>Barcode is the class representing barcodes.</b>
 * <p>A Barcode is characterized by the following information :</p>
 * <ul>
 * <li>The data contained in the barcode.</li>
 * <li>The format of the generated image.</li>
 * <li>The output path.</li>
 * <li>The width of the barcode.</li>
 * <li>The height of the barcode.</li>
 * </ul>
 */
public class Barcode
{
	private String data;
	private String format;
    private String output;
    private int width;
    private int height;
    
    /**
	 * Default constructor.
	 */
	public Barcode() {
		super();
		this.setData("Default");
		this.setFormat("png");
		this.setOutput("CodeBarre.png");
		this.setWidth(fromCMToPPI(5.7));
		this.setHeight(fromCMToPPI(3.2));
	}
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param data
	 * 		The data to be converted into barcode.
	 */
	public Barcode(String data) {
		super();
		this.setData(data);
		this.setFormat("png");
		this.setOutput(data + "." + format);
		this.setWidth(fromCMToPPI(5.7));
		this.setHeight(fromCMToPPI(3.2));
	}
	
	/**
	 * Sends the barcode to the selected printer for printing.
	 */
	public void print() {
		
		PrintService printer = PrinterService.getPrinter();
		
		if(printer != null)
		{
			PrinterJob pj = PrinterJob.getPrinterJob();
			
			try {
				pj.setPrintService(printer);
			} catch (PrinterException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
			
			PageFormat pf = pj.defaultPage();
	        Paper paper = pf.getPaper();
	        double width = fromCMToPPI(8.8);
	        double height = fromCMToPPI(3.5);
	        paper.setSize(width, height);
	        paper.setImageableArea(
	                        fromCMToPPI(0.25), 
	                        fromCMToPPI(0.5), 
	                        width - fromCMToPPI(0.25), 
	                        height - fromCMToPPI(0.5));
	        pf.setOrientation(PageFormat.PORTRAIT);
	        pf.setPaper(paper);
	        pj.setPrintable(new PrinterService(), pf);
	        
	        try {
				pj.print();
			} catch (PrinterException e) {
				LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
			}
		}
		else
		{
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Erreur d'impression");
			alert.setHeaderText("Imprimante non détectée.");
			alert.setContentText("L'imprimante sélectionnée dans les paramètres est introuvable.");
			alert.showAndWait();
		}
	}
	
	/**
	 * Saves the barcode at the specified location.
	 * 
	 * @param format
	 * 		The format of the generated image.
	 * @param output
	 * 		The output path.
	 */
	public void saveToImage(String format, String output)
	{
		saveToImage(format, output, true);
	}
	
	/**
	 * Saves the barcode to the specified location with the possibility to display the text under the barcode.
	 * 
	 * @param format
	 * 		The format of the generated image.
	 * @param output
	 * 		The output path.
	 * @param withText
	 * 		Including or excluding text under the barcode.
	 */
	public void saveToImage(String format, String output, boolean withText)
	{
		if(withText)
		{
			encodeWithText(data, format, output, width, height);
		}
		else
		{
			encodeWhitoutText(data, format, output, width, height);
		}
	}
	
	/**
	 * Generates a barcode with text below.
	 * 
	 * @param data
	 * 		The data contained in the barcode.
	 * @param format
	 * 		The format of the generated image.
	 * @param output
	 * 		The output path.
	 * @param width
	 * 		The width of the barcode.
	 * @param height
	 * 		The height of the barcode.
	 */
	private void encodeWithText(String data, String format, String output, int width, int height)
	{
        encodeWhitoutText(data, format, output, width, height);
		addTextToBarcode(data, format, output, output);
	}
	
	/**
	 * Generates a barcode without text below.
	 * 
	 * @param data
	 * 		The data contained in the barcode.
	 * @param format
	 * 		The format of the generated image.
	 * @param output
	 * 		The output path.
	 * @param width
	 * 		The width of the barcode.
	 * @param height
	 * 		The height of the barcode.
	 */
	public static void encodeWhitoutText(String data, String format, String output, int width, int height)
	{
		try
		{
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(data, BarcodeFormat.CODE_128, width, height);
            MatrixToImageWriter.writeToPath(matrix, format, Paths.get(output));
        }
		catch (WriterException | IOException e)
		{
        	LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
        }
	}
	
	/**
	 * Decodes a barcode as an image to retrieve the information it contains.
	 * 
	 * @param input
	 * 		The path to the image.
	 * 
	 * @return The data contained in the barcode in text form.
	 */
	public static String decodeFromImage(String input)
	{
		MultiFormatReader reader = new MultiFormatReader();
        BinaryBitmap binaryBitmap;
        Result data = null;
        
		try
		{
			binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(ImageIO.read(new FileInputStream(input)))));
			data = reader.decode(binaryBitmap);
			return data.getText();
		}
		catch (IOException | NotFoundException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
        
        return "";
	}
	
	/**
	 * Adds text below a barcode.
	 * 
	 * @param text
	 * 		The text to add.
	 * @param type
	 * 		The format of the selected image.
	 * @param input
	 * 		The path to the image.
	 * @param output
	 * 		The output path.
	 */
	private void addTextToBarcode(String text, String type, String input, String output)
	{
		try
		{
			BufferedImage image = ImageIO.read(new FileInputStream(output));
			
			// determine image type and handle correct transparency
	        int imageType = "png".equalsIgnoreCase(type) ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB;
	        BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight() + 32, imageType);
	        
	        // initializes necessary graphic properties
	        Graphics2D w = (Graphics2D) newImage.getGraphics();
	        w.setPaint(new Color(255,255,255));
	        w.fillRect(0,0,newImage.getWidth(),newImage.getHeight());
	        w.drawImage(image, 0, 0, null);
	        w.setColor(Color.BLACK);
	        w.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
	        FontMetrics fontMetrics = w.getFontMetrics();
	        Rectangle2D rect = fontMetrics.getStringBounds(text, w);

	        // calculate center of the image
	        int centerX = (image.getWidth() - (int) rect.getWidth()) / 2;
	        int centerY = image.getHeight() + 19;

	        // add text to the image
	        w.drawString(text, centerX, centerY);
	        ImageIO.write(newImage, type, Paths.get(input).toFile());
	        w.dispose();
		}
		catch (IOException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
    }
	
	/**
	 * Converted centimeters to pixels per inch.
	 * 
	 * @param cm
	 * 	The value in centimeters to convert.
	 * 
	 * @return The result of the conversion to pixel per inch.
	 */
	protected static int fromCMToPPI(double cm) {            
        return toPPI(cm * 0.393700787);            
    }
	
	/**
	 * Converted inch to pixel per inch.
	 * 
	 * @param inch
	 * 		The value in inches to convert.
	 * 
	 * @return The result of the conversion to pixel per inch.
	 */
    protected static int toPPI(double inch) {            
        return (int) (inch * 72d);            
    }

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
