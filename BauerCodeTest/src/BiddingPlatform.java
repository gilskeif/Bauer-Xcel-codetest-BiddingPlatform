import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * This class represents the Bauer Bidding Platform
 */
public class BiddingPlatform {

	private static final Logger LOGGER = Logger.getLogger(BiddingPlatform.class.getName());
	static String workspacePath = System.getProperty("user.dir");
	static String jsonFilePath = workspacePath + "\\src\\";
	static String biddersFileName = "bidders.json";
	static String itemsFileName = "items.json";
	static String winnersFileName = "winning_bids.json";
	private HashMap<String, Bidder> bidders = new HashMap<String, Bidder>();
	private ArrayList<Item> items = new ArrayList<Item>();

	// constructor
	public BiddingPlatform() {
	}

	/**
	 * @param none
	 *            will read the input files (JSON), and create instances of
	 *            Bidders and Items accordingly.
	 */
	public void readBiddersFromJSON() throws FileNotFoundException {
		try {
			JsonArray jsonTree = extractJsonTreeFromFile(jsonFilePath + biddersFileName, "bidders");
			for (Object node : jsonTree) {
				String name = ((JsonObject) node).get("name").getAsString();
				String moneyStr = ((JsonObject) node).get("money").getAsString();
				double money = convertCurrencyToDouble(moneyStr);
				Bidder bidder = new Bidder(name, money);
				bidders.put(name, bidder);
			}
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
			ex.printStackTrace();
		}
	}

	/**
	 * @param none
	 *            will read the input files (JSON), and create instances of
	 *            Bidders, Items and Rules accordingly.
	 */
	public void readItemsFromJSON() throws FileNotFoundException {
		try {
			JsonArray jsonTree = extractJsonTreeFromFile(jsonFilePath + itemsFileName, "items");
			for (Object node : jsonTree) {
				String title = ((JsonObject) node).get("title").getAsString();
				String type = ((JsonObject) node).get("type").getAsString();
				String subject = ((JsonObject) node).get("subject").getAsString();
				String startingBidStr = ((JsonObject) node).get("startingBid").getAsString();
				double startingBid = convertCurrencyToDouble(startingBidStr);
				String size = ((JsonObject) node).get("size").getAsString();
				String artist = ((JsonObject) node).get("artist").getAsString();
				Item item = new Item(title, type, subject, startingBid, size, artist);
				items.add(item);
			}
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
			ex.printStackTrace();
		}
	}

	/**
	 * @param none
	 *            will execute a simple logic to create rules, base on the
	 *            specifications as described in the project requirements wiki,
	 *            and will then associate those to the relevant Bidders from the
	 *            bidders.JSON file.
	 */
	public void loadRulesPerBidders() {
		// Set rule for Bob Briskey:
		// will bid double for paintings where the subject is horses.
		Rule rule1 = new Rule();
		rule1.setSubject("horses");
		rule1.increaseBidRatioToStartingBid(2);
		bidders.get("Bob Briskey").addRule(rule1);
		// Set rule for Bob Briskey:
		// will bid double for paintings that are larger or equal to 625 sqIn.
		Rule rule2 = new Rule();
		rule2.setMinSquareInch(625);
		rule2.setEqualSquareInch(625);
		rule2.increaseBidRatioToStartingBid(2);
		bidders.get("Bob Briskey").addRule(rule2);

		// Set rule for Amanda Wu:
		Rule rule3 = new Rule();
		// will bid 1.5 times for paintings where the subject is Mrs Frisby.
		rule3.setSubject("Mrs Frisby");
		rule3.increaseBidRatioToStartingBid(1.5);
		// will bid 1.5 times for paintings where the subject is nature.
		rule3.setSubject("nature");
		rule3.increaseBidRatioToStartingBid(1.5);
		bidders.get("Amanda Wu").addRule(rule3);
		// will never bid more than $100,000 for any item.
		bidders.get("Amanda Wu").setBidLimit(100000);

		// Set rule for Sue Perkins:
		Rule rule4 = new Rule();
		// will only bid for paintings smaller than 625 square inches.
		rule4.setMaxSquareInch(625);
		// will bid double for paintings smaller than 625 square inches.
		rule4.increaseBidRatioToStartingBid(2);
		bidders.get("Sue Perkins").addRule(rule4);
		// will not bid for any paintings bigger or equal 625 square inches.
		Rule rule5 = new Rule();
		rule5.setMinSquareInch(625);
		rule5.setEqualSquareInch(625);
		rule5.increaseBidRatioToStartingBid(0);
		bidders.get("Sue Perkins").addRule(rule5);

		// Set rule for Donald von Neuman:
		// will not bid for paintings with a subject of tomatoes.
		Rule rule6 = new Rule();
		rule6.setSubject("tomatos");
		rule6.increaseBidRatioToStartingBid(0);
		bidders.get("Donald von Neuman").addRule(rule6);
		// will bid triple for paintings by Shelley Adler.
		Rule rule7 = new Rule();
		rule7.setArtist("Shelley Adler");
		rule7.increaseBidRatioToStartingBid(3);
		bidders.get("Donald von Neuman").addRule(rule7);
	}

