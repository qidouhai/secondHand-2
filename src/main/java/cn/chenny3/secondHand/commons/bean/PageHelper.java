package cn.chenny3.secondHand.commons.bean;

import java.util.List;

public class PageHelper<T> {
    private int first=1;
    private int pre;
    private int curPage;
    private int next;
    private int last;
    private int pageSize;
    private int count;
    private List<T> contents;


    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getPre() {
        return curPage>1?curPage-1:first;
    }

    public void setPre(int pre) {
        this.pre = pre;
    }

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getNext() {
        return curPage<getLast()?curPage+1:getLast();
    }

    public void setNext(int next) {
        this.next = next;
    }

    public int getLast() {
        return (int)Math.ceil(count*1.0/pageSize);
    }

    public void setLast(int last) {
        this.last = last;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getContents() {
        return contents;
    }

    public void setContents(List<T> contents) {
        this.contents = contents;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
