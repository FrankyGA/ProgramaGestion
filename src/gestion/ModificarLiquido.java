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

public class ModificarLiquido implements WindowListener, ActionListener {
	
	//Componentes de la ventana
	Frame ventana = new Frame("Modificaci�n de l�quidos");
	
	Label lblCabecera = new Label("Elegir el l�quido a modificar:");
	Choice choLiquidos = new Choice();
	
	//Creamos ventana di�logo de edici�n
	Dialog dlgEditar = new Dialog(ventana, "Edici�n l�quido", true);
	Label lblCabecera2 = new Label("Editando el l�quido n� ");
	
	Label lblId = new Label("N� l�quido:");
	Label lblMarca = new Label("Marca:");
	Label lblModelo = new Label("Modelo:");
	Label lblCapacidad = new Label("Capacidad:");
	
	//Creamos campos de texto para introducci�n de datos
	TextField txtId = new TextField(15);
	TextField txtMarca = new TextField(15);
	TextField txtModelo = new TextField(15);
	TextField txtCapacidad = new TextField(15);
	
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
	
	int tipoUsuario;

	//Constructor de modificar tienda
	public ModificarLiquido(int tipoUsuario){
		
		this.tipoUsuario= tipoUsuario;
		
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
		ventana.setBackground(Color.green);
		
		ventana.add(lblCabecera);
		rellenarChoiceLiquidos();
		ventana.add(choLiquidos);
		ventana.add(btnEditar);
		ventana.add(btnCancelar);
		//Fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true);//Mostrarla
	}
	
	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	//Funcionalidad de cerrar ventana
	public void windowClosing(WindowEvent we){
		if(dlgMensaje.isActive()){
			dlgMensaje.setVisible(false);
		}
		else if(dlgEditar.isActive()){
			dlgEditar.setVisible(false);
		}
		else{
			ventana.setVisible(false);
		}
	}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	@Override
	public void actionPerformed(ActionEvent evento) {
		//Si pulsamos en editar...
		if(evento.getSource().equals(btnEditar)){
			//Si ha seleccionado alg�n elemento del choice que no sea...
			if(!choLiquidos.getSelectedItem().equals("Elegir l�quido...")){
				//Metemos en un array de string los datos quit�ndole los guiones
				String[] seleccionado = choLiquidos.getSelectedItem().split("-");
				
				// Conectar BD y sacar los datos del l�quido seleccionado
				bd.conectar();
				//Usamos m�todo
				rs = bd.consultarLiquido(seleccionado[0], tipoUsuario);
				//System.out.println(rs);
				try{
					rs.next();
					txtMarca.setText(rs.getString("marcaLiquido"));
					txtModelo.setText(rs.getString("modeloLiquido"));
					txtCapacidad.setText(rs.getString("capacidadLiquido"));
				}
				catch(SQLException sqle) {}
				bd.desconectar();
				
				//Muestro los datos de la tienda elegida
				//en la pantalla de edici�n
				dlgEditar.setSize(200, 350);
				//No Permitir redimensionar
				dlgEditar.setResizable(false);

				dlgEditar.setLayout(new FlowLayout());
				dlgEditar.setBackground(Color.gray);
				
				dlgEditar.add(lblCabecera2);
				dlgEditar.add(lblId);
				
				txtId.setEnabled(false);
				//rs.getInt("idTipoLiquido"); �ndice por id
				txtId.setText(seleccionado[0]);
				dlgEditar.add(txtId);
				
				dlgEditar.add(lblMarca);
				dlgEditar.add(txtMarca);
				
				dlgEditar.add(lblModelo);
				dlgEditar.add(txtModelo);
				
				dlgEditar.add(lblCapacidad);
				dlgEditar.add(txtCapacidad);
				
				dlgEditar.add(btnModificar);
				dlgEditar.add(btnDenegar);
				
				dlgEditar.setLocationRelativeTo(null);
				dlgEditar.setVisible(true);
			}
			else {
	            mostrarDialogo("No se ha seleccionado ninguna opci�n.");
	        }
			
		}
		//Si pulsamos bot�n Modificar...
		else if(evento.getSource().equals(btnModificar))
		{
			//Conectamos con BD
			bd.conectar();
			//Metemos en resultado el valor del m�todo pasandole 
			//los datos del campo texto como par�metros
			int resultado = bd.actualizarLiquido(txtId.getText(), txtMarca.getText(), txtModelo.getText(), txtCapacidad.getText(), tipoUsuario);
			//Desconectamos de la BD
			bd.desconectar();
			//Usamos m�todo para rellenar el choice
			rellenarChoiceLiquidos();
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
			//ventana.setBackground(Color.green);
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
	
	//M�todo para rellenar el listado
		private void rellenarChoiceLiquidos(){
			
			choLiquidos.removeAll();
			//Rellenar Choice
			choLiquidos.add("Elegir l�quido...");
			//Conectar BD
			bd.conectar();
			//Sacar los datos de la tabla l�quidos
			rs=bd.rellenarLiquido();
			//Registro a registro, meterlos en el Choice
			try {
				//Mientras haya registros
				while(rs.next()){
					
					//A�adimos al choice
					choLiquidos.add(rs.getInt("idTipoLiquido")+" - "+
					" Liquido: " + rs.getString("marcaLiquido")+ 
					" modelo: " + rs.getString("modeloLiquido")+ 
					" capacidad: " + rs.getString("capacidadLiquido"));
				}
			} 
			catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Desconectar BD
			bd.desconectar();
		}
	
	//M�todo para avisar que no se ha elegido opci�n
	private void mostrarDialogo(String mensaje) {
	    dlgMensaje.setSize(250, 100);
	    dlgMensaje.setLayout(new FlowLayout());
	    lblMensaje.setText(mensaje);
	    dlgMensaje.add(lblMensaje);
	    dlgMensaje.setLocationRelativeTo(null);
	    dlgMensaje.setVisible(true);
	}
}
