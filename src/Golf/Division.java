package Golf;

import java.util.ArrayList;
import java.util.Collections;

import General.Utility;

public class Division {
	
	private ArrayList<Player> 				_players;
	private ArrayList<Player>				_members;
	private ArrayList<Player>				_nonMembers;
	private ArrayList<Player>				_placeholderPlayers;
	private RPPRules						_rppRules;
	private ArrayList<Tournament> 			_tournaments;
	private String							_tour;
		
	private int 							_numPartTimePlayers;
	
	private ArrayList<TournamentResults>	_tournamentResults;
	//private String				_seasonResults;
		
	/**
	 * Constructor
	 */
	public Division(RPPRules rppRules, ArrayList<Tournament> tournaments, String tour, int numPartTimePlayers){
		_players = new ArrayList<Player>();
		_members = new ArrayList<Player>();
		_nonMembers = new ArrayList<Player>();
		_placeholderPlayers = new ArrayList<Player>();
		_rppRules = rppRules;
		_tournaments = tournaments;
		_tournamentResults = new ArrayList<TournamentResults>();
		_tour = tour;
		_numPartTimePlayers = numPartTimePlayers;
	}
	
	/**
	 * Add/remove teams
	 */
	public void addPlayer(Player player){
		/**
		 * Adds the specified player into the division
		 * Used to initially create the divisions and also during promotion/relegation
		 */
		_players.add(player);
	}
	
	public void setPlayers(ArrayList<Player> players){
		_players = players;
	}
	
	private void removePlayer(Player player){
		/**
		 * Removes the specified team from the division
		 * Used during promotion/relegation to remove the promote/relegated team
		 */
		_players.remove(player);
	}
	
	/**
	 * Public use only
	 */
	public Player getMember(int index){
		/**
		 * Gets the x-th team of the division
		 */
		return _members.get(index);
	}
	public ArrayList<Player> getMembers(){
		/**
		 * Gets all of the teams in the division
		 */
		return _members;
	}
	public int getNumMembers(){
		/**
		 * Gets the number of teams in the division
		 */
		return _members.size();
	}
	
