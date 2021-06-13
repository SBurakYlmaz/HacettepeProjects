#include <stdio.h>
#include <stdlib.h>
#include <string.h>


#define MAX(x, y) (((x) > (y)) ? (x) : (y))
#define MIN(x, y) (((x) < (y)) ? (x) : (y))

typedef struct st_character {
    char type[16];
    char name[64];
    int hp;
    int damage;
    int xp;
    int pos_x;
    int pos_y;
} CHARACTER;

typedef struct st_character_list {
    int character_count;
    CHARACTER **characters;
} CHARACTERS;

typedef struct st_command {
    char cmd[32];
    char params[256];
} COMMAND;

typedef struct st_commands {
    int cmd_count;
    COMMAND **commands;
} COMMANDS;

typedef struct st_map {
    int rows;
    int columns;
    CHARACTER ***character_data;
} MAP;

/* --- Function declarations --- */
/* File operations */
CHARACTERS *read_character_file(char *filename);

COMMANDS *read_commands_file(char *filename);

FILE *open_output_file(char *filename);

int get_file_line_count(FILE *fp);

char *get_line(char *buf, int size, FILE *fp);

/* Game functions */
MAP *init_map(int rows, int columns);

void put_characters(MAP *map, CHARACTERS *characters, char *type, char *params);

void attack(FILE *fp, COMMANDS *commands, MAP *map, CHARACTERS *characters, char *type);

void move_characters(FILE *fp, MAP *map, CHARACTERS *characters, char *type, char *params);

/* Print Status operations */
void print_character_status(FILE *fp, CHARACTERS *characters, char *type);

void print_map(FILE *fp, MAP *map);

void exit_program(COMMANDS *commands, CHARACTERS *characters, MAP *map, int ret);


int main(int argc, char *argv[]) {
    int ret = 0;

    int i = 0;
    int cursor = 0;

    MAP *map = NULL;
    FILE *outfp = open_output_file(argv[3]);
    /** Loading character data */

    CHARACTERS *characters = read_character_file(argv[1]);
    COMMANDS *commands = read_commands_file(argv[2]);

    for (i = 0; i < commands->cmd_count; i++) {
        char *cmd = (char *) commands->commands[i]->cmd;
        char *params = (char *) commands->commands[i]->params;

        if (strcmp(cmd, "SHOW") == 0) {

            char *t = strtok(params, " ");
            if (strcmp(t, "HERO") == 0 || strcmp(t, "MONSTER") == 0) {
                print_character_status(outfp, characters, t);
            } else {
                if (!map) {
                    fprintf(stdout, "Map not initialized!");
                    exit_program(commands, characters, map, -2);
                }
                print_map(outfp, map);
            }
        } else if (strcmp(cmd, "LOADMAP") == 0) {

            int row;
            int col;

            sscanf(params, "%d %d", &row, &col);

            map = init_map(row, col);
        } else if (strcmp(cmd, "ATTACK") == 0) {
            if (!map) {
                fprintf(stdout, "Map not initialized!");
                exit_program(commands, characters, map, -2);
            }

            attack(outfp, commands, map, characters, params);
        } else if (strcmp(cmd, "PUT") == 0) {
            if (!map) {
                fprintf(stdout, "Map not initialized!");
                exit_program(commands, characters, map, -2);
            }

            char *t = strtok(params, " ");
            put_characters(map, characters, t, strtok(NULL, ""));
        } else if (strcmp(cmd, "MOVE") == 0) {
            if (!map) {
                fprintf(stdout, "Map not initialized!");
                exit_program(commands, characters, map, -2);
            }
            char *t = strtok(params, " ");
            move_characters(outfp, map, characters, t, strtok(NULL, ""));
        }
    }
    fflush(outfp);
    fclose(outfp);

    exit_program(commands, characters, map, ret);
}

void exit_program(COMMANDS *commands, CHARACTERS *characters, MAP *map, int ret) {
    int i = 0;
    int j = 0;

    if (commands) {
        for (i = 0; i < commands->cmd_count; i++) {
            free(commands->commands[i]);
        }
        free(commands);
    }

    if (characters) {
        for (i = 0; i < characters->character_count; i++) {
            free(characters->characters[i]);
        }
        free(characters);
    }

    if (map) {
        free(map);
    }

    exit(ret);
}

