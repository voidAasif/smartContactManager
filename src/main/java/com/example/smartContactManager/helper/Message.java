package com.example.smartContactManager.helper;

//this is class is used by HttpSession class to display message of form input;

public class Message {
    private String content; //it is for message;
    private String type; //it is for class for styling; eg red for error, and green for success;

    public Message(String content, String type){
        super();

        this.content = content;
        this.type = type;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    
}
