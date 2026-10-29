package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

    private ProductoMenu producto;

    @BeforeEach
    public void setUp() {
        producto = new ProductoMenu("corralita", 13000);
    }

    @Test
    public void testGetNombre() {
        assertEquals("corralita", producto.getNombre(), "El nombre del producto no coincide.");
    }

    @Test
    public void testGetPrecio() {
        assertEquals(13000, producto.getPrecio(), "El precio base del producto no coincide.");
    }

    @Test
    public void testGenerarTextoFactura() {
        
    	String textoEsperado = "corralita\n            13000\n";
        assertEquals(textoEsperado, producto.generarTextoFactura(), "El texto de la factura del producto no es correcto.");
    }
    
    @Test
    void testProductoConPrecioCero( )
    {
        ProductoMenu gratis = new ProductoMenu( "agua", 0 );
        assertEquals( 0, gratis.getPrecio( ) );
        assertEquals( "agua\n" + "            0\n", gratis.generarTextoFactura( ) );
    }
}
