#include <stdio.h>
#include <sys/types.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>
typedef struct application_layer{
    char *sender_id;
    char *receiver_id;
    char *message_chuck;
}application_layer;

typedef struct transport_layer{
    char *sender_port_number;
    char *receiver_port_number;
}transport_layer;

typedef struct network_layer{
    char *sender_ip_address;
    char *receiver_ip_address;
}network_layer;

typedef struct physical_layer{
    char *sender_mac_address;
    char *receiver_mac_address;
}physical_layer;

typedef struct log_info{
    char real_time[24];
    char *message;
    char *sender_id_1;
    char *receiver_id;
    char *activity;
    char *success;
    int number_of_frames;
}log_info;

typedef struct stack{
    void *stack[4];
    int top;
}stack;
typedef struct queue{
    stack **queues;
    int rear;
    int front;
}queue;

typedef  struct client{
    char  *client_name;
    char  *client_ip;
    char  *client_address;
    queue *outgoing_buffer;
    queue *incoming_buffer;
    log_info *logInfo;
    int hops;
} client;

void client_initializer(client**,int,char*,int);
void message(client**,char*,char*,char*,char*,char*,stack*,int,char*);
void show_frame(client**,char*,char*,int,int,int);
void show_q(client**,char*,char*,int);
void send(client**,char*,char*,int,char*,int,char*,char*);
void print_log(client**,char*,int,char*,char*);
void* pop(stack *stack1);
void push(stack *stack1,void*);
void enqueue(queue*,stack*);
stack* deque(queue *item);
client** clients_reader(char *,int*);
char *neighbour_finder(char *,int);
void commands_reader(char *,int*,client**,int,char*,char*,char*,int);
int transmit_finder(int,client**,int, const char*,char);
void free_memory(client **, int);



int main(int argc, char *argv[]) {
    int number_client,number_commands;
    int message_chunk=strtol(argv[4],NULL,10);
    char* send_port_number=argv[5];
    char* receiver_port_number=argv[6];
    client **client_list=clients_reader(argv[1],&number_client);
    char *route= neighbour_finder(argv[2],number_client);
    commands_reader(argv[3],&number_commands,client_list,message_chunk,send_port_number,receiver_port_number,route,number_client);
    return 0;
}

