package be.pxl.auctions.dao;

import be.pxl.auctions.model.Auction;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AuctionRepository extends CrudRepository<Auction, Long> {
	Optional<Auction> findAuctionById(Long id);
	List<Auction> findAll();
}
