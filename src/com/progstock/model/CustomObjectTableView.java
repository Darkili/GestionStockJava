package com.progstock.model;

/**
 * <b>CustomObjectTableView is the class that represents unique items.</b>
 * <p>A CustomObjectTableView is characterized by the following information :</p>
 * <ul>
 * <li>A unique identifier definitively assigned.</li>
 * <li>A unique identifier giving access to additional information.</li>
 * <li>The type of article.</li>
 * <li>The name of the article.</li>
 * <li>The location of the article.</li>
 * <li>The unique code assigned to the article.</li>
 * <li>A unique identifier that links the article to a movement in stock.</li>
 * <li>A unique barcode assigned to the article.</li>
 * <li>The reference of the order to which the article belongs.</li>
 * </ul>
 */
public class CustomObjectTableView {
	
	private Integer idProduct;
	private Integer idInfoSupp;
	private String familyProduct;
	private String nameProduct;
	private String location;
	private String codeProduct;
	private String movement;
	private String barcode;
	private String refCommande;
	private String note;
	private Boolean disable;
	
	public Integer getIdProduct() {
		return idProduct;
	}

	public void setIdProduct(Integer idProduct) {
		this.idProduct = idProduct;
	}
	

	public Integer getIdInfoSupp() {
		return idInfoSupp;
	}

	public void setIdInfoSupp(Integer idInfoSupp) {
		this.idInfoSupp = idInfoSupp;
	}

	public String getFamilyProduct() {
		return familyProduct;
	}
	
	public void setFamilyProduct(String familyProduct) {
		this.familyProduct = familyProduct;
	}
	
	public String getNameProduct() {
		return nameProduct;
	}
	
	public void setNameProduct(String nameProduct) {
		this.nameProduct = nameProduct;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getCodeProduct() {
		return codeProduct;
	}
	
	public void setCodeProduct(String codeProduct) {
		this.codeProduct = codeProduct;
	}
	
	public String getMovement() {
		return movement;
	}
	
	public void setMovement(String movement) {
		this.movement = movement;
	}
	
	public String getBarcode() {
		return barcode;
	}
	
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	public String getRefCommande() {
		return refCommande;
	}

	public void setRefCommande(String refCommande) {
		this.refCommande = refCommande;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String toString()
	{
		return String.format("%s %s %s %s %s %s %n",familyProduct,nameProduct,location,codeProduct,movement,barcode);
	}
}
