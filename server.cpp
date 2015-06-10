#include <iostream>
#include <sys/types.h>   // Types used in sys/socket.h and netinet/in.h
#include <netinet/in.h>  // Internet domain address structures and functions
#include <sys/socket.h>  // Structures and functions used for socket API
#include <netdb.h>       // Used for domain/DNS hostname lookup
#include <unistd.h>
#include <stdlib.h>
#include <errno.h>

using namespace std;

main()
{
   int socketHandle;
	#define MAXHOSTNAME 256

   // create socket

   if((socketHandle = socket(AF_INET, SOCK_STREAM, IPPROTO_IP)) < 0)
   {
      close(socketHandle);
      exit(EXIT_FAILURE);
   }

struct sockaddr_in socketAddress;
   char sysHost[MAXHOSTNAME+1];  // Hostname of this computer we are running on
   struct hostNamePtr *hPtr;
   int portNumber = 8080;

   //bzero(&socketInfo, sizeof(sockaddr_in));  // Clear structure memory

   // Get system information

   gethostname(sysHost, MAXHOSTNAME);  // Get the name of this computer we are running on
   /*if((hPtr = gethostbyname(sysHost)) == NULL)
   {
      cerr << "System hostname misconfigured." << endl;
      exit(EXIT_FAILURE);
   }*/

   // Load system information into socket data structures

   socketInfo.sin_family = AF_INET;
   // Use any address available to the system. This is a typical configuration for a server.
   // Note that this is where the socket client and socket server differ.
   // A socket client will specify the server address to connect to.
   socketInfo.sin_addr.s_addr = htonl(INADDR_ANY); // Translate long integer to network byte order.
   socketInfo.sin_port = htons(portNumber);        // Set port number

   // Bind the socket to a local socket address

   if( bind(socketHandle, (struct sockaddr *) &socketInfo, sizeof(struct sockaddr_in)) < 0)
   {
      close(socketHandle);
      //perror("bind");
      cout << "error"  << endl;
	exit(EXIT_FAILURE);
   }

   listen(socketHandle, 1);
cout << "listening on: " << portNumber << endl;
   int socketConnection;
   if( (socketConnection = accept(socketHandle, NULL, NULL)) < 0)
   {
      close(socketHandle);
      exit(EXIT_FAILURE);
   }

   

   // read/write to socket here

   
   
}
