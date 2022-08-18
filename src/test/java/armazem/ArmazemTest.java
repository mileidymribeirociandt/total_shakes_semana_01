package armazem;

import ingredientes.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ArmazemTest {
    private Armazem armazem;

    @BeforeEach
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
    @DisplayName("Throw exception when trying to register an ingredient that is already registered")
    void shouldThrowException_whenIngredientIsAlreadyRegistered(){
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(new Fruta(TipoFruta.BANANA));
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente já cadastrado", expectedIllegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Register an ingredient that is not registered")
    void shouldRegisterIngredient_whenItsNotRegistered(){
        Ingrediente ingredient = new Base(TipoBase.LEITE);
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertTrue(armazem.isAvailable(ingredient));
    }

    @Test
    @DisplayName("Throw exception when trying to unregister an ingredient that is not registered")
    void shouldThrowException_whenTryToUnregisterAnIngredientThatIsNotRegistered(){
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(new Base(TipoBase.IOGURTE));
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedIllegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Unregister an registed ingredient")
    void shouldUnregisterIngredient_whenItsRegistered(){
        Ingrediente ingredient = new Base(TipoBase.SORVETE);
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertFalse(armazem.isAvailable(ingredient));
    }

    @Test
    @DisplayName("Increment quantity from registered ingredient")
    void shouldIncrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 12);
    }

    @Test
    @DisplayName("Throw exception when incrementing an invalid quantity (above or equals zero)")
    void shouldThrowException_whenQuantityToIncrementIsEqualOrAboveZero(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityEqualsZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 0);
        Executable executableQuantityAboveZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient,-2);
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQuantityEqualsZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
        expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQuantityAboveZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Throw exception when incrementing an unregistered ingredient")
    void shouldThrowException_whenIngredientToIncrementIsNotRegistered(){
        Ingrediente ingredient = new Base(TipoBase.IOGURTE);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }

    @Test
    @DisplayName("Decrement quantity from registered ingredient")
    void shouldDecrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingredient = new Fruta(TipoFruta.BANANA);
        Executable executable = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 8);
    }

    @Test
    @DisplayName("Throw exception when decrementing an unregistered ingredient")
    void shouldThrowException_whenIngredientToDecrementIsNotRegistered(){
        Ingrediente ingredient = new Topping(TipoTopping.MEL);
        Executable executable = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 2);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }

    @Test
    @DisplayName("Throw exception when decrementing an invalid quantity (above or equals zero)")
    void shouldThrowException_whenQuantityToDecrementIsEqualOrAboveZero(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityEqualsZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient, 0);
        Executable executableQuantityAboveZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient,-2);
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQuantityEqualsZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
        expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQuantityAboveZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Throw exception when decrementing an invalid quantity (bigger than stock)")
    void shouldThrowException_whenQuantityToDecrementIsBiggerThanStock(){
        Ingrediente ingredient = new Topping(TipoTopping.AVEIA);
        Executable executableQuantityBiggerThanStock= () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingredient,14);
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQuantityBiggerThanStock);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
    }

    @Test
    @DisplayName("Return quantity when ingredient is registered")
    void shouldReturnQuantity_whenIngredientIsRegistered(){
        Ingrediente ingredient = new Fruta(TipoFruta.ABACATE);
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient) == 10);
    }

    @ParameterizedTest(name = "Test search for quantity when {0} isn't registered")
    @MethodSource("getNonRegisteredIngredientsList")
    @DisplayName("Throw exception in searching a non existing ingredient's quantity")
    void showThrowException_whenReturningQuantityOfIngredientThatDoesNotExists(Ingrediente ingredient){
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingredient);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }

    private static List<Ingrediente> getNonRegisteredIngredientsList(){
        return List.of(
                new Base(TipoBase.IOGURTE),
                new Base(TipoBase.LEITE),
                new Fruta(TipoFruta.MORANGO),
                new Topping(TipoTopping.CHOCOLATE)
        );
    }
}
