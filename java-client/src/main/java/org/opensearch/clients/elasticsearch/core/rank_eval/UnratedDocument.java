/*
 * Licensed to Elasticsearch B.V. under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch B.V. licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

//----------------------------------------------------
// THIS CODE IS GENERATED. MANUAL EDITS WILL BE LOST.
//----------------------------------------------------

package org.opensearch.clients.elasticsearch.core.rank_eval;

import org.opensearch.clients.json.JsonpDeserializable;
import org.opensearch.clients.json.JsonpDeserializer;
import org.opensearch.clients.json.JsonpMapper;
import org.opensearch.clients.json.JsonpSerializable;
import org.opensearch.clients.json.ObjectBuilderDeserializer;
import org.opensearch.clients.json.ObjectDeserializer;
import org.opensearch.clients.util.ApiTypeHelper;
import org.opensearch.clients.util.ObjectBuilder;
import org.opensearch.clients.util.ObjectBuilderBase;
import jakarta.json.stream.JsonGenerator;
import java.util.function.Function;

// typedef: _global.rank_eval.UnratedDocument

/**
 *
 * @see <a href=
 *      "https://github.com/elastic/elasticsearch-specification/tree/98036c3/specification/_global/rank_eval/types.ts#L148-L151">API
 *      specification</a>
 */
@JsonpDeserializable
public class UnratedDocument implements JsonpSerializable {
	private final String id;

	private final String index;

	// ---------------------------------------------------------------------------------------------

	private UnratedDocument(Builder builder) {

		this.id = ApiTypeHelper.requireNonNull(builder.id, this, "id");
		this.index = ApiTypeHelper.requireNonNull(builder.index, this, "index");

	}

	public static UnratedDocument of(Function<Builder, ObjectBuilder<UnratedDocument>> fn) {
		return fn.apply(new Builder()).build();
	}

	/**
	 * Required - API name: {@code _id}
	 */
	public final String id() {
		return this.id;
	}

	/**
	 * Required - API name: {@code _index}
	 */
	public final String index() {
		return this.index;
	}

	/**
	 * Serialize this object to JSON.
	 */
	public void serialize(JsonGenerator generator, JsonpMapper mapper) {
		generator.writeStartObject();
		serializeInternal(generator, mapper);
		generator.writeEnd();
	}

	protected void serializeInternal(JsonGenerator generator, JsonpMapper mapper) {

		generator.writeKey("_id");
		generator.write(this.id);

		generator.writeKey("_index");
		generator.write(this.index);

	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Builder for {@link UnratedDocument}.
	 */

	public static class Builder extends ObjectBuilderBase implements ObjectBuilder<UnratedDocument> {
		private String id;

		private String index;

		/**
		 * Required - API name: {@code _id}
		 */
		public final Builder id(String value) {
			this.id = value;
			return this;
		}

		/**
		 * Required - API name: {@code _index}
		 */
		public final Builder index(String value) {
			this.index = value;
			return this;
		}

		/**
		 * Builds a {@link UnratedDocument}.
		 *
		 * @throws NullPointerException
		 *             if some of the required fields are null.
		 */
		public UnratedDocument build() {
			_checkSingleUse();

			return new UnratedDocument(this);
		}
	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Json deserializer for {@link UnratedDocument}
	 */
	public static final JsonpDeserializer<UnratedDocument> _DESERIALIZER = ObjectBuilderDeserializer.lazy(Builder::new,
			UnratedDocument::setupUnratedDocumentDeserializer);

	protected static void setupUnratedDocumentDeserializer(ObjectDeserializer<UnratedDocument.Builder> op) {

		op.add(Builder::id, JsonpDeserializer.stringDeserializer(), "_id");
		op.add(Builder::index, JsonpDeserializer.stringDeserializer(), "_index");

	}

}
