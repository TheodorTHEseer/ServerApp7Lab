import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Cat implements Serializable {
    private final Map <String, String> nameRace = new HashMap<>();
    private final Map <String, Double> nameParams = new HashMap<>();
    private final String [] names = {"Лютик", "Барсик", "Мурка", "Тигра", "Летиция", "Аэлита"};
    private final double [] params = {1.2, 2.3, 4.3, 1.7, 2.3, 3.3};
    private void load (){
        nameRace.put(names[0], "Abyssinian");
        nameRace.put(names[1], "Persian");
        nameRace.put(names[2], "Brazilian Shorthair");
        nameRace.put(names[3], "Burmilla");
        nameRace.put(names[4], "York");
        nameRace.put(names[5], "British Longhair");
        for (int count = 0; count<names.length && count<params.length; count++){
            nameParams.put(names[count], params[count]);
        }

    }
    private String name;
    private String race;
    private double weight;

    public Cat (int num){
        load();
        name = names[num];
        race = nameRace.get(name);
        weight = params[num];
    }

    public Cat (String name){
        try {
            load();
            this.name=name;
            this.race = nameRace.get(name);
            this.weight = nameParams.get(name);
        }
        catch (Exception e){
            System.out.println("Кошка не найдена");
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", race='" + race +
                ", weight=" +weight + '\'' +
                '}';
    }

    public void store(){

    }
}
