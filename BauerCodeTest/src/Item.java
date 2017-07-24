/**
 * This class represents an item to be auctioned at the Bauer Bidding Platform
 */
public class Item {

	private String title;
	private String type;
	private String subject;
	private double startingBid;
	private double highestBidRatioToStartingBid;
	private Bidder highestBidder;
	private double currentBidRatioToStartingBid;
	private Bidder currentBidder;
	// 1st, 2nd and 3rd entries in array represent Length, Width and Depth
	// respectively
	private double[] dimensionLengthWidthDepth = new double[3];
	// item artist/ origin/ manufacturer name brand
	private String artist;

	// constructor
	public Item(String title, String type, String subject, double startingBid, String size, String artist) {
		this.title = title;
		this.type = type;
		this.subject = subject;
		this.startingBid = startingBid;
		setSizeDimensions(size);
		this.artist = artist;
	}

	// Setters
	public void setSizeDimensions(String size) {
		String[] sizeArr = size.split("x");
		for (int i = 0; i < sizeArr.length; i++) {
			double dim = Double.parseDouble(sizeArr[i].replace(",", "").replaceAll("[^\\d.-]", ""));
			if (dim >= 0) { // a dimension can only be a positive value
				dimensionLengthWidthDepth[i] = dim;
			}
		}
	}

	public void accumulateHighestBidRatioToStartingBid(double ratioToStartingBid) {
		this.highestBidRatioToStartingBid = ratioToStartingBid;
	}

	public void setHighestBidder(Bidder bidder) {
		this.highestBidder = bidder;
	}

	public void accumulateCurrentBidRatioToStartingBid(double ratioToStartingBid) {
		this.currentBidRatioToStartingBid = currentBidRatioToStartingBid * ratioToStartingBid;
	}

	public void initializeCurrentBid() {
		this.currentBidRatioToStartingBid = 1;
		this.currentBidder = null;
	}

	public void setCurrentBidRatio(double ratio) {
		this.currentBidRatioToStartingBid = 1;
	}

	public void setCurrentBidder(Bidder bidder) {
		this.currentBidder = bidder;
	}

	public void setDimensionLength(double length) {
		this.dimensionLengthWidthDepth[0] = length;
	}

	public void setDimensionWidth(double width) {
		this.dimensionLengthWidthDepth[1] = width;
	}

	public void setDimensionDepth(double depth) {
		this.dimensionLengthWidthDepth[2] = depth;
	}

	// Getters
	public String getTitle() {
		return title;
	}

	public String getType() {
		return type;
	}

	public String getSubject() {
		return subject;
	}

	public double getStartingBid() {
		return startingBid;
	}

	public double getHighestBidRatioToStartingBid() {
		return highestBidRatioToStartingBid;
	}

	public Bidder getHighestBidder() {
		return highestBidder;
	}

	public double getCurrentBidRatioToStartingBid() {
		return currentBidRatioToStartingBid;
	}

	public Bidder getCurrentBidder() {
		return currentBidder;
	}

	public double getDimensionLength() {
		return dimensionLengthWidthDepth[0];
	}

	public double getDimensionWidth() {
		return dimensionLengthWidthDepth[1];
	}

	public double getDimensionDepth() {
		return dimensionLengthWidthDepth[2];
	}

	public double getSquareInch() {
		double l = dimensionLengthWidthDepth[0];
		double w = dimensionLengthWidthDepth[1];
		double d = dimensionLengthWidthDepth[2];
		// the Item builder ensures only positive or 0 values for the dimensions
		// if l or w == 0, the squareInch is 0 by default
		// If depth == 0 the squareInch is not 0 by default cause this could be
		// a flat
		// shape
		if (d > 0) {
			return l * w * d;
		} else {
			return l * w;
		}
	}

	public String getArtist() {
		return artist;
	}

}