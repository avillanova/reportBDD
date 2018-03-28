package model;

public class Categoria {
    private String nome;

    public Categoria(String categoria){
        nome = categoria;
    }


    public String getNome() {
        return nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Categoria)) {
            return false;
        }
        final Categoria other = (Categoria) obj;
        return this.getNome().equals(other.getNome());
    }
}
