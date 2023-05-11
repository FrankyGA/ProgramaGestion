package gestion;

import java.awt.Button;
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

public class AltaTienda implements WindowListener, ActionListener 
{
	//Componentes de la ventana
	Frame ventana = new Frame("Alta de tienda");
	
	Label lblNombre = new Label("Nombre:");
	Label lblDireccion = new Label("Direcci�n:");
	
	//Creamos campos de texto para introducci�n de datos
	TextField txtNombre = new TextField(25);
	TextField txtDireccion = new TextField(25);
	
	//Creamos botones
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	Button btnLimpiar = new Button("Limpiar");
	
	//Creamos ventana di�logo en modal
	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXX");
	
	//Creamos objeto conexi�n
	ConexionVapers ConexionVapers = new ConexionVapers();

	int tipoUsuario;
	
	//Constructor de alta tienda
	public AltaTienda(int tipoUsuario){
		
		this.tipoUsuario = tipoUsuario;
		
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		
		//Par�metros de la pantalla
		ventana.setSize(300, 200); 
		ventana.setResizable(false); // No Permitir redimensionar

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.yellow);
		
		ventana.add(lblNombre);
		ventana.add(txtNombre);
		
		ventana.add(lblDireccion);
		ventana.add(txtDireccion);
		
		ventana.add(btnAceptar);
		ventana.add(btnLimpiar);
		ventana.add(btnCancelar);
		
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true); //Mostrar la pantalla
	}
	
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	
	public void windowClosing(WindowEvent we)
	{
		if(dlgMensaje.isActive())
		{
			dlgMensaje.setVisible(false);
			limpiar();
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
		// Aceptar
		if(evento.getSource().equals(btnAceptar))
		{
			//Conectar
			ConexionVapers.conectar();
			
			//Coger los datos del formulario
			String nombre = txtNombre.getText();
			String direccion = txtDireccion.getText();
			
			//Hacer el INSERT con esos datos. guardando datos en String y aplicando m�todo
			String sentencia = "INSERT INTO tiendas VALUES(null,'"+
			nombre+"','"+direccion+"');";
			int resultado = ConexionVapers.insertarTienda(sentencia, tipoUsuario);
			
			//If para controlar que los datos est�n introducidos
			if(txtNombre.getText().length()==0||txtDireccion.getText().length()==0) {
				lblMensaje.setText("Los campos est�n vacios");
			}
			//Si el resultado es correcto
			else if(resultado == 0)
			{
				lblMensaje.setText("Alta Correcta");
			}
			//Sino es correcto
			else
			{
				lblMensaje.setText("Error en Alta");
			}
			// Desconectar
			ConexionVapers.desconectar();
			
			dlgMensaje.setSize(180,75);
			dlgMensaje.setLayout(new FlowLayout());
			dlgMensaje.setResizable(false);
			dlgMensaje.add(lblMensaje);
			dlgMensaje.setLocationRelativeTo(null);
			dlgMensaje.setVisible(true);
		}
		else if(evento.getSource().equals(btnCancelar))
		{
			ventana.setVisible(false);
		}
		//Limpiar los campos
		else if(evento.getSource().equals(btnLimpiar))
		{
			limpiar();
		}
	}
	
	//M�todo reseteo
	private void limpiar() 
	{
		txtNombre.selectAll();
		txtNombre.setText("");
		
		txtDireccion.selectAll();
		txtDireccion.setText("");
		//Para que al cancelar vaya ah� el cursor
		txtNombre.requestFocus();
	}
}
