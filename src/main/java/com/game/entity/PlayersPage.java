package com.game.entity;

import org.springframework.data.domain.Sort;

public class PlayersPage {
    private int pageNumber;
    private int pageSize;
    private Sort.Direction sortDirection;

    public PlayersPage(int pageNumber, int pageSize, Sort.Direction sortDirection) {
        this.pageNumber = pageNumber;

        if (pageSize == 0){
            this.pageSize = 3;}
        else this.pageSize = pageSize;

        if (sortDirection == null){
            this.sortDirection = Sort.Direction.ASC;
        } else this.sortDirection = sortDirection;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Sort.Direction getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(Sort.Direction sortDirection) {
        this.sortDirection = sortDirection;
    }

}