	/**
	 * @param amount
	 *            Help method, to format 100,000$ into 100000.00 Returns double
	 *            representing the amount numeric value.
	 */
	public double convertCurrencyToDouble(String amount) {
		return Double.parseDouble(amount.replace(",", "").replaceAll("[^\\d.-]", ""));
	}

	/**
	 * @param amount
	 *            Help method, to format 100000.00 into 100,000$ Returns String
	 *            representing the amount US Dollar currency value.
	 */
	public String convertDoubleToCurrency(double amount) {
		// Format double to proper display: USD currency and Remove
		// trailing zeros past decimal point
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		return formatter.format(amount).replaceAll("\\.0*$", "");
	}

	/**
	 * @param fullJsonFilePath
	 * @param headerField
	 *            Help method, returns a JsonArray Object by given a JSON file
	 *            Path, and header (“bidders” / “items”)
	 */
	public JsonArray extractJsonTreeFromFile(String fullJsonFilePath, String headerField) throws FileNotFoundException {
		JsonReader jsonReader = new JsonReader(new FileReader(fullJsonFilePath));
		JsonParser jsonParser = new JsonParser();
		JsonElement jsonElement = jsonParser.parse(jsonReader);
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		return jsonObject.getAsJsonArray(headerField);
	}

	/**
	 * @param none
	 *            will operate much like an actual Bidding platform: Much like
	 *            in an actual bidding war, we present Item by Item, and go to
	 *            Bidder by Bidder.
	 * 
	 *            The first loop will iterate over all the Items, for all
	 *            Bidders. Placing the highest possible bids for each Item, It
	 *            will determine the Bidder that placed the highest possible Bid
	 *            for each and every Item, and set this bidder as the winner. a.
	 *            Iterating over each of the Items, b. For each Item, Iterating
	 *            over each of the Bidders: Suggesting each Item to each of the
	 *            Bidders, c. For each Bidder, Iterating over each of his/hers
	 *            full list of rules, going through the offers a bidder is
	 *            willing to make
	 * 
	 *            The second loop: Since the rules determine that each bidder
	 *            can place the starting bid, this method will iterate over all
	 *            the Items that do not have a winner after the first loop. In
	 *            this case no bidder was able to afford to place a bid higher
	 *            than the Item’s starting bid. The auction will accept bids at
	 *            the starting Price.
	 * 
	 */
	public void executeAuction() {
		for (Item i : items) {
			LOGGER.log(Level.FINE, "Item: " + i.getTitle() + " is Offered for sale");
			for (Bidder b : bidders.values()) {
				LOGGER.log(Level.FINE, "Bidder: " + b.getFullName() + " is placing all bids");
				i.initializeCurrentBid();
				for (Rule r : b.getRules()) {
					LOGGER.log(Level.FINE,
							"Bid is now placed for: " + r.getBidRatioToStartingBid() + "times more than starting bid");
					if (verifyRuleMatchItem(i, r)) {
						accumulateRulesToBid(i, b, r);
						i.setCurrentBidder(b);
					}
				}
				if (i.getCurrentBidder() != null) {
					if (canAffordToBid(i, b)) {
						LOGGER.log(Level.FINE, "Bidder: " + b.getFullName() + " can afford to place bid");
						placeMaximalBid(i, b);
					}
				}
			}
			// we have a final winner for this item once all bidders placed all
			// bids (rules)
			closeWinningBids(i);
		}

		for (Item i : items) {
			if (i.getHighestBidder() == null) {

				for (Bidder b : bidders.values()) {
					i.initializeCurrentBid();
					for (Rule r : b.getRules()) {
						LOGGER.log(Level.FINE, "Reading rule for bidder" + b.getFullName());
						if (verifyRuleMatchItem(i, r)) {
							accumulateRulesToBid(i, b, r);
							LOGGER.log(Level.FINE,
									"Accumulating more than one rule per item for bidder" + b.getFullName());
							i.setCurrentBidder(b);
						}
					}
					if (i.getCurrentBidder() != null) {
						if (canAffordToBidForStartPriceOnly(i, b)) {
							placeStartingBid(i, b);
						}
					}
				}
				// we have a final winner for this item once all bidders placed
				// all
				// bids (rules)
				closeWinningBids(i);
			}
		}
	}

