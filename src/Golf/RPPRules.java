package Golf;

public class RPPRules {
	
	private int		_numPGARemain;
	private int		_numPGAFromNationwideAuto;
	private int		_numPGAFromTournament;
	private int		_numNationwideNew;
	
	public RPPRules(int numPGARemain, int numPGAFromNationwideAuto, int numPGAFromTournament, int numNationwideNew){
		_numPGARemain = numPGARemain;
		_numPGAFromNationwideAuto = numPGAFromNationwideAuto;
		_numPGAFromTournament = numPGAFromTournament;
		_numNationwideNew = numNationwideNew;
	}
	
	public int getNumPGARemain(){
		return _numPGARemain;
	}
	
	public int getNumPGAFromNationwideAuto(){
		return _numPGAFromNationwideAuto;
	}
	
	public int getNumPGAFromTournament(){
		return _numPGAFromTournament;
	}
	
	public int getNumNationwideNew(){
		return _numNationwideNew;
	}
}
