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

public class AltaLiquido implements WindowListener, ActionListener 
{
	//Componentes de la ventana
	Frame ventana = new Frame("Alta de liquido");
	
	Label lblMarca = new Label("Marca:");
	Label lblModelo = new Label("Modelo:");
	Label lblCapacidad = new Label("Capacidad:");
	
	//Creamos campos de texto para introducción de datos
	TextField txtMarca = new TextField(25);
	TextField txtModelo = new TextField(25);
	TextField txtCapacidad = new TextField(25);
	
	//Creamos botones
	Button btnAceptar = new Button("Aceptar");
	Button btnCancelar = new Button("Cancelar");
	Button btnLimpiar = new Button("Limpiar");
	
	//Creamos ventana diálogo en modal
	Dialog dlgMensaje = new Dialog(ventana, "Mensaje", true);
	Label lblMensaje = new Label("XXXXXXXXXXXXXX");
	
	//Creamos objeto conexión
	ConexionVapers ConexionVapers = new ConexionVapers();

	//Constructor de alta líquido
	public AltaLiquido()
	{
		//Listener para dar funcionalidad
		ventana.addWindowListener(this);
		dlgMensaje.addWindowListener(this);
		btnAceptar.addActionListener(this);
		btnCancelar.addActionListener(this);
		btnLimpiar.addActionListener(this);
		
		//Parámetros de la pantalla
		ventana.setSize(320, 200); 
		ventana.setResizable(false); // No Permitir redimensionar

		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.green);
		
		ventana.add(lblMarca);
		ventana.add(txtMarca);
		
		ventana.add(lblModelo);
		ventana.add(txtModelo);
		
		ventana.add(lblCapacidad);
		ventana.add(txtCapacidad);
		
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
			String marca = txtMarca.getText();
			String modelo = txtModelo.getText();
			String capacidad = txtCapacidad.getText();
			
			//Hacer el INSERT con esos datos
			String sentencia = "INSERT INTO tipoliquidos VALUES(null,'"+
					marca+"','"+modelo+"','"+capacidad+"');";
			int resultado = ConexionVapers.insertarLiquido(sentencia);
			System.out.println(sentencia);
			
			//If para controlar que los datos estén introducidos
			if(txtMarca.getText().length()==0||txtModelo.getText().length()==0||txtCapacidad.getText().length()==0) {
				lblMensaje.setText("Los campos están vacios");
			}
			else if(resultado == 0)
			{
				lblMensaje.setText("Alta Correcta");
			}
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
		// Limpiar
		else if(evento.getSource().equals(btnLimpiar))
		{
			limpiar();
		}
	}
	
	//
	//Método reseteo
	private void limpiar() 
	{
		txtMarca.selectAll();
		txtMarca.setText("");
		
		txtModelo.selectAll();
		txtModelo.setText("");
		
		txtCapacidad.selectAll();
		txtCapacidad.setText("");
		//Para que al cancelar vaya ahí el cursor
		txtMarca.requestFocus();
	}
}
