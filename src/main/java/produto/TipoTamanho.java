package produto;

import java.util.Arrays;

public enum TipoTamanho {
    P(0.0){
        @Override
        public double getPreco(double precoBase) {
            return precoBase;
        }
    },
    M(0.30){
        @Override
        public double getPreco(double precoBase) {
            return precoBase + (precoBase * M.multiplicador);
        }
    },
    G(0.50){
        @Override
        public double getPreco(double precoBase) {
            return precoBase + (precoBase * G.multiplicador);
        }
    };
    public final double multiplicador;

    TipoTamanho(double multiplicador){
        this.multiplicador = multiplicador;
    }

    public abstract double getPreco(double precoBase);


}
