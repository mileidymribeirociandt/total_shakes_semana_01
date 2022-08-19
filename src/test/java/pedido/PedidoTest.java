package pedido;

import exception.ItemNotFoundException;
import ingredientes.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import produto.Shake;
import produto.TipoTamanho;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PedidoTest{

    Pedido pedido;
    Cardapio cardapio;

    @BeforeAll
    void setup(){
        cardapio = new Cardapio();
        cardapio.adicionarIngrediente(new Base(TipoBase.IOGURTE), 10.0);
        cardapio.adicionarIngrediente(new Base(TipoBase.SORVETE), 5.0);
        cardapio.adicionarIngrediente(new Fruta(TipoFruta.BANANA), 1.0);
        cardapio.adicionarIngrediente(new Fruta(TipoFruta.MORANGO), 10.0);
        cardapio.adicionarIngrediente(new Topping(TipoTopping.AVEIA), 2.0);
        cardapio.adicionarIngrediente(new Topping(TipoTopping.MEL), 1.0);
        cardapio.adicionarIngrediente(new Topping(TipoTopping.CHOCOLATE), 100.0);
    }

    @BeforeEach
    void resetPedido(){
        pedido = new Pedido(1, new ArrayList<>(), new Cliente(
                1,
                "Cliente Test",
                "cliente.test@email.com"
        ));
    }

    @Test
    @DisplayName("Test adding new item on pedido")
    void shouldAddItemOnPedido(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);

        pedido.adicionarItemPedido(itemPedido);

        assertEquals(1, pedido.getItens().size());
        pedido.getItens().forEach(item -> {
            assertEquals(new Base(TipoBase.SORVETE), item.getShake().getBase());
            assertEquals(new Fruta(TipoFruta.MORANGO), item.getShake().getFruta());
            assertEquals(new Topping(TipoTopping.MEL), item.getShake().getTopping());
            assertEquals(2, item.getShake().getAdicionais().size());
            assertEquals(new Topping(TipoTopping.AVEIA), item.getShake().getAdicionais().get(0));
            assertEquals(new Fruta(TipoFruta.BANANA), item.getShake().getAdicionais().get(1));
            assertEquals(TipoTamanho.P, item.getShake().getTipoTamanho());
            assertEquals(1, item.getQuantidade());
        });
    }

    @Test
    @DisplayName("Test updating item quantity on pedido")
    void shouldUpdateItemQuantityOnPedido(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);
        ItemPedido itemPedido2 = new ItemPedido(shake, 2);

        pedido.adicionarItemPedido(itemPedido);
        pedido.adicionarItemPedido(itemPedido2);

        assertEquals(1, pedido.getItens().size());
        pedido.getItens().forEach(item -> {
            assertEquals(new Base(TipoBase.SORVETE), item.getShake().getBase());
            assertEquals(new Fruta(TipoFruta.MORANGO), item.getShake().getFruta());
            assertEquals(new Topping(TipoTopping.MEL), item.getShake().getTopping());
            assertEquals(new ArrayList<>(), item.getShake().getAdicionais());
            assertEquals(TipoTamanho.P, item.getShake().getTipoTamanho());
            assertEquals(3, item.getQuantidade());
        });
    }

    @Test
    @DisplayName("Test different items on pedido")
    void shouldAddDifferentItemsOnPedido(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        Shake shake2 = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.MEL))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);
        ItemPedido itemPedido2 = new ItemPedido(shake2, 2);

        pedido.adicionarItemPedido(itemPedido);
        pedido.adicionarItemPedido(itemPedido2);

        assertEquals(2, pedido.getItens().size());
        assertEquals(itemPedido, pedido.getItens().get(0));
        assertEquals(itemPedido2, pedido.getItens().get(1));
    }

    @Test
    @DisplayName("Test removing item of pedido")
    void shouldRemoveItemOfPedido(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);

        pedido.adicionarItemPedido(itemPedido);
        pedido.removeItemPedido(itemPedido);

        assertEquals(0, pedido.getItens().size());
    }

    @Test
    @DisplayName("Tem removing item of pedido when quantity is bigger than one")
    void shouldRemoveOneUnitOfItem_whenItemQuantityIsBiggerThanOne(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        Shake shakeRemovido = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Topping(TipoTopping.AVEIA), new Fruta(TipoFruta.BANANA))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 3);
        ItemPedido itemPedidoRemovido = new ItemPedido(shakeRemovido, 10);

        pedido.adicionarItemPedido(itemPedido);
        pedido.removeItemPedido(itemPedidoRemovido);

        assertEquals(1, pedido.getItens().size());
        assertEquals(2, pedido.getItens().get(0).getQuantidade());
    }

    @Test
    @DisplayName("Test removing item of pedido when quantity is one")
    void shouldRemoveOneUnitOfItem_whenItemQuantityIsOne(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        Shake shakeRemovido = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Topping(TipoTopping.AVEIA), new Fruta(TipoFruta.BANANA))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);
        ItemPedido itemPedidoRemovido = new ItemPedido(shakeRemovido, 10);

        pedido.adicionarItemPedido(itemPedido);
        pedido.removeItemPedido(itemPedidoRemovido);

        assertEquals(0, pedido.getItens().size());
    }

    @Test
    @DisplayName("Test removing a non existing item of pedido")
    void shouldThrowException_whenTryingToRemoveANonExistingItem(){
        Shake shake = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Fruta(TipoFruta.BANANA), new Topping(TipoTopping.AVEIA))),
                TipoTamanho.P
        );

        Shake shakeRemovido = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.AVEIA),
                new ArrayList<>(List.of(new Topping(TipoTopping.AVEIA), new Fruta(TipoFruta.BANANA))),
                TipoTamanho.P
        );

        ItemPedido itemPedido = new ItemPedido(shake, 1);
        ItemPedido itemPedidoRemovido = new ItemPedido(shakeRemovido, 10);

        pedido.adicionarItemPedido(itemPedido);
        Executable executable = () -> pedido.removeItemPedido(itemPedidoRemovido);

        ItemNotFoundException itemNotFoundException = assertThrows(ItemNotFoundException.class, executable);
        assertEquals("Item nao existe no pedido.", itemNotFoundException.getMessage());

    }

    @Test
    @DisplayName("Test calculating the total price with additional items")
    void shouldCalculateTheTotalPriceWithAdditionalItems(){
        Shake shake1 = new Shake(new Base(TipoBase.IOGURTE),
                new Fruta(TipoFruta.BANANA),
                new Topping(TipoTopping.AVEIA),
                new ArrayList<>(List.of(new Topping(TipoTopping.MEL))),
                TipoTamanho.G); //16

        Shake shake2 = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(List.of(new Topping(TipoTopping.CHOCOLATE))),
                TipoTamanho.M); // 106.5

        ItemPedido itemPedido1 = new ItemPedido(shake1, 1);
        ItemPedido itemPedido2 = new ItemPedido(shake2, 2);

        pedido.adicionarItemPedido(itemPedido1);
        pedido.adicionarItemPedido(itemPedido2);

        assertEquals(229, pedido.calcularTotal(cardapio));
    }

    @Test
    @DisplayName("Test calculating the total price without additional items")
    void shouldCalculateTheTotalPriceWithoutAdditionalItems(){
        Shake shake1 = new Shake(new Base(TipoBase.IOGURTE),
                new Fruta(TipoFruta.BANANA),
                new Topping(TipoTopping.AVEIA),
                new ArrayList<>(),
                TipoTamanho.G); //15

        Shake shake2 = new Shake(new Base(TipoBase.SORVETE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.MEL),
                new ArrayList<>(),
                TipoTamanho.P); // 5

        ItemPedido itemPedido1 = new ItemPedido(shake1, 1);
        ItemPedido itemPedido2 = new ItemPedido(shake2, 2);

        pedido.adicionarItemPedido(itemPedido1);
        pedido.adicionarItemPedido(itemPedido2);

        assertEquals(25, pedido.calcularTotal(cardapio));
    }

}