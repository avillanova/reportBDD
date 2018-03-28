package model;

import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class FuncionalidadeTest {

    @Test
    public void Test(){
        Funcionalidade func = new Funcionalidade("Funcionalidade 1");
        func.addCategorias("Test1", "Test2", "Test2", "Test4");

        Cenario cen = new Cenario("Cenario Teste");
        func.addCenario(cen);

        cen.addCategorias("Cenario1");
        Step step = new Step("Step 1");
        cen.addStep(step);
        step = new Step("Step 2");
        cen.addStep(step);
        step = new Step("Step 3");
        cen.addStep(step);
        step = new Step("Step 4");
        cen.addStep(step);

        cen = new Cenario("Cenario Teste 2");
        func.addCenario(cen);
        cen.addCategorias("Cenario Categoria", "Cenario 2");
        step = new Step("Step 1");
        cen.addStep(step);
        step = new Step("Step 2");
        cen.addStep(step);
        step = new Step("Step 3");
        cen.addStep(step);
        step = new Step("Step 4");
        cen.addStep(step);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

        System.out.println("-------------------LISTANDO TUDO-------------------");
        System.out.print("Feature: "+func.getNome()+" "+df.format(func.getInicio())+"\n\t\tCategorias: ");
        for(Categoria cat : func.getListCategoria()){
            System.out.print("\t\t"+cat.getNome());
        }

        for(Cenario c : func.getCenarios()){
            System.out.print("\nCenario: "+c.getNome()+"\n\t\tCategorias Cenario: ");
            for(Categoria cat : c.getListCategoria()){
                System.out.print("\t\t"+cat.getNome());
            }
            System.out.println("\nSteps: ");
            for(Step s : c.getListStep()){
                System.out.println("\t\t"+s.getNome());
            }
        }

    }

}