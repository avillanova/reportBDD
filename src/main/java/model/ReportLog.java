package model;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Villanova
 */
public class ReportLog {


    private static List<Funcionalidade> listFuncionalidade = new ArrayList<Funcionalidade>();

    public static List<Funcionalidade> getListFuncionalidade() {
        return listFuncionalidade;
    }

    protected static void addFunc(Funcionalidade funcionalidade) {
        listFuncionalidade.add( funcionalidade );
    }

    protected Integer getTotalFunc(){
        return listFuncionalidade.size();
    }

    protected Integer getPassedsFeatures(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            if(f.getStatus().equals( LogStatus.PASS ))
                num=num+1;
        }
        return num;
    }
    protected Integer getFailedFeatures(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            if(f.getStatus().equals( LogStatus.FAIL ))
                num=num+1;
        }
        return num;
    }
    protected Integer getSkipedFeatures(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            if(f.getStatus().equals( LogStatus.SKIP ))
                num=num+1;
        }
        return num;
    }
    protected Integer getUnknowFeatures(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            if(f.getStatus().equals( LogStatus.UNKNOW ))
                num=num+1;
        }
        return num;
    }


    protected Integer getTotalCen(){
        Integer tCenarios=0;
        for(Funcionalidade f : listFuncionalidade){
            tCenarios = tCenarios+f.getCenarios().size();
        }
        return tCenarios;
    }

    protected Integer getPassedsCenarios(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            for(Cenario c : f.getCenarios()){
                if(c.getStatus().equals( LogStatus.PASS ))
                    num=num+1;
            }
        }
        return num;
    }
    protected Integer getFailedCenarios(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            for(Cenario c : f.getCenarios()){
                if(c.getStatus().equals( LogStatus.FAIL ))
                    num=num+1;
            }
        }
        return num;
    }
    protected Integer getSkipedCenarios(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            for(Cenario c : f.getCenarios()){
                if(c.getStatus().equals( LogStatus.SKIP ))
                    num=num+1;
            }
        }
        return num;
    }
    protected Integer getUnknowCenarios(){
        Integer num=0;
        for(Funcionalidade f : listFuncionalidade){
            for(Cenario c : f.getCenarios()){
                if(c.getStatus().equals( LogStatus.UNKNOW ))
                    num=num+1;
            }
        }
        return num;
    }

    protected List<Categoria> getCategoryList(){
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
