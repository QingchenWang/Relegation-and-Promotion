package Golf;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class QSchool {

	public boolean _old;
	private int _numPGAFromTournament;
	private int _numNationwideNew;
	private int _numPGAFromNationwideAuto;
	private int _numPGARemain;
	private ArrayList<Player> _playersToPromotePGA;
	private ArrayList<Player> _playersToPromoteNationwide;

	private ArrayList<Player> _playersPGADemoted;
	private ArrayList<Player> _playersNationwideDemoted;

	// private ArrayList<Player> _playersAmateur;

	public QSchool(boolean old, int numPGAFromTournament, int numNationwideNew,
			int numPGAFromNationwideAuto, int numPGARemain,
			ArrayList<Player> playersPGADemoted,
			ArrayList<Player> playersNationwideDemoted) {
		_old = old;
		_numPGAFromTournament = numPGAFromTournament;
		_numNationwideNew = numNationwideNew;
		_numPGAFromNationwideAuto = numPGAFromNationwideAuto;
		_numPGARemain = numPGARemain;
		_playersPGADemoted = playersPGADemoted;
		_playersNationwideDemoted = playersNationwideDemoted;

		if (_old) {
			_playersToPromotePGA = new ArrayList<Player>();
		}
		_playersToPromoteNationwide = new ArrayList<Player>();

		// _playersPGADemoted = new ArrayList<Player>();
		// _playersNationwideDemoted = new ArrayList<Player>();
		// _playersAmateur = new ArrayList<Player>();
	}

	public ArrayList<Player> getPromotedPGAPlayers() {
		ArrayList<Player> totalExperiencedPlayers = new ArrayList<Player>();
		totalExperiencedPlayers.addAll(_playersPGADemoted);
		totalExperiencedPlayers.addAll(_playersNationwideDemoted);
		Random generator = new Random();
		int roll = 0;
		int topPGAIndex = _playersPGADemoted.get(0).getRank();
		int topPGAIndexPlus25 = _playersPGADemoted.get(24).getRank();
		int countQPromotePGA = 0;
		int countQPromoteNat = 0;
		boolean playerCounted = false;

		// note that part-time players from the PGA tour will not take
		// membership of the PGA or Nationwide tours
		Collections.shuffle(totalExperiencedPlayers);
		for (int i = 0; i < totalExperiencedPlayers.size(); i++) {
			roll = generator.nextInt(100);
			// made it to PGA if from PGA ranked 126-150 and in lucky 20% and
			// there's space remaining
			if (totalExperiencedPlayers.get(i).getDivision() == 0
					&& totalExperiencedPlayers.get(i).getRank() > topPGAIndex
					&& totalExperiencedPlayers.get(i).getRank() <= (topPGAIndexPlus25)
					&& roll < ((_numPGAFromTournament * 10) / 15)
					&& _playersToPromotePGA.size() < _numPGAFromTournament
					&& !playerCounted) {
				_playersToPromotePGA.add(totalExperiencedPlayers.get(i));
				_playersPGADemoted.remove(totalExperiencedPlayers.get(i));
				countQPromotePGA++;
				playerCounted = true;
			}
			// made it to PGA if from Nationwide ranked 26-40 and in lucky 20%
			// and there's space remaining
			if (totalExperiencedPlayers.get(i).getDivision() == 1
					&& totalExperiencedPlayers.get(i).getRank() > _numPGAFromNationwideAuto
					&& totalExperiencedPlayers.get(i).getRank() <= (_numPGAFromNationwideAuto + 15)
					&& roll < ((_numPGAFromTournament * 10) / 15)
					&& _playersToPromotePGA.size() < _numPGAFromTournament
					&& !playerCounted) {
				_playersToPromotePGA.add(totalExperiencedPlayers.get(i));
				_playersNationwideDemoted.remove(totalExperiencedPlayers.get(i));
				countQPromoteNat++;
				playerCounted = true;
			}
			// made it to PGA if from remainder of PGA and Nationwide full time
			// and in lucky 10% and there's space remaining
			if (roll < ((_numPGAFromTournament * 10) / 30)
					&& _playersToPromotePGA.size() < _numPGAFromTournament
					&& ((totalExperiencedPlayers.get(i).getRank() > (topPGAIndexPlus25) && totalExperiencedPlayers.get(i).getDivision() == 0) || (totalExperiencedPlayers.get(i).getRank() > (_numPGAFromNationwideAuto + 15) && totalExperiencedPlayers.get(i).getDivision() == 1))
					&& totalExperiencedPlayers.get(i).getFullTime()
					&& !playerCounted) {
				_playersToPromotePGA.add(totalExperiencedPlayers.get(i));
				if (totalExperiencedPlayers.get(i).getDivision() == 0){
					_playersPGADemoted.remove(totalExperiencedPlayers.get(i));
					countQPromotePGA++;
				}
				else{
					_playersNationwideDemoted.remove(totalExperiencedPlayers.get(i));
					countQPromoteNat++;
				}
				playerCounted = true;
			}
			if (_playersToPromotePGA.size() == _numPGAFromTournament) {
				break;
			}
			playerCounted = false;
		}
		System.out.println("Q Promote PGA: " + countQPromotePGA);
		System.out.println("Q Promote Nat: " + countQPromoteNat);
		System.out.println("Q Promote New: " + (_numPGAFromTournament - _playersToPromotePGA.size()));
		// finally assign remaining spots to new inexperienced players
		_playersToPromotePGA.addAll(General.Utility.generatePlayers(
				(_numPGAFromTournament - _playersToPromotePGA.size()), false,
				true, true, false));

		return _playersToPromotePGA;
	}

	public ArrayList<Player> getPromotedNationwidePlayers() {
		ArrayList<Player> totalExperiencedPlayers = new ArrayList<Player>();
		totalExperiencedPlayers.addAll(_playersPGADemoted);
		totalExperiencedPlayers.addAll(_playersNationwideDemoted);
		Random generator = new Random();
		int roll = 0;
		int countQPromotePGA = 0;
		int countQPromoteNat = 0;

		Collections.shuffle(totalExperiencedPlayers);
		// first keep the PGA ranked 126-180 and Nationwide ranked 26-60
		for (int i = 0; i < totalExperiencedPlayers.size(); i++) {
			if (totalExperiencedPlayers.get(i).getDivision() == 0
					&& totalExperiencedPlayers.get(i).getRank() > _numPGARemain
					&& totalExperiencedPlayers.get(i).getRank() < 181
					&& (totalExperiencedPlayers.get(i).getFullTime() || totalExperiencedPlayers
							.get(i).getPlaceholder())) {
				_playersToPromoteNationwide.add(totalExperiencedPlayers.get(i));
				_playersPGADemoted.remove(totalExperiencedPlayers.get(i));
				countQPromotePGA++;
			}
			if (totalExperiencedPlayers.get(i).getDivision() == 1
					&& totalExperiencedPlayers.get(i).getRank() > _numPGAFromNationwideAuto
					&& totalExperiencedPlayers.get(i).getRank() < 101
					&& (totalExperiencedPlayers.get(i).getFullTime() || totalExperiencedPlayers
							.get(i).getPlaceholder())) {
				_playersToPromoteNationwide.add(totalExperiencedPlayers.get(i));
				_playersNationwideDemoted.remove(totalExperiencedPlayers.get(i));
				countQPromoteNat++;
			}
		}

		totalExperiencedPlayers = new ArrayList<Player>();
		totalExperiencedPlayers.addAll(_playersPGADemoted);
		totalExperiencedPlayers.addAll(_playersNationwideDemoted);

		// next keep some players from the remainder set of experienced players
		Collections.shuffle(totalExperiencedPlayers);
		for (int i = 0; i < totalExperiencedPlayers.size(); i++) {
			roll = generator.nextInt(100);
			if (roll < 10 && _playersToPromoteNationwide.size() < 140
					&& totalExperiencedPlayers.get(i).getFullTime()) {
				_playersToPromoteNationwide.add(totalExperiencedPlayers.get(i));
				_playersNationwideDemoted.remove(totalExperiencedPlayers.get(i));
				if(totalExperiencedPlayers.get(i).getDivision() == 0)
					countQPromotePGA++;
				else
					countQPromoteNat++;
			}
		}
		System.out.println("Q nat PGA: " + countQPromotePGA);
		System.out.println("Q nat Nat: " + countQPromoteNat);
		System.out.println("Q nat New: " + (140 - _playersToPromoteNationwide.size()));

		// finally create new players for entry into nationwide
		// System.out.println(120 - _playersToPromoteNationwide.size());
		_playersToPromoteNationwide.addAll(General.Utility.generatePlayers(140 - _playersToPromoteNationwide.size(), false, false, true, false));

		return _playersToPromoteNationwide;
	}
}
