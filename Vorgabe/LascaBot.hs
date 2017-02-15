--- module (NICHT AENDERN!)
module LascaBot where
--- imports (NICHT AENDERN!)
import Util
import Data.Char
import System.Environment
--- external signatures (NICHT AENDERN!)
getMove   :: String -> String
listMoves :: String -> String

-- *==========================================* --
-- |    HIER BEGINNT EURE IMPLEMENTIERUNG!    | --
-- *==========================================* --


--- types/structures (TODO)

data Color = White | Black


data Field = Field Char Int

data Move = Move Field Field



type Row = [String]

type Board = [Row]

data Game = Game Board Color Move |
            Game2 Board Color 


data Direction = TopLeft | TopRight | BottomLeft | BottomRight |
                 JumpTopLeft | JumpTopRight | JumpBottomLeft | JumpBottomRight




--- logic (TODO)
getMove   s = "g3-f4" -- Eigene Definition einfügen!

--listMoves s = "[g3-f4,...]" 


listMoves s = 

    let g = parse s in
    
    printMoves (findMoves g)



-- gibt die verfügbaren züge zurück
findMoves :: Game -> [Move]
findMoves (Game2 b c) = iterateOverBoardJump b b 0 c

{-
-- falls die normalMoves eine leere liste sind, gib jumpMoves zurück
-- sonst normalMoves
checkWichMoves :: Game -> [Move] -> [Move]
checkWichMoves g [] = findJumpMoves g
checkWichMoves g m  = m
-- VORSICHT: DAS IST FALSCHRUM


-}

isEmpty :: String -> Bool
isEmpty "" = True
isEmpty s = False


getNullMove :: Move
getNullMove = Move (Field 'z' 0) (Field 'z' 0)


isFromPlayer :: String -> Color -> Bool

isFromPlayer ('b':_) Black = True
isFromPlayer ('B':_) Black = True
isFromPlayer ('w':_) White = True
isFromPlayer ('W':_) White = True
isFromPlayer _ _           = False


invertColor :: Color -> Color
invertColor Black = White
invertColor White = Black


checkValidCoords :: Int -> Int -> Bool
checkValidCoords x y = 
    if x >= 0 && x <= 6 && y >= 0 && y <= 6
        then True
        else False


--data Direction = TopLeft | TopRight | BottomLeft | BottomRight |
--                 JumpTopLeft | JumpTopRight | JumpBottomLeft | JumpBottomRight

calcCoords :: Int -> Int -> Direction -> (Int, Int)
calcCoords x y d = 

    if (mod y 2) == 0
    then

        if d == TopLeft then
            ((x - 1), (y - 1))

        else if d == TopRight then
            ((x), (y - 1))

        else if d == JumpTopLeft then
            ((x - 1), (y - 2))

        else if d == JumpTopRight then
            ((x + 1), (y - 2))

        else 
            (0, 0)

    else

        if d == TopLeft then
            ((x), (y - 1))

        else if d == TopRight then
            ((x + 1), (y - 1))

        else if d == JumpTopLeft then
            ((x - 1), (y - 2))

        else if d == JumpTopRight then
            ((x + 1), (y - 2))

        else 
            (0, 0)

calcCoordsBetween :: Int -> Int -> Direction -> (Int, Int)
calcCoordsBetween x y d =

    if d == JumpTopLeft then
        calcCoords x y TopLeft

    else if d == JumpTopRight then
        calcCoords x y TopRight

    else 
        (0, 0)


{-

-- berücksichtige, dass bei unterschiedlichen y die x werte anders werden
calcCoords :: Int -> Int -> Direction -> (Int, Int)
calcCoords x y d = 

    if d == TopLeft 
        then ((x-1),(y-1))
    else if d == TopRight
        then ((x+1),(y-1))
    else 
        (0, 0)


calcCoordsBetween :: Int -> Int -> Direction -> (Int, Int)
calcCoordsBetween x y d =


    if(d == JumpTopLeft) then 
        ((x-1),(y-1)) -- TopLeft Coordinates

    else

        (0, 0)

-}

getFieldAt :: Board -> Int -> Int -> String
getFieldAt (head:_)    x 0 = getFieldAtInRow head x
getFieldAt (head:tail) x y = getFieldAt tail x (y-1)

getFieldAtInRow :: Row -> Int -> String
getFieldAtInRow (head:_)    0 = head
getFieldAtInRow (head:tail) x = getFieldAtInRow tail (x-1)


getFieldFromCoord :: Int -> Int -> Field
getFieldFromCoord x y = Field (getCharFromXCoord x y) (7-y)

getCharFromXCoord :: Int -> Int -> Char
getCharFromXCoord x y = 

    if (mod y 2) == 0
    then

        if x == 0 then
            'a'
        else if x == 1 then
            'c'
        else if x == 2 then
            'e'
        else 
            'g'

    else

        if x == 0 then
            'b'
        else if x == 1 then
            'd'
        else 
            'f'


