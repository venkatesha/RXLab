package com.venku.rx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import rx.Observable;

/**
 * Sync to Async implemented in Netflix at service layer.
 * 
 * <p>
 * <a href="https://www.youtube.com/watch?v=ET_SMMXkE5s">Reactive.community: Ben Christensen, Reactive Extensions (Rx) at Netflix</a>
 * 
 * 
 * @author Venkatesha Chandru
 * 
 */
public class RxBlocking {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // perform service composition asynchronously
            getShippingStatusForCustomerOrdersAsync(req.getParameter("customerId"))
                    .toBlocking() // block on servlet thread
                    .forEach(status -> {
                        try {
                            // write output using blocking IO
                            resp.getWriter().write(status.toString());
                        } catch (Exception e) {
                            throw new RuntimeException("unable to write", e);
                        }
                    });
        } catch (Exception e) {
            // toBlocking and forEach will convert async onError events into thrown exceptions
            resp.setStatus(500);
            // write and log error ...
        }
    }

    Observable<ShippingStatus> getShippingStatusForCustomerOrdersAsync(String customerId) {
        return getOrdersAsync(Integer.parseInt(customerId))
                .limit(1)
                .flatMap(o -> {
                    return getProductsAsync(o)
                            .flatMap(p -> {
                        return getShippingStatusAsync(o, p);
                    });
                });
    }

    private Observable<? extends ShippingStatus> getShippingStatusAsync(ShippingStatus o, ShippingStatus p) {
        // TODO Auto-generated method stub
        return null;
    }

    private Observable<ShippingStatus> getProductsAsync(ShippingStatus o) {
        // TODO Auto-generated method stub
        return null;
    }

    private Observable<ShippingStatus> getOrdersAsync(int parseInt) {
        // TODO Auto-generated method stub
        return null;
    }
}

class ShippingStatus {

}