client** clients_reader(char *file_name,int* number_ptr){
    FILE *fp = fopen(file_name,"r");
    char *token;
    char delimiter[]=" \n";
    int i=0;

    char *buffer=(char*)malloc(sizeof(int));
    *number_ptr=(int)strtol(fgets(buffer, sizeof(char)*4,fp),NULL,10);
    client **clients_ptr=(client**)malloc(sizeof(client*)*(*number_ptr));

    buffer=(char*)realloc(buffer, sizeof(char)*100);

    while (fgets(buffer, 100,fp)!=NULL){
        client *current_client=(client*)malloc(sizeof(client)); /*for each client using a pointer to hold the initial address*/

        token=strtok(buffer,delimiter);     /*parsing the client file considering the delimiter as space*/

        /*allocating memory for the pointer inside the structs, and then copy the values to the pointers*/
        current_client->client_name=(char*)malloc(sizeof(char)*strlen(token));
        strcpy(current_client->client_name,token);

        token=strtok(NULL,delimiter);
        current_client->client_ip=(char*)malloc(sizeof(char)*strlen(token));
        strcpy(current_client->client_ip,token);

        token=strtok(NULL,delimiter);
        current_client->client_address=(char*)malloc(sizeof(char)*strlen(token));
        strcpy(current_client->client_address,token);

        /*assigning the current clients address to the double_pointer array to keep the address properly*/
        clients_ptr[i]=current_client;
        i++;
    }
    free(buffer);
    fclose(fp);
    return clients_ptr;
}
char* neighbour_finder(char *filename,int number){
    FILE *fp=fopen(filename,"r");
    char delimiter[]=" \n";
    char *buffer=(char*)malloc(sizeof(char)*100);
    char *token;
    int i=0;
    char *destination_target=(char*)malloc(sizeof(char)*number*(number-1)*2);
    /*Creating an array to store the neighbour pairs*/
    while (fgets(buffer,100,fp)!=NULL) {
        if (strcmp(buffer,"-\n") != 0) {
            token = strtok(buffer, delimiter);
            destination_target[i]=*token;
            token = strtok(NULL, delimiter);
            destination_target[number*(number-1)+i]=*token;  /*Assigning the neighbour considering the numbver of clients and the calculation*/
            i++;
        }
    }
    destination_target[number*(number-1)*2]='\0';
    free(buffer);
    fclose(fp);
    return destination_target;
}
void commands_reader(char *filename, int *number_commands,client **client_list,int message_chunk,char *s_port,char *r_port,char *route,
                     int number_client) {
    int i;
    FILE *fp = fopen(filename, "r");
    char *buffer = (char *)malloc(1001);
    char delimiter[] = " \n";
    char *token;
    int number_of_frames=0;
    char* real_message;
    char* full_message=NULL;
    *number_commands = (int) strtol(fgets(buffer, sizeof(int), fp), NULL, 10); /*Determining number of commands*/
    char *sender=NULL;
    char *receiver=NULL;
    char* message_buffer=NULL;

    /*Reading commands.txt and deciding which commands gonna use*/
    while (fgets(buffer, 1000, fp) != NULL) {

        for(i=0;i<strlen(buffer)+strlen("Command")+1;i++){printf("%s","-");}
        printf("\n");
        printf("Command: %s",buffer);
        for(i=0;i<strlen(buffer)+strlen("Command")+1;i++){printf("%s","-");}
        printf("\n");

        token = strtok(buffer, delimiter);   /*Token for separate the whole command line considering the delimiters*/
        if (strcmp(token, "MESSAGE") == 0) {
            token=strtok(NULL,delimiter);
            sender=malloc(strlen(token));
            strcpy(sender,token);
            token=strtok(NULL,delimiter);
            receiver=malloc(strlen(token));
            strcpy(receiver,token);
            real_message=strtok(NULL,"#");
            full_message=(char*)malloc(sizeof(char)*strlen(real_message));
            strcpy(full_message,real_message);
            printf("Message to be sent: %s\n",real_message);
            if(message_chunk==0)
                number_of_frames=1;
            else if(strlen(real_message)%message_chunk==0)
                number_of_frames=strlen(real_message)/message_chunk;   /*Determining the how many frames gonna use*/
            else
                number_of_frames=(strlen(real_message)/message_chunk)+1;

            client_initializer(client_list,number_of_frames,sender,number_client);
            for(i=0;i<number_of_frames;i++){
                printf("\nFrame #%d\n",i+1);

                if(number_of_frames==1){
                    message_buffer=(char*)malloc(strlen(full_message)+1);
                    strcpy(message_buffer,full_message);
                }
                else {
                    message_buffer = (char *) malloc(sizeof(char) * message_chunk);
                    strncpy(message_buffer, real_message,sizeof(char) * message_chunk);  //Copying the message considering the message chunk
                    message_buffer[message_chunk] = '\0';
                }
                real_message=real_message+message_chunk;                  //Moving pointer to another position to take the restricted message

                stack *stack_ptr=(stack*)malloc(sizeof(stack));
                message(client_list,sender,receiver,message_buffer,s_port,r_port,stack_ptr,number_client,route);

            }
            printf("\n");
        }
        else if (strcmp(token, "SHOW_FRAME_INFO") == 0) {
            char *sender_client=strtok(NULL,delimiter);
            char *in_out=strtok(NULL,delimiter);
            show_frame(client_list,sender_client,in_out,strtol(strtok(NULL,delimiter),NULL,10),number_client,number_of_frames);

        }
        else if (strcmp(token, "SHOW_Q_INFO") == 0) {
            char *sender_client=strtok(NULL,delimiter);
            show_q(client_list,sender_client,strtok(NULL,delimiter),number_client);
        }
        else if (strcmp(token, "SEND") == 0) {
            send(client_list,strtok(NULL,delimiter),route,number_client,receiver,number_of_frames,sender,full_message);
            printf("Message: %s\n",full_message);
        }
        else if (strcmp(token, "PRINT_LOG") == 0) {
            print_log(client_list,strtok(NULL,delimiter),number_client,sender,receiver);
        }
        else {
            printf("Invalid command\n");
        }
    }
    free(buffer);
    free_memory(client_list, number_client);
    fclose(fp);
}

