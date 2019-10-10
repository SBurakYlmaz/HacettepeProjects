#include <iostream>
#include <fstream>
#include <string>
#include <sstream>
#include <map>
#include <set>
#include <iterator>
#define get_max(a,b) ((a)>(b)?(0):(1))

using namespace std;
typedef struct Away_Team{
    string against_team;
    int score_time{};
    int match_id{};
    Away_Team *awayTeam_pointer{};
    Away_Team *awayTeam_prev{};
}Away_Team;

typedef struct Home_Team{
    string name;
    string home_team;
    Away_Team *awayTeam_pointer{};
    Home_Team *next_player_pointer{};
}Home_Team;
/*Function prototypes*/
Home_Team* file_reader(char*,Home_Team*);
Home_Team* create_player(const string &,Home_Team*);
Home_Team* add_player(Home_Team*,Home_Team*);
void operations(Home_Team*,ostream&);
void operations2(char*,Home_Team*,ostream&);
void match_finder(Home_Team*, const string &,ostream&);
void ascending_order_printing_matches(Home_Team*, const string &,ostream&);
void descending_order_printing_matches(Home_Team*, const string &,ostream&);
void print_recursively(Away_Team*, const string &,int,ostream&);

int main(int argc, char *argv[]) {
    ofstream output;
    output.open(argv[3]);
    Home_Team *home_team_list=nullptr;
    home_team_list=file_reader(argv[1],home_team_list);
    operations(home_team_list,output);
    operations2(argv[2],home_team_list,output);
    output.close();
    return 0;
}

Home_Team* file_reader(char *file,Home_Team *home_team_list){
    ifstream my_File;
    my_File.open(file);
    string line;
    /*Reading line by line*/
    while (getline(my_File,line)){
        home_team_list=create_player(line,home_team_list);
    }
    my_File.close();
    return home_team_list;
}