	public void simulateSeason(){
		/**
		 * Simulates each tournament
		 */
		_members = new ArrayList<Player>();
		_nonMembers = new ArrayList<Player>();
		_placeholderPlayers = new ArrayList<Player>();
		boolean PGA = false;
		int division = 1;
		if("PGA".equals(_tour)){
			PGA = true;
			division = 0;
		}
		for(int i = 0; i < _players.size(); i++){
			_members.add(_players.get(i));
		}
		ArrayList<Player> partTimePlayers = Utility.generatePlayers(_numPartTimePlayers, false, PGA, false, false);
		for(int i = 0; i < partTimePlayers.size(); i++){
			partTimePlayers.get(i).setDivision(division);
		}
		_players.addAll(partTimePlayers);
		_nonMembers.addAll(partTimePlayers);
		
		_placeholderPlayers = Utility.generatePlayers(500, false, PGA, false, true);
		_players.addAll(_placeholderPlayers);
		
		_tournamentResults = new ArrayList<TournamentResults>();
		for(int i = 0; i < _tournaments.size(); i++){
			//Assume that all tournaments are 4 round stroke play with 18 holes per round and cuts made after 2 rounds.
			ArrayList<PlayerResult> playerResults = new ArrayList<PlayerResult>();
			ArrayList<Player> enteringPlayers = new ArrayList<Player>();
			Collections.shuffle(_members);
			Collections.shuffle(_nonMembers);
			for(int j = 0; j < _members.size(); j++){
				if(_tournaments.get(i).getSize() > enteringPlayers.size() && _members.get(j).play(_tournaments.get(i))){
					enteringPlayers.add(_members.get(j));
				}
			}
			for(int j = 0; j < _nonMembers.size(); j++){
				if(_tournaments.get(i).getSize() > enteringPlayers.size() && _nonMembers.get(j).play(_tournaments.get(i))){
					enteringPlayers.add(_nonMembers.get(j));
				}
			}
			if(enteringPlayers.size() < _tournaments.get(i).getSize()){
				Collections.shuffle(_placeholderPlayers);
				for(int j = enteringPlayers.size(); j < _tournaments.get(i).getSize(); j++){
					enteringPlayers.add(_placeholderPlayers.get(j));
				}
			}
			
			/**
			 * Simulate rounds 1 and 2 and sort
			 */
			for(int k = 0; k < enteringPlayers.size(); k++){
				int r1Score = enteringPlayers.get(k).generateScore();
				int r2Score = enteringPlayers.get(k).generateScore();
				playerResults.add(new PlayerResult(enteringPlayers.get(k), _tournaments.get(i),
						r1Score, r2Score, 0, 0, 0));
			}
			Collections.shuffle(playerResults);
			sortPlayersPreCut(playerResults);
			
			/**
			 * Simulate rounds 3 and 4 and sort
			 */
			int cut = 0;
			if("PGA".equals(_tour))
				cut = 69;
			else
				cut = 59;
			int offset = 1;
			while(playerResults.get(cut).getRoundOne() + playerResults.get(cut).getRoundTwo() == playerResults.get(cut+offset).getRoundOne() + playerResults.get(cut+offset).getRoundTwo()){
				cut++;
			}
			
			//PGA cut = best 70 and ties; Nationwide cut = best 60 and ties.
			for(int k = 0; k <= cut; k++){
				int r3Score = playerResults.get(k).getPlayer().generateScore();
				int r4Score = playerResults.get(k).getPlayer().generateScore();
				playerResults.get(k).setRoundThree(r3Score);
				playerResults.get(k).setRoundFour(r4Score);
			}
			sortPlayersPostCut(playerResults, cut);

			for(int k = 0; k < playerResults.size(); k++){
				playerResults.get(k).setTournamentFinish(playerResults.get(k).getRoundOne() + playerResults.get(k).getRoundTwo()
						+ playerResults.get(k).getRoundThree() + playerResults.get(k).getRoundFour());
			}
			
			/**
			 * Assign prizes
			 */
			int tieBreakerMin = -1;
			int tieBreakerMax = -1;
			playerResults.get(0).setPrize(_tournaments.get(i).getPrize(1, 1));
			playerResults.get(0).setTournamentFinishRank(1);
			for(int k = 1; k <= cut; k++){
				//TODO: Consider tie-breakers
				// starts the group of ties
				if(playerResults.get(k).getTournamentFinish() == playerResults.get(k+1).getTournamentFinish() && tieBreakerMin == -1){
					tieBreakerMin = k;
				}
				// ending the group of ties
				else if(playerResults.get(k).getTournamentFinish() != playerResults.get(k+1).getTournamentFinish() && tieBreakerMin != -1){
					tieBreakerMax = k;
					// sets prize for all players in the group of ties
					for(int l = tieBreakerMin; l <= tieBreakerMax; l++){
						playerResults.get(l).setPrize(_tournaments.get(i).getPrize(tieBreakerMin+1, tieBreakerMax+1));
						playerResults.get(l).setTournamentFinishRank(tieBreakerMin+1);
					}
					tieBreakerMin = -1;
				}
				// continuing the group of ties
				else if(playerResults.get(k).getTournamentFinish() == playerResults.get(k+1).getTournamentFinish() && tieBreakerMin != -1){
					// do nothing
				}
				// single position finish
				else if(playerResults.get(k).getTournamentFinish() != playerResults.get(k+1).getTournamentFinish() && tieBreakerMin == -1){
					playerResults.get(k).setPrize(_tournaments.get(i).getPrize(k+1, k+1));
					playerResults.get(k).setTournamentFinishRank(k+1);
				}
			}
			
			_tournamentResults.add(new TournamentResults(_tournaments.get(i).getTournamentName(), playerResults));
			//TODO: Handle seasonal results for players
		}
		int index = 0;
		while(index < _players.size()){
			if(_players.get(index).getTournamentsPlayed().size() <= 0){
				_players.remove(index);
			}
			else{
				index++;
			}
		}
		sortStandings();
	}
	
	private void sortPlayersPreCut(ArrayList<PlayerResult> playerResults){
		/**
		 * Sorts the player results after rounds 1 and 2
		 */
		for(int i = 0; i < playerResults.size(); i++){
			int j = i;
			PlayerResult B = playerResults.get(i);
			while ((j > 0) && (playerResults.get(j-1).getRoundOne() + playerResults.get(j-1).getRoundTwo() > B.getRoundOne() + B.getRoundTwo())){
				playerResults.set(j, playerResults.get(j-1));
				j--;
			}
			playerResults.set(j, B);
		}
	}
	private void sortPlayersPostCut(ArrayList<PlayerResult> playerResults, int cut){
		/**
		 * Sorts the player results after rounds 1 and 2
		 */
		for(int i = 0; i <= cut; i++){
			int j = i;
			PlayerResult B = playerResults.get(i);
			while ((j > 0) && (playerResults.get(j-1).getRoundOne() + playerResults.get(j-1).getRoundTwo() + playerResults.get(j-1).getRoundThree() + playerResults.get(j-1).getRoundFour() > 
					B.getRoundOne() + B.getRoundTwo() + B.getRoundThree() + B.getRoundFour())){
				playerResults.set(j, playerResults.get(j-1));
				j--;
			}
			playerResults.set(j, B);
		}
	}
	
	public void reset(){
		for(int i = 0; i < _players.size(); i++){
			_players.get(i).reset();
		}
	}

