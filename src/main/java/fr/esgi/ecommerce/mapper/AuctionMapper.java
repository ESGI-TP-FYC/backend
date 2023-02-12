package fr.esgi.ecommerce.mapper;

import fr.esgi.ecommerce.domain.Auction;
import fr.esgi.ecommerce.domain.Bid;
import fr.esgi.ecommerce.domain.BidProduct;
import fr.esgi.ecommerce.domain.Product;
import fr.esgi.ecommerce.dto.auction.AuctionDto;
import fr.esgi.ecommerce.dto.auction.BidDto;
import fr.esgi.ecommerce.dto.auction.BidProductDto;
import fr.esgi.ecommerce.dto.product.ProductRequest;
import fr.esgi.ecommerce.dto.product.ProductResponse;
import fr.esgi.ecommerce.service.AuctionService;
import fr.esgi.ecommerce.service.FilesStorageService;
import fr.esgi.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuctionMapper {

    private final ModelMapper modelMapper;
    private final AuctionService auctionService;
    private final FilesStorageService filesStorageService;

    private Auction convertToEntity(AuctionDto auctionDto) {
        return modelMapper.map(auctionDto, Auction.class);
    }

    BidProductDto convertBidProductToResponseDto(BidProduct product) {
        var response = modelMapper.map(product, BidProductDto.class);
        if(!(response.getFilename().isEmpty() && response.getFilename().isBlank())){
            var resource = filesStorageService.load(response.getFilename());
            response.setFile(resource);
        }
        return response;
    }

    List<BidProductDto> convertListBidProductToResponseDto(List<BidProduct> products) {
        return products != null && !products.isEmpty() ? products.stream()
                .map(this::convertBidProductToResponseDto)
                .collect(Collectors.toList()): new ArrayList<BidProductDto>();
    }

    AuctionDto convertToResponseDto(Auction auction) {
        var dto = modelMapper.map(auction, AuctionDto.class);
        dto.setProducts(convertListBidProductToResponseDto(auction.getProducts()));
        return dto;
    }

    List<AuctionDto> convertListToResponseDto(List<Auction> auctions) {
        return auctions != null && !auctions.isEmpty() ? auctions.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList()): new ArrayList<AuctionDto>();
    }

    BidProduct convertBidProductToEntity(BidProductDto bidProductDto) {
        return modelMapper.map(bidProductDto, BidProduct.class);
    }
    Bid convertBidToEntity(BidDto bidDto) {
        return modelMapper.map(bidDto, Bid.class);
    }
    BidDto convertBidToResponseDto(Bid bid) {
        return modelMapper.map(bid, BidDto.class);
    }

    List<BidDto> convertListBidToResponseDto(List<Bid> bids) {
        return bids != null && !bids.isEmpty() ? bids.stream()
                .map(this::convertBidToResponseDto)
                .collect(Collectors.toList()): new ArrayList<BidDto>();
    }

    public AuctionDto findAuctionById(Long auctionId){
        return convertToResponseDto(auctionService.findAuctionById(auctionId));
    }

    public List<AuctionDto> findAllAuctions(){
        return convertListToResponseDto(auctionService.findAllAuctions());
    }

    public AuctionDto createAuction(AuctionDto auctionDto){
        return convertToResponseDto(auctionService.createAuction(convertToEntity(auctionDto)));
    }
    public AuctionDto addBidProduct(Long auctionId, BidProductDto productDto, MultipartFile file){
        return convertToResponseDto(auctionService.addBidProduct(auctionId,convertBidProductToEntity(productDto),file));
    }
    public void deleteAuction(Long auctionId){
        auctionService.deleteAuction(auctionId);
    }

    public AuctionDto addBid(Long auctionId,Long productId, BidDto bid) {
        return convertToResponseDto(auctionService.addBid(auctionId,productId,convertBidToEntity(bid)));
    }
}
