package Basketball;
import java.util.Random;


public class Division {
	
	private Team[] 				_teams;
	private RPPRules			_rppRules;
	private String				_seasonResults;
		
	/**
	 * Constructor
	 */
	public Division(RPPRules rppRules){
		_teams = new Team[0];
		_rppRules = rppRules;
	}
	
	/**
	 * Add/remove teams
	 */
	public void addTeam(Team team){
		/**
		 * Adds the specified team into the division
		 * Used to initially create the divisions and also during promotion/relegation
		 */
		Team[] tempTeams = new Team[_teams.length + 1];
		for(int i = 0; i < _teams.length; i++){
			tempTeams[i] = _teams[i];
		}
		tempTeams[tempTeams.length - 1] = team;
		_teams = tempTeams;
	}
	private void removeTeam(Team team){
		/**
		 * Removes the specified team from the division
		 * Used during promotion/relegation to remove the promote/relegated team
		 */
		Team[] tempTeams = new Team[_teams.length - 1];
		int count = 0;
		for(int i = 0; i < _teams.length; i++){
			if(!_teams[i].equals(team)){
				tempTeams[count] = _teams[i];
				count++;
			}
		}
		_teams = tempTeams;
	}
	
	/**
	 * Public use only
	 */
	public Team getTeam(int index){
		/**
		 * Gets the x-th team of the division
		 */
		return _teams[index];
	}
	public Team[] getTeams(){
		/**
		 * Gets all of the teams in the division
		 */
		return _teams;
	}
	public int getNumTeams(){
		/**
		 * Gets the number of teams in the division
		 */
		return _teams.length;
	}
	public void simulateRoundRobinGames(){
		/**
		 * For each team in the division, play every other team once
		 */
		for(int i = 0; i < _teams.length; i++){
			for(int j = 0; j < _teams.length; j++){
				if(j > i){
					int result = simulateGame(_teams[i], _teams[j]);
					// home team wins
					if(result == 0){
						_teams[i].addWin();
						_teams[j].addLoss();
					}
					// away team wins
					else if(result == 1){
						_teams[i].addLoss();
						_teams[j].addWin();
					}
					// draw
					else{
						_teams[i].addDraw();
						_teams[j].addDraw();
					}
				}
			}
		}
	}
	public int simulateIndividualGame(int divisionTeamIndex, Team otherTeam){
		/**
		 * Simulates a game between a team of this division and another specified team
		 * This is used to simulate inter-division games
		 */
		int result = simulateGame(_teams[divisionTeamIndex], otherTeam);
		if(result == 0){
			_teams[divisionTeamIndex].addWin();
		}
		else if(result == 1){
			_teams[divisionTeamIndex].addLoss();
		}
		else{
			_teams[divisionTeamIndex].addDraw();
		}
		
		return result;
	}
	public void activateProgression(int numDivisions){
		/**
		 * Activates progress for each team in the division
		 */
		for(int i = 0; i < _teams.length; i++){
			_teams[i].progressByRating();
		}
	}
	public Team[] activatePromotionRelegation(boolean top, boolean bottom) throws Exception{	
		/**
		 * This method activates promotion and relegation based on the RPPRules for this division
		 */
		int numToPromote = _rppRules.getNumToPromote();
		int numToRelegate = _rppRules.getNumToRelegate();
		
		if(numToPromote + numToRelegate > _teams.length){
			throw new Exception();
		}
		if(top)
			numToPromote = 0;
		if(bottom)
			numToRelegate = 0;
		
		Team[] teamsToMove = new Team[numToPromote + numToRelegate];
		
		int count = 0;

		sortStandings();

		// Promote teams
		for(int i = 0; i < numToPromote; i++){
			_teams[0].setDivision(_teams[0].getDivision() - 1);
			teamsToMove[count] = _teams[0];
			removeTeam(_teams[0]);
			count++;
		}

		// Relegate teams
		for(int i = 0; i < numToRelegate; i++){
			int index = _teams.length - numToRelegate + i;
			_teams[index].setDivision(_teams[index].getDivision() + 1);
			teamsToMove[count] = _teams[index];
			removeTeam(_teams[index]);
			count++;
		}

		// Test code
		for(int i = 0; i < teamsToMove.length; i++){
			System.out.println(teamsToMove[i].getInfo());
		}
		
		/**
		 * Make sure that the correct number of teams are being promoted/relegated
		 */
		if(top && teamsToMove.length != numToRelegate)
			throw new Exception();
		else if(bottom && teamsToMove.length != numToPromote)
			throw new Exception();
		else if(teamsToMove.length != (numToPromote + numToRelegate))
			throw new Exception();
		
		/**
		 * Make sure that the correct teams are being promoted/relegated
		 */
		for(int i = 0; i < numToPromote; i++){
			for(int j = 0; j < _teams.length; j++){
				if(teamsToMove[i].getWinPercentage() < _teams[j].getWinPercentage())
					throw new Exception();
			}
		}
		for(int i = 0; i < numToRelegate; i++){
			for(int j = 0; j < _teams.length; j++){
				if(teamsToMove[i+numToPromote].getWinPercentage() > _teams[j].getWinPercentage())
					throw new Exception();
			}
		}
				
		return teamsToMove;
	}
	public String getResults(){
		/**
		 * Gets the season results in text
		 */
		return _seasonResults;
	}
	public void printResults(){
		/**
		 * Prints the texual seasonal results of the division
		 */
		sortStandings();
		StringBuffer results = new StringBuffer();
		for(int i = 0; i < _teams.length; i++){
			results.append(_teams[i].getInfo() + "\n");
		}
		results.append("Average Win Percentage: " + getAverageWinPercentage() + "\n");
		results.append("Standard Deviation: " + getStandardDeviationWinPercentage() + "\n");
		results.append("Average Rating: " + getAverageRating() + "\n");
		results.append("Standard Deviation Rating: " + getStandardDeviationRating() + "\n");
		_seasonResults = results.toString();
	}
	public StringBuffer printResultInfo(){
		/**
		 * Returns the seasonal resultsInfo of the division
		 */
		StringBuffer temp = new StringBuffer();
		temp.append(getAverageRating() + "," + getStandardDeviationRating() + ",");
		
		return temp;
	}
	public void wipeRecords(){
		/**
		 * Clears all records of this season
		 */
		for(int i = 0; i < _teams.length; i++){
			_teams[i].wipeRecord();
		}
	}
	public Team findTeam(String teamName){
		/**
		 * Gets the specified team
		 */
		for(int i = 0; i < _teams.length; i++){
			if(teamName.equals(_teams[i].getName())){
				return _teams[i];
			}
		}
		return null;
	}
	
