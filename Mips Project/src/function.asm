.data
number_a: .word 3
number_b: .word 3

result:$s3
.text
main:
	lw $s0,number_a
	lw $s1,number_b
	beq $s0,$s1,branch	#checking if the numbers are equal if it is continue from branch if not continue from next instruction
	jal compare		#jumo to compare function
	add $s3,$v0,$zero	#putting the returning value into result (register s3)
	j final			#finishing the program

compare:
	addi $sp,$sp,-4		#allocate memory for stack pointer
	sw   $ra, 0($sp)	#storing the instruction after jal award
	slt $t0,$s1,$s0 	#checks variable B > variable A
	bne $t0,$0,else		#check if B > A if it it is  kontrol et
	jal punish		#continues from punish
	lw   $ra, 0($sp)	#load the address holds by stack pointer into ra register
	addi $sp,$sp,4		#reallocate the memory for stack pointer
	jr  $ra			#jumps to address hold by ra register
else:
	jal award		#continues from award
	lw $ra,0($sp)		#load the address holds by stack pointer into ra register
	addi $sp,$sp,4		#reallocate the memory for stack pointer
	jr $ra			#jumps to address hold by ra register

branch:
	add $s3,$s0,$s1		#adding two equal numbers into s3 that means A+B=result
	or $v0,$zero,$s3	#copying the result into return register v0
	j final			#finishing the program
	
award:  addi $sp,$sp,-4		#allocate memory for stack pointer
	sw   $ra, 0($sp)	#storing the instruction after jal award
	add  $t1,$s0,$s0	#a+a=2a
	add  $t2,$s1,$s1	#b+b=2b
	add  $v0,$t1,$t2	#storing the 2a+2b in return register v0
	lw   $ra, 0($sp)	#load the address holds by stack pointer into ra register
	addi $sp,$sp,4		#reallocate the memory for stack pointer
	jr   $ra		#jumps to address hold by ra register

punish:	
	addi $sp,$sp,-4		#allocate memory for stack pointer
	sw   $ra, 0($sp)    	#storing the instruction after jal punish
	add  $t1,$s0,$s0	#a+a=2a
	add  $t2,$s1,$s1	#b+b=2b
	sub  $v0,$t1,$t2	#storing the 2a-2b in return register v0
	lw   $ra, 0($sp)	#load the address holds by stack pointer into ra register
	addi $sp,$sp,4		#reallocate the memory for stack pointer
	jr   $ra		#jumps to address hold by ra register
final:  	