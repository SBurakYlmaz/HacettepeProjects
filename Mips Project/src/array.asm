.data
A: .word 1, 3, 3, 5, 5 
B: .word 2, 2, 3, 4, 5 
.text
main:
la $t1, A  				#we load the address of A into register t1
la $t2, B  				#we load the address of B into register t2
addi $s7,$0,5				#this corresponds the size
add $t7,$0,$0				#this is the loop counter i

loop:
	slt $s5,$t7,$s7			#if i<size (t7<s7) it sets s5=1 otherwise s5 =0
	beq $s5,$zero,finish		#if not then program finish
	sll $t0,$t7,2			#we store in t0 =i*4 for array A
	sll $s3,$t7,2			#we store in t0 =i*4 for array B
	add $t0,$t0,$t1			# address of Array A[i]
	add $t4,$s3,$t2			# address of Array B[i]
	lw $t3,0($t0)			# $t3 = arrayA[i]?
	lw $t5,0($t4)			# $t5 = arrayB[i]?
	slt $t6,$t5,$t3			#checking b[i]<a[i] it sets to t6=1 otherwise 0
	bne $t6,$zero,swap
count:					#if it is then swap the elements
	addi $t7,$t7,1			#i=i+1
	j loop
swap:	sw $t3,0($t4)			#copies data from register t3 to address at t4
	sw $t5,0($t0)			#copies data from register t5 to address at t0
	j count

finish: