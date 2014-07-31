package Golf;

import java.util.ArrayList;
import java.util.Collections;

public class EndOfSeasonEvent {
	
	private ArrayList<Player>		_players;
	private ArrayList<Player>		_playersPGA;
	private ArrayList<Player>		_playersNationwide;
	private ArrayList<Player> 		_playersPromoted;
	private ArrayList<Player>		_playersNotPromoted;
	private ArrayList<Tournament>	_tournaments;
	private ArrayList<TournamentResults>	_tournamentResults;
	
	private int						_numPGAFromNationwideAuto;
	private int						_numPGARemained;
	
	private int						_numToPromote;
	
	// format:  1 = no seeding, 2 = static seeding, 3 = dynamic seeding (continuing the season)
	private int						_format;
	
	public EndOfSeasonEvent(ArrayList<Player> playersPGA, ArrayList<Player> playersNationwide, int format, int numToPromote, int numPGAFromNationwideAuto, int numPGARemained){
		_playersPGA = playersPGA;
		_playersNationwide = playersNationwide;

		_format = format;
		_numToPromote = numToPromote;
		_numPGAFromNationwideAuto = numPGAFromNationwideAuto;
		_numPGARemained = numPGARemained;
		_players = new ArrayList<Player>();
		_players.addAll(_playersPGA);
		_players.addAll(_playersNationwide);
		_playersPromoted = new ArrayList<Player>();
		_playersNotPromoted = new ArrayList<Player>();
		_tournaments = new ArrayList<Tournament>();
		_tournamentResults = new ArrayList<TournamentResults>();
		_tournaments.add(new Tournament("EOS Tournament1", 144, 1, 4, 500000, 60));
		_tournaments.add(new Tournament("EOS Tournament2", 144, 1, 4, 500000, 60));
		_tournaments.add(new Tournament("EOS Tournament3", 144, 1, 4, 500000, 60));
		initializeSeeding();
		setAuto();
	}
	
	private void setAuto(){
		sortByMoney(_playersNationwide);
		for(int i = 0; i < _numPGAFromNationwideAuto; i++){
			_playersPromoted.add(_playersNationwide.get(i));
		}
	}
	
	private void initializeSeeding(){
		sortByMoney(_playersPGA);
		sortByMoney(_playersNationwide);
		
		// event without seeding
		if(_format == 1){
			// DO NOTHING because 
		}
		// seeded event
		else if(_format == 2){
			//TODO: figure this out sometime
		}
		// continuing the season
		else if(_format == 3){
			for(int i = 0; i < _playersNationwide.size(); i++){
				_playersNationwide.get(i).addEOSPoints(_playersNationwide.get(i).getMoney());
			}
			for(int i = 0; i < _playersPGA.size(); i++){
				_playersPGA.get(i).addEOSPoints(_playersNationwide.get(_numPGAFromNationwideAuto + i).getEOSPoints());
			}
		}
	}
	
	public void simulateEvent(){
		for(int i = 0; i < _tournaments.size(); i++){

			ArrayList<PlayerResult> playerResults = new ArrayList<PlayerResult>();
			Collections.shuffle(_players);

			/**
			 * Simulate rounds 1 and 2 and sort
			 */
			for(int k = 0; k < _players.size(); k++){
				int r1Score = _players.get(k).generateScore();
				int r2Score = _players.get(k).generateScore();
				playerResults.add(new PlayerResult(_players.get(k), _tournaments.get(i),
						r1Score, r2Score, 0, 0, 0));
			}
			Collections.shuffle(playerResults);
			sortPlayersPreCut(playerResults);

			/**
			 * Simulate rounds 3 and 4 and sort
			 */
			int cut = _tournaments.get(i).getCut() - 1;
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

			_tournamentResults.add(new TournamentResults(_tournaments.get(i).getTournamentName(), playerResults, true));
		}
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
	
	public ArrayList<Player> getPlayersPromoted(){
		sortEOSStandings();
		while(_playersPromoted.size() < _numToPromote){
			if(!_playersPromoted.contains(_players.get(0))){
				_playersPromoted.add(_players.get(0));
			}
			_players.remove(0);
		}
		return _playersPromoted;
	}
	
	public ArrayList<Player> getPlayersNotPromoted(){
		// must be called after getting promoted players
		_playersNotPromoted.addAll(_players);
		return _playersNotPromoted;
	}
	
	private void sortEOSStandings(){
		/**
		 * Sorts the players by earnings
		 */
		for(int i = 0; i < _players.size(); i++){
			int j = i;
			Player B = _players.get(i);
			while ((j > 0) && (_players.get(j-1).getEOSPoints() < B.getEOSPoints())){
				_players.set(j, _players.get(j-1));
				j--;
			}
			_players.set(j, B);
		}
	}
	
	private void sortByMoney(ArrayList<Player> players){
		/**
		 * Sorts the players by earnings
		 */
		for(int i = 0; i < players.size(); i++){
			int j = i;
			Player B = players.get(i);
			while ((j > 0) && (players.get(j-1).getMoney() < B.getMoney())){
				players.set(j, players.get(j-1));
				j--;
			}
			players.set(j, B);
		}
	}

}
