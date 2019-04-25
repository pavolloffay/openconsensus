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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link MetricRegistry}. */
@RunWith(JUnit4.class)
public class MetricRegistryTest {
  @Rule public ExpectedException thrown = ExpectedException.none();

  private final MetricRegistry metricRegistry =
      NoopMetrics.newNoopMeter().buildMetricRegistry().build();

  @Test
  public void noopAddLongGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.longGaugeBuilder(null);
  }

  @Test
  public void noopAddDoubleGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.doubleGaugeBuilder(null);
  }

  @Test
  public void noopAddDerivedLongGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.derivedLongGaugeBuilder(null);
  }

  @Test
  public void noopAddDerivedDoubleGauge_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.derivedDoubleGaugeBuilder(null);
  }

  @Test
  public void noopAddDoubleCumulative_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.doubleCumulativeBuilder(null);
  }

  @Test
  public void noopAddDerivedDoubleCumulative_NullName() {
    thrown.expect(NullPointerException.class);
    thrown.expectMessage("name");
    metricRegistry.derivedDoubleCumulativeBuilder(null);
  }
}
