package be.pxl.auctions.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static be.pxl.auctions.builder.AuctionBuilder.anAuction;
import static be.pxl.auctions.builder.BidBuilder.aBid;
import static org.junit.jupiter.api.Assertions.*;

class AuctionTest {

	@Test
	void returnsCorrectIsFinishedWhenAuctionIsFinished() {
		Auction auction = anAuction()
				.withEndDate(LocalDate.now().minusDays(1))
				.build();

		assertTrue(auction.isFinished());
	}

	@Test
	void returnsCorrectIsFinishedWhenAuctionIsNotFinished() {
		Auction auction = anAuction()
				.withEndDate(LocalDate.now().plusDays(1))
				.build();

		assertFalse(auction.isFinished());
	}

	// when end date is today, auction is not finished
	@Test
	void returnsCorrectIsFinishedWhenAuctionEndDateIsToday() {
		Auction auction = anAuction()
				.withEndDate(LocalDate.now())
				.build();

		assertFalse(auction.isFinished());
	}



	@Test
	void findHighestBidWhenBidsNotEmpty() {

		List<Bid> bids = new ArrayList<>();

		bids.add(aBid().withAmount(10).build());
		bids.add(aBid().withAmount(20).build());
		bids.add(aBid().withAmount(30).build());

		Auction auction = anAuction()
				.withBids(bids)
				.build();

		assertTrue(auction.findHighestBid().isPresent());
		assertEquals(30, auction.findHighestBid().get().getAmount());
	}

	@Test
	void findHighestBidWhenBidsEmpty() {

		List<Bid> bids = new ArrayList<>();

		Auction auction = anAuction()
				.withBids(bids)
				.build();

		assertFalse(auction.findHighestBid().isPresent());
	}
}