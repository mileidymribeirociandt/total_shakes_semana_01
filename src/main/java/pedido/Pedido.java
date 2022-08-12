package pedido;

import ingredientes.Adicional;
import java.util.List;

public class Pedido {

    private int id;
    private List<ItemPedido> itens;
    private Cliente cliente;

    public Pedido(int id, List<ItemPedido> itens, Cliente cliente) {
        this.id = id;
        this.itens = itens;
        this.cliente = cliente;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public int getId() {
        return this.id;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    public double calcularTotal(Cardapio cardapio) {
        double total = 0;
        total += itens.stream().map(itemPedido -> {
            double subTotal = itemPedido.getShake().getTipoTamanho().getPreco(cardapio.buscarPreco(itemPedido.getShake().getBase()));
            if (itemPedido.getShake().getAdicionais() == null || itemPedido.getShake().getAdicionais().isEmpty()) {
                return subTotal * itemPedido.getQuantidade();
            }
            for (Adicional adicional : itemPedido.getShake().getAdicionais()) {
                subTotal += cardapio.buscarPreco(adicional);
            }
            return subTotal * itemPedido.getQuantidade();
        }).reduce(0.0, Double::sum);
        return total;
    }

    public void adicionarItemPedido(ItemPedido itemPedidoAdicionado) {
        if (itens == null) {
            throw new NullPointerException("Can't add new ItemPedido to null list");
        }
        itens.stream().filter(itemPedido -> itemPedido.equals(itemPedidoAdicionado)).findFirst().ifPresentOrElse(itemPedido -> itemPedido.updateQuantidade(itemPedidoAdicionado.getQuantidade()), () -> {
            itens.add(itemPedidoAdicionado);
        });
    }

    public boolean removeItemPedido(ItemPedido itemPedidoRemovido) {
        itens.stream().filter(itemPedido -> itemPedido.equals(itemPedidoRemovido)).findFirst().ifPresentOrElse(itemPedido -> {
            itemPedido.updateQuantidade(-1);
            if (itemPedido.getQuantidade() <= 0) {
                itens.remove(itemPedidoRemovido);
            }
        }, () -> {
            throw new IllegalArgumentException("Item nao existe no pedido.");
        });
        return true;
    }

    @Override
    public String toString() {
        return this.itens + " - " + this.cliente;
    }
}
