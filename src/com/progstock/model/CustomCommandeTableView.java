package com.progstock.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;

/**
 * <b>CustomCommandTableView is the class that represents orders.</b>
 * <p>A CustomCommandeTableView is characterized by the following information :</p>
 * <ul>
 * <li>A unique identifier definitively assigned.</li>
 * <li>The date when the order was encoded.</li>
 * <li>The type of article.</li>
 * <li>The order reference.</li>
 * <li>The name of the supplier.</li>
 * <li>The name of the article.</li>
 * <li>The quantity of article that has been added.</li>
 * </ul>
 */
public class CustomCommandeTableView {

	private Integer id;
	private String date;
	private String type;
	private String reference;
	private String fournisseur;
	private String article;
	private String quantite;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
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
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getReference() {
		return reference;
	}
	
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getFournisseur() {
		return fournisseur;
	}
	
	public void setFournisseur(String fournisseur) {
		this.fournisseur = fournisseur;
	}
	
	public String getArticle() {
		return article;
	}
	
	public void setArticle(String article) {
		this.article = article;
	}
	
	public String getQuantite() {
		return quantite;
	}
	
	public void setQuantite(String quantite) {
		this.quantite = quantite;
	}
	
	public String toString() {
		return String.format("%s %s %s %s %s %s %n",date,type,reference,fournisseur,article,quantite);
	}
}
