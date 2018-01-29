package cn.chenny3.secondHand.common.bean.dto;

import java.util.List;

/**
 * Bootstrap Table pagination Result
 * bootstrap table 专用分页结果包装类
 */
public class PaginationResult<T> {
    private int total;
    private List<T> rows;

    public PaginationResult() {
    }

    public PaginationResult(int total, List<T> rows) {
        this.total = total;
        this.rows = rows;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }
}
