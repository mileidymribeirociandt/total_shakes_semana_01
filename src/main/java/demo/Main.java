package demo;

import application.Application;
import application.impl.ApplicationImpl;
import pedido.Pedido;

import java.time.LocalDate;
import java.util.Iterator;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Application application = ApplicationImpl.getSoleInstance();
        Scanner scan = new Scanner(System.in);
        do{
            application.addPedido();
            System.out.println("Deseja adicionar um novo pedido? \n S - SIM \n N - NÃO");
            System.out.flush();
        }while(scan.next().charAt(0) == 'S');

        application.serializarPedidos();
        mostrarTodosPedidos(application.getPedidos());

        System.out.println("Digite o minuto de serializacao dos pedidos");
        int minuto = scan.nextInt();
        mostrarTodosPedidos(application.desserializarPedidos(LocalDate.now(), minuto));
        System.out.println("Status da desserialização" + application.desserializarPedidos(LocalDate.now(), minuto).equals(application.getPedidos()));
    }
    static void mostrarTodosPedidos(Iterator<Pedido> pedidoIterator){
        while (pedidoIterator.hasNext()){
            System.out.println(pedidoIterator.next().toString());
        }
    }
}