	/**
	 * @param Item
	 * @param Bidder
	 *            Help method, returns true if: - Bidder has enough money to
	 *            place the Starting Bid, and - Bidder does not have a bidding
	 *            limit below the Starting Bid. Returns false otherwise.
	 */
	private boolean canAffordToBidForStartPriceOnly(Item i, Bidder b) {
		double currentRatio = i.getCurrentBidRatioToStartingBid();
		if (currentRatio < 1) {
			// can't offer bid below starting bid
			LOGGER.log(Level.FINE, "Can't offer bid below starting bid for bidder item: " + i.getTitle() + " bidder: "
					+ b.getFullName());
			return false;
		}
		double currentBid = i.getStartingBid() * 1;
		if (b.isBidLimit()) {
			return ((currentBid <= b.getTotalMoney()) && (currentBid <= b.getBidLimit()));
		} else
			return (currentBid <= b.getTotalMoney());
	}

	/**
	 * @param Item
	 * @param Rule
	 *            Help method, returns true if a rule apples to item. Returns
	 *            false otherwise.
	 */
	public boolean verifyRuleMatchItem(Item item, Rule rule) {
		boolean result = false;
		if (rule.isArtistRule()) {
			result = (item.getArtist().equals(rule.getArtist()));
			LOGGER.log(Level.FINE, "Match verified for Artist Rule: " + item.getArtist());
		} else if (rule.isSubjectRule()) {
			result = (item.getSubject().equals(rule.getSubject()));
			LOGGER.log(Level.FINE, "Match verified for Subject Rule: " + item.getSubject());
		} else if (rule.isEqualSquareInchRule()) {
			// less or equal than square Inch
			LOGGER.log(Level.FINE, "Match verified for = Square-Inch Rule: " + item.getSquareInch());
			if (rule.isMaxSquareInchRule() && rule.isEqualSquareInchRule()) {
				LOGGER.log(Level.FINE, "Match verified for <= Square-Inch Rule: " + item.getSquareInch());
				result = ((item.getSquareInch() < rule.getMaxSquareInch())
						|| (item.getSquareInch() == rule.getEqualSquareInch()));
			} else if (rule.isMinSquareInchRule() && rule.isEqualSquareInchRule()) {
				LOGGER.log(Level.FINE, "Match verified for >= Square-Inch Rule: " + item.getSquareInch());
				// more or equal than square Inch
				result = ((item.getSquareInch() > rule.getMinSquareInch())
						|| (item.getSquareInch() == rule.getEqualSquareInch()));
			} else // equal to square Inch
				result = (item.getSquareInch() == rule.getEqualSquareInch());
			LOGGER.log(Level.FINE, "Match verified for == Square-Inch Rule: " + item.getSquareInch());
		} else {// not less or equal than square Inch
			if (rule.isMaxSquareInchRule()) {
				// less than square Inch
				result = (item.getSquareInch() < rule.getMaxSquareInch());
				LOGGER.log(Level.FINE, "Match verified for < Square-Inch Rule: " + item.getSquareInch());
			} else if (rule.isMinSquareInchRule()) {
				// more than square Inch
				result = (item.getSquareInch() > rule.getMinSquareInch());
				LOGGER.log(Level.FINE, "Match verified for > Square-Inch Rule: " + item.getSquareInch());
			}
		}
		return result;
	}

