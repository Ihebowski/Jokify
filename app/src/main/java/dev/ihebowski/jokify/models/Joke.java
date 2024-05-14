package dev.ihebowski.jokify.models;

public class Joke {
    public String id;
    public String setup;
    public String punchline;
    public String username;
    public String uid;

    public Joke(){
    }

    public Joke(String setup,String punchline, String username, String uid){
        this.setup = setup;
        this.punchline = punchline;
        this.username = username;
        this.uid = uid;
    }

    public Joke(String setup, String punchline, String uid) {
        this.setup = setup;
        this.punchline = punchline;
        this.uid = uid;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }

    public String getUid() {
        return uid;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
