package be.pxl.auctions.rest;

import be.pxl.auctions.rest.resource.AuctionDTO;
import be.pxl.auctions.rest.resource.BidDTO;
import be.pxl.auctions.service.AuctionService;
import be.pxl.auctions.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("auctions")
public class AuctionRest {

	private static final Logger LOGGER = LogManager.getLogger(AuctionRest.class);


	private final AuctionService auctionService;

	@Autowired
	public AuctionRest(AuctionService auctionService) {
		this.auctionService = auctionService;
	}


	@GetMapping
	public List<AuctionDTO> getAllAuctions() {
		return auctionService.getAllAuctions();
	}

	@GetMapping("/{id}")
	public AuctionDTO getAuctionById(@PathVariable("id") Long id) {
		return auctionService.getAuctionById(id);
	}

	@GetMapping("/current")
	public List<AuctionDTO> retrieveCurrentAuctions() {
		return auctionService.retrieveCurrentAuctions();
	}

	@PostMapping
	public AuctionDTO createAuction(@RequestBody AuctionDTO auctionDTO) {
		return auctionService.createAuction(auctionDTO);
	}

	@PostMapping("/{id}/bids")
	public BidDTO bid(@PathVariable("id") Long id, @RequestBody BidDTO bidDTO) {
		return auctionService.makeBid(id, bidDTO);
	}

	@GetMapping("/{id}/bids")
	public List<BidDTO> getBidsByAuctionId(@PathVariable("id") Long id) {
		return auctionService.getBidsByAuctionId(id);
	}
}


