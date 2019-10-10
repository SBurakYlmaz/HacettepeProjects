#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#define max_letter 26

typedef struct Login_System{
    char letter;
    struct Login_System *children[max_letter];
    char *password;
}Login_System;

void File_reader(char*,Login_System*,FILE*);
void Create_User(char*,char*,Login_System*,FILE*);
int Find_Location(char);
Login_System* Add_tree(Login_System*,Login_System*,int*);
void Search_tree(char*,Login_System*,FILE*);
void Check_tree(Login_System*,char*,FILE*,char*);
Login_System* Delete_tree(char*,Login_System*,FILE *);
void Removing_name(char*,Login_System**,int);
int Find_Child_Number(Login_System*);
void List_tree_second(Login_System *root,char*,int,FILE*,int*);

int main(int argc, char *argv[]) {
    Login_System *root=(Login_System*)malloc(sizeof(Login_System));
    root->letter='-';
    int i;
    for(i=0;i<26;i++){
        root->children[i]=NULL;
    }
    FILE *output_fp=fopen("output.txt","w");
    File_reader(argv[1],root,output_fp);
    return 0;
}

void File_reader(char *filename,Login_System* root,FILE* output_file){
    FILE *fp=fopen(filename,"r");
    char buffer[500];
    char *token;
    char delimiter[]=" \n";
    while (fgets(buffer, 100,fp)!=NULL){
        token=strtok(buffer,delimiter);
        /*Reading the commands and calling the proper function*/
        switch(*(token+1)){
            case 'a':{char *name= strtok(NULL,delimiter);
                Create_User(name,strtok(NULL,delimiter),root,output_file);
                break;
            }
            case 's':{char *name= strtok(NULL,delimiter);
                int location=Find_Location(*name);
                Search_tree(name,root->children[location],output_file);
                break;
            }
            case 'q':{char *name= strtok(NULL,delimiter);
                int location=Find_Location(*name);
                Check_tree(root->children[location],name,output_file,strtok(NULL,delimiter));
                break;
            }
            case 'd':{char *name= strtok(NULL,delimiter);
                int location=Find_Location(*name);
                root->children[location]=Delete_tree(name,root->children[location],output_file);
                break;
            }
            case 'l':{
                char name[50];
                List_tree_second(root,name,0,output_file,0);
                break;
            }
            default:break;
        }
    }
}

void Create_User(char* name,char *password,Login_System* root,FILE* output_file){
    Login_System *temp=root;
    int length=strlen(name);
    int i,j,reserved_name=-1;
    for(i=0;i<length;i++){
        Login_System *user=(Login_System*)malloc(sizeof(Login_System));
        user->letter=name[i];
        user->password=NULL;
        /*First filling all the children as null*/
        for(j=0;j<26;j++){
            user->children[j]=NULL;
        }
        /*When it comes to next character it associate the last character with the given password*/
        if(i==length-1){
            user->password=(char*)malloc(sizeof(char)*(strlen(password)+1));
            strcpy(user->password,password);
        }
        temp=Add_tree(temp,user,&reserved_name);
    }
    /*If the reserved name value changed that means the username already added to the database*/
    if(reserved_name!=-1){
        fprintf(output_file,"\"%s\" reserved username\n",name);
    } else
        fprintf(output_file,"\"%s\" was added\n",name);
    reserved_name=-1;
}

Login_System* Add_tree(Login_System* root,Login_System* user,int *reserved_name){
    Login_System *temp=root;
    int location;
    location= Find_Location(user->letter);
    if(temp->children[location]!=NULL){
        if(user->password!=NULL){
            /*If the last character of name has already a password It determines it has been already reserved so
             * it gives a warning message*/
            if(temp->children[location]->password!=NULL){
                *reserved_name=0;
            }else{
                temp->children[location]->password=(char *)malloc(sizeof(char)*(strlen(user->password)+1));
                strcpy(temp->children[location]->password,user->password);
            }
        }
    } else
        temp->children[location]=user;
    return temp->children[location];
}

void Search_tree(char *name,Login_System* root,FILE* output_file){
    Login_System *temp=root;
    int location=0,i=1;
    if(temp==NULL){
        fprintf(output_file,"\"%s\" no record\n",name);
        return;
    } else{
        /*It searches properly considering the given name characters*/
        while (i<strlen(name)){
            location=Find_Location(*(name+i));
            if(temp->children[location]==NULL){
                fprintf(output_file,"\"%s\" incorrect username\n",name);
                return;
            } else{
                temp=temp->children[location];/*Changes the root all the time if it has record*/
            }
            i++;
        }
        /*If the while loop completes properly that means i is equal to name length but if the last character password is null
         * that means not enough username*/
        if(temp->password==NULL){
            fprintf(output_file,"\"%s\" not enough username\n",name);
        }
            /*If the while loop properly completed and if the final character has a password write password of the given name*/
        else{
            fprintf(output_file,"\"%s\" password \"%s\"\n",name,temp->password);
        }
    }
}

