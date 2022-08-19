package pedido;

import exception.IngredientNotFoundException;
import exception.InvalidPriceException;
import ingredientes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CardapioTest {

    Cardapio cardapio;

    @BeforeEach
    void resetCardapio(){
        cardapio = new Cardapio();
    }

    @ParameterizedTest(name = "Adding a non existing ingredient when its {0} with valid price {1}")
    @MethodSource("getIngredientAndValidPrice")
    @DisplayName("Test adding a non existing ingredient with valid price")
    void shouldAddIngredient_whenItsNotRegistered(Ingrediente ingredient, Double price){
        cardapio.adicionarIngrediente(ingredient, price);
        assertTrue(cardapio.isAvailable(ingredient));
        assertEquals(price, cardapio.buscarPreco(ingredient));
    }

    @ParameterizedTest(name = "Adding a non existing ingredient when its {0} with invalid price {1}")
    @MethodSource("getIngredientAndInvalidPrice")
    @DisplayName("Test adding a non existing ingredient with invalid price")
    void shouldThrowException_whenAddingIngredientsWithInvalidPrice(Ingrediente ingredient, Double price){
        Executable executable = () -> cardapio.adicionarIngrediente(ingredient, price);
        InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class, executable);
        assertEquals("Preco invalido.", invalidPriceException.getMessage());
    }

    @Test
    @DisplayName("Test updating an existing ingredient with valid price")
    void shouldUpdateIngredient_whenItsAValidPrice(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);
        cardapio.adicionarIngrediente(ingrediente, 1.0);

        cardapio.atualizarIngrediente(new Base(TipoBase.IOGURTE), 9.0);

        assertTrue(cardapio.isAvailable(new Base(TipoBase.IOGURTE)));
        assertEquals(9.0, cardapio.getPrecos().get(new Base(TipoBase.IOGURTE)));
    }

    @Test
    @DisplayName("Test updating an existing ingredient with invalid price")
    void shouldThrowException_whenTryingToUpdateIngredientWithInvalidPrice(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);
        cardapio.adicionarIngrediente(ingrediente, 10.0);

        Executable executableBelowZero = () -> cardapio.atualizarIngrediente(ingrediente, -8.0);
        Executable executableEqualsZero = () -> cardapio.atualizarIngrediente(ingrediente, 0.0);

        InvalidPriceException invalidPriceException = assertThrows(InvalidPriceException.class, executableBelowZero);
        assertEquals("Preco invalido.", invalidPriceException.getMessage());
        invalidPriceException = assertThrows(InvalidPriceException.class, executableEqualsZero);
        assertEquals("Preco invalido.", invalidPriceException.getMessage());
    }

    @ParameterizedTest(name = "Updating a non existing ingredient when its {0}")
    @MethodSource("getNonExistingIngredient")
    @DisplayName("Test updating a non existing ingredient")
    void shouldThrowException_whenUpdatingANonExistingIngredient(Ingrediente ingredient){
        Ingrediente base = new Base(TipoBase.IOGURTE);
        Ingrediente fruta = new Fruta(TipoFruta.MORANGO);
        Ingrediente topping = new Topping(TipoTopping.AVEIA);

        cardapio.adicionarIngrediente(base, 9.0);
        cardapio.adicionarIngrediente(fruta, 16.0);
        cardapio.adicionarIngrediente(topping, 10.0);
        Executable executable = () -> cardapio.atualizarIngrediente(ingredient, 12.3);

        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente nao existe no cardapio.", ingredientNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Test removing an existing ingredient")
    void shouldRemoveIngredient_whenItIsPresentOnMenu(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);

        cardapio.adicionarIngrediente(ingrediente, 1.0);
        cardapio.removerIngrediente(new Base(TipoBase.IOGURTE));

        Executable executable = () ->  cardapio.buscarPreco(ingrediente);
        assertThrows(IngredientNotFoundException.class, executable);
    }

    @Test
    @DisplayName("Test matching prices when ingredient is present in menu")
    void shouldMatchPrice_whenItIsPresentInMenu(){

        Ingrediente base = new Base(TipoBase.IOGURTE);
        Ingrediente fruta = new Fruta(TipoFruta.MORANGO);
        Ingrediente topping = new Topping(TipoTopping.MEL);

        cardapio.adicionarIngrediente(base, 1.0);
        cardapio.adicionarIngrediente(fruta, 5.0);
        cardapio.adicionarIngrediente(topping, 10.0);

        assertAll(
                () -> assertTrue(cardapio.isAvailable(base)),
                () -> assertEquals(cardapio.buscarPreco(new Base(TipoBase.IOGURTE)),1.0),
                () -> assertTrue(cardapio.isAvailable(fruta)),
                () -> assertEquals(cardapio.buscarPreco(new Fruta(TipoFruta.MORANGO)),5.0),
                () -> assertTrue(cardapio.isAvailable(topping)),
                () -> assertEquals(cardapio.buscarPreco(new Topping(TipoTopping.MEL)),10.0)
        );

    }

    @Test
    @DisplayName("Test removing a non existing ingredient")
    void shouldThrowException_whenRemovingNonExistingIngredient(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);

        cardapio.adicionarIngrediente(ingrediente, 1.0);
        Executable executable = () -> cardapio.buscarPreco(new Base(TipoBase.SORVETE));

        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente nao existe no cardapio.", ingredientNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Test searching for an existing ingredient")
    void shouldFindIngredient_whenSearchingForAnExistingOne(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);

        cardapio.adicionarIngrediente(ingrediente, 1.0);

        assertEquals(1.0, cardapio.buscarPreco(new Base(TipoBase.IOGURTE)));
    }

    @Test
    @DisplayName("Test searching for a non existing ingredient")
    void shouldThrownException_whenSearchingForNonExistingIngredient(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);

        cardapio.adicionarIngrediente(ingrediente, 1.0);
        Executable executable = () -> cardapio.buscarPreco(new Base(TipoBase.SORVETE));

        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente nao existe no cardapio.", ingredientNotFoundException.getMessage());
    }

    private List<Ingrediente> getNonExistingIngredient(){
        return List.of(
                new Fruta(TipoFruta.BANANA),
                new Topping(TipoTopping.CHOCOLATE)
        );
    }

    private Stream<Arguments> getIngredientAndInvalidPrice(){
        return Stream.of(
                Arguments.of(new Base(TipoBase.IOGURTE), -2.8),
                Arguments.of(new Fruta(TipoFruta.MORANGO), -3.6)
        );
    }

    private Stream<Arguments> getIngredientAndValidPrice(){
        return Stream.of(
                Arguments.of(new Base(TipoBase.IOGURTE), 8.8),
                Arguments.of(new Fruta(TipoFruta.MORANGO), 12.6),
                Arguments.of(new Topping(TipoTopping.CHOCOLATE), 7.6),
                Arguments.of(new Base(TipoBase.LEITE), 7.6)
        );
    }

}