package course.concurrency.exams.auction;

public class AuctionStoppablePessimistic implements AuctionStoppable {

    private Notifier notifier;

    public AuctionStoppablePessimistic(Notifier notifier) {
        this.notifier = notifier;
    }
    private volatile Bid latestBid = new Bid(-1L, -1L, -1L);

    private final Object lock = new Object();
    private volatile boolean isOpen = true;

    public boolean propose(Bid bid) {
        if (isOpen && (bid.price > latestBid.price)) {
            synchronized (lock) {
                if (isOpen && bid.price > latestBid.price) {
                    notifier.sendOutdatedMessage(latestBid);
                    latestBid = bid;
                    return true;
                }
            }
        }
        return false;
    }

    public Bid getLatestBid() {
        return latestBid;
    }

    public Bid stopAuction() {
        synchronized (lock) {
            isOpen = false;
            return latestBid;
        }
    }
}
