/**
 * This class represents a bidding rule assigned to a Bidder at the Bauer
 * Bidding Platform
 */
public class Rule {

	// 1 means a bid at the start price will be placed
	private double bidRatioToStartingBid = 1;

	// item subject
	private String subject;
	private boolean isSubjectRule = false;

	// item artist
	private String artist;
	private boolean isArtistRule = false;

	private double maxSquareInch;
	private boolean isMaxSquareInchRule = false;

	private double minSquareInch;
	private boolean isMinSquareInchRule = false;

	private double equalSquareInch;
	private boolean isEqualSquareInchRule = false;

	// constructor
	public Rule() {
		// initially when a rule is created it has not yet been applied
	}

	// Setters
	// increases the current bidRatioToStartingBid by bidRatio
	public void increaseBidRatioToStartingBid(double bidRatio) {
		this.bidRatioToStartingBid = this.bidRatioToStartingBid * bidRatio;
	}

	public void setSubject(String subject) {
		this.subject = subject;
		this.isSubjectRule = true;
	}

	public void setArtist(String artist) {
		this.artist = artist;
		this.isArtistRule = true;
	}

	public void setMaxSquareInch(double squareInch) {
		if (squareInch >= 0) {
			this.maxSquareInch = squareInch;
			isMaxSquareInchRule = true;
		} else
			throw new IllegalArgumentException("Square Inch dimentions value cannot be negative");
	}

	public void setMinSquareInch(double squareInch) {
		if (squareInch >= 0) {
			this.minSquareInch = squareInch;
			isMinSquareInchRule = true;
		} else
			throw new IllegalArgumentException("Square Inch dimentions value cannot be negative");
	}

	public void setEqualSquareInch(double squareInch) {
		if (squareInch >= 0) {
			this.equalSquareInch = squareInch;
			isEqualSquareInchRule = true;
		} else
			throw new IllegalArgumentException("Square Inch dimentions value cannot be negative");
	}

	// Getters
	public boolean isArtistRule() {
		return isArtistRule;
	}

	public boolean isSubjectRule() {
		return isSubjectRule;
	}

	public boolean isMaxSquareInchRule() {
		return isMaxSquareInchRule;
	}

	public boolean isMinSquareInchRule() {
		return isMinSquareInchRule;
	}

	public boolean isEqualSquareInchRule() {
		return isEqualSquareInchRule;
	}

	public String getArtist() {
		return artist;
	}

	public String getSubject() {
		return subject;
	}

	public double getBidRatioToStartingBid() {
		return bidRatioToStartingBid;
	}

	public double getMaxSquareInch() {
		return maxSquareInch;
	}

	public double getMinSquareInch() {
		return minSquareInch;
	}

	public double getEqualSquareInch() {
		return equalSquareInch;
	}
}
