package model;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Test {
    private String nome;
    private String status;
    private Date inicio;
    private Date fim;
    private long duracao;
    private String prioridade;
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

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(LogStatus status) {
        this.status = status.toString();
    }

    public Date getInicio() {
        return inicio;
    }


    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFim() {
        return fim;
    }

    public void setFim(Date fim) {
        this.fim = fim;
        duracao = fim.getTime() - inicio.getTime();
    }

    public String getDuracao() {
        if (duracao < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long hours = TimeUnit.MILLISECONDS.toHours(duracao) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duracao) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duracao) % 60;
        long milliseconds = duracao % 1000;
        return String.format("%dh %dm %ds+%dms",
                hours, minutes, seconds, milliseconds);
    }


    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(LogPriority prioridade) {
        this.prioridade = prioridade.toString();
    }
}
