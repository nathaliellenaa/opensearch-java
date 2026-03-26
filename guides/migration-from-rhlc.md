# Migrating from RestHighLevelClient to opensearch-java

This guide helps teams migrate from the legacy `RestHighLevelClient` (RHLC) to the new `opensearch-java` client, especially codebases that rely on mutable request building patterns.

## Quick Start: Testing with the Feature Branch

### 1. Clone and publish to local Maven

```bash
git clonehttps://github.com/nathaliellenaa/opensearch-java.git
cd opensearch-java
git checkout rhlc_migration

# Publish to local Maven repository
./gradlew :java-client:publishToMavenLocal
```

### 2. Update your project dependency

**Gradle (Kotlin DSL):**
```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation("org.opensearch.client:opensearch-java:4.0.0-SNAPSHOT")
}
```

**Gradle (Groovy):**
```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'org.opensearch.client:opensearch-java:4.0.0-SNAPSHOT'
}
```

**Maven:**
```xml
<dependency>
    <groupId>org.opensearch.client</groupId>
    <artifactId>opensearch-java</artifactId>
    <version>4.0.0-SNAPSHOT</version>
</dependency>
```

### 3. Verify it works

```java
import org.opensearch.client.opensearch.core.helpers.StatefulSearchRequestBuilder;

StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder();
builder.index("my-index");
// If this compiles, you're using the right version
```

---

## Core Concepts

The RHLC uses mutable builders (`SearchRequest`, `SearchSourceBuilder`, `BoolQueryBuilder`) that you create once and mutate across multiple methods. The new `opensearch-java` client uses immutable request objects built via functional builders.

The `StatefulSearchRequestBuilder` bridges this gap: it provides a mutable object you can pass through your existing pipeline, then produces an immutable `SearchRequest` at the end.

---

## Migration Patterns

### Pattern 1: Pipeline-based request construction

**Before (RHLC):**
```kotlin
// Step 1
val source = SearchSourceBuilder()
context.esRequest = SearchRequest(catalogIndex).source(source)

// Step 2 — mutates the same request
context.esRequest.source().query(boolQuery)

// Step 3 — mutates again
context.esRequest.source().postFilter(postFilterQuery)

// Step 4 — reads state back, then mutates
context.esRequest.source().aggregation(someAgg)

// Execute
client.search(context.esRequest, RequestOptions.DEFAULT)
```

**After (opensearch-java):**
```java
import org.opensearch.client.opensearch.core.helpers.StatefulSearchRequestBuilder;

// Step 1
StatefulSearchRequestBuilder builder = new StatefulSearchRequestBuilder()
    .index(catalogIndex);

// Step 2
builder.query(boolQuery);

// Step 3
builder.postFilter(postFilterQuery);

// Step 4
builder.addAggregation("my_agg", someAgg);

// Build immutable request and execute
SearchRequest request = builder.build();
SearchResponse<MyDoc> response = client.search(request, MyDoc.class);
```

### Pattern 2: Mutator helper methods

**Before (RHLC):**
```kotlin
fun BoolQueryBuilder.baseRequestFilters(request: SearchRequestInternal): BoolQueryBuilder {
    this.filter(QueryBuilders.existsQuery("price"))
        .filter(QueryBuilders.termQuery("store_id", request.storeId))
        .filter(QueryBuilders.termQuery("published", true))
        .mustNot(QueryBuilders.rangeQuery("start_date").gte("now+1d/d"))
        .mustNot(QueryBuilders.termQuery("search_suppressed", true))
    return this
}
```

**After (opensearch-java):**
```java
// Option A: Return a Query object (preferred — pure factory)
static Query baseRequestFilters(String storeId) {
    return Query.of(q -> q.bool(b -> b
        .filter(f -> f.exists(e -> e.field("price")))
        .filter(f -> f.term(t -> t.field("store_id").value(FieldValue.of(storeId))))
        .filter(f -> f.term(t -> t.field("published").value(FieldValue.of(true))))
        .mustNot(mn -> mn.range(r -> r.field("start_date").gte(JsonData.of("now+1d/d"))))
        .mustNot(mn -> mn.term(t -> t.field("search_suppressed").value(FieldValue.of(true))))
    ));
}

// Then set it on the builder
builder.query(baseRequestFilters(request.getStoreId()));
```

```java
// Option B: Mutate the builder directly (easier migration, less refactoring)
static void addBaseFilters(StatefulSearchRequestBuilder builder, String storeId) {
    builder.query(Query.of(q -> q.bool(b -> b
        .filter(f -> f.exists(e -> e.field("price")))
        .filter(f -> f.term(t -> t.field("store_id").value(FieldValue.of(storeId))))
        .filter(f -> f.term(t -> t.field("published").value(FieldValue.of(true))))
        .mustNot(mn -> mn.range(r -> r.field("start_date").gte(JsonData.of("now+1d/d"))))
        .mustNot(mn -> mn.term(t -> t.field("search_suppressed").value(FieldValue.of(true))))
    )));
}
```

