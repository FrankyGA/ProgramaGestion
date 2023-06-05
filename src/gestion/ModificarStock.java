package gestion;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModificarStock implements  WindowListener, ActionListener {

	Frame ventana = new Frame ("Modificar stocks");

	TextArea txaListado= new TextArea(10,67);

	Label lblCabecera = new Label ("Elegir stock");

	//Campo para añadir el nuevo stock
	Label lblStock= new Label("Nuevo stock");
	TextField txtStock = new TextField(20);

	//Creamos los 3 choice
	Choice choStocks = new Choice();
	Choice choTiendas = new Choice();
	Choice choLiquidos = new Choice();

	//Creamos los diálogos
	//Dialog dlgEditar= new Dialog (ventana,"Edición stocks", true);
	Dialog dlgModificar = new Dialog(ventana,"Modificación", true);
	Dialog dlgMensaje = new Dialog(ventana,"Mensaje", true);
	Label lblMensaje = new Label("Modificación correcta");
	//Dialog dlgError = new Dialog(ventana,"Error", true);
	//Label lblError = new Label("XXXXXXXXXXXXXX");

	//Creamos los botones
	Button btnModificar = new Button("Modificar");
	Button btnAceptar = new Button("Aceptar");
	Button btnSi = new Button("Sí");
	Button btnNo = new Button("No");
	Button btnCancelar= new Button("cancelar");

	ConexionVapers bd = new ConexionVapers();

	ResultSet rs = null;
	ResultSet rs2 = null;

	//Variables para guardar datos
	int idTipoLiquidoTienda = 0;
	int idTienda = 0;
	int idTipoLiquido = 0;
	int fkTienda;
	int fkLiquido;

	int tipoUsuario;

	//Constructor de stock, pasamos tipo usuario por parámetro
	public 	ModificarStock( int tipoUsuario){

		this.tipoUsuario=tipoUsuario;

		//Añadimos listener
		ventana.addWindowListener(this);
		btnModificar.addActionListener(this);

		//Pantalla
		ventana.setLayout(new FlowLayout());
		ventana.setBackground(Color.orange);
		ventana.setSize(530,360);
		ventana.setResizable(false);
		ventana.add(lblCabecera);

		//Sacamos los datos de la BD
		//Conectar
		bd.conectar();		
		//Sacar la información y meterla en el TextArea
		txaListado.setText(bd.consultaStock(tipoUsuario));
		//Desconectar
		bd.desconectar();

		//Añadimos listado
		ventana.add(txaListado);
		txaListado.setEditable(false);
		//Rellenar el Choice cabecera
		choStocks.add("Seleccionar un stock por índice...");

		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla stocks y rellenar choice
		rs=bd.rellenarStock();

		try{
			//Mientras haya registros...
			while (rs.next()){
				//Añadimos al choice
				choStocks.add(rs.getInt("idTipoLiquidoTienda") + "-" + 
						"-" + rs.getString("stockLiquido")+ "-" +
						"-" + rs.getInt("idTiendaFk")+ "-" +
						"-" + rs.getInt("idTipoLiquidoFk"));
			}
		}
		catch (SQLException e){
			e.printStackTrace();
		}

		//Registro a registro, meteros en el choice
		//Desconectar la base de datos
		bd.desconectar();

		//Añadimos el choice y el botón modificar a la primera ventana
		ventana.add(choStocks);
		choStocks.setPreferredSize(new Dimension(280, 20));
		ventana.add(btnModificar);

		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent evento) {

		//Si pulsamos en modificar
		if(evento.getSource().equals(btnModificar)){

			//Si selecciona alguna opción del choice
			if (!choStocks.getSelectedItem().equals("Seleccionar un stock por índice...")){

				//Sacamos los datos del choice quitando guiones
				String[] seleccionado=choStocks.getSelectedItem().split("-");

				bd.conectar();
				//Guardamos el id
				rs=bd.consultarStock(seleccionado[0], tipoUsuario);

				try {   
					//Guardamos datos 
					rs.next();
					idTipoLiquidoTienda= rs.getInt("idTipoLiquidoTienda");
					txtStock.setText(rs.getString("stock"));
					fkTienda = rs.getInt("idTiendaFk");
					fkLiquido = rs.getInt("idtipoLiquidoFk");
				}

				catch (SQLException sqle){}	

				bd.desconectar();

				//------------------------------------------Tabla tiendas---------------------------------------//

				//Quitamos todo del choice
				choTiendas.removeAll();
				// Rellenar el Choice
				choTiendas.add("Elegir tienda");
				// Conectar BD
				bd.conectar();

				rs=bd.rellenarTienda();

				int posicionTienda=0;
				int i=1;

				try{ 
					//Metemos datos en el choice
					while (rs.next()){

						choTiendas.add(rs.getInt("idTienda")+"-"+
								"-" + rs.getString("nombreTienda")+ "-" +
								"-" + rs.getString("direccionTienda"));

						//Si el fk guardado es igual al id de la tienda
						//Si el valor del fkTienda coincide con el id de una tienda en rs, se guarda la posición.
						if (fkTienda == rs.getInt("idTienda")) {
							posicionTienda=i;
						}
						i++;
					}
				}
				catch(Exception e){
					System.out.println(e.getMessage());
				}
				//Posiciona dentro del choice
				choTiendas.select(posicionTienda);

				//Desconectar la base de datos
				bd.desconectar();

				//------------------------------------------Tabla líquidos---------------------------------------//

				//Quitamos todo del choice
				choLiquidos.removeAll();
				// Rellenar el Choice
				choLiquidos.add("Elegir líquido");
				// Conectar BD
				bd.conectar();

				rs=bd.rellenarLiquido();

				int posicionLiquido=0;
				int j=1;
				try{ 
					//Metemos datos en el choice
					while (rs.next()){

						choLiquidos.add(rs.getInt("idTipoLiquido")+"-"+
								"-" + rs.getString("marcaLiquido")+ "-" + 
								"-" + rs.getString("modeloLiquido")+ "-" +
								"-" + rs.getString("capacidadLiquido"));
						//Si el fk guardado es igual al id del líquido
						//Si el valor del fkLiquido coincide con el id de un líquidoa en rs, se guarda la posición.
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
				//Posiciona dentro del choice
				choLiquidos.select(posicionLiquido);

				//Desconectar la base de datos
				bd.desconectar();

				//------------------------------Montar la ventana Modificación----------------------------//
				dlgModificar.setLayout(new FlowLayout());
				dlgModificar.setBackground(Color.gray);
				dlgModificar.setSize(330,200);

				//dlgModificar.add(txaListado);
				dlgModificar.add(choTiendas);
				dlgModificar.add(choLiquidos);
				dlgModificar.add(lblStock);
				dlgModificar.add(txtStock);
				dlgModificar.add(btnAceptar);
				dlgModificar.add(btnCancelar);
				choTiendas.setPreferredSize(new Dimension(280, 20));
				choLiquidos.setPreferredSize(new Dimension(280, 20));

				//Listener
				dlgModificar.addWindowListener(this);
				btnModificar.addActionListener(this);
				btnAceptar.addActionListener(this);
				btnCancelar.addActionListener(this);

				//mostrar en pantalla
				dlgModificar.setResizable(false);
				dlgModificar.setLocationRelativeTo(null);
				dlgModificar.setVisible(true);
			}
			else {
				lblMensaje.setText("Debe seleccionar los datos a modificar");
				mostrarMensaje();
			}
		}
		//Botón cancelar muestra ventana principal
		else if(evento.getSource().equals(btnCancelar)){
			dlgModificar.setVisible(false);
		}
		//botón modificar si todo va bien indicamos qué hacer
		else if(evento.getSource().equals(btnAceptar)){

			//Si selecciona alguna opción del choice
			if ((!choTiendas.getSelectedItem().equals("Elegir tienda")) && (!choLiquidos.getSelectedItem().equals("Elegir líquido"))){

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
				if ((bd.actualizarStock(idTipoLiquidoTienda, txtStock.getText(), idTipoLiquidoFk, idTiendaFk, tipoUsuario)==0)){
					//Si todo bien
					lblMensaje.setText("Modificación correcta");
				}
				//Sino mostrar mensaje de error
				else{
					lblMensaje.setText("Modificación errónea");
				}
				//Desconectar la base
				bd.desconectar();
				
				dlgModificar.setVisible(false);
				mostrarMensaje();
			}
			else {
				lblMensaje.setText("Debe seleccionar los datos a modificar");
				mostrarMensaje();
			}
		}
	}		

	//Método para mensaje
	private void mostrarMensaje(){
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.setSize(250,75);
		dlgMensaje.addWindowListener(this);
		dlgMensaje.setResizable(false);
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
	/*//Método para avisar que no se ha elegido opción
	private void mostrarDialogo(String mensaje) {
		dlgError.setSize(250, 100);
		dlgError.setLayout(new FlowLayout());
		lblError.setText(mensaje);
		dlgError.add(lblMensaje);
		dlgError.setLocationRelativeTo(null);
		dlgError.setVisible(true);
	}*/

	@Override
	public void windowClosing(WindowEvent e) {

		if (dlgMensaje.isActive()){
			dlgMensaje.setVisible(false);
		}
		else if(dlgModificar.isActive()){
			dlgModificar.setVisible(false);
		}
		//else if(dlgError.isActive()){
			//dlgError.setVisible(false);
		//}
		else{
			ventana.setVisible(false);
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {}
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
