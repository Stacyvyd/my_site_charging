package ru.msu.cmc.my_site.models;

public interface CommonEntity<ID> {
    ID getId();
    void setId(ID id);
}