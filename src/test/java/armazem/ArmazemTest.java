package armazem;

import armazem.*;
import ingredientes.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class ArmazemTest {
    private Armazem armazem;

    @BeforeAll
    static void setup(){
        Ingrediente sorvete = new Base(TipoBase.SORVETE);
        Ingrediente abacate = new Fruta(TipoFruta.ABACATE);
        Ingrediente banana = new Fruta(TipoFruta.BANANA);
        Ingrediente aveia = new Topping(TipoTopping.AVEIA);

        armazem.cadastrarIngredienteEmEstoque(sorvete);
        armazem.cadastrarIngredienteEmEstoque(abacate);
        armazem.cadastrarIngredienteEmEstoque(banana);
        armazem.cadastrarIngredienteEmEstoque(aveia);

        adicionarQuantidadeDoIngredienteEmEstoque.cadastrarIngredienteEmEstoque(sorvete, 10);
        adicionarQuantidadeDoIngredienteEmEstoque.cadastrarIngredienteEmEstoque(abacate, 10);
        adicionarQuantidadeDoIngredienteEmEstoque.cadastrarIngredienteEmEstoque(banana, 10);
        adicionarQuantidadeDoIngredienteEmEstoque.cadastrarIngredienteEmEstoque(aveia, 10);
    }
    @Test
    void shouldThrowException_whenIngredientIsAlreadyRegistered(){
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(new Fruta(TipoFruta.BANANA));
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente já cadastrado", expectedIllegalArgumentException.getMessage());
    }

    @Test
    void shouldRegisterIngredient_whenItsNotRegistered(){
        Ingrediente ingredientToRegister = new Base(TipoBase.LEITE);
        Executable executable = () -> armazem.cadastrarIngredienteEmEstoque(ingredientToRegister);
        assertDoesNotThrow(executable);
        assertTrue(armazem.isAvaiable(ingredientToRegister));
    }

    @Test
    void shouldThrowException_whenTryToUnregisterAnIngredientThatIsNotRegistered(){
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(new Base(TipoBase.IOGURTE));
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedIllegalArgumentException.getMessage());
    }

    @Test
    void shouldUnregisterIngredient_whenItsRegistered(){
        Ingrediente ingredientToUnregister = new Base(TipoBase.SORVETE);
        Executable executable = () -> armazem.descadastrarIngredienteEmEstoque(ingredientToUnregister);
        assertDoesNotThrow(executable);
        assertFalse(armazem.isAvaiable(ingredientToUnregister));
    }

    @Test
    void shouldIncrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingrediente = new Topping(TipoTopping.AVEIA);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingrediente, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingrediente) == 12);
    }

    @Test
    void shouldThrowException_whenQuantityToIncrementIsEqualOrAboveZero(){
        Ingrediente ingrediente = new Topping(TipoTopping.AVEIA);
        Executable executableQtyEqualsZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingrediente, 0);
        Executable executableQtyAboveZero = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingrediente,-2);
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQtyEqualsZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
        expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQtyAboveZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
    }

    @Test
    void shouldThrowException_whenIngredientToIncrementIsNotRegistered(){
        Ingrediente ingrediente = new Base(TipoBase.IOGURTE);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingrediente, 2);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }

    @Test
    void shouldDecrementIngredientQuantity_whenItsRegistered(){
        Ingrediente ingrediente = new Fruta(TipoFruta.BANANA);
        Executable executable = () -> armazem.adicionarQuantidadeDoIngredienteEmEstoque(ingrediente, 2);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingrediente) == 8);
    }

    @Test
    void shouldThrowException_whenIngredientToDecrementIsNotRegistered(){
        Ingrediente ingrediente = new Topping(TipoTopping.MEL);
        Executable executable = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingrediente, 2);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }

    @Test
    void shouldThrowException_whenQuantityToDecrementIsEqualOrAboveZero(){
        Ingrediente ingrediente = new Topping(TipoTopping.AVEIA);
        Executable executableQtyEqualsZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingrediente, 0);
        Executable executableQtyAboveZero = () -> armazem.reduzirQuantidadeDoIngredienteEmEstoque(ingrediente,-2);
        IllegalArgumentException expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQtyEqualsZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
        expectedIllegalArgumentException = assertThrows(IllegalArgumentException.class, executableQtyAboveZero);
        assertEquals("Quantidade invalida", expectedIllegalArgumentException.getMessage());
    }

    @Test
    void shouldReturnQuantity_whenIngredientIsRegistered(){
        Ingrediente ingrediente = new Fruta(TipoFruta.ABACATE);
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingrediente);
        assertDoesNotThrow(executable);
        assertTrue(armazem.consultarQuantidadeDoIngredienteEmEstoque(ingrediente) == 12);
    }

    @Test
    void showThrowException_whenReturningQuantityOfIngredientThatDoesNotExists(){
        Ingrediente ingrediente = new Base(TipoBase.LEITE);
        Executable executable = () -> armazem.consultarQuantidadeDoIngredienteEmEstoque(ingrediente, 2);
        IllegalArgumentException expectedException = assertThrows(IllegalArgumentException.class, executable);
        assertEquals("Ingrediente não encontrado", expectedException.getMessage());
    }
}
