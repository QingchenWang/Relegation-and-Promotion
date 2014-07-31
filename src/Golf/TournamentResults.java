package Golf;

import java.util.ArrayList;

public class TournamentResults {
	
	private String					_tournamentName;
	private ArrayList<PlayerResult>	_results;
	
	public TournamentResults(String tournamentName, ArrayList<PlayerResult>	results){
		_tournamentName = tournamentName;
		_results = results;
		sortResultsByPrize();
		for(int i = 0; i < _results.size(); i++){
			_results.get(i).getPlayer().addTournamentPlayed(_results.get(i).getTournament());
			_results.get(i).getPlayer().addMoney(_results.get(i).getPrize());
			if(i == 0)
				_results.get(i).getPlayer().addWin();
			_results.get(i).getPlayer().addRoundsPlayed(_results.get(i).getRoundsPlayed());
			_results.get(i).getPlayer().addTotalScore(_results.get(i).getTotalScore());
		}
	}
	
	private void sortResultsByPrize(){
		for(int i = 0; i < _results.size(); i++){
			int j = i;
			PlayerResult B = _results.get(i);
			while ((j > 0) && (_results.get(j-1).getPrize() < B.getPrize())){
				_results.set(j, _results.get(j-1));
				j--;
			}
			_results.set(j, B);
		}
	}
	
	public String getTournamentName(){
		return _tournamentName;
	}
	
	public ArrayList<PlayerResult> getResults(){
		return _results;
	}

}
