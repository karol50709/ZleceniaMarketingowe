package com.karol.edc;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Task {


    private final IntegerProperty id = new SimpleIntegerProperty(this,"id");
    private final StringProperty text = new SimpleStringProperty(this,"text");
    private final IntegerProperty time = new SimpleIntegerProperty(this,"time");


    public Task(int id,String text,int time){
        setId(id);
        setText(text);
        setTime(time);

    }

    public final void setId(int x){
        this.id.set(x);
    }

    public final void setText(String text){
        this.text.set(text);
    }

    public final void setTime(int x){
        this.time.set(x);
    }

    public final IntegerProperty idProperty(){
        return id;
    }

    public final StringProperty textProperty(){
        return text;
    }

    public final IntegerProperty timeProperty(){
        return time;
    }

    @Override
    public String toString() {
        return id.get()+""+";"+text.getValue()+";"+time.get()+"";
    }
}
