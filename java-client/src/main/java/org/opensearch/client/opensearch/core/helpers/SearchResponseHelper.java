/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.client.opensearch.core.helpers;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.opensearch.client.opensearch._types.aggregations.Aggregate;
import org.opensearch.client.opensearch._types.aggregations.LongTermsBucket;
import org.opensearch.client.opensearch._types.aggregations.StringTermsBucket;
import org.opensearch.client.opensearch.core.SearchResponse;

/**
 * Helper methods for easier {@link SearchResponse} parsing.
 * Simplifies common aggregation access patterns.
 *
 * <p>Example usage:
 * <pre>{@code
 * import static org.opensearch.client.opensearch.core.helpers.SearchResponseHelper.*;
 *
 * termsBuckets(response, "brands").forEach(bucket -> {
 *     String key = bucket.key();
 *     long count = bucket.docCount();
 * });
 * }</pre>
 */
public final class SearchResponseHelper {

    private SearchResponseHelper() {
        // Utility class
    }

    /**
     * Gets string term buckets from a terms aggregation.
     *
     * @param response the search response
     * @param aggName the aggregation name
     * @return stream of string term buckets, or empty stream if aggregation not found
     */
    public static <T> Stream<StringTermsBucket> stringTermsBuckets(SearchResponse<T> response, String aggName) {
        return Optional.ofNullable(response.aggregations())
                .map(aggs -> aggs.get(aggName))
                .filter(Aggregate::isSterms)
                .map(agg -> agg.sterms().buckets().array().stream())
                .orElse(Stream.empty());
    }

    /**
     * Gets long term buckets from a terms aggregation.
     *
     * @param response the search response
     * @param aggName the aggregation name
     * @return stream of long term buckets, or empty stream if aggregation not found
     */
    public static <T> Stream<LongTermsBucket> longTermsBuckets(SearchResponse<T> response, String aggName) {
        return Optional.ofNullable(response.aggregations())
                .map(aggs -> aggs.get(aggName))
                .filter(Aggregate::isLterms)
                .map(agg -> agg.lterms().buckets().array().stream())
                .orElse(Stream.empty());
    }

    /**
     * Gets the cardinality value from a cardinality aggregation.
     *
     * @param response the search response
     * @param aggName the aggregation name
     * @return the cardinality value, or empty if aggregation not found
     */
    public static <T> Optional<Long> cardinalityValue(SearchResponse<T> response, String aggName) {
        return Optional.ofNullable(response.aggregations())
                .map(aggs -> aggs.get(aggName))
                .filter(Aggregate::isCardinality)
                .map(agg -> agg.cardinality().value());
    }

    /**
     * Gets sub-aggregations from a filter aggregation.
     *
     * @param response the search response
     * @param aggName the aggregation name
     * @return the sub-aggregations map, or empty if aggregation not found
     */
    public static <T> Optional<Map<String, Aggregate>> filterAggregations(SearchResponse<T> response, String aggName) {
        return Optional.ofNullable(response.aggregations())
                .map(aggs -> aggs.get(aggName))
                .filter(Aggregate::isFilter)
                .map(agg -> agg.filter().aggregations());
    }
}
