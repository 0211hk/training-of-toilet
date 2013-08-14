package org.hk.training.toilet.util;

import java.util.ArrayList;

public class KeyListPair {

    public String key = "";
    public final ArrayList<String> values = new ArrayList<String>();

    public KeyListPair(final String key, final String val){
        this.key = key;
        values.add(val);
    }

    public KeyListPair(){
    }

    public final void startBrackets(){
        this.key = "(" + this.key;
    }

    public final void endBrackets(){
        this.key = this.key + ")";
    }

    public final String[] toValueStrings(){
        return values.toArray(new String[values.size()]);
    }

    public final void add(final KeyListPair src,final String with){
        key = key + with + src.key;
        values.addAll(src.values);
    }

    @Override
    public boolean equals(final Object src){
        if(src instanceof KeyListPair){
            final KeyListPair s = (KeyListPair)src;
            if(s.key.equals(key)){
                if(s.values.size() == values.size()){
                    for(int i = 0 ; i < s.values.size() ; i++){
                        if(!s.values.get(i).equals(values.get(i))){
                            return false;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
