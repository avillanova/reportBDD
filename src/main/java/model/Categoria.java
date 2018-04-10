package model;

public class Categoria {
    private String nome;

    public Categoria(String categoria){
        System.out.println( "Iniciou a categoria"+categoria );
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
