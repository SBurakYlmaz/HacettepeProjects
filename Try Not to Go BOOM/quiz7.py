'''
This starter code provides you the expected message formats 
'''

#Separator for each line of calculation (should be inserted at the top!):
print("------------")
    
#IOError message:
print("IOError: cannot open", filename_that_caused_trouble)

#IndexError message for missing file names:
print("IndexError: number of input files less than expected.")

# ValueError  message:
print("ValueError: only numeric input is accepted.")
print("Given input:", given_input_that_caused_trouble)

# IndexError message for missing operands:
print("IndexError: number of operands less than expected.")
print("Given input:", given_input_that_caused_trouble)

# ZeroDivisionError message:
print("ZeroDivisionError: You can't divide by 0.")
print("Given input:", given_input_that_caused_trouble)

# Comparing results messages:
print("My results:\t\t", your_results)
print("Results to compare:\t", given_comparison_data)

# AssertionError message for result mismatch:
print("Assertion Error: results don't match.")

# Message for result match:
print("Goool!!!")

# Error message for any other exception:
print("kaBOOM: run for your life!")

# Message for terminating the program:
print("\n~ Game Over ~")