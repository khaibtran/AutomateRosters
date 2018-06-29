package org.ccs.rosters.schoolrunner.service;

import org.apache.http.NameValuePair;

import java.util.List;

public interface ISRService {

    String request(String endpoint, List<NameValuePair> parameters);
}
