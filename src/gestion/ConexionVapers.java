package gestion;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.io.PrintWriter;
import java.io.BufferedWriter;

public class ConexionVapers
{
	String driver = "com.mysql.cj.jdbc.Driver"; 
	String url = "jdbc:mysql://localhost:3306/programagestionvapers"; 
	String login = "userTiendas"; //Usuario MySQL
	String password = "Studium2023;"; //Su clave correspondiente
	String sentencia = "";
	Connection connection = null; 
	Statement statement = null;
	
	int tipoUsuario;

	public ConexionVapers()
	{

	}
	
	//------------------------------------------------------------------------------------------//
	public void guardarLog(int tipoUsuario, String mensaje)
	{
		//Creamos variable para guardar el tipo de usuario
		String usuario;
		//ResultSet rs = null;
		//Si es tipo 0 es administrador y sino es usuario com�n
		if(tipoUsuario==0)
		{
			usuario = "Administrador";
		}
		else
		{
			usuario = "Usuario" /*+ rs.getString("nombreUsuario")*/;
		}
		//Se crea un objeto Date para obtener la fecha y hora actuales del sistema
		//Sacamos la fecha y la hora del sistema
		Date fecha = new Date();
		//Se define el patr�n de formato de fecha como "dd/MM/YYYY HH:mm:ss" utilizando la clase SimpleDateFormat.
		//Damos formato a la fecha
		String pattern = "dd/MM/YYYY HH:mm:ss";
		//Creamos objeto
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		
		//Bloque para controlar excepciones al trabajar con el archivo
		try
		{
			//Abrir el fichero para a�adir
			FileWriter fw = new FileWriter("Archivo control.log", true);
			BufferedWriter bw = new BufferedWriter(fw);
			//Metemos el apunte
			PrintWriter salida = new PrintWriter(bw);
			//Damos formato al registro del movimiento, println para escribir linea a linea
			salida.println("["+simpleDateFormat.format(fecha)+"]["+
					usuario + "]["+mensaje+"]");
			//Cerrar el fichero
			salida.close();
			bw.close();
			fw.close();
		}
		catch(IOException ioe)
		{
			System.out.println("Error: "+ioe.getMessage());
		}
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
	//M�todo consultar usuario
	public int consultar(String sentencia, int tipoUsuario)
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
	public String obtenerTiendas(int tipoUsuario)
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
			guardarLog(tipoUsuario, "SELECT * FROM tiendas");
			while(rs.next()) //Si hay, al menos uno
			{
				//Sacamos los siguientes datos
				//En getString o getInt tambi�n se puede poner un n�mero que indica posici�n
				contenido = contenido + /*rs.getInt("idTienda") + " "+*/
				"Tienda: "+ rs.getString("nombreTienda") + 
				", direcci�n: " + rs.getString("direccionTienda") + "\n";
			}
		}
		catch (SQLException sqle){}
		
