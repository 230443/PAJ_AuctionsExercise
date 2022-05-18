package be.pxl.auctions.servlet;

import be.pxl.auctions.dao.AuctionRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@WebServlet(value = "/count")
public class CounterServlet extends HttpServlet {

	private final AuctionRepository auctionRepository;

	@Autowired
	public CounterServlet(AuctionRepository auctionRepository) {
		this.auctionRepository = auctionRepository;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter writer = resp.getWriter();
		int numberOfAuctions = auctionRepository.findAll().size();
		writer.println("""
				<html>
					<body>
						<h1> Number of auctions: %d </h1>
					</body>
				</html>
				""".formatted(numberOfAuctions));
	}
}