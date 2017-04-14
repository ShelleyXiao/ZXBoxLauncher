package com.zx.zxboxlauncher.bean;

/**
 * User: ShaudXiao
 * Date: 2017-04-14
 * Time: 10:39
 * Company: zx
 * Description: Launcher 桌面 item
 * FIXME
 */


public class Item {

    private String title;
    private String url;
    private String islink;
    private String link;
    private String pkg;
    private String act;
    private String tag;
    private String icon;
    private String ishold;
    private String islock;
    private String way;
    private String wayval;
    private String imgurl;
    private int id = 0;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIslink() {
        return islink;
    }

    public void setIslink(String islink) {
        this.islink = islink;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getIshold() {
        return ishold;
    }

    public void setIshold(String ishold) {
        this.ishold = ishold;
    }

    public String getIslock() {
        return islock;
    }

    public void setIslock(String islock) {
        this.islock = islock;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }

    public String getWayval() {
        return wayval;
    }

    public void setWayval(String wayval) {
        this.wayval = wayval;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", islink='" + islink + '\'' +
                ", link='" + link + '\'' +
                ", pkg='" + pkg + '\'' +
                ", act='" + act + '\'' +
                ", tag='" + tag + '\'' +
                ", icon='" + icon + '\'' +
                ", ishold='" + ishold + '\'' +
                ", islock='" + islock + '\'' +
                ", way='" + way + '\'' +
                ", wayval='" + wayval + '\'' +
                ", imgurl='" + imgurl + '\'' +
                ", id=" + id +
                '}';
    }
}
