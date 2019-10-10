# BBM103 Introduction to Programming Lab 1 - Fall 2017 - Programming Assignment 3 Starter Code
import sys
'''
This program will save the human race if done properly, 
so please do your best to get 100 from this assignment. 
You can do it! :)
'''

# Opening the input files

hurap_file = open(sys.argv[1], "r")

schuckscii_file = open(sys.argv[2], "r")

virus_codes_file = open(sys.argv[3], "r")

# Mission 00: Decrypting the HuRAP

print("""*********************
     Mission 00 
*********************""", end="\n\n")

print("""--- hex of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the hexadecimal representation
# of HuRAP goes here

print("""\n--- encrypted code ----
-----------------------""", end="\n\n")

# Your code which calculates and prints the encrypted character
# representation of HuRAP goes here

print("""\n--- decrypted code ---
----------------------""", end="\n\n")

# Your code which decrypts and prints the
# HuRAP goes here

# Mission 01: Coding the virus

print("""\n*********************
     Mission 01 
*********************""", end="\n\n")

# Your code which transforms the original HuRAP and prints
# the virus-infected HuRAP goes here
	  
	  
# Mission 10: Encrypting the virus-infected HuRAP

print("""\n*********************
     Mission 10 
*********************""", end="\n\n")


print("""--- encrypted code ---
----------------------""", end="\n\n")

# Your code which encrypts and prints the encrypted character
# representation of the virus-infected HuRAP goes here

print("""\n--- hex of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the hexadecimal representation
# of virus-infected and encrypted HuRAP goes here

print("""\n--- bin of encrypted code ---
-----------------------------""", end="\n\n")

# Your code which calculates and prints out the binary representation
# of virus-infected and encrypted HuRAP goes here

# Closing the input files

hurap_file.close()
schuckscii_file.close()
virus_codes_file.close()