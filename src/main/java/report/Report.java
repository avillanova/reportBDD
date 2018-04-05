package report;

import com.sun.org.apache.xalan.internal.utils.FeatureManager;
import model.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Report extends ReportLog{
    private String reportName;
    private String userName;
    private String OS;
    private String browser;
    private String hostName;
    private Date dateCreation;
    private Date dateFinish;
    private String version;
    File htmlTemplateFile;
    String htmlString;
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");


    public Report(String src) throws IOException {
        String sprint = "Sprint 15";
        reportName = "REPORT ALEX"     ;
        userName   = System.getProperty( "user.name" )     ;
        OS         = System.getProperty( "os.name" )     ;
        browser    = "Chrome"    ;
        dateCreation       = new Date( );
        version    = "1.0-SNAPSHOT" ;
        try {
            hostName   = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        htmlTemplateFile = new File("./src/main/java/report/template.html");
        htmlString = FileUtils.readFileToString(htmlTemplateFile);
        htmlString = htmlString.replace("$reportName", reportName);
        htmlString = htmlString.replace("$reportDescription", sprint);
        htmlString = htmlString.replace("$reportDate", df.format(dateCreation));
        htmlString = htmlString.replace("$version", version);
        htmlString = htmlString.replace("$userName", userName);
        htmlString = htmlString.replace("$OS", OS);
        htmlString = htmlString.replace("$browser", browser);
        htmlString = htmlString.replace("$hostName", hostName);




    }

    public static void addFuncionalidade(Funcionalidade funcionalidade) {
       addFunc( funcionalidade );
    }

    public String getVersion() {
        return version;
    }

    public void flush() throws IOException {

      //  htmlString = htmlString.replace( "$featureNumber", getTotalFunc().toString() );
      //  htmlString = htmlString.replace( "$cenarioNumber", getTotalCen().toString() );
        dateFinish = new Date();
        String duracao = getDuracao( dateFinish.getTime() - dateCreation.getTime() );
        htmlString = htmlString.replace("$duracaoTotal", duracao);
        htmlString = htmlString.replace("$dateStart", df.format( dateCreation ));
        htmlString = htmlString.replace("$dateEnd", df.format( dateFinish ));
      //  htmlString = htmlString.replace( "$totalFeaturesPassed", getPassedsFeatures().toString() );
      // htmlString = htmlString.replace( "$totalFeaturesFailed", getFailedFeatures().toString() );
      //  Integer totalFeatOthers = getTotalFunc()-getPassedsFeatures()-getFailedFeatures();
      //  htmlString = htmlString.replace( "$totalFeaturesOthers", totalFeatOthers.toString() );

      //  htmlString = htmlString.replace( "$totalCenariosPassed",getPassedsCenarios().toString() );
      //  htmlString = htmlString.replace( "$totalCenariosFailed",getFailedCenarios().toString() );
      //  Integer totalCenOthers = getTotalCen()-getPassedsCenarios()-getFailedCenarios();
      //  htmlString = htmlString.replace( "$totalCenariosOthers",totalCenOthers.toString() );
      //  Integer totalPassPercent = 100*getPassedsCenarios()/getTotalCen();
      //  htmlString = htmlString.replace( "$passPercent", totalPassPercent.toString() );


        String categoryList = "";
        String categoryDrop = "";
        for( Categoria c : getCategoryList()){
            categoryList = categoryList+"<tr><td>"+c.getNome()+"</td></tr>";
            categoryDrop = categoryDrop+ "<li class=\""+c.getNome()+"\"><a href=\"#!\">"+c.getNome()+"</a></li>";
        }
        htmlString = htmlString.replace( "$categoriesList", categoryList );
        //$categories[i] /para dropdown fazer loop no li
        htmlString = htmlString.replace( "$categories[i]", categoryDrop );
        htmlString = htmlString.replace( "$testes", getBaseTest() );
        htmlString = htmlString.replace( "$AllCat", getHtmlAllCat() );


        File newHtmlFile = new File("./src/main/resources/new.html");
        FileUtils.writeStringToFile(newHtmlFile, htmlString);
    }

    private String getBaseTest(){
        String htmlBase="";
        for(Funcionalidade f : getListFuncionalidade() ){
            String catFeature="";
            String catTags = "";
            for(Categoria cat : f.getListCategoria()){
                catFeature = catFeature+" "+cat.getNome();
                catTags = catTags+"<span class=\"category text-white\">"+cat.getNome()+"</span>\n";
            }
            htmlBase=htmlBase+
            "<li class=\"collection-item test displayed "+f.getStatus()+" hasChildren category-assigned "+catFeature+"\">\n"
                +"<div class=\"test-head\">\n"
                    +"<span class=\"test-name\">"+f.getNome()+"</span>\n"
                    +"<span class=\"test-status label right outline capitalize "+f.getStatus()+"\">"+f.getStatus()+"</span>\n"
                +"</div>\n"
                +"<div class=\"test-body\">\n"
                    +"<div class=\"test-info\">\n"
                        +"<span title=\"Test started time\" alt=\"Test started time\" class=\"test-started-time label green lighten-1 text-white\">"+df.format( f.getInicio())+"</span>\n"+
                        "<span title=\"Test ended time\" alt=\"Test ended time\" class=\"test-ended-time label red lighten-1 text-white\">"+df.format(f.getFim())+"</span>\n" +
                        "<span title=\"Time taken to finish\" alt=\"Time taken to finish\" class=\"test-time-taken label blue-grey lighten-3 text-white\">"+f.getDuracao()+"</span>\n" +
                    "</div>\n"+
                    "<div class=\"test-desc\"><div class=\"test-node-desc\">"+f.getDescricao()+"</div></div>\n" +
                    "<div class=\"test-attributes\">\n" +
                        "<div class=\"categories\">\n" +
                            catTags+
                        "</div>\n" +
                    "</div>\n"+
                    "<ul class=\"collapsible node-list\" data-collapsible=\"accordion\">\n" +
                        getHtmlCenarios(f)+
                    "</ul>\n"+
                "</div>\n"+
            "</li>";

        }
        return htmlBase;
    }

    private String getHtmlCenarios(Funcionalidade f){
        String htmlCenarios="";
        for( Cenario c : f.getCenarios()){
            String listCatCenario="";
            for(Categoria cat : c.getListCategoria()){
                listCatCenario=listCatCenario+" "+cat.getNome();
            }
            htmlCenarios=htmlCenarios+
                 "<li class=\"displayed "+c.getStatus()+" node-1x category-assigned "+listCatCenario+"\">\n" +
                     "<div class=\"collapsible-header test-node "+c.getStatus()+" category-assigned  "+listCatCenario+"\">\n" +
                         "<div class=\"right test-info\">\n" +
                             "<span title=\"Test started time\" alt=\"Test started time\" class=\"test-started-time label green lighten-2 text-white\">"+df.format( c.getInicio() )+"</span>\n" +
                             "<span title=\"Test ended time\" alt=\"Test ended time\" class=\"test-ended-time label red lighten-2 text-white\">"+df.format( c.getFim() )+"</span>\n" +
                             "<span title=\"Time taken to finish\" alt=\"Time taken to finish\" class=\"test-time-taken label blue-grey lighten-2 text-white\">"+c.getDuracao()+"</span>\n" +
                             "<span class=\"test-status label outline capitalize "+c.getStatus()+"\">"+c.getStatus()+"</span>\n" +
                         "</div>\n" +
                         "<div class=\"test-node-name\">"+c.getNome()+"</div>\n" +
                         "<div class=\"test-node-desc\">"+c.getDescricao()+"</div>\n" +
                     "</div>\n" +
                     "<div class=\"collapsible-body\" style=\"display: none;\">\n" +
                         "<div class=\"test-steps\">\n" +
                             "<table class=\"bordered table-results\">\n" +
                                 "<thead>\n" +
                                     "<tr>\n" +
                                         "<th>Status</th>\n" +
                                         "<th>Timestamp</th>\n" +
                                         "<th>Details</th>\n" +
                                     "</tr>\n" +
                                 "</thead>\n" +
                                "<tbody>\n" +
                                    getHtmlSteps( c )+
                                "</tbody>\n"+
                            "</table>\n" +
                         "</div>\n" +
                     "</div>\n"+
                 "</li>\n";
        }
        return htmlCenarios;
    }

    private String getHtmlSteps(Cenario c){
        String htmlSteps="";
        DateFormat dfs = new SimpleDateFormat("HH:mm:ss");
        for( Step s : c.getListStep()){
            htmlSteps = htmlSteps+
               "<tr>\n" +
                   "<td class=\"status "+s.getStatus()+"\" title=\""+s.getStatus()+"\" alt=\""+s.getStatus()+"\">" +
                       "<i class=\"mdi-action-check-circle\"></i>" +
                    "</td>\n" +
                   "<td class=\"timestamp\">"+dfs.format( s.getInicio() )+"</td>\n" +
                   "<td class=\"step-details\">"+s.getNome()+"</td>\n" +
               "</tr>\n";
        }
        return htmlSteps;
    }


    private String getHtmlAllCat(){
        String htmlCat ="";
        for(Funcionalidade f : getListFuncionalidade()) {
            for ( Categoria cat : f.getListCategoria() ) {

                htmlCat = htmlCat+
                       " <li class=\"category-item displayed active\">\n" +
                       "                                <div class=\"cat-head\">\n" +
                       "                                    <span class=\"category-name\">"+cat.getNome()+"</span>\n" +
                       "                                </div>\n" +
                       "                                <!-- se tiver fail, pass ou other-->\n" +
                       "                                <div class=\"category-status-counts\">\n" +
                       "                                    <span class=\"fail label dot\">Fail: $failCategoryCount</span>\n" +
                       "                                </div>\n" +
                       "                                <div class=\"cat-body\">\n" +
                       "                                    <div class=\"category-status-counts\">\n" +
                       "                                        <div class=\"button-group\">\n" +
                       "                                            <a href=\"#!\" class=\"pass label filter\">Pass <span class=\"icon\">$totalPass</span></a>\n" +
                       "                                            <a href=\"#!\" class=\"fail label filter\">Fail <span class=\"icon\">$totalFail</span></a>\n" +
                       "                                            <a href=\"#!\" class=\"other label filter\">Others <span class=\"icon\">$totalOther</span></a>\n" +
                       "                                        </div>\n" +
                       "                                    </div>\n" +
                       "                                    <div class=\"cat-tests\">\n" +
                       "                                        <table class=\"bordered\">\n" +
                       "                                            <thead>\n" +
                       "                                            <tr>\n" +
                       "                                                <th>RunDate</th>\n" +
                       "                                                <th>Test Name</th>\n" +
                       "                                                <th>Status</th>\n" +
                       "                                            </tr>\n" +
                       "                                            </thead>\n" +
                       "                                            <tbody>\n" +
                       "                                            <tr class=\"fail\">\n" +
                       "                                                <td>$featureStart</td>\n" +
                       "                                                <td><span class=\"category-link linked\">$featureName</span></td>\n" +
                       "                                                <td><div class=\"status label capitalize fail\">$featureStatus</div></td>\n" +
                       "                                            </tr>\n" +
                       "                                            </tbody><tbody>\n" +
                       "                                        </tbody></table>\n" +
                       "                                    </div>\n" +
                       "                                </div>\n" +
                       "                            </li>";
            }
            for(Cenario c : f.getCenarios()){
                for(Categoria cat : c.getListCategoria()){
                    htmlCat = htmlCat + "<span class=\"category-name\">" + cat.getNome() + "</span>\n";
                }
            }
        }
        return htmlCat;
    }


    private String getDuracao(long duracao) {
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


}
