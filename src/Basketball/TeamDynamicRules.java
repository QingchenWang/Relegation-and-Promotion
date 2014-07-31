package Basketball;

public class TeamDynamicRules {
	
	//NEW_PCT = ALPHA * (ACT_PCT - MEAN) + MEAN + RANDOM
	
	private int 			_numYearsInBuild;
	private int				_numYearsInGrowth;
	private int 			_numYearsInPeak;
	private int				_numYearsInDecline;
	private int				_randomVariability;
	private int				_growthRatingChange;
	private int				_buildRatingChange;
	private int				_peakRatingChange;
	private int				_declineRatingChange;
	private int				_divisionEffect;
	
	private int 			_numCategories;
	private double[][]		_percentages;
	private int[][]			_ratingCategories;
	private int				_maxRating;
	private int				_minRating;
	
	public TeamDynamicRules(int numYearsInBuild, int numYearsInGrowth, int numYearsInPeak, int numYearsInDecline, int randomVariability,
			int buildRatingChange, int growthRatingChange, int peakRatingChange, int declineRatingChange, int divisionEffect){
		_numYearsInBuild = numYearsInBuild;
		_numYearsInGrowth = numYearsInGrowth;
		_numYearsInPeak = numYearsInPeak;
		_numYearsInDecline = numYearsInDecline;
		_randomVariability = randomVariability;
		
		_buildRatingChange = buildRatingChange;
		_growthRatingChange = growthRatingChange;
		_peakRatingChange = peakRatingChange;
		_declineRatingChange = declineRatingChange;
		_divisionEffect = divisionEffect;
	}
	
	public int getNumYearsInBuild(){
		return _numYearsInBuild;
	}
	
	public int getNumYearsInGrowth(){
		return _numYearsInGrowth;
	}
	
	public int getNumYearsInPeak(){
		return _numYearsInPeak;
	}
	
	public int getNumYearsInDecline(){
		return _numYearsInDecline;
	}
	
	public int getRandomVariability(){
		return _randomVariability;
	}
	
	public int getBuildRatingChange(){
		return _buildRatingChange;
	}
	
	public int getGrowthRatingChange(){
		return _growthRatingChange;
	}
	
	public int getPeakRatingChange(){
		return _peakRatingChange;
	}
	
	public int getDeclineRatingChange(){
		return _declineRatingChange;
	}
	
	public int getDivisionEffect(){
		return _divisionEffect;
	}
	
	public TeamDynamicRules(int numCategories, double[][] percentages, int[][]ratingCategories, int maxRating, int minRating){
		_numCategories = numCategories;
		_percentages = percentages;
		_ratingCategories = ratingCategories;
		_maxRating = maxRating;
		_minRating = minRating;
	}
	
	public int getNumCategories(){
		return _numCategories;
	}
	
	public double[][] getPercentages(){
		return _percentages;
	}
	
	public int[][] getRatingCategories(){
		return _ratingCategories;
	}
	
	public int getMaxRating(){
		return _maxRating;
	}
	
	public int getMinRating(){
		return _minRating;
	}

}