### Pattern 3: Reading back state from the request

**Before (RHLC):**
```kotlin
fun SearchRequest.appendTotalCountAgg(requireCollapse: Boolean) {
    val collapse = this.source().collapse()  // read existing collapse
    val postFilter = this.source().postFilter()  // read existing post-filter

    val cardinalityAgg = AggregationBuilders.cardinality("total_count")
        .field(collapse.field)

    val filterAgg = AggregationBuilders.filter("total_count", postFilter)
        .subAggregation(cardinalityAgg)

    this.source().aggregation(filterAgg)
}
```

**After (opensearch-java):**
```java
static void appendTotalCountAgg(StatefulSearchRequestBuilder builder) {
    FieldCollapse collapse = builder.getCollapse();  // read existing collapse
    Query postFilter = builder.getPostFilter();       // read existing post-filter

    if (collapse != null && postFilter != null) {
        Aggregation cardinalityAgg = Aggregation.of(a -> a
            .cardinality(c -> c.field(collapse.field()))
        );

        // Add as separate aggregations (filter + cardinality)
        builder.addAggregation("total_count_filter", Aggregation.of(a -> a
            .filter(postFilter)
        ));
        builder.addAggregation("total_count", cardinalityAgg);
    }
}
```

### Pattern 4: Collapse with sort reading

**Before (RHLC):**
```kotlin
fun setToEsRequest(request: SearchRequestInternal, esRequest: SearchRequest) {
    val innerQuery = InnerHitBuilder()
    request.groupSort?.let {
        val currentSort = esRequest.source().sorts() ?: emptyList()  // reads existing sorts
        val groupSort = listOf(SortBuilders.fieldSort(request.groupSort).order(SortOrder.ASC))
        innerQuery.setSorts(groupSort + currentSort)
    }
    innerQuery.size = 20
    innerQuery.name = "sku_collapse"

    esRequest.source().collapse(
        CollapseBuilder("parent_id").setInnerHits(innerQuery)
    )
}
```

**After (opensearch-java):**
```java
static void addCollapse(StatefulSearchRequestBuilder builder, String groupSort) {
    List<SortOptions> currentSort = builder.getSort();  // read existing sorts

    List<SortOptions> innerSorts = new ArrayList<>();
    if (groupSort != null) {
        innerSorts.add(SortOptions.of(s -> s.field(f -> f.field(groupSort).order(SortOrder.Asc))));
        innerSorts.addAll(currentSort);
    }

    List<SortOptions> finalInnerSorts = innerSorts;
    builder.collapse(FieldCollapse.of(c -> c
        .field("parent_id")
        .innerHits(ih -> ih
            .name("sku_collapse")
            .size(20)
            .sort(finalInnerSorts)
        )
    ));
}
```

### Pattern 5: Facet filters with post-filter

**Before (RHLC):**
```kotlin
val postFilterQuery = QueryBuilders.boolQuery()
for ((facetName, facetValues) in filters) {
    postFilterQuery.filter(QueryBuilders.termsQuery(facet.indexField, facetValues))
}
esRequest.source().postFilter(postFilterQuery)
```

**After (opensearch-java):**
```java
List<Query> filterQueries = new ArrayList<>();
for (Map.Entry<String, List<String>> entry : filters.entrySet()) {
    String field = entry.getKey();
    List<FieldValue> values = entry.getValue().stream()
        .map(FieldValue::of)
        .toList();
    filterQueries.add(Query.of(q -> q.terms(t -> t
        .field(field)
        .terms(tf -> tf.value(values))
    )));
}

builder.postFilter(Query.of(q -> q.bool(b -> {
    filterQueries.forEach(b::filter);
    return b;
})));
```

### Pattern 6: Plugin ext support (LTR logging)

**Before (RHLC):**
```kotlin
fun SearchSourceBuilder.addFeatureLogging(): SearchSourceBuilder {
    this.ext(listOf(
        LoggingSearchExtBuilder().name("ltr_features").namedQuery("logged_features")
    ))
    return this
}
```

**After (opensearch-java):**
```java
Map<String, Object> logSpecs = Map.of(
    "name", "ltr_features",
    "named_query", "logged_features"
);
Map<String, Object> ltrLog = Map.of("log_specs", logSpecs);

builder.addExt("ltr_log", JsonData.of(ltrLog));
```

### Pattern 7: Session-based routing

**Before (RHLC):**
```kotlin
esRequest.preference(sessionId)
```

**After (opensearch-java):**
```java
builder.preference(sessionId);
```

### Pattern 8: Raw JSON / wrapper queries (KNN, plugin DSL)