	/**
	 * Private use only
	 */
	private int simulateGame(Team home, Team away){
		/**
		 * This helper method simulates a game between two teams and determines the result
		 * Outcome determination uses logistic regression of actual NBA games
		 * @param home - The home team
		 * @param away - The away team
		 * @return - The result as an integer (0 for home win, 1 for away win, 2 for draw)
		 */
		int gameResult = 0;
		
		double exp = -.011891894 + (0.046047370 * home.getRating()) - (0.045830480 * away.getRating());
		double regEq = Math.exp(-exp);
		double probHome = 1 / (1 + regEq);
		int percHome = (int)(probHome * 100);
		
		Random generator = new Random();
		if(generator.nextInt(100) < percHome){
			gameResult = 0;
		}
		else{
			gameResult = 1;
		}

		return gameResult;
	}
	private double getAverageWinPercentage(){
		/**
		 * Gets the average winning percentage of the division
		 */
		double totalWinPercentage = 0;
		for(int i = 0; i < _teams.length; i++){
			totalWinPercentage += _teams[i].getWinPercentage();
		}
		return totalWinPercentage / _teams.length;
	}
	private double getStandardDeviationWinPercentage(){
		/**
		 * Gets the standard deviation of winning percentage of the division
		 */
		double averageWinPercentage = getAverageWinPercentage();
		double sumOfVariances = 0;
		for(int i = 0; i < _teams.length; i++){
			sumOfVariances += Math.pow(_teams[i].getWinPercentage() - averageWinPercentage, 2);
		}
		return Math.sqrt(sumOfVariances / _teams.length);
	}
	private double getAverageRating(){
		/**
		 * Gets the average rating of the division
		 */
		double totalRating = 0;
		for(int i = 0; i < _teams.length; i++){
			totalRating += _teams[i].getRating();
		}
		return (double)totalRating / _teams.length;
	}
	private double getStandardDeviationRating(){
		/**
		 * Gets the standard deviation of rating of the division
		 */
		double averageRating = getAverageRating();
		double sumOfVariances = 0;
		for(int i = 0; i < _teams.length; i++){
			sumOfVariances += Math.pow((_teams[i].getRating() - averageRating), 2.0);
		}
		return Math.sqrt(sumOfVariances / (double)_teams.length);
	}
	private void sortStandings(){
		/**
		 * Sorts the teams by number of wins
		 */
		for(int i = 0; i < _teams.length; i++){
			int j = i;
			Team B = _teams[i];
			while ((j > 0) && (_teams[j-1].getWins() < B.getWins())){
				_teams[j] = _teams[j-1];
				j--;
			}
			_teams[j] = B;
		}
		for(int i = 0; i < _teams.length; i++){
			_teams[i].setRank(i+1);
		}
	}
	private void sortTeamsByRatings(){
		/**
		 * Sorts the teams by rating
		 */
		for(int i = 0; i < _teams.length; i++){
			int j = i;
			Team B = _teams[i];
			while ((j > 0) && (_teams[j-1].getRating() < B.getRating())){
				_teams[j] = _teams[j-1];
				j--;
			}
			_teams[j] = B;
		}
	}
	
}
