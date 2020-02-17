package com.progstock.model;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * <b>DataBase is the class representing the database.</b>
 * <p>A DataBase is characterized by the following information :</p>
 * <ul>
 * <li>An instance of connection to the database.</li>
 * <li>An sql request statement.</li>
 * <li>An sql request.</li>
 * <li>The access path to the database.</li>
 * <li>The username to connect to the database.</li>
 * <li>The password to connect to the database.</li>
 * </ul>
 */
public class DataBase
{
	private Connection connection;
	private Statement statement;
	private String sql;
	private String url;
	private String username;
	private String password;
	
	/**
	 * Default constructor.
	 */
	public DataBase() {
		
	}
	
	/**
	 * Constructor with some initial data.
	 * 
	 * @param url
	 * 		The access path to the database.
	 * @param username
	 * 		The username to connect to the database.
	 * @param password
	 * 		The password to connect to the database.
	 */
	public DataBase(String url, String username, String password)
	{
		this.url = url;
		this.username = username;
		this.password = password;	
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = (Connection) DriverManager.getConnection(url,username,password);
		}
		catch (Exception e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
	}
	
	/**
	 * Set up a connection with the database.
	 * 
	 * @return An instance of connection to the database.
	 */
	public Connection connexion()
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			this.connection = (Connection) DriverManager.getConnection(url,username,password);
		}
		catch (Exception e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		return connection;
	}
	
	/**
	 * Closes a connection with the database.
	 * 
	 * @return An instance of connection to the database.
	 */
	public Connection closeConnexion()
	{
		try
		{
			connection.close();
		}
		catch (Exception e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		return connection;
	}
	
	/**
	 * Executes a sql query that does not modify the database.
	 * 
	 * @param sql
	 * 		An sql request.
	 * 
	 * @return The result of the query.
	 */
	public ResultSet executionQuery(String sql)
	{
		//connexion();
		ResultSet resultSet = null;
		
		try
		{
			
			statement =  (Statement) connection.createStatement();
			//System.out.println(sql);
			resultSet = statement.executeQuery(sql);
			
		}
		catch (SQLException e)
		{
			LoggerWrapper.getInstance().getMylogger().log(Level.SEVERE,e.toString(),e);
		}
		
		return resultSet;
	}
	
	/**
	 * Executes a sql query that modifies the database.
	 * 
	 * @param sql
	 * 		An sql request.
	 * 
	 * @return The result of the query.
	 */
	public String executionUpdate(String sql)
	{
		connexion();
		String result = "";
		
		try
		{
			statement = (Statement) connection.createStatement();
			statement.executeUpdate(sql);
			result = sql;
		}
		catch (SQLException ex)
		{
			result = ex.toString();
		}
		
		return result;
	}
	
	/**
	 * Retrieves all data from a table.
	 * 
	 * @param nomTable
	 * 		The name of the table to retrieve.
	 * 
	 * @return The result of the query.
	 */
	public ResultSet querySelectAll(String nomTable)
	{
		connexion();
		sql = "SELECT * FROM " + nomTable;
		//System.out.println(sql);
		return this.executionQuery(sql);
	}
	
	/**
	 * Retrieves all data from a table by adding a condition.
	 * 
	 * @param nomTable
	 * 		The name of the table to retrieve.
	 * @param etat
	 * 		The condition of sorting.
	 * 
	 * @return The result of the query.
	 */
	public ResultSet querySelectAll(String nomTable,String etat)
	{
		connexion();
		sql = "SELECT * FROM " + nomTable +/*" inner join ROLEUTILISATEUR using (ID_ROLEUTILISATEUR)"*/ " WHERE " + etat;
		return this.executionQuery(sql);
	}
	
	/**
	 * Retrieves data from a table, only for selected columns.
	 * 
	 * @param nomColonne
	 * 		An array containing the names of the selected columns.
	 * @param nomTable
	 * 		The name of the table to retrieve.
	 * 
	 * @return The result of the query.
	 */
	public ResultSet querySelect(String[] nomColonne, String nomTable)
	{
		connexion();
		StringBuilder bld = new StringBuilder();
		
		bld.append("SELECT ");
		
		for(int i = 0 ; i <= nomColonne.length - 1 ; i++)
		{
			bld.append(nomColonne[i]);
			
			if(i < nomColonne.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(" FROM " + nomTable);
		return this.executionQuery(bld.toString());
	}
	
	/**
	 * Retrieves data from a table, only for selected columns by adding a condition.
	 * 
	 * @param nomColonne
	 * 		An array containing the names of the selected columns.
	 * @param nomTable
	 * 		The name of the table to retrieve.
	 * @param etat
	 * 		The condition of sorting.
	 * 
	 * @return The result of the query.
	 */
	public ResultSet querySelect(String[] nomColonne, String nomTable, String etat)
	{
		connexion();
		StringBuilder bld = new StringBuilder();
		
		bld.append("SELECT ");
		
		for(int i = 0 ; i <= nomColonne.length - 1 ; i++)
		{
			bld.append(nomColonne[i]);
			
			if(i < nomColonne.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(" FROM " + nomTable + " WHERE " + etat);
		return this.executionQuery(bld.toString());
	}
	
	/**
	 * Insert data in the selected table.
	 * 
	 * @param nomTable
	 * 		The name of the table.
	 * @param contenuTableau
	 * 		An array containing the values to be added.
	 * 
	 * @return The result of the query.
	 */
	public String queryInsert(String nomTable, String[] contenuTableau)
	{
		connexion();
		StringBuilder bld = new StringBuilder();
		
		bld.append("INSERT INTO " + nomTable + " VALUES(");
		
		for(int i = 0 ; i <= contenuTableau.length - 1 ; i++)
		{
			bld.append("'" + contenuTableau[i] + "'");
			
			if(i < contenuTableau.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(")");
		return this.executionUpdate(bld.toString());
	}
	
	/**
	 * Insert data in the selected table and columns.
	 * 
	 * @param nomTable
	 * 		The name of the table.
	 * @param nomColonne
	 * 		An array containing the names of the selected columns.
	 * @param contenuTableau
	 * 		An array containing the values to be added.
	 * 
	 * @return The result of the query.
	 */
	public String queryInsert(String nomTable, String[] nomColonne, String[] contenuTableau)
	{
		connexion();
		StringBuilder bld = new StringBuilder();
		
		bld.append("INSERT INTO " + nomTable + "(");
		
		for(int i = 0 ; i <= nomColonne.length - 1 ; i++)
		{
			bld.append(nomColonne[i]);
			
			if(i < nomColonne.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(") VALUES(");
		
		for(int i = 0 ; i <= contenuTableau.length - 1 ; i++)
		{
			bld.append("'" + contenuTableau[i] + "'");
			
			if(i < contenuTableau.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(")");
		return this.executionUpdate(bld.toString());
	}
	
	/**
	 * Updates the contents of the columns of a table by adding a condition.
	 * 
	 * @param nomTable
	 * 		The name of the table.
	 * @param nomColonne
	 * 		An array containing the names of the selected columns.
	 * @param contenuTableau
	 * 		An array containing the values to be added.
	 * @param etat
	 * 		The condition of sorting.
	 * 
	 * @return The result of the query.
	 */
	public String queryUpdate(String nomTable, String[] nomColonne, String[] contenuTableau, String etat)
	{
		connexion();
		StringBuilder bld = new StringBuilder();
		
		bld.append("UPDATE " + nomTable + " SET ");
		
		for(int i = 0 ; i <= nomColonne.length - 1 ; i++)
		{
			bld.append(nomColonne[i] + "='" + contenuTableau[i] + "'");
			
			if(i < nomColonne.length - 1)
			{
				bld.append(", ");
			}
		}
		
		bld.append(" WHERE " + etat);
		return this.executionUpdate(bld.toString());
	}
	
	/**
	 * Deletes data from a table.
	 * 
	 * @param nomTable
	 * 		The name of the table.
	 * 
	 * @return The result of the query.
	 */
	public String queryDelete(String nomTable)
	{
		connexion();
		sql = "DELETE FROM " + nomTable;
		return this.executionUpdate(sql);
	}
	
	/**
	 * Deletes data from a table by adding a condition.
	 * @param nomTable
	 * 		The name of the table.
	 * @param etat
	 * 		The condition of sorting.
	 * 
	 * @return The result of the query.
	 */
	public String queryDelete(String nomTable, String etat)
	{
		connexion();
		sql = "DELETE FROM " + nomTable + " WHERE " + etat;
		return this.executionUpdate(sql);
	}
}
