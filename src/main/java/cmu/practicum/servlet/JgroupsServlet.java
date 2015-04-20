package cmu.practicum.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jgroups.View;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;

import cmu.practicum.JgroupsRpc;
import cmu.practicum.app.Vehicle;

public class JgroupsServlet  extends HttpServlet {
  private static JgroupsRpc jrpc;
  public void init() {
    System.setProperty("java.net.preferIPv4Stack" , "true");
    jrpc = new JgroupsRpc();

    try {
      jrpc.start();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public void destroy() {}

  public void doGet(HttpServletRequest req, HttpServletResponse rsp)
    throws ServletException, IOException {
    rsp.setHeader("Access-Control-Allow-Origin", "*");
    rsp.setContentType("text/html");
    jrpc.join();

    PrintWriter pw = rsp.getWriter();

    RspList<Vehicle> rsp_list = jrpc.dispatch(
      ResponseMode.GET_ALL,
      5000,
      "getVehicle",
      new Vehicle(),
      Vehicle.class
    );
    List<Vehicle> it = rsp_list.getResults();

    float avgSpeed = 0;
    for (Vehicle sinfo: it){
      pw.println("Vehicle Name: " + sinfo.getVehicleName());
      avgSpeed += sinfo.getSpeed();
    }
    avgSpeed = avgSpeed / it.size();
    pw.println("Average Speed: " + avgSpeed);

    pw.println(jrpc.getChannelView());
  }

  public void doPost(HttpServletRequest req, HttpServletResponse rsp)
    throws ServletException, IOException {
    rsp.setHeader("Access-Control-Allow-Origin", "*");
    rsp.setContentType("html/text");
    HashMap<String, Integer> list = new HashMap<String, Integer>();
    list.put(req.getParameter("name"), Integer.parseInt(req.getParameter("speed")));
    RspList<HashMap> rsp_list =
      jrpc.dispatch(ResponseMode.GET_ALL, 5000, "addVehicle", list, HashMap.class);
    List<HashMap> it = rsp_list.getResults();
    PrintWriter pw = rsp.getWriter();
    pw.println(req.getParameter("name") + " received");
    for (HashMap sinfo : it) {
      pw.println(sinfo.keySet());
      pw.println(sinfo.values());
    }
  }
}
