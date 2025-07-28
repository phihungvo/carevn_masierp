package com.carevn.masi.sdk;

import java.util.Map;

public interface VoipGraph {
    public Map sendRequest(String token, String path, Map params);
}
