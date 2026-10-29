package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;

public class IngredienteTest
{
    private Ingrediente ingrediente1;

    @BeforeEach
    void setUp( ) throws Exception
    {
        ingrediente1 = new Ingrediente( "lechuga", 1500 );
    }

    @AfterEach
    void tearDown( ) throws Exception
    {
    }

    @Test
    void testGetNombre( )
    {
        assertEquals( "lechuga", ingrediente1.getNombre( ), "El nombre del ingrediente es incorrecto." );
    }

    @Test
    void testGetCostoAdicional( )
    {
        assertEquals( 1500, ingrediente1.getCostoAdicional( ), "El costo adicional no coincide." );
    }
    @Test
    void testIngredienteSinCosto( )
    {
        Ingrediente sinCosto = new Ingrediente( "cebolla", 0 );
        assertEquals( "cebolla", sinCosto.getNombre( ) );
        assertEquals( 0, sinCosto.getCostoAdicional( ) );
    }

}
