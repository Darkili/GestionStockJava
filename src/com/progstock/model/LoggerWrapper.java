package com.progstock.model;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * <b>LoggerWrapper is the class for generating logs.</b>
 */
public class LoggerWrapper {

	/**
	 * A static instance of the java built-in logger class.
	 */
	public static final Logger myLogger = Logger.getLogger(LoggerWrapper.class.getName());
	 
	/**
	 * A static instance of the class itself.
	 */
	private static LoggerWrapper instance = null;
	
	/**
	 * Default constructor.
	 */
	public LoggerWrapper()
	{
		// Inutilisé.
	}
	
	/**
	 * Returns the class instance.
	 * 
	 * @return The static instance contained in the class.
	 */
	public static LoggerWrapper getInstance()
	{
		if(instance == null)
		{
			prepareLogger();
			instance = new LoggerWrapper();
		}
		
		return instance;
	}
	
	/**
	 * Prepare the file to host the logs.
	 * <p>
	 * Created a file for each month of the year.
	 * They are rewritten every year so that the system is not cluttered with files too large.
	 * </p>
	 */
	private static void prepareLogger()
	{
		FileHandler myFileHandler;
		
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;

		String fileName = (month+""+year+"-logs.txt");

		File appDataPath = new File(System.getProperty("user.home"),".GestionStock\\logs");

		for(File file : appDataPath.listFiles())
		{
			if(file.getName().regionMatches(0, String.valueOf(month), 0, 1))
			{
				if(!(file.getName().regionMatches(1, fileName, 1, 4)))
				{
					file.delete();
				}
			}
		}
		
		try
		{
			myFileHandler = new FileHandler(appDataPath+"\\"+fileName,true);
			myFileHandler.setFormatter(new SimpleFormatter());
	        myLogger.addHandler(myFileHandler);
	        myLogger.setUseParentHandlers(false);
	        myLogger.setLevel(Level.FINEST);
		}
		catch(SecurityException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the java built-in logger class instance.
	 * 
	 * @return The static instance of the java built-in logger class contained in the class.
	 */
	public Logger getMylogger() {
		return myLogger;
	}
}
