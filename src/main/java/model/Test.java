package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Test {
    protected String nome;
    protected String status;
    protected Date inicio;
    protected Date fim;
    protected Date duracao;
    private List<Categoria> listCategoria = new ArrayList<Categoria>();


    public void addCategorias(String... categorias){
        for(String cat : categorias){
            Categoria c = new Categoria(cat);
            if(!listCategoria.contains(c)){
                listCategoria.add(c);
            }
        }
    }

    public List<Categoria> getListCategoria() {
        return listCategoria;
    }

    public String getNome() {
        return nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getInicio() {
        return inicio;
    }
}
