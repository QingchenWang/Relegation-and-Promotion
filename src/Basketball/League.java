package Basketball;
import java.util.ArrayList;
import java.util.Random;

public class League {
	
	private static final int 		ALL_GAMES_WITHIN_DIVISION = 0;
	private static final int		HALF_WITHIN_HALF_OUT = 1;
	private static final int		NO_DIVISION_BARRIER = 2;
	
	private static final int		HOME_WIN = 0;
	private static final int		HOME_LOSS = 1;
	private static final int		DRAW = 2;
		
	public Division[] 				_divisions;
	public Team[] 					_teams;
	public SchedulingRules			_schedulingRules;
	public RPPRules					_rppRules;
	
	private StringBuffer			_textResults;
	
	private StringBuffer[]			_teamInfo;
	private String[]				_teamName;
	private StringBuffer			_resultsInfo;
	
	
	
	
	
	/**
	 * Constructor
	 * Initializes rules, teams, divisions, resultsInfo, teamInfos
	 */
	public League(Team[] teams, SchedulingRules schedulingRules, RPPRules rppRules) {
		/**
		 * 1.  Teams, Scheduling Rules, RPP Rules all get passed in from the UI
		 * 2.  Create divisions in the league from the teams that were passed in
		 * 3.  Initialize the structures holding the three different results:  text, division, team
		 */
		_teams = teams;
		_schedulingRules = schedulingRules;
		_rppRules = rppRules;
		_divisions = createDivisions(_teams);
		_textResults = new StringBuffer();
		initializeResultsInfo();
		initializeTeamInfo();
	}
	private Division[] createDivisions(Team[] teams){
		/**
		 * Creates divisions from the teams that are used for the simulation
		 * 
		 * 1.  Create a list of division number
		 * 2.  For each division number, create a division
		 * 3.  For each division, go through each team and if that team belongs in this division, add the team into the division
		 * 4.  Return the array of divisions
		 */
		ArrayList<Integer> divisionRanks = getDivisionRanks(teams);
		Division[] divisions = new Division[divisionRanks.size()];
		for(int i = 0; i < divisionRanks.size(); i++){
			divisions[i] = new Division(_rppRules);
			for(int j = 0; j < teams.length; j++){				
				if(teams[j].getDivision() == divisionRanks.get(i)){
					divisions[i].addTeam(teams[j]);
				}
			}
		}
		
		return divisions;
	}
			private ArrayList<Integer> getDivisionRanks(Team[] teams){
		/**
		 * Gets the division ranks from the teams and sorts them from small to large
		 * 
		 * 1.  Go through each team, if that team's division does not exist in the list of divisions add that division
		 * 2.  Sorts the list of divisions from largest number to smallest number
		 * 3.  Return the sorted list of division numbers
		 */
		ArrayList<Integer> divisionRanks = new ArrayList<Integer>();
		boolean divisionAlreadyIn = false;
		for(int i = 0; i < teams.length; i++){
			divisionAlreadyIn = false;
			for(int j = 0; j < divisionRanks.size(); j++){
				if(teams[i].getDivision() == divisionRanks.get(j)){
					divisionAlreadyIn = true;
					break;
				}
			}
			if(!divisionAlreadyIn){
				divisionRanks.add(teams[i].getDivision());
			}
		}
		
		for(int i = 0; i < divisionRanks.size(); i++){
			int j = i;
			int B = divisionRanks.get(i);
			while ((j > 0) && (divisionRanks.get(j-1) > B)){
				divisionRanks.set(j, divisionRanks.get(j-1));
				j--;
			}
			divisionRanks.set(j, B);
		}
		return divisionRanks;
	}	
	private void initializeResultsInfo(){
		/**
		 * 1.  Create a new StringBuffer called resultsInfo
		 * 2.  Append "Year"
		 * 3.  For each division, append that division's "Average Rating", "Standard Deviation of Rating", and "Number of teams in division who are supposed to be in division"
		 * 4.  Append new line character
		 */
		_resultsInfo = new StringBuffer();
		_resultsInfo.append("Year,");
		for(int i = 0; i < _divisions.length; i++){
			_resultsInfo.append("Division " + i + "-Average Rating,");
			_resultsInfo.append("Division " + i + "-Standard Deviation of Rating,");
			_resultsInfo.append("Division " + i + "-Number of teams in division who are supposed to be in division,");
		}
		_resultsInfo.append("\n");
	}	
	private void initializeTeamInfo(){
		/**
		 * 1.  Create an array of StringBuffer called teamInfo with size equal to number of teams in league
		 * 2.  Create an array of strings called teamName with size equal to number of teams in league
		 * 3.  For each StringBuffer in teamInfo, create a new StringBuffer structure
		 * 4.  Assign each team's name into the teamName array
		 */
		_teamInfo = new StringBuffer[_teams.length];
		_teamName = new String[_teams.length];
		for(int i = 0; i < _teamInfo.length; i++){
			_teamInfo[i] = new StringBuffer();
		}
		for(int i = 0; i < _teamName.length; i++){
			_teamName[i] = _teams[i].getName();
		}
	}
	
	
	
	
	
