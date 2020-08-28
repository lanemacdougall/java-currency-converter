package com.lane_macdougall.responses;

import java.util.Map;


/* PURPOSE: JSON object returned by currencylayer API is mapped to an object of this class using the Jackson (databind)
 * ObjectMapper class. The desired value of the JSON object can then be retrieved using the object's corresponding
 * getter method.
 *
 * Each of the class' attributes corresponds to the returned JSON object's keys.
 */
public class CurrencyLayerApiResponse {

    private boolean success;
    private Map<String, Object> error;
    private String terms;
    private String privacy;
    private long timestamp;
    private String source;
    private Map<String, Double> quotes;

    /* Methods */
    public boolean isSuccess() { return success; }

    public void setSuccess(boolean success) { this.success = success; }

    public Map<String, Object> getError() { return error; }

    public void setError(Map<String, Object> error) { this.error = error; }

    public String getTerms() { return terms; }

    public void setTerms(String terms) { this.terms = terms; }

    public String getPrivacy() { return privacy; }

    public void setPrivacy(String privacy) { this.privacy = privacy; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getSource() { return source; }

    public void setSource(String source) { this.source = source; }

    public Map<String, Double> getQuotes() { return quotes; }

    public void setQuotes(Map<String, Double> quotes) { this.quotes = quotes; }

}
