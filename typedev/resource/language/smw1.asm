InitializeNameTables:
              lda PPU_STATUS            ;reset flip-flop
              lda Mirror_PPU_CTRL_REG1  ;load mirror of ppu reg $2000
              ora #%00010000            ;set sprites for first 4k and background for second 4k
              and #%11110000            ;clear rest of lower nybble, leave higher alone
              jsr WritePPUReg1
              lda #$24                  ;set vram address to start of name table 1
              jsr WriteNTAddr
              lda #$20                  ;and then set it to name table 0
WriteNTAddr:  sta PPU_ADDRESS
              lda #$00
              sta PPU_ADDRESS
              ldx #$04                  ;clear name table with blank tile #24
              ldy #$c0
              lda #$24