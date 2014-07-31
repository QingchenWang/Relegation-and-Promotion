package Golf;

import java.util.ArrayList;

public class League {

	public ArrayList<Division> 		_divisions;
	public ArrayList<Player> 		_players;
	public RPPRules					_rppRules;
	
	private StringBuffer			_resultsInfo;
	private StringBuffer			_seasonalResults;
	private ArrayList<ArrayList<ArrayList<TournamentResults>>> 	_tournamentResults;
		
	private static ArrayList<Tournament> 			TOURNAMENTS_PGA;
	private static ArrayList<Tournament> 			TOURNAMENTS_NATIONWIDE;
	private static final int						TOURNAMENT_TYPE_PGA = 0;
	private static final int						TOURNAMENT_TYPE_NATIONWIDE = 1;
	private static final int						TOURNAMENT_SIZE_PGA = 156;
	private static final int						TOURNAMENT_SIZE_NATIONWIDE = 144;
	
	private ArrayList<StringBuffer>	_careersInfo;
	
	/*
	 * Constructor
	 */
	public League(ArrayList<Player> players, RPPRules rppRules){
		initializeTournaments();
		_players = players;
		_rppRules = rppRules;
		_divisions = createDivisions(_players);
		_tournamentResults = new ArrayList<ArrayList<ArrayList<TournamentResults>>>();
		for(int i = 0; i < _divisions.size(); i++){
			_tournamentResults.add(new ArrayList<ArrayList<TournamentResults>>());
		}
		initializeResultsInfo();
		initializeSeasonalResults();
		_careersInfo = new ArrayList<StringBuffer>();
		_careersInfo.add(new StringBuffer("Start"));
	}
	
	/*
	 * Sets up the tournaments
	 */
	private void initializeTournaments(){
		//TODO: Consider including a greater variety of tournaments
		TOURNAMENTS_PGA = new ArrayList<Tournament>();
		TOURNAMENTS_NATIONWIDE = new ArrayList<Tournament>();
		
		// add Nationwide tournaments
		for(int i = 0; i < 30; i++){
			TOURNAMENTS_NATIONWIDE.add(new Tournament(Integer.toString(i), TOURNAMENT_SIZE_NATIONWIDE, TOURNAMENT_TYPE_NATIONWIDE, 4, 500000, 60));
		}
		
		// add PGA tournaments
		for(int i = 0; i < 45; i++){
			TOURNAMENTS_PGA.add(new Tournament(Integer.toString(i), TOURNAMENT_SIZE_PGA, TOURNAMENT_TYPE_PGA, 4, 5000000, 70));
		}
	}

	/*
	 * 	Set up the stream to output simulation results
	 */
	private void initializeResultsInfo(){
		/**
		 * 1.  Create a new StringBuffer called resultsInfo
		 * 2.  Append "Year"
		 * 3.  For each division, append that division's "Average Rating", "Standard Deviation of Rating", and "Number of teams in division who are supposed to be in division"
		 * 4.  Append new line character
		 */
		_resultsInfo = new StringBuffer();
		_resultsInfo.append("Year,");
		for(int i = 0; i < _divisions.size(); i++){
			_resultsInfo.append("Division " + i + "-Average Rating,");
			_resultsInfo.append("Division " + i + "-Standard Deviation of Rating,");
			_resultsInfo.append("Division " + i + "-Number of players in division who are supposed to be in division,");
		}
		_resultsInfo.append("\n");
	}
	
	private void initializeSeasonalResults(){
		_seasonalResults = new StringBuffer();
		_seasonalResults.append("Player Name," + "Rank," + "Age," + "Expected Rank," + "Rating," + "Tournaments Played," + "Tournaments Won," + "Earnings," + "Average Score," + "A," + "B," + "C," + "Expected Career Best");
		_seasonalResults.append("\n");
	}
	
	/*
	 * Creates divisions
	 */
	private ArrayList<Division> createDivisions(ArrayList<Player> players){
		/**
		 * Creates divisions from the teams that are used for the simulation
		 * 
		 * 1.  Create a list of division number
		 * 2.  For each division number, create a division
		 * 3.  For each division, go through each team and if that team belongs in this division, add the team into the division
		 * 4.  Return the array of divisions
		 */
		ArrayList<Integer> divisionRanks = getDivisionRanks(players);
		ArrayList<Division> divisions = new ArrayList<Division>();
		for(int i = 0; i < divisionRanks.size(); i++){
			//currently only allows two divisions
			if(i == 0)
				divisions.add(new Division(_rppRules, TOURNAMENTS_PGA, "PGA", 60));
			else if(i == 1)
				divisions.add(new Division(_rppRules, TOURNAMENTS_NATIONWIDE, "Nationwide", 80));
			for(int j = 0; j < players.size(); j++){				
				if(players.get(j).getDivision() == divisionRanks.get(i)){
					divisions.get(i).addPlayer(players.get(j));
				}
			}
		}
		
		return divisions;
	}
	
