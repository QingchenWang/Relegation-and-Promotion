package Golf;

public class Tournament {
	
	private String		_tournamentName;
	private int			_size;
	private int			_numRounds;
	private int			_purse;
	private int			_type;
	private int[]		_prizeListPGA = {900000,540000,340000,240000,200000,180000,167500,155000,145000,135000,
									  125000,115000,105000,95000,90000,85000,80000,75000,70000,65000,
									  60000,56000,52000,48000,44000,40000,38500,37000,35500,34000,
									  32500,31000,29500,28250,27000,25750,24500,24500,22500,21500,
									  20500,19500,18500,17500,16500,15500,14500,13700,13000,12600,
									  12300,12000,11800,11600,11500,11400,11300,11200,11100,11000,
									  10900,10800,10700,10600,10500,10400,10300,10200,10100,10000};
	
	private int[]		_prizeListNationwide = {90000,54000,34000,24000,20000,18000,16750,15500,14500,13500,
												12500,11500,10500,9500,9000,8500,8000,7500,7000,6500,
												6000,5600,5200,4800,4400,4200,4000,3800,3600,3400,
												3300,3200,3100,3000,2900,2800,2700,2600,2500,2400,
												2300,2200,2100,2000,1950,1900,1850,1800,1775,1750,
												1725,1700,1675,1650,1625,1600,1575,1550,1525,1500};
	
	public Tournament(String tournamentName, int size, int type, int numRounds, int purse, int cut){
		_tournamentName = tournamentName;
		_size = size;
		_type = type;
		_numRounds = numRounds;
		_purse = purse;
	}

	public String getTournamentName(){
		return _tournamentName;
	}
	
	public int getSize(){
		return _size;
	}
	
	public int getType(){
		return _type;
	}
	
	public int getNumRounds(){
		return _numRounds;
	}
	
	public int getPurse(){
		return _purse;
	}
	
	public int getPrize(int tieBreakerMin, int tieBreakerMax){
		// includes tie breaking, but not the sudden death playoff for first
		if(tieBreakerMin <= 70 && tieBreakerMax <= 70 && _type == 0){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				sum += (int)(((double)_purse/5000000)*_prizeListPGA[i-1]);
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else if(tieBreakerMin <= 70 && tieBreakerMax > 70 && _type == 0){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				if(i <= 70)
					sum += (int)(((double)_purse/5000000)*_prizeListPGA[i-1]);
				else
					sum += (int)(((double)_purse/5000000)*(_prizeListPGA[70-1]-((i-70)*100)));
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else if(tieBreakerMin > 70 && _type == 0){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				sum += (int)(((double)_purse/5000000)*(_prizeListPGA[70-1]-((i-70)*100)));
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else if(tieBreakerMin <= 60 && tieBreakerMax <= 60 && _type == 1){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				sum += (int)(((double)_purse/500000)*_prizeListNationwide[i-1]);
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else if(tieBreakerMin <= 60 && tieBreakerMax > 60 && _type == 1){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				if(i <= 60)
					sum += (int)(((double)_purse/500000)*_prizeListNationwide[i-1]);
				else
					sum += (int)(((double)_purse/500000)*(_prizeListNationwide[60-1]-((i-60)*10)));
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else if(tieBreakerMin > 60 && _type == 1){
			int sum = 0;
			for(int i = tieBreakerMin; i <= tieBreakerMax; i++){
				sum += (int)(((double)_purse/500000)*(_prizeListNationwide[60-1]-((i-60)*10)));
			}
			return sum / (tieBreakerMax - tieBreakerMin + 1);
		}
		else{
			return 0;
		}
	}
}
