package com.progstock.model;

/**
 * <b>User is the class representing a user of the program.</b>
 * <p>A user is characterized by the following information :</p>
 * <ul>
 * <li>A unique identifier definitively assigned.</li>
 * <li>A name that can be changed.</li>
 * <li>A first name that can be changed.</li>
 * <li>An email that can be changed.</li>
 * <li>An pseudo that can be changed.</li>
 * <li>A password that can be changed.</li>
 * <li>A role that can be changed.</li>
 * <li>A unique barcode definitively assigned.</li>
 * <li>A flag allowing to know, if the user has been deleted</li>
 * </ul>
 */
public class Utilisateur {
	
	private Integer id;
	private String nom;
	private String prenom;
	private String email;
	private String user;
	private String password;
	private String role;
	private String barcode;
	private Boolean disable;
	
	/**
	 * Default constructor.
	 */
	public Utilisateur() {
		
	}
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param nom
	 * 		The name of the user.
	 * @param password
	 * 		The password of the user.
	 */
	public Utilisateur(String nom,String password)
	{
		this.setNom(nom);
		this.setPassword(password);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String toString() {
		return String.format("%s %s %s %s %s %B",nom,prenom,user,password,role,disable);
		
	}
}
