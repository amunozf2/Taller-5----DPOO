package uniandes.dpoo.hamburguesas.tests;
 
import static org.junit.jupiter.api.Assertions.*;
 
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
 
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;
 
public class RestauranteTest {
 
    private Restaurante restaurante;
 
    @TempDir
    Path dirTemporal;
 
    @BeforeEach
    public void setUp() {
        restaurante = new Restaurante();
    }
 
    private File crearArchivoTemporal( String nombre, String contenido ) throws IOException
    {
        File f = dirTemporal.resolve( nombre ).toFile( );
        Files.write( f.toPath( ), contenido.getBytes( ) );
        return f;
    }
 
    @Test
    public void testCargarInformacionRestaurante() throws Exception {
        File archivoIngredientes = new File("data/ingredientes.txt");
        File archivoMenu = new File("data/menu.txt");
        File archivoCombos = new File("data/combos.txt");
 
        restaurante.cargarInformacionRestaurante(archivoIngredientes, archivoMenu, archivoCombos);
 
        assertNotNull(restaurante.getMenuBase(), "El menú no pueden ser nulo.");
        assertFalse(restaurante.getMenuBase().isEmpty(), "El menú base debe tener productos.");
 
        assertNotNull(restaurante.getMenuCombos(), "Los combos no pueden ser nulos.");
        assertFalse(restaurante.getMenuCombos().isEmpty(), "Tiene que haber combos cargados.");
 
        assertNotNull(restaurante.getIngredientes(), "Los ingredientes no pueden ser nulos.");
        assertFalse(restaurante.getIngredientes().isEmpty(), "Debe agregarse ingredientes cargados.");
    }
 
    @Test
    public void testCerrarSinPedidoEnCurso( )
    {
        NoHayPedidoEnCursoException e = assertThrows( NoHayPedidoEnCursoException.class,
                ( ) -> restaurante.cerrarYGuardarPedido( ) );
        assertNotNull( e.getMessage( ) );
    }
 
    @Test
    public void testIniciarPedido() throws Exception {
        restaurante.iniciarPedido("Mariana Hernandez", "Calle 100 # 15-20");
        File factura = new File( "./facturas/factura_" + restaurante.getPedidoEnCurso().getIdPedido() + ".txt" );
        try
        {
            assertNotNull(restaurante.getPedidoEnCurso(), "Debe existir un pedido en curso.");
            assertEquals("Mariana Hernandez", restaurante.getPedidoEnCurso().getNombreCliente(), "El nombre del cliente no coincide.");
 
            restaurante.cerrarYGuardarPedido();
            assertNull(restaurante.getPedidoEnCurso(), "El pedido en curso debe ser nulo después de cerrarlo.");
        }
        finally
        {
            factura.delete( );
        }
    }
 
    @Test
    public void testRestauranteNuevoVacio( )
    {
        assertTrue( restaurante.getPedidos( ).isEmpty( ) );
        assertTrue( restaurante.getIngredientes( ).isEmpty( ) );
        assertTrue( restaurante.getMenuBase( ).isEmpty( ) );
        assertTrue( restaurante.getMenuCombos( ).isEmpty( ) );
        assertNull( restaurante.getPedidoEnCurso( ) );
    }
 
    @Test
    public void testIniciarPedidoConPedidoEnCurso( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 1" );
        YaHayUnPedidoEnCursoException e = assertThrows( YaHayUnPedidoEnCursoException.class,
                ( ) -> restaurante.iniciarPedido( "Luis", "Calle 2" ) );
        assertNotNull( e.getMessage( ) );
        assertEquals( "Ana", restaurante.getPedidoEnCurso( ).getNombreCliente( ),
                "El pedido en curso no debe reemplazarse" );
    }
 
