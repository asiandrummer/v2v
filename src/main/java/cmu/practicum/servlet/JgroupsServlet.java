package cmu.practicum.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;

import cmu.practicum.JgroupsRpc;
import cmu.practicum.app.VehicleDistance;

public class JgroupsServlet  extends HttpServlet

{

	private static JgroupsRpc jrpc;

	public void init()
	{

		System.setProperty("java.net.preferIPv4Stack" , "true");
		jrpc=new JgroupsRpc();
        try {
			jrpc.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      	}

	public void destroy()
	{

	}


	public void doGet( HttpServletRequest req, HttpServletResponse rsp )
			throws ServletException, IOException
	{
		//doPost(req,rsp);
                rsp.setContentType("text/html");
                PrintWriter pw = rsp.getWriter();
                pw.write("test get ok");

	}
	public void doPost( HttpServletRequest req, HttpServletResponse rsp )
			throws ServletException, IOException
	{

		  //This is How to submit an object <SystemInfo> for processing into jgroups rpc
        RspList<VehicleDistance> rsp_list=jrpc.dispatch(ResponseMode.GET_ALL, 5000, new VehicleDistance(), VehicleDistance.class);
        List<VehicleDistance> it= rsp_list.getResults();
        PrintWriter pw= rsp.getWriter();
        for (VehicleDistance sinfo: it){
        	pw.println(sinfo.distance);
        	pw.println("Vehiclename =" +sinfo.vehiclename +" , " +" distance="+ sinfo.distance);
        }

	}

}
