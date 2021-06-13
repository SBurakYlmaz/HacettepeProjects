

import java.io.*;
import java.util.*;

public class Commands{
    public static void command_reader(String path) throws IOException {
        BeJeweled jeweled = new Wildcard();
        int row=0,column;
        List<BeJeweled> score=new ArrayList<>();       //Store each turn score
        List<Integer> full_score = new ArrayList<>();  //Store full scores
        List<Players> players = new ArrayList<>();   //Store each player
        List<Map> jeweledList = new ArrayList<>();      //Store the grid
        Integer x_axis=0;Integer y_axis;
        File file = new File(path);
        Scanner sc = new Scanner(file);

        File leaderboard = new File("leaderboard.txt");
        if(leaderboard.exists()){           //Checking the file if it exists if it is not create this file
            FileReader fr = new FileReader(leaderboard);
            BufferedReader br = new BufferedReader(fr);
            String line = br.readLine();
            while (line!=null){
                String[] temp=line.split(" ");
                players.add(new Players(temp[0],Integer.parseInt(temp[1])));
                line=br.readLine();
            }
        }
        else {
            leaderboard.createNewFile();
        }


        while (sc.hasNextLine()){
            String line = sc.nextLine();
            row++;
        }
        sc.close();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()){
            Map<Integer,BeJeweled> map = new TreeMap<>();
            y_axis=0;
            String line = scanner.nextLine();
            StringTokenizer tokenizer =new StringTokenizer(line);
            while(tokenizer.hasMoreTokens()){         //Putting objects to the grid map
                String word = tokenizer.nextToken();
                switch (word){
                    case "D": map.put(y_axis,new Diamond());
                        break;
                    case "T": map.put(y_axis,new Triangle());
                        break;
                    case "W": map.put(y_axis,new Wildcard());
                        break;
                    case "S": map.put(y_axis,new Square());
                        break;
                    case "-":map.put(y_axis,new Minus());
                        break;
                    case "+":map.put(y_axis,new Plus());
                        break;
                    case "/":map.put(y_axis,new Slash());
                        break;
                    case "\\":map.put(y_axis,new BackSlash());
                        break;
                    case "|":map.put(y_axis,new Pipe());
                        break;
                }
                y_axis++;
            }
            x_axis++;
            jeweledList.add(map);
        }
        column = jeweledList.get(0).size();
        System.out.println("Game grid:\n");
        jeweled.Grid_writer(jeweledList,row,column);
        scanner.close();
        Scanner redirect_file = new Scanner(System.in);
        while(redirect_file.hasNextLine()){
            String line = redirect_file.nextLine();
            System.out.print("Select coordinate or enter E to end the game: ");
            String[] temp_part = line.split(" ");

            if(temp_part[0].equals("E")){
                System.out.println("E");
                boolean equals;

                int sum = full_score.stream().mapToInt(Integer::intValue).sum();//Finding total score of current player

                System.out.println(String.format("\nTotal score: %d points\n",sum));
                line=redirect_file.nextLine();
                System.out.println(String.format("Enter name: %s",line));
                Players current_player = new Players(line,sum);

                //If equals true that means current player never played this game before otherwise current player can only uptade it's point

                equals=current_player.equals_detector(players,current_player);
                if(equals)
                    players.add(current_player);

                Collections.sort(players); //Sorting the list with descending order
                int index = Collections.binarySearch(players,current_player); //Finding the current player rank between all players
                current_player.Rank_Writer(players,index);   //Writing the rank of current player and current status
                jeweled.leaderboard_writer(players,leaderboard);  //writing the players and their scores to leaderboard.txt
                break;
            }
            else {
                try {
                    System.out.println(String.format("%d %d\n", Integer.parseInt(temp_part[0]), Integer.parseInt(temp_part[1])));

                    //Checking the map borders

                    if (row < Integer.parseInt(temp_part[0]) || column < Integer.parseInt(temp_part[1])) {
                        System.out.println("Coordinate is out of map Select another coordinate or E to end the game ");
                        System.out.println("\n");
                        jeweled.Grid_writer(jeweledList, row, column);
                        continue;
                    }
                    BeJeweled target = (BeJeweled) jeweledList.get(Integer.parseInt(temp_part[0])).get(Integer.parseInt(temp_part[1]));
                    target.BejeweledDetector(jeweledList, row, column, Integer.parseInt(temp_part[0]), Integer.parseInt(temp_part[1]), score, target);
                    target.Grid_Shifter(jeweledList, row, column); //Shifting the grid
                    target.Grid_writer(jeweledList, row, column);  //Writing grid to the screen
                    full_score.add(target.Score_Writer(score));
                    score.clear();
                }catch (NullPointerException e){
                    System.out.println("Coordinate is empty Select another coordinate or E to end the game ");
                    System.out.println("\n");
                    jeweled.Grid_writer(jeweledList, row, column);
                    continue;
                }
            }
        }
    }
}
