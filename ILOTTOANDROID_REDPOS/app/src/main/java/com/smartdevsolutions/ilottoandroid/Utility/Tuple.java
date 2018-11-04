package com.smartdevsolutions.ilottoandroid.Utility;

public class Tuple<X,Y> {
    private X _x;
    private Y _y;

    public Tuple(X item1, Y item2) {
        this._x = item1;
        this._y = item2;
    }

    public X item1() {
        return _x;
    }

    public void setItem1(X item1) {
        this._x = item1;
    }

    public Y item2() {
        return _y;
    }

    public void setItem2(Y item2) {
        this._y = item2;
    }
}
