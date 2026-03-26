/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * The OpenSearch Contributors require contributions made to
 * this file be licensed under the Apache-2.0 license or a
 * compatible open source license.
 */

package org.opensearch.client.opensearch.core.helpers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.opensearch.client.json.JsonData;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.SortOptions;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.query_dsl.FieldAndFormat;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.search.FieldCollapse;
import org.opensearch.client.opensearch.core.search.Highlight;
import org.opensearch.client.opensearch.core.search.SourceConfig;

/**
 * Stateful builder for {@link SearchRequest} that allows incremental construction
 * across multiple methods. Useful for migrating from RestHighLevelClient where
 * requests are built through mutation.
 *
 * <p>Example usage:
 * <pre>{@code
 * StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder()
 *     .index("products")
 *     .query(baseQuery);
 *
 * addFilters(builder);  // helper method can mutate builder
 * addAggregations(builder);  // another helper adds more
 *
 * SearchRequest request = builder.build();
 * }</pre>
 */
public class StatefulSearchRequestBuilder {

    private final List<String> index = new ArrayList<>();
    private Query query;
    private Query postFilter;
    private final Map<String, Aggregation> aggregations = new HashMap<>();
    private FieldCollapse collapse;
    private final List<SortOptions> sort = new ArrayList<>();
    private final List<FieldAndFormat> fields = new ArrayList<>();
    private final List<String> storedFields = new ArrayList<>();
    private SourceConfig source;
    private Highlight highlight;
    private Integer from;
    private Integer size;
    private Float minScore;
    private Integer terminateAfter;
    private String timeout;
    private Boolean trackScores;
    private String routing;
    private String preference;
    private String pipeline;
    private final Map<String, JsonData> ext = new HashMap<>();
    private final List<FieldValue> searchAfter = new ArrayList<>();
    private Boolean seqNoPrimaryTerm;

    /**
     * Sets the index or indices to search.
     */
    public StatefulSearchRequestBuilder index(String... indices) {
        this.index.addAll(Arrays.asList(indices));
        return this;
    }

    /**
     * Sets the query.
     */
    public StatefulSearchRequestBuilder query(Query query) {
        this.query = query;
        return this;
    }

    /**
     * Sets the post filter query.
     */
    public StatefulSearchRequestBuilder postFilter(Query postFilter) {
        this.postFilter = postFilter;
        return this;
    }

    /**
     * Adds an aggregation.
     */
    public StatefulSearchRequestBuilder addAggregation(String name, Aggregation aggregation) {
        this.aggregations.put(name, aggregation);
        return this;
    }

    /**
     * Sets the field collapse configuration.
     */
    public StatefulSearchRequestBuilder collapse(FieldCollapse collapse) {
        this.collapse = collapse;
        return this;
    }

    /**
     * Adds a sort option.
     */
    public StatefulSearchRequestBuilder addSort(SortOptions sortOption) {
        this.sort.add(sortOption);
        return this;
    }

    /**
     * Adds a field to retrieve.
     */
    public StatefulSearchRequestBuilder addField(FieldAndFormat field) {
        this.fields.add(field);
        return this;
    }

    /**
     * Sets the source filtering configuration.
     */
    public StatefulSearchRequestBuilder source(SourceConfig source) {
        this.source = source;
        return this;
    }

    /**
     * Sets the highlight configuration.
     */
    public StatefulSearchRequestBuilder highlight(Highlight highlight) {
        this.highlight = highlight;
        return this;
    }

    /**
     * Sets the starting offset for pagination.
     */
    public StatefulSearchRequestBuilder from(int from) {
        this.from = from;
        return this;
    }

    /**
     * Sets the number of hits to return.
     */
    public StatefulSearchRequestBuilder size(int size) {
        this.size = size;
        return this;
    }

    /**
     * Sets the minimum score threshold.
     */
    public StatefulSearchRequestBuilder minScore(float minScore) {
        this.minScore = minScore;
        return this;
    }

    /**
     * Sets the maximum number of documents to collect per shard.
     */
    public StatefulSearchRequestBuilder terminateAfter(int terminateAfter) {
        this.terminateAfter = terminateAfter;
        return this;
    }

