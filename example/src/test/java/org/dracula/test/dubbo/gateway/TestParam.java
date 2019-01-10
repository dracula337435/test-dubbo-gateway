package org.dracula.test.dubbo.gateway;

import java.io.Serializable;

/**
 * @author dk
 */
public class TestParam implements Serializable {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
