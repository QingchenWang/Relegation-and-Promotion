package Basketball;

public class RPPRules {
	
	private int 	_numToPromote;
	private int		_numToRelegate;
	
	public RPPRules(int numToPromote, int numToRelegate){
		_numToPromote = numToPromote;
		_numToRelegate = numToRelegate;
	}
	
	public int getNumToPromote(){
		return _numToPromote;
	}
	
	public int getNumToRelegate(){
		return _numToRelegate;
	}

}
