package model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import report.Report;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.codec.binary.Base64;

public class FuncionalidadeTest {
    static Report report;

    @Before
    public void GerarRelatorio() throws IOException {
        report = new Report("./src/main/resources/new.html");
    }

    @Test
    public void Test() throws InterruptedException, IOException {

        //FUNCIONALIDADE 1
        Funcionalidade func = new Funcionalidade("Funcionalidade 1");
        func.setDescricao( "Testando o relatorio" );
        func.addCategorias("Test1", "Test2", "Test2", "Test4");
        func.setStatus( LogStatus.FAIL );
        func.setPrioridade(LogPriority.HIGHEST);
            Cenario cen = new Cenario("Cenario Teste");
            cen.setStatus( LogStatus.PASS );
            cen.addCategorias("Cenario1", "Test1");
            cen.setPrioridade(LogPriority.HIGHEST);
                Step step = new Step("Step 1");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 2");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 3");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 4");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);

            cen = new Cenario("Cenario Teste 2");
            cen.setStatus( LogStatus.FAIL );
            cen.setPrioridade(LogPriority.LOW);
            cen.addCategorias("Cenario Categoria", "Cenario 2");
                step = new Step("Outro Step 1");
                step.setStatus( LogStatus.PASS );
                //Thread.sleep( 1500 );
                cen.addStep(step);
                step = new Step("Outro Step 2");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Outro Step 3");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Outro Step 4");
                step.setStatus( LogStatus.FAIL );
                cen.addStep(step);
               // Thread.sleep( 60000 );
                cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);
        func.setFim( new Date(System.currentTimeMillis()) );

    }

    @Test
    public void Test2() throws InterruptedException {
        //FUNCIONALIDADE 2
        Funcionalidade func = new Funcionalidade("Funcionalidade 2");
        func.addCategorias("Test1", "Test2", "Test2", "Func2");
        func.setStatus( LogStatus.PASS );
        func.setPrioridade(LogPriority.HIGHEST);
            Cenario cen = new Cenario("Cenario Teste Func 2");
            cen.setStatus( LogStatus.PASS );
            cen.addCategorias("Cenario1");
            cen.setPrioridade(LogPriority.HIGHEST);
                Step step = new Step("Step 1");
                step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 2");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 3");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Step 4");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);
            cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);

            cen = new Cenario("Cenario Teste 2");
            cen.setStatus( LogStatus.PASS );
            cen.setPrioridade(LogPriority.LOW);
            cen.addCategorias("Cenario Categoria", "Cenario 2");
                step = new Step("Outro Step 1");
        step.setStatus( LogStatus.PASS );

                cen.addStep(step);
                step = new Step("Outro Step 2");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Outro Step 3");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);
                step = new Step("Outro Step 4");
        step.setStatus( LogStatus.PASS );
                cen.addStep(step);


                step = new Step( "Imagem" );
        step.setStatus( LogStatus.INFO );
                step.setImg( pngToBase64( "C:\\Users\\adm\\Downloads\\exemplo.png" ) );

        cen.addStep(step);
        cen.setFim( new Date(System.currentTimeMillis()) );
            func.addCenario(cen);
        func.setFim( new Date(System.currentTimeMillis()) );
    }


    @After
    public void after() throws IOException {
        report.flush();
    }



    public static String pngToBase64(String src){
        String cod64 = "";
        try {
            File file = new File(src);
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int)file.length()];
            fileInputStreamReader.read(bytes);
            cod64 = new String( Base64.encodeBase64(bytes), "UTF-8");
            // System.out.println( src );
            //System.out.println( "O CODIGO DA IMAGEM É: "+cod64 );
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return cod64;
        //return cod64;
    }

    public void Print(){
        //PRINTS
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
        DateFormat ds = new SimpleDateFormat("HH:mm:ss");
        DateFormat dc = new SimpleDateFormat("dd/MM/yyyy");

        System.out.println("-------------------LISTANDO TUDO-------------------");//Verificar aqui
        System.out.println("CONFIGURAÇÕES" );
   //     System.out.println("\tHost Name: "+report.getHostName() );
   //     System.out.println("\tUser Name: "+report.getUserName());
   //     System.out.println("\tOS: "+report.getOS());
   //     System.out.println("\tHost Date: "+dc.format( report.getDate()));
   //     System.out.println("\tBrowser: "+report.getBrowser() );
        System.out.println("\tVersion: "+report.getVersion() );

        for(Funcionalidade f : ReportLog.getListFuncionalidade()){
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