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

public class LoginVapers implements WindowListener, ActionListener
{
	//Diseño de una ventana
	//Crear ventana
	Frame ventana = new Frame("Login"); 
	
	Label lblUsuario = new Label("Usuario");
	Label lblClave = new Label("Clave");
	
	TextField txtUsuario = new TextField(15);
	TextField txtClave = new TextField(15);
	
	Button btnAceptar = new Button("Aceptar");
	Button btnLimpiar = new Button("Limpiar");
	//Crear ventana diálogo
	//Si es modal, hasta que no se cierre no se puede volver a la ventana de atrás, true
	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXXXXXXXX");
	int tipoUsuario;
	
	//Constructor de la clase
	public LoginVapers()
	{
		//Listener, añadir funcionalidad
		ventana.addWindowListener(this);//añade cerrar a la ventana
		dlgMensaje.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		
		//Formato a la ventana
		ventana.setSize(250, 180);
		ventana.setResizable(false); //No Permitir redimensionar
		//Fijar la distribucion de labels and text fields
		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.red);
		
		ventana.add(lblUsuario);
		ventana.add(txtUsuario);
		ventana.add(lblClave);
		//Para no mostrar la clave
		txtClave.setEchoChar('*'); 
		ventana.add(txtClave);
		ventana.add(btnAceptar);
		ventana.add(btnLimpiar);
		
		ventana.setLocationRelativeTo(null); //Fijar ventana
		ventana.setVisible(true); //Mostrar ventana
	}

	//Clase principal del programa
	public static void main(String[] args)
	{
		new LoginVapers();
	}
	
	//Funcionalidad de botones
	public void actionPerformed(ActionEvent ae)
	{
		//Si se ha pulsado el botón cancelar, resetea
		if(ae.getSource().equals(btnLimpiar))
		{
			txtUsuario.selectAll(); 
			txtUsuario.setText("");
			txtClave.selectAll();
			txtClave.setText("");
			txtUsuario.requestFocus();
		}
		//Si se ha pulsado el botón aceptar...
		else if(ae.getSource().equals(btnAceptar))
		{
			//Coge los textos de la ventana: usuario y clave
			String usuario = txtUsuario.getText();
			String clave = txtClave.getText();
			//Si el usuario es el administrador es tipo 0
			if (usuario.equals("administrador")) {
				tipoUsuario= 0 ;
			}else {
				//Si es cualquier otro es tipo 1
				tipoUsuario = 1;
			}
			//Crea objeto y conecta BD
			ConexionVapers bd = new ConexionVapers();
			bd.conectar();
			//Hacer un SELECT para consultar datos de conexión
			//Con tabla usuarios
			//Clave codificada SHA2
			//También añadimos consulta para el tipo de usuario
			int resultado = bd.consultar("SELECT * FROM usuarios WHERE nombreUsuario = '"+usuario+
					"' AND claveUsuario = SHA2('"+clave+"', 256);", tipoUsuario);
			//Si no son correctos los datos
			//Caso negativo (-1): Mostrar Mensaje Error
			if(resultado==-1)
			{
				dlgMensaje.setSize(180,75);
				dlgMensaje.setLayout(new FlowLayout());
				dlgMensaje.setResizable(false);
				lblMensaje.setText("Credenciales incorrectas");
				dlgMensaje.add(lblMensaje);
				dlgMensaje.setLocationRelativeTo(null);
				dlgMensaje.setVisible(true);
			}
			//Si los datos son correctos
			else
			{
				//Caso afirmativo (0-1): Mostrar Menú Principal
				new MenuPrincipalVapers(resultado);
				ventana.setVisible(false);
				bd.guardarLog(tipoUsuario, "Login");
			}
			//Desconectar BD
			bd.desconectar();
		}
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
		else
		{
			System.exit(0);
		}
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}
}
