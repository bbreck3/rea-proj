

/* The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>

char c[1000];

void error(const char *msg)
{
    perror(msg);
    exit(1);
}
char* getData() {

	
   	FILE *fptr;
   	if ((fptr=fopen("pub.key","r"))==NULL){
       		printf("Error! opening file");
       		exit(1);         /* Program exits if file pointer returns NULL. */
   	}
   	fscanf(fptr,"%[^\n]",c);
	int size = sizeof(c)/sizeof(c[0]);
	char *data = malloc(size *sizeof(char));
   	printf("Data from file:\n%s",c);
   	fclose(fptr);
return c;

   /* char char1= 'm';
    char char2= 'y';
    char *str = malloc(3 * sizeof(char));
    if(str == NULL) return NULL;
    str[0] = char1;
    str[1] = char2;
    str[2] = '\0';
    printf("str %s: ",str);
    return str;*/
}
int main(int argc, char *argv[])
{
     printf("Debug");
     int sockfd, newsockfd, portno;
     socklen_t clilen;
     char buffer[256];
     struct sockaddr_in serv_addr, cli_addr;
     int n;
     if (argc < 2) {
         fprintf(stderr,"ERROR, no port provided\n");
         exit(1);
     }
     // create a socket
     // socket(int domain, int type, int protocol)
     sockfd =  socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd < 0) 
        error("ERROR opening socket");

     // clear address structure
     bzero((char *) &serv_addr, sizeof(serv_addr));

     portno = atoi(argv[1]);
   
     /* setup the host_addr structure for use in bind call */
     // server byte order
     serv_addr.sin_family = AF_INET;  

     // automatically be filled with current host's IP address
     serv_addr.sin_addr.s_addr = inet_addr("10.107.237.17");
	// EC:IP//"10.107.237.17");//INADDR_ANY;  
	//DORM:IP: 10.102.119.130
     // convert short integer value for port must be converted into network byte order
     serv_addr.sin_port = htons(portno);

     // bind(int fd, struct sockaddr *local_addr, socklen_t addr_length)
     // bind() passes file descriptor, the address structure, 
     // and the length of the address structure
     // This bind() call will bind  the socket to the current IP address on port, portno
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");

     // This listen() call tells the socket to listen to the incoming connections.
     // The listen() function places all incoming connection into a backlog queue
     // until accept() call accepts the connection.
     // Here, we set the maximum size for the backlog queue to 5.
     
     listen(sockfd,5);
     printf("Listening on port: %i",portno); 
     
	
     // The accept() call actually accepts an incoming connection
     clilen = sizeof(cli_addr);

     // This accept() function will write the connecting client's address info 
     // into the the address structure and the size of that structure is clilen.
     // The accept() returns a new socket file descriptor for the accepted connection.
     // So, the original socket file descriptor can continue to be used 
     // for accepting new connections while the new socker file descriptor is used for
     // communicating with the connected client.
     newsockfd = accept(sockfd, 
                 (struct sockaddr *) &cli_addr, &clilen);
     if (newsockfd < 0) 
          error("ERROR on accept");
	
	

     printf("\nserver: got connection from %c port %d\n",
            inet_ntoa(cli_addr.sin_addr), ntohs(cli_addr.sin_port));
	//fflush()
	/**
			Read the key in from the file.
	*/

	
	 


	
     // This send() function sends the 13 bytes of the string to the new socket
     //send(newsockfd, c, 13, 0);
     send(newsockfd, c, 1000, 0);

     bzero(buffer,256);

     n = read(newsockfd,buffer,255);
     if (n < 0) error("ERROR reading from socket");
     printf("Here is the message: %s\n",buffer);
	

     close(newsockfd);
     close(sockfd);
     return 0; 
}
