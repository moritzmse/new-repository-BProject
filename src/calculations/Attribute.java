package calculations;

public class Attribute {

    private String name;
    private int counter;

    public String getName() {
        return name;
    }

    public int getCounter() {
        return counter;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }



    Attribute(String name, int counter){
        this.name=name;
        this.counter=counter;
    }



}

//Anzahl Attribute = 14