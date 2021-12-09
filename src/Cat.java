import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

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

    public void upload(){
        try {
            FileWriter fileWriter = new FileWriter(home + File.separator + "Desktop" + File.separator +
                    "testGameFolder"+File.separator+ "SettlementBuildings.txt" , true);
            for (int count =0; count<BuildingsRd.size(); count++) {
                try {
                    fileWriter.write(BuildingsRd.get(count).getStringInfo()+":");
                } catch (Exception exception) {

                }
            }
            fileWriter.close();
        }
        catch (Exception exception){

        }
        try {
            FileWriter fileWriter = new FileWriter(home + File.separator + "Desktop" + File.separator +
                    "testGameFolder" + File.separator + "SettlementMoney.txt", false);
            fileWriter.write(String.valueOf(getMoneyValue()));
            fileWriter.close();
        }
        catch (Exception e){

        }
    }
    public void download(){

        try {
            FileReader fileReader = new FileReader(home + File.separator + "Desktop" + File.separator +
                    "testGameFolder" + File.separator + "SettlementBuildings.txt");
            Scanner scanner = new Scanner(fileReader);
            try {
                String[] stringsBuilds = scanner.nextLine().split(":");

                for (int count=0;count<stringsBuilds.length; count++){
                    String [] paramsMas = stringsBuilds[count].split(",");
                    String name = String.valueOf(paramsMas[0]);
                    int xCord =Integer.parseInt(paramsMas[1]);
                    int yCord =Integer.parseInt(paramsMas[2]);
                    Building market = new Market();
                    Building mine = new Mine();
                    Building townHall = new TownHall();
                    if (name.equals("[\u001B[36mMark\u001B[0m]")){
                        market.setCord(xCord,yCord);
                        addSavedBuilding(market);
                    }
                    if (name.equals("[\u001B[36mMine\u001B[0m]")){
                        mine.setCord(xCord,yCord);
                        addSavedBuilding(mine);
                    }
                    if (name.equals("[\u001B[36mHall\u001B[0m]")){
                        townHall.setCord(xCord,yCord);
                        addSavedBuilding(townHall);
                    }
                }
            } catch (Exception exception) {

            }
            fileReader.close();
        }
        catch (Exception exception){

        }
        try {
            FileReader fileReader = new FileReader(home + File.separator + "Desktop" + File.separator +
                    "testGameFolder" + File.separator + "SettlementMoney.txt");
            Scanner scan = new Scanner(fileReader);
            int money = scan.nextInt();
            fileReader.close();
            setMoneyValue(money);
        } catch (Exception e) {

        }

    }
}
