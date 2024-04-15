package com.valeria.demo.db.entity;

public enum OrderState {
    WAITING, IN_PROCESSING, RESOLVED;
}

//WAITING(SUBMITTED)-ожидает принятия, IN_PROCESSING-доставляется, RESOLVED-доставлен