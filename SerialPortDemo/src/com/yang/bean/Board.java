package com.yang.bean;

import java.util.Date;

public class Board implements Comparable {

    private Integer id;
    private Integer isCall;
    private Date time;

    public Board() {
    }

    public Board(Integer id, Integer isCall, Date time) {
        this.id = id;
        this.isCall = isCall;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsCall() {
        return isCall;
    }

    public void setIsCall(Integer isCall) {
        this.isCall = isCall;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Board{" +
                "id=" + id +
                ", isCall=" + isCall +
                ", time=" + time +
                '}';
    }

    public int compareTo(Object o) {
        Date date1 = this.getTime();
        Date date2 = ((Board)o).getTime();
        return date1.compareTo(date2);
    }
}