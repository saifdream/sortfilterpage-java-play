package filter.template;


public abstract class FilterBase {

    public int currentPage = 1;

    public int rowsPerPage = 10;

    public int totalPageCount;

    public int totalRowsCount;

    public String displayCounter;

    public String sortColumnName = "id";

    public boolean isAscending = true;
}
