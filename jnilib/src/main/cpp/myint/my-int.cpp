#include "include/my-int.h"

int getInt() {
    srand((int)time(0));
    return rand()%100;
}
