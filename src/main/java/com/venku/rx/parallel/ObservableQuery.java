// package com.venku.rx.parallel;
//
// import javax.management.Query;
//
// import rx.Observable;
//
/// **
// * https://gist.github.com/igouss/2670d6634d0d84470bf5
// */
// public class ObservableQuery<T> {
// private static final int BATCH_SIZE = 20;
// private Database database;
//
// public ObservableQuery(Database database) {
// this.database = database;
// }
//
// public Observable<T> execute(String queryString) {
// return Observable.defer(() -> Observable.using(this::getStatelessSession,
// session -> executeQuery(queryString, session),
// StatelessSession::close));
// }
//
// private StatelessSession getStatelessSession() {
// return database.getSessionFactory().openStatelessSession();
// }
//
// private Observable<T> executeQuery(String queryString, StatelessSession session) {
// return Observable.using(() -> getScrollableResults(queryString, session),
// scrollableResults -> scrollResults(scrollableResults),
// scrollableResults -> scrollableResults.close());
// }
//
// private ScrollableResults getScrollableResults(String queryString, StatelessSession session) {
// Query query = session.createQuery(queryString);
// query.setCacheable(false);
// query.setReadOnly(true);
// query.setFetchSize(BATCH_SIZE);
// return query.scroll(ScrollMode.FORWARD_ONLY);
// }
//
// private Observable<T> scrollResults(ScrollableResults result) {
// return Observable.<T> create(subscriber -> {
// while (!subscriber.isUnsubscribed() && result.next()) {
// subscriber.onNext((T) result.get(0));
// }
// if (!subscriber.isUnsubscribed()) {
// subscriber.onCompleted();
// }
// });
// }
// }