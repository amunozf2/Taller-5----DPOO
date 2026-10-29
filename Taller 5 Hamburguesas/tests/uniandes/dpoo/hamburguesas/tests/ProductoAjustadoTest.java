package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ProductoAjustadoTest
{
    private ProductoMenu productoBase;
    private ProductoAjustado productoAjustado;
    private Ingrediente tocineta;
    private Ingrediente queso;
    private Ingrediente tomate;

    @BeforeEach
    public void setUp( )
    {
        productoBase = new ProductoMenu( "corralita", 13000 );
        productoAjustado = new ProductoAjustado( productoBase );

        tocineta = new Ingrediente( "tocineta express", 2500 );
        queso = new Ingrediente( "queso mozzarella", 2500 );
        tomate = new Ingrediente( "tomate", 1000 );
    }

    @Test
    public void testGetNombre( )
    {
        assertEquals( "corralita", productoAjustado.getNombre( ) );
    }

    @Test
    public void testGetPrecioSinAjustes( )
    {
        assertEquals( 13000, productoAjustado.getPrecio( ) );
    }

    @Test
    public void testGetPrecioConAgregados( )
    {
        productoAjustado.agregarIngrediente( tocineta );
        productoAjustado.agregarIngrediente( queso );
        assertEquals( 18000, productoAjustado.getPrecio( ) );
    }

    @Test
    public void testGetPrecioConIngredienteRepetido( )
    {
        productoAjustado.agregarIngrediente( tocineta );
        productoAjustado.agregarIngrediente( tocineta );
        assertEquals( 18000, productoAjustado.getPrecio( ) );
    }

    @Test
    public void testGetPrecioConEliminadosNoCambia( )
    {
        productoAjustado.eliminarIngrediente( tomate );
        assertEquals( 13000, productoAjustado.getPrecio( ) );
    }

    @Test
    public void testGetPrecioConAgregadosYEliminados( )
    {
        productoAjustado.agregarIngrediente( tocineta );
        productoAjustado.eliminarIngrediente( tomate );
        assertEquals( 15500, productoAjustado.getPrecio( ) );
    }

    @Test
    public void testGenerarTextoFacturaSinAjustes( )
    {
        String esperado = "corralita\n" + "            13000\n" + "            13000\n";
        assertEquals( esperado, productoAjustado.generarTextoFactura( ) );
    }

    @Test
    public void testGenerarTextoFacturaConAjustes( )
    {
        productoAjustado.agregarIngrediente( tocineta );
        productoAjustado.eliminarIngrediente( tomate );

        String esperado = "corralita\n"
                + "            13000\n"
                + "    +tocineta express                2500\n"
                + "    -tomate\n"
                + "            15500\n";
        assertEquals( esperado, productoAjustado.generarTextoFactura( ) );
    }
}