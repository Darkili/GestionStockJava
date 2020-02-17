package com.progstock.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;

import com.progstock.model.LoggerWrapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * <b>DetailsProduct is the class that represents the controller of the additional article details window.</b>
 */
public class DetailsProduct implements Initializable {

	private AnchorPane anchorPane;
	private PrincipalUi principalUiCtrl;
	private Integer idInfoSupp;
	
	private ObservableList<String> listLocation = FXCollections.observableArrayList();
	
	@FXML
	private TextField typeProduct_Tf;
	@FXML
	private TextField marqueProduct_Tf;
	@FXML
	private TextField modeleProduct_Tf;
	@FXML
	private TextField numSerieProduct_Tf;
	@FXML
	private TextField prixProduct_Tf;
	@FXML
	private ComboBox<String> etatProduct_Cb;
	@FXML
	private TextField codeBarre_Tf;
	@FXML
	private TextField refCommande_Tf;
	@FXML
	private ComboBox<String> typeInvestissement_Cb;
	@FXML
	private TextField prixInvestissement_Tf;
	@FXML
	private TextField anneeInvestissement_Tf;
	@FXML
	private TextField bdcInvestissement_Tf;
	@FXML
	private DatePicker dateLivraison_Dp;
	@FXML
	private DatePicker dateMiseService_Dp;
	@FXML
	private ComboBox<String> localisation_Cb;
	@FXML
	private TextField responsable_Tf;
	@FXML
	private ComboBox<String> typeAssurance_Cb;
	@FXML
	private TextField numPoliceAssurance_Tf;
	@FXML
	private TextField ctrlSIPPT_Tf;
	@FXML
	private ComboBox<String> avisSIPPT_Cb;
	@FXML
	private DatePicker dateAvisSIPPT_Dp;
	@FXML
	private TextField remarqueSIPPT_Tf;
	@FXML
	private DatePicker dateEntretient_Dp;
	@FXML
	private TextField numFactureEntretient_Tf;
	@FXML
	private TextField remarqueEntretient_Tf;
	@FXML
	private DatePicker dateDemandeDeclassement_Dp;
	@FXML
	private DatePicker dateAvisFavorable_Dp;
	@FXML
	private DatePicker dateEnlevement_Dp;
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param principalUi
	 * 		The main window controller.
	 */
	public DetailsProduct(PrincipalUi principalUi)
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/progstock/view/DetailsProductUi.fxml"));
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
	public void initialize(URL url, ResourceBundle rb) {
		
		ObservableList<String> listEtat = FXCollections.observableArrayList();
		listEtat.add("Mauvais");
		listEtat.add("Bon");
		listEtat.add("Excellent");
		etatProduct_Cb.setItems(listEtat);
		
		ObservableList<String> listTypeInvestissement = FXCollections.observableArrayList();
		listTypeInvestissement.add("FFT");
		listTypeInvestissement.add("AFT");
		listTypeInvestissement.add("FFA");
		typeInvestissement_Cb.setItems(listTypeInvestissement);
		
		ObservableList<String> listTypeAssurance = FXCollections.observableArrayList();
		listTypeAssurance.add("Ville");
		listTypeAssurance.add("Ecole");
		typeAssurance_Cb.setItems(listTypeAssurance);
		
		ObservableList<String> listAvisSIPPT = FXCollections.observableArrayList();
		listAvisSIPPT.add("Vert");
		listAvisSIPPT.add("Orange");
		listAvisSIPPT.add("Rouge");
		avisSIPPT_Cb.setItems(listAvisSIPPT);
		
		initComboBoxLocation();
	}
	
