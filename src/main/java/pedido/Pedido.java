package pedido;

import ingredientes.Adicional;
import produto.TipoTamanho;

import java.util.ArrayList;
import java.util.List;

public class Pedido{

    private int id;
    private List<ItemPedido> itens;
    private Cliente cliente;

    public Pedido(int id, List<ItemPedido> itens,Cliente cliente){
        this.id = id;
        this.itens=itens;
        this.cliente=cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public int getId(){
        return this.id;
    }

    public Cliente getCliente(){
        return this.cliente;
    }

    public double calcularTotal(Cardapio cardapio){
        double total= 0;
        itens.stream().mapToDouble(itemPedido -> {
            double subTotal = 0.0;
            subTotal += cardapio.buscarPreco(itemPedido.getShake().getTopping());
            subTotal += TipoTamanho.getPreco(cardapio.buscarPreco(itemPedido.getShake().getBase()), itemPedido.getShake().getTipoTamanho());
            subTotal += cardapio.buscarPreco(itemPedido.getShake().getFruta());

            if(itemPedido.getShake().getAdicionais() == null){
                return subTotal;
            }

            for(Adicional adicional : itemPedido.getShake().getAdicionais()){
                subTotal += cardapio.buscarPreco(adicional);
            }

            return subTotal;
        });
        return total;
    }

    public void adicionarItemPedido(ItemPedido itemPedidoAdicionado){
        if(itens == null){
            throw new NullPointerException("Can't add new ItemPedido to null list");
        }
        itens.stream()
                .filter(itemPedido -> itemPedido.equals(itemPedidoAdicionado))
                .findFirst()
                .ifPresentOrElse(itemPedido -> itemPedido.updateQuantidade(itemPedidoAdicionado.getQuantidade()),
                        () ->{itens.add(itemPedidoAdicionado);});
    }

    public boolean removeItemPedido(ItemPedido itemPedidoRemovido) {
        itens.stream()
                .filter(itemPedido -> itemPedido.equals(itemPedidoRemovido))
                .findFirst()
                .ifPresentOrElse(
                        itemPedido -> {
                            itemPedido.updateQuantidade(-1);
                            if(itemPedido.getQuantidade() <=0){
                                itens.remove(itemPedidoRemovido);
                            }
                        },
                        () -> {throw new IllegalArgumentException("Item nao existe no pedido.");}
                );
        return true;
    }

    @Override
    public String toString() {
        return this.itens + " - " + this.cliente;
    }
}
