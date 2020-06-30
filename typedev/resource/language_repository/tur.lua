function tunnelling(givenLenght)
    local distance = 0
    for index = 1,givenLenght do
            if turtle.detect() then
                    for index = 1,5 do
                            turtle.dig()
                    end
            end
            turtle.forward()
            distance = distance + 1
            turtle.digUp()
            turtle.select(1)
            fuelling()
   
            if distance == 10 then
                    print("Placing torch...")
                    turtle.select(15)
                    turnAround()
                    turtle.place()
                    turnAround()
                    distance = 0
            end
    end
end