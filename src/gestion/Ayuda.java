package gestion;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class Ayuda {
	
	int tipoUsuario;
    ConexionVapers bd = new ConexionVapers();

    public Ayuda(int tipoUsuario) {
    	
    	this.tipoUsuario=tipoUsuario;
        abrirArchivoAyuda();
        
    }
    //Método para abrir el archivo
    private void abrirArchivoAyuda() {
        String ayuda = "Ayuda.chm";

        try {
            File archivoAyuda = new File(ayuda);
            Desktop.getDesktop().open(archivoAyuda);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd.guardarLog( tipoUsuario, "Ayuda" );	
    }
}

