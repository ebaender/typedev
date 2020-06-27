import qualified OpenAPI.Generate.Types as OAT
import System.Exit

-- | Decodes an OpenAPI File
decodeOpenApi :: String -> IO OAT.OpenApiSpecification
decodeOpenApi fileName = do
  res <- decodeFileEither fileName
  case res of
    Left exc -> die $ "Could not parse OpenAPI specification '" <> fileName <> "': " <> show exc
    Right o -> pure o