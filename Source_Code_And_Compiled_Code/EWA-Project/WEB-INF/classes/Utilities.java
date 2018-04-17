import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class Utilities {

    private PrintWriter printWriter;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private String catalinaHome = System.getProperty("catalina.home").replace("\\", "/");


    public Utilities(PrintWriter printWriter, HttpServletRequest request, HttpServletResponse response){
        this.printWriter = printWriter;
        this.request = request;
        this.response = response;
    }

    public void printHtml(String fileName) throws ServletException, java.io.IOException {
        String htmlFilePath = catalinaHome + "/webapps/EWA-Project/" + fileName;
        HttpSession session = request.getSession(true);
        if(fileName.equals("header.html")){
            printHeader(htmlFilePath,session);
           if (session.getAttribute("username")!=null) {
             if (session.getAttribute("role").equals("Admin")) {
               printWriter.print("<li class='upper-links'><a class='links' href='AdminServlet'>Admin Tools</a></li>");
             } else if (session.getAttribute("role").equals("FoodVendor")) {
                 printWriter.print("<li class='upper-links'><a class='links' href='FoodVendorServlet'>Food Vendor Tools</a></li>");
             }
              printWriter.print("<li class='upper-links'><a class='links' href='LoginServlet?logout=true'>Logout</a></li>");
            }
            String content = htmlToString(catalinaHome + "/webapps/EWA-Project/headermain.html");

            int index = content.indexOf("item-number");
            if(index!=-1 && session.getAttribute("cartItems")!=null){
                LinkedHashMap<String,Integer> cartItems = (LinkedHashMap<String,Integer>) session.getAttribute("cartItems");
                String cartLength = String.valueOf(cartItems.size());
                content = content.substring(0,index+13).concat(cartLength).concat(content.substring(index+13+cartLength.length()));
            }
            printWriter.print(content);
        }else{
            printWriter.print(htmlToString(htmlFilePath));
        }
    }
    private void printHeader(String htmlFilePath, HttpSession session) {
        String content = htmlToString(htmlFilePath);
        int index = content.indexOf("Signin");
        if(index!=-1 && session.getAttribute("username")!=null){
            content = content.substring(0,index).concat("Hello "+(String) session.getAttribute("username")).concat(content.substring(index+"Signin".length()));
        }

        printWriter.print(content);
    }

    public static String htmlToString(String fileName) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                htmlContentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        return htmlContentBuilder.toString();
    }

    public static void clearSessionVariables(HttpServletRequest request) throws IOException{
        HttpSession session = request.getSession();
        session.removeAttribute("username");
        session.removeAttribute("password");
        session.removeAttribute("role");
        session.removeAttribute("cartItems");
        session.removeAttribute("restaurantId");
    }

    public static String addHours(Date date, int hours) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.HOUR, hours);
        return simpleDateFormat.format(cal.getTime());
    }

    public static String addMinutes(Date date, int minutes) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minutes);
        return simpleDateFormat.format(cal.getTime());
    }

    public static Date getDate(String dateString){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CST"));
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long generateRandomNumber() {
        Random random = new Random();
        long randomNumber = random.nextInt(100000000 - 100000) + 100000;
        return randomNumber;
    }

}
