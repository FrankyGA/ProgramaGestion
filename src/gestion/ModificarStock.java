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

	//Campo para a�adir el nuevo stock
	Label lblStock= new Label("Nuevo stock");
	TextField txtStock = new TextField(20);

	//Creamos los 3 choice
	Choice choStocks = new Choice();
	Choice choTiendas = new Choice();
	Choice choLiquidos = new Choice();

	//Creamos los di�logos
	//Dialog dlgEditar= new Dialog (ventana,"Edici�n stocks", true);
	Dialog dlgModificar = new Dialog(ventana,"Modificaci�n", true);
	Dialog dlgMensaje = new Dialog(ventana,"Mensaje", true);
	Label lblMensaje = new Label("Modificaci�n correcta");
	//Dialog dlgError = new Dialog(ventana,"Error", true);
	//Label lblError = new Label("XXXXXXXXXXXXXX");

	//Creamos los botones
	Button btnModificar = new Button("Modificar");
	Button btnAceptar = new Button("Aceptar");
	Button btnSi = new Button("S�");
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

	//Constructor de stock, pasamos tipo usuario por par�metro
	public 	ModificarStock( int tipoUsuario){

		this.tipoUsuario=tipoUsuario;

		//A�adimos listener
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
		//Sacar la informaci�n y meterla en el TextArea
		txaListado.setText(bd.consultaStock(tipoUsuario));
		//Desconectar
		bd.desconectar();

		//A�adimos listado
		ventana.add(txaListado);
		txaListado.setEditable(false);
		//Rellenar el Choice cabecera
		choStocks.add("Seleccionar un stock por �ndice...");

		//Conectar BD
		bd.conectar();
		//Sacar los datos de la tabla stocks y rellenar choice
		rs=bd.rellenarStock();

		try{
			//Mientras haya registros...
			while (rs.next()){
				//A�adimos al choice
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

		//A�adimos el choice y el bot�n modificar a la primera ventana
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

			//Si selecciona alguna opci�n del choice
			if (!choStocks.getSelectedItem().equals("Seleccionar un stock por �ndice...")){

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
						//Si el valor del fkTienda coincide con el id de una tienda en rs, se guarda la posici�n.
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

				//------------------------------------------Tabla l�quidos---------------------------------------//

				//Quitamos todo del choice
				choLiquidos.removeAll();
				// Rellenar el Choice
				choLiquidos.add("Elegir l�quido");
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
						//Si el fk guardado es igual al id del l�quido
						//Si el valor del fkLiquido coincide con el id de un l�quidoa en rs, se guarda la posici�n.
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

				//------------------------------Montar la ventana Modificaci�n----------------------------//
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
		//Bot�n cancelar muestra ventana principal
		else if(evento.getSource().equals(btnCancelar)){
			dlgModificar.setVisible(false);
		}
		//bot�n modificar si todo va bien indicamos qu� hacer
		else if(evento.getSource().equals(btnAceptar)){

			//Si selecciona alguna opci�n del choice
			if ((!choTiendas.getSelectedItem().equals("Elegir tienda")) && (!choLiquidos.getSelectedItem().equals("Elegir l�quido"))){

				bd.conectar();

				//Metemos los datos en el array
				String[] seleccionado = choTiendas.getSelectedItem().split("-");

				//Sacamos el �ndice
				int idTiendaFk = Integer.parseInt(seleccionado[0]);

				//Metemos los datos en el array
				String[] seleccionado1 = choLiquidos.getSelectedItem().split("-");

				//Sacamos el �ndice
				int idTipoLiquidoFk = Integer.parseInt(seleccionado1[0]);

				//Si la modificaci�n ha sido correcta
				if ((bd.actualizarStock(idTipoLiquidoTienda, txtStock.getText(), idTipoLiquidoFk, idTiendaFk, tipoUsuario)==0)){
					//Si todo bien
					lblMensaje.setText("Modificaci�n correcta");
				}
				//Sino mostrar mensaje de error
				else{
					lblMensaje.setText("Modificaci�n err�nea");
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

	//M�todo para mensaje
	private void mostrarMensaje(){
		dlgMensaje.setLayout(new FlowLayout());
		dlgMensaje.setSize(250,75);
		dlgMensaje.addWindowListener(this);
		dlgMensaje.setResizable(false);
		dlgMensaje.add(lblMensaje);
		dlgMensaje.setLocationRelativeTo(null);
		dlgMensaje.setVisible(true);
	}
	/*//M�todo para avisar que no se ha elegido opci�n
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
