package com.coalesce.core.text;

public class Title {
    
    private final Builder builder;
    
    public Title(Builder builder) {
        this.builder = builder;
    }
    
    public static Builder builder() {
        return new Builder();
    }
    
    public void send(String player) {
    }
    
    public String getSubTitle() {
        return builder.subTitle;
    }
    
    public String getTitle() {
        return builder.title;
    }
    
    public int durationTicks() {
        return builder.duration;
    }
    
    public int fadeInTicks() {
        return builder.fadeIn;
    }
    
    public int fadeOutTicks() {
        return builder.fadeOut;
    }
    
    public static class Builder {
        
        private String subTitle;
        private String title;
        private int duration = 70;
        private int fadeIn = 10;
        private int fadeOut = 20;
        
        public Builder setSubTitle(String subTitle) {
            this.subTitle = subTitle;
            return this;
        }
        
        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }
        
        public Builder setDuration(int durationTicks) {
            this.duration = durationTicks;
            return this;
        }
        
        public Builder setFadeIn(int fadeInTicks) {
            this.fadeIn = fadeInTicks;
            return this;
        }
        
        public Builder setFadeOut(int fadeOutTicks) {
            this.fadeOut = fadeOutTicks;
            return this;
        }
        
    }
    
}