	public ArrayList<ArrayList<Player>> activatePromotionRelegation(boolean top, boolean bottom) throws Exception{	
		/**
		 * This method activates promotion and relegation based on the RPPRules for this division
		 */
		
		// gets the specified numbers from the relegation and promotion rules
		ArrayList<ArrayList<Player>> outputList = new ArrayList<ArrayList<Player>>();
		int numPGARemain = _rppRules.getNumPGARemain();
		int numPGAFromNationwideAuto = _rppRules.getNumPGAFromNationwideAuto();
		int numPGAFromTournament = _rppRules.getNumPGAFromTournament();
		int numNationwideNew = _rppRules.getNumNationwideNew();
		
		if(numPGAFromNationwideAuto + numPGAFromTournament + numNationwideNew > _members.size()){
			throw new Exception();
		}
		
		// split the division into two groups based on the specified numbers: players to keep and players to demote
		ArrayList<Player> playersToKeep = new ArrayList<Player>();
		ArrayList<Player> playersToDemote = new ArrayList<Player>();
		sortStandings();
		
		// top represents PGA, which doesn't promote
		if(top){
			// the top x number of players in PGA remain
			// note that the top part-time players have no interest in joining the PGA tour as a full-time member (players of European tour)
			int count = 0;
			int index = 0;
			while(count < numPGARemain){
				if(_players.get(index).getFullTime() || _players.get(index).getPlaceholder()){
					playersToKeep.add(_players.get(index));
					count++;
				}
				index++;
			}
			// the rest are demoted
			// again note that part-time players are uninterested in either the Nationwide tour or Q-School
			for(int i = index; i < _players.size(); i++){
				if(_players.get(i).getFullTime() || _players.get(i).getPlaceholder()){
					playersToDemote.add(_players.get(i));
				}
			}
			outputList.add(playersToKeep);
			outputList.add(playersToDemote);
		}
		// bottom represents Nationwide, which doesn't have to worry about demoting due to how the code is designed
		// note that part-time players have no interest in staying on the Nationwide tour as a full-time member
		else{
			outputList.add(new ArrayList<Player>());
			outputList.add(_players);
		}
				
		return outputList;
	}

	private void sortStandings(){
		/**
		 * Sorts the players by earnings
		 */
		for(int i = 0; i < _players.size(); i++){
			int j = i;
			Player B = _players.get(i);
			while ((j > 0) && (_players.get(j-1).getMoney() < B.getMoney())){
				_players.set(j, _players.get(j-1));
				j--;
			}
			_players.set(j, B);
		}
		for(int i = 0; i < _players.size(); i++){
			_players.get(i).setRank(i+1);
		}
	}
	
	public void activateProgression(){
		for(int i = 0; i < _players.size(); i++){
//			if(_players.get(i).getDivision() != 0 || !_players.get(i).getFullTime())
				_players.get(i).progress();
//			else
//				_players.set(i, General.Utility.generatePlayers(1, true, true, true, false).get(0));
		}
	}
	
	public int retirement(){
		int count = 0;
		int i = 0;
		while(i < _players.size()){
			if(_players.get(i).retirement()){
				count++;
				_players.remove(i);
			}
			else{
				i++;
			}

		}
		return count;
	}
	
	public StringBuffer printResultInfo(){
		/**
		 * Returns the seasonal resultsInfo of the division
		 */
		StringBuffer temp = new StringBuffer();
		temp.append(getAverageRating() + "," + getStandardDeviationRating() + ",");
		
		return temp;
	}
	
	public StringBuffer printSeasonalResults(){
		/**
		 * Returns the seasonalResults of the division
		 */
		sortStandings();
		StringBuffer temp = new StringBuffer();
		for(int i = 0; i < _players.size(); i++){
			temp.append(_players.get(i).getName() + "," + (i+1) + "," + _players.get(i).getAge() + "," + _players.get(i).getExpectedRank() + "," + 
					_players.get(i).getRating() + "," + _players.get(i).getTournamentsPlayed().size() + "," +
					_players.get(i).getWins() + "," + _players.get(i).getMoney() + "," + _players.get(i).getAverageScore() + "," +
					_players.get(i).getCareerCurveVariableA() + "," + _players.get(i).getCareerCurveVariableB() + "," + _players.get(i).getCareerCurveVariableC() + "," + _players.get(i).getExpectedCareerBestRank() + "\n");
		}
		return temp;
	}
	
	public ArrayList<TournamentResults> getTournamentResults(){
		return _tournamentResults;
	}
	
	private double getAverageRating(){
		/**
		 * Gets the average rating of the division
		 */
		double totalRating = 0;
		for(int i = 0; i < _members.size(); i++){
			totalRating += _members.get(i).getRating();
		}
		return (double)totalRating / _members.size();
	}
	private double getStandardDeviationRating(){
		/**
		 * Gets the standard deviation of rating of the division
		 */
		double averageRating = getAverageRating();
		double sumOfVariances = 0;
		for(int i = 0; i < _members.size(); i++){
			sumOfVariances += Math.pow((_members.get(i).getRating() - averageRating), 2.0);
		}
		return Math.sqrt(sumOfVariances / (double)_members.size());
	}
}
