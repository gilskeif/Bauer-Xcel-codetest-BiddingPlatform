# Bauer-Xcel-codetest-BiddingPlatform

How to run the code:
1.	Import the project to your local Java IDE (eclipse JEE Neon is recommended). 
Use “import project” menu: Right click and select import Project > >existing project into workspace > select zip file path, and enter zip file name  > finish

2. Run the static main method in class BiddingPlatform:
 
3. The File winning_bids.json will now hold the bidding platform execution results
 
Note: In case you see a need to read/write from JASN files that are in a file path different than that of your workspace folder, please manually alter the variable workspacePath in line 23 of the BiddingPlatform class code.

Architecture:
•	Bidder ObjectClass
private String fullName;
private double totalMoney = 0;
private double bidLimit;
private boolean isBidLimit = false;
private ArrayList<Rule> rules; 
Much like in an actual bid the full list of rules associated with a bidder will be stored in a list, as a private member of the Bidder object.
In an actual bidding war, each offer is associated with a certain bidder. That same logic created a conceptual need to store the list of rules relevant to a certain Bidder as an internal list of Bidder.

•	Rule ObjectClass
// 1 means a bid at the start price will be placed
private double bidRatioToStartingBid = 1;
private String subject;
private String artist;
private double maxSquareInch;
private double minSquareInch;
private double equalSquareInch;
	
•	Item ObjectClass
Much like in an actual bidding platform, each Item will be associated with it's relevant traits (title, measurements, etc)

private String title;
private String type;
private String subject;
private double startingBid;
private double highestBidRatioToStartingBid;
I chose to associate the max current bid, and then the winning bid, and winning bidder ID as part of the Item, since again, in actual bid, upon placing all the bids, we associate those to the item.
private Bidder highestBidder;
private double currentBidRatioToStartingBid;
private Bidder currentBidder;
// 1st, 2nd and 3rd entries in array represent Length, Width and Depth respectively
private double[] dimensionLengthWidthDepth = new double[3];
I have encapsulated the internal implementation of internal members, such that, the outside user of this class is indifferent to the fact that an item's dimensions are stored an array with 3 coordinates. Using getters and setters, the user of the Item class is unaware of how dimensions (sqft) are calculated internally.
private String artist;

•	BiddingPlatform ObjectClass
This class represents the Bauer Bidding Platform
I have chosen to implement a BiddingPlatform ObjectClass much like an actual bidding platform in "real life":
It will hold references to the following objects: Bidder, Rule and Item.
This BiddingPlatform class will create an instance of a Bidding Platform Object.

void main(String[] args)	Will conduct a bidding war on the Bauer bidding platform for the given Bidders, Items and rules. Will advertise winners into the winning_bids.json file.

void readItemsFromJSON()
void
readBiddersFromJSON()	Will read the input files (JSON), and create instances of Bidders, Items and Rules accordingly.

void  loadRulesPerBidders() 	Will execute a simple logic to create rules, base on the specifications as described in the project requirements wiki, and will then associate those to the relevant Bidders from the bidders.JSON file.

void executeAuction();
	Will operate much like an actual Bidding platform: Much like in an actual bidding war, we present Item by Item, and go to Bidder by Bidder.

	The first loop will iterate over all the Items, for all Bidders. Placing the highest possible bids for each Item, It will determine the Bidder that placed the highest possible Bid for each and every Item, and set this bidder as the winner.
a.	Iterating over each of the Items, 
b.	For each Item, Iterating over each of the Bidders: 
Suggesting each Item to each of the Bidders,
c.	For each Bidder, Iterating over each of his/hers full list of rules, going through the offers a bidder is willing to make
	The second loop: Since the rules determine that each bidder can place the starting bid, this method will iterate over all the Items that do not have a winner after the first loop. In this case no bidder was able to afford to place a bid higher than the Item’s starting bid. The auction will accept bids at the starting Price.
	Note: I was instructed to Ignore end cases that were not relevant to the given bidding scenario:
- A case where more than one bidder place the same highest winning bid for the same Item did not occur.
- A case where a bidder had more that one possible winning and bidding outcomes for different items.
Unlike an actual bidding war, this method does not cater to those cases.

void
writeToWinnersJSON();	Writes winners to winnig_bids.JSON file.

Methods for Additional steps:
Void placeMaximalBid(Item i, Bidder b)	If current Bidder out-bids previous winner for an Item: Will set current bidder as winner (highest bidder) for current Item. Will refund previous bidder for item, and charge current bidder for item.

Void placeStartingBid(Item i, Bidder b)	If no previous bidders have won Item: Will set current bidder, Bidder as winner (highest bidder) for current Item, by placing the starting bid. Will charge current bidder for item.

Void closeWinningBids(Item i)	Once all bids are place (after first and after second run of
the executeAuction() method) this method will associate the highest bidder for each item as the winner for this Item.

Help methods:

double convertCurrencyToDouble(String amount)	Help method, to format 100,000$ into 100000.00 Returns double representing the amount numeric value.

String convertDoubleToCurrency(double amount)	Help method, to format 100000.00 into 100,000$ Returns String representing the amount US Dollar currency value.

JsonArray extractJsonTreeFromFile(String fullJsonFilePath, String headerField)	Help method, returns a JsonArray Object by given a JSON file Path, and header (“bidders” / “items”)



boolean
verifyRuleMatchItem(Item item, Rule rule)	Help method, returns true if a rule apples to item.
Returns false otherwise.

void
accumulateRulesToBid(
Item item, Bidder bidder, Rule rule)	Help method, applies all the given rules that match a certain
Item. Such that, if Bidder b has two Rules R1 (place bid x
times the starting bid) and R2 (place bid y times the starting
bid) that apply for Item I, this method will update the bidding amount accordingly to x*y*starting bid for I.

boolean
canAffordToBid(Item i, Bidder b)	Help method, returns true if:
- Bidder has enough money to place current bid as determined by the rules, and
- Bidder does not have a bidding limit below current bid, and
- Bidder’s bid is above starting bid.
Returns false otherwise.

boolean
canAffordToBidForStartPriceOnly(Item i, Bidder b)	Help method, returns true if:
- Bidder has enough money to place the Starting Bid, and 
- Bidder does not have a bidding limit below the Starting Bid.
Returns false otherwise.


•	End cases management:
Having asked via email, I was explicitly instructed to ignore the following end cases:
1. Handling a theoretical case where two bidders make a final highest winning bid for the same item, at the same amount.
“I suspect it might be an edge case that does not _actually_ occur with the given data ,but might occur with other data. There's no need to solve for that case if it doesn't actually exist in the data”.

2. Handling a theoretical case where one bidder can have more than one possible setting of bidding outcomes.
“The "rules" to the bidding platform are incomplete, just like most business requests. It's possible there are lots of theoretical edge cases to worry about. However, your code will not be graded on how many edge cases are uncovered. Instead, you should focus on solving for only the outcomes for the given data. If that passes, your code is correct. The grading will focus more on the readability, cleanliness, and structure of your code.”

3. An implementation of adding new future rules, outside the given rules.txt file?
“The solution here should stay within the scope of the challenge presented. While your code should strive to be extensible, the code challenge should be approached without assumptions about the future direction of the platform. Choosing where to make tradeoffs in extensibility vs simplicity is part of the challenge.”
