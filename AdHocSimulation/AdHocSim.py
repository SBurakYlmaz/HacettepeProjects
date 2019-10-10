import sys
import math

'''
AdHocSim: A program that simulates Ad-Hoc Network.

Program starts just after network establishment and 
find routes using rucursions finally chooses the best 
one as optimal route.

'''

# print in format of SIMULATION TIME: <HH>:<MM>:<SS>
print('SIMULATION TIME: '+ str().zfill(2) + ':' + str().zfill(2) + ':' + str().zfill(2))

# prints these labels at the beginning of output
print('********************************')
print('AD-HOC NETWORK SIMULATOR - BEGIN')
print('********************************')

# prints this message just after evaluation of CRNODE command
print("\tCOMMAND *CRNODE*: New node " +  + " is created")

# prints this message just after evaluation of SEND command
print("\tCOMMAND *SEND*: Data is ready to send from " +  + " to " + )

# prints this message just after evaluation of MOVE command
print("\tCOMMAND *MOVE*: The location of node " +  + " is changed")

# prints this message just after evaluation of RMNODE command
print("\tCOMMAND *RMNODE*: Node " +  + " is removed")

# prints this message just after evaluation of CHBTTRY command
print("\tCOMMAND *CHBTTRY*: Battery level of node " +  + " is changed to " + )

# print this message every time a node is added/removed/relocated or battery of it is modified
print('\tNODES & THEIR NEIGHBORS:', end= " ")

# when there is no route found between node A and B
print('\n\tNO ROUTE FROM ' + + ' TO ' ++ ' FOUND.')

# prints number of possible routes found
print("\t" + + ' ROUTE(S) FOUND:')

# prints every route with its cost in a format given in the assignment paper
print('\tROUTE '++': ' + " -> ".join() + '\t COST: {0:.4f}'.format())

# prints optimal route with its cost in a format given in the assignment paper
print("\tSELECTED ROUTE (ROUTE " +  + "): " + " -> ".join())

# prints packet ID that is just sent
print("\tPACKET " + + " HAS BEEN SENT")

#prints remaining data size in format
print("\tREMAINING DATA SIZE: " + '{0:.6}'.format() + " BYTE")


# prints these labels at the end of output
print('******************************')
print('AD-HOC NETWORK SIMULATOR - END')
print('******************************')
