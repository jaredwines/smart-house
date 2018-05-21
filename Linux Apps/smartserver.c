#include <stdio.h> 
#include <string.h>   //strlen 
#include <stdlib.h> 
#include <errno.h> 
#include <unistd.h>   //close 
#include <arpa/inet.h>    //close 
#include <sys/types.h> 
#include <sys/socket.h> 
#include <netinet/in.h> 
#include <sys/time.h> //FD_SET, FD_ISSET, FD_ZERO macros 
   
#define TRUE   1 
#define FALSE  0 
#define PORT 8000 

int opt = TRUE;  
int master_socket , addrlen , new_socket , client_socket[30] , 
    max_clients = 30 , activity, i , valread , sd;  
int max_sd;  
struct sockaddr_in address;  
       
char buffer[100];  //data buffer of 0.1K 
	
FILE *fp;
char path[1024];

//isAcceptableCommand is a white list of acceptable commands the server is allow to excute
int isAcceptableCommand(char command[100])
{
	int isAcceptableCommandBool = FALSE;
	char * arrOfAcceptableCommands[] = 
	{
		"remote tv power",
		"remote tv volup",
		"remote tv voldown",
		"remote soundbar power",
		"remote soundbar volup",
		"remote soundbar voldown",
		"remote soundbar source",
		"remote soundbar mute",
		"remote soundbar left",
		"remote soundbar right",
		"remote soundbar next",
		"remote soundbar play",
		"remote soundbar pre",
		"remote soundbar effect",
		"remote soundbar sound",
		"remote soundbar bluetooth",
		"remote soundbar control"
	};
	int arrSize = sizeof(arrOfAcceptableCommands) / sizeof(arrOfAcceptableCommands[0]);
	
	if((strncmp(command, "gate", 4) == 0) || (strncmp(command, "garage", 4) == 0))
		isAcceptableCommandBool = TRUE;
	
	int i;
	for (i = 0; i < arrSize; i++)
	{
		if(strcmp(command, arrOfAcceptableCommands[i]) == 0)
			isAcceptableCommandBool = TRUE;
	}

	return isAcceptableCommandBool;
}

//excutes the command on the linux server and sends the result to the client
void excuteCommand(char command[100], int charSize)
{
	command[charSize - 3] = '\0';
	
	if(isAcceptableCommand(command) == TRUE)
	{
		fp = popen(command, "r");
		if (fp == NULL) {
			printf("Failed to run command\n" );
			exit(1);
		}
		/* Read the output a line at a time - output it. */
		while (fgets(path, sizeof(path), fp) != NULL) {
		printf("%s", path);
		send(sd , path , strlen(path) , 0 );  
		}

		/* close */
		pclose(fp); 
		memset(path, 0, sizeof(path));
	}
}
   
int main(int argc , char *argv[])  
{       
    //set of socket descriptors 
    fd_set readfds;  
   
    //initialise all client_socket[] to 0 so not checked 
    for (i = 0; i < max_clients; i++)  
    {  
        client_socket[i] = 0;  
    }  
       
    //create a master socket 
    if( (master_socket = socket(AF_INET , SOCK_STREAM , 0)) == 0)  
    {  
        perror("socket failed");  
        exit(EXIT_FAILURE);  
    }  
   
    //set master socket to allow multiple connections , 
    //this is just a good habit, it will work without this 
    if( setsockopt(master_socket, SOL_SOCKET, SO_REUSEADDR, (char *)&opt, 
          sizeof(opt)) < 0 )  
    {  
        perror("setsockopt");  
        exit(EXIT_FAILURE);  
    }  
   
    //type of socket created 
    address.sin_family = AF_INET;  
    address.sin_addr.s_addr = INADDR_ANY;  
    address.sin_port = htons( PORT );  
       
    //bind the socket to localhost port 8888 
    if (bind(master_socket, (struct sockaddr *)&address, sizeof(address))<0)  
    {  
        perror("bind failed");  
        exit(EXIT_FAILURE);  
    }  
       
    //try to specify maximum of 3 pending connections for the master socket 
    if (listen(master_socket, 3) < 0)  
    {  
        perror("listen");  
        exit(EXIT_FAILURE);  
    }  
       
    //accept the incoming connection 
    addrlen = sizeof(address);  
       
    while(TRUE)  
    {  
        //clear the socket set 
        FD_ZERO(&readfds);  
   
        //add master socket to set 
        FD_SET(master_socket, &readfds);  
        max_sd = master_socket;  
           
        //add child sockets to set 
        for ( i = 0 ; i < max_clients ; i++)  
        {  
            //socket descriptor 
            sd = client_socket[i];  
               
            //if valid socket descriptor then add to read list 
            if(sd > 0)  
                FD_SET( sd , &readfds);  
               
            //highest file descriptor number, need it for the select function 
            if(sd > max_sd)  
                max_sd = sd;  
        }  
   
        //wait for an activity on one of the sockets , timeout is NULL , 
        //so wait indefinitely 
        activity = select( max_sd + 1 , &readfds , NULL , NULL , NULL);  
     
        if ((activity < 0) && (errno!=EINTR))  
        {  
            printf("select error");  
        }  
           
        //If something happened on the master socket , 
        //then its an incoming connection 
        if (FD_ISSET(master_socket, &readfds))  
        {  
            if ((new_socket = accept(master_socket, 
                    (struct sockaddr *)&address, (socklen_t*)&addrlen))<0)  
            {  
                perror("accept");  
                exit(EXIT_FAILURE);  
            }  
           
            //add new socket to array of sockets 
            for (i = 0; i < max_clients; i++)  
            {  
                //if position is empty 
                if( client_socket[i] == 0 )  
                {  
                    client_socket[i] = new_socket;                       
                    break;  
                }  
            }  
        }  
           
        //else its some IO operation on some other socket
        for (i = 0; i < max_clients; i++)  
        {  
            sd = client_socket[i];  
               
            if (FD_ISSET( sd , &readfds))  
            {  
                //Check if it was for closing , and also read the 
                //incoming message 
                if ((valread = read( sd , buffer, 1024)) == 0)  
                {  
                    //Somebody disconnected , get his details and print 
                    getpeername(sd , (struct sockaddr*)&address , \
                        (socklen_t*)&addrlen);  
                       
                    //Close the socket and mark as 0 in list for reuse 
                    close( sd );  
                    client_socket[i] = 0;  
                }  
                   
                //Echo back the message that came in 
                else 
                {  	
			excuteCommand(buffer, valread);
                }  
            }  
        }  
    }  
    return 0;  
}  
