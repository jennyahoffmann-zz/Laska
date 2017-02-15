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


--type Move = String -- noch einen besseren typ ausdenken!


data Field = Field Char Int
data Move = Move Field Field


type Row = [String]

type Board = [Row]


data Game = Game Board Color Move |
            Game2 Board Color 



--- logic (TODO)
getMove   s = "g3-f4" -- Eigene Definition einfügen!
listMoves s = "[g3-f4,...]" -- Eigene Definition einfügen!



--listMoves s = let
--    g = parseInput s

 --   in



-- input and parsing

-- spaltet an den leerzeichen
parse :: String -> Game
parse s = parseInput (splitOn " " s)

-- sammelt die einzelteile und erstellt ein Game
parseInput :: [String] -> Game
parseInput (g:c:[]) = Game2  (parseBoard g) (parseColor c) 
parseInput (g:c:m:[]) = Game (parseBoard g) (parseColor c) (parseMove m)


parseColor :: String -> Color
parseColor "w" = White
parseColor "b" = Black

toInt :: Char -> Int
toInt c = ((ord c) - (ord 'a') + 1)


-- erstellt aus einzelnen zeilen ein Board
parseBoard :: String -> Board 
parseBoard b = map parseRow (splitOn "/" b) 


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

    --- ... ---