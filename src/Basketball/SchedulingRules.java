package Basketball;

public class SchedulingRules {
	
	// 3 types of schedules - 1: all games in division  2: most in one division and some in another  3: completely random
	private int 		_type;
	private int 		_numGamesAgainstDiv;
	private int 		_numGamesAgainstOut;
	
	public SchedulingRules(int type, int numGamesAgainstDiv, int numGamesAgainstOut){
		_numGamesAgainstDiv = numGamesAgainstDiv;
		_type = type;
		_numGamesAgainstOut = numGamesAgainstOut;
	}
	
	public int getNumGamesAgainstDiv(){
		return _numGamesAgainstDiv;
	}
	
	public int getType(){
		return _type;
	}
	
	public int getNumGamesAgainstOut(){
		return _numGamesAgainstOut;
	}
}
