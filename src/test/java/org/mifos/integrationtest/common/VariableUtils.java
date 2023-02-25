package org.mifos.integrationtest.common;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "channel-connector.collection")
public class VariableUtils {

    private List<Integer> values;

    public List<Integer> getValues() {
        return values;
    }

    public void setValues(List<Integer> values) {
        this.values = values;

    }
}
