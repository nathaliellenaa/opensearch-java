package org.opensearch.client.opensearch.core.helpers;

import org.junit.Assert;
import org.junit.Test;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.search.FieldCollapse;

import java.util.Arrays;

public class StatefulSearchRequestBuilderTest extends Assert {

    @Test
    public void testPipelinePattern() {
        StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder()
                .index("products");

        addBaseFilters(builder);
        addFacetFilters(builder);
        addAggregations(builder);
        addCollapse(builder);
        addConditionalAggregation(builder);

        SearchRequest request = builder.build();

        assertNotNull(request);
        assertEquals(1, request.index().size());
        assertEquals("products", request.index().get(0));
        assertNotNull(request.query());
        assertNotNull(request.postFilter());
        assertEquals(3, request.aggregations().size());
        assertNotNull(request.collapse());
    }

    private void addBaseFilters(StatefulSearchRequestBuilder builder) {
        Query query = Query.of(q -> q.bool(b -> b
                .filter(f -> f.term(t -> t.field("status").value(FieldValue.of("active"))))
                .filter(f -> f.term(t -> t.field("store_id").value(FieldValue.of("123"))))
                .filter(f -> f.exists(e -> e.field("price")))
        ));
        builder.query(query);
    }

    private void addFacetFilters(StatefulSearchRequestBuilder builder) {
        Query postFilter = Query.of(q -> q.bool(b -> b
                .filter(f -> f.terms(t -> t
                        .field("brand")
                        .terms(tf -> tf.value(Arrays.asList(
                                FieldValue.of("Nike"),
                                FieldValue.of("Adidas")
                        )))
                ))
        ));
        builder.postFilter(postFilter);
    }

    private void addAggregations(StatefulSearchRequestBuilder builder) {
        builder.addAggregation("brands", Aggregation.of(a -> a
                .terms(t -> t.field("brand").size(10))
        ));
        builder.addAggregation("categories", Aggregation.of(a -> a
                .terms(t -> t.field("category").size(10))
        ));
    }

    private void addCollapse(StatefulSearchRequestBuilder builder) {
        builder.collapse(FieldCollapse.of(c -> c.field("parent_id")));
    }

    private void addConditionalAggregation(StatefulSearchRequestBuilder builder) {
        FieldCollapse collapse = builder.getCollapse();
        Query postFilter = builder.getPostFilter();

        if (collapse != null && postFilter != null) {
            builder.addAggregation("total_count", Aggregation.of(a -> a
                    .cardinality(c -> c.field(collapse.field()))
            ));
        }
    }

    @Test
    public void testReadingState() {
        StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder();

        Query query = Query.of(q -> q.term(t -> t.field("status").value(FieldValue.of("active"))));
        builder.query(query);

        Query retrieved = builder.getQuery();
        assertNotNull(retrieved);
        assertEquals(query, retrieved);
    }

    @Test
    public void testIncrementalBuilding() {
        StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder();

        builder.index("products");
        builder.from(10);
        builder.size(20);
        builder.minScore(0.5f);

        SearchRequest request = builder.build();

        assertEquals(Integer.valueOf(10), request.from());
        assertEquals(Integer.valueOf(20), request.size());
        assertEquals(0.5f, request.minScore(), 0.001);
    }
}