void client_initializer(client **client_list,int number_of_frames,char *sender,int number_client){
    int i;
    /*Initializing the some values of clients */
    for(i=0;i<number_client;i++){
        client_list[i]->outgoing_buffer=(queue*)malloc(sizeof(queue));
        client_list[i]->incoming_buffer=(queue*)malloc(sizeof(queue));
        client_list[i]->outgoing_buffer->rear=-1;
        client_list[i]->outgoing_buffer->front=-1;
        client_list[i]->incoming_buffer->rear=-1;
        client_list[i]->incoming_buffer->front=-1;
        client_list[i]->hops=0;
        /*When the sender client find it allocates the memory of Ä±ts outgoing buffer*/
        if(strcmp(sender,client_list[i]->client_name)==0) {
            client_list[i]->outgoing_buffer->queues = (stack **) malloc(sizeof(stack *) * number_of_frames);
        }
    }
}

void message(client** client_list,char *sender,char *receiver,char* message,char *s_port,char *r_port,stack *current_frame
        ,int number_client,char *route){

    int j,i, send_no = 0,receiver_index;
    current_frame->top=-1;

    application_layer *application;
    transport_layer *transport;
    network_layer *network;
    physical_layer *physical;

    application=(application_layer*)malloc(sizeof(application_layer));/*Allocating memory for each layer  */
    transport=(transport_layer*)malloc(sizeof(transport_layer));
    network=(network_layer*)malloc(sizeof(network_layer));
    physical=(physical_layer*)malloc(sizeof(physical_layer));

    application->sender_id=sender;
    application->receiver_id=receiver;                  /*Filling each layer with correct values*/
    application->message_chuck=message;
    transport->sender_port_number=s_port;
    transport->receiver_port_number=r_port;
    for(j=0;j<number_client;j++){

        if(strcmp(sender,client_list[j]->client_name)==0 ) {
            physical->sender_mac_address=client_list[j]->client_address;        /*Assigning the values to each layer*/
            network->sender_ip_address=client_list[j]->client_ip;
            network->receiver_ip_address=client_list[j]->client_ip;
            receiver_index = transmit_finder(j,client_list,number_client,route,*receiver);
            physical->receiver_mac_address=client_list[receiver_index]->client_address;
            client_list[j]->logInfo=(log_info*)malloc(sizeof(log_info)*2);
            send_no=j;
        }
        if(strcmp(receiver,client_list[j]->client_name)==0)
            network->receiver_ip_address=client_list[j]->client_ip;
    }
    /*Adding layers to current frame considering stack operations*/
    push(current_frame,application);
    push(current_frame,transport);
    push(current_frame,network);
    push(current_frame,physical);
    /*Printing the current frame values*/
    printf("Sender MAC address: %s, Receiver MAC address: %s\n",physical->sender_mac_address,physical->receiver_mac_address);
    printf("Sender IP address: %s, Receiver IP address: %s\n",network->sender_ip_address,network->receiver_ip_address);
    printf("Sender port number: %s, Receiver port number: %s\n",transport->sender_port_number,transport->receiver_port_number);
    printf("Sender ID: %s, Receiver ID: %s\n",application->sender_id,application->receiver_id);
    printf("Message chunk carried: %s\n",message);

    for(i=0;i<strlen("Frame #i");i++){printf("%s","-");}
    enqueue(client_list[send_no]->outgoing_buffer,current_frame);
}

