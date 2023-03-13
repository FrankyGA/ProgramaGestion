package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
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

public class ModificarTienda implements WindowListener, ActionListener 
{
	//Componentes de la ventana
	Frame ventana = new Frame("Modificaci�n de tiendas");
	
	Label lblCabecera = new Label("Elegir la tienda a modificar:");
	Choice choTiendas = new Choice();
	
	
	//Creamos ventana di�logo de edici�n
	Dialog dlgEditar = new Dialog(ventana, "Edici�n tienda", true);
	Label lblCabecera2 = new Label("Editando la tienda n� ");
	
	Label lblId = new Label("N� tienda:");
	Label lblNombre = new Label("Nombre:");
	Label lblDireccion = new Label("Direcci�n:");
	
	//Creamos campos de texto para introducci�n de datos
	TextField txtId = new TextField(15);
	TextField txtNombre = new TextField(15);
	TextField txtDireccion = new TextField(15);
	
	//Creamos los botones
	Button btnEditar = new Button("Editar");
	Button btnModificar = new Button("Modificar");
	Button btnCancelar = new Button("Cancelar");
	Button btnDenegar = new Button("Denegar");
	
	//Creamos ventana di�logo en modal
	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXX");

	//Creamos objeto conexi�n
	ConexionVapers bd = new ConexionVapers();
	ResultSet rs = null;

	//Constructor de modificar tienda
	public ModificarTienda()
	{
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		btnEditar.addActionListener(this);
		btnModificar.addActionListener(this);
		btnCancelar.addActionListener(this);
		dlgEditar.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnDenegar.addActionListener(this);

		//Par�metros de la pantalla
		ventana.setSize(350, 150);
		//No Permitir redimensionar
		ventana.setResizable(false);

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.yellow);
		
		ventana.add(lblCabecera);
		rellenarChoiceTiendas();
		ventana.add(choTiendas);
		ventana.add(btnEditar);
		ventana.add(btnCancelar);
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true);//Mostrarla
	}
	
	//M�todo para rellenar el listado
	private void rellenarChoiceTiendas() 
	{
		choTiendas.removeAll();
		//Rellenar Choice
		choTiendas.add("Elegir tienda...");
		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla tiendas
		rs=bd.rellenarTienda();
		//Registro a registro, meterlos en el Choice
		try 
		{
			//Mientras haya registros...
			while(rs.next())
			{
				//A�adimos al choice
				choTiendas.add(rs.getInt("idTienda")+" - "+
				"Tienda: " + rs.getString("nombreTienda")+ 
				" direcci�n: " + rs.getString("direccionTienda"));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Desconectar BD
		bd.desconectar();
	}
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	//Funcionalidad de cerrar ventana
	public void windowClosing(WindowEvent we)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		else if(dlgEditar.isActive())
		{
			dlgEditar.setVisible(false);
		}
		else
		{
			ventana.setVisible(false);
		}
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		//Si pulsamos en editar...
		if(evento.getSource().equals(btnEditar))
		{
			//Si ha seleccionado alg�n elemento del choice que no sea...
			if(!choTiendas.getSelectedItem().equals("Elegir tienda..."))
			{
				//Metemos en un array de string los datos quit�ndole los guiones
				String[] seleccionado = choTiendas.getSelectedItem().split("-");
				//Conectar BD y sacar los datos de la tienda seleccionada
				bd.conectar();
				//Usamos m�todo
				rs = bd.consultarTienda(seleccionado[0]);
				try
				{
					rs.next();
					txtNombre.setText(rs.getString("nombreTienda"));
					txtDireccion.setText(rs.getString("direccionTienda"));
				}
				catch(SQLException sqle) {}
				bd.desconectar();
				
				//Muestro los datos de la tienda elegida
				//en la pantalla de edici�n
				dlgEditar.setSize(200, 350);
				//No Permitir redimensionar
				dlgEditar.setResizable(false);

				dlgEditar.setLayout(new FlowLayout());
				//ventana.setBackground(Color.yellow);
				
				dlgEditar.add(lblCabecera2);
				dlgEditar.add(lblId);
				
				txtId.setEnabled(false);
				//rs.getInt("idTienda"); �ndice por id
				txtId.setText(seleccionado[0]);
				dlgEditar.add(txtId);
				
				dlgEditar.add(lblNombre);
				dlgEditar.add(txtNombre);
				
				dlgEditar.add(lblDireccion);
				dlgEditar.add(txtDireccion);
				
				dlgEditar.add(btnModificar);
				dlgEditar.add(btnDenegar);
				
				dlgEditar.setLocationRelativeTo(null);
				dlgEditar.setVisible(true);
			}
			
		}
		//Si pulsamos bot�n Modificar...
		else if(evento.getSource().equals(btnModificar))
		{
			//Conectamos con BD
			bd.conectar();
			//Metemos en resultado el valor del m�todo pasandole 
			//los datos del campo texto como par�metros
			int resultado = bd.actualizarTienda(txtId.getText(), txtNombre.getText(), txtDireccion.getText());
			//Desconectamos de la BD
			bd.desconectar();
			//Usamos m�todo para rellenar el choice
			rellenarChoiceTiendas();
			//Si el resultado es 0, modificaci�n correcta
			if(resultado == 0)
			{
				lblMensaje.setText("Modificaci�n Correcta");
			}
			//Sino es correcta, sale mensaje de error
			else
			{
				lblMensaje.setText("Error en Modificaci�n");
			}
			dlgMensaje.setSize(180,75);
			dlgMensaje.setLayout(new FlowLayout());
			//ventana.setBackground(Color.yellow);
			dlgMensaje.setResizable(false);
			dlgMensaje.add(lblMensaje);
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
		}
		else if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		else if(evento.getSource().equals(btnDenegar))
		{
			dlgEditar.setVisible(false);
		}
	}
}
