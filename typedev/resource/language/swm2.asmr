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