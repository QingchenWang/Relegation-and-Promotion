package Golf;

public class PlayerResult {
	
	private Player		_player;
	private Tournament	_tournament;
	private int			_roundOneFinish;
	private int			_roundTwoFinish;
	private int			_roundThreeFinish;
	private int			_roundFourFinish;
	private int			_tournamentFinish;
	private int			_prize;
	private int			_tournamentFinishRank;
	private int			_roundsPlayed;
	private int			_totalScore;
	
	public PlayerResult(Player player, Tournament tournament, int r1Finish, 
			int r2Finish, int r3Finish, int r4Finish, int prize){
		_player = player;
		_tournament = tournament;
		_roundOneFinish = r1Finish;
		_roundTwoFinish = r2Finish;
		_roundThreeFinish = r3Finish;
		_roundFourFinish = r4Finish;
		_prize = prize;
		_roundsPlayed = 0;
		_totalScore = 0;
		if(_roundOneFinish > 50){
			_roundsPlayed++;
			_totalScore += _roundOneFinish;
		}
		if(_roundTwoFinish > 50){
			_roundsPlayed++;
			_totalScore += _roundTwoFinish;
		}
		if(_roundThreeFinish > 50){
			_roundsPlayed++;
			_totalScore += _roundThreeFinish;
		}
		if(_roundFourFinish > 50){
			_roundsPlayed++;
			_totalScore += _roundFourFinish;
		}
	}
	
	public void setRoundOne(int r1){
		_roundOneFinish = r1;
	}
	public void setRoundTwo(int r2){
		_roundTwoFinish = r2;
	}
	public void setRoundThree(int r3){
		_roundThreeFinish = r3;
	}
	public void setRoundFour(int r4){
		_roundFourFinish = r4;
	}
	public void setTournamentFinish(int tournamentFinish){
		_tournamentFinish = tournamentFinish;
	}
	public void setPrize(int prize){
		_prize = prize;
	}
	public void setTournamentFinishRank(int tournamentFinishRank){
		_tournamentFinishRank = tournamentFinishRank;
	}
	public int getRoundOne(){
		return _roundOneFinish;
	}
	public int getRoundTwo(){
		return _roundTwoFinish;
	}
	public int getRoundThree(){
		return _roundThreeFinish;
	}
	public int getRoundFour(){
		return _roundFourFinish;
	}
	public int getTournamentFinish(){
		return _tournamentFinish;
	}
	public int getPrize(){
		return _prize;
	}
	public int getTournamentFinishRank(){
		return _tournamentFinishRank;
	}
	public Player getPlayer(){
		return _player;
	}
	public Tournament getTournament(){
		return _tournament;
	}
	public int getRoundsPlayed(){
		return _roundsPlayed;
	}
	public int getTotalScore(){
		return _totalScore;
	}
}
