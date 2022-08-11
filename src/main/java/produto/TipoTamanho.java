package produto;

import java.util.Arrays;

public enum TipoTamanho {
    P(0.0),
    M(0.30),
    G(0.50);
    public final double multiplicador;

    TipoTamanho(double multiplicador){
        this.multiplicador = multiplicador;
    }

    public static double getPreco(double precoBase, TipoTamanho tipoTamanho){
        for(TipoTamanho tamanho : values()){
            if(tamanho.equals(tipoTamanho)){
                return precoBase + (precoBase + tamanho.multiplicador);
            }
        }
        throw new IllegalArgumentException("TipoTamanho is not present in list");
    }


}
