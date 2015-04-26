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
    rsp.setContentType("application/json");

    PrintWriter pw = rsp.getWriter();

    RspList<Vehicle> rsp_list = jrpc.dispatch(
      ResponseMode.GET_ALL,
      5000,
      "getVehicle",
      new Vehicle(),
      Vehicle.class
    );
    List<Vehicle> it = rsp_list.getResults();

    String vehicleNames = "";
    float avgSpeed = 0;
    for (Vehicle sinfo: it){
      vehicleNames += sinfo.getVehicleName() + " ";
      avgSpeed += sinfo.getSpeed();
    }
    avgSpeed = avgSpeed / it.size();
    vehicleNames = vehicleNames.trim();

    pw.println("{");
    pw.println("\"names\": \"" + vehicleNames + "\", ");
    pw.println("\"average_speed\": \"" + avgSpeed + "\"");
    pw.println("}");
  }

  public void doPost(HttpServletRequest req, HttpServletResponse rsp)
    throws ServletException, IOException {
    rsp.setHeader("Access-Control-Allow-Origin", "*");
    rsp.setContentType("application/json");

    PrintWriter pw = rsp.getWriter();
    jrpc.send("test message");

    RspList<Vehicle> rsp_list = jrpc.dispatch(
      ResponseMode.GET_ALL,
      5000,
      "getVehicle",
      new Vehicle(),
      Vehicle.class
    );
    List<Vehicle> it = rsp_list.getResults();

    String vehicleNames = "";
    float avgSpeed = 0;
    for (Vehicle sinfo: it){
      vehicleNames += sinfo.getVehicleName() + " ";
      avgSpeed += sinfo.getSpeed();
    }
    avgSpeed = avgSpeed / it.size();
    vehicleNames = vehicleNames.trim();

    pw.println("{");
    pw.println("\"names\": \"" + vehicleNames + "\", ");
    pw.println("\"average_speed\": \"" + avgSpeed + "\"");
    pw.println("}");
  }
}