**Before (RHLC):**
```kotlin
val knnQuery = """
    { "knn": { "embedding_field": { "vector": $vector, "k": $k } } }
""".trimIndent()
QueryBuilders.wrapperQuery(knnQuery)
```

**After (opensearch-java):**

For queries that the client doesn't natively support (like `template` or custom plugin queries), use the low-level `RestClient` to send raw JSON:

```java
RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200)).build();

String queryJson = "{ \"query\": { \"knn\": { ... } } }";
Request request = new Request("POST", "/my-index/_search");
request.setJsonEntity(queryJson);
Response response = restClient.performRequest(request);
```

---

## Response Parsing

### Before (RHLC):
```kotlin
val aggregations = esResponse.aggregations.asMap()
val termsAgg = aggregations["brands"] as ParsedTerms
termsAgg.buckets.forEach { bucket ->
    println("${bucket.keyAsString}: ${bucket.docCount}")
}

val cardinalityAgg = aggregations["total_count"] as ParsedCardinality
println("Total: ${cardinalityAgg.value}")
```

### After (opensearch-java):
```java
import static org.opensearch.client.opensearch.core.helpers.SearchResponseHelper.*;

// String terms buckets
stringTermsBuckets(response, "brands").forEach(bucket -> {
    System.out.println(bucket.key() + ": " + bucket.docCount());
});

// Long terms buckets
longTermsBuckets(response, "numeric_agg").forEach(bucket -> {
    System.out.println(bucket.key() + ": " + bucket.docCount());
});

// Cardinality
cardinalityValue(response, "total_count").ifPresent(count -> {
    System.out.println("Total: " + count);
});

// Filter aggregation with sub-aggregations
filterAggregations(response, "filtered_agg").ifPresent(subAggs -> {
    // Access sub-aggregations
});
```

---

## Transport Migration

### Before (RHLC):
```kotlin
val client = RestHighLevelClient(
    RestClient.builder(HttpHost("localhost", 9200, "http"))
)

// Async with coroutines
suspend fun search(request: SearchRequest): SearchResponse =
    suspendCancellableCoroutine { cont ->
        client.searchAsync(request, RequestOptions.DEFAULT,
            object : ActionListener<SearchResponse> {
                override fun onResponse(response: SearchResponse) = cont.resume(response)
                override fun onFailure(e: Exception) = cont.resumeWithException(e)
            }
        )
    }
```

### After (opensearch-java):
```java
final OpenSearchTransport transport = ApacheHttpClient5TransportBuilder
    .builder(new HttpHost("http", "localhost", 9200))
    .setMapper(new JacksonJsonpMapper())
    .build();
OpenSearchClient client = new OpenSearchClient(transport);

// Async client
OpenSearchAsyncClient asyncClient = new OpenSearchAsyncClient(transport);
CompletableFuture<SearchResponse<MyDoc>> future = asyncClient.search(request, MyDoc.class);
```

For Kotlin coroutines, wrap the `CompletableFuture`:
```kotlin
suspend fun <T> CompletableFuture<T>.await(): T = suspendCancellableCoroutine { cont ->
    this.whenComplete { result, error ->
        if (error != null) cont.resumeWithException(error)
        else cont.resume(result)
    }
    cont.invokeOnCancellation { this.cancel(true) }
}

// Usage
val response = asyncClient.search(request, MyDoc::class.java).await()
```

---

## StatefulSearchRequestBuilder API Reference

| Method | Description | Readable via |
|--------|-------------|--------------|
| `index(String...)` | Set indices to search | `getIndex()` |
| `query(Query)` | Set the query | `getQuery()` |
| `postFilter(Query)` | Set the post-filter | `getPostFilter()` |
| `addAggregation(String, Aggregation)` | Add a named aggregation | `getAggregations()` |
| `collapse(FieldCollapse)` | Set field collapse | `getCollapse()` |
| `addSort(SortOptions)` | Add a sort option | `getSort()` |
| `addField(FieldAndFormat)` | Add a fetch field | — |
| `addStoredField(String)` | Add a stored field | — |
| `source(SourceConfig)` | Set source filtering | `getSource()` |
| `highlight(Highlight)` | Set highlight config | — |
| `from(int)` | Set pagination offset | — |
| `size(int)` | Set result count | — |
| `minScore(float)` | Set minimum score | `getMinScore()` |
| `terminateAfter(int)` | Set max docs per shard | — |
| `timeout(String)` | Set search timeout | — |
| `trackScores(boolean)` | Enable score tracking | — |
| `routing(String)` | Set routing value | — |
| `preference(String)` | Set preference (session routing) | — |
| `pipeline(String)` | Set search pipeline | — |
| `addExt(String, JsonData)` | Add ext entry (plugin params) | `getExt()` |
| `addSearchAfter(FieldValue...)` | Add search_after values | — |
| `seqNoPrimaryTerm(boolean)` | Return seq_no and primary_term | — |