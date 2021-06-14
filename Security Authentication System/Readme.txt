Starting KDC --> run CryptoKDC
Starting Mail Server--> run CryptoMail
Starting Web Server--> run CryptoWeb
Starting Database Server--> run CryptoDatabase
Starting Client(Alice)--> run CryptoUser

First you should run KDC in order to allow KDC to prepare certificates and password.Then you should choose a server
and run it.Finally you can run client.
Note:The password which is established by KDC will appear on the terminal for the conveinence. You can also check it
from KDC log file. 

Important!: I tried to check it on dev servers.Unfortunately I got many errors about to threads and out of memory.
Right know I am unable to connect dev servers or execute any command on dev servers.It is better if you try it on
your computer.