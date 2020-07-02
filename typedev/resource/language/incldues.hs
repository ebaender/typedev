import Text.Pandoc.JSON
import Text.Pandoc.Walk

slice :: Int -> Int -> [a] -> [a]
slice from to xs = take (to - from + 1) (drop from xs)

doSlice :: Block -> IO Block
doSlice cb@(CodeBlock (id, classes, namevals) contents) = do
  res <- return $ do
    upper <- readMaybe =<< lookup "upper" namevals
    lower <- readMaybe =<< lookup "lower" namevals
    file  <- lookup "slice" namevals
    return (upper, lower, file)