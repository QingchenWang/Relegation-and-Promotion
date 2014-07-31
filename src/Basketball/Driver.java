package Basketball;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//
//
//public class Driver {
//	
//	public static void main(String[] args){
//
//		try{
//			File file = new File("Input/TeamData.csv");
//			BufferedReader reader = null;
//			ArrayList<Team> teamList = new ArrayList<Team>();
//			
//			reader = new BufferedReader(new FileReader(file));
//			String text = null;
//			String name, division, rating, mean, alpha, RMSE;
//			//lifeCycle, numYears;
//			
//			reader.readLine();
//			while ((text = reader.readLine()) != null){
//				String[] teamString = text.split(",");
//				name = teamString[0].trim();
//				division = teamString[1].trim();
//				rating = teamString[2].trim();
//				mean = teamString[3].trim();
//				alpha = teamString[4].trim();
//				RMSE = teamString[5].trim();
//				
//				//lifeCycle = teamString[3].trim();
//				//numYears = teamString[4].trim();	
//				//teamList.add(new Team(name, Integer.parseInt(division), Integer.parseInt(rating), Integer.parseInt(lifeCycle), Integer.parseInt(numYears)));
//				teamList.add(new Team(name, Integer.parseInt(division), Integer.parseInt(rating), Double.parseDouble(mean), Double.parseDouble(alpha), Double.parseDouble(RMSE)));
//			}
//			reader.close();
//			// Create teams from data read in
//			Team[] teams = new Team[teamList.size()];
//			for (int i = 0; i < teams.length; i++){
//				teams[i] = teamList.get(i);
//			}
//			
//			// RPP rules
//			reader = new BufferedReader(new FileReader("Input/RPPRules.csv"));
//			text = null;
//			String numToPromote, numToRelegate;
//			reader.readLine();
//			ArrayList<RPPRules> RPPRulesList = new ArrayList<RPPRules>();
//			while ((text = reader.readLine()) != null){
//				String[] RPPString = text.split(",");
//				numToPromote = RPPString[1].trim();
//				numToRelegate = RPPString[2].trim();
//				RPPRulesList.add(new RPPRules(Integer.parseInt(numToPromote), Integer.parseInt(numToRelegate)));
//			}
//			reader.close();
//			RPPRules[] myRPPRules = new RPPRules[RPPRulesList.size()];
//			for (int i = 0; i < myRPPRules.length; i++){
//				myRPPRules[i] = RPPRulesList.get(i);
//			}
//			
//			// scheduling rules
//			reader = new BufferedReader(new FileReader("Input/ScheduleRules.csv"));
//			text = null;
//			String type, numGames, numOutDivision;
//			reader.readLine();
//			text = reader.readLine();
//			String[] scheduleString = text.split(",");
//			type = scheduleString[0].trim();
//			numGames = scheduleString[1].trim();
//			numOutDivision = scheduleString[2].trim();
//			reader.close();
//			SchedulingRules mySchedule = new SchedulingRules(Integer.parseInt(type), Integer.parseInt(numGames), Integer.parseInt(numOutDivision));
//
//			// team dynamics rules
//			reader = new BufferedReader(new FileReader("Input/TeamDynamicRules.csv"));
//			text = null;
//			String numYearsInBuild, numYearsInGrowth, numYearsInPeak, numYearsInDecline, randomVariability,
//				buildRatingChange, growthRatingChange, peakRatingChange, declineRatingChange, divisionEffect;
//			ArrayList<TeamDynamicRules> teamDynamicRulesList = new ArrayList<TeamDynamicRules>();
//			reader.readLine();
//			while ((text = reader.readLine()) != null){
//				String[] progressionString = text.split(",");
//				numYearsInBuild = progressionString[1].trim();
//				numYearsInGrowth = progressionString[2].trim();
//				numYearsInPeak = progressionString[3].trim();
//				numYearsInDecline = progressionString[4].trim(); 
//				randomVariability = progressionString[5].trim();
//				buildRatingChange = progressionString[6].trim();
//				growthRatingChange = progressionString[7].trim();
//				peakRatingChange = progressionString[8].trim();
//				declineRatingChange = progressionString[9].trim();
//				divisionEffect = progressionString[10].trim();
//				teamDynamicRulesList.add(new TeamDynamicRules(Integer.parseInt(numYearsInBuild), Integer.parseInt(numYearsInGrowth), 
//						Integer.parseInt(numYearsInPeak), Integer.parseInt(numYearsInDecline), Integer.parseInt(randomVariability), 
//						Integer.parseInt(buildRatingChange), Integer.parseInt(growthRatingChange), Integer.parseInt(peakRatingChange), 
//						Integer.parseInt(declineRatingChange), Integer.parseInt(divisionEffect)));
//			}
//			reader.close();
//			TeamDynamicRules[] myDynamics = new TeamDynamicRules[teamDynamicRulesList.size()];
//			for(int i = 0; i < myDynamics.length; i++){
//				myDynamics[i] = teamDynamicRulesList.get(i);
//			}
//
//			
//			//double[][] percentages = new double[][] {{0.8, 0.2, 0, 0, 0},{0.2, 0.6, 0.2, 0, 0},{0, 0.2, 0.6, 0.2, 0},{0, 0, 0.2, 0.6, 0.2},{0, 0, 0, 0.2, 0.8}};
////			Dynamics[0] = new TeamDynamicRules(5, percentages, new int[][] {{90, 96}, {86, 92}, {82, 88}, {78, 84}, {74, 80}}, 100, 70);
////			Dynamics[1] = new TeamDynamicRules(5, percentages, new int[][] {{70, 76}, {66, 72}, {62, 68}, {58, 64}, {54, 60}}, 80, 50);
////			Dynamics[2] = new TeamDynamicRules(5, percentages, new int[][] {{50, 56}, {46, 52}, {42, 48}, {38, 44}, {34, 40}}, 60, 30);
//			
//
//						
//			League myLeague = new League("My Sport League", teams, mySchedule, myRPPRules);
//			String results = myLeague.run(1000);
//			
//			System.out.println("it works!!");
//
//			FileWriter outFile = new FileWriter("Output/testFile.rtf");
//			PrintWriter out = new PrintWriter(outFile);
//			out.print(results);
//			out.close();
//			
//			FileWriter outFile2 = new FileWriter("Output/teamInfo.csv");
//			PrintWriter out2 = new PrintWriter(outFile2);
//			for(int i = 0; i < myLeague._teamInfo.length; i++){
//				out2.print(myLeague._teamInfo[i].toString());
//			}
//			out2.close();
//			
//			FileWriter outFileResultsInfo = new FileWriter("Output/resultsInfo.csv");
//			PrintWriter outResultsInfo = new PrintWriter(outFileResultsInfo);
//			outResultsInfo.print(myLeague.getResultsInfo());
//			outResultsInfo.close();
//
//			
//		}
//		catch(Exception e){
//
//		}
//	}
//}