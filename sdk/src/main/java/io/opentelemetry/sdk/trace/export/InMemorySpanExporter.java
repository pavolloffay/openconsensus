/*
 * Copyright 2019, OpenTelemetry Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.opentelemetry.sdk.trace.export;

import io.opentelemetry.proto.trace.v1.Span;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link SpanExporter} implementation that can be used to test OpenTelemetry integration.
 *
 * <p>Example usage:
 *
 * <pre><code>
 * class MyClassTest {
 *   private final Tracer tracer = new TracerSdk();
 *   private final InMemorySpanExporter testExporter = InMemorySpanExporter.create();
 *
 *   {@literal @}Before
 *   public void setup() {
 *     tracer.addSpanProcessor(SimpleSampledSpansProcessor.newBuilder(testExporter).build());
 *   }
 *
 *   {@literal @}Test
 *   public void getFinishedSpanData() {
 *     tracer.spanBuilder("span").startSpan().end();
 *
 *     {@code List<Span> spanItems} = exporter.getFinishedSpanItems();
 *     assertThat(spanItems).isNotNull();
 *     assertThat(spanItems.size()).isEqualTo(1);
 *     assertThat(spanItems.get(0).getName()).isEqualTo("span");
 *   }
 * </code></pre>
 */
public final class InMemorySpanExporter implements SpanExporter {
  private final List<Span> finishedSpanDataItems = new ArrayList<>();
  private boolean isStopped = false;

  /**
   * Returns a new instance of the {@code InMemorySpanExporter}.
   *
   * @return a new instance of the {@code InMemorySpanExporter}.
   */
  public static InMemorySpanExporter create() {
    return new InMemorySpanExporter();
  }

  /**
   * Returns a {@code List} of the finished {@code Span}s, represented by {@code
   * io.opentelemetry.proto.trace.v1.Span}.
   *
   * @return a {@code List} of the finished {@code Span}s.
   */
  public List<Span> getFinishedSpanItems() {
    synchronized (this) {
      return Collections.unmodifiableList(new ArrayList<>(finishedSpanDataItems));
    }
  }

  /**
   * Clears the internal {@code List} of finished {@code Span}s.
   *
   * <p>Does not reset the state of this exporter if already shutdown.
   */
  public void reset() {
    synchronized (this) {
      finishedSpanDataItems.clear();
    }
  }

  @Override
  public void export(List<Span> spans) {
    synchronized (this) {
      if (!isStopped) {
        finishedSpanDataItems.addAll(spans);
      }
    }
  }

  @Override
  public void shutdown() {
    synchronized (this) {
      finishedSpanDataItems.clear();
      isStopped = true;
    }
  }

  private InMemorySpanExporter() {}
}