	/**
	 * Run the simulation (x number of years)
	 * Simulates season, off season, returns text results
	 */
	public void run(int years) throws Exception{
		/**
		 * 1.  For each year
		 * 2.  Append "Year: " onto textResults
		 * 3.  Simulate season's worth of games
		 * 4.  Append season standings onto textResults
		 * 5.  Append teamInfo and resultsInfo for years after the first 100
		 * 6.  Simulate off season
		 */
		for(int i = 1; i <= years; i++){
			_textResults.append("Year: " + i + "\n");
			simulateSeasonPlay();
			appendStandings();
			if(i > 100){
				appendTeamInfo();
				_resultsInfo.append("Year " + i + "," + appendResultsInfo());
			}
			simulateOffseason();
		}
	}
	private void simulateSeasonPlay(){
		/**
		 * Simulates a season of play for each division with respect to scheduling rules
		 * 1.  Get number of games to play
		 * 2.  Figure out the schedule type
		 * 3.  Within Division Play:  for each division, get number of games to play against each team within division, then call simulateRoundRobinGames()
		 * 4.  Half In Half Out:  First simulate games within division just like above.  Then simulate individual games between teams out of division
		 * 5.  Inter-Division Play:  Same as above
		 * 6.  Print text results
		 */
		int numGamesAgainstDiv = _schedulingRules.getNumGamesAgainstDiv();
		int numGamesAgainstOut = _schedulingRules.getNumGamesAgainstOut();
		
		switch(_schedulingRules.getType()){
		case ALL_GAMES_WITHIN_DIVISION:
			// All games within division
			// Each team plays every other team within its division in a round robin style
			for(int i = 0; i < _divisions.length; i++){
				// First, simulate round robin games
				for(int j = 0; j < numGamesAgainstDiv; j++){
					_divisions[i].simulateRoundRobinGames();
				}
			}
			break;
			
		case HALF_WITHIN_HALF_OUT:
			// Half games within division, half out
			// Simulate games within division first
			for(int i = 0; i < _divisions.length; i++){
				// First, simulate round robin games within division
				for(int j = 0; j < numGamesAgainstDiv; j++){
					_divisions[i].simulateRoundRobinGames();
				}
			}

			// Simulate out of division games
			for(int x = 0; x < numGamesAgainstOut; x++){
				for(int i = 0; i < _divisions.length; i++){
					for(int j = 0; j < _divisions[i].getNumTeams(); j++){
						for(int k = 0; k < _divisions.length; k++){
							if(k > i){
								for(int a = 0; a < _divisions[k].getNumTeams(); a++){
									Team awayTeam = _divisions[k].getTeam(a);
									int result = _divisions[i].simulateIndividualGame(j, awayTeam);

									switch(result){
									case HOME_WIN:
										awayTeam.addLoss();
										break;
									case HOME_LOSS:
										awayTeam.addWin();
										break;
									case DRAW:
										awayTeam.addDraw();
										break;
									}
								}
							}
						}
					}
				}
			}	
			break;
			
		case NO_DIVISION_BARRIER:
			// Simulate in division games
			for(int i = 0; i < _divisions.length; i++){
				for(int j = 0; j < numGamesAgainstDiv; j++){
					_divisions[i].simulateRoundRobinGames();
				}
			}
			
			// Simulate out of division games
			for(int x = 0; x < numGamesAgainstOut; x++){
				for(int i = 0; i < _divisions.length; i++){
					for(int j = 0; j < _divisions[i].getNumTeams(); j++){
						for(int k = 0; k < _divisions.length; k++){
							if(k > i){
								for(int a = 0; a < _divisions[k].getNumTeams(); a++){
									Team awayTeam = _divisions[k].getTeam(a);
									int result = _divisions[i].simulateIndividualGame(j, awayTeam);

									switch(result){
									case HOME_WIN:
										awayTeam.addLoss();
										break;
									case HOME_LOSS:
										awayTeam.addWin();
										break;
									case DRAW:
										awayTeam.addDraw();
										break;
									}
								}
							}
						}
					}
				}
			}	
			break;
		}
		for(int i = 0; i < _divisions.length; i++){
			_divisions[i].printResults();
		}
	}
	private void appendStandings(){
		/**
		 * Obtains current standings for each division, and appends it onto the league results
		 */
		for(int i = 0; i < _divisions.length; i++){
			_textResults.append("Division: " + i + "\n");
			_textResults.append(_divisions[i].getResults() + "\n");
		}
	}
	private void appendTeamInfo(){
		/**
		 * Puts together the team info for each team this season
		 * 
		 * 1.  For each team, append the team name, division, rank, rating, and winning percentage
		 */
		for(int i = 0; i < _teamName.length; i++){
			Team team = findTeam(_teamName[i]);
			_teamInfo[i].append(team.getName() + "," +team.getDivision() + "," + 
					team.getRank() + "," +  team.getRating() + "," + team.getWinPercentage() + "\n");
		}
	}
			private Team findTeam(String teamName){
		/**
		 * Returns the desired team
		 */
		for(int i = 0; i < _divisions.length; i++){
			Team team = _divisions[i].findTeam(teamName);
			if(team != null){
				return team;
			}
		}
		return null;
	}
	private StringBuffer appendResultsInfo(){
		/**
		 * Puts together results info for each division and each team for this season
		 * 
		 * 1.  Create an array of teams ranked by their rating
		 * 2.  For each division, compare each team within that division with the set of teams that should be in that division based on rating to determine the number of teams that are correctly slotted
		 * 3.  Return resultsInfo
		 */
		StringBuffer temp = new StringBuffer();
		Team[] team = rankTeamsByRating();
		int count = 0;
		
		for(int i = 0; i < _divisions.length; i++){
			int numTeamsShouldBelong = 0;
			Team[] teamSet = new Team[_divisions[i].getNumTeams()];
			for(int j = 0; j < teamSet.length; j++){
				teamSet[j] = team[count];
				count++;
			}
			Team[] divisionTeamSet = _divisions[i].getTeams();
			int divTeamRating = 0;
			int idealTeamRatingMin = 0;
			int idealTeamRatingMax = 0;
			for(int j = 0; j < divisionTeamSet.length; j++){
				divTeamRating = divisionTeamSet[j].getRating();
				idealTeamRatingMin = teamSet[teamSet.length - 1].getRating();
				idealTeamRatingMax = teamSet[0].getRating();
				if(divTeamRating >= idealTeamRatingMin && divTeamRating <= idealTeamRatingMax){
					numTeamsShouldBelong++;
				}
//			
//				for(int k = 0; k < teamSet.length; k++){
//					if(divisionTeamSet[j].getRating() == teamSet[k].getRating()){
//						numTeamsShouldBelong++;
//						break;
//					}
//				}

			}
			temp.append(_divisions[i].printResultInfo() + "" + numTeamsShouldBelong + ",");
		}
		
		temp.append("\n");
		return temp;
	}
			private Team[] rankTeamsByRating(){
		/**
		 * Ranks all teams in the league by rating
		 * 
		 * 1.  Create an array of all teams from all divisions
		 * 2.  Sort by rating (from large to small)
		 * 3.  Return the sorted array
		 */
		Team[] team = new Team[_teams.length];
		int count = 0;
		for(int i = 0; i < _divisions.length; i++){
			Team[] temp = _divisions[i].getTeams();
			for(int j = 0; j < temp.length; j++){
				team[count] = temp[j];
				count++;
			}
		}

		for(int i = 0; i < team.length; i++){
			int j = i;
			Team B = team[i];
			while ((j > 0) && (team[j-1].getRating() < B.getRating())){
				team[j] = team[j-1];
				j--;
			}
			team[j] = B;
		}
		return team;
	}
	private void simulateOffseason() throws Exception{
		/**
		 * Simulate off season
		 * 
		 * 1.  Promote/Relegate
		 * 2.  Progression
		 * 3.  Wipe records
		 */
		promoteRelegate();
		progressRegress();
		wipeRecords();
	}
			private void wipeRecords(){
		/**
		 * Wipes all records for each division
		 */
		for(int i = 0; i < _divisions.length; i++){
			_divisions[i].wipeRecords();
		}
	}
			private void progressRegress(){
		/**
		 * Activates progression for each division based on its team dynamics rule
		 */
		for(int i = 0; i < _divisions.length; i++){
			_divisions[i].activateProgression(_divisions.length);
		}
	}
			private void promoteRelegate() throws Exception{
		/**
		 * Promotes and relegates teams from each division based on its RPP rules
		 * 
		 * 1.  Create an array of teams for the teams that need to be moved (whether it's promotion or relegation)
		 * 2.  For each division, call activatePromotionRelegation() and merge the result with the teamsToMove array (top division only relegate, bottom division only promote, the rest go both ways)
		 * 3.  For each team to be moved, assign that team to the correct division by adding that team to the division
		 */
		Team[] teamsToMove = new Team[0];
		for(int i = 0; i < _divisions.length; i++){
			if(i == 0)
				teamsToMove = mergeArrays(teamsToMove, _divisions[i].activatePromotionRelegation(true, false));
			else if(i == _divisions.length - 1)
				teamsToMove = mergeArrays(teamsToMove, _divisions[i].activatePromotionRelegation(false, true));
			else
				teamsToMove = mergeArrays(teamsToMove, _divisions[i].activatePromotionRelegation(false, false));
		}
		
		for(int i = 0; i < teamsToMove.length; i++){
			int division = teamsToMove[i].getDivision();
			_divisions[division].addTeam(teamsToMove[i]);
		}
	}
					private Team[] mergeArrays(Team[] array1, Team[] array2){
				/**
				 * This is a helper method to merge two arrays of teams into one
				 *
				 * 1.  Create a new array that's the size of both input arrays
				 * 2.  Assign each item in both input arrays into the new array
				 * 3.  Return the new array
				 */
				Team[] myArray = new Team[array1.length + array2.length];
				for(int i = 0; i < myArray.length; i++){
					if(i < array1.length){
						myArray[i] = array1[i];
					}
					else{
						myArray[i] = array2[i - array1.length];
					}
				}
				return myArray;
			}
	
	
	
	
	/**
	 * Getter methods
	 */
	public String getResultsInfo(){
		/**
		 * Getter method for resultsInfo
		 */
		return _resultsInfo.toString();
	}
	public StringBuffer[] getTeamInfo(){
		/**
		 * Getter method for teamInfo
		 */
		return _teamInfo;
	}
	public String getTextResults(){
		return _textResults.toString();
	}
	
}
