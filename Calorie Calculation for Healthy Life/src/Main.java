public class Main {

    public static void main(String[] args) {           //Creating objects and arrays to use in other classes methods
        writer write =new writer("monitoring.txt");
        write.check_empty();
        people[] human = new people[100];
        food[] meal = new food[100];
        sport[] ea_sport = new sport[100];
        people creature = new people();
        sport sportive = new sport();
        food delicious = new food();
        command cmd = new command();
        creature.reader_people(human);
        sportive.reader_sport(ea_sport);
        delicious.reader_food(meal);
        cmd.cm_reader(args[0],human,meal,ea_sport,write);

    }
}