		return(contenido);
	}
	
	//M�todo para consultar datos de tabla tiendas y crear documentos pdf y excel
	public String consultarTiendasPDF()
	{
		String contenido = "";
		//ResultSet para guardar los datos obtenidos en la base de datos
		ResultSet rs = null; 
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//y ejecutar la sentencia SQL . Para ejecutar el SELECT (se usa el executeQuery)
			rs = statement.executeQuery("SELECT * FROM tiendas");
			
			while(rs.next()) //Si hay, al menos uno
			{
				//Sacamos los siguientes datos
				//En getString o getInt tambi�n se puede poner un n�mero que indica posici�n
				contenido = contenido + 
				rs.getInt("idTienda")+"\t"+  
				rs.getString("nombreTienda") + "\t" + 
				rs.getString("direccionTienda") +"\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//M�todo para insertar datos tabla tiendas
	public int insertarTienda(String sentencia, int tipoUsuario)
	{
		int resultado = 0; //Correcto
		
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el INSERT
			statement.executeUpdate(sentencia);
			guardarLog(tipoUsuario, sentencia);
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
	public int borrarTienda(int idTienda, int tipoUsuario)
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
			guardarLog(tipoUsuario, sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; // Error
		}
		return(resultado);
	}

	//M�todo para consultar datos de las tiendas por la id
	public ResultSet consultarTienda(String idTienda, int tipoUsuario){
		
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
	
	//M�todo para actualizar datos de la tabla tiendas, pasamos por par�metro
	public int actualizarTienda(String idTienda, String nombreNuevo, String direccionNueva, int tipoUsuario){
		
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
			guardarLog(tipoUsuario,sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; //Error 
		}
		return(resultado);
	}
	
	//------------------------------------------------------------------------------------------//
	 //M�todo para consultar datos de tabla l�quidos
	public String obtenerLiquidos(int tipoUsuario)
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
			guardarLog(tipoUsuario, "SELECT * FROM tipoliquidos");
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
	
	//M�todo para consultar datos de tabla l�quidos y crear documentos pdf y excel
	public String consultarLiquidosPDF()
	{
		String contenido = "";
		//ResultSet para guardar los datos obtenidos en la base de datos
		ResultSet rs = null; 
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//y ejecutar la sentencia SQL . Para ejecutar el SELECT (se usa el executeQuery)
			rs = statement.executeQuery("SELECT * FROM tipoliquidos");
			
			while(rs.next()) //Si hay, al menos uno
			{
				//Sacamos los siguientes datos
				//En getString o getInt tambi�n se puede poner un n�mero que indica posici�n
				contenido = contenido + 
				rs.getInt("idTipoLiquido")+"\t"+  
				rs.getString("marcaLiquido") + "\t" + 
				rs.getString("modeloLiquido") + "\t"+ 
				rs.getString("capacidadLiquido") +"\n";
			}
		}
		catch (SQLException sqle)
		{}
		return(contenido);
	}
	
	//M�todo para insertar datos tabla l�quidos
	public int insertarLiquido(String sentencia, int tipoUsuario)
	{
		int resultado = 0; //Correcto
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el INSERT
			statement.executeUpdate(sentencia);
			guardarLog(tipoUsuario, sentencia);
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
	public int borrarLiquido(int idTipoLiquido, int tipoUsuario)
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
			guardarLog(tipoUsuario, sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; // Error
		}
		return(resultado);
	}

	//M�todo para consultar datos de las l�quidos por la id
	public ResultSet consultarLiquido(String idTipoLiquido, int tipoUsuario)
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
	
	//M�todo para actualizar datos de la tabla l�quidos, pasamos por par�metro
	public int actualizarLiquido(String idTipoLiquido, String marcaNueva, String modeloNuevo, String capacidadNueva, int tipoUsuario)
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
			guardarLog(tipoUsuario,sentencia);
		}
		catch (SQLException sqle)
		{
			resultado = -1; //Error
		}
		return(resultado);
	} 
	
	//------------------------------------------------------------------------------------------//
	//M�todo para consultar datos de tabla tipoliquidotienda(stocks)
	public String consultaStock(int tipoUsuario) 
	{
		String contenido="";
		ResultSet rs= null;

		try
		{
			// Crear una sentencia
			statement = connection.createStatement();
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery("SELECT * FROM tipoliquidotienda JOIN tiendas on idTiendaFk=idtienda "+
			"JOIN tipoliquidos on idtipoLiquidoFk=idtipoLiquido"); 
			guardarLog(tipoUsuario, "SELECT * FROM tipoliquidotienda JOIN tiendas on idTiendaFk=idtienda "+
			"JOIN tipoliquidos on idtipoLiquidoFk=idtipoLiquido");

			while(rs.next())// Si hay almenos uno
			{
				contenido=contenido+"�ndice Stock: "+rs.getInt("idTipoLiquidoTienda")+", Tienda: "+ rs.getString("nombreTienda") +", Marca l�quido: "+ rs.getString("marcaLiquido")
						+ ", modelo: " + rs.getString("modeloLiquido") + ", Stock: " + rs.getString("stockLiquido") + "\n";	
			}
		}
		catch (SQLException sqle){}
		return(contenido);
	}
	
	//M�todo para consultar datos de tabla tipoliquidotienda(stocks) y crear documentos pdf y excel
	public String consultarStockPDF() {
		
		String contenido="";
		ResultSet rs= null;

		try
		{
			// Crear una sentencia
			statement = connection.createStatement();
			// Crear un objeto ResultSet para guardar lo obtenido
			// y ejecutar la sentencia SQL
			rs = statement.executeQuery("SELECT * FROM tipoliquidotienda JOIN tiendas on idTiendaFk=idtienda "+
			"JOIN tipoliquidos on idtipoLiquidoFk=idtipoLiquido"); 
			
			while(rs.next())// Si hay almenos uno
			{
				contenido=contenido + 
				rs.getInt("idTipoLiquidoTienda") + "\t"+
				rs.getString("nombreTienda") + "\t"+
				rs.getString("marcaLiquido") + "\t"+
				rs.getString("modeloLiquido") + "\t"+
				rs.getString("stockLiquido") + "\n";	
			}
		}
		catch (SQLException sqle){}
		return(contenido);
	}
	
	//M�todo para insertar datos tabla de tipoliquidotienda(stocks)
	public int insertarStock(int stockLiquido, int idTipoLiquidoFk, int idTiendaFk, int tipoUsuario)
	{
		int resultado = 0;
		//Inserta los valores en la tabla
		String sentencia = "INSERT INTO tipoliquidotienda VALUES(null, " 
                + stockLiquido + ", " 
                + idTipoLiquidoFk + ", "
                + idTiendaFk 
                + ");";
		try
		{
			statement = connection.createStatement();
			statement.executeUpdate(sentencia);
			guardarLog(tipoUsuario,sentencia);
		}
		catch (SQLException e)
		{
			resultado = -1;
		}
		return (resultado);
	}
	
	//M�todo para sacar y rellenar datos de la tabla tipoliquidotienda(stocks)
	public ResultSet rellenarStock()
	{
		ResultSet rs = null;

		try
		{
			//Crear la sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tipoliquidotienda");
		}
		catch (SQLException sqle)
		{}
		return (rs);
	}
	
	//M�todo para borra datos tabla tipoliquidotienda(stocks)
	public int borrarStock(int idTipoLiquidoTienda, int tipoUsuario){ 
		
		int resultado=0;
		// devolver un 0--- borrado exito
		//Devolver un -1---- borrado error
		try
		{
			// Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar el delete
			sentencia="DELETE FROM tipoliquidotienda WHERE  idTipoLiquidoTienda= "+idTipoLiquidoTienda; 
			statement.executeUpdate(sentencia);
			guardarLog(tipoUsuario, sentencia);
			//System.out.println(sentencia);
		}
		catch (SQLException sqle)
		{
			resultado= -1;// error
		}
		System.out.println(resultado);
		return(resultado);
	}

	//M�todo para consultar datos de la tabla tipoliquidotienda(stocks) por la id
	public ResultSet consultarStock(String idTipoLiquidoTienda, int tipoUsuario)
	{
		ResultSet rs = null;
		try
		{
			//Crear una sentencia
			statement = connection.createStatement();
			//Ejecutar el SELECT
			rs = statement.executeQuery("SELECT * FROM tipoliquidotienda WHERE idTipoLiquidoTienda = "+idTipoLiquidoTienda);
		}
		catch (SQLException sqle)
		{
			System.out.println(sqle.getMessage());
		}
		return (rs);
	}
	//M�todo para actualizar datos de la tabla tipoliquidotienda(stocks), pasamos por par�metro
	public int actualizarStock(int idTipoLiquidoTienda, String stockLiquido, int idTipoLiquidoFk, int idTiendaFk, int tipoUsuario) 

	{
		int resultado=0;
		String sentencia = "UPDATE tipoliquidotienda SET stockLiquido= '" + stockLiquido +
				"', idTipoLiquidoFk='" + idTipoLiquidoFk +
				"', idTiendaFk=" + idTiendaFk +
				" Where idTipoLiquidoTienda =" + idTipoLiquidoTienda;
		
		System.out.println(sentencia);

		// devolver un 0--- Modificaci�n con exito
		//Devolver un -1---- o si la Modificaci�n a dado error error
		try
		{
			// Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar el update
			statement.executeUpdate(sentencia);
			guardarLog(tipoUsuario , sentencia);	
			}

		catch (SQLException sqle)
		{
			resultado= -1;// error
		}
		return(resultado);
	}
}
