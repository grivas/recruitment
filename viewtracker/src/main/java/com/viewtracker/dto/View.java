package com.viewtracker.dto;

import org.joda.time.DateTime;

/**
 * Representation of a profile view that contains the id of the viewed profile, the id of the viewer and the date in
 * which the profile was viewed.
 */
public class View {
    private long userId;
    private long viewerId;
    private DateTime viewedOn = new DateTime();

    public static Builder newBuilder(){
        return new Builder(new View());
    }

    public long getViewerId() {
        return viewerId;
    }

    public long getUserId() {
        return userId;
    }

    public DateTime getViewedOn() {
            return viewedOn;
    }

    public static class Builder{
        private final View instance;

        public Builder(View instance) {
            this.instance = instance;
        }

        public Builder withViewerId(long viewer) {
            instance.viewerId = viewer;
            return this;
        }

        public Builder withUserId(long userId) {
            instance.userId = userId;
            return this;
        }

        public Builder viewedOn(DateTime viewedOn) {
            instance.viewedOn = viewedOn;
            return this;
        }

        public View build(){
            return instance;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof View)) return false;

        View view = (View) o;

        if (userId != view.userId) return false;
        if (viewerId != view.viewerId) return false;
        if (viewedOn != null ? !(viewedOn.getMillis() == view.viewedOn.getMillis()) : view.viewedOn != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (userId ^ (userId >>> 32));
        result = 31 * result + (int) (viewerId ^ (viewerId >>> 32));
        result = 31 * result + (viewedOn != null ? viewedOn.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "View{" +
                "userId=" + userId +
                ", viewerId=" + viewerId +
                ", viewedOn=" + viewedOn +
                '}';
    }
}
