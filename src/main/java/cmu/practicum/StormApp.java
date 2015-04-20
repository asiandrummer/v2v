package cmu.practicum;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.drpc.LinearDRPCTopologyBuilder;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.StormTopology;
import backtype.storm.topology.BasicOutputCollector;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseBasicBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import backtype.storm.utils.DRPCClient;

public class StormApp {
	  public static class FindLargestHostnameBolt extends BaseBasicBolt {
	    @Override
	    public void execute(Tuple tuple, BasicOutputCollector collector) {
	      String input = tuple.getString(1);
	      String hostname="";
	      try {
	      java.net.InetAddress addr = java.net.InetAddress.getLocalHost();
	       hostname = addr.getHostName();   
	      } catch (Exception e){
	    	  
	      }
	   //   if(hostname.length()>input.length()){
	    	  input=hostname+"_"+input;
	     // }
	      collector.emit(new Values(tuple.getValue(0), input ));
	    }

	    @Override
	    public void declareOutputFields(OutputFieldsDeclarer declarer) {
	      declarer.declare(new Fields("id", "result"));
	    }

	  }

	  /*Syntax 
	   * Run below from stormreleasefolder/bin
	   * ./storm jar  </path/to>/toyota-v2v/target/toyota-v2v-1.0-SNAPSHOT.jar cmu.practicum.StormApp   <parallelism> <workers>
	   * you can kill a topology using "./storm  kill largehost-drpc"  and restart afresh always
	   */
	  
	  public static void main(String[] args) throws Exception {
	    LinearDRPCTopologyBuilder builder = new LinearDRPCTopologyBuilder("largehost");
	    int parallelism= Integer.parseInt(args[1]);
	    int workers= Integer.parseInt(args[0]);
	    builder.addBolt(new FindLargestHostnameBolt(), parallelism);

	    Config conf = new Config();
	    conf.setNumWorkers(workers);
	    
	    
	    String hostname="abc";
	    try{
	     hostname=java.net.InetAddress.getLocalHost().getHostName();
	    } catch (Exception e){
	  	  
	    }
	    
	    

	    try {
	    StormSubmitter.submitTopologyWithProgressBar("largehost-drpc", conf, builder.createRemoteTopology());
	    }catch ( AlreadyAliveException e){
		    System.out.println("Already alive");

	    }
	    catch(RuntimeException e){

		    System.out.println("Already deployed");

	    }
	    DRPCClient drpcClient = new DRPCClient("hks-lnx.cisco.com", 3772);
	    String result=drpcClient.execute("largehost", hostname);
	    
	    System.out.println("!!!!!! "+ result);
	    
	    System.out.println("!!!!!!");

	  }
	}
