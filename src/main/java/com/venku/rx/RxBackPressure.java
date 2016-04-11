package com.venku.rx;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javaslang.control.Try;
import rx.Observable;
import rx.observables.AbstractOnSubscribe;
import rx.schedulers.Schedulers;

/**
 * 
 * @author Venkatesha Chandru
 * 
 */
public class RxBackPressure {

    @SuppressWarnings("resource")
    public static Observable backPressure() {
        return Observable.create(AbstractOnSubscribe.<Order, ResultSet> create(s -> {
            ResultSet rs = s.state();
            try {
                if (rs.next()) {
                    s.onNext(new Order(rs.getInt(1)));
                } else {
                    s.onCompleted();
                }
            } catch (SQLException e) {
                s.onError(e);
            }
        } ,
                s -> {
                    Connection c = getConnection(); // Database.getConnection();
                    try {
                        Statement stmt = c.createStatement(java.sql.ResultSet.TYPE_FORWARD_ONLY,
                                java.sql.ResultSet.CONCUR_READ_ONLY);
                        stmt.setFetchSize(Integer.MIN_VALUE);
                        return stmt.executeQuery("select * from orders");
                    } catch (SQLException e) {
                        s.onError(e);
                        return null;
                    }
                } ,
                rs -> Try.run(() -> rs.close())))
                .subscribeOn(Schedulers.io());

    }

    public static Connection getConnection() {
        return null;
    }

}

class Order {

    public Order(int id) {

    }
}
