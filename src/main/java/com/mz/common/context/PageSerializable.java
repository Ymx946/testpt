package com.mz.common.context;


import com.github.pagehelper.Page;

import java.io.Serializable;
import java.util.List;

public class PageSerializable<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    protected int total;
    protected List<T> list;

    public PageSerializable() {
    }

    public PageSerializable(List<T> list) {
        this.list = list;
        if (list instanceof Page) {
            this.total = Integer.valueOf(String.valueOf(((Page) list).getTotal()));
        } else {
            this.total = list.size();
        }

    }

//    public static <T> com.github.pagehelper.PageSerializable<T> of(List<T> list) {
//        return new com.github.pagehelper.PageSerializable(list);
//    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return this.list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String toString() {
        return "PageSerializable{total=" + this.total + ", list=" + this.list + '}';
    }
}
