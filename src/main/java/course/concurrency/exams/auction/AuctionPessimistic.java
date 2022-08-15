package course.concurrency.exams.auction;

public class AuctionPessimistic implements Auction {

    private Notifier notifier;

    public AuctionPessimistic(Notifier notifier) {
        this.notifier = notifier;
    }

    private volatile Bid latestBid = new Bid(-1L, -1L, -1L);;

    private final Object lock = new Object();
    public boolean propose(Bid bid) {
        if (bid.price > latestBid.price) {
            synchronized (lock) {
                if (bid.price > latestBid.price) {
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
}
