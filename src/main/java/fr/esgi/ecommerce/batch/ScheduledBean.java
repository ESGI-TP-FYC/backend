package fr.esgi.ecommerce.batch;

import fr.esgi.ecommerce.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ScheduledBean {
    private final AuctionService auctionService;
    @Scheduled(cron = "${application.scheduler.cron}", zone = "application.scheduler.zone")
    private void scheduledCron() throws InterruptedException {
        auctionService.managedAuctions();
    }
}