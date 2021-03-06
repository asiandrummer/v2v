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
  String hasAlert;
  final int MAX_SPEED = 80;
  final int MIN_SPEED = 50;

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

  public String getHasAlert() {
    return this.hasAlert;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public void setHasAlert(String alert) {
    this.hasAlert = alert;
  }

  public void execute() {
    Random rand = new Random();
    this.speed = rand.nextInt((MAX_SPEED - MIN_SPEED) + 1) + MIN_SPEED;
    try {
      this.vehicleName = java.net.InetAddress.getLocalHost().getHostName();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
