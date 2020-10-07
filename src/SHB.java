import cmd.CmdOptions;
import cmd.GetOptions;
import engine.racedetectionengine.shb.distance.SHBOfflineEngine;

public class SHB {

	public SHB() {

	}
	
	public static void main(String[] args) {		
		CmdOptions options = new GetOptions(args).parse();
		
		boolean time_reporting = true;
		long startTimeAnalysis = 0;
		if(time_reporting){
			startTimeAnalysis = System.currentTimeMillis();
		}
		
		SHBOfflineEngine engine = new SHBOfflineEngine(options.parserType, options.path);
		engine.analyzeTrace(options.multipleRace, options.verbosity);
		
		if(time_reporting){
			long stopTimeAnalysis = System.currentTimeMillis();
			long timeAnalysis = stopTimeAnalysis - startTimeAnalysis;
			System.out.println("Time for analysis = " + timeAnalysis + " milliseconds");
		}	
	}
}
