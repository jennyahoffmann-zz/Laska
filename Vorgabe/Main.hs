{-# LANGUAGE DoAndIfThenElse #-}
module Main 
where

import LascaBot
import System.Environment

main :: IO ()
main = do 
	args <- getArgs
	let oneString = foldr (\x y -> if y == "" then x else x ++ " " ++ y) "" args in
		putStrLn ( getMove oneString )