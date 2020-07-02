PROCEDURE DIVISION.
MAIN-PROCEDURE.
    PERFORM INIT-SEED.
    PERFORM GENERATE-RANDOM-NUM.
    PERFORM SORTING-ARRAY.
    GOBACK.
    INIT-SEED SECTION.
    MOVE FUNCTION RANDOM(FUNCTION SECONDS-PAST-MIDNIGHT) TO SEED.
    GENERATE-RANDOM-NUM SECTION.
    PERFORM VARYING W-I FROM 1 BY 1 UNTIL W-I > W-LEN-ARR
     PERFORM W-LEN-ARR TIMES
       COMPUTE W-RAN-NUM = FUNCTION RANDOM *
                     (W-MAX-NUM - W-MIN-NUM + 1) +
                      W-MIN-NUM
       END-PERFORM