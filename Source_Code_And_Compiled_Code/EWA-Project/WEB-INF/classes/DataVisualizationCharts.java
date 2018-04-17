
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.util.*;

@WebServlet("/DataVisualizationCharts")

public class DataVisualizationCharts extends HttpServlet {
  HttpSession session;
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
                        throws ServletException, IOException {
    session = request.getSession();

    String typeOfChart = request.getParameter("typeOfChart");
    String chartHeading = request.getParameter("chartHeading");
    PrintWriter pw = response.getWriter();
    Utilities utility = new Utilities(pw,request,response);
    utility.printHtml("header.html");

    pw.print("<div class='panel panel-primary ' id='formcontainer' style='width: 70%;margin-left:200px; margin-top:180px;overflow: hidden;'>");
    pw.print("<div class='panel-heading'> "+chartHeading+" </div>"+
            "<div class='panel-body'>");
    // pw.print(typeOfChart);
    pw.print("  <div id='chart_div'> </div>"+
    "<script>"+
    "$.ajax({"+
      "url: 'DataVisualization',"+
      "type: 'POST',"+
      "data: {"+
        "'reqType': '"+typeOfChart+"'"+
      "},"+
      "dataType: 'json',"+
      "success: function (response) {"+
        // "console.log(response);"+
        typeOfChart+"(response);"+
      "},"+
      "error: function (errorThrown) {"+
        "alert('NO data available for Visualization');"+
      "}"+
    "});"+
    "</script>");
    pw.print("</div></div></div>");
    utility.printHtml("footer.html");
  }
}
