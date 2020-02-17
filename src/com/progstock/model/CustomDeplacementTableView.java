package com.progstock.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 * <b>CustomDeplacementTableView is the class that represents the movements of articles in internal.</b>
 * <p>A CustomDeplacementTableView is characterized by the following information :</p>
 * <ul>
 * <li>The barcode of the article.</li>
 * <li>The name of the article.</li>
 * <li>The person that borrowed the article.</li>
 * <li>The condition of the article (Borrowed or returned).</li>
 * <li>The movement date of the article.</li>
 * </ul>
 */
public class CustomDeplacementTableView {
	
	private String codeBarre;
	private String objet;
	private String personne;
	private String etat;
	private String date;
	
	public String getCodeBarre() {
		return codeBarre;
	}
	
	public void setCodeBarre(String codeBarre) {
		this.codeBarre = codeBarre;
	}
	
	public String getObjet() {
		return objet;
	}
	
	public void setObjet(String objet) {
		this.objet = objet;
	}
	
	public String getPersonne() {
		return personne;
	}
	
	public void setPersonne(String personne) {
		this.personne = personne;
	}
	
	public String getEtat() {
		return etat;
	}
	
	public void setEtat(String etat) {
		this.etat = etat;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	/**
	 * Converts the date into a European format.
	 * 
	 * @param date
	 * 		The date to convert.
	 */
	public void setDateFormat(String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			this.date = formatter2.format(formatter.parse(date));
		} catch (ParseException e) {
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
}
