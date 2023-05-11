package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BajaTienda implements WindowListener, ActionListener  
{
	//Componentes de la ventana
	Frame ventana = new Frame("Baja de tienda");
	
	Label lblCabecera = new Label("Elegir la tienda a Borrar:");
	
	//Creamos Choose para listado tiendas
	Choice choTiendas = new Choice();
	Button btnBorrar = new Button("Borrar");
	Button btnCancelar = new Button("Cancelar");
	
	//Ventana para confirmar el borrado
	Dialog dlgConfirmacion = new Dialog(ventana, "Confirmación", true);
	Label lblConfirmacion = new Label("XXXXXXXXXXXXXXXXXXXXXXX");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");

	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXX");

	ConexionVapers bd = new ConexionVapers();
	//variable para guardar los datos
	ResultSet rs = null;
	
	int tipoUsuario;

	//Constructor de la clase
	public BajaTienda(int tipoUsuario){
		
		this.tipoUsuario=tipoUsuario;
		
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		btnBorrar.addActionListener(this);
		btnCancelar.addActionListener(this);

		//Parámetros de la pantalla
		ventana.setSize(350, 200); 
		//No Permitir redimensionar
		ventana.setResizable(false); 

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.yellow);
		
		ventana.add(lblCabecera);
		//Rellenar Choice cabecera
		choTiendas.add("Elegir tienda...");
		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla tiendas y rellenar choice
		rs=bd.rellenarTienda();
		//Registro a registro, meterlos en el Choice con formato
		try 
		{
			while(rs.next())
			{
				choTiendas.add(rs.getInt("idTienda")+"-"+
				"-" + rs.getString("nombreTienda")+ "-" +
				"-" + rs.getString("direccionTienda"));
			}
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Desconectar BD
		bd.desconectar();
		
		ventana.add(choTiendas);
		choTiendas.setPreferredSize(new Dimension(280, 20));
		ventana.add(btnBorrar);
		ventana.add(btnCancelar);
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true); // Mostrarla
	}
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	//Funcionalidad de cerrar ventana
	public void windowClosing(WindowEvent we)
	{
		//Si el diálogo confirmación esta activo, ese es el que hay que ocultar
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
		}
		//Si el diálogo mensaje esta activo, ese es el que hay que ocultar
		else if(dlgConfirmacion.isActive())
		{
			dlgConfirmacion.setVisible(false);
		}
		//Si ninguno esta activo, se oculta la ventana
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
		//Si hemos pulsado el botón borrar...
		if(evento.getSource().equals(btnBorrar))
		{
			//Mostrar el diálogo de confirmación
			//Listener para dar funcionalidad
			dlgConfirmacion.addWindowListener(this);
			btnSi.addActionListener(this);
			btnNo.addActionListener(this);

			//Parámetros de la ventana
			dlgConfirmacion.setSize(350, 200);
			//No Permitir redimensionar
			dlgConfirmacion.setResizable(false); 

			dlgConfirmacion.setLayout(new FlowLayout());
			
			//Pregunta de confirmación de la tienda seleccionada
			lblConfirmacion.setText("¿Borrar la tienda "+
			choTiendas.getSelectedItem()+"?");
			
			dlgConfirmacion.add(lblConfirmacion);
			dlgConfirmacion.add(btnSi);
			dlgConfirmacion.add(btnNo);
			//Fijar que la ventana salga siempre en el medio
			dlgConfirmacion.setLocationRelativeTo(null); 
			dlgConfirmacion.setVisible(true);
		}
		else if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		//Si pulsamos en no...
		else if(evento.getSource().equals(btnNo))
		{
			dlgConfirmacion.setVisible(false);
		}
		//Si pulsamos en si...
		else if(evento.getSource().equals(btnSi))
		{
			//Conectamos a la BD
			bd.conectar();
			//Hacer el DELETE en la tabla guardando los datos en el array
			String[] registros = choTiendas.getSelectedItem().split("-");
			//Borrar los datos de ese registro por posición(id) usando el método
			int resultado = bd.borrarTienda(Integer.parseInt(registros[0]), tipoUsuario);
			//Si el resultado es 0, borrado con éxito
			if(resultado==0)
			{
				lblMensaje.setText("Borrado con éxito");
			}
			//Sino, error
			else
			{
				lblMensaje.setText("Error en borrado");
			}
			// Desconectar
			bd.desconectar();

			dlgMensaje.addWindowListener(this);

			//Parámetros de la pantalla
			dlgMensaje.setSize(350, 100);
			//No Permitir redimensionar
			dlgMensaje.setResizable(false);

			dlgMensaje.setLayout(new FlowLayout());
			dlgMensaje.add(lblMensaje);
			//fijar que la ventana salga siempre en el medio
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
			dlgConfirmacion.setVisible(false);
		}
	}
}
