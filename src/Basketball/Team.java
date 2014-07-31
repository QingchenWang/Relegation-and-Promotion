package Basketball;
import java.util.Random;

import General.UserInterface;

public class Team {


	private String 		_teamName;
	private int 		_division;
	private int 		_rating;
	private int 		_wins;
	private int 		_losses;
	private int			_rank;
	
	private double		_mean;
	private double		_alpha;
	private double 		_RMSE;
	
	public static StringBuffer test = new StringBuffer();
		
	public Team(String teamName, int division, int rating, int lifeCycle, int numYearsInCycle){
		_teamName = teamName;
		_division = division;
		_rating = rating;
		_rank = 0;
	}
	
	public Team(String teamName, int division, int rating, double mean, double alpha, double RMSE){
		_teamName = teamName;
		_division = division;
		_rating = rating;
		_mean = mean;
		_alpha = alpha;
		_RMSE = RMSE;
		_rank = 0;
	}
	
	public void setName(String teamName){
		_teamName = teamName;
	}
	
	public String getName(){
		return _teamName;
	}
	
	public void setDivision(int division){
		_division = division;
	}
	
	public int getDivision(){
		return _division;
	}
	
	public void setRating(int rating){
		_rating = rating;
	}
	
	public int getRating(){
		return _rating;
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
	
	public void addLoss(){
		_losses++;
	}
	
	public void addDraw(){
		// add a draw
	}
	
	public void wipeRecord(){
		_wins = 0;
		_losses = 0;
	}
	
	public int getWins(){
		return _wins;
	}
	
	public double getWinPercentage(){
		return (double)((double)_wins/(_wins + _losses));
	}
	
	public String getInfo(){
		StringBuffer info = new StringBuffer();
		info.append(_teamName + ",");
		info.append(_division + ",");
		info.append(_rating + ",");
		info.append(_wins + ",");
		info.append(_losses + ",");
		
		return info.toString();
	}
	
	//NEW_PCT = ALPHA * (ACT_PCT - MEAN) + MEAN + RANDOM
	public void progressByRating(){
		//N(mean, variance) = mean + standard deviation * Z
		
		Random generator = new Random();
		double randomGaussian = generator.nextGaussian();
		double random = _RMSE * randomGaussian;
		//double random = _RMSE * generator.nextGaussian() * .5;
		//double random = _RMSE * generator.nextGaussian() * 2;
		test.append(Double.toString(randomGaussian) + "\n");

		double rating = (double)_rating/100;
		_rating = (int)(((_alpha * (rating - _mean)) + _mean + random) * 100);
		
		while(_rating > 90 || _rating < 10){
			if(UserInterface.TEST_MODE){
				//System.out.println("Regenerating Random Activated!!");
				//System.out.println(_teamName + ", Random = " + random + ", Rating = " + _rating);
			}
			randomGaussian = generator.nextGaussian();
			random = _RMSE * randomGaussian;
			//random = _RMSE * generator.nextGaussian() * .5;
			//random = _RMSE * generator.nextGaussian() * 2;
			test.append(Double.toString(randomGaussian) + "\n");

			_rating = (int)(((_alpha * (rating - _mean)) + _mean + random) * 100);
		}
	}
	
	public void progressByWinP(){
		Random generator = new Random();
		double random = _RMSE * generator.nextGaussian();
		double winPercentage = getWinPercentage();
		_rating = (int)(((_alpha * (winPercentage - _mean)) + _mean + random) * 100);
		if(_rating > 99){
			_rating = 99;
		}
		if(_rating < 1){
			_rating = 1;
		}
	}
	
	public void progressByRatingAndWinP(){
		Random generator = new Random();
		double random = _RMSE * generator.nextGaussian();
		double average = (((double)_rating/100) + getWinPercentage()) / 2;
		_rating = (int)(((_alpha * (average - _mean)) + _mean + random) * 100);
		if(_rating > 99){
			_rating = 99;
		}
		if(_rating < 1){
			_rating = 1;
		}
	}
}