getJumpMoveForField :: Board -> Int -> Int -> Direction -> Color -> Move
getJumpMoveForField b x y d c =

    let

        new = calcCoords x y d

        nx = fst new
        ny = snd new

        bet = calcCoordsBetween x y d

        bx = fst bet
        by = snd bet

    in
        -- sind die koordinaten valide?
        if (checkValidCoords nx ny) == True
            then
                -- ist das sprungziel leer
                if (isEmpty (getFieldAt b nx ny)) == True
                    then
                        -- ist auf dem feld dazwischen ein spieler vom gegner?
                        if isFromPlayer (getFieldAt b bx by) (invertColor c) == True
                            then
                                -- gebe das move zurück
                                Move (getFieldFromCoord x y) (getFieldFromCoord nx ny)

                            else
                                getNullMove
                    else
                        getNullMove
            else
                getNullMove


getJumpMovesForField :: Board -> String -> Int -> Int -> Color -> [Move]
getJumpMovesForField b s x y c = 

    if (isFromPlayer s c) == True 
        then

            (getJumpMoveForField b x y JumpTopLeft c):([]) --(getJumpMoveForField b x y JumpTopRight c)

        else 
            []


iterateOverRowJump :: Row -> Board -> Int -> Int -> Color -> [Move]
iterateOverRowJump ([])        b x y c = getNullMove:([])
iterateOverRowJump (last:[])   b x y c = getJumpMovesForField b last x y c
iterateOverRowJump (head:tail) b x y c = (getJumpMovesForField b head x y c) ++ (iterateOverRowJump tail b (x + 1) y c)

iterateOverBoardJump :: Board -> Board -> Int -> Color -> [Move]
iterateOverBoardJump ([])        b y c = getNullMove:([])
iterateOverBoardJump (last:[])   b y c = (iterateOverRowJump last b 0 y c)
iterateOverBoardJump (head:tail) b y c = (iterateOverRowJump head b 0 y c) ++ (iterateOverBoardJump tail b (y+1) c)



{-

printField :: String -> Int -> Int -> String
printField field x y = field ++ " (x:" ++ ((toChar x):[]) ++ " | y:" ++ ((toChar y):[]) ++ ")"

iterateOverRow :: Row -> Int -> Int -> String
iterateOverRow ([])        x y = ""
iterateOverRow (last:[])   x y = printField last x y
iterateOverRow (head:tail) x y = (printField head x y) ++ ", " ++ (iterateOverRow tail (x+1) y)


iterateOverBoard :: Board -> Int -> String
iterateOverBoard ([])        y = ""
iterateOverBoard (last:[])   y = (iterateOverRow last 0 y)
iterateOverBoard (head:tail) y = (iterateOverRow head 0 y) ++ "/" ++ (iterateOverBoard tail (y+1))

-}


-- input and parsing

-- spaltet an den leerzeichen
parse :: String -> Game
parse s = parseInput (splitOn " " s)

-- sammelt die einzelteile und erstellt ein Game
parseInput :: [String] -> Game
parseInput (g:c:[])   = 
    let 
        clr = parseColor c
    in
        Game2  (parseBoard g clr) clr

parseInput (g:c:m:[]) = 
    let 
        clr = parseColor c
    in
        Game (parseBoard g clr) clr (parseMove m)


parseColor :: String -> Color
parseColor "w" = White
parseColor "b" = Black

toInt :: Char -> Int
toInt c = ((ord c) - (ord 'a') + 1)


toChar :: Int -> Char
toChar i = chr (i + ord '0')

-- erstellt aus einzelnen zeilen ein Board
parseBoard :: String -> Color -> Board 
parseBoard b White = map parseRow (splitOn "/" b)
parseBoard b Black = reverse (map parseRow (splitOn "/" b)) 

-- erstellt aus einzelnen feldern eine zeile
parseRow :: String -> Row 
parseRow r = splitOn "," r


-- erstellt eine Liste von 2 Fields 
-- gibt sie als Move zurück
parseMove :: String -> Move
parseMove s = parseFields (map parseField (splitOn "-" s))

-- nimmt 2 Fields und packt sie in ein Move
parseFields :: [Field] -> Move
parseFields (f:g:[]) = Move f g 

-- aus einem string ein field
parseField :: String -> Field
parseField (s:t:[]) = Field s (toInt t)


--- output (TODO)

colorToString :: Color -> String
colorToString White = "w"
colorToString Black = "b"

instance Show Color where
    show = colorToString
    
instance Eq Color where
    (==) White White = True
    (==) Black Black = True
    (==) _ _ = False    


instance Eq Direction where
    (==) TopLeft TopLeft = True
    (==) TopRight TopRight = True
    (==) BottomLeft BottomLeft = True
    (==) BottomRight BottomRight = True
    (==) JumpTopLeft JumpTopLeft = True
    (==) JumpTopRight JumpTopRight = True
    (==) JumpBottomLeft JumpBottomLeft = True
    (==) JumpBottomRight JumpBottomRight = True
    (==) _ _ = False    



printMove :: Move -> String
printMove (Move (Field c1 i1) (Field c2 i2)) =
    c1:(toChar i1):'-':c2:(toChar i2):[]

printMoves2 :: [Move] -> String
printMoves2 ([]) = ""
printMoves2 ((Move (Field 'z' 0) (Field 'z' 0)):tail) = printMoves2 tail
printMoves2 (last:[])   = printMove last
printMoves2 (head:tail) = (printMove head) ++ "," ++ (printMoves2 tail)

printMoves :: [Move] -> String
printMoves m = "[" ++ (printMoves2 m) ++ "]"