	/**
	 * @param Item
	 * @param Bidder
	 * @param Rule
	 *            Help method, applies all the given rules that match a certain
	 *            Item. Such that, if Bidder b has two Rules R1 (place bid x
	 *            times the starting bid) and R2 (place bid y times the starting
	 *            bid) that apply for Item I, this method will update the
	 *            bidding amount accordingly to x*y*starting bid for I.
	 */
	public void accumulateRulesToBid(Item item, Bidder bidder, Rule rule) {
		LOGGER.log(Level.FINE, "Match verified for >= Square-Inch Rule: " + item.getSquareInch());
		item.accumulateCurrentBidRatioToStartingBid(rule.getBidRatioToStartingBid());
		item.setCurrentBidder(bidder);
	}

	/**
	 * @param Item
	 * @param Bidder
	 *            Help method, returns true if: - Bidder has enough money to
	 *            place the Starting Bid for Item, and - Bidder does not have a
	 *            bidding limit below the Starting Bid for Item. Returns false
	 *            otherwise.
	 */
	private boolean canAffordToBid(Item i, Bidder b) {
		double currentRatio = i.getCurrentBidRatioToStartingBid();
		if (currentRatio < 1) {
			// can't offer bid below starting bid
			LOGGER.log(Level.FINE, "Bidder lost for offering under starting bid");
			return false;
		}
		double currentBid = i.getStartingBid() * currentRatio;
		if (b.isBidLimit()) {
			LOGGER.log(Level.FINE, "Bidder has bidding limit");
			return ((currentBid <= b.getTotalMoney()) && (currentBid <= b.getBidLimit()));
		} else
			LOGGER.log(Level.FINE, "Bidder has no bidding limit");
		return (currentBid <= b.getTotalMoney());
	}

	/**
	 * @param Bidder
	 * @param Item
	 *            If current Bidder out-bids previous winner for an Item: Will
	 *            set current bidder as winner (highest bidder) for current
	 *            Item. Will refund previous bidder for item, and charge current
	 *            bidder for item.
	 */
	private void placeMaximalBid(Item i, Bidder b) {
		double startingBid = i.getStartingBid();
		Bidder previousWinner = i.getHighestBidder();
		double previousWinningBid = startingBid * i.getHighestBidRatioToStartingBid();
		double currRatio = i.getCurrentBidRatioToStartingBid();
		double currBid = startingBid * currRatio;
		if (previousWinner == null) {
			// no previous bids were made for this item, current bidder wins
			LOGGER.log(Level.FINE, "No previous bids were made for this item, current bidder wins by defualt");
			i.setHighestBidder(b);
			i.accumulateHighestBidRatioToStartingBid(currRatio);
			b.setTotalMoney(b.getTotalMoney() - currBid);
		} else if (currBid > previousWinningBid) {
			// current bidder out-bid all previous offers
			LOGGER.log(Level.FINE, "Current bidder: " + b.getFullName() + " out-bid all previous offers");
			i.setHighestBidder(b);
			i.accumulateHighestBidRatioToStartingBid(currRatio);
			b.setTotalMoney(b.getTotalMoney() - currBid);
			// return money to previous winner
			LOGGER.log(Level.FINE, "Bidder: " + previousWinner.getFullName() + " was outbid, and got a refund for bid");
			previousWinner.setTotalMoney(previousWinner.getTotalMoney() + previousWinningBid);
		}
	}

