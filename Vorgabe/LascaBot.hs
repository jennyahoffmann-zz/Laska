--- module (NICHT AENDERN!)
module LascaBot where
--- imports (NICHT AENDERN!)
import Util
import Data.Char
import System.Environment
--- external signatures (NICHT AENDERN!)
getMove   :: String -> String
-- listMoves :: String -> String

-- *==========================================* --
-- |    HIER BEGINNT EURE IMPLEMENTIERUNG!    | --
-- *==========================================* --

--- types/structures (TODO)

data Color = White | Black

currentState = "b,b,b,b/b,b,b/b,b,,b/,b,/w,w,w,w/w,w,w/w,w,w,w w" :: String
-- showMoves = listMoves currentState :: String
    --- ... ---

listMoves :: String
listMoves = parseInput (splitOn " " currentState)

parseInput :: [String] -> String
parseInput (board:color:[]) = (findMoves (splitOn "/" currentState) color)

findMoves :: [String] -> String -> String
-- findMoves board "w" = head (splitOn "," (head board))
findMoves board "w" = findAllJumps board "w" 1 1
findMoves board "b" = "blacks turn"

findAllJumps :: [String] -> String -> Integer -> Integer -> String
findAllJumps board "w" 7 7 = if (isOwnedByWhite(getField board 7 7)) 
							then addMoveToList
							else "false"
findAllJumps board "w" row column = if (isOwnedByWhite(getField board row column)) 
							then findAllJumpsPerRow board "w" 6
							else "false"

findAllJumpsPerRow board "w" 7 = "row"
findAllJumpsPerRow board "w" i = findAllJumpsPerRow board "w" (i+1)

addMoveToList = "move"

isOwnedByWhite :: String -> Bool
isOwnedByWhite s
			| (((head s) == 'w') || ((head s) == 'W')) = True
isOwnedByWhite _ = False

getField :: [String] -> Integer -> Integer -> String
getField (a:b:c:d:e:f:g:[]) 1 column = getElementOdd (splitOn "," g) column
getField (a:b:c:d:e:f:g:[]) 2 column = getElementEven (splitOn "," f) column
getField (a:b:c:d:e:f:g:[]) 3 column = getElementOdd (splitOn "," e) column
getField (a:b:c:d:e:f:g:[]) 4 column = getElementEven (splitOn "," d) column
getField (a:b:c:d:e:f:g:[]) 5 column = getElementOdd (splitOn "," c) column
getField (a:b:c:d:e:f:g:[]) 6 column = getElementEven (splitOn "," b) column
getField (a:b:c:d:e:f:g:[]) 7 column = getElementOdd (splitOn "," a) column

getElementEven :: [String] -> Integer -> String
getElementEven (b:d:f:[]) 2 = b
getElementEven (b:d:f:[]) 4 = d
getElementEven (b:d:f:[]) 6 = f
getElementEven _ _ = "x"

getElementOdd :: [String] -> Integer -> String
getElementOdd (a:c:e:g:[]) 1 = a
getElementOdd (a:c:e:g:[]) 3 = c
getElementOdd (a:c:e:g:[]) 5 = e
getElementOdd (a:c:e:g:[]) 7 = g
getElementOdd _ _ = "x"

toString 1 = "1"
toString 2 = "2"
toString 3 = "3"
toString 4 = "4"
toString 5 = "5"
toString 6 = "6"
toString 7 = "7"




--getRow :: [String] -> Integer -> String
--getRow board 7 = head board
--getRow (_:rest) (odd r) = getRow rest (succ r)
--getRow (_:rest) (even r) = getRow rest (succ r)

--getElement row 1 = head row
--getElement (_:rest) e = getElement rest (pred e)

--- logic (TODO)
getMove   s = "g3-f4" -- Eigene Definition einfügen!
-- listMoves s = "[g3-f4,...]" -- Eigene Definition einfügen!
--listMoves :: String -> [String]
--listMoves s = parseInput s

--parseInput :: String -> [String]
--parseInput s = splitOn "/" s
    --- ... ---

--- input (TODO)

-- parse :: String -> Color
-- parse :: String -> [String]
-- parse s = parseInput (splitOn " " s)

-- parseInput :: [String] -> Color
-- parseInput (board:color:[])   = (parseColor color)
-- parseInput (board:color:move:[]) = ... (parseColor color) ...

parseColor :: String -> Color
parseColor "w" = White
parseColor "b" = Black

toInt :: Char -> Int
toInt c = ((ord c) - (ord 'a') + 1)

    --- ... ---

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

    --- ... ---