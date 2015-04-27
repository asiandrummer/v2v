package cmu.practicum;

import java.util.List;
import java.util.HashMap;

import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.ReceiverAdapter;
import org.jgroups.Message;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

import cmu.practicum.app.Vehicle;

public class JgroupsRpc extends ReceiverAdapter {
    JChannel      channel;
    String        props; // set by application
    public static RpcDispatcher      disp;
    public static HashMap<String, Integer> vehicles;

    public void start() throws Exception {
      vehicles = new HashMap<String, Integer>();
      channel  = new JChannel(props);
      disp     = new RpcDispatcher(channel, this);
      try {
        channel.setReceiver(this);
        channel.connect("toyota");
      } catch (Exception e) {
      }
    }

    public void viewAccepted(View new_view) {
      System.out.println("** view: " + new_view);
    }

    public void receive (Message msg) {
      System.out.println("received: " + msg.getSrc() + ", " + msg.getObject());
    }

    public void send (String str) {
      try {
        Message msg = new Message(null, null, str);
        channel.send(msg);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public View getChannelView() {
      return channel.getView();
    }

    public String getLogicalName() {
      return channel.getName();
    }

    public <T extends CommonAPI<?>> T getVehicle(T appObj) throws Exception {
      appObj.execute();
      return appObj;
    }

    public <T> RspList<T> dispatch(
      ResponseMode responseMode,
      int timeout,
      String methodName,
      T value,
      Class<T> valType
    ) {
      RspList<T> rsp_list;
      RequestOptions opts = new RequestOptions(ResponseMode.GET_ALL, timeout);
      try {
        rsp_list = JgroupsRpc.disp.callRemoteMethods(null,
          "getVehicle",
          new Object[]{value},
          new Class[]{valType},
          opts);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        rsp_list = null;
      }
      return rsp_list;
    }

    public static void main(String[] args) throws Exception {
      new JgroupsRpc().start();
    }
}