void show_frame(client **client_list,char *sender_client,char *in_or_out,int frame,int number_client,int number_of_frames){
    int i;
    for(i=0;i<number_client;i++){
        if(strcmp(sender_client,client_list[i]->client_name)==0){
            if(strcmp(in_or_out,"in")==0){
                if(frame<number_of_frames+1){
                    if(client_list[i]->incoming_buffer->front!=client_list[i]->incoming_buffer->rear) {
                        application_layer *app = (application_layer *) client_list[i]->incoming_buffer->queues[frame -
                                                                                                               1]->stack[0];
                        transport_layer *trp = (transport_layer *) client_list[i]->incoming_buffer->queues[frame -
                                                                                                           1]->stack[1];
                        network_layer *nw = (network_layer *) client_list[i]->incoming_buffer->queues[frame -
                                                                                                      1]->stack[2];
                        physical_layer *phy = (physical_layer *) client_list[i]->incoming_buffer->queues[frame -
                                                                                                         1]->stack[3];
                        printf("Current Frame #%d on the outgoing queue of client %s\n", frame, sender_client);
                        printf("Carried Message: %s\n", app->message_chuck);
                        printf("Layer %d info: Sender ID: %s, Receiver ID: %s\n", 0, app->sender_id, app->receiver_id);
                        printf("Layer %d info: Sender port number: %s, Receiver port number: %s\n", 1,
                               trp->sender_port_number, trp->receiver_port_number);
                        printf("Layer %d info: Sender IP address: %s, Receiver IP address: %s\n", 2,
                               nw->sender_ip_address, nw->receiver_ip_address);
                        printf("Layer %d info: Sender MAC address: %s, Receiver MAC address:%s\n", 3,
                               phy->sender_mac_address, phy->receiver_mac_address);
                        printf("Number of hops so far: %d\n", client_list[i]->hops);
                    }else {
                        printf("No Such Frame\n");
                    }
                } else{
                    printf("No Such Frame\n");
                }
            } else {
                if (frame < number_of_frames) {
                    if(client_list[i]->outgoing_buffer->front!=client_list[i]->outgoing_buffer->rear) {
                        application_layer *app = (application_layer *) client_list[i]->outgoing_buffer->queues[frame -
                                                                                                               1]->stack[0];
                        transport_layer *trp = (transport_layer *) client_list[i]->outgoing_buffer->queues[frame -
                                                                                                           1]->stack[1];
                        network_layer *nw = (network_layer *) client_list[i]->outgoing_buffer->queues[frame -
                                                                                                      1]->stack[2];
                        physical_layer *phy = (physical_layer *) client_list[i]->outgoing_buffer->queues[frame -
                                                                                                         1]->stack[3];
                        printf("Current Frame #%d on the outgoing queue of client %s\n", frame, sender_client);
                        printf("Carried Message: %s\n", app->message_chuck);
                        printf("Layer %d info: Sender ID: %s, Receiver ID: %s\n", 0, app->sender_id, app->receiver_id);
                        printf("Layer %d info: Sender port number: %s, Receiver port number: %s\n", 1,
                               trp->sender_port_number, trp->receiver_port_number);
                        printf("Layer %d info: Sender IP address: %s, Receiver IP address: %s\n", 2,
                               nw->sender_ip_address,
                               nw->receiver_ip_address);
                        printf("Layer %d info: Sender MAC address: %s, Receiver MAC address:%s\n", 3,
                               phy->sender_mac_address, phy->receiver_mac_address);
                        printf("Number of hops so far: %d\n", client_list[i]->hops);
                    }else{
                        printf("No Such Frame\n");
                    }
                }else{
                    printf("No Such Frame\n");
                }
            }
        }
    }

}
void show_q(client **client_list,char *sender_client,char *in_or_out,int number_clients){
    int i;

    for(i=0;i<number_clients;i++) {
        if (strcmp(sender_client, client_list[i]->client_name) == 0) {
            if (strcmp(in_or_out, "in") == 0) {
                if(client_list[i]->incoming_buffer->front!=client_list[i]->incoming_buffer->rear) {
                    printf("Client %s Incoming Queue Status\n", sender_client);
                    printf("Current total number of frames: %d\n", client_list[i]->incoming_buffer->rear + 1);
                } else{
                    printf("Client %s Incoming Queue Status\n", sender_client);
                    printf("Current total number of frames: %d\n", 0);
                }
            } else{
                if((client_list[i]->outgoing_buffer->front!=client_list[i]->outgoing_buffer->rear)) {
                    printf("Client %s Outgoing Queue Status\n", sender_client);
                    printf("Current total number of frames: %d\n", client_list[i]->outgoing_buffer->rear + 1);
                }
                else{
                    printf("Client %s Incoming Queue Status\n", sender_client);
                    printf("Current total number of frames: %d\n", 0);
                }
            }
        }
    }
}
void send(client **client_list,char *current_client,char *route,int number_clients,char *receiver,int number_of_frames,char *real_sender,
          char* full_message) {
    int i,index_of_client_s,index_of_client_r,k;
    if (strcmp(current_client, receiver) == 0) {
        for(i=0;i<number_clients;i++){
            if (strcmp(current_client, client_list[i]->client_name) == 0){
                printf("A message received by client %s from client %s after a total of %d hops.\n",receiver,real_sender,client_list[i]->hops);
                break;
            }
        }
    } else {
        for (i = 0; i < number_clients; i++) {
            if (strcmp(client_list[i]->client_name, current_client) == 0) {
                time_t t = time(NULL);
                struct tm *info;
                time(&t);
                info =localtime(&t);
                strftime(client_list[i]->logInfo->real_time,30,"%Y-%m-%d %H:%M:%S",info);

                index_of_client_s=transmit_finder(i,client_list,number_clients,route,*receiver);
                index_of_client_r=transmit_finder(index_of_client_s,client_list,number_clients,route,*receiver);


                client_list[index_of_client_s]->outgoing_buffer->queues=(stack**)malloc(sizeof(stack*)*number_of_frames);
                client_list[index_of_client_s]->incoming_buffer->queues=(stack**)malloc(sizeof(stack*)*number_of_frames);
                /*If you find the correct receiver we don't have to forward the message just take the message to incoming
                 * queue and transfer to outgoing buffer*/

                if(strcmp(client_list[index_of_client_s]->client_name,receiver)!=0)
                    printf("A message received by client %s, but intended for client %s. Forwarding...\n",
                           client_list[index_of_client_s]->client_name,
                           receiver);

                /*Iterating each frame to change the address of message if it needs a hop to reach the final destination*/
                for(k=0;k<number_of_frames;k++) {

                    stack *current_frame = deque(client_list[i]->outgoing_buffer);
                    void *layer = pop(current_frame);
                    physical_layer *phy_changer = (physical_layer *) layer;
                    if(index_of_client_r!=index_of_client_s) {
                        phy_changer->sender_mac_address = client_list[index_of_client_s]->client_address;
                        phy_changer->receiver_mac_address = client_list[index_of_client_r]->client_address;
                    } else{
                        phy_changer->receiver_mac_address = client_list[index_of_client_r]->client_address;
                    }
                    push(current_frame, phy_changer);
                    enqueue(client_list[index_of_client_s]->incoming_buffer, current_frame);

                    if(strcmp(client_list[index_of_client_s]->client_name,receiver)!=0) {
                        current_frame = deque(client_list[index_of_client_s]->incoming_buffer);
                        enqueue(client_list[index_of_client_s]->outgoing_buffer, current_frame);
                    }

                    if(strcmp(client_list[index_of_client_s]->client_name,receiver)!=0&&route[index_of_client_r]!='-')
                        printf("\tFrame #%d MAC address change: New sender MAC %s, new receiver MAC %s\n",k+1,phy_changer->sender_mac_address
                                ,phy_changer->receiver_mac_address);
                }
                client_list[i]->logInfo->number_of_frames=number_of_frames;
                client_list[i]->logInfo->sender_id_1=real_sender;
                client_list[i]->logInfo->receiver_id=receiver;
                client_list[i]->logInfo->success="Yes";
                if(strcmp(client_list[i]->client_name,real_sender)==0)
                    client_list[i]->logInfo[1].activity="Message Sent";
                else
                    client_list[i]->logInfo[1].activity="Message Forwarded";

                client_list[i]->logInfo->message=full_message;

                client_list[index_of_client_s]->logInfo=(log_info*)malloc(sizeof(log_info)*2);
                client_list[index_of_client_s]->logInfo->number_of_frames=number_of_frames;
                strftime(client_list[index_of_client_s]->logInfo->real_time,30,"%Y-%m-%d %H:%M:%S",info);
                client_list[index_of_client_s]->logInfo->sender_id_1=real_sender;
                client_list[index_of_client_s]->logInfo->receiver_id=receiver;
                client_list[index_of_client_s]->logInfo->success="Yes";
                client_list[index_of_client_s]->logInfo[0].activity="Message Received";
                client_list[index_of_client_s]->logInfo->message=full_message;
                client_list[index_of_client_s]->hops = client_list[i]->hops+1;

                send(client_list, client_list[index_of_client_s]->client_name, route, number_clients,receiver,number_of_frames,real_sender,full_message);
                break;
            }
        }
    }
}
void print_log(client **client_list,char *current_client,int number_clients,char *sender,char *receiver){
    int i,k;
    for(i=0;i<number_clients;i++) {
        if (sender != NULL && receiver != NULL) {
            if (strcmp(current_client, client_list[i]->client_name) == 0) {
                if (strcmp(sender, client_list[i]->client_name) != 0 && strcmp(receiver, client_list[i]->client_name) != 0) {
                    if (client_list[i]->hops != 0) {
                        for (k = 0; k < 2; k++) {
                            printf("Log Entry #%d:\n", k + 1);
                            printf("Timestamp: %s\n", client_list[i]->logInfo->real_time);
                            printf("Message: %s\n", client_list[i]->logInfo->message);
                            printf("Number of frames: %d\n", client_list[i]->logInfo->number_of_frames);
                            printf("Number of hops: %d\n", client_list[i]->hops);
                            printf("Sender ID: %s\n", client_list[i]->logInfo->sender_id_1);
                            printf("Receiver ID: %s\n", client_list[i]->logInfo->receiver_id);
                            printf("Activity: %s\n", client_list[i]->logInfo[k].activity);
                            printf("Success: %s\n", client_list[i]->logInfo->success);
                            if (k != 1)
                                printf("--------------\n");
                        }
                    } else {
                        printf("No log records detected\n");
                        break;
                    }

                } else  {
                    if(client_list[i]->outgoing_buffer->front==-1 && client_list[i]->incoming_buffer->rear==-1){
                        printf("No log records detected\n");
                        break;
                    }
                    printf("Log Entry #1:\n");
                    printf("Timestamp: %s\n", client_list[i]->logInfo->real_time);
                    printf("Message: %s\n", client_list[i]->logInfo->message);
                    printf("Number of frames: %d\n", client_list[i]->logInfo->number_of_frames);
                    printf("Number of hops: %d\n", client_list[i]->hops);
                    printf("Sender ID: %s\n", client_list[i]->logInfo->sender_id_1);
                    printf("Receiver ID: %s\n", client_list[i]->logInfo->receiver_id);
                    if (strcmp(sender, client_list[i]->client_name) == 0)
                        printf("Activity: %s\n", client_list[i]->logInfo[1].activity);
                    else
                        printf("Activity: %s\n", client_list[i]->logInfo[0].activity);
                    printf("Success: %s\n", client_list[i]->logInfo->success);
                }
            }
        } else{
            printf("No log record detected\n");
            break;
        }
    }

}
int transmit_finder(int index,client **client_list,int number_clients, const char *route,char receiver) {
    int i, k;
    int count = index * (number_clients - 1);  /*Count means finding index of current client neighbour*/
    for (i = count; i < count + number_clients - 1; i++) { /*Iterating to find the correct hops to deliver the message to the real receiver*/
        if (route[i] == receiver) {
            for (k = 0; k < number_clients; k++) {
                if (route[i + number_clients * (number_clients - 1)] == *(client_list[k]->client_name)) {
                    return k; /*Returning the correct neighbour index in client list to reach the values*/
                }
            }
        }
    }
    return index;
}
void push(stack *stack1, void *layer){   /*Push layers to stack*/
    stack1->top++;
    stack1->stack[stack1->top]=layer;
}

void* pop(stack *stack1){
    void * layer=stack1->stack[stack1->top];
    stack1->top--;                              /*Popping elements from the stack*/
    return layer;
}

void enqueue(queue *incoming_buffer,stack *frame){      /*Adding frames to queue*/
    incoming_buffer->rear++;
    incoming_buffer->queues[incoming_buffer->rear]=frame;
}

stack* deque(queue *item){
    item->front++;                              /*Deleting frames from to queue*/
    stack *current_frame=item->queues[item->front];
    return current_frame;
}
void free_memory(client **client_list, int number_of_client){
    int i;
    for(i=0;i<number_of_client;i++)
        free(client_list[i]);
    free(client_list);
}
