package net.bluehack.kiosk.api;

import android.support.v4.util.Pair;

import java.util.ArrayList;

public class GetMethodParam {

    private ArrayList<Pair<String, String>> params;

    public GetMethodParam(Builder builder) {
        this.params = builder.params;
    }

    public ArrayList<Pair<String, String>> getParams() {
        return params;
    }

    public static class Builder {
        private transient ArrayList<Pair<String, String>> params = new ArrayList<>();

        public Builder addParam(String key, String value) {
            params.add(new Pair<String, String>(key, value));
            return this;
        }

        public GetMethodParam build() {
            return new GetMethodParam(this);
        }
    }
}
