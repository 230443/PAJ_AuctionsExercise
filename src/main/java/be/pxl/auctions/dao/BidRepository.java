package be.pxl.auctions.dao;

import be.pxl.auctions.model.Bid;
import org.springframework.data.repository.CrudRepository;

public interface BidRepository extends CrudRepository<Bid, Long> {
}
