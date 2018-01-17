package com.coalesce.core.text;

import com.coalesce.core.Coalesce;
import com.coalesce.core.wrappers.CoPlayer;

import java.util.function.Predicate;

public class ActionBar {
    
    private final Builder builder;
    
    public ActionBar(Builder builder) {
        this.builder = builder;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void send(String player) {
    
    }
    
    public int getDuration() {
        return builder.duration;
    }
    
    public boolean fadesIn() {
        return builder.fadeIn;
    }
    
    public boolean fadesOut() {
        return builder.fadeOut;
    }
    
    public String getText() {
        return builder.text;
    }
    
    public static class Builder {
    
        private String text;
        private int duration;
        private boolean fadeIn;
        private boolean fadeOut;
        
        public ActionBar build() {
            return new ActionBar(this);
        }
        
        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }
        
        public Builder fadeIn(boolean fadeIn) {
            this.fadeIn = fadeIn;
            return this;
        }
        
        public Builder fadeOut(boolean fadeOut) {
            this.fadeOut = fadeOut;
            return this;
        }
        
        public Builder text(String text) {
            this.text = text;
            return this;
        }
        
    }
    
}