void Check_tree(Login_System *root,char *name,FILE* output_file,char* password){
    Login_System *temp=root;
    int location=0,i=1;
    if(temp==NULL){
        fprintf(output_file,"\"%s\" no record\n",name);
        return;
    }else{
        /*It searches properly considering the given name characters*/
        while (i<strlen(name)){
            location=Find_Location(*(name+i));
            if(temp->children[location]==NULL){
                fprintf(output_file,"\"%s\" incorrect username\n",name);
                return;
            } else{
                temp=temp->children[location];/*Changes the root all the time if it has record*/
            }
            i++;
        }
        /*If the while loop completes properly that means i is equal to name length but if the last character password is null
         * that means not enough username*/
        if(temp->password==NULL){
            fprintf(output_file,"\"%s\" not enough username\n",name);
        }
            /*If the while loop properly completed and if the final character has a password write password of the given name*/
        else{
            if(strcmp(password,temp->password)==0)
                fprintf(output_file,"\"%s\" successful login\n",name);
            else
                fprintf(output_file,"\"%s\" incorrect password\n",name);
        }
    }
}

Login_System* Delete_tree(char *name,Login_System *root,FILE *output_file){
    Login_System *temp=root;
    int location=0,i=1;
    if(temp==NULL){
        fprintf(output_file,"\"%s\" no record\n",name);
        return NULL;
    }
    while (i<strlen(name)){
        location=Find_Location(*(name+i));
        /*Checks the incorrect username condition*/
        if(temp->children[location]==NULL){
            fprintf(output_file,"\"%s\" incorrect username\n",name);
            return root;
        } else{
            temp=temp->children[location];/*Changes the root all the time if it has record*/}
        i++;
    }
    /*Checks the not enough username condition*/
    if(temp->password==NULL && i==strlen(name)){
        fprintf(output_file,"\"%s\" not enough username\n",name);
        return root;
    } else if(temp->password!=NULL && i==strlen(name)){
        //temp->password=NULL;
        Removing_name(name,&root,1);
        fprintf(output_file,"\"%s\" deletion is successful\n",name);
        //return NULL;
    }
    return root;
}

void Removing_name(char *name ,Login_System **root,int counter){
    Login_System *temp=*root;
    int location=0;
    int j;
    if(counter==strlen(name) && (*root)->password!=NULL){/*If it is the letter of the name it deletes the password*/
        free(temp->password);
        temp->password=NULL;
        for(j=0;j<26;j++){
            if(temp->children[j]!=NULL){ /*If that node is still a part of another username it does not delete or free just return*/
                return;
            }
        }
        free(*root); /*First freeing the node and then assign it to null*/
        *root=NULL;
    } else{
        location=Find_Location(*(name+counter));
        counter++;
        Removing_name(name,&temp->children[location],counter);
        for(j=0;j<26;j++){
            if(temp->children[j]!=NULL){
                return;
            }
        }
        free(*root);
        *root=NULL;
    }
}
/*Finding the number of child of the root and returns the amount*/
int Find_Child_Number(Login_System *root){
    int number_of_child=0,i;
    for(i=0;i<26;i++){
        if(root->children[i]!=NULL){
            number_of_child++;
        }
    }
    return number_of_child;
}

void List_tree_second(Login_System *root,char name[],int memory_counter,FILE *output_file,int *first_branch){
    Login_System *temp=root;
    int child_number=Find_Child_Number(temp);
    int i,j;
    /*If it has no child that me no more connections related to that node*/
    if(child_number==0){
        name[memory_counter]=temp->letter;
        name[memory_counter+1]='\0';
        fprintf(output_file,"%s\n",name);
        return;
    }
    /*It checks the first main root that can contain different elements and finds that*/
    if(temp->letter=='-'){
        int counter=0;
        for(i=0;i<26;i++){
            if(temp->children[i]!=NULL){
                counter++;
                fprintf(output_file,"-");
                List_tree_second(temp->children[i],name,memory_counter,output_file,first_branch);
                int length=strlen(name);
                for(j=0;j<length;j++)
                    name[j]='\0';
            }
        }
    }
    /*If the current node has one child it finds that child and then no reason to search for others*/
    else if(child_number==1){
       // int prev_first_branch=*first_branch;
        name[memory_counter]=temp->letter;
        if(temp->password!=NULL){
            name[memory_counter+1]='\0';
            fprintf(output_file,"%s,",name);
            if(first_branch==0){
                fprintf(output_file,"\n\t-");
            }
        }
        for(i=0;i<26;i++){
            if(temp->children[i]!=NULL){
                /*if(Find_Child_Number(temp->children[i])!=1){
                    fprintf(output_file,"\n\t-");
                } else{
                    //fprintf(output_file,",");
                }*/
                List_tree_second(temp->children[i],name,memory_counter+1,output_file,first_branch);
                return;
            }
        }
    }
    /*If it has more than one child it must be the common root so it prints first the common root and if common root also a username
     * it checks also that condition and then finding the other children of the current node*/
    else if(child_number>1){
        int counter=0;
        name[memory_counter]=temp->letter;
        name[memory_counter+1]='\0';
        if(first_branch==0)
            fprintf(output_file,"%s\n\t-",name);
        first_branch++;
        if(temp->password!=NULL){
            name[memory_counter+1]='\0';
            fprintf(output_file,"%s\n\t-",name);
        }
        for(i=0;i<26;i++){
            if(temp->children[i]!=NULL){
                counter++;
                List_tree_second(temp->children[i],name,memory_counter+1,output_file,first_branch);
                if(counter!=child_number)
                    fprintf(output_file,"\t-");
            }
        }
        first_branch=0;
    }
}
/*Find the correct location to locate the character*/
int Find_Location(char letter){
    return letter-'a';
}
