package fr.esgi.ecommerce.controller;

import fr.esgi.ecommerce.dto.auction.AuctionDto;
import fr.esgi.ecommerce.dto.auction.BidDto;
import fr.esgi.ecommerce.dto.auction.BidProductDto;
import fr.esgi.ecommerce.mapper.AuctionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auction")
public class AuctionController {

    private final AuctionMapper auctionMapper;

    @GetMapping("/{auctionId}")
    ResponseEntity<AuctionDto> findAuctionById(@PathVariable(value = "auctionId") Long auctionId) {
        return ResponseEntity.ok(auctionMapper.findAuctionById(auctionId));
    }

    @GetMapping
    ResponseEntity<List<AuctionDto>> findAllAuctions() {
        return ResponseEntity.ok(auctionMapper.findAllAuctions());
    }

    @PostMapping
    ResponseEntity<AuctionDto> createAuction(@RequestBody AuctionDto auction) {
        return ResponseEntity.ok(auctionMapper.createAuction(auction));
    }

    @PostMapping("/{auctionId}/product")
    ResponseEntity<AuctionDto> addBidProduct(@PathVariable(value = "auctionId") Long auctionId,
                                             @RequestPart(name = "product") BidProductDto product,
                                             @RequestPart(name = "file") MultipartFile file) {
        return ResponseEntity.ok(auctionMapper.addBidProduct(auctionId, product, file));
    }

    @PostMapping("/{auctionId}/product/{productId}/bid")
    ResponseEntity<AuctionDto> addBid(@PathVariable(value = "auctionId") Long auctionId,
                                      @PathVariable(value = "productId") Long productId,
                                      @RequestBody BidDto bid) {
        return ResponseEntity.ok(auctionMapper.addBid(auctionId,productId, bid));
    }

    @DeleteMapping("/{auctionId}")
    ResponseEntity<Void> deleteAuction(@PathVariable(value = "auctionId") Long auctionId) {
        auctionMapper.deleteAuction(auctionId);
        return ResponseEntity.ok().build();
    }
}