	/**
	 * Initializes the combobox of the locations.
	 */
	public void initComboBoxLocation()
	{
		listLocation.clear();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().querySelectAll(
				"emplacement",String.format("emplacement.DESCRIPTION like '%s%%'",localisation_Cb.getEditor().getText().toUpperCase()));
		
		try
		{
			while(result.next())
			{
				listLocation.add(result.getString("DESCRIPTION"));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		localisation_Cb.setItems(listLocation);
		localisation_Cb.setValue("");
	}
	
	/**
	 * Loads additional details of the sent article.
	 * 
	 * @param id
	 * 		The article identifier.
	 */
	public void loadData(Integer id)
	{
		this.setIdInfoSupp(id);
		
		this.getPrincipalUiCtrl().getDatabase().connexion();
		ResultSet result = this.getPrincipalUiCtrl().getDatabase().executionQuery(String.format("call infosupp(\"%s\")",id));
		
		try
		{
			while(result.next())
			{
				typeProduct_Tf.setText(result.getString("NOM_TYPEARTICLE"));
				marqueProduct_Tf.setText(result.getString("MARQUE"));
				modeleProduct_Tf.setText(result.getString("MODELE"));
				numSerieProduct_Tf.setText(result.getString("NUMSERIE"));
				prixProduct_Tf.setText(result.getString("PRIX"));
				etatProduct_Cb.setValue(result.getString("ETAT"));
				codeBarre_Tf.setText(result.getString("DESIGN_CODEBARRE"));
				refCommande_Tf.setText(result.getString("REFERENCE_COMMANDE"));
				typeInvestissement_Cb.setValue(result.getString("TYPEINVESTISSEMENT"));
				prixInvestissement_Tf.setText(result.getString("PRIXINVESTISSEMENT"));
				anneeInvestissement_Tf.setText(result.getString("ANNEEINVESTISSEMENT"));
				bdcInvestissement_Tf.setText(result.getString("NUMBDC"));
				dateLivraison_Dp.setValue(dateFormatter(result.getString("DATELIVRAISON")));
				dateMiseService_Dp.setValue(dateFormatter(result.getString("DATEMISESERVICE")));
				localisation_Cb.setValue(result.getString("DESCRIPTION"));
				responsable_Tf.setText(result.getString("RESPONSABLE"));
				typeAssurance_Cb.setValue(result.getString("TYPEASSURANCE"));
				numPoliceAssurance_Tf.setText(result.getString("NUMASSURANCE"));
				ctrlSIPPT_Tf.setText(result.getString("CONTRÔLESIPPT"));
				avisSIPPT_Cb.setValue(result.getString("AVISSIPPT"));
				dateAvisSIPPT_Dp.setValue(dateFormatter(result.getString("DATEAVISSIPPT")));
				remarqueSIPPT_Tf.setText(result.getString("REMARQUESIPPT"));
				dateEntretient_Dp.setValue(dateFormatter(result.getString("DATEENTRETIEN")));
				numFactureEntretient_Tf.setText(result.getString("NUMBDCENTRETIEN"));
				remarqueEntretient_Tf.setText(result.getString("REMARQUEENTRETIEN"));
				dateDemandeDeclassement_Dp.setValue(dateFormatter(result.getString("DATEDEMANDEDECLASSEMENT")));
				dateAvisFavorable_Dp.setValue(dateFormatter(result.getString("DATEAVISFAVORABLE")));
				dateEnlevement_Dp.setValue(dateFormatter(result.getString("DATEENLEVEMENT")));
			}
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Updates the additional details of the article.
	 */
	public void updateButton()
	{
		this.getPrincipalUiCtrl().getDatabase().connexion();

		this.getPrincipalUiCtrl().getDatabase().executionQuery(
		String.format("call alterInfosupp("
        + "%s,%s,%s,%s,%s,%s,%s,"
        + "%s,%s,%s,%s,%s,%s,%s,%s,"
        + "%s,%s,%s,%s,%s,%s,%s,%s,%s,%s);",
        this.getIdInfoSupp(),
        parceurNull(numSerieProduct_Tf.getText()),
        parceurNull(etatProduct_Cb.getValue()),
        parceurNull(typeInvestissement_Cb.getValue()),
        parceurNull(prixInvestissement_Tf.getText()),
        parceurNull(anneeInvestissement_Tf.getText()),
        parceurNull(bdcInvestissement_Tf.getText()),
        parceurNull(typeAssurance_Cb.getValue()),
        parceurNull(numPoliceAssurance_Tf.getText()),
        parceurNull(ctrlSIPPT_Tf.getText()),
        parceurNull(avisSIPPT_Cb.getValue()),
        parceurDateNull(dateAvisSIPPT_Dp.getValue()),
        parceurNull(remarqueSIPPT_Tf.getText()),
        parceurDateNull(dateEntretient_Dp.getValue()),
        parceurNull(numFactureEntretient_Tf.getText()),
        parceurNull(remarqueEntretient_Tf.getText()),
        parceurDateNull(dateLivraison_Dp.getValue()),
        parceurDateNull(dateMiseService_Dp.getValue()),
        parceurDateNull(dateDemandeDeclassement_Dp.getValue()),
        parceurDateNull(dateAvisFavorable_Dp.getValue()),
        parceurDateNull(dateEnlevement_Dp.getValue()),
        parceurNull(responsable_Tf.getText()),
        parceurNull(prixProduct_Tf.getText()),
        parceurNull(marqueProduct_Tf.getText()),
        parceurNull(modeleProduct_Tf.getText())
        ));
        
		// Updates the location.
        this.getPrincipalUiCtrl().getDatabase().executionQuery(
				String.format("call updateLocationArticle(\"%s\",\"%s\")",idInfoSupp,localisation_Cb.getValue()));
        
        this.getPrincipalUiCtrl().getInvPageCtrl().initCustomListTableView();
    }
	
	/**
	 * Returns to the inventory page.
	 */
	public void returnButtonClick() {
		this.getPrincipalUiCtrl().getInvPageCtrl().displayItems();
		this.getPrincipalUiCtrl().inventoriesPageToFront();
	}
	
	/**
	 * Format the date to display it correctly in the interface.
	 * 
	 * @param dateString
	 * 		The date has been formatted.
	 * 
	 * @return The formatted date.
	 */
	public static final LocalDate dateFormatter(String dateString){
		
		if(dateString != null)
		{
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			return LocalDate.parse(dateString, formatter);
		}
		
	    return null;
	}
	
	/**
	 * Check the string and add quotes if necessary.
	 * 
	 * @param string
	 * 		The string that needs to be parsed.
	 * 
	 * @return The modified chain.
	 */
	public String parceurNull(String string)
    {
        String tempo = String.valueOf(string);
        
        if(!tempo.equals("null"))
        {
            tempo = "'"+string+"'";
            
            if(string.equals(""))
    		{
    			tempo = null;
    		}
            
            return tempo;
        }
        else return tempo;
    }
	
	/**
	 * Check the date and add quotes if necessary.
	 * 
	 * @param localdate
	 * 		The date that needs to be parsed.
	 * 
	 * @return The modified date.
	 */
	public String parceurDateNull(LocalDate localdate)
    {
        String tempo = String.valueOf(localdate);
        
        if(!tempo.equals("null"))
        {
            tempo = "'"+tempo+"'";
            return tempo;
        }
        else return tempo;
    }
	
	public Integer getIdInfoSupp() {
		return idInfoSupp;
	}

	public void setIdInfoSupp(Integer idInfoSupp) {
		this.idInfoSupp = idInfoSupp;
	}

	public PrincipalUi getPrincipalUiCtrl() {
		return principalUiCtrl;
	}

	public void setPrincipalUiCtrl(PrincipalUi principalUiCtrl) {
		this.principalUiCtrl = principalUiCtrl;
	}

	public AnchorPane getAnchorPane() {
		return anchorPane;
	}

	public void setAnchorPane(AnchorPane anchorPane) {
		this.anchorPane = anchorPane;
	}

	public String getTypeProduct_Tf() {
		return typeProduct_Tf.getText();
	}

	public void setTypeProduct(String value) {
		this.typeProduct_Tf.setText(value);
	}

	public String getMarqueProduct() {
		return marqueProduct_Tf.getText();
	}

	public void setMarqueProduct(String value) {
		this.marqueProduct_Tf.setText(value);
	}

	public String getModeleProduct() {
		return modeleProduct_Tf.getText();
	}

	public void setModeleProduct(String value) {
		this.modeleProduct_Tf.setText(value);
	}

	public String getNumSerieProduct() {
		return numSerieProduct_Tf.getText();
	}

	public void setNumSerieProduct(String value) {
		this.numSerieProduct_Tf.setText(value);
	}

	public String getPrixProduct() {
		return prixProduct_Tf.getText();
	}

	public void setPrixProduct(String value) {
		this.prixProduct_Tf.setText(value);
	}

	public String getEtatProduct() {
		return etatProduct_Cb.getValue();
	}

	public void setEtatProduct(String value) {
		this.etatProduct_Cb.setValue(value);
	}
	
	public String getCodeBarre() {
		return codeBarre_Tf.getText();
	}

	public void setCodeBarre(String value) {
		this.codeBarre_Tf.setText(value);
	}
	
	public String getRefCommande() {
		return refCommande_Tf.getText();
	}

	public void setRefCommande(String value) {
		this.refCommande_Tf.setText(value);
	}

	public String getTypeInvestissement() {
		return typeInvestissement_Cb.getValue();
	}

	public void setTypeInvestissement(String value) {
		this.typeInvestissement_Cb.setValue(value);
	}

	public String getPrixInvestissement() {
		return prixInvestissement_Tf.getText();
	}

	public void setPrixInvestissement(String value) {
		this.prixInvestissement_Tf.setText(value);
	}

	public String getAnneeInvestissement() {
		return anneeInvestissement_Tf.getText();
	}

	public void setAnneeInvestissement(String value) {
		this.anneeInvestissement_Tf.setText(value);
	}

	public String getBdcInvestissement() {
		return bdcInvestissement_Tf.getText();
	}

	public void setBdcInvestissement(String value) {
		this.bdcInvestissement_Tf.setText(value);
	}

	public String getDateLivraison() {
		return String.valueOf(dateLivraison_Dp.getValue());
	}

	public void setDateLivraison(String value) {
		this.dateLivraison_Dp.setValue(LocalDate.parse(value));
	}

	public String getDateMiseService() {
		return String.valueOf(dateMiseService_Dp.getValue());
	}

	public void setDateMiseService(String value) {
		this.dateMiseService_Dp.setValue(LocalDate.parse(value));
	}

	public String getLocalisation() {
		return localisation_Cb.getValue();
	}

	public void setLocalisation(String value) {
		this.localisation_Cb.setValue(value);
	}

	public String getResponsable() {
		return responsable_Tf.getText();
	}

	public void setResponsable(String value) {
		this.responsable_Tf.setText(value);
	}

	public String getTypeAssurance() {
		return typeAssurance_Cb.getValue();
	}

	public void setTypeAssurance(String value) {
		this.typeAssurance_Cb.setValue(value);
	}

	public String getNumPoliceAssurance() {
		return numPoliceAssurance_Tf.getText();
	}

	public void setNumPoliceAssurance(String value) {
		this.numPoliceAssurance_Tf.setText(value);
	}

	public String getCtrlSIPPT() {
		return ctrlSIPPT_Tf.getText();
	}

	public void setCtrlSIPPT(String value) {
		this.ctrlSIPPT_Tf.setText(value);
	}

	public String getAvisSIPPT() {
		return avisSIPPT_Cb.getValue();
	}

	public void setAvisSIPPT(String value) {
		this.avisSIPPT_Cb.setValue(value);
	}

	public String getDateAvisSIPPT() {
		return String.valueOf(dateAvisSIPPT_Dp.getValue());
	}

	public void setDateAvisSIPPT(String value) {
		this.dateAvisSIPPT_Dp.setValue(LocalDate.parse(value));
	}

	public String getRemarqueSIPPT() {
		return remarqueSIPPT_Tf.getText();
	}

	public void setRemarqueSIPPT(String value) {
		this.remarqueSIPPT_Tf.setText(value);
	}

	public String getDateEntretient() {
		return String.valueOf(dateEntretient_Dp.getValue());
	}

	public void setDateEntretient(String value) {
		this.dateEntretient_Dp.setValue(LocalDate.parse(value));
	}

	public String getNumFactureEntretient() {
		return numFactureEntretient_Tf.getText();
	}

	public void setNumFactureEntretient(String value) {
		this.numFactureEntretient_Tf.setText(value);
	}

	public String getRemarqueEntretient() {
		return remarqueEntretient_Tf.getText();
	}

	public void setRemarqueEntretient(String value) {
		this.remarqueEntretient_Tf.setText(value);
	}

	public String getDateDemandeDeclassement() {
		return String.valueOf(dateDemandeDeclassement_Dp.getValue());
	}

	public void setDateDemandeDeclassement(String value) {
		this.dateDemandeDeclassement_Dp.setValue(LocalDate.parse(value));
	}

	public String getDateAvisFavorable() {
		return String.valueOf(dateAvisFavorable_Dp.getValue());
	}

	public void setDateAvisFavorable(String value) {
		this.dateAvisFavorable_Dp.setValue(LocalDate.parse(value));
	}

	public String getDateEnlevement_Dp() {
		return String.valueOf(dateEnlevement_Dp.getValue());
	}

	public void setDateEnlevement_Dp(String value) {
		this.dateEnlevement_Dp.setValue(LocalDate.parse(value));
	}
}
