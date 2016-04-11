package com.venku.rx;

/**
 * 
 * @author Venkatesha Chandru
 * 
 */
public class RxBlockingVideo {

    /**
     * <pre>
     * public Observable<Void> handle(HttpServerRequest<ByteBuf> request, HttpServerResponse<ByteBuf> response) {
     *       // first request User object
     *       return getUser(request.getQueryParameters().get("userId")).flatMap(user -> {
     *               // then fetch personal catalog
     *               Observable<Map<String, Object>> catalog = getPersonalizedCatalog(user)
     *                       .flatMap(catalogList -> {
     *                           return catalogList.videos().<Map<String, Object>> flatMap(video -> {
     *                               Observable<Bookmark> bookmark = getBookmark(video);
     *                               Observable<Rating> rating = getRating(video);
     *                               Observable<VideoMetadata> metadata = getMetadata(video);
     *                               return Observable.zip(bookmark, rating, metadata, (b, r, m) -> {
     *                               return combineVideoData(video, b, r, m);
     *                               });
     *                           });
     *                       });
     *               // and fetch social data in parallel
     *               Observable<Map<String, Object>> social = getSocialData(user).map(s -> {
     *               return s.getDataAsMap();
     *               });
     *               // merge the results
     *               return Observable.merge(catalog, social);
     *           }).flatMap(data -> {
     *           // output as SSE as we get back the data (no waiting until all is done)
     *           return response.writeAndFlush(new ServerSentEvent(SimpleJson.mapToJson(data)));
     *       });
     *   }
     * </pre>
     * 
     */
}