Home_Team* create_player(const string &line ,Home_Team *home_team_list){
    auto home_player=new Home_Team();
    auto away_player=new Away_Team();
    string word;
    int i;
    home_player->awayTeam_pointer=away_player;

    istringstream stream(line);
    getline(stream,word,',');
    /*Splitting each line and assign the values to correct places of the players*/
    for(i=0;i<5;i++){
        switch(i){
            case 0:home_player->name=word;
                getline(stream,word,',');
                break;
            case 1:home_player->home_team=word;
                getline(stream,word,',');
                break;
            case 2:away_player->against_team=word;
                getline(stream,word,',');
                break;
            case 3:away_player->score_time=atoi(word.c_str());
                getline(stream,word,',');
                break;
            case 4:away_player->match_id=atoi(word.c_str());
                getline(stream,word,',');
                break;
            default:break;
        }
    }
    home_team_list=add_player(home_player,home_team_list); /*Calling function to add the current player to the linked list*/
    return home_team_list;
}
Home_Team* add_player(Home_Team *adding_player,Home_Team *home_team_list){
    Home_Team *rear_player= nullptr;
    Home_Team *front_player= home_team_list;

    /*Considering the ascending order of names of the players and then adding to linked list*/
    while ( front_player!= nullptr && front_player->name.compare(adding_player->name)<0){
        rear_player=front_player;
        front_player=front_player->next_player_pointer;
    }
    /*If the player already added to linked list and if it continues from this if loop that means current player's different
     * goal of same or different match*/

    if(front_player!= nullptr && front_player->name == adding_player->name){
        Away_Team *rear_id=nullptr;
        Away_Team *front_id=front_player->awayTeam_pointer;

        /*Considering the ascending order of match id's and added to another linked list of that player so we can trace
         * each player different scores and matches*/

        while ( front_id!= nullptr && front_id->match_id <= adding_player->awayTeam_pointer->match_id){
            rear_id=front_id;
            front_id=front_id->awayTeam_pointer;
        }
        /*It means we are going to add different score to the beginning*/
        if(rear_id== nullptr){
            if(front_id->match_id <= adding_player->awayTeam_pointer->match_id){
                front_id->awayTeam_pointer=adding_player->awayTeam_pointer;
                adding_player->awayTeam_pointer->awayTeam_prev=front_id;
            } else{
                adding_player->awayTeam_pointer->awayTeam_pointer=front_id;
                front_id->awayTeam_prev=adding_player->awayTeam_pointer;
                front_player->awayTeam_pointer=adding_player->awayTeam_pointer; /*adding to beginning*/
                //front_player->awayTeam_pointer=adding_player->awayTeam_pointer;
            }

        }
        /*It means we are going to add different score to the middle or to the end*/
        else{
            adding_player->awayTeam_pointer->awayTeam_pointer=rear_id->awayTeam_pointer;
            if(front_id!= nullptr)
                adding_player->awayTeam_pointer->awayTeam_prev=front_id->awayTeam_prev;
            else
                adding_player->awayTeam_pointer->awayTeam_prev=rear_id;
            //adding_player->awayTeam_pointer->awayTeam_prev=front_id->awayTeam_prev;
            rear_id->awayTeam_pointer=adding_player->awayTeam_pointer;
            if(front_id!= nullptr)
                front_id->awayTeam_prev=adding_player->awayTeam_pointer;
        }
    }
    /*The player that we haven't added to linked list yet continues form this else statement*/
    else {

        if (rear_player == nullptr) {
            adding_player->next_player_pointer = front_player;
            home_team_list = adding_player;
        } else {
            adding_player->next_player_pointer = front_player;
            rear_player->next_player_pointer = adding_player;
        }
    }
    return home_team_list;
}
void operations(Home_Team* home_team_list,ostream& output){
    int counter=1,goal_count=0,top_score=0,id;
    map<string,int> player_score;/*For storing each players scores*/
    set <string> teams; /*For storing teams*/
    Home_Team *temporary;
    Away_Team *halftime;
    int first_half=0,second_half=0;

    while (counter<=5){
        temporary=home_team_list;
        /*When counter is 1 it calculates each goal's minute and deciding the most goals scored half*/
        if(counter==1){
            while (temporary!= nullptr){
                halftime=temporary->awayTeam_pointer;
                while (halftime!= nullptr){
                    if(halftime->score_time <=45)
                        first_half++;
                    else
                        second_half++;
                    halftime=halftime->awayTeam_pointer;
                }
                temporary=temporary->next_player_pointer;
            }
            output<<"1)THE MOST SCORED HALF"<<endl;
            if(first_half==second_half)
                output<<-1<<endl;
            else
                output<<get_max(first_half,second_half)<<endl;
            /*Calculating the most scorer player of the league*/
        } else if(counter==2){
            output<<"2)GOAL SCORER"<<endl;
            while (temporary!= nullptr){
                halftime=temporary->awayTeam_pointer;
                while (halftime!= nullptr){
                    goal_count++;
                    halftime=halftime->awayTeam_pointer;
                }
                if(goal_count>top_score)
                    top_score=goal_count;
                player_score.insert(make_pair(temporary->name,goal_count));
                temporary=temporary->next_player_pointer;
                goal_count=0;
            }
            auto it = player_score.begin(); /*Iterating over map and find most scorer player of the league*/
            while (it != player_score.end()){
                if(it->second == top_score)
                    output<<it->first<<endl;
                it++;
            }
            /*Calculating the hat-trick feature for every player*/
        } else if(counter==3){
            output<<"3)THE NAMES OF FOOTBALLERS WHO SCORED HAT-TRICK"<<endl;
            while (temporary!= nullptr){
                halftime=temporary->awayTeam_pointer;
                id=halftime->match_id;
                while (halftime!= nullptr){
                    if(halftime->match_id==id)
                        goal_count++;
                    else{
                        goal_count=0;
                        id=halftime->match_id;
                        goal_count++;
                    }
                    if(goal_count==3){
                        output<<temporary->name<<endl;
                        break;
                    }
                    halftime=halftime->awayTeam_pointer;
                }
                temporary=temporary->next_player_pointer;
                goal_count=0;
            }
            /*Checking the team list and adding different teams to the set of teams*/
        } else if(counter==4){
            output<<"4)LIST OF TEAMS"<<endl;
            while (temporary!= nullptr){
                teams.insert(temporary->home_team);
                halftime=temporary->awayTeam_pointer;
                while (halftime!= nullptr){
                    teams.insert(halftime->against_team);
                    halftime=halftime->awayTeam_pointer;
                }
                temporary=temporary->next_player_pointer;
            }
            auto it=teams.begin(); /*Iterating over set and writing each team to the file*/
            while (it != teams.end()){
                output<<*it<<endl;
                it++;
            }
            /*Finding each player and printing them to the output.txt*/
        } else if(counter==5){
            output<<"5)LIST OF FOOTBALLERS"<<endl;
            while (home_team_list!= nullptr){
                output<<home_team_list->name<<endl;
                home_team_list=home_team_list->next_player_pointer;
            }
        }
        counter++;
    }
}
void operations2(char *file,Home_Team* home_team_list,ostream& output){
    ifstream my_File;
    my_File.open(file);
    string line,word;
    int counter=1;
    while (getline(my_File,line)){  /*Reading line by line*/
        if(counter==1)
            output<<"6)MATCHES OF GIVEN FOOTBALLER"<<endl;
        else if(counter==2)
            output<<"7)ASCENDING ORDER ACCORDING TO MATCH ID"<<endl;
        else
            output<<"8)DESCENDING ORDER ACCORDING TO MATCH ID"<<endl;
        istringstream stream(line);
        while (getline(stream,word,',')){ /*Splitting the line by comma*/
            /*Calling the auxilary function for the calculations*/
            switch (counter){
                case 1:match_finder(home_team_list,word,output);
                    break;
                case 2:ascending_order_printing_matches(home_team_list,word,output);
                    break;
                case 3:descending_order_printing_matches(home_team_list,word,output);
                    break;
                default:break;
            }
        }
        counter++;
    }
    my_File.close();
}
void match_finder(Home_Team* home_team_list, const string &name,ostream& output){
    Home_Team *temporary=home_team_list;
    Away_Team *matches;
    /*Finding the matches of given footballers and printing them to the txt*/
    while (temporary!= nullptr){
        matches=temporary->awayTeam_pointer;
        if(temporary->name == name){
            output<<"Matches of "+ name<<endl;
            while (matches!= nullptr){
                output<<"Footballer Name: " + temporary->name + ",Away Team: " +matches->against_team + ",Min of Goal: " +
                to_string(matches->score_time) + ",Match ID: "+ to_string(matches->match_id)<<endl;
                matches=matches->awayTeam_pointer;
            }
            break;
        }
        temporary=temporary->next_player_pointer;
    }
}
void ascending_order_printing_matches(Home_Team *home_team_list, const string & name,ostream& output){
    Home_Team *temporary=home_team_list;
    Away_Team *matches;
    int id=-1;
    /*Printing different matches of given footballers considering the match id of each data(ascending order)*/
    while (temporary!= nullptr){
        matches=temporary->awayTeam_pointer;
        if(temporary->name == name){
            while (matches!= nullptr){
                if(id!=matches->match_id){
                    output<<"footballer Name: " + temporary->name + ",Match ID: " + to_string(matches->match_id)<<endl;
                }
                id=matches->match_id;
                matches=matches->awayTeam_pointer;
            }
            break;
        }
        temporary=temporary->next_player_pointer;
    }
}
void descending_order_printing_matches(Home_Team *home_team_list, const string & name,ostream& output){
    Home_Team *temporary=home_team_list;
    Away_Team *matches;
    /*Finding the given footballers match and calling an auxilary function to print them recursively because of descending order*/
    while (temporary!= nullptr){
        if(temporary->name == name){
            matches=temporary->awayTeam_pointer;
            print_recursively(matches,name,-1,output);
            break;
        }
       temporary=temporary->next_player_pointer;
    }
}
void print_recursively(Away_Team *matches, const string &name,int id,ostream& output){
    if(matches== nullptr)
        return;
        /*Printing different matches of given footballers considering the match id of each data(descending order)
     * recursively*/
    else{
        print_recursively(matches->awayTeam_pointer,name,matches->match_id,output);
        if(matches->match_id!=id){
            output<<"footballer Name: " + name +  ",Match ID: "+ to_string(matches->match_id)<<endl;
        }
    }
}