package gestion;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
//import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class MenuPrincipalVapers implements WindowListener, ActionListener
{
	//Creamos ventana
	Frame ventana = new Frame("Menú Tiendas de Vapers");
	//Creamos Menú
	MenuBar barraMenu = new MenuBar();

	//Menú CRUD
	//Tabla tiendas
	Menu mnuTiendas = new Menu("Tiendas"); 
	MenuItem mniAltaTienda = new MenuItem("Nueva tienda");
	MenuItem mniBajaTienda = new MenuItem("Eliminar tienda");
	MenuItem mniConsultaTiendas = new MenuItem("Listado de tiendas");
	MenuItem mniModificacionTienda = new MenuItem("Modificar tienda");

	//Tabla líquidos
	Menu mnuLiquidos = new Menu("Líquidos");
	MenuItem mniAltaLiquido = new MenuItem("Nuevo liquido");
	MenuItem mniBajaLiquido = new MenuItem("Eliminar liquido");
	MenuItem mniConsultaLiquidos = new MenuItem("Listado de liquidos");
	MenuItem mniModificacionLiquido = new MenuItem("Modificar liquido");

	//Tabla Stock liquidos en tiendas
	Menu mnuStockLiquidos = new Menu("Stock de líquidos");
	MenuItem mniAltaStockLiquido = new MenuItem("Nuevo stock");
	MenuItem mniBajaStockLiquido = new MenuItem("Eliminar stock");
	MenuItem mniConsultaStocksLiquido = new MenuItem("Listado de stocks");
	MenuItem mniModificacionStockLiquido = new MenuItem("Modificar stock");

	//Menú para ayuda
	Menu mnuAyuda = new Menu("Ayuda");
	MenuItem mniAyuda = new MenuItem("Ayuda");

	int tipoUsuario;
	Label lblUsuario= new Label("");//Usuario

	//Se pasa el tipoUsuario metido en la base de datos para el control de las opciones de gestión.
	public MenuPrincipalVapers(int tipoUsuario){

		this.tipoUsuario= tipoUsuario;

		//Listener para dar funcionalidad
		ventana.addWindowListener(this);

		//Funcionalidad a opciones de Tienda
		mniAltaTienda.addActionListener(this);
		mniBajaTienda.addActionListener(this);
		mniConsultaTiendas.addActionListener(this);
		mniModificacionTienda.addActionListener(this);

		//Funcionalidad a opciones de Tipo de líquido
		mniAltaLiquido.addActionListener(this);
		mniBajaLiquido.addActionListener(this);
		mniConsultaLiquidos.addActionListener(this);
		mniModificacionLiquido.addActionListener(this);

		//Funcionalidad a opciones de Stock
		mniAltaStockLiquido.addActionListener(this);
		mniBajaStockLiquido.addActionListener(this);
		mniConsultaStocksLiquido.addActionListener(this);
		mniModificacionStockLiquido.addActionListener(this);

		//Funcionalidad a opción ayuda
		mniAyuda.addActionListener(this);

		//Crear ventana
		ventana.setSize(330, 200);
		//No Permitir redimensionar
		ventana.setResizable(false); 

		//Fijar la distribución de labels and text fields
		ventana.setLayout(new FlowLayout()); 
		ventana.setBackground(Color.red);

		/*Añade los elementos del menú
		Diferencia entre usuarios de tipo 0 y tipo 1
		Si son usuarios solo podrán usar la opción de alta
		Si es el administrador podrá usar todas las opciones*/
		mnuStockLiquidos.add(mniAltaStockLiquido);
		if(tipoUsuario==0)
		{
			mnuStockLiquidos.add(mniConsultaStocksLiquido);
			mnuStockLiquidos.add(mniBajaStockLiquido);
			mnuStockLiquidos.add(mniModificacionStockLiquido);
		}
		barraMenu.add(mnuStockLiquidos);

		mnuTiendas.add(mniAltaTienda);
		if(tipoUsuario==0)
		{
			mnuTiendas.add(mniConsultaTiendas);
			mnuTiendas.add(mniBajaTienda);
			mnuTiendas.add(mniModificacionTienda);
		}
		barraMenu.add(mnuTiendas);

		mnuLiquidos.add(mniAltaLiquido);
		if(tipoUsuario==0)
		{
			mnuLiquidos.add(mniConsultaLiquidos);
			mnuLiquidos.add(mniBajaLiquido);
			mnuLiquidos.add(mniModificacionLiquido);
		}
		barraMenu.add(mnuLiquidos);

		mnuAyuda.add(mniAyuda);
		barraMenu.add(mnuAyuda);

		ventana.setMenuBar(barraMenu);

		//ventana.add(lblUsuario);
		//fijar que la ventana salga siempre en el medio
		ventana.setLocationRelativeTo(null); 
		ventana.setVisible(true); // Mostrarla
	}

	public void windowActivated(WindowEvent we) {}
	public void windowClosed(WindowEvent we) {}
	public void windowDeactivated(WindowEvent we) {}
	public void windowDeiconified(WindowEvent we) {}
	public void windowIconified(WindowEvent we) {}
	public void windowOpened(WindowEvent we) {}

	//Funcionalidad de cerrar ventana
	public void windowClosing(WindowEvent we)
	{
		System.exit(0);
		ConexionVapers bd= new ConexionVapers();
		bd.guardarLog(tipoUsuario, "Logout");
	}

	@Override
	public void actionPerformed(ActionEvent evento) 
	{
		//Acciones para tiendas
		//Saca origen del evento y lo compara con la acción
		if(evento.getSource().equals(mniAltaTienda))
		{
			/*Crea objeto de la clase y
			entra en la clase Alta de tiendas*/
			new AltaTienda(tipoUsuario);
		}
		else if(evento.getSource().equals(mniBajaTienda))
		{
			new BajaTienda(tipoUsuario);
		}
		else if(evento.getSource().equals(mniConsultaTiendas))
		{
			new ConsultaTiendas(tipoUsuario);
		}
		else if(evento.getSource().equals(mniModificacionTienda))
		{
			new ModificarTienda(tipoUsuario);
		}
		//Acciones para líquidos
		else if(evento.getSource().equals(mniAltaLiquido))
		{
			new AltaLiquido(tipoUsuario);
		}
		else if(evento.getSource().equals(mniBajaLiquido))
		{
			new BajaLiquido(tipoUsuario);
		}
		else if(evento.getSource().equals(mniConsultaLiquidos))
		{
			new ConsultaLiquidos(tipoUsuario);
		}
		else if(evento.getSource().equals(mniModificacionLiquido))
		{
			new ModificarLiquido(tipoUsuario);
		}
		//Acciones para Stock líquidos en tiendas
		else if(evento.getSource().equals(mniAltaStockLiquido))
		{
			new AltaStock(tipoUsuario);
		}
		else if(evento.getSource().equals(mniConsultaStocksLiquido))
		{
			new ConsultaStock(tipoUsuario);
		}

		else if(evento.getSource().equals(mniBajaStockLiquido))
		{
			new BajaStock(tipoUsuario);
		}

		else if(evento.getSource().equals(mniModificacionStockLiquido))
		{
			new ModificarStock(tipoUsuario);
		}
		else if(evento.getSource().equals(mniAyuda))
		{
			new Ayuda(tipoUsuario);
		}
		//ventana.setVisible(false);//Quita el menú
	}
}
