package com.example.reach;

/**
 * Event Object Class definition
 * @Author Jordan Harris
 */
public class Item {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private String mText3;

    /**
     * Event Object constructor
     * @param imageResource event logo
     * @param text1 eventID + event name string
     * @param text2 event location string
     * @param text3 event id string
     */
    public Item(int imageResource, String text1, String text2, String text3) {
        mImageResource = imageResource;
        mText1 = text1;
        mText2 = text2;
        mText3 = text3;
    }

    /**
     *
     * @return int event_icon
     */
    public int getImageResource() {
        return mImageResource;
    }

    /**
     *
     * @return string event name
     */
    public String getText1() {
        return mText1;
    }

    /**
     *
     * @return event location stirng
     */
    public String getText2() {
        return mText2;
    }

    /**
     *
     * @return event id
     */
    public String getText3(){ return mText3; }
}
