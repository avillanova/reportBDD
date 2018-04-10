package report;

import model.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static Date dateCreation;
    private static Date dateFinish;
    private String version;

    static String htmlString;
    static DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    static DateFormat dfs = new SimpleDateFormat("HH:mm:ss");
    static String src2 = "";

    public Report(String src) throws IOException {

        src2 = src;

        Properties props = new Properties();
        FileInputStream file = new FileInputStream("src/main/resources/config.properties");
        props.load(new InputStreamReader( file, "UTF8"));

        String sprint = "Sprint "+props.getProperty( "sprint" );
        reportName = props.getProperty( "sistema" );
        userName   = System.getProperty( "user.name" );
        OS         = System.getProperty( "os.name" );
        browser    = props.getProperty( "Browser" );
        dateCreation = new Date( );
        version    = "1.0-SNAPSHOT" ;
        try {
            hostName   = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        htmlString = htmlTemplate();
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

    public static void flush() throws IOException {
        System.out.println( "iniciando o Flush" );
        dateFinish = new Date();
        String duracao = getDuracao( dateFinish.getTime() - dateCreation.getTime() );
        htmlString = htmlString.replace("$duracaoTotal", duracao);
        htmlString = htmlString.replace("$dateStart", df.format( dateCreation ));
        htmlString = htmlString.replace("$dateEnd", df.format( dateFinish ));


        String categoryList = "";
        String categoryDrop = "";


        for( Categoria c : getCategoryList()){
            categoryList = categoryList+"<tr><td>"+c.getNome()+"</td></tr>";
            categoryDrop = categoryDrop+ "<li class=\""+c.getNome()+"\"><a href=\"#!\">"+c.getNome()+"</a></li>";
        }

        htmlString = htmlString.replace( "$categoriesList", categoryList );
        htmlString = htmlString.replace( "$categories[i]", categoryDrop );
        htmlString = htmlString.replace( "$testes", getBaseTest() );
        htmlString = htmlString.replace( "$AllCat", getHtmlAllCat() );

        System.out.println( "Gravando em "+ src2 );
        File newHtmlFile = new File(src2);
        FileUtils.writeStringToFile(newHtmlFile, htmlString);

        System.out.println( "Gravado" );
    }

    private static String getBaseTest(){
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

    private static String getHtmlCenarios(Funcionalidade f){
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

    private static String getHtmlSteps(Cenario c){
        String htmlSteps="";

        for( Step s : c.getListStep()){
            if(s.getImg()==null) {
                htmlSteps = htmlSteps +
                 "<tr>\n" +
                   "<td class=\"status " + s.getStatus() + "\" title=\"" + s.getStatus() + "\" alt=\"" + s.getStatus() + "\">" +
                      "<i class=\"mdi-action-check-circle\"></i>" +
                    "</td>\n" +
                   "<td class=\"timestamp\">" + dfs.format( s.getInicio() ) + "</td>\n" +
                   "<td class=\"step-details\">" + s.getNome() + "</td>\n" +
                "</tr>\n";
            }else {
                htmlSteps = htmlSteps +
                        "<tr>\n" +
                            "<td class=\"status " + s.getStatus() + "\" title=\"" + s.getStatus() + "\" alt=\"" + s.getStatus() + "\">" +
                                "<i class=\"mdi-action-check-circle\"></i>" +
                            "</td>\n" +
                            "<td class=\"timestamp\">" + dfs.format( s.getInicio() ) + "</td>\n" +
                            "<td class=\"step-details\">"+
                                "<a href=\"data:image/png;base64, "+s.getImg()+"\" data-featherlight=\"image\">"+
                                    "<img class=\"report-img\" src=\"data:image/png;base64, "+s.getImg()+"\">"+
                                "</a>"+
                            "</td>\n" +
                        "</tr>\n";
            }
        }
        return htmlSteps;
    }

    private static String getHtmlAllCat(){
        String htmlCat ="";
        List<Categoria> listCategorias= new ArrayList<Categoria>();
        for(Funcionalidade f : getListFuncionalidade()) {
            for ( Categoria cat : f.getListCategoria() ) {
                if(!listCategorias.contains(cat)){
                    htmlCat = htmlCat+
                            "<li class=\"category-item displayed active\">\n" +
                                "<div class=\"cat-head\">\n" +
                                    "<span class=\"category-name\">"+cat.getNome()+"</span>\n" +
                                "</div>\n" +
                                getCatCounts(cat)+
                            "</li>";
                    listCategorias.add(cat);
                }

            }
        }
        return htmlCat;
    }

    private static String getCatCounts(Categoria c){
        String basePin ="";
        String baseNums = "";
        Integer pass=0;
        Integer fail=0;
        Integer other=0;

        String pinList ="";
        for(Funcionalidade f : getListFuncionalidade()){
            for(Categoria cat : f.getListCategoria()){
                if(cat.equals( c )){
                    if(f.getStatus().equals( LogStatus.PASS )){
                        pass=pass+1;
                        if(!pinList.contains( "pass" ))
                            pinList = pinList+"<span class=\"pass label dot\"></span>\n";
                    }
                    if(f.getStatus().equals( LogStatus.FAIL )){
                        fail=fail+1;
                        if(!pinList.contains( "fail" ))
                            pinList = pinList+"<span class=\"fail label dot\"></span>\n";
                    }
                    if(!f.getStatus().equals( LogStatus.PASS ) && !f.getStatus().equals( LogStatus.FAIL )){
                        other=other+1;
                        if(!pinList.contains( "other" ))
                            pinList = pinList+"<span class=\"other label dot\"></span>\n";
                    }
                }
            }
        }
        basePin = "<div class=\"category-status-counts\">\n" +pinList+"\n</div>\n";
        baseNums = "<div class=\"cat-body\">\n" +
                        "<div class=\"category-status-counts\">\n" +
                            "<div class=\"button-group\">\n" +
                                "<a href=\"#!\" class=\"pass label filter\">Pass <span class=\"icon\">"+pass+"</span></a>\n" +
                                "<a href=\"#!\" class=\"fail label filter\">Fail <span class=\"icon\">"+fail+"</span></a>\n" +
                                "<a href=\"#!\" class=\"other label filter\">Others <span class=\"icon\">"+other+"</span></a>\n"+
                            "</div>\n" +
                        "</div>\n"+
                        "<div class=\"cat-tests\">\n" +
                            "<table class=\"bordered\">\n" +
                                "<thead>\n" +
                                    "<tr>\n" +
                                        "<th>RunDate</th>\n" +
                                        "<th>Test Name</th>\n" +
                                        "<th>Status</th>\n" +
                                    "</tr>\n" +
                                "</thead>\n" +
                                "<tbody>\n" +
                                    getFuncPerCat( c )+
                                "</tbody>" +
                                "<tbody>\n" +
                                "</tbody>" +
                            "</table>\n" +
                        "</div>\n" +
                    "</div>\n";

        return basePin+baseNums;
    }

    private static String getFuncPerCat(Categoria c) {
        String htmlFuncCat = "";
            for(Funcionalidade f : getListFuncionalidade()){
                for(Categoria cat : f.getListCategoria()){
                    if(cat.equals( c )){
                        htmlFuncCat = htmlFuncCat+
                                "<tr class=\""+f.getStatus()+"\">\n" +
                                    "<td>"+dfs.format( f.getInicio())+"</td>\n" +
                                    "<td>" +
                                        "<span class=\"category-link linked\">"+f.getNome()+"</span>" +
                                    "</td>\n" +
                                    "<td>" +
                                        "<div class=\"status label capitalize "+f.getStatus()+"\">"+f.getStatus()+"</div>" +
                                    "</td>\n" +
                                "</tr>\n" ;
                    }
                }
            }



        return htmlFuncCat;
    }

    private static String getDuracao(long duracao) {
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



    private static String htmlTemplate(){
        return "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"robots\" content=\"noodp, noydir\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "    <title>\n" +
                "        $reportName\n" +
                "    </title>\n" +
                "    <link href=\"https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600\" rel=\"stylesheet\" type=\"text/css\">\n" +
                "    <link href=\"https://cdn.rawgit.com/anshooarora/extentreports/6032d73243ba4fe4fb8769eb9c315d4fdf16fe68/cdn/extent.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body class=\"extent standard\">\n" +
                "<!-- nav -->\n" +
                "<nav>\n" +
                "    <div class=\"logo-container blue darken-2\">\n" +
                "        <a class=\"logo-content\" href=\"#\">\n" +
                "            <span>$reportName</span>\n" +
                "        </a>\n" +
                "        <a href=\"#\" data-activates=\"slide-out\" class=\"button-collapse hide-on-large-only\">\n" +
                "            <i class=\"mdi-navigation-apps\">        </i></a>\n" +
                "    </div>\n" +
                "    <ul id=\"slide-out\" class=\"side-nav fixed hide-on-med-and-down\">\n" +
                "        <li class=\"analysis waves-effect active\">\n" +
                "            <a href=\"#!\" class=\"test-view\" onclick=\"_updateCurrentStage(0)\">\n" +
                "                <i class=\"mdi-action-dashboard\"></i>\n" +
                "                \"Detalhes dos testes\"\n" +
                "            </a>\n" +
                "        </li>\n" +
                "        <li class=\"analysis waves-effect\">\n" +
                "            <a href=\"#!\" class=\"categories-view\" onclick=\"_updateCurrentStage(1)\">\n" +
                "                <i class=\"mdi-maps-local-offer\"></i>\n" +
                "                \"Categorias\"\n" +
                "            </a>\n" +
                "        </li>\n" +
                "        <li class=\"analysis waves-effect\">\n" +
                "            <a href=\"#!\" onclick=\"_updateCurrentStage(-1)\" class=\"dashboard-view\">\n" +
                "                <i class=\"mdi-action-track-changes\"></i>\n" +
                "                \"Análise\"\n" +
                "            </a>\n" +
                "        </li>\n" +
                "    </ul>\n" +
                "    <span class=\"report-name\"></span><span class=\"report-headline\">$reportDescription</span>\n" +
                "    <ul class=\"right hide-on-med-and-down nav-right\">\n" +
                "        <li class=\"theme-selector\" alt=\"Click to toggle dark theme. To enable by default, use js configuration $(&quot;body&quot;).addClass(&quot;dark&quot;);\" title=\"Click to toggle dark theme. To enable by default, use js configuration $(&quot;body&quot;).addClass(&quot;dark&quot;);\">\n" +
                "            <i class=\"mdi-hardware-desktop-windows\"></i>\n" +
                "        </li>\n" +
                "        <li>\n" +
                "            <span class=\"suite-started-time\">$reportDate</span>\n" +
                "        </li>\n" +
                "        <li>\n" +
                "            <span>$version</span>\n" +
                "        </li>\n" +
                "    </ul>\n" +
                "</nav>\n" +
                "<!-- /nav -->\n" +
                "\n" +
                "<!-- container -->\n" +
                "<div class=\"container\">\n" +
                "\n" +
                "    <!-- dashboard -->\n" +
                "    <div id=\"dashboard-view\" class=\"row hide\">\n" +
                "        <div class=\"time-totals\">\n" +
                "            <div class=\"col l2 m4 s6\">\n" +
                "                <div class=\"card suite-total-tests\">\n" +
                "                    <span class=\"panel-name\">Total Features</span>\n" +
                "                    <span class=\"total-tests\"> <span class=\"panel-lead\">$featureNumber</span> </span>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col l2 m4 s6\">\n" +
                "                <div class=\"card suite-total-steps\">\n" +
                "                    <span class=\"panel-name\">Total Scenarios</span>\n" +
                "                    <span class=\"total-steps\"> <span class=\"panel-lead\">$cenarioNumber</span> </span>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col s12 m6 l4 fh\">\n" +
                "                <div class=\"card suite-total-time-current\">\n" +
                "                    <span class=\"panel-name\">Total Time Taken</span>\n" +
                "                    <span class=\"suite-total-time-current-value panel-lead\">$duracaoTotal</span>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col l2 m4 s6 suite-start-time\">\n" +
                "                <div class=\"card accent green-accent\">\n" +
                "                    <span class=\"panel-name\">Start</span>\n" +
                "                    <span class=\"panel-lead suite-started-time\">$dateStart</span>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col l2 m4 s6 suite-end-time\">\n" +
                "                <div class=\"card accent pink-accent\">\n" +
                "                    <span class=\"panel-name\">End</span>\n" +
                "                    <span class=\"panel-lead suite-ended-time\">$dateEnd</span>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"charts\">\n" +
                "            <div class=\"col s12 m6 l4 fh\">\n" +
                "                <div class=\"card-panel\">\n" +
                "                    <div>\n" +
                "                        <span class=\"panel-name\">Features View</span>\n" +
                "                    </div>\n" +
                "                    <div class=\"panel-setting modal-trigger test-count-setting right\">\n" +
                "                        <a href=\"#test-count-setting\"><i class=\"mdi-navigation-more-vert text-md\"></i></a>\n" +
                "                    </div>\n" +
                "                    <div class=\"chart-box\">\n" +
                "                        <canvas class=\"text-centered\" id=\"test-analysis\" width=\"160\" height=\"130\" style=\"width: 160px; height: 130px;\"></canvas>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"weight-light\">\n" +
                "                            <span class=\"t-pass-count weight-normal\">\n" +
                "                            </span> feature(s) passed</span>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"weight-light\">\n" +
                "                        <span class=\"t-fail-count weight-normal\"></span> feature(s) failed,\n" +
                "                        <span class=\"t-others-count weight-normal\"></span> others</span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col s12 m6 l4 fh\">\n" +
                "                <div class=\"card-panel\">\n" +
                "                    <div>\n" +
                "                        <span class=\"panel-name\">Scenarios View</span>\n" +
                "                    </div>\n" +
                "                    <div class=\"panel-setting modal-trigger step-status-filter right\">\n" +
                "                        <a href=\"#step-status-filter\"><i class=\"mdi-navigation-more-vert text-md\"></i></a>\n" +
                "                    </div>\n" +
                "                    <div class=\"chart-box\">\n" +
                "                        <canvas class=\"text-centered\" id=\"step-analysis\" width=\"160\" height=\"130\" style=\"width: 160px; height: 130px;\"></canvas>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"weight-light\"><span class=\"s-pass-count weight-normal\"></span> scenario(s) passed </span>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <span class=\"weight-light\"><span class=\"s-fail-count weight-normal\"></span> scenario(s) failed,\n" +
                "                        <span class=\"s-others-count weight-normal\"></span> others</span>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "            <div class=\"col s12 m12 l4 fh\">\n" +
                "                <div class=\"card-panel\">\n" +
                "                    <span class=\"panel-name\">Pass Percentage</span>\n" +
                "                    <span class=\"pass-percentage panel-lead\"></span>\n" +
                "                    <div class=\"progress light-blue lighten-3\">\n" +
                "                        <div class=\"determinate light-blue\" style=\"width:\"></div>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"system-view hide\">\n" +
                "            <div class=\"col l4 m12 s12\">\n" +
                "                <div class=\"card-panel\">\n" +
                "                    <span class=\"label info outline right\">Environment</span>\n" +
                "                    <table>\n" +
                "                        <thead>\n" +
                "                        <tr>\n" +
                "                            <th>Param</th>\n" +
                "                            <th>Value</th>\n" +
                "                        </tr>\n" +
                "                        </thead>\n" +
                "                        <tbody>\n" +
                "                        <tr>\n" +
                "                            <td>User Name</td>\n" +
                "                            <td>$userName</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>OS</td>\n" +
                "                            <td>$OS</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>Browser</td>\n" +
                "                            <td>$browser</td>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                            <td>Host Name</td>\n" +
                "                            <td>$hostName</td>\n" +
                "                        </tr>\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class=\"category-summary-view hide\">\n" +
                "            <div class=\"col l4 m6 s12\">\n" +
                "                <div class=\"card-panel\">\n" +
                "                    <span class=\"label info outline right\">Categorias</span>\n" +
                "                    <table>\n" +
                "                        <thead>\n" +
                "                        <tr>\n" +
                "                            <th>Name</th>\n" +
                "                        </tr>\n" +
                "                        </thead>\n" +
                "                        <tbody>\n" +
                "                            $categoriesList\n" +
                "                        </tbody>\n" +
                "                    </table>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <!-- /dashboard -->\n" +
                "\n" +
                "    <!-- features -->\n" +
                "    <div id=\"test-view\" class=\"row _addedTable\">\n" +
                "        <div class=\"col _addedCell1\" style=\"resize: horizontal; height: 612px;\">\n" +
                "            <div class=\"contents\" style=\"height: 597px;\">\n" +
                "                <div class=\"card-panel heading\">\n" +
                "                    <h5>Testes</h5>\n" +
                "                </div>\n" +
                "                <div class=\"card-panel filters\">\n" +
                "                    <div>\n" +
                "                        <a class=\"dropdown-button btn-floating btn-small waves-effect waves-light grey tests-toggle\" data-activates=\"tests-toggle\" data-constrainwidth=\"true\" data-beloworigin=\"true\" data-hover=\"true\" href=\"#\">\n" +
                "                            <i class=\"mdi-action-reorder\"></i>\n" +
                "                        </a><ul id=\"tests-toggle\" class=\"dropdown-content\">\n" +
                "                        <li class=\"pass\"><a href=\"#!\">Pass</a></li>\n" +
                "                        <li class=\"fail\"><a href=\"#!\">Fail</a></li>\n" +
                "                        <li class=\"skip\"><a href=\"#!\">Skip</a></li>\n" +
                "                        <li class=\"divider\"></li>\n" +
                "                        <li class=\"clear\"><a href=\"#!\">Clear Filters</a></li>\n" +
                "                    </ul>\n" +
                "\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <a class=\"dropdown-button btn-floating btn-small waves-effect waves-light grey category-toggle\" data-activates=\"category-toggle\" data-constrainwidth=\"false\" data-beloworigin=\"true\" data-hover=\"true\" href=\"#\">\n" +
                "                            <i class=\"mdi-maps-local-offer\"></i>\n" +
                "                        </a>\n" +
                "                        <ul id=\"category-toggle\" class=\"dropdown-content\">\n" +
                "                            $categories[i]\n" +
                "                            <li class=\"divider\"></li>\n" +
                "                            <li class=\"clear\"><a href=\"#!\">Clear Filters</a></li>\n" +
                "                        </ul>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                       <a class=\"dropdown-button btn-floating btn-small waves-effect waves-light grey priority-toggle\" data-activates=\"priority-toggle\" data-constrainwidth=\"true\" data-beloworigin=\"true\" data-hover=\"true\" href=\"#\">\n" +
                "                          <i class=\"mdi-action-reorder\"></i>\n" +
                "                       </a>\n" +
                "                       <ul id=\"priority-toggle\" class=\"dropdown-content\">\n" +
                "                            <li class=\"highest\"><a href=\"#!\">Highest</a></li>\n" +
                "                            <li class=\"high\"><a href=\"#!\">High</a></li>\n" +
                "                            <li class=\"medium\"><a href=\"#!\">Medium</a></li>\n" +
                "                            <li class=\"low\"><a href=\"#!\">Low</a></li>\n" +
                "                            <li class=\"lowest\"><a href=\"#!\">Lowest</a></li>\n" +
                "                            <li class=\"divider\"></li>\n" +
                "                            <li class=\"clear\"><a href=\"#!\">Clear Filters</a></li>\n" +
                "                        </ul>\n" +
                "                     </div>\n" +
                "\n" +
                "                    <div>\n" +
                "                        <a class=\"btn-floating btn-small waves-effect waves-light grey\" id=\"enableDashboard\" alt=\"Enable Dashboard\" title=\"Enable Dashboard\">\n" +
                "                            <i class=\"mdi-action-track-changes\"></i>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <a class=\"btn-floating btn-small waves-effect waves-light blue\" id=\"refreshCharts\" alt=\"Refresh Charts on Filters\" title=\"Refresh Charts on Filters\">\n" +
                "                            <i class=\"mdi-navigation-refresh active\"></i>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div class=\"search\" alt=\"Search Tests\" title=\"Search Tests\">\n" +
                "                        <div class=\"input-field left\">\n" +
                "                            <input id=\"searchTests\" type=\"text\" class=\"validate\" placeholder=\"Search Tests...\">\n" +
                "                        </div>\n" +
                "                        <a href=\"#\" class=\"btn-floating btn-small waves-effect waves-light grey\">\n" +
                "                            <i class=\"mdi-action-search\"></i>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                    <div>\n" +
                "                        <a class=\"btn-floating btn-small waves-effect waves-light grey\" id=\"clear-filters\" alt=\"Clear Filters\" title=\"Clear Filters\">\n" +
                "                            <i class=\"mdi-navigation-close\"></i>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "\n" +
                "            <!-- Features List -->\n" +
                "                <div class=\"card-panel no-padding-h no-padding-v no-margin-v\">\n" +
                "                    <div class=\"wrapper\">\n" +
                "                        <ul id=\"test-collection\" class=\"test-collection\">\n" +
                "                            $testes\n" +
                "                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div id=\"test-details-wrapper\" class=\"col _addedCell2\" style=\"height: 612px; width: 1259px;\">\n" +
                "            <div class=\"contents\" style=\"height: 597px;\">\n" +
                "                <div class=\"card-panel details-view\">\n" +
                "                    <h5 class=\"details-name\">Detalhes</h5>\n" +
                "                    <div class=\"step-filters right\">\n" +
                "                        <span class=\"btn-floating btn-small waves-effect waves-light blue\" status=\"info\" alt=\"info\" title=\"info\"><i class=\"mdi-action-info-outline\"></i></span>\n" +
                "                        <span class=\"btn-floating btn-small waves-effect waves-light green\" status=\"pass\" alt=\"pass\" title=\"pass\"><i class=\"mdi-action-check-circle\"></i></span>\n" +
                "                        <span class=\"btn-floating btn-small waves-effect waves-light red\" status=\"fail\" alt=\"fail\" title=\"fail\"><i class=\"mdi-navigation-cancel\"></i></span>\n" +
                "                        <span class=\"btn-floating btn-small waves-effect waves-light cyan\" status=\"skip\" alt=\"skip\" title=\"skip\"><i class=\"mdi-content-redo\"></i></span>\n" +
                "                        <span class=\"btn-floating btn-small waves-effect waves-light grey darken-2\" status=\"clear-step-filter\" alt=\"Clear filters\" title=\"Clear filters\"><i class=\"mdi-content-clear\"></i></span>\n" +
                "                    </div>\n" +
                "                    <div class=\"details-container\">\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <!-- /tests -->\n" +
                "\n" +
                "    <!-- categories -->\n" +
                "    <div id=\"categories-view\" class=\"row _addedTable hide\">\n" +
                "        <div class=\"col _addedCell1\" style=\"resize: horizontal;\">\n" +
                "            <div class=\"contents\">\n" +
                "                <div class=\"card-panel heading\">\n" +
                "                    <h5>Categorias</h5>\n" +
                "                </div>\n" +
                "                <div class=\"card-panel filters\">\n" +
                "                    <div class=\"search\" alt=\"Search tests\" title=\"Search tests\">\n" +
                "                        <div class=\"input-field left\">\n" +
                "                            <input id=\"searchTests\" type=\"text\" class=\"validate\" placeholder=\"Search...\">\n" +
                "                        </div>\n" +
                "                        <a href=\"#\" class=\"btn-floating btn-small waves-effect waves-light blue lighten-1\">\n" +
                "                            <i class=\"mdi-action-search\"></i>\n" +
                "                        </a>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "                <div class=\"card-panel no-padding-h no-padding-v\">\n" +
                "                    <div class=\"wrapper\">\n" +
                "                        <ul id=\"cat-collection\" class=\"cat-collection\">\n" +
                "                            $AllCat\n" +
                "                        </ul>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div id=\"cat-details-wrapper\" class=\"col _addedCell2\">\n" +
                "            <div class=\"contents\">\n" +
                "                <div class=\"card-panel details-view\">\n" +
                "                    <h5 class=\"cat-name\"></h5>\n" +
                "                    <div class=\"cat-container\"><div class=\"cat-body\">\n" +
                "                        <div class=\"category-status-counts\">\n" +
                "                            <div class=\"button-group\">\n" +
                "                                <a href=\"#!\" class=\"pass label filter\">Pass <span class=\"icon\"></span></a>\n" +
                "                                <a href=\"#!\" class=\"fail label filter\">Fail <span class=\"icon\"></span></a>\n" +
                "                                <a href=\"#!\" class=\"other label filter\">Others <span class=\"icon\"></span></a>\n" +
                "                            </div>\n" +
                "                        </div>\n" +
                "                        <div class=\"cat-tests\">\n" +
                "                            <table class=\"bordered\">\n" +
                "                                <thead>\n" +
                "                                <tr>\n" +
                "                                    <th>RunDate</th>\n" +
                "                                    <th>Test Name</th>\n" +
                "                                    <th>Status</th>\n" +
                "                                </tr>\n" +
                "                                </thead>\n" +
                "                                <tbody>\n" +
                "                                </tbody>\n" +
                "                                <tbody>\n" +
                "                            </tbody></table>\n" +
                "                        </div>\n" +
                "                    </div></div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <!-- /categories -->\n" +
                "\n" +
                "</div>\n" +
                "<!-- /container -->\n" +
                "\n" +
                "<!-- test dashboard counts setting -->\n" +
                "<div id=\"test-count-setting\" class=\"modal bottom-sheet\">\n" +
                "    <div class=\"modal-content\">\n" +
                "        <h5>Configure Tests Count Setting</h5>\n" +
                "        <input name=\"test-count-setting\" type=\"radio\" id=\"parentWithoutNodes\" class=\"with-gap\">\n" +
                "        <label for=\"parentWithoutNodes\">Parent Tests Only (Does not include child nodes in counts)</label>\n" +
                "        <br>\n" +
                "        <input name=\"test-count-setting\" type=\"radio\" id=\"parentWithoutNodesAndNodes\" class=\"with-gap\">\n" +
                "        <label for=\"parentWithoutNodesAndNodes\">Parent Tests Without Child Tests + Child Tests</label>\n" +
                "        <br>\n" +
                "        <input name=\"test-count-setting\" type=\"radio\" id=\"childNodes\" class=\"with-gap\">\n" +
                "        <label for=\"childNodes\">Child Tests Only</label>\n" +
                "    </div>\n" +
                "    <div class=\"modal-footer\">\n" +
                "        <a href=\"#!\" class=\"modal-action modal-close waves-effect waves-green btn\">Save</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<!-- /test dashboard counts setting -->\n" +
                "\n" +
                "<!-- filter for step status -->\n" +
                "<div id=\"step-status-filter\" class=\"modal bottom-sheet\">\n" +
                "    <div class=\"modal-content\">\n" +
                "        <h5>Select status</h5>\n" +
                "        <input checked=\"\" class=\"filled-in\" type=\"checkbox\" id=\"step-dashboard-filter-pass\">\n" +
                "        <label for=\"step-dashboard-filter-pass\">Pass</label>\n" +
                "        <br>\n" +
                "        <input checked=\"\" class=\"filled-in\" type=\"checkbox\" id=\"step-dashboard-filter-fail\">\n" +
                "        <label for=\"step-dashboard-filter-fail\">Fail</label>\n" +
                "        <br>\n" +
                "        <input checked=\"\" class=\"filled-in\" type=\"checkbox\" id=\"step-dashboard-filter-skip\">\n" +
                "        <label for=\"step-dashboard-filter-skip\">Skipped</label>\n" +
                "        <br>\n" +
                "        <input checked=\"\" class=\"filled-in\" type=\"checkbox\" id=\"step-dashboard-filter-info\">\n" +
                "        <label for=\"step-dashboard-filter-info\">Info</label>\n" +
                "        <br>\n" +
                "        <input checked=\"\" class=\"filled-in\" type=\"checkbox\" id=\"step-dashboard-filter-unknown\">\n" +
                "        <label for=\"step-dashboard-filter-unknown\">Unknown</label>\n" +
                "    </div>\n" +
                "    <div class=\"modal-footer\">\n" +
                "        <a href=\"#!\" class=\"modal-action modal-close waves-effect waves-green btn\">Save</a>\n" +
                "    </div>\n" +
                "</div>\n" +
                "<!-- /filter for step status -->\n" +
                "\n" +
                "<script src=\"extent.js\" type=\"text/javascript\"></script>\n" +
                "<script>$(document).ready(function() {});</script>\n" +
                "\n" +
                "<div class=\"hiddendiv common\"></div></body>\n" +
                "\n" +
                "</html>";
    }

}
