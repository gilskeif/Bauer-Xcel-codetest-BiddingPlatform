import java.util.ArrayList;

/**
 * This class represents an bidder participating at the Bauer Bidding Platform
 */

public class Bidder {

	private String fullName;
	private double totalMoney = 0;
	// no bid will be placed over this amount
	private double bidLimit;
	private boolean isBidLimit = false;
	private ArrayList<Rule> rules;

	public Bidder(String fullName, double totalMoney) {
		this.fullName = fullName;
		if (totalMoney >= 0) {
			this.totalMoney = totalMoney;
		}
		// a bidder is not necessarily required to place any bids, hence does
		// not have t have any rules
		this.rules = new ArrayList<Rule>();
	}

	// Setters
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public void setRules(ArrayList<Rule> rules) {
		this.rules = rules;
	}

	public void setBidLimit(double bidLimit) {
		if (bidLimit >= 0) {
			this.bidLimit = bidLimit;
			this.isBidLimit = true;
		} else
			throw new IllegalArgumentException("Maximal Bid value cannot be negative");
	}

	public void addRule(Rule rule) {
		this.rules.add(rule);
	}

	// Getters
	public String getFullName() {
		return fullName;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public ArrayList<Rule> getRules() {
		return rules;
	}

	public boolean isBidLimit() {
		return isBidLimit;
	}

	public double getBidLimit() {
		return bidLimit;
	}
}