/* --- Function implementations --- */
CHARACTERS *read_character_file(char *filename) {  /*Reading and putting the characters into an array*/
    int cursor = 0;
    FILE *fp;
    if ((fp = fopen(filename, "r")) == NULL) {
        fprintf(stderr, "Could not open character file");
        return NULL;
    }

    int line = get_file_line_count(fp);

    CHARACTERS *characters = (CHARACTERS *) calloc(1, sizeof(CHARACTERS));

    characters->character_count = line;
    characters->characters = calloc((size_t) line, sizeof(CHARACTER *));

    /* init alloc for file line buffer */
    char *buf = (char *) calloc(128, sizeof(char *));
    while (get_line(buf, 128, fp) != NULL) {
        CHARACTER *c = (CHARACTER *) calloc(1, sizeof(CHARACTER));

        strcpy(c->type, strtok(buf, ","));
        strcpy(c->name, strtok(NULL, ","));
        c->hp = atoi(strtok(NULL, ","));
        c->damage = atoi(strtok(NULL, ","));

        characters->characters[cursor] = c;

        cursor++;
    }
    free(buf);
    fclose(fp);
    return characters;
}

COMMANDS *read_commands_file(char *filename) {    /*Reading commands file*/
    int cursor = 0;
    FILE *fp;
    if ((fp = fopen(filename, "r")) == NULL) {
        fprintf(stderr, "Could not open command file");
        return NULL;
    }

    int line = get_file_line_count(fp);

    COMMANDS *commands = (COMMANDS *) calloc(1, sizeof(COMMANDS));

    commands->cmd_count = line;
    commands->commands = calloc((size_t) line, sizeof(COMMAND *));

    /* init alloc for file line buffer */

    char *buf = calloc(512, sizeof(char *));
    while (get_line(buf, 512, fp) != NULL) {

        COMMAND *c = (COMMAND *) calloc(1, sizeof(COMMAND));

        strcpy(c->cmd, strtok(buf, " "));
        char *p = strtok(NULL, "");
        p[strcspn(p, "\n")] = '\0';

        strcpy(c->params, p);

        commands->commands[cursor] = c;
        cursor++;
    }
    free(buf);
    fclose(fp);
    return commands;
}

FILE *open_output_file(char *filename) {   /*Creating new file for writing the output*/

    FILE *fp = fopen(filename, "w");

    if (fp == NULL) {
        fprintf(stderr, "Could not create file!");
    }
    return fp;
}

char *get_line(char *buf, int size, FILE *fp) {
    int c;
    char *p;

    for (p = buf, size--; size > 0; size--) {
        c = fgetc(fp);
        if (c == '\n' || c == EOF) {
            break;
        }
        /* If input file is in Windows CRLF newline format, omit '\r' character */
        if (c != '\r')
            *p++ = (char) c;
    }
    *p = 0;

    if (p == buf)
        return NULL;

    return (p);
}

int get_file_line_count(FILE *fp) {      /*Finding total line in characters file*/
    int line = 0;

    while (!feof(fp)) {
        int ch = fgetc(fp);
        if (ch == '\n' || ch == EOF) {
            line++;
        }
    }
    rewind(fp);
    return line;
}

MAP *init_map(int rows, int columns) {     /*Starting 2D map*/
    int i = 0;
    int j = 0;

    MAP *map = calloc(1, sizeof(MAP));

    map->rows = rows;
    map->columns = columns;
    map->character_data = (CHARACTER ***) calloc((size_t) (rows * columns), sizeof(CHARACTER **));

    for (i = 0; i < (rows); i++) {
        map->character_data[i] = (CHARACTER **) calloc((size_t) columns, sizeof(CHARACTER *));
        for (j = 0; j < columns; j++) {
            map->character_data[i][j] = (CHARACTER *) calloc(1, sizeof(CHARACTER));
        }
    }

    return map;
}

void print_character_status(FILE *fp, CHARACTERS *characters, char *type) {   /*Printing character status*/

    int i = 0;
    fprintf(fp, "%s STATUS\n", type);

    for (i = 0; i < characters->character_count; i++) {
        if (characters->characters[i] != NULL && strcmp(characters->characters[i]->type, type) == 0) {
            fprintf(fp, "%s HP: %d", characters->characters[i]->name, characters->characters[i]->hp);
            if (strcmp(type, "HERO") == 0) {
                fprintf(fp, " XP: %d", characters->characters[i]->xp);
            }
            fprintf(fp, "\n");
        }
    }
    fprintf(fp, "\n");
}

void print_map(FILE *fp, MAP *map) {       /*Printing map*/

    int i = 0;
    int j = 0;
    fprintf(fp, "MAP STATUS\n");

    for (i = 0; i < map->rows; i++) {
        for (j = 0; j < map->columns; j++) {
            CHARACTER *c = map->character_data[i][j];

            if (c->hp <= 0) {
                fprintf(fp, ". ");
            } else {
                fprintf(fp, "%c ", map->character_data[i][j]->name[0]);
            }
        }
        fprintf(fp, "\n");
    }
    fprintf(fp, "\n");

    fflush(fp);
}

