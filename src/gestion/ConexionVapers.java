package gestion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionVapers
{
	String driver = "com.mysql.cj.jdbc.Driver"; 
	String url = "jdbc:mysql://localhost:3306/programagestionvapers"; 
	String login = "userTiendas"; //Usuario MySQL
	String password = "Studium2023;"; //Su clave correspondiente
	String sentencia = "";
	Connection connection = null; 
	Statement statement = null;

	public ConexionVapers()
	{

	}
	
	//------------------------------------------------------------------------------------------//
	//M�todo conectar
	public void conectar()
	{
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexi�n con la BD Empresa
			connection = DriverManager.getConnection(url, login, password);
		}
		catch (ClassNotFoundException cnfe)
		{}
		catch (SQLException sqle)
		{}
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}
	
	//------------------------------------------------------------------------------------------//
	//M�todo consultar usuario
	public int consultar(String sentencia)
	{
		int resultado = -1;
		ResultSet rs = null;
		
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery(sentencia);
			
			if(rs.next()) // Si hay, al menos uno
			{
				resultado = rs.getInt("tipoUsuario");
			}
		}
		catch (SQLException sqle)
		{}
		return resultado;
	}
	
	//------------------------------------------------------------------------------------------//
	//M�todo para consultar datos de tabla tiendas
	public String obtenerTiendas()
	{
		String contenido = "";
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery("SELECT * FROM tiendas");
			while(rs.next()) //Si hay, al menos uno
			{
				//Sacamos los siguientes datos
				//En getString o getInt tambi�n se puede poner un n�mero que indica posici�n
				contenido = contenido + /*rs.getInt("idTienda") + " "+*/
				"Tienda: "+ rs.getString("nombreTienda") + 
				", direcci�n: " + rs.getString("direccionTienda") + "\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//M�todo para insertar datos tabla tiendas
	public int insertarTienda(String sentencia)
	{
		int resultado = 0; //Correcto
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el INSERT
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; //Error
		}
		return(resultado);
	}

	//M�todo para sacar y rellenar datos de la tabla tiendas
	public ResultSet rellenarTienda()
	{
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tiendas");
		}
		catch (SQLException sqle)
		{}
		return (rs);
	}
	
	//M�todo para borrar datos tabla tiendas
	public int borrarTienda(int idTienda)
	{
		int resultado = 0;
		//Devolver un 0 --> Borrado �xito
		//Devolver un -1 --> Borrado error
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar el DELETE
			String sentencia = "DELETE FROM tiendas WHERE idTienda="+idTienda;
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; // Error
		}
		return(resultado);
	}

	//M�todo para consultar datos de las tiendas por la id
	public ResultSet consultarTienda(String idTienda)
	{
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tiendas WHERE idTienda = "+idTienda);
		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		return (rs);
	}
	
	//M�todo para actualizar datos de las tiendas, pasamos por par�metro
	public int actualizarTienda(String idTienda, String nombreNuevo, String direccionNueva)
	{
		int resultado = 0;
		String sentencia = "UPDATE tiendas SET nombreTienda = '"+nombreNuevo+"', direccionTienda='"+direccionNueva+"' WHERE idTienda = " + idTienda;
		//Devolver un 0 --> Modificaci�n con �xito
		//Devolver un -1 --> Modificaci�n error
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el UPDATE
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; //Error 
		}
		return(resultado);
	}
	//------------------------------------------------------------------------------------------//
	//M�todo para desconectar de la BD
	public void desconectar()
	{
		try
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		catch (SQLException e)
		{}
	}
	
	//------------------------------------------------------------------------------------------//
	 //M�todo para consultar datos de tabla l�quidos
	public String obtenerLiquidos()
	{
		String contenido = "";
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Crear un objeto ResultSet para guardar lo obtenido
			//y ejecutar la sentencia SQL
			rs = statement.executeQuery("SELECT * FROM tipoliquidos");
			while(rs.next()) //Si hay, al menos uno
			{
				//Sacamos los siguientes datos
				//En getString o getInt tambi�n se puede poner un n�mero que indica posici�n
				contenido = contenido /*+ rs.getInt("idTipoLiquido") + " "*/+
				"Marca l�quido: "+ rs.getString("marcaLiquido") + 
				", modelo: " + rs.getString("modeloLiquido") + 
				", capacidad: " + rs.getString("capacidadLiquido") + "\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//M�todo para insertar datos tabla l�quidos
	public int insertarLiquido(String sentencia)
	{
		int resultado = 0; //Correcto
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el INSERT
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1;//Error
		}
		return(resultado);
	}

	//M�todo para sacar y rellenar datos de la tabla l�quidos
	public ResultSet rellenarLiquido()
	{
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tipoliquidos");
		}
		catch (SQLException sqle)
		{}
		return (rs);
	}
	
	//M�todo para borra datos tabla l�quidos
	public int borrarLiquido(int idTipoLiquido)
	{
		int resultado = 0;
		//Devolver un 0 --> Borrado �xito
		//Devolver un -1 --> Borrado error
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar el DELETE
			String sentencia = "DELETE FROM tipoliquidos WHERE idTipoLiquido="+idTipoLiquido;
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; // Error
		}
		return(resultado);
	}

	//M�todo para consultar datos de las l�quidos por la id
	public ResultSet consultarLiquido(String idTipoLiquido)
	{
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tipoliquidos WHERE idTipoLiquido = "+idTipoLiquido);
		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		return (rs);
	}
	
	//M�todo para actualizar datos de las l�quidos, pasamos por par�metro
	public int actualizarLiquido(String idTipoLiquido, String marcaNueva, String modeloNuevo, String capacidadNueva)
	{
		int resultado = 0;
		String sentencia = "UPDATE tipoliquidos SET marcaLiquido = '"+marcaNueva+"', modeloLiquido='"+modeloNuevo+"', capacidadLiquido='"+capacidadNueva+"' WHERE idTipoLiquido = " + idTipoLiquido;
		//Devolver un 0 --> Modificaci�n con �xito
		//Devolver un -1 --> Modificaci�n error
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el UPDATE
			statement.executeUpdate(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; //Error
		}
		return(resultado);
	} 
	 
}
