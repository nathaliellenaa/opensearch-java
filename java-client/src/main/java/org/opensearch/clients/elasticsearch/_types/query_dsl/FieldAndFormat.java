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

package org.opensearch.clients.elasticsearch._types.query_dsl;

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
import javax.annotation.Nullable;

// typedef: _types.query_dsl.FieldAndFormat

/**
 * A reference to a field with formatting instructions on how to return the
 * value
 * 
 * @see <a href=
 *      "https://github.com/elastic/elasticsearch-specification/tree/98036c3/specification/_types/query_dsl/abstractions.ts#L204-L218">API
 *      specification</a>
 */
@JsonpDeserializable
public class FieldAndFormat implements JsonpSerializable {
	private final String field;

	@Nullable
	private final String format;

	@Nullable
	private final Boolean includeUnmapped;

	// ---------------------------------------------------------------------------------------------

	private FieldAndFormat(Builder builder) {

		this.field = ApiTypeHelper.requireNonNull(builder.field, this, "field");
		this.format = builder.format;
		this.includeUnmapped = builder.includeUnmapped;

	}

	public static FieldAndFormat of(Function<Builder, ObjectBuilder<FieldAndFormat>> fn) {
		return fn.apply(new Builder()).build();
	}

	/**
	 * Required - Wildcard pattern. The request returns values for field names
	 * matching this pattern.
	 * <p>
	 * API name: {@code field}
	 */
	public final String field() {
		return this.field;
	}

	/**
	 * Format in which the values are returned.
	 * <p>
	 * API name: {@code format}
	 */
	@Nullable
	public final String format() {
		return this.format;
	}

	/**
	 * API name: {@code include_unmapped}
	 */
	@Nullable
	public final Boolean includeUnmapped() {
		return this.includeUnmapped;
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

		generator.writeKey("field");
		generator.write(this.field);

		if (this.format != null) {
			generator.writeKey("format");
			generator.write(this.format);

		}
		if (this.includeUnmapped != null) {
			generator.writeKey("include_unmapped");
			generator.write(this.includeUnmapped);

		}

	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Builder for {@link FieldAndFormat}.
	 */

	public static class Builder extends ObjectBuilderBase implements ObjectBuilder<FieldAndFormat> {
		private String field;

		@Nullable
		private String format;

		@Nullable
		private Boolean includeUnmapped;

		/**
		 * Required - Wildcard pattern. The request returns values for field names
		 * matching this pattern.
		 * <p>
		 * API name: {@code field}
		 */
		public final Builder field(String value) {
			this.field = value;
			return this;
		}

		/**
		 * Format in which the values are returned.
		 * <p>
		 * API name: {@code format}
		 */
		public final Builder format(@Nullable String value) {
			this.format = value;
			return this;
		}

		/**
		 * API name: {@code include_unmapped}
		 */
		public final Builder includeUnmapped(@Nullable Boolean value) {
			this.includeUnmapped = value;
			return this;
		}

		/**
		 * Builds a {@link FieldAndFormat}.
		 *
		 * @throws NullPointerException
		 *             if some of the required fields are null.
		 */
		public FieldAndFormat build() {
			_checkSingleUse();

			return new FieldAndFormat(this);
		}
	}

	// ---------------------------------------------------------------------------------------------

	/**
	 * Json deserializer for {@link FieldAndFormat}
	 */
	public static final JsonpDeserializer<FieldAndFormat> _DESERIALIZER = ObjectBuilderDeserializer.lazy(Builder::new,
			FieldAndFormat::setupFieldAndFormatDeserializer);

	protected static void setupFieldAndFormatDeserializer(ObjectDeserializer<FieldAndFormat.Builder> op) {

		op.add(Builder::field, JsonpDeserializer.stringDeserializer(), "field");
		op.add(Builder::format, JsonpDeserializer.stringDeserializer(), "format");
		op.add(Builder::includeUnmapped, JsonpDeserializer.booleanDeserializer(), "include_unmapped");

		op.shortcutProperty("field");

	}

}
