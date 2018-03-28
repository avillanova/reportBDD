package model;

import com.sun.org.apache.xpath.internal.functions.FunctionOneArg;
import org.junit.After;
import org.junit.Test;
import report.Report;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FuncionalidadeTest {
    static Report report;

    @Test
    public void Test() throws InterruptedException {
        report = new Report();

//FUNCIONALIDADE 1
        Funcionalidade func = new Funcionalidade("Funcionalidade 1");
        func.addCategorias("Test1", "Test2", "Test2", "Test4");
        func.setStatus( LogStatus.FAIL );
        func.setPrioridade(LogPriority.HIGHEST);
            Cenario cen = new Cenario("Cenario Teste");
            cen.setStatus( LogStatus.PASS );
            cen.addCategorias("Cenario1");
            cen.setPrioridade(LogPriority.HIGHEST);
                Step step = new Step("Step 1");
                cen.addStep(step);
                step = new Step("Step 2");
                cen.addStep(step);
                step = new Step("Step 3");
                cen.addStep(step);
                step = new Step("Step 4");
                cen.addStep(step);
                Thread.sleep( 1200 );
                cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);

            cen = new Cenario("Cenario Teste 2");
            cen.setStatus( LogStatus.FAIL );
            cen.setPrioridade(LogPriority.LOW);
            cen.addCategorias("Cenario Categoria", "Cenario 2");
                step = new Step("Outro Step 1");
                Thread.sleep( 1500 );
                cen.addStep(step);
                step = new Step("Outro Step 2");
                cen.addStep(step);
                step = new Step("Outro Step 3");
                cen.addStep(step);
                step = new Step("Outro Step 4");
                cen.addStep(step);
                Thread.sleep( 60000 );
                cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);
        func.setFim( new Date(System.currentTimeMillis()) );
        report.addFuncionalidade( func );
    }

    @Test
    public void Test2() throws InterruptedException {
        //FUNCIONALIDADE 2
        Funcionalidade func = new Funcionalidade("Funcionalidade 2");
        func.addCategorias("Test1", "Test2", "Test2", "Test4");
        func.setStatus( LogStatus.FAIL );
        func.setPrioridade(LogPriority.HIGHEST);
            Cenario cen = new Cenario("Cenario Teste");
            cen.setStatus( LogStatus.PASS );
            cen.addCategorias("Cenario1");
            cen.setPrioridade(LogPriority.HIGHEST);
                Step step = new Step("Step 1");
                cen.addStep(step);
                step = new Step("Step 2");
                cen.addStep(step);
                step = new Step("Step 3");
                cen.addStep(step);
                step = new Step("Step 4");
                cen.addStep(step);
                Thread.sleep( 1200 );
            cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);

            cen = new Cenario("Cenario Teste 2");
            cen.setStatus( LogStatus.FAIL );
            cen.setPrioridade(LogPriority.LOW);
            cen.addCategorias("Cenario Categoria", "Cenario 2");
                step = new Step("Outro Step 1");
                Thread.sleep( 1500 );
                cen.addStep(step);
                step = new Step("Outro Step 2");
                cen.addStep(step);
                step = new Step("Outro Step 3");
                cen.addStep(step);
                step = new Step("Outro Step 4");
                cen.addStep(step);
                Thread.sleep( 60000 );
                cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);
        func.setFim( new Date(System.currentTimeMillis()) );
        report.addFuncionalidade( func );
    }

    @After
    public void Print(){
        //PRINTS
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        DateFormat ds = new SimpleDateFormat("HH:mm:ss");
        DateFormat dc = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("-------------------LISTANDO TUDO-------------------");//Verificar aqui
        System.out.println("CONFIGURAÇÕES" );
        System.out.println("\tHost Name: "+report.getHostName() );
        System.out.println("\tUser Name: "+report.getUserName());
        System.out.println("\tOS: "+report.getOS());
        System.out.println("\tHost Date: "+dc.format( report.getDate()));
        System.out.println("\tBrowser: "+report.getBrowser() );
        System.out.println("\tVersion: "+report.getVersion() );

        for(Funcionalidade f : report.getListFuncionalidade()){
            System.out.print("Feature: "+f.getNome()+" "
                    +df.format(f.getInicio())+" - "
                    +df.format( f.getFim() )+" - "
                    +f.getDuracao()
                    +"\t\tStatus: "+f.getStatus()
                    +"\n\t\tCategorias: ");
            for(Categoria cat : f.getListCategoria()){
                System.out.print("\t\t"+cat.getNome());
            }
            System.out.println( "\n\t\tPrioridade:  "+f.getPrioridade() );
            for(Cenario c : f.getCenarios()){
                System.out.print("\nCenario: "+c.getNome()+" "
                        +df.format(c.getInicio())+" - "
                        +df.format( c.getFim() )+" - "
                        +c.getDuracao()
                        +"\t\tStatus: "+c.getStatus()
                        +"\n\t\tCategorias Cenario: ");
                for(Categoria cat : c.getListCategoria()){
                    System.out.print("\t\t"+cat.getNome());
                }
                System.out.println( "\n\t\tPrioridade:  "+c.getPrioridade() );
                System.out.println("\nSteps: ");
                for(Step s : c.getListStep()){
                    System.out.println("\t\t"+s.getNome()+" timestamp: "+ds.format( s.getInicio()));
                }
            }
        }
    }

}