    /**
     * Sets the search timeout.
     */
    public StatefulSearchRequestBuilder timeout(String timeout) {
        this.timeout = timeout;
        return this;
    }

    /**
     * Sets whether to calculate and return document scores.
     */
    public StatefulSearchRequestBuilder trackScores(boolean trackScores) {
        this.trackScores = trackScores;
        return this;
    }

    /**
     * Adds a stored field to retrieve.
     */
    public StatefulSearchRequestBuilder addStoredField(String field) {
        this.storedFields.add(field);
        return this;
    }

    /**
     * Sets the routing value.
     */
    public StatefulSearchRequestBuilder routing(@Nullable String routing) {
        this.routing = routing;
        return this;
    }

    /**
     * Sets the preference value (e.g. for session-based routing).
     */
    public StatefulSearchRequestBuilder preference(@Nullable String preference) {
        this.preference = preference;
        return this;
    }

    /**
     * Sets the search pipeline name.
     */
    public StatefulSearchRequestBuilder pipeline(@Nullable String pipeline) {
        this.pipeline = pipeline;
        return this;
    }

    /**
     * Adds an ext entry (e.g. for plugin-specific parameters like LTR logging).
     */
    public StatefulSearchRequestBuilder addExt(String key, JsonData value) {
        this.ext.put(key, value);
        return this;
    }

    /**
     * Adds search_after values for pagination.
     */
    public StatefulSearchRequestBuilder addSearchAfter(FieldValue... values) {
        this.searchAfter.addAll(Arrays.asList(values));
        return this;
    }

    /**
     * Sets whether to return sequence number and primary term.
     */
    public StatefulSearchRequestBuilder seqNoPrimaryTerm(boolean seqNoPrimaryTerm) {
        this.seqNoPrimaryTerm = seqNoPrimaryTerm;
        return this;
    }

    // Getters for reading state

    @Nullable
    public Query getQuery() {
        return query;
    }

    @Nullable
    public Query getPostFilter() {
        return postFilter;
    }

    public Map<String, Aggregation> getAggregations() {
        return aggregations;
    }

    @Nullable
    public FieldCollapse getCollapse() {
        return collapse;
    }

    public List<SortOptions> getSort() {
        return sort;
    }

    public List<String> getIndex() {
        return index;
    }

    @Nullable
    public SourceConfig getSource() {
        return source;
    }

    @Nullable
    public Float getMinScore() {
        return minScore;
    }

    public Map<String, JsonData> getExt() {
        return ext;
    }

    /**
     * Builds an immutable {@link SearchRequest} from the current state.
     */
    public SearchRequest build() {
        return SearchRequest.of(s -> {
            if (!index.isEmpty()) {
                s.index(index);
            }
            if (query != null) {
                s.query(query);
            }
            if (postFilter != null) {
                s.postFilter(postFilter);
            }
            if (!aggregations.isEmpty()) {
                s.aggregations(aggregations);
            }
            if (collapse != null) {
                s.collapse(collapse);
            }
            if (!sort.isEmpty()) {
                s.sort(sort);
            }
            if (!fields.isEmpty()) {
                s.fields(fields);
            }
            if (source != null) {
                s.source(source);
            }
            if (highlight != null) {
                s.highlight(highlight);
            }
            if (from != null) {
                s.from(from);
            }
            if (size != null) {
                s.size(size);
            }
            if (minScore != null) {
                s.minScore(minScore);
            }
            if (terminateAfter != null) {
                s.terminateAfter(terminateAfter);
            }
            if (timeout != null) {
                s.timeout(timeout);
            }
            if (trackScores != null) {
                s.trackScores(trackScores);
            }
            if (!storedFields.isEmpty()) {
                s.storedFields(storedFields);
            }
            if (routing != null) {
                s.routing(routing);
            }
            if (preference != null) {
                s.preference(preference);
            }
            if (pipeline != null) {
                s.pipeline(pipeline);
            }
            if (!ext.isEmpty()) {
                s.ext(ext);
            }
            if (!searchAfter.isEmpty()) {
                s.searchAfter(searchAfter);
            }
            if (seqNoPrimaryTerm != null) {
                s.seqNoPrimaryTerm(seqNoPrimaryTerm);
            }
            return s;
        });
    }
}
