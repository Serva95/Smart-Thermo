package dispatcher;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.rmi.ServerException;
import java.util.logging.Level;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import services.logservice.LogService;

@MultipartConfig(fileSizeThreshold=1024*1024*5, maxFileSize=1024*1024*20, maxRequestSize=1024*1024*30)
@WebServlet(name = "Connector", urlPatterns = {"/Connector"})
public class Connector extends HttpServlet {
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            
            String livesearch = request.getParameter("livesearch");
            if(livesearch!=null){
                String controllerAction="HomeManagement."+livesearch.split(",")[0];
                
                String[] splittedAction=controllerAction.split("\\.");
                Class<?> controllerClass=Class.forName("controller."+splittedAction[0]);
                Method controllerMethod=controllerClass.getMethod(splittedAction[1],HttpServletRequest.class,HttpServletResponse.class);
                //LogService.getApplicationLogger().log(Level.INFO,splittedAction[0]+" "+splittedAction[1]);
                controllerMethod.invoke(null,request,response);
                /*
                String viewUrl=(String)request.getAttribute("viewUrl");
                
                RequestDispatcher view;
                view=request.getRequestDispatcher(viewUrl+".jsp");
                view.forward(request,response);*/
                
            }else{
                
                String controllerAction=request.getParameter("ca");
                
                if (controllerAction==null) controllerAction="HomeManagement.view";
                
                String[] splittedAction=controllerAction.split("\\.");
                Class<?> controllerClass=Class.forName("controller."+splittedAction[0]);
                Method controllerMethod=controllerClass.getMethod(splittedAction[1],HttpServletRequest.class,HttpServletResponse.class);
                //LogService.getApplicationLogger().log(Level.INFO,splittedAction[0]+" "+splittedAction[1]);
                controllerMethod.invoke(null,request,response);
                
                String viewUrl=(String)request.getAttribute("viewUrl");
                
                RequestDispatcher view;
                view=request.getRequestDispatcher(viewUrl+".jsp");
                view.forward(request,response);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace(out);
            throw new ServerException("Dispacther Servlet Error",e);
            
        } finally {
            out.close();
        }
        
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
