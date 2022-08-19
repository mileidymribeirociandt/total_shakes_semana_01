package armazem;

import exception.IngredientNotFoundException;
import exception.InvalidQuantityException;
import exception.RegisteredIngredientException;
import ingredientes.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ArmazemTest {
    private Armazem armazem;

    @BeforeAll
    void setup(){

        armazem = new Armazem();

        Ingrediente sorvete = new Base(TipoBase.SORVETE);
        Ingrediente abacate = new Fruta(TipoFruta.ABACATE);
        Ingrediente banana = new Fruta(TipoFruta.BANANA);
        Ingrediente aveia = new Topping(TipoTopping.AVEIA);

        armazem.cadastrarIngredienteEmEstoque(sorvete);
        armazem.cadastrarIngredienteEmEstoque(abacate);
        armazem.cadastrarIngredienteEmEstoque(banana);
        armazem.cadastrarIngredienteEmEstoque(aveia);

        armazem.adicionarQuantidadeDoIngredienteEmEstoque(sorvete, 10);
        armazem.adicionarQuantidadeDoIngredienteEmEstoque(abacate, 10);
        armazem.adicionarQuantidadeDoIngredienteEmEstoque(banana, 10);
        armazem.adicionarQuantidadeDoIngredienteEmEstoque(aveia, 10);
    }

    @Test
    @DisplayName("Test registering an ingredient that is already registered")
    void shouldThrowException_whenIngredientIsAlreadyRegistered(){
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(new Fruta(TipoFruta.BANANA));
        RegisteredIngredientException registeredIngredientException = assertThrows(RegisteredIngredientException.class, executable);
        assertEquals("Ingrediente já cadastrado", registeredIngredientException.getMessage());
    }

    @Test
    @DisplayName("Test registering an unregistered ingredient")
    void shouldRegisterIngredient_whenItsUnregistered(){
        Ingrediente ingredient = new Base(TipoBase.LEITE);
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertTrue(armazem.isAvailable(ingredient));
    }

    @Test
    @DisplayName("Test unregistering an unregistered ingredient")
    void shouldThrowException_whenTryToUnregisterAnUnregisteredIngredient(){
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(new Base(TipoBase.IOGURTE));
        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente não encontrado", ingredientNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Test unregistering an registered ingredient")
    void shouldUnregisterIngredient_whenItsRegistered(){
        Ingrediente ingredient = new Base(TipoBase.SORVETE);
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertFalse(armazem.isAvailable(ingredient));
    }

    @Test
    @DisplayName("Test incrementing quantity from registered ingredient")
    void shouldIncrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 12);
    }

    @Test
    @DisplayName("Test incrementing an invalid quantity (below or equals zero)")
    void shouldThrowException_whenQuantityToIncrementIsEqualOrBelowZero(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityEqualsZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 0);
        Executable executableQuantityBelowZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient,-2);
        InvalidQuantityException invalidQuantityException = assertThrows(InvalidQuantityException.class, executableQuantityEqualsZero);
        assertEquals("Quantidade invalida", invalidQuantityException.getMessage());
        invalidQuantityException = assertThrows(InvalidQuantityException.class, executableQuantityBelowZero);
        assertEquals("Quantidade invalida", invalidQuantityException.getMessage());
    }

    @Test
    @DisplayName("Test incrementing an unregistered ingredient")
    void shouldThrowException_whenIngredientToIncrementIsNotRegistered(){
        Ingrediente ingredient = new Base(TipoBase.IOGURTE);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente não encontrado", ingredientNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Test decrementing quantity from registered ingredient")
    void shouldDecrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingredient = new Fruta(TipoFruta.BANANA);
        Executable executable = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 8);
    }

    @Test
    @DisplayName("Test decrementing an unregistered ingredient")
    void shouldThrowException_whenIngredientToDecrementIsNotRegistered(){
        Ingrediente ingredient = new Topping(TipoTopping.MEL);
        Executable executable = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente não encontrado", ingredientNotFoundException.getMessage());
    }

    @Test
    @DisplayName("Test decrementing with an invalid quantity (below or equals zero)")
    void shouldThrowException_whenQuantityToDecrementIsEqualOrBelowZero(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityEqualsZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 0);
        Executable executableQuantityBelowZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient,-2);
        InvalidQuantityException invalidQuantityException = assertThrows(InvalidQuantityException.class, executableQuantityEqualsZero);
        assertEquals("Quantidade invalida", invalidQuantityException.getMessage());
        invalidQuantityException = assertThrows(InvalidQuantityException.class, executableQuantityBelowZero);
        assertEquals("Quantidade invalida", invalidQuantityException.getMessage());
    }

    @Test
    @DisplayName("Test decrementing with an invalid quantity (bigger than stock)")
    void shouldThrowException_whenQuantityToDecrementIsBiggerThanStock(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityBiggerThanStock= () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient,14);
        InvalidQuantityException invalidQuantityException = assertThrows(InvalidQuantityException.class, executableQuantityBiggerThanStock);
        assertEquals("Quantidade invalida", invalidQuantityException.getMessage());
    }

    @Test
    @DisplayName("Test searching an existing ingredient's quantity")
    void shouldReturnQuantity_whenIngredientIsRegistered(){
        Ingrediente ingredient = new Fruta(TipoFruta.ABACATE);
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 10);
    }

    @ParameterizedTest(name = "Test searching for quantity when {0} isn't registered")
    @MethodSource("getNonRegisteredIngredient")
    @DisplayName("Test searching a non existing ingredient's quantity")
    void showThrowException_whenReturningQuantityOfIngredientThatDoesNotExists(Ingrediente ingredient){
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient);
        IngredientNotFoundException ingredientNotFoundException = assertThrows(IngredientNotFoundException.class, executable);
        assertEquals("Ingrediente não encontrado", ingredientNotFoundException.getMessage());
    }

    private static List<Ingrediente> getNonRegisteredIngredient(){
        return List.of(
                new Base(TipoBase.IOGURTE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.CHOCOLATE)
        );
    }
}
