/*
 * 
 * Author Tooryanand Seetohul with partner Arun Maganti
 * 
 */


public class CharFreq {
	public Character Character;
	public Integer Frequency;
	
	public CharFreq(Character a, Integer b) {
		this.Character = a;
		this.Frequency = b;
	}
	
	public Character getCharacter() {
		return Character;
	}
	
	public Integer getFrequency() {
		return Frequency;
	}
	
	@Override
	public String toString() {
		return "  " + Character  + "  "+ Frequency;
	}
}