void attack(FILE *fp, COMMANDS *commands, MAP *map, CHARACTERS *characters,
            char *type) {   /*implementation of attack commands*/
    int final_control_mons = 0;
    int final_control_hero = 0;
    int i = 0;
    int j = 0;
    int k = 0;
    fprintf(fp, "%s ATTACKED\n", (strncmp(type, "HERO", 4) == 0) ? "HEROES" : "MONSTERS");

    char *cmp = strncmp(type, "HERO", 4) == 0 ? "HERO" : "MONSTER";

    for (i = 0; i < characters->character_count; i++) {
        CHARACTER *c = characters->characters[i];
        if (strcmp(c->type, cmp) == 0 && c->hp > 0) {
            for (j = MAX(c->pos_x - 1, 0); j <= MIN(c->pos_x + 1, map->rows - 1); j++) {
                for (k = MAX(c->pos_y - 1, 0); k <= MIN(c->pos_y + 1, map->columns - 1); k++) {
                    if (!(j == c->pos_x && k == c->pos_y)) {
                        if (map->character_data[j][k]->hp > 0 && strcmp(map->character_data[j][k]->type, cmp) != 0) {
                            int damage = MAX(0, map->character_data[j][k]->hp - c->damage);
                            map->character_data[j][k]->hp = damage;
                            if (damage == 0 && strcmp(c->type, "HERO") == 0) {
                                c->xp++;
                                map->character_data[j][k] = calloc(1, sizeof(CHARACTER));
                            }
                        }
                    }
                }
            }
        }
    }
    for (i = 0; i < characters->character_count; i++) {
        if (characters->characters[i]->hp != 0 && strcmp(characters->characters[i]->type, "MONSTER") == 0)
            final_control_mons++;

        if (characters->characters[i]->hp != 0 && strcmp(characters->characters[i]->type, "HERO") == 0)
            final_control_hero++;
    }
    if (final_control_hero == 0) {
        fprintf(fp, "\nALL HEROES ARE DEAD!");
        exit_program(commands, characters, map, 0);
    }
    if (final_control_mons == 0) {
        fprintf(fp, "\nALL MONSTERS ARE DEAD!");
        exit_program(commands, characters, map, 0);
    }
    fprintf(fp, "\n");
}

void put_characters(MAP *map, CHARACTERS *characters, char *type,
                    char *params) { /*Putting characters into the correct locations*/

    int i = 0;
    char name[64];
    int x = 0;
    int y = 0;
    int pos = 0;
    while (strlen(params) > 0) {
        sscanf(params, "%s %d %d%n", name, &x, &y, &pos);
        for (i = 0; i < characters->character_count; i++) {
            if (strcmp(characters->characters[i]->type, type) == 0) {
                if (strcmp(characters->characters[i]->name, name) == 0) {
                    /* Check character input location and map bounds */
                    if (x >= 0 && x < map->rows && y >= 0 && y < map->columns) {
                        characters->characters[i]->pos_x = x;
                        characters->characters[i]->pos_y = y;
                        map->character_data[x][y] = characters->characters[i];
                    } else {
                        fprintf(stderr, "Cannot put character %s, out of map bounds!\n", name);
                    }
                }
            }
        }
        params += pos;
    }
}

void move_characters(FILE *fp, MAP *map, CHARACTERS *characters, char *type, char *params) {  /*Moving characters the correct map location
*/
    int i = 0;
    char name[64];
    int x = 0;
    int y = 0;
    int pos = 0;

    fprintf(fp, "HEROES MOVED\n");
    while (strlen(params) > 0) {
        sscanf(params, "%s %d %d%n", name, &x, &y, &pos);
        for (i = 0; i < characters->character_count; i++) {
            if (strcmp(characters->characters[i]->type, type) == 0) {
                if (strcmp(characters->characters[i]->name, name) == 0) {

                    if ((x >= 0 && x < map->rows) && (y >= 0 && y < map->columns)) {

                        if (characters->characters[i]->hp > 0) {

                            if (map->character_data[x][y]->hp > 0) {
                                fprintf(fp, "%s can't move. Place is occupied.\n", characters->characters[i]->name);
                            } else {
                                map->character_data[characters->characters[i]->pos_x][characters->characters[i]->pos_y]
                                        = (CHARACTER *) calloc(1, sizeof(CHARACTER));

                                CHARACTER *c = characters->characters[i];
                                c->pos_x = x;
                                c->pos_y = y;
                                map->character_data[x][y] = c;
                            }

                        } else {
                            fprintf(fp, "%s can't move. Dead.\n", characters->characters[i]->name);
                        }
                    } else {
                        fprintf(fp, "%s can't move. There is a wall.\n", characters->characters[i]->name);
                    }
                }
            }
        }
        params += pos;
    }
    fprintf(fp, "\n");
}
