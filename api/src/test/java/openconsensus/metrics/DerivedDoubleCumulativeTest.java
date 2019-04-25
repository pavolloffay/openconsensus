/*
 * Copyright 2019, OpenConsensus Authors
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

package openconsensus.metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link DerivedDoubleCumulative}. */
@RunWith(JUnit4.class)
public class DerivedDoubleCumulativeTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  private static final String NAME = "name";
  private static final String DESCRIPTION = "description";
  private static final String UNIT = "1";
  private static final List<LabelKey> LABEL_KEY =
      Collections.singletonList(LabelKey.create("key", "key description"));
  private static final List<LabelValue> LABEL_VALUES =
      Collections.singletonList(LabelValue.create("value"));
  private static final List<LabelValue> EMPTY_LABEL_VALUES = new ArrayList<LabelValue>();

  private final MetricRegistry metricRegistry =
      NoopMetrics.newNoopMeter().buildMetricRegistry().build();
  private final DerivedDoubleCumulative derivedDoubleCumulative =
      metricRegistry
          .derivedDoubleCumulativeBuilder(NAME)
          .setDescription(DESCRIPTION)
          .setLabelKeys(LABEL_KEY)
          .setUnit(UNIT)
          .build();
  private static final ToDoubleFunction<Object> doubleFunction =
      new ToDoubleFunction<Object>() {
        @Override
        public double applyAsDouble(Object value) {
          return 5.0;
        }
      };

  @Test
  public void noopCreateTimeSeries_WithNullLabelValues() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("labelValues");
    derivedDoubleCumulative.createTimeSeries(null, null, doubleFunction);
  }

  @Test
  public void noopCreateTimeSeries_WithNullElement() {
    List<LabelValue> labelValues = Collections.singletonList(null);
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("labelValue");
    derivedDoubleCumulative.createTimeSeries(labelValues, null, doubleFunction);
  }

  @Test
  public void noopCreateTimeSeries_WithInvalidLabelSize() {
    thrown.expect(IllegalArgumentException.class);
    thrown.expectMessage("Label Keys and Label Values don't have same size.");
    derivedDoubleCumulative.createTimeSeries(EMPTY_LABEL_VALUES, null, doubleFunction);
  }

  @Test
  public void createTimeSeries_WithNullFunction() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("function");
    derivedDoubleCumulative.createTimeSeries(LABEL_VALUES, null, null);
  }

  @Test
  public void noopRemoveTimeSeries_WithNullLabelValues() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("labelValues");
    derivedDoubleCumulative.removeTimeSeries(null);
  }
}
