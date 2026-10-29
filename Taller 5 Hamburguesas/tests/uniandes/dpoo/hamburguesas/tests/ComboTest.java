package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {

    private Combo combo;
    private ArrayList<ProductoMenu> items;

    @BeforeEach
    public void setUp() {
        items = new ArrayList<>();
        items.add(new ProductoMenu("corralita", 13000));
        items.add(new ProductoMenu("papas medianas", 5000));
        
      
        combo = new Combo("Corral Combo", 0.1, items);
    }

    @Test
    public void testGetNombre() {
        assertEquals("Corral Combo", combo.getNombre(), "El nombre del combo no es igual.");
    }

    @Test
    public void testGetPrecio() {
  
        int precioEsperado = (int) ((13000 + 5000) * 0.9);
        assertEquals(precioEsperado, combo.getPrecio(), "El precio del combo con descuento es incorrecto.");
    }
    										
    @Test
    public void testGenerarTextoFactura() {
        String textoFactura = combo.generarTextoFactura();
        assertNotNull(textoFactura, "El texto de la factura del combo no puede ser nulo.");
        assertTrue(textoFactura.contains("Corral Combo"), "La factura debe incluir el nombre del combo.");
        assertTrue(textoFactura.contains("16200"), "La factura debe incluir el precio.");
    }
    
    @Test
    public void testComboSinProductos( )
    {
        Combo vacio = new Combo( "Vacio", 0.5, new ArrayList<ProductoMenu>( ) );
        assertEquals( 0, vacio.getPrecio( ) );
    }
    
    @Test
    public void testGetPrecioDescuentoCompleto( )
    {
        Combo gratis = new Combo( "Gratis", 1, items );
        assertEquals( 0, gratis.getPrecio( ) );
    }	
    
    @Test
    public void testGetPrecioConDescuento( )
    {
        assertEquals( 16200, combo.getPrecio( ) );
    }

    @Test
    public void testGetPrecioSinDescuento( )
    {
        Combo sinDescuento = new Combo( "Sin descuento", 0, items );
        assertEquals( 18000, sinDescuento.getPrecio( ) );
    }
}