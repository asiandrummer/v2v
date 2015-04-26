package cmu.practicum.app;

import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.jgroups.blocks.RequestOptions;
import org.jgroups.blocks.ResponseMode;
import org.jgroups.util.RspList;

import cmu.practicum.CommonAPI;
import cmu.practicum.JgroupsRpc;

/*
 * Sample object which can be submitted into jgroups rpc for processing
 */
public class Vehicle extends CommonAPI {

  int speed;
  String vehicleName;

  /**
  * @return distance  distance of the vehicle
  */
  public int getSpeed() {
    return this.speed;
  }

  /**
  * @return vehiclename  name of the vehicle
  */
  public String getVehicleName() {
    return this.vehicleName;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public void execute() {
    Random rdn = new Random();
    this.speed = rdn.nextInt(30) + 50;
    try {
      this.vehicleName = java.net.InetAddress.getLocalHost().getHostName();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
  }
}
