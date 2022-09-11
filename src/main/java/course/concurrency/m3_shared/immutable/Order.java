package course.concurrency.m3_shared.immutable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static course.concurrency.m3_shared.immutable.Order.Status.NEW;

public class Order {
    public enum Status {NEW, IN_PROGRESS, DELIVERED}

    private static final AtomicLong nextId = new AtomicLong();
    private final Long id;
    private final List<Item> items;
    private final PaymentInfo paymentInfo;
    private final boolean isPacked;
    private Status status;

    public Order(List<Item> items) {
        this.items = items;
        this.id = nextId.incrementAndGet();
        this.paymentInfo = null;
        this.isPacked = false;
        this.status = NEW;
    }

    private Order(Long id, List<Item> items, PaymentInfo paymentInfo, boolean isPacked, Status status) {
        this.items = items;
        this.id = nextId.incrementAndGet();
        this.paymentInfo = null;
        this.isPacked = false;
        this.status = NEW;
    }

    public boolean checkStatus() {
        if (paymentInfo != null && isPacked) {
            return true;
        }
        return false;
    }

    public Order withPaymentInfo(PaymentInfo paymentInfo) {
        return new Order(this.id, this.items, paymentInfo, this.isPacked, this.status);
    }

    public Order withStatus(Status status) {
        return new Order(this.id, this.items, this.paymentInfo, this.isPacked, status);
    }

    public Order doPack() {
        return new Order(this.id, this.items, this.paymentInfo, true, this.status);
    }

    public Status getStatus() {
        return status;
    }

    public Long getId() {
        return id;
    }

    public List<Item> getItems() {
        return items;
    }

}