	private ArrayList<Integer> getDivisionRanks(ArrayList<Player> players){
		/**
		 * Gets the division ranks from the teams and sorts them from small to large
		 * 
		 * 1.  Go through each team, if that team's division does not exist in the list of divisions add that division
		 * 2.  Sorts the list of divisions from largest number to smallest number
		 * 3.  Return the sorted list of division numbers
		 */
		ArrayList<Integer> divisionRanks = new ArrayList<Integer>();
		boolean divisionAlreadyIn = false;
		for(int i = 0; i < players.size(); i++){
			divisionAlreadyIn = false;
			for(int j = 0; j < divisionRanks.size(); j++){
				if(players.get(i).getDivision() == divisionRanks.get(j)){
					divisionAlreadyIn = true;
					break;
				}
			}
			if(!divisionAlreadyIn){
				divisionRanks.add(players.get(i).getDivision());
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
	
	public void run(int years) throws Exception{
		/*
		 * For each division, first simulation season play, then append the results information, and finally simulate offseason
		 */
		for(int i = 0; i < years; i++){
			System.out.println("Simulating season " + i);
			simulateSeasonPlay();
			if(i >= 100){
				_resultsInfo.append("Year " + i + "," + appendResultsInfo());
			}
			if(i >= (years - 100)){
				_seasonalResults.append("Year " + i + "\n");
				_seasonalResults.append(appendSeasonalResults());
				
				// fill out the career info data
				for(int a = 0; a < _divisions.size(); a++){
					if(i >= (years - 10)){
						_tournamentResults.get(a).add(_divisions.get(a).getTournamentResults());
					}
//
//					for(int j = 0; j < _divisions.get(a).getMembers().size(); j++){
//						// for each full time player, append this season's ISL
//						boolean exists = false;
//						for(int k = 0; k < _careersInfo.size(); k++){
//							if(_careersInfo.get(k).toString().startsWith(_divisions.get(a).getMembers().get(j).getName() + ",")){
//								_careersInfo.get(k).append(_divisions.get(a).getMembers().get(j).getRating() + ",");
//								exists = true;
//								break;
//							}
//						}
//						if(!exists)
//							_careersInfo.add(new StringBuffer(_divisions.get(a).getMembers().get(j).getName() + "," + _divisions.get(a).getMembers().get(j).getRating() + ","));
//					}
				}
			}
			simulateOffseason();
		}
	}
	
	public void simulateSeasonPlay(){
		for(int j = 0; j < _divisions.size(); j++){
			_divisions.get(j).simulateSeason();
		}
	}
	
	public void simulateOffseason() throws Exception{
		/*
		 * first go through relegation and promotion (retirement included), then go through player progression, finally reset the information for each division
		 */
		promoteRelegate();
		progressRegress();
		reset();
	}
	
	public void promoteRelegate() throws Exception{
		/*
		 * first retire players and add the extra slots into the slots allowed for entry via end-of-season tournament, then relegate and promote
		 */
		int numPGARetirement = _divisions.get(0).retirement();
		int numNationwideRetirement = _divisions.get(1).retirement();
		
		// split the divisions into groups: PGA-keep (top 125 default), PGA-remove, Nationwide-all
		ArrayList<ArrayList<Player>> relegationSystemPGA = _divisions.get(0).activatePromotionRelegation(true, false);
		ArrayList<ArrayList<Player>> relegationSystemNationwide = _divisions.get(1).activatePromotionRelegation(false, true);
		
		ArrayList<Player> playersPGAKeep = relegationSystemPGA.get(0);
		ArrayList<Player> playersPGADemoted = relegationSystemPGA.get(1);
		ArrayList<Player> playersNationwide = relegationSystemNationwide.get(1);
		ArrayList<Player> playersNationwideKeep = new ArrayList<Player>();
		
		// direct promotion of top Nationwide players
		// note that all Nationwide players accept promotion into the PGA tour regardless of part-time or placeholder status
		for(int i = 0; i < _rppRules.getNumPGAFromNationwideAuto(); i++){
			playersNationwide.get(0).setDivision(0);
			playersNationwide.get(0).setFullTime(true);
			playersPGAKeep.add(playersNationwide.get(0));
			playersNationwide.remove(0);
		}
		
		QSchool qSchool = new QSchool(true, (_rppRules.getNumPGAFromTournament() + numPGARetirement), _rppRules.getNumNationwideNew(), 
				_rppRules.getNumPGAFromNationwideAuto(), _rppRules.getNumPGARemain(),
				playersPGADemoted, playersNationwide);
		
		// direct promotion from QSchool into PGA
		// note that part-time PGA players will not accept membership in PGA
		ArrayList<Player> playersPromotedToPGA = qSchool.getPromotedPGAPlayers();
		while(playersPromotedToPGA.size() > 0){
			playersPromotedToPGA.get(0).setDivision(0);
			playersPGAKeep.add(playersPromotedToPGA.get(0));
			playersPromotedToPGA.remove(0);
		}
		
		// retain players into Nationwide tour along with some generated new players
		// note that part-time players from either the Nationwide or PGA tours will not accept membership in Nationwide
		ArrayList<Player> playersRetainedNationwide = qSchool.getPromotedNationwidePlayers();
		while(playersRetainedNationwide.size() > 0){
			playersRetainedNationwide.get(0).setDivision(1);
			playersNationwideKeep.add(playersRetainedNationwide.get(0));
			playersRetainedNationwide.remove(0);
		}
		
		// assign players into the correct divisions and set up proper full-time and placeholder status
		for(int i = 0; i < playersPGAKeep.size(); i++){
			playersPGAKeep.get(i).setFullTime(true);
			playersPGAKeep.get(i).setPlaceholder(false);
			if(playersPGAKeep.get(i).getName().startsWith("Placeholder") || playersPGAKeep.get(i).getName().startsWith("Part")){
				playersPGAKeep.get(i).setName("Former " + playersPGAKeep.get(i).getName());
			}
		}
		for(int i = 0; i < playersNationwideKeep.size(); i++){
			playersNationwideKeep.get(i).setFullTime(true);
			playersNationwideKeep.get(i).setPlaceholder(false);
			if(playersNationwideKeep.get(i).getName().startsWith("Placeholder") || playersNationwideKeep.get(i).getName().startsWith("Part")){
				playersNationwideKeep.get(i).setName("Former " + playersNationwideKeep.get(i).getName());
			}
		}
		
		_divisions.get(0).setPlayers(playersPGAKeep);
		_divisions.get(1).setPlayers(playersNationwideKeep);
	}
	
	public void progressRegress(){
		for(int i = 0; i < _divisions.size(); i++){
			_divisions.get(i).activateProgression();
		}
	}
	
	public void reset(){
		for(int i = 0; i < _divisions.size(); i++){
			_divisions.get(i).reset();
		}
	}
	
	private Player[] mergeArrays(Player[] array1, Player[] array2){
		/**
		 * This is a helper method to merge two arrays of teams into one
		 *
		 * 1.  Create a new array that's the size of both input arrays
		 * 2.  Assign each item in both input arrays into the new array
		 * 3.  Return the new array
		 */
		Player[] myArray = new Player[array1.length + array2.length];
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
	
	private StringBuffer appendResultsInfo(){
		/**
		 * Puts together results info for each division and each team for this season
		 * 
		 * 1.  Create an array of teams ranked by their rating
		 * 2.  For each division, compare each team within that division with the set of teams that should be in that division based on rating to determine the number of teams that are correctly slotted
		 * 3.  Return resultsInfo
		 */
		StringBuffer temp = new StringBuffer();
		ArrayList<Player> player = rankPlayersByRating();
		double idealplayerRatingBest = 0;
		double idealplayerRatingWorst = 0;
		
		// for each division, calculate the number of membership players in that division that should have been in the division based on ISL
		for(int i = 0; i < _divisions.size(); i++){
			int numplayersShouldBelong = 0;
			int totalPlayersBefore = 0;
			for(int k = i-1; k >= 0; k--){
				totalPlayersBefore += _divisions.get(k).getNumMembers();
			}
			idealplayerRatingBest = player.get(totalPlayersBefore).getRating();
			idealplayerRatingWorst = player.get(totalPlayersBefore + _divisions.get(i).getNumMembers() - 1).getRating();
			for(int j = 0; j < _divisions.get(i).getNumMembers(); j++){
				if(_divisions.get(i).getMember(j).getRating() >= idealplayerRatingBest && _divisions.get(i).getMember(j).getRating() <= idealplayerRatingWorst)
					numplayersShouldBelong++;
			}
			
			temp.append(_divisions.get(i).printResultInfo() + "" + numplayersShouldBelong + ",");
		}
		
		temp.append("\n");
		return temp;
	}
	
	private StringBuffer appendSeasonalResults(){
		StringBuffer temp = new StringBuffer();
		for(int i = 0; i < _divisions.size(); i++){
			temp.append(_divisions.get(i).printSeasonalResults());
			temp.append("\n");
		}
		return temp;
	}
	
	private ArrayList<Player> rankPlayersByRating(){
		/**
		 * Ranks all teams in the league by rating
		 * 
		 * 1.  Create an array of all teams from all divisions
		 * 2.  Sort by rating (from large to small)
		 * 3.  Return the sorted array
		 */
		ArrayList<Player> players = new ArrayList<Player>();
		for(int i = 0; i < _divisions.size(); i++){
			players.addAll(_divisions.get(i).getMembers());
		}

		for(int i = 0; i < players.size(); i++){
			int j = i;
			Player B = players.get(i);
			while ((j > 0) && (players.get(j-1).getRating() > B.getRating())){
				players.set(j, players.get(j-1));
				j--;
			}
			players.set(j, B);
		}
		return players;
	}
	
	public String getResultsInfo(){
		/**
		 * Getter method for resultsInfo
		 */
		return _resultsInfo.toString();
	}
	
	public String getSeasonalResults(){
		/**
		 * Getter method for seasonalResults
		 */
		return _seasonalResults.toString();
	}
	
	public ArrayList<StringBuffer> getCareersInfo(){
		return _careersInfo;
	}
	
	public String getPGATournamentResults(){
		/**
		 * Getter method for tournamentResults
		 */
		StringBuffer tournamentResults = new StringBuffer();
		tournamentResults.append("Year," + "Tournament," + "Rank," + "Player Name," + "Player Rating," + "Round 1," + "Round 2," + "Round 3," + "Round 4," + "Total Score," + "Prize" + "\n");
		for(int i = 0; i < _tournamentResults.get(0).size(); i++){
			for(int j = 0; j < _tournamentResults.get(0).get(i).size(); j++){
				ArrayList<PlayerResult> p = _tournamentResults.get(0).get(i).get(j).getResults();
				for(int k = 0; k < p.size(); k++){
					tournamentResults.append(i + "," + _tournamentResults.get(0).get(i).get(j).getTournamentName() + "," + p.get(k).getTournamentFinishRank() + "," + p.get(k).getPlayer().getName() + "," + p.get(k).getPlayer().getRating() + "," 
							+ p.get(k).getRoundOne() + "," + p.get(k).getRoundTwo() + "," + p.get(k).getRoundThree() + "," + p.get(k).getRoundFour() + "," + p.get(k).getTournamentFinish() + "," + p.get(k).getPrize() + "\n");
				}
			}
		}
		return tournamentResults.toString();
	}
	public String getNationwideTournamentResults(){
		/**
		 * Getter method for tournamentResults
		 */
		StringBuffer tournamentResults = new StringBuffer();
		tournamentResults.append("Year," + "Tournament," + "Rank," + "Player Name," + "Player Rating," + "Round 1," + "Round 2," + "Round 3," + "Round 4," + "Total Score," + "Prize" + "\n");
		for(int i = 0; i < _tournamentResults.get(1).size(); i++){
			for(int j = 0; j < _tournamentResults.get(1).get(i).size(); j++){
				ArrayList<PlayerResult> p = _tournamentResults.get(1).get(i).get(j).getResults();
				for(int k = 0; k < p.size(); k++){
					tournamentResults.append(i + "," + _tournamentResults.get(1).get(i).get(j).getTournamentName() + "," + p.get(k).getTournamentFinishRank() + "," + p.get(k).getPlayer().getName() + "," + p.get(k).getPlayer().getRating() + "," 
							+ p.get(k).getRoundOne() + "," + p.get(k).getRoundTwo() + "," + p.get(k).getRoundThree() + "," + p.get(k).getRoundFour() + "," + p.get(k).getTournamentFinish() + "," + p.get(k).getPrize() + "\n");
				}
			}
		}
		return tournamentResults.toString();
	}
}