    @Test
    public void testCerrarYGuardarPedido( ) throws Exception
    {
        restaurante.iniciarPedido( "Ana", "Calle 1" );
        Pedido pedido = restaurante.getPedidoEnCurso( );
        File factura = new File( "./facturas/factura_" + pedido.getIdPedido( ) + ".txt" );
        try
        {
            restaurante.cerrarYGuardarPedido( );
 
            assertNull( restaurante.getPedidoEnCurso( ) );
            assertEquals( 1, restaurante.getPedidos( ).size( ), "El pedido cerrado debe quedar en el histórico" );
            assertSame( pedido, restaurante.getPedidos( ).get( 0 ) );
            assertTrue( factura.exists( ), "Debe existir el archivo de la factura" );
            String contenido = new String( Files.readAllBytes( factura.toPath( ) ) );
            assertEquals( pedido.generarTextoFactura( ), contenido );
        }
        finally
        {
            factura.delete( );
        }
    }
 
    @Test
    public void testCargarArchivosCorrectos( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "tocineta;2500\n\nqueso;2000\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\npapas;5500\n" );
        File combos = crearArchivoTemporal( "combos.txt", "combo especial;50%;corral;papas\n" );
 
        restaurante.cargarInformacionRestaurante( ing, menu, combos );
 
        assertEquals( 2, restaurante.getIngredientes( ).size( ), "La línea vacía debe ignorarse" );
        assertEquals( "tocineta", restaurante.getIngredientes( ).get( 0 ).getNombre( ) );
        assertEquals( 2500, restaurante.getIngredientes( ).get( 0 ).getCostoAdicional( ) );
 
        assertEquals( 2, restaurante.getMenuBase( ).size( ) );
        assertEquals( "papas", restaurante.getMenuBase( ).get( 1 ).getNombre( ) );
        assertEquals( 5500, restaurante.getMenuBase( ).get( 1 ).getPrecio( ) );
 
        assertEquals( 1, restaurante.getMenuCombos( ).size( ) );
        assertEquals( "combo especial", restaurante.getMenuCombos( ).get( 0 ).getNombre( ) );
        assertEquals( 9750, restaurante.getMenuCombos( ).get( 0 ).getPrecio( ) );
    }
 
    @Test
    public void testIngredienteRepetido( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "queso;2000\nqueso;2500\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\n" );
        File combos = crearArchivoTemporal( "combos.txt", "" );
        IngredienteRepetidoException e = assertThrows( IngredienteRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ing, menu, combos ) );
        assertNotNull( e.getMessage( ) );
    }
 
    @Test
    public void testProductoRepetido( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "queso;2000\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\ncorral;15000\n" );
        File combos = crearArchivoTemporal( "combos.txt", "" );
        ProductoRepetidoException e = assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ing, menu, combos ) );
        assertEquals( "El producto corral está repetido", e.getMessage( ) );
    }
 
    @Test
    public void testComboRepetido( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "queso;2000\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\n" );
        File combos = crearArchivoTemporal( "combos.txt", "c1;10%;corral\nc1;20%;corral\n" );
        assertThrows( ProductoRepetidoException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ing, menu, combos ) );
    }
 
    @Test
    public void testComboConProductoFaltante( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "queso;2000\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\n" );
        File combos = crearArchivoTemporal( "combos.txt", "c1;10%;corral;pizza\n" );
        ProductoFaltanteException e = assertThrows( ProductoFaltanteException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ing, menu, combos ) );
        assertEquals( "El producto pizza no aparece en la información del restaurante", e.getMessage( ) );
    }
 
    @Test
    public void testArchivoInexistente( )
    {
        File noExiste = new File( "noExiste.txt" );
        assertThrows( IOException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( noExiste, noExiste, noExiste ) );
    }
 
    @Test
    public void testCostoInvalido( ) throws Exception
    {
        File ing = crearArchivoTemporal( "ing.txt", "queso;abc\n" );
        File menu = crearArchivoTemporal( "menu.txt", "corral;14000\n" );
        File combos = crearArchivoTemporal( "combos.txt", "" );
        assertThrows( NumberFormatException.class,
                ( ) -> restaurante.cargarInformacionRestaurante( ing, menu, combos ) );
    }
}
 