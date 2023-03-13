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
	//Método conectar
	public void conectar()
	{
		try
		{
			//Cargar los controladores para el acceso a la BD
			Class.forName(driver);
			//Establecer la conexión con la BD Empresa
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
	//Método consultar usuario
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
	//Método para consultar datos de tabla tiendas
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
				//En getString o getInt también se puede poner un número que indica posición
				contenido = contenido + /*rs.getInt("idTienda") + " "+*/
				"Tienda: "+ rs.getString("nombreTienda") + 
				", dirección: " + rs.getString("direccionTienda") + "\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//Método para insertar datos tabla tiendas
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

	//Método para sacar y rellenar datos de la tabla tiendas
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
	
	//Método para borrar datos tabla tiendas
	public int borrarTienda(int idTienda)
	{
		int resultado = 0;
		//Devolver un 0 --> Borrado éxito
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

	//Método para consultar datos de las tiendas por la id
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
	
	//Método para actualizar datos de las tiendas, pasamos por parámetro
	public int actualizarTienda(String idTienda, String nombreNuevo, String direccionNueva)
	{
		int resultado = 0;
		String sentencia = "UPDATE tiendas SET nombreTienda = '"+nombreNuevo+"', direccionTienda='"+direccionNueva+"' WHERE idTienda = " + idTienda;
		//Devolver un 0 --> Modificación con éxito
		//Devolver un -1 --> Modificación error
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
	//Método para desconectar de la BD
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
	 //Método para consultar datos de tabla líquidos
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
				//En getString o getInt también se puede poner un número que indica posición
				contenido = contenido /*+ rs.getInt("idTipoLiquido") + " "*/+
				"Marca líquido: "+ rs.getString("marcaLiquido") + 
				", modelo: " + rs.getString("modeloLiquido") + 
				", capacidad: " + rs.getString("capacidadLiquido") + "\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//Método para insertar datos tabla líquidos
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

	//Método para sacar y rellenar datos de la tabla líquidos
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
	
	//Método para borra datos tabla líquidos
	public int borrarLiquido(int idTipoLiquido)
	{
		int resultado = 0;
		//Devolver un 0 --> Borrado éxito
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

	//Método para consultar datos de las líquidos por la id
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
	
	//Método para actualizar datos de las líquidos, pasamos por parámetro
	public int actualizarLiquido(String idTipoLiquido, String marcaNueva, String modeloNuevo, String capacidadNueva)
	{
		int resultado = 0;
		String sentencia = "UPDATE tipoliquidos SET marcaLiquido = '"+marcaNueva+"', modeloLiquido='"+modeloNuevo+"', capacidadLiquido='"+capacidadNueva+"' WHERE idTipoLiquido = " + idTipoLiquido;
		//Devolver un 0 --> Modificación con éxito
		//Devolver un -1 --> Modificación error
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
