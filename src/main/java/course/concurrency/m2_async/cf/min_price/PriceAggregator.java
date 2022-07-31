package course.concurrency.m2_async.cf.min_price;


import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PriceAggregator {

    private PriceRetriever priceRetriever = new PriceRetriever();

    public void setPriceRetriever(PriceRetriever priceRetriever) {
        this.priceRetriever = priceRetriever;
    }

    private Collection<Long> shopIds = Set.of(10l, 45l, 66l, 345l, 234l, 333l, 67l, 123l, 768l);

    public void setShops(Collection<Long> shopIds) {
        this.shopIds = shopIds;
    }

    // fixed executor is used to limit threads
    private ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public double getMinPrice(long itemId) {
        List<CompletableFuture<Double>> completableFutureList = new ArrayList<>();

        for (var shopId : shopIds) {

            CompletableFuture<Double> cfForShop =
                    CompletableFuture.supplyAsync(() -> priceRetriever.getPrice(itemId, shopId), executor)
                            .completeOnTimeout(Double.POSITIVE_INFINITY, 2900, TimeUnit.MILLISECONDS)
                            .handle((res, ex) -> res != null ? res : Double.POSITIVE_INFINITY);
            completableFutureList.add(cfForShop);
        }
        //await completion of a set of independent CompletableFutures before continuing a program,
        CompletableFuture
                .allOf(completableFutureList.toArray(CompletableFuture[]::new))
                .join();

        return completableFutureList
                .stream()
                .mapToDouble(CompletableFuture::join)
                .filter(Double::isFinite)
                .min()
                .orElse(Double.NaN);
    }
}
