package org.opensearch.client.opensearch.core.helpers;

import org.junit.Test;
import org.opensearch.client.opensearch._types.FieldValue;
import org.opensearch.client.opensearch._types.aggregations.Aggregation;
import org.opensearch.client.opensearch._types.query_dsl.Query;
import org.opensearch.client.opensearch.core.SearchRequest;
import org.opensearch.client.opensearch.core.search.FieldCollapse;
import org.opensearch.client.opensearch.model.ModelTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Test that replicates customer's exact patterns from their documentation
 */
public class CustomerPatternTest extends ModelTestCase {

    // Simulate their SearchContext
    static class SearchContext {
        StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder();
        String catalogIndex = "products";
        String storeId = "123";
        String catalogId = "catalog-1";
        String groupId = "group-1";
        List<String> facetFilters = List.of("brand:Nike", "brand:Adidas");
    }

    // Simulate their pipeline steps
    interface QueryStep {
        void apply(SearchContext context);
    }

    @Test
    public void testCustomerPipeline() {
        SearchContext context = new SearchContext();

        // Their pipeline
        List<QueryStep> steps = List.of(
                new InitializeSearchRequestStep(),
                new AddBaseFiltersStep(),
                new AddCategoryFilterStep(),
                new AddFacetFiltersStep(),
                new AddAggregationsStep(),
                new AddCollapseStep(),
                new AddConditionalAggStep()
        );

        // Execute pipeline
        steps.forEach(step -> step.apply(context));

        // Build request
        SearchRequest request = context.builder.build();

        // Verify it worked
        assertNotNull(request);
        assertNotNull(request.query());
        assertNotNull(request.postFilter());
        assertFalse(request.aggregations().isEmpty());
        assertNotNull(request.collapse());
    }

    // Customer's step implementations
    static class InitializeSearchRequestStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            context.builder.index(context.catalogIndex);
        }
    }

    static class AddBaseFiltersStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            Query query = Query.of(q -> q.bool(b -> b
                    .filter(f -> f.exists(e -> e.field("price")))
                    .filter(f -> f.term(t -> t.field("store_id").value(FieldValue.of(context.storeId))))
                    .filter(f -> f.term(t -> t.field("published").value(FieldValue.of(true))))
            ));
            context.builder.query(query);
        }
    }

    static class AddCategoryFilterStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            // Read existing query
            Query existing = context.builder.getQuery();

            // Add category filter
            String catGroupId = context.catalogId + ":" + context.groupId;
            Query withCategory = Query.of(q -> q.bool(b -> {
                if (existing != null) {
                    b.must(existing);
                }
                b.filter(f -> f.term(t -> t.field("catgroup_id").value(FieldValue.of(catGroupId))));
                return b;
            }));

            context.builder.query(withCategory);
        }
    }

    static class AddFacetFiltersStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            List<Query> filters = new ArrayList<>();

            for (String facetFilter : context.facetFilters) {
                String[] parts = facetFilter.split(":");
                String field = parts[0];
                String value = parts[1];

                filters.add(Query.of(q -> q.term(t -> t
                        .field(field)
                        .value(FieldValue.of(value))
                )));
            }

            Query postFilter = Query.of(q -> q.bool(b -> {
                filters.forEach(b::filter);
                return b;
            }));

            context.builder.postFilter(postFilter);
        }
    }

    static class AddAggregationsStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            context.builder.addAggregation("brands", Aggregation.of(a -> a
                    .terms(t -> t.field("brand").size(10))
            ));
            context.builder.addAggregation("categories", Aggregation.of(a -> a
                    .terms(t -> t.field("category").size(10))
            ));
        }
    }

    static class AddCollapseStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            context.builder.collapse(FieldCollapse.of(c -> c
                    .field("parent_id")
                    .innerHits(ih -> ih.size(20))
            ));
        }
    }

    static class AddConditionalAggStep implements QueryStep {
        @Override
        public void apply(SearchContext context) {
            // Read state (like customer does)
            FieldCollapse collapse = context.builder.getCollapse();
            Query postFilter = context.builder.getPostFilter();

            // Add conditional aggregation
            if (collapse != null && postFilter != null) {
                context.builder.addAggregation("total_count", Aggregation.of(a -> a
                        .cardinality(c -> c.field(collapse.field()))
                ));
            }
        }
    }
}
