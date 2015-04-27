package cmu.practicum.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import java.lang.StringBuilder;

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
  private static Vehicle vehicle;
  public void init() {
    System.setProperty("java.net.preferIPv4Stack" , "true");
    vehicle = new Vehicle();
    jrpc = new JgroupsRpc();

    try {
      jrpc.start();
      vehicle.setVehicleName(jrpc.getLogicalName());
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

    StringBuilder stringBuilder = new StringBuilder(1000);
    Scanner s = new Scanner(req.getInputStream());

    while (s.hasNextLine()) {
      stringBuilder.append(s.nextLine());
    }

    StringTokenizer st = new StringTokenizer(stringBuilder.toString(), ",");

    int speed = Integer.parseInt(st.nextToken());
    String hasAlert = st.nextToken();

    RspList<Vehicle> rsp_list = jrpc.dispatch(
      ResponseMode.GET_ALL,
      5000,
      "getVehicle",
      this.vehicle,
      Vehicle.class
    );
    List<Vehicle> it = rsp_list.getResults();

    String vehicleNames = "";
    String alertMessage = "";
    String clusterHasAlert = "false";
    float avgSpeed = 0;

    for (Vehicle sinfo: it){
      vehicleNames += sinfo.getVehicleName() + " ";
      if (sinfo.getVehicleName().compareTo(java.net.InetAddress.getLocalHost().getHostName()) == 0) {
        sinfo.setSpeed(speed);
        sinfo.setHasAlert(hasAlert);
      }

      if (sinfo.getHasAlert() != null &&
          sinfo.getHasAlert().compareTo("false") != 0) {
        System.out.println(sinfo.getHasAlert());
        clusterHasAlert = "true";
        alertMessage += "There has been an alert: " + sinfo.getVehicleName() + "\n";
      }

      avgSpeed += sinfo.getSpeed();
    }
    avgSpeed = avgSpeed / it.size();
    vehicleNames = vehicleNames.trim();
    alertMessage = alertMessage.trim();

    pw.println("{");
    pw.println("\"names\": \"" + vehicleNames + "\", ");
    pw.println("\"average_speed\": \"" + avgSpeed + "\", ");
    pw.println("\"hasAlert\": \"" + clusterHasAlert + "\", ");
    pw.println("\"alertMessage\": \"" + alertMessage + "\"");
    pw.println("}");
  }
}
