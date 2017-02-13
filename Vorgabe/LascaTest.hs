module Grading where

import Control.Monad
import Test.HUnit

import Util

import LascaBot

missing :: (Eq a) => [a] -> [a] -> [a]
missing xs ys = filter (\x -> not (x `elem` ys)) xs

included :: (Eq a) => [a] -> [a] -> Bool
included xs ys = null (missing xs ys)

same :: (Eq a) => [a] -> [a] -> Bool
same xs ys = (xs `included` ys) && (ys `included` xs)

assertIn :: (Eq a, Show a) => String -> [a] -> [a] -> Assertion
assertIn preface actual expected =
    unless (actual `included` expected) (assertFailure msg)
    where msg = (if null preface then "" else preface ++ "\n") ++ "additional: " ++ show (missing actual expected)

assertSame :: (Eq a, Show a) => String -> [a] -> [a] -> Assertion
assertSame preface actual expected =
    unless (actual `same` expected) (assertFailure (msg ++ miss ++ added))
    where
        msg = (if null preface then "" else preface ++ "\n")
        miss = (if null (missing expected actual) then ""
            else "missing: " ++ show (missing expected actual) ++ "\n")
        added = (if null (missing actual expected) then ""
            else "additional: " ++ show (missing actual expected) ++ "\n")

assertALL :: String -> String -> Assertion
assertALL ss sm = let moves = parseMoves sm in assertSame ("State: " ++ ss) (parseMoves (LascaBot.listMoves ss)) moves

parseMoves :: String -> [String]
parseMoves s = splitOn "," (init (tail s))

isRow, isCol :: Char -> Bool
isRow r = r `elem` ['1'..'7']
isCol c = c `elem` ['a'..'g']

formatMove :: String -> Bool
formatMove (a:b:c:d:e:[])
    | (isCol a) && (isRow b) && c == '-' && (isCol d) && (isRow e) = True
formatMove _ = False

formatList :: String -> Bool
formatList s
    | (head s == '[') && (last s == ']') = foldr (\ sm y -> y && (formatMove sm)) True (splitOn "," (init (tail s)) )
formatList _ = False

assertFormat :: String -> (String -> Bool) -> Assertion
assertFormat actual check =
    unless (check actual) (assertFailure msg)
    where msg = "Wrong format! Looks like: \"" ++ actual ++ "\""

--------------------------------------------------------------------------    

format :: Test
format = TestList [  (TestLabel "MOVE FORMAT WRONG!" (TestCase (assertFormat (LascaBot.getMove "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w") formatMove))),
    (TestLabel "LIST FORMAT WRONG!" (TestCase (assertFormat (LascaBot.listMoves "b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w w") formatList))) ]

game :: Test
game = TestList [
-- Example game
    (TestLabel "Game As Black Hit"       (TestCase (assertALL "b,b,b,b/b,b,b/b,b,b,b/,w,/w,,w,w/w,w,w/w,w,w,w b" "[e5-c3]"))),
    (TestLabel "Game As Black Continue"  (TestCase (assertALL "b,,,/b,wbb,Wb/b,w,,/,w,wWb/wb,,w,Bbbww/,,/,,,w b e1-g3" "[g3-e5]")))
    -- done!
    ]

both :: Test
both = TestList
    [ TestLabel "format" format
    , TestLabel "game" game
    ]

main :: IO Counts
main = runTestTT both
