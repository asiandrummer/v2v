package cmu.practicum;

import java.util.List;
import java.util.HashMap;

import org.jgroups.JChannel;
import org.jgroups.blocks.MethodCall;
import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.blocks.RpcDispatcher;
import org.jgroups.util.RspList;
import org.jgroups.util.Util;

import cmu.practicum.app.Vehicle;

public class JgroupsRpc {
    JChannel           channel;
    public static  RpcDispatcher      disp;
    String             props; // set by application
    public static HashMap<String, Integer> vehicles;

    public void start() throws Exception {
      vehicles = new HashMap<String, Integer>();
      channel=new JChannel(props);
      disp=new RpcDispatcher(channel, this);
    }

    public void join() {
      try {
        channel.connect("toyota");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    public <T extends CommonAPI<?>> T aggregate() throws Exception {
      return null;
    }

    public HashMap<String, Integer> addVehicle(HashMap<String, Integer> list) throws Exception {
      vehicles.put("test", 2);
      vehicles.put("test2", 2);
      return vehicles;
    }

    public <T> RspList<T> dispatch(
      ResponseMode responseMode,
      int timeout,
      String methodName,
      T value,
      Class<T> valType
    ) {
      RspList<T> rsp_list;
      RequestOptions opts=new RequestOptions(ResponseMode.GET_ALL, timeout);
      try {
        rsp_list=JgroupsRpc.disp.callRemoteMethods(null,
          methodName,
          new Object[]{value},
          new Class[]{valType},
          opts);
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        rsp_list=null;
      }
      return rsp_list;
    }

    public static void main(String[] args) throws Exception {
        new JgroupsRpc().start();
    }
}
