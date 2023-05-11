package gestion;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificarStock implements  WindowListener, ActionListener {

	Frame ventana = new Frame ("Modificar stocks");
	
	Label lblCabecera = new Label ("Elegir stock");
	
	Label lblStock= new Label("Nuevo stock");
	TextField txtStock = new TextField(15);
	
	Choice choStocks = new Choice();
	Choice choTiendas = new Choice();
	Choice choLiquidos = new Choice();
	
	Dialog dlgEditar= new Dialog (ventana,"Edición Clientes", true);
	Dialog dlgModificar = new Dialog(ventana,"Modificación", true);
	
	Button btnModificar = new Button("Modificar");
	Button btnModificar2 = new Button("Modificar2");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	Button btnCancelar= new Button("cancelar");
	
	Dialog dlgMensaje = new Dialog(ventana,"Mensaje", true);
	Label lblMensaje = new Label("Modificación correcta");

	ConexionVapers bd = new ConexionVapers();
	
	ResultSet rs = null;
	ResultSet rs2 = null;
	
	int idTipoLiquidoTienda = 0;
	int idTienda = 0;
	int idTipoLiquido = 0;
	int fkTienda;
	int fkLiquido;
	
	int tipoUsuario;

	//Constructor
	public 	ModificarStock( int tipoUsuario){

		this.tipoUsuario=tipoUsuario;
		
		//Listener
		ventana.addWindowListener(this);
		btnModificar.addActionListener(this);

		//Pantalla
		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.orange);
		ventana.setSize(300,200);
		ventana.setResizable(false);
		ventana.add(lblCabecera);

		//Rellenar el Choice cabecera
		choStocks.add("Seleccionar un stock...");
		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla stocks y rellenar choice
		rs=bd.rellenarStock();
		
		try
		{
			//Mientras haya registros...
			while (rs.next())
			{
				//Añadimos al choice
				choStocks.add(rs.getInt("idTipoLiquidoTienda") + "-" + 
				"-" + rs.getString("stockLiquido")+ "-" +
				"-" + rs.getInt("idTiendaFk")+ "-" +
				"-" + rs.getInt("idTipoLiquidoFk"));
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		//Registro a registro, meteros en el choice
		//Desconectar la base de datos
		bd.desconectar();
		ventana.add(choStocks);
		ventana.add(btnModificar);

		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent evento) {


		//Si pulsamos en Editar
		if(evento.getSource().equals(btnModificar))
		{
			if (choStocks.getSelectedItem().equals("Seleccionar un stock..."))
			{
				lblMensaje.setText("Debes seleccionar un stock");
				mostrarMensaje();
			}
			else
			{
				String[] seleccionado=choStocks.getSelectedItem().split("-");
				
				bd.conectar();
				rs=bd.consultarStock(seleccionado[0], tipoUsuario);
				try {   
				rs.next();
				idTipoLiquidoTienda= rs.getInt("idTipoLiquidoTienda");
				txtStock.setText(rs.getString("stock"));
				fkTienda = rs.getInt("idTiendaFk");
				fkLiquido = rs.getInt("idtipoLiquidoFk");

				}
				catch (SQLException sqle)
				{}	

				bd.desconectar();
				
				//------------------------------------------Tabla tiendas---------------------------------------//
				
				choTiendas.removeAll();
				// Rellenar el Choice
				choTiendas.add("Elegir tienda");
				// Conectar BD
				bd.conectar();
				
				rs=bd.rellenarTienda();

				int posicionTienda=0;
				int i=1;
				try{ 
					while (rs.next()){
						
						choTiendas.add(rs.getInt("idTienda")+"-"+
						"-" + rs.getString("nombreTienda")+ "-" +
						"-" + rs.getString("direccionTienda"));
						
						if (fkTienda == rs.getInt("idTienda")) {
							posicionTienda=i;
						}
						i++;
					}
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
				choTiendas.select(posicionTienda);

				//Desconectar la base de datos
				bd.desconectar();
				
				//------------------------------------------Tabla líquidos---------------------------------------//
				
				choLiquidos.removeAll();
				// Rellenar el Choice
				choLiquidos.add("Elegir líquido");
				// Conectar BD
				bd.conectar();
				
				rs=bd.rellenarLiquido();

				int posicionLiquido=0;
				int j=1;
				try
				{ 

					while (rs.next())
					{

						choLiquidos.add(rs.getInt("idTipoLiquido")+"-"+
						"-" + rs.getString("marcaLiquido")+ "-" + 
						"-" + rs.getString("modeloLiquido")+ "-" +
						"-" + rs.getString("capacidadLiquido"));

						if (fkLiquido==rs.getInt("idTipoLiquido")) {

							posicionLiquido=j;

						}
						j++;
					}
				}
				catch(Exception e)
				{
					System.out.println(e.getMessage());
				}
				choLiquidos.select(posicionLiquido);

				//Registro a registro, meteros en el choice
				//Desconectar la base de datos
				bd.desconectar();
				
				//Montar la ventana Modificación
				dlgModificar.setLayout(new FlowLayout());
				dlgModificar.setSize(320,200); //ancho , alto
				
				dlgModificar.add(choTiendas);
				dlgModificar.add(choLiquidos);
				dlgModificar.add(lblStock);
				dlgModificar.add(txtStock);
				dlgModificar.add(btnModificar2);
				dlgModificar.add(btnCancelar);
				choTiendas.setPreferredSize(new Dimension(280, 20));
				choLiquidos.setPreferredSize(new Dimension(280, 20));
				
				//Listener
				dlgModificar.addWindowListener(this);
				btnModificar.addActionListener(this);
				btnModificar2.addActionListener(this);
				btnCancelar.addActionListener(this);

				//mostrar en pantalla
				dlgModificar.setResizable(true);
				dlgModificar.setLocationRelativeTo(null);
				dlgModificar.setVisible(true);
			}
		}
		//Botón cancelar muestra ventana principal
		else if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		//botón modificar si todo va bien indicamos qué hacer
		else if(evento.getSource().equals(btnModificar2))
		{

			bd.conectar();

			//Metemos los datos en el array
			String[] seleccionado = choTiendas.getSelectedItem().split("-");

			//Sacamos el índice
			int idTiendaFk = Integer.parseInt(seleccionado[0]);
			
			//Metemos los datos en el array
			String[] seleccionado1 = choLiquidos.getSelectedItem().split("-");

			//Sacamos el índice
			int idTipoLiquidoFk = Integer.parseInt(seleccionado1[0]);

			//Si la modificación ha sido correcta
			if ((bd.actualizarStock(idTipoLiquidoTienda, txtStock.getText(), idTipoLiquidoFk, idTiendaFk, tipoUsuario)==0))
			{
				//Si todo bien
				lblMensaje.setText("Modificación correcta");
			}
			//Sino mostrar mensaje de error
			else
			{
				lblMensaje.setText("Modificación errónea");
			}
			//Desconectar la base
			bd.desconectar();
			dlgModificar.setVisible(false);
			mostrarMensaje();
		}
	}		

	private void mostrarMensaje()
	{
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.setSize(300,200);
		dlgMensaje.addWindowListener(this);
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
		//dlgConfirmacion.setVisible(false);
	}

	@Override
	public void windowOpened(WindowEvent e) {}

	@Override
	public void windowClosing(WindowEvent e) {
		if (dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}

		else
		{
			ventana.setVisible(false);
		}
	}

	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
}
