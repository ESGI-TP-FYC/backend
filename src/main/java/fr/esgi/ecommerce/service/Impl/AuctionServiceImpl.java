package fr.esgi.ecommerce.service.Impl;

import fr.esgi.ecommerce.domain.Auction;
import fr.esgi.ecommerce.domain.Bid;
import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.repository.AuctionRepository;
import fr.esgi.ecommerce.repository.BidProductRepository;
import fr.esgi.ecommerce.service.AuctionService;
import fr.esgi.ecommerce.service.FilesStorageService;
import fr.esgi.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {
    private final AuctionRepository auctionRepository;
    private final FilesStorageService filesStorageService;
    private final OrderService orderService;
    private final BidProductRepository bidProductRepository;
    @Override
    public Auction findAuctionById(Long auctionId) {
        return auctionRepository.findById(auctionId).get();
    }

    @Override
    public List<Auction> findAllAuctions() {
        return auctionRepository.findAll();
    }

    @Override
    public Auction createAuction(Auction auction) {
        return auctionRepository.save(auction);
    }

    @Override
    public Auction addBidProduct(Long auctionId, BidProduct product, MultipartFile multipartFile) {
        String fileName = UUID.randomUUID() + "." + multipartFile.getOriginalFilename();
        filesStorageService.save(multipartFile, Path.of(fileName));
        product.setFilename(fileName);
        var auction = auctionRepository.findById(auctionId).get();
        if(auction.getProducts().isEmpty()){
            auction.setProducts(new ArrayList());
        }
        auction.getProducts().add(bidProductRepository.save(product));

        return auctionRepository.save(auction);
    }

    @Override
    public void deleteAuction(Long auctionId) {
        auctionRepository.deleteById(auctionId);
    }

    @Override
    public Auction addBid(Long auctionId,Long productId, Bid bid) {
        var auction = auctionRepository.findById(auctionId).get();
        auction.getProducts().forEach(product->{
            if(Objects.equals(product.getId(),productId)){
                if( product.getBids().isEmpty()){
                    product.setBids(new ArrayList<Bid>());
                }
                product.getBids().add(bid);
                bidProductRepository.save(product);
            }
        });
        return auctionRepository.findById(auctionId).get();
    }

    @Override
    public void managedAuctions() {
        //Get auction List
        var auctions = auctionRepository.findAll().stream().filter(auction -> auction.getDateFin().isAfter(LocalDateTime.now())).collect(Collectors.toList());
        //Create and send order
        auctions.stream().map(Auction::getProducts).flatMap(x -> x.stream()).map(BidProduct::getId).forEach(orderService::postAuctionOrder);
        //Delete Auction
        auctionRepository.deleteAll(auctions);
    }
}
