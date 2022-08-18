package armazem;
import ingredientes.Ingrediente;

import java.util.Map;
import java.util.TreeMap;

public class Armazem {
    private Map<Ingrediente, Integer> estoque;

    public Armazem(){
        estoque = new TreeMap<>();
    }

    public void cadastrarIngredienteEmEstoque(Ingrediente ingredient) {
        if(estoque.containsKey(ingredient)){
            throw new IllegalArgumentException("Ingrediente já cadastrado");
        }
        estoque.put(ingredient, 0);
    }

    public boolean isAvailable(Ingrediente ingredient) {
        return estoque.containsKey(ingredient);
    }

    public void descadastrarIngredienteEmEstoque(Ingrediente ingredient) {
        if(!estoque.containsKey(ingredient)){
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }
        estoque.remove(ingredient);
    }

    public void adicionarQuantidadeDoIngredienteEmEstoque(Ingrediente ingredient, Integer quantidade) {
        if(!isQuantityAboveZero(quantidade)){
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if(!estoque.containsKey(ingredient)){
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }
        estoque.put(ingredient, quantidade + estoque.get(ingredient));
    }

    public int consultarQuantidadeDoIngredienteEmEstoque(Ingrediente ingredient) {
        if(!estoque.containsKey(ingredient)){
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }
        return estoque.get(ingredient);
    }

    public void reduzirQuantidadeDoIngredienteEmEstoque(Ingrediente ingredient, Integer quantidade) {
        if(!estoque.containsKey(ingredient)){
            throw new IllegalArgumentException("Ingrediente não encontrado");
        }
        if((estoque.get(ingredient) - quantidade) < 0){
            throw new IllegalArgumentException("Quantidade invalida");
        }
        if(!isQuantityAboveZero(quantidade)){
            throw new IllegalArgumentException("Quantidade invalida");
        }
        estoque.put(ingredient, estoque.get(ingredient) - quantidade);
    }

    private boolean isQuantityAboveZero(Integer quantity){
        if(quantity <= 0){
            return false;
        }
        return true;
    }
}
