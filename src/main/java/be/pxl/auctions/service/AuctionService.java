package be.pxl.auctions.service;

import be.pxl.auctions.dao.AuctionRepository;
import be.pxl.auctions.model.Auction;
import be.pxl.auctions.rest.resource.AuctionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;

	@Autowired
	public AuctionService(AuctionRepository auctionRepository) {
		this.auctionRepository = auctionRepository;
	}

	public List<AuctionDTO> getAllAuctions() {
		return auctionRepository.findAll().stream().map(this::mapAuctionToDTO).collect(Collectors.toList());
	}

	public AuctionDTO createAuction(AuctionDTO auctionDTO) {
		Auction auction = mapDTOToAuction(auctionDTO);
		auction = auctionRepository.save(auction);
		return mapAuctionToDTO(auction);
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
}
