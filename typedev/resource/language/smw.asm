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
InitNTLoop:   sta PPU_DATA              ;count out exactly 768 tiles
              dey
              bne InitNTLoop
              dex
              bne InitNTLoop
              ldy #64                   ;now to clear the attribute table (with zero this time)
              txa
              sta VRAM_Buffer1_Offset   ;init vram buffer 1 offset
              sta VRAM_Buffer1          ;init vram buffer 1
InitATLoop:   sta PPU_DATA
              dey
              bne InitATLoop
              sta HorizontalScroll      ;reset scroll variables
              sta VerticalScroll
              jmp InitScroll            ;initialize scroll registers to zero