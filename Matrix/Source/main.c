#include <stdio.h>
#include <string.h>
#include <stdlib.h>

typedef struct matrix{
    int rows;
    int columns;
    int **map;
} matrix;

void Matrix_reader(char *,matrix*);
void Free_Allocation(matrix*,matrix*);
void Matrixes_Boundaries(char *,char*,matrix*,matrix*);
void Treasure_Searcher(matrix*,matrix*,int,int,int,int,int,FILE*);

int main(int argc, char *argv[]) {
    matrix main_matrix,key_matrix;
    FILE *outputfp=fopen(argv[5],"w");
    Matrixes_Boundaries(argv[1],argv[2],&main_matrix,&key_matrix);

    Matrix_reader(argv[3],&main_matrix);
    Matrix_reader(argv[4],&key_matrix);

    Treasure_Searcher(&main_matrix,&key_matrix,0,key_matrix.rows,0,key_matrix.columns,-1,outputfp);
    Free_Allocation(&main_matrix,&key_matrix);
    fclose(outputfp);
    return 1;
}
void Matrix_reader(char *file_name,matrix *main_matrix){
    FILE *fp=fopen(file_name, "r");
    int i,j,count=0;
    char delimiter[]=" \n";   /*To split the string*/
    char *token;

    char *buffer=(char*)malloc(sizeof(char)*(main_matrix->columns*4));         /*Allocating memory for reading and keep the line inside that*/

    main_matrix->map=(int**)malloc(sizeof(int*)*main_matrix->rows);
    /*first allocate rows and then each row is gonna keep one column*/

    for(i=0;i<main_matrix->rows;i++){                                       /*memory allocation*/
        main_matrix->map[i]=(int*)malloc(sizeof(int)*main_matrix->columns);
    }
    while (fgets(buffer,(main_matrix->columns*4),fp )!= NULL){         /*Assigning map to the 2D array value by value*/
        token=strtok(buffer,delimiter);
        for(i=count,j=0;j<main_matrix->columns;j++){
            main_matrix->map[i][j]=(int)strtol(token,NULL,10);
            token=strtok(NULL,delimiter);
        }
        count++;
    }
    fclose(fp);
}

void Matrixes_Boundaries(char  *main_matrix,char *key_matrix,matrix *main,matrix *key){
    char delimiter[]="x"; char *ptr;
    char *splitter = strtok(main_matrix,delimiter);

    main->rows=(strtol(splitter,&ptr,10));       /*splitting the arguments */
    splitter=strtok(NULL,delimiter);            /*assigning the values to the 2D map*/

    main->columns=strtol(splitter,&ptr,10);

    key->columns=strtol(key_matrix,&ptr,10);
    key->rows=key->columns;
}

void Free_Allocation(matrix *main_matrix,matrix *key_matrix) {
    int i;
    for (i = 0; i < main_matrix->rows; i++)
        free(main_matrix->map[i]);
    for (i = 0; i < key_matrix->rows; i++)
        free(key_matrix->map[i]);
    free(main_matrix->map);     free(key_matrix->map);
}

void Treasure_Searcher(matrix *map_matrix,matrix *key_matrix,int row_start,int row_end,int column_start,int column_end,int remainder,FILE *output) {
    int slider = key_matrix->columns;
    int new_remainder,i, j, key_start, key_end,sum = 0;
    /* if incoming remainder is 0, we have found treasure. No more calculation needed.*/
    if(remainder==0){
        //printf("Treasure found");
    } else {
        /* Boundary check*/
        if(remainder==1&& row_start-slider< 0){                                /* Going down in next call*/
            new_remainder=2;
        } else if(remainder==2 && map_matrix->rows < row_end+slider){      /* Going up in next call*/
            new_remainder=1;
        } else if(remainder==3&& column_end+slider > map_matrix->columns){  /* Going left in next call*/
            new_remainder=4;
        } else if(remainder==4 && column_start-slider < 0){                 /* Going right in next call*/
            new_remainder=3;
        } else {
            /* We haven't hit boundaries.*/
            /* Update next sub-matrix center position here.*/
            if(remainder == 1) {
                row_start -= slider;
                row_end -= slider;
            } else if(remainder == 2) {
                row_start += slider;
                row_end += slider;
            }else if(remainder == 3) {
                column_start += slider;
                column_end += slider;
            }else if(remainder == 4) {
                column_start -= slider;
                column_end -= slider;
            }

            /* If incoming remainder is -1, we assume that the call is (initial) first time.*/
            for(i=row_start,key_start=0;i<row_end;i++,key_start++) {            /*calculating the multiplication of submatrix and keymatrix*/
                for (j = column_start, key_end = 0; j < column_end; j++, key_end++) {
                    sum = sum + (map_matrix->map[i][j] * key_matrix->map[key_start][key_end]);
                }
            }
            new_remainder=sum%5;
            fprintf(output,"%d,%d:%d\n",(row_start+row_end)/2,(column_start+column_end)/2,sum);
        }

        /* Recursive call itself.*/
        Treasure_Searcher(map_matrix,key_matrix, row_start, row_end, column_start, column_end, new_remainder, output);
    }
}
