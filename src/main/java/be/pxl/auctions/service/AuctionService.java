package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.dao.BidRepository;
import be.pxl.auctions.dao.UserRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.model.Bid;
import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.util.exception.AuctionNotFoundException;
import be.pxl.auctions.util.exception.InvalidBidException;
import be.pxl.auctions.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final BidRepository bidRepository;
	private final UserRepository userRepository;

	@Autowired
	public AuctionService(AuctionRepository auctionRepository, BidRepository bidRepository, UserRepository userRepository) {
		this.auctionRepository = auctionRepository;
		this.bidRepository = bidRepository;
		this.userRepository = userRepository;
	}

	public List<AuctionDTO> getAllAuctions() {
		return auctionRepository.findAll().stream().map(this::mapAuctionToDTO).collect(Collectors.toList());
	}

	public AuctionDTO getAuctionById(Long id) {
		return mapAuctionToDTO(auctionRepository.findById(id).orElseThrow(() -> new AuctionNotFoundException("Unable to find Auction with id [" + id + "]")));
	}

	public List<AuctionDTO> retrieveCurrentAuctions() {
		return auctionRepository.findAll().stream().filter(not(Auction::isFinished)).map(this::mapAuctionToDTO).collect(Collectors.toList());
	}

	public AuctionDTO createAuction(AuctionDTO auctionDTO) {
		Auction auction = mapDTOToAuction(auctionDTO);
		auction = auctionRepository.save(auction);
		return mapAuctionToDTO(auction);
	}

	public BidDTO makeBid(Long auctionId, BidDTO bidDTO) {
		Auction auction = auctionRepository
				.findById(auctionId)
				.orElseThrow(
						() -> new AuctionNotFoundException("Unable to find Auction with id [" + auctionId + "]")
				);

//		The auctionId must belong to be a valid current auction. Bidding on a finished auction is not allowed.
//		The email must belong to an existing user.
//		The price of the bid must exceed previous bids.
//		The bid is not created by the user with the current highest bid.

		if (auction.isFinished()) {
			throw new InvalidBidException("Bidding on a finished auction is not allowed.");
		}

		Bid bid;

		try {
			bid = mapDTOToBid(bidDTO, auction);
		} catch (UserNotFoundException e) {
				throw new InvalidBidException("Unable to find user with email [" + bidDTO.getEmail() + "]");
		}

		Bid finalBid = bid;
		auction.findHighestBid().ifPresent(highestBid -> {
			if (highestBid.getAmount() >= finalBid.getAmount()) {
				throw new InvalidBidException("The bid price must exceed the current highest bid.");
			}
			if (highestBid.getUser().getEmail().equals(finalBid.getUser().getEmail())) {
				throw new InvalidBidException("The bid is created by the user with the current highest bid.");
			}
		});

		return mapBidToDTO(bidRepository.save(finalBid));
	}

	public List<BidDTO> getBidsByAuctionId(Long id) {
		Auction auction = auctionRepository
				.findById(id)
				.orElseThrow(() -> new AuctionNotFoundException("Unable to find Auction with id [" + id + "]"));
		return auction
				.getBids()
				.stream()
				.map(this::mapBidToDTO)
				.collect(Collectors.toList());
	}

	private Auction mapDTOToAuction(AuctionDTO auctionDTO) {
		Auction auction = new Auction();
		auction.setDescription(auctionDTO.getDescription());
		auction.setEndDate(auctionDTO.getEndDate());
		return auction;
	}

	private AuctionDTO mapAuctionToDTO(Auction auction) {
		AuctionDTO auctionDTO = new AuctionDTO();
		auctionDTO.setId(auction.getId());
		auctionDTO.setDescription(auction.getDescription());
		auctionDTO.setEndDate(auction.getEndDate());
		auctionDTO.setFinished(auction.isFinished());
		auctionDTO.setNumberOfBids(auction.getBids().size());
		var highestBid = auction.findHighestBid();
		if (highestBid.isPresent()) {
			auctionDTO.setHighestBid(highestBid.get().getAmount());
			auctionDTO.setHighestBidBy(highestBid.get().getUser().getEmail());
		}
		return auctionDTO;
	}

	private BidDTO mapBidToDTO(Bid bid) {
		BidDTO bidDTO = new BidDTO();
		bidDTO.setId(bid.getId());
		bidDTO.setPrice(bid.getAmount());
		bidDTO.setEmail(bid.getUser().getEmail());

		return bidDTO;
	}

	private Bid mapDTOToBid(BidDTO bidDTO, Auction auction) {
		Bid bid = new Bid();
		bid.setAmount(bidDTO.getPrice());
		bid.setUser(userRepository.findUserByEmail(bidDTO.getEmail()).orElseThrow(() -> new UserNotFoundException("Unable to find User with email [" + bidDTO.getEmail() + "]")));
		bid.setAuction(auction);

		return bid;
	}
}
