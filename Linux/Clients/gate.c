#include<string.h>
#include<stdio.h>		//printf
#include<string.h>		//strlen
#include<sys/socket.h>		//socket
#include<arpa/inet.h>		//inet_addr

int main (int argc, char *argv[])
{
  int sock;
  struct sockaddr_in server;
  char server_reply[1000];

  if (argc != 2)
    {
      fprintf (stderr, "Error: ./gate <passCode> \n");
      return 1;
    }

  else if (strlen (argv[1]) != 4)
    {
      fprintf (stderr, "Error: argument must be 4 characters long! \n");
      return 1;
    }

  //Create socket
  sock = socket (AF_INET, SOCK_STREAM, 0);
  if (sock == -1)
    {
      printf ("Could not create socket");
    }
  // puts("Socket created");

  server.sin_addr.s_addr = inet_addr ("192.168.0.55");
  server.sin_family = AF_INET;
  server.sin_port = htons (23);

  //Connect to remote server
  if (connect (sock, (struct sockaddr *) &server, sizeof (server)) < 0)
    {
      perror ("connect failed. Error");
      return 1;
    }

  // puts("Connected\n");

  //Send some data
  send (sock, "$", 1, 0);
  if (send (sock, argv[1], strlen (argv[1]), 0) < 0)
    {
      puts ("Send failed");
      close (sock);
      return 1;
    }

  //Receive a reply from the server
  if (recv (sock, server_reply, 1000, 0) < 0)
    {
      puts ("recv failed");
      close (sock);
      return 1;
    }

  puts (server_reply);
    
  close (sock);
  return 0;
}
