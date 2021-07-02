/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.sdk.extension.trace.jaeger.sampler;

import io.opentelemetry.sdk.autoconfigure.ConfigProperties;
import io.opentelemetry.sdk.autoconfigure.spi.ConfigurableSamplerProvider;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import java.util.concurrent.TimeUnit;

public class JaegerRemoteSamplerProvider implements ConfigurableSamplerProvider {

  @Override
  public Sampler createSampler(ConfigProperties config) {
    String serviceName = config.getString("jaeger.service");
    JaegerRemoteSamplerBuilder builder = JaegerRemoteSampler.builder().setServiceName(serviceName);

    // Optional configuration
    String endpoint = config.getString("jaeger.remote.sampler.endpoint");
    if (endpoint != null) {
      builder.setEndpoint(endpoint);
    }
    Double initialRate = config.getDouble("jaeger.remote.sampler.initial.rate");
    if (initialRate != null) {
      builder.setInitialSampler(Sampler.parentBased(Sampler.traceIdRatioBased(initialRate)));
    }
    Integer poolingInterval = config.getInt("jaeger.remote.sampler.pooling.interval");
    if (poolingInterval != null) {
      builder.setPollingInterval(poolingInterval, TimeUnit.MILLISECONDS);
    }

    return builder.build();
  }

  @Override
  public String getName() {
    return "jaeger_remote_sampler";
  }
}
