package com.coalesce.core.i18n;

public enum DummyLang implements Translatable {
    ;
    
    @Override
    public String getKey() {
        throw new UnsupportedOperationException("Please specify a language enum");
    }
    
    @Override
    public String[] getHolders() {
        throw new UnsupportedOperationException("Please specify a language enum");
    }
}
