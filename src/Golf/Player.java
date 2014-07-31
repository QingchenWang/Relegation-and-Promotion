package Golf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Player {
	
	public static int	_replacedCount = 0;

	private String 		_playerName;
	private int 		_division;
	
	private double		_playingProbability;
	private double		_expectedRank;
	private double 		_rating;
	private double		_stdevRating;
	private int			_age;
	private double 		_A;
	private double		_B;
	private double		_C;
	private double		_expectedCareerBestAge;
	private double		_expectedCareerBestRank;
	private boolean		_fullTime;
	private boolean		_placeholder;
	
	private int			_money;
	private int			_rank;
	private int 		_wins;
	private int			_roundsPlayed;
	private int			_totalScore;
	private double		_averageScore;
	
	private ArrayList<Tournament>	_tournamentsPlayed;

	public Player(String playerName, double expectedRank, double rating, double stdevRating, int age, double A, double B, double C, double playingProbability, boolean fullTime, boolean placeholder){
		_playerName = playerName;
		_expectedRank = expectedRank;
		_rating = rating;
		_stdevRating = stdevRating;
		_age = age;
		_A = A;
		_B = B;
		_C = C;
		_expectedCareerBestAge = -_B / (2*_A);
		_expectedCareerBestRank = _A*Math.pow(_expectedCareerBestAge, 2) + _B*_expectedCareerBestAge + _C;
		_playingProbability = playingProbability;
		_fullTime = fullTime;
		_placeholder = placeholder;
		_money = 0;
		_rank = 0;
		_wins = 0;
		_tournamentsPlayed = new ArrayList<Tournament>();
	}
	
	public Player(String playerName, int division, double expectedRank, double rating, double stdevRating, int age, double A, double B, double C, double playingProbability, boolean fullTime, boolean placeholder){
		this(playerName, expectedRank, rating, stdevRating, age, A, B, C, playingProbability, fullTime, placeholder);
		this._division = division;
	}
	
	public void setName(String playerName){
		_playerName = playerName;
	}
	
	public String getName(){
		return _playerName;
	}
	
	public void setDivision(int division){
		_division = division;
	}
	
	public int getDivision(){
		return _division;
	}
	
	public void setExpectedRank(double expectedRank){
		_expectedRank = expectedRank;
	}
	
	public double getExpectedRank(){
		return _expectedRank;
	}
	
	public void setRating(double rating){
		_rating = rating;
	}
	
	public double getRating(){
		return _rating;
	}
	
	public void setStdevRating(double stdevRating){
		_stdevRating = stdevRating;
	}
	
	public double getStdevRating(){
		return _stdevRating;
	}
	
	public void setAge(int age){
		_age = age;
	}
	
	public int getAge(){
		return _age;
	}
	
	public double getCareerCurveVariableA(){
		return _A;
	}
	
	public void setCareerCurveVariableA(double A){
		_A = A;
	}
	
	public double getCareerCurveVariableB(){
		return _B;
	}
	
	public void setCareerCurveVariableB(double B){
		_B = B;
	}
	
	public double getCareerCurveVariableC(){
		return _C;
	}
	
	public void setCareerCurveVariableC(double C){
		_C = C;
	}
	
	public double getExpectedCareerBestAge(){
		return _expectedCareerBestAge;
	}
	
	public void setExpectedCareerBestAge(double expectedCareerBestAge){
		_expectedCareerBestAge = expectedCareerBestAge;
	}
	
	public double getExpectedCareerBestRank(){
		return _expectedCareerBestRank;
	}
	
	public void setExpectedCareerBestRank(double expectedCareerBestRank){
		_expectedCareerBestRank = expectedCareerBestRank;
	}
	
	public void setPlayingProbability(double playingProbability){
		_playingProbability = playingProbability;
	}

	public double getPlayingProbability(){
		return _playingProbability;
	}
	
	public void setFullTime(boolean fullTime){
		_fullTime = fullTime;
	}
	
	public boolean getFullTime(){
		return _fullTime;
	}
	
	public void setPlaceholder(boolean placeholder){
		_placeholder = placeholder;
	}
	
	public boolean getPlaceholder(){
		return _placeholder;
	}
	
	public void setMoney(int money){
		_money = money;
	}
	
	public void addMoney(int money){
		_money += money;
	}
	
	public int getMoney(){
		return _money;
	}
	
	public void setRank(int rank){
		_rank = rank;
	}
	
	public int getRank(){
		return _rank;
	}
	
	public void addWin(){
		_wins++;
	}
	
	public int getWins(){
		return _wins;
	}
	
	public void addTournamentPlayed(Tournament tournament){
		_tournamentsPlayed.add(tournament);
	}
	
	public ArrayList<Tournament> getTournamentsPlayed(){
		return _tournamentsPlayed;
	}
	
	public void addRoundsPlayed(int numRounds){
		_roundsPlayed += numRounds;
		_averageScore = (double)_totalScore / (double)_roundsPlayed;
	}
	
	public int getRoundsPlayed(){
		return _roundsPlayed;
	}
	
	public void addTotalScore(int score){
		_totalScore += score;
		_averageScore = (double)_totalScore / (double)_roundsPlayed;
	}
	
	public double getAverageScore(){
		return _averageScore;
	}
	
	public String getInfo(){
		StringBuffer info = new StringBuffer();
		info.append(_playerName + ",");
		info.append(_division + ",");
		info.append(_expectedRank + ",");
		info.append(_rating + ",");
		info.append(_stdevRating + ",");
		info.append(_tournamentsPlayed.size() + ",");
		info.append(_wins + ",");
		info.append(_money + ",");
		
		return info.toString();
	}
	
	public void reset(){
		_money = 0;
		_rank = 0;
		_wins = 0;
		_tournamentsPlayed = new ArrayList<Tournament>();
		_roundsPlayed = 0;
		_totalScore = 0;
		_averageScore = 0;
	}
	
	public int generateScore(){
		Random generator = new Random();
		double randomGaussian = generator.nextGaussian();
		double random = _stdevRating*randomGaussian;
		
//		double score = General.Utility.sampleRoundScore(_rating);
		return Math.round((float)(General.Utility.sampleRoundScore(_rating)));
	}
	
	public boolean play(Tournament tournament){
		Random generator = new Random();
		int randomInt = generator.nextInt(100);
		int playingThreshold = (int)(_playingProbability * 100);
		if(randomInt < playingThreshold){
			return true;
		}
		else{
			return false;
		}
	}
	
	/*
	 * The playing probability changes when a player has been promoted or relegated
	 */
	public void promotionRelegationEffect(int previousDivision, int nextDivision){
		// promoted from Nationwide to PGA
		if(previousDivision > nextDivision){
			//TODO: sample a new _playingThreshold
		}
		// relegated from PGA to Nationwide
		else{
			//TODO: sample a new _playingThreshold
		}
	}
	
	public boolean retirement(){
		if(_age >= 55 && _division == 0){
			return true;
		}
		else if(_age >= 50 && _division == 1){
			return true;
		}
		else{
			return false;
		}
	}
	
	public void progress(){
		Random generator = new Random();
//		if(_age >= 30 && _age <= 47){
//			if(generator.nextBoolean())
//				_age = _age + 2;
//			else
//				_age++;
//		}
//		else
			_age++;
//		if(_division == 0)
//			_age = General.Utility.sampleIntFromCDF(General.Utility.convertSampleDistributionToCDFDiscrete(General.Utility._PGAPlayerAgesDistribution));
//		else
//			_age = General.Utility.sampleIntFromCDF(General.Utility.convertSampleDistributionToCDFDiscrete(General.Utility._NationwidePlayerAgesDistribution));
		
		
		
//		double careerCurveRMSE = 0.448535653;
		double careerMoneyRankCurveRMSE = 31.68909266;
//		_rating = General.Utility.quadraticRegSample(_age, _A, _B, _C, careerCurveRMSE);
		_expectedRank = 0;
		int iterations = 0;
		while(!(_expectedRank >= 1 && _expectedRank <= 500)){
			_expectedRank = General.Utility.quadraticRegSample(_age, _A, _B, _C, careerMoneyRankCurveRMSE);
			iterations++;
			if(iterations > 500){
				System.out.println("ERROR!! Bad Career Curve: " + _expectedRank);
				if(_expectedRank < 0)
					_expectedRank = 1;
				else
					_expectedRank = 500;
			}
		}
//		_rating = (66.59496+87.66800*(_expectedRank)+0.01205*Math.pow(_expectedRank, 2))/(1+1.24873*(_expectedRank)) + (generator.nextGaussian()*careerCurveRMSE);
		_rating = Math.exp((4.226485)/(1-(0.008565512)*(Math.log(_expectedRank))+(8.160515E-03)*Math.pow(Math.log(_expectedRank), 2)-(3.838415E-03)*Math.pow(Math.log(_expectedRank), 3)+(7.932228E-04)*Math.pow(Math.log(_expectedRank), 4)-(5.995422E-05)*Math.pow(Math.log(_expectedRank), 5)));
//		if(_rating <= 70)
//			_rating += generator.nextGaussian()*0.35;
//		else
//			_rating += generator.nextGaussian()*0.10;	
		
		// Temporarily use standard deviation to sample player scores
		double stdGeneratingRatingMean = 70.98874174;
		double stdGeneratingRatingStdev = 1.005276;
		double stdGeneratingStdMean = 2.827590179;
		double stdGeneratingStdStdev = 0.489190309;
		_stdevRating = Double.valueOf((new DecimalFormat("#.##")).format(((((_rating-stdGeneratingRatingMean)/stdGeneratingRatingStdev) * 0.175) + (0.9848123 * generator.nextGaussian()) * stdGeneratingStdStdev) + stdGeneratingStdMean));

		if(_fullTime){
			if(_division == 0){
				_playingProbability = (-22.998915123649 - (0.201317971471147*_age) + (0.790749879196105*_rating)) / 45.0;
			}
			else{
				_playingProbability = (88.2401364635201 - (0.196965752117979*_age) - (0.832661124943235*_rating)) / 30.0;
			}
		}
		else{
			_playingProbability = 0.25 + generator.nextGaussian()*0.5;
		}
		
//		Random generator = new Random();
//		double stdRatingPGA = 0.786464;
//		double stdRatingNationwide = 0.772535;
//		double stdGeneratingRatingMean = 70.98874174;
//		double stdGeneratingRatingStdev = 1.005276;
//		double stdGeneratingStdMean = 2.827590179;
//		double stdGeneratingStdStdev = 0.489190309;
//		if(_age < _peakAge){
//			_rating -= Double.valueOf((new DecimalFormat("#.##")).format(0.1 + generator.nextGaussian()*0.15)); 
//		}
//		else if(_age > _peakAge){
//			_rating += Double.valueOf((new DecimalFormat("#.##")).format(0.1 + generator.nextGaussian()*0.15));
//		}
//		else{
//			_rating += Double.valueOf((new DecimalFormat("#.##")).format(generator.nextGaussian()*0.15)); 
//		}
//		_stdevRating = Double.valueOf((new DecimalFormat("#.##")).format(((((_rating-stdGeneratingRatingMean)/stdGeneratingRatingStdev) * 0.175) + (0.9848123 * generator.nextGaussian()) * stdGeneratingStdStdev) + stdGeneratingStdMean));
//		_age++;
//		
//		// replace player with new player if relegated and over the age of 45
//		if(_division == 1 && _age > 45){
//			_playerName = "Replaced Player " + _replacedCount;
//			_replacedCount++;
//    		_rating = Double.valueOf((new DecimalFormat("#.##")).format(72.11377 + (stdRatingNationwide * generator.nextGaussian())));
//			_stdevRating = Double.valueOf((new DecimalFormat("#.##")).format(((((_rating-stdGeneratingRatingMean)/stdGeneratingRatingStdev) * 0.175) + (0.9848123 * generator.nextGaussian()) * stdGeneratingStdStdev) + stdGeneratingStdMean));
//    		_age = (int)(3 * generator.nextGaussian() + 24);
//    		_peakAge = 38;
//    		_money = 0;
//    		_rank = 0;
//    		_wins = 0;
//    		_tournamentsPlayed = new ArrayList<Tournament>();
//    		_fullTime = true;
//		}
		
	}
}
