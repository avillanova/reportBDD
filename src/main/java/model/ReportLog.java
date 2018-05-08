package model;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Villanova
 */
public class ReportLog {


    private static List<Funcionalidade> listFuncionalidade = new ArrayList<Funcionalidade>();

    public static List<Funcionalidade> getListFuncionalidade() {
        for(Funcionalidade f : listFuncionalidade){
            for(Cenario c : f.getCenarios()){
                for(Step s : c.getListStep()){
                    if(s.getStatus().equals( LogStatus.FAIL )){
                        c.setStatus( LogStatus.FAIL );
                    }
                }
                if(c.getStatus() != LogStatus.FAIL){
                    c.setStatus( LogStatus.PASS );
                }
                if(c.getStatus() == LogStatus.FAIL ){
                    f.setStatus( LogStatus.FAIL );
                }
                if(f.getStatus() != LogStatus.FAIL){
                    f.setStatus( LogStatus.PASS );
                }
            }
        }

        return listFuncionalidade;

    }

    protected static void addFunc(Funcionalidade funcionalidade) {
        listFuncionalidade.add( funcionalidade );
    }

    protected static List<Categoria> getCategoryList(){
        List<Categoria> categorias = new ArrayList<Categoria>();
        for(Funcionalidade f : listFuncionalidade){
           for(Categoria c : f.getListCategoria()){
               if(!categorias.contains(c)){
                   categorias.add( c );
               }
           }
           for(Cenario c : f.getCenarios()){
               for(Categoria cat : c.getListCategoria()){
                   if(!categorias.contains(cat)){
                       categorias.add( cat );
                   }
               }
           }
        }
        return categorias;
    }
}
