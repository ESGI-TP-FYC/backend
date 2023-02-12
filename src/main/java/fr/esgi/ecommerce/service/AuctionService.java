package fr.esgi.ecommerce.service;

import fr.esgi.ecommerce.domain.Auction;
import fr.esgi.ecommerce.domain.Bid;
import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.domain.Product;
import graphql.schema.DataFetcher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AuctionService {

    Auction findAuctionById(Long auctionId);

    List<Auction> findAllAuctions();

    Auction createAuction(Auction auction);

    Auction addBidProduct(Long auctionId, BidProduct product, MultipartFile file);

    void deleteAuction(Long auctionId);

    Auction addBid(Long auctionId, Long productId, Bid convertBidToEntity);

    void managedAuctions();
}
