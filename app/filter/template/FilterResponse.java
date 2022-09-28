package filter.template;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;


public class FilterResponse {
//public class FilterResponse<T> {

    public int currentPage = 1;

    public int rowsPerPage = 10;

    public int totalPageCount;
    
    public int totalRowsCount;
    
    public String displayCounter;

    public String sortColumnName = "id";

    public boolean isAscending = true;

    public List<JsonNode> data;
}
