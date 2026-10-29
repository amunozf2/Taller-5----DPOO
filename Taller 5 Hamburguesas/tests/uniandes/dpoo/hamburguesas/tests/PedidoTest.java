package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {

    private Pedido pedido;
    private ProductoMenu producto1;
    private ProductoMenu producto2;

    @BeforeEach
    public void setUp() {
        pedido = new Pedido("Mariana Hernandez", "Calle 100 # 15-20");
        producto1 = new ProductoMenu("corral", 14000);
        producto2 = new ProductoMenu("papas medianas", 5500);
    }

    @Test
    public void testGetIdPedido() {
        Pedido otro = new Pedido("Maria", "Calle 120");
        assertNotEquals(pedido.getIdPedido(), otro.getIdPedido(), "Dos pedidos no pueden tener el mismo id.");
    }
    @Test
    public void testGetNombreCliente() {
        assertEquals("Mariana Hernandez", pedido.getNombreCliente(), "El nombre del cliente debe coincidir.");
    }

    @Test
    public void testAgregarProductosYCalculos() {
        pedido.agregarProducto(producto1);
        pedido.agregarProducto(producto2);
       
        String factura = pedido.generarTextoFactura();
        assertNotNull(factura, "El texto de la factura no debe ser nulo.");
        assertTrue(factura.contains("Precio Neto:  19500"), "Debe tener el precio neto correcto.");
        assertTrue(factura.contains("IVA:          3705"), "Debe tener el cálculo exacto del IVA.");
        assertTrue(factura.contains("Precio Total: 23205"), "Debe tener el precio total correcto.");
    }

    @Test
    public void testGenerarTextoFacturaCompleta() {
        pedido.agregarProducto(producto1);
        String factura = pedido.generarTextoFactura();
        
        assertTrue(factura.contains("Cliente: Mariana Hernandez"), "La factura debe incluir el cliente.");
        assertTrue(factura.contains("Dirección: Calle 100 # 15-20"), "La factura debe incluir la dirección.");
        assertTrue(factura.contains("corral"), "La factura debe incluir el producto agregado.");
    }

    @Test
    public void testGuardarFactura() throws FileNotFoundException {
        pedido.agregarProducto(producto1);
        
        File archivo = new File("factura_test.txt");
        pedido.guardarFactura(archivo);
        
        assertTrue(archivo.exists(), "El archivo de la factura se creo correctamente.");
        
        if (archivo.exists()) {
            archivo.delete();
        }
        
    } 
    @Test
    public void testPedidoVacio( )
    {
        assertEquals( 0, pedido.getPrecioTotalPedido( ) );

        String esperado = "Cliente: Mariana Hernandez\n"
                + "Dirección: Calle 100 # 15-20\n"
                + "----------------\n"
                + "----------------\n"
                + "Precio Neto:  0\n"
                + "IVA:          0\n"
                + "Precio Total: 0\n";
        assertEquals( esperado, pedido.generarTextoFactura( ) );
    }

    @Test
    public void testPrecioTotal( )
    {
        pedido.agregarProducto( producto1 );
        pedido.agregarProducto( producto2 );
        assertEquals( 23205, pedido.getPrecioTotalPedido( ) );
    }

    @Test
    public void testGenerarTextoFacturaExacto( )
    {
        pedido.agregarProducto( producto1 );
        pedido.agregarProducto( producto2 );

        String esperado = "Cliente: Mariana Hernandez\n"
                + "Dirección: Calle 100 # 15-20\n"
                + "----------------\n"
                + "corral\n"
                + "            14000\n"
                + "papas medianas\n"
                + "            5500\n"
                + "----------------\n"
                + "Precio Neto:  19500\n"
                + "IVA:          3705\n"
                + "Precio Total: 23205\n";
        assertEquals( esperado, pedido.generarTextoFactura( ) );
   }
    
}
