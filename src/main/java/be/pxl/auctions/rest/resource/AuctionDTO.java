package be.pxl.auctions.rest.resource;

import be.pxl.auctions.util.exception.InvalidDateException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AuctionDTO {

	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/uuuu");

	private Long id;
	private String description;
	private LocalDate endDate;

	private Boolean finished;
	private Integer numberOfBids;
	private Double highestBid;
	private String highestBidBy;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public void setEndDate(String endDate) {
		try {
			this.endDate = LocalDate.parse(endDate, DATE_FORMAT);
		} catch (DateTimeParseException e) {
			throw new InvalidDateException("[" + endDate + "] is not a valid date. Excepted format: dd/mm/yyyy");
		}
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Integer getNumberOfBids() {
		return numberOfBids;
	}

	public void setNumberOfBids(Integer numberOfBids) {
		this.numberOfBids = numberOfBids;
	}

	public Double getHighestBid() {
		return highestBid;
	}

	public void setHighestBid(Double highestBid) {
		this.highestBid = highestBid;
	}

	public String getHighestBidBy() {
		return highestBidBy;
	}

	public void setHighestBidBy(String highestBidBy) {
		this.highestBidBy = highestBidBy;
	}
}
