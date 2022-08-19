package produto;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum TipoTamanho {
    P(1.0, 'P'),
    M(1.30, 'M'),
    G(1.50, 'G');
    public final double multiplicador;
    public final char charTamanho;

    TipoTamanho(double multiplicador, char charTamanho){
        this.multiplicador = multiplicador;
        this.charTamanho = charTamanho;
    }

    public static Double getPreco(TipoTamanho tamanho, double precoBase){
        for(TipoTamanho tipoTamanho : values()){
            if(tipoTamanho.equals(tamanho)){
                return precoBase * tipoTamanho.multiplicador;
            }
        }
        return null;
    }
    public static TipoTamanho getTipoTamanho(char tamanho){
        Optional<TipoTamanho> optTipoTamanho = Arrays.stream(values())
                .filter(tipoTamanho -> tipoTamanho.charTamanho == tamanho)
                .findFirst();

        if(optTipoTamanho.isPresent()){
            return optTipoTamanho.get();
        }
        throw new IllegalArgumentException("Tamanho não existe no cardápio.");
    }


}
