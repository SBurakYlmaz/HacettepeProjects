all: findtreasure
findtreasure: findtreasure.o
	 gcc -o findtreasure findtreasure.o

findtreasure.o: main.c
	 gcc -o findtreasure.o -c main.c

clean:
	 rm findtreasure.o findtreasure