	/**
	 * @param Bidder
	 * @param Item
	 *            If no previous bidders have won Item: Will set current bidder,
	 *            Bidder as winner (highest bidder) for current Item, by placing
	 *            the starting bid. Will charge current bidder for item
	 */
	private void placeStartingBid(Item i, Bidder b) {
		Bidder previousWinner = i.getHighestBidder();
		if (previousWinner == null) {
			// no previous bids were made for this item, current bidder wins for
			// start price
			LOGGER.log(Level.FINE, "No previous bids were made for item: " + i.getTitle() + " Bidder: "
					+ b.getFullName() + " wins for start price");
			double startingBid = i.getStartingBid();
			double currRatio = 1;
			double currBid = startingBid * currRatio;
			i.setHighestBidder(b);
			i.accumulateHighestBidRatioToStartingBid(currRatio);
			b.setTotalMoney(b.getTotalMoney() - currBid);
		}
	}

	/**
	 * @param none
	 *            Once all bids are place (after first and after second run of
	 *            the executeAuction() method) this method will associate the
	 *            highest bidder for each item as the winner for this Item.
	 */
	private void closeWinningBids(Item i) {
		Bidder highestBidder = i.getHighestBidder();
		if (highestBidder != null) {
			String title = i.getTitle();
			String winnerName = highestBidder.getFullName();
			double bid = i.getHighestBidRatioToStartingBid() * i.getStartingBid();
			LOGGER.log(Level.FINE,
					title + "; winner " + winnerName + "; bid " + bid + "; ratio " + i.getHighestBidRatioToStartingBid()
							+ "; sbid " + i.getStartingBid() + "; moneyLeft" + highestBidder.getTotalMoney());
		} else
			LOGGER.log(Level.FINE, "No Bidder won this item: by Artist: " + i.getArtist() + "; Subject: "
					+ i.getSubject() + "; Title: " + i.getTitle());
	}

	/**
	 * @param none
	 *            Writes winners to winnig_bids.JSON file.
	 */
	public void writeToWinnersJSON() {
		JsonWriter writer;
		try {
			writer = new JsonWriter(new FileWriter(jsonFilePath + winnersFileName));
			writer.setIndent("  ");
			writer.beginObject();
			writer.name("winningBids");
			writer.beginArray();
			for (Item i : items) {
				LOGGER.log(Level.FINE, "Writing item titeled" + i.getTitle() + " to winning_bids file");
				String title = i.getTitle();
				String winner = i.getHighestBidder().getFullName();
				String bid = convertDoubleToCurrency(i.getHighestBidRatioToStartingBid() * i.getStartingBid());
				writer.beginObject();
				writer.name("title").value(title);
				writer.name("winner").value(winner);
				writer.name("bid").value(bid);
				writer.endObject();
			}
			writer.endArray();
			writer.endObject();
			writer.close();
		} catch (IOException ex) {
			LOGGER.log(Level.SEVERE, ex.toString(), ex);
			ex.printStackTrace();
		}
	}

	/**
	 * @param args
	 * @throws FileNotFoundException
	 *             Will conduct a bidding war on the Bauer bidding platform for
	 *             the given Bidders, Items and rules. Will advertise winners
	 *             into the winning_bids.json file
	 */
	public static void main(String[] args) throws FileNotFoundException {
		BiddingPlatform b = new BiddingPlatform();
		b.readItemsFromJSON();
		b.readBiddersFromJSON();
		b.loadRulesPerBidders();
		b.executeAuction();
		b.writeToWinnersJSON();
	}
}
